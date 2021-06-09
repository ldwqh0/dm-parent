package com.dm.security.oauth2.client.userinfo;

import com.dm.collections.CollectionUtils;
import com.dm.security.oauth2.core.OidcUserDetailsDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.converter.ClaimConversionService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DmOidcReactiveOAuth2UserService implements ReactiveOAuth2UserService<OidcUserRequest, OidcUser> {

    private ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = new DefaultReactiveOAuth2UserService();

    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";

    public void setOauth2UserService(ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService) {
        Assert.notNull(oauth2UserService, "oauth2UserService cannot be null");
        this.oauth2UserService = oauth2UserService;
    }

    private static Converter<Object, ?> getConverter(TypeDescriptor targetDescriptor) {
        final TypeDescriptor sourceDescriptor = TypeDescriptor.valueOf(Object.class);
        return (source) -> ClaimConversionService.getSharedInstance().convert(source, sourceDescriptor, targetDescriptor);
    }

    @Override
    public Mono<OidcUser> loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        final OidcIdToken idToken = userRequest.getIdToken();
        // @formatter:off
        return getUserInfo(userRequest)
            .map((userInfo) ->
                new OidcUserAuthority(idToken, userInfo)
            )
            .defaultIfEmpty(new OidcUserAuthority(idToken, null))
            .map((authority) -> {
                OidcUserInfo userinfo = authority.getUserInfo();
                OidcUserDetailsDto userdetails;
                if (Objects.isNull(userinfo)) {
                    userdetails = copyProperties(new OidcUserDetailsDto(idToken), idToken);
                } else {
                    userdetails = copyProperties(new OidcUserDetailsDto(idToken, userinfo), userinfo);
                }
                Set<String> scopes = userRequest.getAccessToken().getScopes();
                userdetails.setScopes(scopes);
                return userdetails;
            });
    }

    private Mono<OidcUserInfo> getUserInfo(OidcUserRequest userRequest) {
        if (shouldRetrieveUserInfo(userRequest)) {
            return this.oauth2UserService
                .loadUser(userRequest)
                .map(OAuth2User::getAttributes)
                .map(OidcUserInfo::new)
                .doOnNext((userInfo) -> {
                    String subject = userInfo.getSubject();
                    if (Objects.isNull(subject) || !Objects.equals(subject, userRequest.getIdToken().getSubject())) {
                        OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE);
                        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                    }
                });
        } else {
            return Mono.empty();
        }
    }


    /**
     * 判断是否需要重新获取用户信息， 代码来自org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequestUtils
     *
     * @param userRequest 用户请求信息
     */
    private boolean shouldRetrieveUserInfo(OidcUserRequest userRequest) {
        // Auto-disabled if UserInfo Endpoint URI is not provided
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        if (StringUtils.isBlank(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())) {
            return false;
        }
        // The Claims requested by the profile, email, address, and phone scope values
        // are returned from the UserInfo Endpoint (as described in Section 5.3.2),
        // when a response_type value is used that results in an Access Token being
        // issued.
        // However, when no Access Token is issued, which is the case for the
        // response_type=id_token,
        // the resulting Claims are returned in the ID Token.
        // The Authorization Code Grant Flow, which is response_type=code, results in an
        // Access Token being issued.
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
            // Return true if there is at least one match between the authorized scope(s)
            // and UserInfo scope(s)
            return CollectionUtils.containsAny(userRequest.getAccessToken().getScopes(), userRequest.getClientRegistration().getScopes());
        }
        return false;
    }

    private OidcUserDetailsDto copyProperties(OidcUserDetailsDto userdetails, StandardClaimAccessor idToken) {
        List<Map<String, Object>> roles = idToken.getClaim("roles");
        if (CollectionUtils.isNotEmpty(roles)) {
            List<GrantedAuthority> authorities = roles.stream()
                .map(role -> role.get("authority").toString())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            userdetails.setGrantedAuthority(authorities);
        }
        userdetails.setAttributes(idToken.getClaims());
        userdetails.setUsername(idToken.getPreferredUsername());
        userdetails.setFullname(idToken.getFullName());
        userdetails.setMobile(idToken.getPhoneNumber());
        userdetails.setEmail(idToken.getEmail());
        return userdetails;
    }

}
