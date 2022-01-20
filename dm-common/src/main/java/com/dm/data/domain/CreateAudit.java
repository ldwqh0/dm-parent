package com.dm.data.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CreateAudit implements Audit<Long, String>, Serializable, Cloneable {
    private static final long serialVersionUID = -6472426570089325611L;

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

    @Override
    public Long getUserid() {
        return userid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public CreateAudit clone() {
        try {
            return (CreateAudit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
