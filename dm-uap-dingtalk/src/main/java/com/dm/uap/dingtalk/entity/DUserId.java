package com.dm.uap.dingtalk.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class DUserId implements Serializable {

    private static final long serialVersionUID = 1901138623178602506L;

    private String corpId;

    private String userid;

    public DUserId(String corpId, String userid) {
        super();
        this.corpId = corpId;
        this.userid = userid;
    }

    public DUserId() {
        super();
    }

}
