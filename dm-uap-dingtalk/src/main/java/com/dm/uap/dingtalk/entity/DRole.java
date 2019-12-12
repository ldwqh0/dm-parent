package com.dm.uap.dingtalk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.dm.uap.entity.Role;

import lombok.Getter;
import lombok.Setter;
import static javax.persistence.CascadeType.*;

import java.io.Serializable;

@Entity(name = "dd_role_")
@Getter
@Setter
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
