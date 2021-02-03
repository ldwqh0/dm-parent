package com.dm.uap.dingtalk.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class DUserId implements Serializable {

    private static final long serialVersionUID = 1901138623178602506L;

    private String corpId;

    private String unionid;

    DUserId(String corpId, String unionid) {
        this.corpId = corpId;
        this.unionid = unionid;
    }

    public static DUserId of(String corpId, String unionid) {
        return new DUserId(corpId, unionid);
    }

    DUserId() {
    }

}
