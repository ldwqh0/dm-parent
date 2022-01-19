package com.dm.auth.dto;

import com.dm.auth.entity.Menu.MenuType;
import com.dm.collections.CollectionUtils;
import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.*;

/**
 * 菜单结构
 *
 * @author LiDong
 */
@JsonInclude(Include.NON_EMPTY)
public class MenuTreeDto implements Serializable, IdentifiableDto<Long> {
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

    /**
     * 排序
     */
    @JsonIgnore
    private final Long order;

    /**
     * 是否在新窗口中打开菜单
     */
    private final Boolean openInNewWindow;

    @JsonIgnore
    private final Long parentId;

    /**
     * 下级菜单列表
     */
    private final List<MenuTreeDto> children = new ArrayList<>();

    public List<MenuTreeDto> getChildren() {
        List<MenuTreeDto> result = new ArrayList<>(this.children);
        result.sort((v1, v2) -> (int) (Optional.of(v1).map(MenuTreeDto::getOrder).orElse(0L) - Optional.of(v2).map(MenuTreeDto::getOrder).orElse(0L)));
        return Collections.unmodifiableList(result);
    }

    public void addChild(MenuTreeDto item) {
        children.add(item);
    }

    public boolean remove(MenuTreeDto item) {
        return children.remove(item);
    }


    public MenuTreeDto(Long id,
                       String name,
                       String title,
                       Boolean enabled,
                       String url,
                       String icon,
                       String description,
                       MenuType type,
                       Long order,
                       Boolean openInNewWindow,
                       Long parentId,
                       List<MenuTreeDto> children) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.enabled = enabled;
        this.url = url;
        this.icon = icon;
        this.description = description;
        this.type = type;
        this.order = order;
        this.openInNewWindow = openInNewWindow;
        this.parentId = parentId;
        if (CollectionUtils.isNotEmpty(children)) {
            this.children.addAll(children);
        }
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

    public Long getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTreeDto that = (MenuTreeDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(title, that.title) && Objects.equals(enabled, that.enabled) && Objects.equals(url, that.url) && Objects.equals(icon, that.icon) && Objects.equals(description, that.description) && type == that.type && Objects.equals(order, that.order) && Objects.equals(openInNewWindow, that.openInNewWindow) && Objects.equals(parentId, that.parentId) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, enabled, url, icon, description, type, order, openInNewWindow, parentId, children);
    }

    @Override
    public String toString() {
        return "MenuTreeDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", title='" + title + '\'' +
            ", enabled=" + enabled +
            ", url='" + url + '\'' +
            ", icon='" + icon + '\'' +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", order=" + order +
            ", openInNewWindow=" + openInNewWindow +
            ", parentId=" + parentId +
            ", children=" + children +
            '}';
    }
}
