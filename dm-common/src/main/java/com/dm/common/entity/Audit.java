package com.dm.common.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 审计信息对象模型，包含用户ID和用户姓名
 * 
 * @author LiDong
 *
 */
public interface Audit extends Serializable {

    public Long getUserid();

    public String getUsername();

    public static Audit of(Long userid, String username) {
        return new SimpleAuditImpl(userid, username);
    }

}

@Data
class SimpleAuditImpl implements Audit, Serializable {
    private final Long userid;
    private final String username;

    private static final long serialVersionUID = 7129258376953572142L;

    public SimpleAuditImpl() {
        this(null, null);
    }

    public SimpleAuditImpl(Long userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public SimpleAuditImpl(Audit audit) {
        this(audit.getUserid(), audit.getUsername());
    }
}
