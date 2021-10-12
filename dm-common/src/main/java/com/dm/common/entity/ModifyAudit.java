package com.dm.common.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ModifyAudit implements Audit<Long, String> {


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

    public ModifyAudit(Audit<Long, String> audit) {
        this(audit.getUserid(), audit.getUsername());
    }


    @Override
    public Long getUserid() {
        return userid;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
