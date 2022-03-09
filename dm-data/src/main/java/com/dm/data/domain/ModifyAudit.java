package com.dm.data.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ModifyAudit implements Audit<Long, String>, Serializable, Cloneable {

    private static final long serialVersionUID = -6472426570089325611L;

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

    @Override
    public ModifyAudit clone() {
        try {
            return (ModifyAudit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
