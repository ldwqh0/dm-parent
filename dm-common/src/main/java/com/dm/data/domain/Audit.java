package com.dm.data.domain;


import java.io.Serializable;

/**
 * 审计信息对象模型，包含用户ID和用户姓名
 *
 * @author LiDong
 */
public interface Audit<ID extends Serializable, NAME extends Serializable> extends Serializable {

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

    static <ID extends Serializable, NAME extends Serializable> Audit<ID, NAME> of(ID userid, NAME username) {
        return new SimpleAuditImpl<>(userid, username);
    }

    static <ID extends Serializable, NAME extends Serializable> Audit<ID, NAME> of(Audit<ID, NAME> audit) {
        return new SimpleAuditImpl<>(audit.getUserid(), audit.getUsername());
    }

}

class SimpleAuditImpl<ID extends Serializable, NAME extends Serializable> implements Audit<ID, NAME>, Serializable {
    private static final long serialVersionUID = 7129258376953572142L;
    private final ID userid;
    private final NAME username;

    public SimpleAuditImpl(ID userid, NAME username) {
        this.userid = userid;
        this.username = username;
    }

    @Override
    public ID getUserid() {
        return userid;
    }

    @Override
    public NAME getUsername() {
        return username;
    }
}
