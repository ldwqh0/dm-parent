/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dm.security.oauth2.client.web.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 另外一个 {@link ServerOAuth2AuthorizationRequestResolver}的实现<br>
 * <p>
 * 大部分的实现都源自
 * {@link org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver}<br>
 * <p>
 * 但当client隐藏在代理服务器后端时，默认实现在解析 {baseUrl}/login/oauth2/code/{registrationId}
 * 这样的配置时baseUrl会被解析为服务器自身的ip和port.从而导致redirect_uri不正确<br>
 * 虽然我们可以通过固定baseUrl ，比如写成
 * http://xxx.xxx.xxx/login/oauth2/code/{registrationId}这样的形式<br>
 * 但如果某个客户端在不同的域名被访问时，可能会出现只会跳转到固定地址，而不会跟随实际访问地址变化<br>
 * <p>
 * 示例：现有服务部署于nginx后端。<br>
 * 我们可以通过两个地址访问nginx,分别是http://aaa.com和http://bbb.com<br>
 * 比如通过访问地址为http://aaa.com/test访问页面时,在oauth跳转时,我们期望oauth认证地址为
 * http://oauth/authorize?xxx&redirect_uri=http://aaa.com/login/oauth2/code/{registrationId}<br>
 * 而在通过http://bbb.com/test访问页面时我们期望oauth认证地址为
 * http://oauth/authorize?xxx&redirect_uri=http://bbb.com/login/oauth2/code/{registrationId}<br>
 * <b>注意链接的redirect_uri部分</b>。
 *
 * <p>
 * 在这个实现中，增加了一个{xForwardedHost}参数, 我们在配置redirect-uri时可以写成
 * {xForwardedProto}://{xForwardedHost}/login/oauth2/code/{registrationId}的形式
 * </p>
 * <p>
 * 前端代理服务器需要通过一个x-Forwarded-Proto和x-Forwarded-Host请求头将实际请求中的host传递给服务器
 * </p>
 * <p>
 * 另外一个需求
 * </p>
 *
 * @author ldwqh0@outlook.com
 * @since 5.1
 */
