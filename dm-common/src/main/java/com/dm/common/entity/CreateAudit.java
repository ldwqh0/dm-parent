package com.dm.common.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
public class CreateAudit<ID extends Serializable, NAME extends Serializable> implements Audit<ID, NAME> {

    private static final long serialVersionUID = 379407708683930698L;

    /**
     * 创建人相关信息不能被修改
     */
    @Column(name = "created_user_id_", updatable = false)
    private ID userid;

    /**
     * 创建人相关信息不能被修改
     */
    @Column(name = "created_user_name_", updatable = false)
    private NAME username;

    public CreateAudit(ID createUserId, NAME createUserName) {
        super();
        this.userid = createUserId;
        this.username = createUserName;
    }

    public CreateAudit() {
    }

    public CreateAudit(Audit<ID, NAME> audit) {
        this(audit.getUserid(), audit.getUsername());
    }

}
