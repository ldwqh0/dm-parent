package com.dm.security.core.userdetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

@JsonInclude(NON_ABSENT)
public class GrantedAuthorityDto implements GrantedAuthority {
    private static final long serialVersionUID = 4062924753193768577L;
    private final Long id;
    private final String authority;

    public static final GrantedAuthorityDto ROLE_ADMIN = new GrantedAuthorityDto(3L, "内置分组_ROLE_ADMIN");
    public static final GrantedAuthorityDto ROLE_AUTHENTICATED = new GrantedAuthorityDto(1L, "内置分组_ROLE_AUTHENTICATED");
    public static final GrantedAuthorityDto ROLE_ANONYMOUS = new GrantedAuthorityDto(2L, "内置分组_ROLE_ANONYMOUS");

    public GrantedAuthorityDto(Long id, String authority) {
        this.authority = authority;
        this.id = id;
    }

    public GrantedAuthorityDto(String authority) {
        this(null, authority);
    }

    public GrantedAuthorityDto() {
        this(null);
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GrantedAuthorityDto [authority=" + authority + ", id=" + id + "]";
    }

}
