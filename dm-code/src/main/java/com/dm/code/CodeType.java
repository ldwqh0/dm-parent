package com.dm.code;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 编码类型
 *
 * @author 李东
 */
@Entity
@Table(name = "dm_code_type_")
public class CodeType {

    @Id
    @Column(name = "name_", length = 100, unique = true)
    private String name;

    @Column(name = "details", length = 4000)
    private String details;

}
