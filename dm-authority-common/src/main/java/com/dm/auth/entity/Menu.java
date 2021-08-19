package com.dm.auth.entity;

import com.dm.collections.CollectionUtils;
import com.dm.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dm_menu_", indexes = {
    @Index(columnList = "parent_", name = "idx_dm_menu_parent_")}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_dm_menu_parent_name_", columnNames = {"name_", "parent_"})
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
        HYPERLINK
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
    private Boolean openInNewWindow = Boolean.FALSE;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> children;

    public void setChildren(List<Menu> children) {
        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(child -> child.setParent(this));
        }
        this.children = children;
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

    public Menu children(List<Menu> children) {
        this.setChildren(children);
        return this;
    }

    public Menu() {

    }
}
