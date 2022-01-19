package com.dm.auth.dto;

import com.dm.auth.entity.Menu.MenuType;
import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private final Long id;
    /**
     * 菜单名称
     */
    private final String name;
    /**
     * 菜单标题
     */
    private final String title;
    /**
     * 菜单是否可用
     */
    private final Boolean enabled;
    /**
     * 菜单地址
     */
    private final String url;
    /**
     * 菜单图标
     */
    private final String icon;
    /**
     * 菜单描述信息
     */
    private final String description;
    /**
     * 菜单类型
     */
    private final MenuType type;

    private final Long order;
    /**
     * 父菜单
     */
    @JsonIgnoreProperties(value = {"parent", "url", "description", "openInNewWindow"})
    private final MenuDto parent;

    /**
     * 是否在新窗口中打开菜单
     */
    private final Boolean openInNewWindow;

    /**
     * 子菜单数量
     */
    private final Long childrenCount;

    /**
     * @param id              id
     * @param name            菜单名称
     * @param title           菜单标题
     * @param enabled         菜单是否启用
     * @param url             菜单url
     * @param icon            菜单图标
     * @param description     菜单描述
     * @param type            菜单类型
     * @param order           菜单排序
     * @param parent          父级菜单
     * @param openInNewWindow 是否在新窗口中打开菜单
     * @param childrenCount   字菜单个数
     */
    public MenuDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("title") String title,
        @JsonProperty("enabled") Boolean enabled,
        @JsonProperty("url") String url,
        @JsonProperty("icon") String icon,
        @JsonProperty("description") String description,
        @JsonProperty("type") MenuType type,
        @JsonProperty("order") Long order,
        @JsonProperty("parent") MenuDto parent,
        @JsonProperty("openInNewWindow") Boolean openInNewWindow,
        @JsonProperty("childrenCount") Long childrenCount) {
        this.id = id;
        this.name = name;
        this.title = title;
        if (Objects.isNull(enabled)) {
            this.enabled = Boolean.TRUE;
        } else {
            this.enabled = enabled;
        }
        this.url = url;
        this.icon = icon;
        this.description = description;
        if (Objects.isNull(type)) {
            this.type = MenuType.COMPONENT;
        } else {
            this.type = type;
        }
        this.order = order;
        this.parent = parent;
        if (Objects.isNull(openInNewWindow)) {
            this.openInNewWindow = Boolean.FALSE;
        } else {
            this.openInNewWindow = openInNewWindow;
        }
        this.childrenCount = childrenCount;
    }

    public MenuDto(String name, String title, String url) {
        this(null,
            name,
            title,
            Boolean.TRUE,
            url,
            null,
            null,
            MenuType.COMPONENT,
            null,
            null, Boolean.FALSE,
            null
        );
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getUrl() {
        return url;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public MenuType getType() {
        return type;
    }

    public Long getOrder() {
        return order;
    }

    public Boolean getOpenInNewWindow() {
        return openInNewWindow;
    }

    public Long getChildrenCount() {
        return childrenCount;
    }

    @JsonGetter
    public Boolean hasChildren() {
        if (Objects.isNull(childrenCount)) {
            return null;
        } else {
            return childrenCount > 0;
        }
    }

    public Optional<MenuDto> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(id, menuDto.id) && Objects.equals(name, menuDto.name) && Objects.equals(title, menuDto.title) && Objects.equals(enabled, menuDto.enabled) && Objects.equals(url, menuDto.url) && Objects.equals(icon, menuDto.icon) && Objects.equals(description, menuDto.description) && type == menuDto.type && Objects.equals(order, menuDto.order) && Objects.equals(parent, menuDto.parent) && Objects.equals(openInNewWindow, menuDto.openInNewWindow) && Objects.equals(childrenCount, menuDto.childrenCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, enabled, url, icon, description, type, order, parent, openInNewWindow, childrenCount);
    }
}
