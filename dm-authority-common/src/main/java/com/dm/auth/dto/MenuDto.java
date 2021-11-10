package com.dm.auth.dto;

import com.dm.auth.entity.Menu.MenuType;
import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * 菜单结构
 *
 * @author LiDong
 */
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

    private long childrenCount = 0;

    public MenuDto() {
    }

    public MenuDto(Long id, String name, String title, Boolean enabled, String url, String icon, String description, MenuType type, Long order, MenuDto parent, Boolean openInNewWindow, long childrenCount) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.enabled = enabled;
        this.url = url;
        this.icon = icon;
        this.description = description;
        this.type = type;
        this.order = order;
        this.parent = parent;
        this.openInNewWindow = openInNewWindow;
        this.childrenCount = childrenCount;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public void setParent(MenuDto parent) {
        this.parent = parent;
    }

    public Boolean getOpenInNewWindow() {
        return openInNewWindow;
    }

    public void setOpenInNewWindow(Boolean openInNewWindow) {
        this.openInNewWindow = openInNewWindow;
    }

    public long getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(long childrenCount) {
        this.childrenCount = childrenCount;
    }

    @JsonGetter
    public boolean hasChildren() {
        return childrenCount > 0;
    }

    public Optional<MenuDto> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuDto menuDto = (MenuDto) o;
        return childrenCount == menuDto.childrenCount && Objects.equals(id, menuDto.id) && Objects.equals(name, menuDto.name) && Objects.equals(title, menuDto.title) && Objects.equals(enabled, menuDto.enabled) && Objects.equals(url, menuDto.url) && Objects.equals(icon, menuDto.icon) && Objects.equals(description, menuDto.description) && type == menuDto.type && Objects.equals(order, menuDto.order) && Objects.equals(parent, menuDto.parent) && Objects.equals(openInNewWindow, menuDto.openInNewWindow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, enabled, url, icon, description, type, order, parent, openInNewWindow, childrenCount);
    }
}
