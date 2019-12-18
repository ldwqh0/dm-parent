package com.dm.security.oauth2.client.userinfo;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.dm.security.oauth2.core.PrincipalExtractor;
import com.dm.security.oauth2.core.UserDetailsDtoPrincipalExtractor;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;

import net.minidev.json.JSONObject;
import reactor.core.publisher.Mono;

public class DmReactiveOAuth2UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

    private WebClient webClient = WebClient.create();

    private PrincipalExtractor principalExtractor = new UserDetailsDtoPrincipalExtractor();

    @Autowired(required = false)
    public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
        this.principalExtractor = principalExtractor;
    }

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
//        userRequest.getAccessToken().getScopes()
        return Mono.defer(() -> {
            Assert.notNull(userRequest, "userRequest cannot be null");

            String userInfoUri = userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint().getUri();
            if (!StringUtils.hasText(
                    userInfoUri)) {
                OAuth2Error oauth2Error = new OAuth2Error(
                        MISSING_USER_INFO_URI_ERROR_CODE,
                        "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
                                + userRequest.getClientRegistration().getRegistrationId(),
                        null);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            }
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();
            if (!StringUtils.hasText(userNameAttributeName)) {
                OAuth2Error oauth2Error = new OAuth2Error(
                        MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
                        "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: "
                                + userRequest.getClientRegistration().getRegistrationId(),
                        null);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            }

            ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<Map<String, Object>>() {
            };

            AuthenticationMethod authenticationMethod = userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint().getAuthenticationMethod();
            WebClient.RequestHeadersSpec<?> requestHeadersSpec;
            if (AuthenticationMethod.FORM.equals(authenticationMethod)) {
                requestHeadersSpec = this.webClient.post()
                        .uri(userInfoUri)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .syncBody("access_token=" + userRequest.getAccessToken().getTokenValue());
            } else {
                requestHeadersSpec = this.webClient.get()
                        .uri(userInfoUri)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers -> headers.setBearerAuth(userRequest.getAccessToken().getTokenValue()));
            }
            Mono<Map<String, Object>> userAttributes = requestHeadersSpec
                    .retrieve()
                    .onStatus(s -> s != HttpStatus.OK, response -> parse(response).map(userInfoErrorResponse -> {
                        String description = userInfoErrorResponse.getErrorObject().getDescription();
                        OAuth2Error oauth2Error = new OAuth2Error(
                                INVALID_USER_INFO_RESPONSE_ERROR_CODE, description,
                                null);
                        throw new OAuth2AuthenticationException(oauth2Error,
                                oauth2Error.toString());
                    }))
                    .bodyToMono(typeReference);

            // 使用自定义的解码器解码
            return userAttributes.map(principalExtractor::extract)
                    .doOnSuccess(i -> {
                        i.getAttributes().put("accessToken", userRequest.getAccessToken());
                    })
                    .onErrorMap(e -> e instanceof IOException,
                            t -> new AuthenticationServiceException(
                                    "Unable to access the userInfoEndpoint " + userInfoUri, t))
                    .onErrorMap(t -> !(t instanceof AuthenticationServiceException), t -> {
                        OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                                "An error occurred reading the UserInfo Success response: " + t.getMessage(), null);
                        return new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), t);
                    });
        });
    }

    /**
     * Sets the {@link WebClient} used for retrieving the user endpoint
     * 
     * @param webClient the client to use
     */
    public void setWebClient(WebClient webClient) {
        Assert.notNull(webClient, "webClient cannot be null");
        this.webClient = webClient;
    }

    private static Mono<UserInfoErrorResponse> parse(ClientResponse httpResponse) {

        String wwwAuth = httpResponse.headers().asHttpHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE);

        if (!StringUtils.isEmpty(wwwAuth)) {
            // Bearer token error?
            return Mono.fromCallable(() -> UserInfoErrorResponse.parse(wwwAuth));
        }

        ParameterizedTypeReference<Map<String, String>> typeReference = new ParameterizedTypeReference<Map<String, String>>() {
        };
        // Other error?
        return httpResponse
                .bodyToMono(typeReference)
                .map(body -> new UserInfoErrorResponse(ErrorObject.parse(new JSONObject(body))));
    }

}
