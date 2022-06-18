package com.dm.data.domain;

import java.io.Serializable;

/**
 * 审计信息对象模型，包含用户ID和用户姓名
 *
 * @author LiDong
 */
public interface Auditor<ID extends Serializable, NAME extends Serializable> extends Serializable {

    /**
     * 用户ID
     *
     * @return ID
     */
    ID getUserid();

    /**
     * 用户名
     *
     * @return 用户名
     */
    NAME getUsername();

    static <ID extends Serializable, NAME extends Serializable> Auditor<ID, NAME> of(ID userid, NAME username) {
        return new SimpleAuditor<>(userid, username);
    }

    static <ID extends Serializable, NAME extends Serializable> Auditor<ID, NAME> from(Auditor<ID, NAME> auditor) {
        return new SimpleAuditor<>(auditor.getUserid(), auditor.getUsername());
    }

}