public class DmServerOAuth2AuthorizationRequestResolver
    implements ServerOAuth2AuthorizationRequestResolver {

    /**
     * The name of the path variable that contains the
     * {@link ClientRegistration#getRegistrationId()}
     */
    public static final String DEFAULT_REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";

    /**
     * The default pattern used to resolve the
     * {@link ClientRegistration#getRegistrationId()}
     */
    public static final String DEFAULT_AUTHORIZATION_REQUEST_PATTERN = "/oauth2/authorization/{"
        + DEFAULT_REGISTRATION_ID_URI_VARIABLE_NAME + "}";

    private static final char PATH_DELIMITER = '/';

    private final ServerWebExchangeMatcher authorizationRequestMatcher;

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    private final StringKeyGenerator stateGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder());

    private final StringKeyGenerator secureKeyGenerator = new Base64StringKeyGenerator(
        Base64.getUrlEncoder().withoutPadding(), 96);
    private String defaultPrefix = "/";

    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
    }

    /**
     * Creates a new instance
     *
     * @param clientRegistrationRepository the repository to resolve the
     *                                     {@link ClientRegistration}
     */
    public DmServerOAuth2AuthorizationRequestResolver(
        ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this(clientRegistrationRepository, new PathPatternParserServerWebExchangeMatcher(
            DEFAULT_AUTHORIZATION_REQUEST_PATTERN));
    }

    /**
     * Creates a new instance
     *
     * @param clientRegistrationRepository the repository to resolve the
     *                                     {@link ClientRegistration}
     * @param authorizationRequestMatcher  the matcher that determines if the
     *                                     request is a match and extracts the
     *                                     {@link #DEFAULT_REGISTRATION_ID_URI_VARIABLE_NAME}
     *                                     from the path variables.
     */
    public DmServerOAuth2AuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                      ServerWebExchangeMatcher authorizationRequestMatcher) {
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        Assert.notNull(authorizationRequestMatcher, "authorizationRequestMatcher cannot be null");
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizationRequestMatcher = authorizationRequestMatcher;
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        return this.authorizationRequestMatcher.matches(exchange)
            .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
            .map(ServerWebExchangeMatcher.MatchResult::getVariables)
            .map(variables -> variables.get(DEFAULT_REGISTRATION_ID_URI_VARIABLE_NAME))
            .cast(String.class)
            .flatMap(clientRegistrationId -> resolve(exchange, clientRegistrationId));
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange,
                                                    String clientRegistrationId) {
        return this.findByRegistrationId(exchange, clientRegistrationId)
            .map(clientRegistration -> authorizationRequest(exchange, clientRegistration));
    }

    private Mono<ClientRegistration> findByRegistrationId(ServerWebExchange exchange, String clientRegistration) {
        return this.clientRegistrationRepository.findByRegistrationId(clientRegistration)
            .switchIfEmpty(Mono.error(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid client registration id")));
    }

    private OAuth2AuthorizationRequest authorizationRequest(ServerWebExchange exchange,
                                                            ClientRegistration clientRegistration) {
        String redirectUriStr = expandRedirectUri(exchange.getRequest(), clientRegistration);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put(OAuth2ParameterNames.REGISTRATION_ID, clientRegistration.getRegistrationId());

        OAuth2AuthorizationRequest.Builder builder;
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
            builder = OAuth2AuthorizationRequest.authorizationCode();
            Map<String, Object> additionalParameters = new HashMap<>();
            if (!CollectionUtils.isEmpty(clientRegistration.getScopes()) &&
                clientRegistration.getScopes().contains(OidcScopes.OPENID)) {
                // Section 3.1.2.1 Authentication Request -
                // https://openid.net/specs/openid-connect-core-1_0.html#AuthRequest
                // scope
                // REQUIRED. OpenID Connect requests MUST contain the "openid" scope value.
                addNonceParameters(attributes, additionalParameters);
            }
            if (ClientAuthenticationMethod.NONE.equals(clientRegistration.getClientAuthenticationMethod())) {
                addPkceParameters(attributes, additionalParameters);
            }
            builder.additionalParameters(additionalParameters);
        } else if (AuthorizationGrantType.IMPLICIT.equals(clientRegistration.getAuthorizationGrantType())) {
            builder = OAuth2AuthorizationRequest.implicit();
        } else {
            throw new IllegalArgumentException(
                "Invalid Authorization Grant Type (" + clientRegistration.getAuthorizationGrantType().getValue()
                    + ") for Client Registration with Id: " + clientRegistration.getRegistrationId());
        }
        return builder
            .clientId(clientRegistration.getClientId())
            .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
            .redirectUri(redirectUriStr).scopes(clientRegistration.getScopes())
            .state(this.stateGenerator.generateKey())
            .attributes(attributes)
            .build();
    }

    /**
     * Expands the {@link ClientRegistration#getRedirectUriTemplate()} with
     * following provided variables:<br/>
     * - baseUrl (e.g. https://localhost/app) <br/>
     * - baseScheme (e.g. https) <br/>
     * - baseHost (e.g. localhost) <br/>
     * - basePort (e.g. :8080) <br/>
     * - basePath (e.g. /app) <br/>
     * - registrationId (e.g. google) <br/>
     * - action (e.g. login) <br/>
     * <p/>
     * Null variables are provided as empty strings.
     * <p/>
     * Default redirectUriTemplate is:
     * {@link org.springframework.security.config.oauth2.client}.CommonOAuth2Provider#DEFAULT_REDIRECT_URL
     *
     * @return expanded URI
     */
    private String expandRedirectUri(ServerHttpRequest request, ClientRegistration clientRegistration) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("registrationId", clientRegistration.getRegistrationId());
        List<String> xForwardedHost = request.getHeaders().get("x-forwarded-host");
        List<String> xForwardedProto = request.getHeaders().get("x-forwarded-proto");
        if (Objects.nonNull(xForwardedHost) && xForwardedHost.size() > 0) {
            uriVariables.put("xForwardedHost", xForwardedHost.get(0));
        } else {
            uriVariables.put("xForwardedHost", "");
        }
        if (Objects.nonNull(xForwardedProto) && xForwardedProto.size() > 0) {
            uriVariables.put("xForwardedProto", xForwardedProto.get(0));
        } else {
            uriVariables.put("xForwardedProto", "");
        }
        List<String> prefix = request.getQueryParams().get("prefix");
        if (com.dm.collections.CollectionUtils.isNotEmpty(prefix)) {
            uriVariables.put("prefix", org.apache.commons.lang3.StringUtils.join(prefix, PATH_DELIMITER));
        } else {
            uriVariables.put("prefix", this.defaultPrefix);
        }
        UriComponents uriComponents = UriComponentsBuilder.fromUri(request.getURI())
            .replacePath(request.getPath().contextPath().value())
            .replaceQuery(null)
            .fragment(null)
            .build();
        String scheme = uriComponents.getScheme();
        uriVariables.put("baseScheme", scheme == null ? "" : scheme);
        String host = uriComponents.getHost();
        uriVariables.put("baseHost", host == null ? "" : host);
        // following logic is based on HierarchicalUriComponents#toUriString()
        int port = uriComponents.getPort();
        uriVariables.put("basePort", port == -1 ? "" : ":" + port);
        String path = uriComponents.getPath();
        if (StringUtils.hasLength(path)) {
            if (path.charAt(0) != PATH_DELIMITER) {
                path = PATH_DELIMITER + path;
            }
        }
        uriVariables.put("basePath", path == null ? "" : path);
        uriVariables.put("baseUrl", uriComponents.toUriString());

        String action = "";
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
            action = "login";
        }
        uriVariables.put("action", action);

        return UriComponentsBuilder.fromUriString(clientRegistration.getRedirectUri())
            .buildAndExpand(uriVariables)
            .toUriString();
    }

    /**
     * Creates nonce and its hash for use in OpenID Connect 1.0 Authentication
     * Requests.
     *
     * @param attributes           where the {@link OidcParameterNames#NONCE} is
     *                             stored for the authentication request
     * @param additionalParameters where the {@link OidcParameterNames#NONCE} hash
     *                             is added for the authentication request
     * @see <a target="_blank" href=
     * "https://openid.net/specs/openid-connect-core-1_0.html#AuthRequest">3.1.2.1.
     * Authentication Request</a>
     * @since 5.2
     */
    private void addNonceParameters(Map<String, Object> attributes, Map<String, Object> additionalParameters) {
        try {
            String nonce = this.secureKeyGenerator.generateKey();
            String nonceHash = createHash(nonce);
            attributes.put(OidcParameterNames.NONCE, nonce);
            additionalParameters.put(OidcParameterNames.NONCE, nonceHash);
        } catch (NoSuchAlgorithmException e) {
        }
    }

    /**
     * Creates and adds additional PKCE parameters for use in the OAuth 2.0
     * Authorization and Access Token Requests
     *
     * @param attributes           where {@link PkceParameterNames#CODE_VERIFIER} is
     *                             stored for the token request
     * @param additionalParameters where {@link PkceParameterNames#CODE_CHALLENGE}
     *                             and, usually,
     *                             {@link PkceParameterNames#CODE_CHALLENGE_METHOD}
     *                             are added to be used in the authorization
     *                             request.
     * @see <a target="_blank" href=
     * "https://tools.ietf.org/html/rfc7636#section-1.1">1.1. Protocol Flow</a>
     * @see <a target="_blank" href=
     * "https://tools.ietf.org/html/rfc7636#section-4.1">4.1. Client Creates a
     * Code Verifier</a>
     * @see <a target="_blank" href=
     * "https://tools.ietf.org/html/rfc7636#section-4.2">4.2. Client Creates
     * the Code Challenge</a>
     * @since 5.2
     */
    private void addPkceParameters(Map<String, Object> attributes, Map<String, Object> additionalParameters) {
        String codeVerifier = this.secureKeyGenerator.generateKey();
        attributes.put(PkceParameterNames.CODE_VERIFIER, codeVerifier);
        try {
            String codeChallenge = createHash(codeVerifier);
            additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, codeChallenge);
            additionalParameters.put(PkceParameterNames.CODE_CHALLENGE_METHOD, "S256");
        } catch (NoSuchAlgorithmException e) {
            additionalParameters.put(PkceParameterNames.CODE_CHALLENGE, codeVerifier);
        }
    }

    private static String createHash(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
