package com.dm.data.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SimpleAuditor<ID extends Serializable, NAME extends Serializable> implements Auditor<ID, NAME>, Serializable {
    private static final long serialVersionUID = 7129258376953572142L;
    private ID userid;
    private NAME username;

    public SimpleAuditor() {

    }

    public SimpleAuditor(ID userid, NAME username) {
        this.userid = userid;
        this.username = username;
    }

    private void setUserid(ID userid) {
        this.userid = userid;
    }

    private void setUsername(NAME username) {
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
