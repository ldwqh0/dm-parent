package com.dm.common.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class CreateAudit implements Audit<Long, String> {


    /**
     * 创建人相关信息不能被修改
     */
    @Column(name = "created_user_id_", updatable = false)
    private Long userid;

    /**
     * 创建人相关信息不能被修改
     */
    @Column(name = "created_user_name_", updatable = false)
    private String username;

    public CreateAudit(Long createUserId, String createUserName) {
        super();
        this.userid = createUserId;
        this.username = createUserName;
    }

    public CreateAudit() {
    }

    public CreateAudit(Audit<Long, String> audit) {
        this(audit.getUserid(), audit.getUsername());
    }

}
