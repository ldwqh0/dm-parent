package com.dm.auth.entity;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 权限信息
 * 
 * @author LiDong
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@Table(name = "dm_authority_")
public class Authority {

    @Column(name = "role_id_", nullable = false)
    private Long id;

    @Id
    @Column(name = "role_name_", length = 100)
    private String roleName;

    @Column(name = "description_")
    private String description;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "dm_authority_menu_", joinColumns = {
            @JoinColumn(name = "role_name_", referencedColumnName = "role_name_") }, inverseJoinColumns = {
                    @JoinColumn(name = "menu_id_", referencedColumnName = "id_") }, indexes = {
                            @Index(columnList = "role_name_", name = "IDX_dm_authority_menu_role_name_") })
    private Set<Menu> menus;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name = "dm_authority_resource_operation_", joinColumns = {
            @JoinColumn(name = "role_name_", referencedColumnName = "role_name_")
    })
    @OrderColumn(name = "order_by_")
    @MapKeyJoinColumn(name = "resource_id_")
    private Map<Resource, ResourceOperation> resourceOperations;

}
