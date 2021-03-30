package com.dm.common.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
public class ModifyAudit<ID extends Serializable, NAME extends Serializable> implements Audit<ID, NAME> {

    private static final long serialVersionUID = 5546618897219690297L;

    @Column(name = "last_modified_user_id_")
    private ID userid;

    @Column(name = "last_modified_user_name_")
    private NAME username;

    public ModifyAudit() {
    }

    public ModifyAudit(ID lastModifiedUserId, NAME lastModifiedUserName) {
        this.userid = lastModifiedUserId;
        this.username = lastModifiedUserName;
    }

    public ModifyAudit(Audit<ID, NAME> audit) {
        this(audit.getUserid(), audit.getUsername());
    }

}
