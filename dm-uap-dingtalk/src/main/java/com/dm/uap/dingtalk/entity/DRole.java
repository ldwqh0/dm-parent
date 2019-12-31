package com.dm.uap.dingtalk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dm.uap.entity.Role;

import lombok.Getter;
import lombok.Setter;
import static javax.persistence.CascadeType.*;

import java.io.Serializable;

@Entity(name = "dd_role_")
@Getter
@Setter
@Table(indexes = { @Index(name = "idx_dd_role_deleted_", columnList = "deleted_") })
public class DRole implements Serializable {

    private static final long serialVersionUID = -8441406771526246885L;

    @Id
    @Column(name = "id_")
    private Long id;

    @Column(name = "name_")
    private String name;

    @OneToOne(cascade = { DETACH, MERGE, PERSIST, REFRESH })
    @JoinColumn(name = "dm_role_id_")
    private Role role;

    @ManyToOne(cascade = REFRESH)
    @JoinColumn(name = "dd_group_id_")
    private DRoleGroup group;

    /**
     * 标识角色是否被删除
     */
    @Column(name = "deleted_")
    private Boolean deleted = false;

    public DRole() {
        super();
    }

    public DRole(Long id) {
        super();
        this.id = id;
    }

    void setId(Long id) {
        this.id = id;
    }

}
