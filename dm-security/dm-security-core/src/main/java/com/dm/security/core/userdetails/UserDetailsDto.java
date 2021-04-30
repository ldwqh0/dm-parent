package com.dm.security.core.userdetails;

import com.dm.collections.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@JsonInclude(Include.NON_ABSENT)
public class UserDetailsDto implements UserDetails, OAuth2User {
    private static final long serialVersionUID = -4337846050031244208L;

    private Long id;
    @JsonIgnore
    private String password;
    private String username;
    private boolean accountExpired;
    private boolean credentialsExpired;
    @JsonProperty(access = Access.WRITE_ONLY)
    private boolean enabled;
    private boolean locked;
    private Collection<? extends GrantedAuthority> grantedAuthority;
    private String fullname;

    private String mobile;

    private String regionCode;

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    private String scenicName;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthority;
    }

    public void setGrantedAuthority(Collection<? extends GrantedAuthority> grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }

    public Collection<GrantedAuthority> getRoles() {
        if (CollectionUtils.isNotEmpty(grantedAuthority)) {
            return new ArrayList<>(grantedAuthority);
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDetailsDto other = (UserDetailsDto) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return username;
    }

}
