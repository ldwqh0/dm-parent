package com.dm.security.core.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.dm.collections.Lists.arrayList;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static java.util.Collections.unmodifiableCollection;

@JsonInclude(NON_ABSENT)
public class UserDetailsDto implements UserDetails {
    private static final long serialVersionUID = -4337846050031244208L;

    private final Long id;

    @JsonIgnore
    private final String password;

    /**
     * 用户名，这个字段的值不能被后续改变
     */
    private final String username;

    private final boolean accountExpired;
    private final boolean credentialsExpired;

    @JsonProperty(access = Access.WRITE_ONLY)
    private final boolean enabled;

    private final boolean locked;

    private final Collection<? extends GrantedAuthority> grantedAuthority;

    private final String fullName;

    private final String mobile;

    private final String email;

    private final String regionCode;

    private final String scenicName;

    /**
     * @param id                 id
     * @param username           用户名
     * @param password           密码
     * @param accountExpired     账号是否过期
     * @param credentialsExpired 密码是否过期
     * @param enabled            账号启用
     * @param locked             账号锁定
     * @param grantedAuthority   用户角色
     * @param fullName           全名称
     * @param mobile             电话
     * @param email              邮箱
     * @param regionCode         地域
     * @param scenicName         景区名称
     */

    protected UserDetailsDto(Long id, String username, String password, boolean accountExpired, boolean credentialsExpired, boolean enabled, boolean locked, Collection<? extends GrantedAuthority> grantedAuthority, String fullName, String mobile, String email, String regionCode, String scenicName) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.accountExpired = accountExpired;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;
        this.locked = locked;
        this.grantedAuthority = arrayList(grantedAuthority);
        this.fullName = fullName;
        this.mobile = mobile;
        this.email = email;
        this.regionCode = regionCode;
        this.scenicName = scenicName;
    }

//    public UserDetailsDto(String username, Collection<? extends GrantedAuthority> grantedAuthority) {
//        this(null, username, ",", false, false, false, false, grantedAuthority, null, null, null, null, null);
//    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return unmodifiableCollection(grantedAuthority);
    }

    public Collection<GrantedAuthority> getRoles() {
        return unmodifiableCollection(grantedAuthority);
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

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getScenicName() {
        return scenicName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getEmail() {
        return email;
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String password;
        private String username;
        private boolean accountExpired = false;
        private boolean credentialsExpired = false;
        private boolean enabled = false;
        private boolean locked = false;
        private Collection<? extends GrantedAuthority> grantedAuthority;
        private String fullName;
        private String mobile;
        private String email;
        private String regionCode;
        private String scenicName;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public Builder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder locked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public Builder grantedAuthority(Collection<? extends GrantedAuthority> grantedAuthority) {
            this.grantedAuthority = grantedAuthority;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder regionCode(String regionCode) {
            this.regionCode = regionCode;
            return this;
        }

        public Builder scenicName(String scenicName) {
            this.scenicName = scenicName;
            return this;
        }

        public UserDetailsDto build() {
            return new UserDetailsDto(id, username, password, accountExpired, credentialsExpired, enabled, locked, grantedAuthority, fullName, mobile, email, regionCode, scenicName);
        }
    }
}
