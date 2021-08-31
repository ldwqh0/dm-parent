package com.dm.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * 菜单授权信息
 *
 * @author LiDong
 */
@JsonInclude(Include.NON_ABSENT)
public class MenuAuthorityDto implements Serializable {
    private static final long serialVersionUID = 4813613447760388284L;
    /**
     * 角色Id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色可见的菜单列表
     */
    @JsonIgnoreProperties(value = {"parent"})
    private Set<MenuDto> authorityMenus;

    public MenuAuthorityDto() {
    }

    public MenuAuthorityDto(Long roleId, String roleName, Set<MenuDto> authorityMenus) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.authorityMenus = authorityMenus;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<MenuDto> getAuthorityMenus() {
        return authorityMenus;
    }

    public void setAuthorityMenus(Set<MenuDto> authorityMenus) {
        this.authorityMenus = authorityMenus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuAuthorityDto that = (MenuAuthorityDto) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(roleName, that.roleName) && Objects.equals(authorityMenus, that.authorityMenus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName, authorityMenus);
    }

    @Override
    public String toString() {
        return "MenuAuthorityDto{" +
            "roleId=" + roleId +
            ", roleName='" + roleName + '\'' +
            ", authorityMenus=" + authorityMenus +
            '}';
    }
}
