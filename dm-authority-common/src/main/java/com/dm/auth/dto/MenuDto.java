package com.dm.auth.dto;

import com.dm.auth.entity.Menu.MenuType;
import com.dm.data.domain.Identifiable;
import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static java.lang.Boolean.TRUE;

/**
 * 菜单结构
 *
 * @author LiDong
 */
@JsonInclude(NON_ABSENT)
public class MenuDto implements Serializable, Identifiable<Long> {
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
    private final boolean enabled;
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
     * 用户菜单输出排序
     */
    @JsonIgnore
    private final Long order;
    /**
     * 父菜单
     */
    @JsonIgnoreProperties({"parent", "url", "description", "openInNewWindow"})
    private final MenuDto parent;

    /**
     * 是否在新窗口中打开菜单
     */
    private final boolean openInNewWindow;

    /**
     * 子菜单数量
     */
    private final Integer childrenCount;

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
    private MenuDto(Long id,
                    String name,
                    String title,
                    Boolean enabled,
                    String url,
                    String icon,
                    String description,
                    MenuType type,
                    Long order,
                    MenuDto parent,
                    Boolean openInNewWindow,
                    Integer childrenCount) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.enabled = TRUE.equals(enabled);
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
        this.openInNewWindow = TRUE.equals(openInNewWindow);
        this.childrenCount = childrenCount;
    }

    @JsonCreator
    private MenuDto(@JsonProperty("id") Long id,
                    @JsonProperty("name") String name,
                    @JsonProperty("title") String title,
                    @JsonProperty("enabled") Boolean enabled,
                    @JsonProperty("url") String url,
                    @JsonProperty("icon") String icon,
                    @JsonProperty("description") String description,
                    @JsonProperty("type") MenuType type,
                    @JsonProperty("order") Long order,
                    @JsonProperty("parent") MenuDto parent,
                    @JsonProperty("openInNewWindow") Boolean openInNewWindow) {
        this(id, name, title, enabled, url, icon, description, type, order, parent, openInNewWindow, null);
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

    public boolean isEnabled() {
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

    public boolean isOpenInNewWindow() {
        return openInNewWindow;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    /**
     * 是否有子菜单存在<br>
     * 在dto中，可能存在没有填充childrenCount的情况，这其实是一种未知状态，所以也返回未知状态
     *
     * @return 返回为空代表未进行自带数量计算
     */
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String name;
        private String title;
        private Boolean enabled;
        private String url;
        private String icon;
        private String description;
        private MenuType type;
        private Long order;
        private MenuDto parent;
        private Boolean openInNewWindow;
        private Integer childrenCount;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(MenuType type) {
            this.type = type;
            return this;
        }

        public Builder order(Long order) {
            this.order = order;
            return this;
        }

        public Builder parent(MenuDto parent) {
            this.parent = parent;
            return this;
        }

        public Builder openInNewWindow(boolean openInNewWindow) {
            this.openInNewWindow = openInNewWindow;
            return this;
        }

        public Builder childrenCount(Integer childrenCount) {
            this.childrenCount = childrenCount;
            return this;
        }

        public MenuDto build() {
            return new MenuDto(id, name, title, enabled, url, icon, description, type, order, parent, openInNewWindow, childrenCount);
        }
    }
}
