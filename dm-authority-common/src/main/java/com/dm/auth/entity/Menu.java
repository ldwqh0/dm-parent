package com.dm.auth.entity;

import com.dm.collections.CollectionUtils;
import com.dm.data.domain.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

@Entity
@Table(name = "dm_menu_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_menu_parent_name_", columnNames = {"name_", "parent_"})
}, indexes = {
    @Index(name = "IDX_dm_menu_parent_", columnList = "parent_"),
    @Index(name = "IDX_dm_menu_enabled_", columnList = "enabled_")
})
public class Menu extends AbstractEntity {

    /**
     * 菜单类型，指定一个菜单的链接类型，可以是链接到业务组件，也可以是页面URL
     *
     * @author LiDong
     */
    public enum MenuType {
        /**
         * 链接到业务组件
         */
        COMPONENT,
        /**
         * 链接到超链接
         */
        HYPERLINK,
        /**
         * 子菜单
         */
        SUBMENU
    }

    /**
     * 菜单名称
     */
    @Column(name = "name_", length = 50, nullable = false)
    @NotNull
    private String name;

    /**
     * 菜单标题
     */
    @Column(name = "title_", length = 50, nullable = false)
    private String title;
    /**
     * 状态 启用true,禁用false
     */
    @Column(name = "enabled_", nullable = false)
    private boolean enabled = true;

    /**
     * 菜单表示的地址
     */
    @Column(name = "url_", length = 1000)
    private String url;

    /**
     * 菜单排序
     */
    @Column(name = "order_")
    private Long order;

    /**
     * 菜单图标
     */
    @Column(name = "icon_", length = 100)
    private String icon;

    /**
     * 备注信息
     */
    @Column(name = "description_", length = 1000)
    private String description;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "parent_", foreignKey = @ForeignKey(name = "FK_dm_menu_parent_"))
    private Menu parent;

    @Column(name = "type_", nullable = false)
    @Enumerated(EnumType.STRING)
    private MenuType type = MenuType.COMPONENT;

    /**
     * 是否在新窗口种打开链接
     */
    @Column(name = "open_in_new_window_", nullable = false)
    private boolean openInNewWindow = false;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Menu> children = new ArrayList<>();

    public void setChildren(List<Menu> children) {
        this.children.clear();
        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(child -> child.setParent(this));
            CollectionUtils.addAll(this.children, children);
        }
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "dm_role_menu_", joinColumns = {
        @JoinColumn(name = "menu_id_", referencedColumnName = "id_", foreignKey = @ForeignKey(name = "FK_dm_role_menu_menu_id_"))
    }, inverseJoinColumns = {
        @JoinColumn(name = "role_id_", referencedColumnName = "id_", foreignKey = @ForeignKey(name = "FK_dm_role_menu_role_id_"))
    }, indexes = {
        @Index(columnList = "role_id_", name = "IDX_dm_role_menu_role_id_"),
        @Index(columnList = "menu_id_", name = "IDX_dm_role_menu_menu_id_")
    })
    private final List<Role> roles = new ArrayList<>();

    public void setRoles(Collection<Role> roles) {
        this.roles.clear();
        CollectionUtils.addAll(this.roles, roles);
    }

    public Menu(@NotNull String name) {
        this(name, null);
    }

    public Menu(@NotNull String name, String url) {
        this(name, name, url);
    }

    public Menu(@NotNull String name, String title, String url) {
        this.name = name;
        this.title = title;
        this.url = url;
    }

    public Menu() {

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
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

    public Optional<Menu> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public boolean isOpenInNewWindow() {
        return openInNewWindow;
    }

    public void setOpenInNewWindow(boolean openInNewWindow) {
        this.openInNewWindow = openInNewWindow;
    }

    public List<Menu> getChildren() {
        return unmodifiableList(children);
    }

    public List<Role> getRoles() {
        return unmodifiableList(this.roles);
    }

    /**
     * 自动生成菜单排序,默认使用id号
     */
    @PostPersist
    private void autoSetOrder() {
        setOrder(getId());
    }

    @Override
    public String toString() {
        return "Menu{" +
            "name='" + name + '\'' +
            ", title='" + title + '\'' +
            ", enabled=" + enabled +
            ", url='" + url + '\'' +
            ", order=" + order +
            ", icon='" + icon + '\'' +
            ", description='" + description + '\'' +
            ", parent=" + parent +
            ", type=" + type +
            ", openInNewWindow=" + openInNewWindow +
            ", children=" + children +
            '}';
    }
}
