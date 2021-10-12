package com.dm.uap.dingtalk.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.CascadeType.*;

@Entity
@Getter
@Setter
@IdClass(CorpLongId.class)
@Table(name = "dd_role_group_", indexes = {@Index(name = "idx_dd_role_group_deleted_", columnList = "deleted_")})
public class DRoleGroup extends CorpLongEntity {

    @Column(name = "name_")
    private String name;

    @OneToMany(cascade = ALL)
    @JoinColumns({
        @JoinColumn(name = "dd_role_group_id_", referencedColumnName = "id_"),
        @JoinColumn(name = "dd_role_group_corp_id_", referencedColumnName = "corp_id_")
    })
    private Set<DRole> roles;

//    @OneToOne(cascade = { DETACH, MERGE, PERSIST, REFRESH })
//    @JoinColumn(name = "group_id_")
//    private RoleGroup group;

    /**
     * 标识角色组是否被删除
     */
    @Column(name = "deleted_")
    private Boolean deleted = false;

    public DRoleGroup(String corpId, Long id) {
        super(corpId, id);
    }

    public DRoleGroup() {

    }
}
