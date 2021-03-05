package com.dm.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 菜单授权信息
 *
 * @author LiDong
 */
@Data
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
}
