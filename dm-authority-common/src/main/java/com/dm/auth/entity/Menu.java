package com.dm.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dm_menu_", indexes = {
        @Index(columnList = "parent_", name = "IDX_bf_menu_parent_") }, uniqueConstraints = {
                @UniqueConstraint(columnNames = { "name_", "parent_" })
        })
public class Menu extends AbstractEntity {

    private static final long serialVersionUID = -5469864417544946812L;

    /**
     * 菜单类型，指定一个菜单的链接类型，可以是链接到业务组件，也可以是页面URL
     * 
     * @author LiDong
     *
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
    @Column(name = "title_", length = 50)
    private String title;
    /**
     * 状态 启用true,禁用false
     */
    @Column(name = "enabled_")
    private boolean enabled = true;

    /**
     * 菜单表示的地址
     */
    @Column(name = "url_")
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

    @ManyToOne
    @JoinColumn(name = "parent_")
    private Menu parent;

    @Column(name = "type_")
    @Enumerated(EnumType.STRING)
    private MenuType type = MenuType.COMPONENT;

    /**
     * 是否在新窗口种打开链接
     */
    @Column(name = "open_in_new_window_")
    private Boolean openInNewWindow;

}
