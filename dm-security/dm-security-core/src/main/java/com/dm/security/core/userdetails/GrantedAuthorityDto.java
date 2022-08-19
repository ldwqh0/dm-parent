package com.dm.security.core.userdetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

@JsonInclude(NON_ABSENT)
public class GrantedAuthorityDto implements GrantedAuthority {
    private static final long serialVersionUID = 4062924753193768577L;
    private final Long id;
    private final String authority;

    public static final GrantedAuthorityDto ROLE_AUTHENTICATED = new GrantedAuthorityDto(1L, "内置分组_ROLE_AUTHENTICATED");
    public static final GrantedAuthorityDto ROLE_ADMIN = new GrantedAuthorityDto(3L, "内置分组_ROLE_ADMIN");
    public static final GrantedAuthorityDto ROLE_ANONYMOUS = new GrantedAuthorityDto(2L, "内置分组_ROLE_ANONYMOUS");

    private GrantedAuthorityDto(Long id, String authority) {
        this.authority = authority;
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public Long getId() {
        return id;
    }

    public static GrantedAuthorityDto of(Long id, String name) {
        return new GrantedAuthorityDto(id, name);
    }

    public static GrantedAuthorityDto of(String name) {
        return new GrantedAuthorityDto(null, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrantedAuthorityDto that = (GrantedAuthorityDto) o;
        return Objects.equals(id, that.id) && Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority);
    }

    @Override
    public String toString() {
        return "GrantedAuthorityDto{" + "id=" + id + ", authority='" + authority + '\'' + '}';
    }
}
