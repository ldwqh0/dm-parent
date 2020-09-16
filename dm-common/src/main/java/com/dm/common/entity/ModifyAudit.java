package com.dm.common.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ModifyAudit implements Audit {

    private static final long serialVersionUID = 5546618897219690297L;

    @Column(name = "last_modified_user_id_")
    private Long userid;

    @Column(name = "last_modified_user_name_")
    private String username;

    public ModifyAudit() {
        super();
    }

    public ModifyAudit(Long lastModifiedUserId, String lastModifiedUserName) {
        super();
        this.userid = lastModifiedUserId;
        this.username = lastModifiedUserName;
    }

    public ModifyAudit(Audit audit) {
        super();
        this.userid = audit.getUserid();
        this.username = audit.getUsername();
    }

    @Override
    public Long getUserid() {
        return this.userid;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
