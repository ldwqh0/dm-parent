package com.dm.common.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class ModifyAudit implements Audit {

    private static final long serialVersionUID = 5546618897219690297L;

    @Column(name = "last_modified_user_id_")
    private Long userid;

    @Column(name = "last_modified_user_name_")
    private String username;

    public ModifyAudit() {
    }

    public ModifyAudit(Long lastModifiedUserId, String lastModifiedUserName) {
        this.userid = lastModifiedUserId;
        this.username = lastModifiedUserName;
    }

    public ModifyAudit(Audit audit) {
        this(audit.getUserid(), audit.getUsername());
    }

}
