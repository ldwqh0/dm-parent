/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.security.oauth2.provider.endpoint;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOpenIdAccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dm.collections.CollectionUtils;

import java.security.Principal;
import java.util.*;

/**
 * <p>
 * Endpoint for token requests as described in the OAuth2 spec. Clients post
 * requests with a <code>grant_type</code> parameter (e.g. "authorization_code")
 * and other parameters as determined by the grant type. Supported grant types
 * are handled by the provided
 * {@link #setTokenGranter(org.springframework.security.oauth2.provider.TokenGranter)
 * token granter}.
 * </p>
 *
 * <p>
 * Clients must be authenticated using a Spring Security {@link Authentication}
 * to access this endpoint, and the client id is extracted from the
 * authentication token. The best way to arrange this (as per the OAuth2 spec)
 * is to use HTTP basic authentication for this endpoint with standard Spring
 * Security support.
 * </p>
 *
 * <p>
 * See the <a href=
 * "https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth
 * 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Dave Syer
 */
@FrameworkEndpoint
public class TokenEndpoint extends AbstractEndpoint {

    private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();

    private Set<HttpMethod> allowedRequestMethods = new HashSet<HttpMethod>(Arrays.asList(HttpMethod.POST));

    @RequestMapping(value = "/oauth/token", method = RequestMethod.GET)
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal,
                                                            @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        if (!allowedRequestMethods.contains(HttpMethod.GET)) {
            throw new HttpRequestMethodNotSupportedException("GET");
        }
        return postAccessToken(principal, parameters);
    }

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal,
                                                             @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }

        String clientId = getClientId(principal);
        ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);

        TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);

        if (clientId != null && !clientId.equals("")) {
            // Only validate the client details if a client authenticated during this
            // request.
            if (!clientId.equals(tokenRequest.getClientId())) {
                // double check to make sure that the client ID in the token request is the same
                // as that in the
                // authenticated client
                throw new InvalidClientException("Given client ID does not match authenticated client");
            }
        }
        if (authenticatedClient != null) {
            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
        }
        if (!StringUtils.hasText(tokenRequest.getGrantType())) {
            throw new InvalidRequestException("Missing grant type");
        }
        if (tokenRequest.getGrantType().equals("implicit")) {
            throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
        }

        if (isAuthCodeRequest(parameters)) {
            // The scope was requested or determined during the authorization step
            if (!tokenRequest.getScope().isEmpty()) {
                logger.debug("Clearing scope of incoming token request");
                tokenRequest.setScope(Collections.<String>emptySet());
            }
        }

        if (isRefreshTokenRequest(parameters)) {
            // A refresh token has its own default scopes, so we should ignore any added by
            // the factory here.
            tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
        }
        OAuth2AccessToken token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
        if (token == null) {
            throw new UnsupportedGrantTypeException("Unsupported grant type");
        } else {
            // 如果scope包含了openid,返回openid相关的信息
            Set<String> scopes = tokenRequest.getScope();
            if (CollectionUtils.isNotEmpty(scopes) && scopes.contains("openid")) {
                // TODO 生成id_token
                token = new DefaultOpenIdAccessToken("Ad", token);
            }
        }
//        JwtTokenStore store = new JwtTokenStore();
        JwtAccessTokenConverter tc = new JwtAccessTokenConverter();
        return getResponse(token);
    }

    /**
     * @param principal the currently authentication principal
     * @return a client id if there is one in the principal
     */
    protected String getClientId(Principal principal) {
        Authentication client = (Authentication) principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        }
        String clientId = client.getName();
        if (client instanceof OAuth2Authentication) {
            // Might be a client and user combined authentication
            clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
        }
        return clientId;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<OAuth2Exception> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        }
        return getExceptionTranslator().translate(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        if (logger.isErrorEnabled()) {
            logger.error("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage(), e);
        }
        return getExceptionTranslator().translate(e);
    }

    @ExceptionHandler(ClientRegistrationException.class)
    public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
        if (logger.isWarnEnabled()) {
            logger.warn("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        }
        return getExceptionTranslator().translate(new BadClientCredentialsException());
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(OAuth2Exception e) throws Exception {
        if (logger.isWarnEnabled()) {
            logger.warn("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        }
        return getExceptionTranslator().translate(e);
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<OAuth2AccessToken>(accessToken, headers, HttpStatus.OK);
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }

    public void setOAuth2RequestValidator(OAuth2RequestValidator oAuth2RequestValidator) {
        this.oAuth2RequestValidator = oAuth2RequestValidator;
    }

    public void setAllowedRequestMethods(Set<HttpMethod> allowedRequestMethods) {
        this.allowedRequestMethods = allowedRequestMethods;
    }
}
