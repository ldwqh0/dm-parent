package com.dm.uap.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 职务
 *
 * @author LiDong
 *
 */
@Embeddable
@Data
public class Post {

    @Column(name = "name_")
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id_")
    private Department department;

}
