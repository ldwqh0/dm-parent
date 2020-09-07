package com.dm.uap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dm_department_", uniqueConstraints = { @UniqueConstraint(columnNames = { "parent_id_", "full_name_" }) })
public class Department extends AbstractEntity {

    /**
     * 部门类型
     * 
     * @author LiDong
     *
     */
    public enum Types {
        /**
         * 机构
         */
        ORGANS,
        /**
         * 部门
         */
        DEPARTMENT,
        /**
         * 分组
         */
        GROUP
    }

    /**
     * 部门名称
     */
    @Column(name = "full_name_", length = 100)
    private String fullname;

    /**
     * 短名称
     */
    @Column(name = "short_name_", length = 100)
    private String shortname;

    /**
     * 上级部门
     */
    @ManyToOne
    @JoinColumn(name = "parent_id_")
    private Department parent;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_")
    private Types type;

    /**
     * 部门描述
     */
    @Column(name = "description_", length = 2000)
    private String description;

    @Column(name = "order_")
    private Long order;
}
