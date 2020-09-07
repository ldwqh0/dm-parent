package com.dm.uap.dingtalk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dm.uap.entity.Department;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@Table(name = "dd_department_", indexes = { @Index(columnList = "deleted_", name = "idx_dd_department_deleted_") })
@IdClass(CorpLongId.class)
public class DDepartment extends CorpLongEntity {

    /**
     * autoAddUser
     */
    private Boolean autoAddUser;
    /**
     * createDeptGroup
     */
    private Boolean createDeptGroup;

    /**
     * name
     */
    @Column(name = "name_")
    private String name;
    /**
     * parentid
     */
    private Long parentid;
    /**
     * sourceIdentifier
     */
    private String sourceIdentifier;

    /**
     * 一个钉钉部门对应的系统部门
     */
    @OneToOne(cascade = { MERGE, PERSIST, REFRESH, DETACH })
    @JoinColumn(name = "dm_department_id_")
    public Department department;

    /**
     * 标识用户是否被删除
     */
    @Column(name = "deleted_")
    private Boolean deleted = false;

    DDepartment() {

    }

    public DDepartment(String corpid, Long id) {
        super(corpid, id);
    }

}
