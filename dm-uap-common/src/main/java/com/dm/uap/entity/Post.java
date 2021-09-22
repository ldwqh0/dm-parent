package com.dm.uap.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 职务
 *
 * @author LiDong
 */
@Embeddable
public class Post {

    @Column(name = "name_", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id_")
    private Department department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
