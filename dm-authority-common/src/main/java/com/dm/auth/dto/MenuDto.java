package com.dm.auth.dto;

import com.dm.auth.entity.Menu.MenuType;
import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

/**
 * 菜单结构
 *
 * @author LiDong
 */
@Data
@JsonInclude(Include.NON_ABSENT)
public class MenuDto implements Serializable, IdentifiableDto<Long> {
    private static final long serialVersionUID = 7184771144233410172L;
    /**
     * 菜单id
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 菜单是否可用
     */
    private Boolean enabled = Boolean.TRUE;
    /**
     * 菜单地址
     */
    private String url;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单描述信息
     */
    private String description;
    /**
     * 菜单类型
     */
    private MenuType type = MenuType.COMPONENT;

    private Long order;
    /**
     * 父菜单
     */
    @JsonIgnoreProperties(value = {"parent", "url", "description", "openInNewWindow"})
    private MenuDto parent;

    /**
     * 是否在新窗口中打开菜单
     */
    private Boolean openInNewWindow = Boolean.FALSE;

    public Optional<MenuDto> getParent() {
        return Optional.ofNullable(parent);
    }
}
