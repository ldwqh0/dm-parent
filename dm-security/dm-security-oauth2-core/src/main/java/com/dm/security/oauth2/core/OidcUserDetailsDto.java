package com.dm.security.oauth2.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

@JsonInclude(NON_ABSENT)
public class OidcUserDetailsDto extends OAuth2UserDetailsDto implements OidcUser {

    private static final long serialVersionUID = 8549068884661769277L;

    private final OidcUserInfo userinfo;
    private final OidcIdToken idToken;

    public OidcUserDetailsDto(OidcIdToken idToken, OidcUserInfo userInfo) {
        super(null, null, null, userInfo.getPreferredUsername(), null, userInfo.getFullName(), userInfo.getPhoneNumber(), userInfo.getEmail(), null, null, Collections.emptyMap());
        this.userinfo = userInfo;
        this.idToken = idToken;
    }

    public OidcUserDetailsDto(OidcIdToken idToken) {
        this(idToken, null);
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getClaims() {
        return getAttributes();
    }

    @Override
    @JsonInclude(value = NON_ABSENT, content = NON_ABSENT)
    public OidcUserInfo getUserInfo() {
        return this.userinfo;
    }

    @Override
    @JsonIgnore
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OidcUserDetailsDto that = (OidcUserDetailsDto) o;
        return Objects.equals(userinfo, that.userinfo) && Objects.equals(idToken, that.idToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userinfo, idToken);
    }
}
