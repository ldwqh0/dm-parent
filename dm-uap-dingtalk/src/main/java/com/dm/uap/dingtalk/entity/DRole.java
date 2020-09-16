package com.dm.uap.dingtalk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.dm.uap.entity.Role;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@Table(name = "dd_role_", indexes = { @Index(name = "idx_dd_role_deleted_", columnList = "deleted_") })
@IdClass(CorpLongId.class)
public class DRole extends CorpLongEntity {

    @Column(name = "name_")
    private String name;

    @OneToOne(cascade = { DETACH, MERGE, PERSIST, REFRESH })
    @JoinColumn(name = "dm_role_id_")
    private Role role;

    @ManyToOne(cascade = REFRESH)
    @JoinColumns({
            @JoinColumn(name = "dd_role_group_id_", referencedColumnName = "id_"),
            @JoinColumn(name = "dd_role_group_corp_id_", referencedColumnName = "corp_id_")
    })
    private DRoleGroup group;

    /**
     * 标识角色是否被删除
     */
    @Column(name = "deleted_")
    private Boolean deleted = false;

    DRole() {
    }

    public DRole(String corpid, Long id) {
        super(corpid, id);
    }

}
