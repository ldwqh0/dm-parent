package com.dm.security.core.userdetails;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_ABSENT)
public class GrantedAuthorityDto implements GrantedAuthority {
    private static final long serialVersionUID = 4062924753193768577L;
    private String authority;
    private Long id;

    public GrantedAuthorityDto(String authority, Long id) {
        super();
        this.authority = authority;
        this.id = id;
    }

    public GrantedAuthorityDto(String authority) {
        this(authority, null);
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "GrantedAuthorityDto [authority=" + authority + ", id=" + id + "]";
    }

}
