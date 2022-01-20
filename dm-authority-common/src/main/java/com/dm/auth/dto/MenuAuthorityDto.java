package com.dm.auth.dto;

import com.dm.collections.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.dm.collections.Sets.hashSet;

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
    private final Long roleId;
    /**
     * 角色名称
     */
    private final String roleName;
    /**
     * 角色可见的菜单列表
     */
    @JsonIgnoreProperties(value = {"parent"})
    private final Set<MenuDto> authorityMenus = new HashSet<>();

    /**
     * @param roleId         角色id
     * @param roleName       角色名称
     * @param authorityMenus 角色的授权菜单
     */
    public MenuAuthorityDto(
        @JsonProperty("roleId") Long roleId,
        @JsonProperty("roleName") String roleName,
        @JsonProperty("authorityMenus") Set<MenuDto> authorityMenus) {
        this.roleId = roleId;
        this.roleName = roleName;
        if (CollectionUtils.isNotEmpty(authorityMenus)) {
            this.authorityMenus.addAll(authorityMenus);
        }
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<MenuDto> getAuthorityMenus() {
        return hashSet(authorityMenus);
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
