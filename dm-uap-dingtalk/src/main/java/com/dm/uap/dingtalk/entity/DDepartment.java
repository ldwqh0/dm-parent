package com.dm.uap.dingtalk.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.dm.uap.entity.Department;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.CascadeType.*;

@Entity(name = "dd_department_")
@Getter
@Setter
public class DDepartment implements Serializable {

    private static final long serialVersionUID = 8399805234987134498L;

    /**
     * id
     */
    @Id
    @Column(name = "id_")
    private Long id;

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
    private boolean deleted = false;

    public DDepartment() {
        super();
    }

    public DDepartment(Long id) {
        super();
        this.id = id;
    }

    void setId(Long id) {
        this.id = id;
    }
}
