package com.dm.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 审计信息对象模型，包含用户ID和用户姓名
 *
 * @author LiDong
 */
public interface Audit extends Serializable {

    Long getUserid();

    String getUsername();

    static Audit of(Long userid, String username) {
        return new SimpleAuditImpl(userid, username);
    }

    static Audit of(Audit audit) {
        return new SimpleAuditImpl(audit.getUserid(), audit.getUsername());
    }

}

@Data
class SimpleAuditImpl implements Audit, Serializable {
    private final Long userid;
    private final String username;
    private static final long serialVersionUID = 7129258376953572142L;
}
