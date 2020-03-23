package com.dm.uap.dingtalk.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class CorpLongId implements Serializable {

    private static final long serialVersionUID = 2683841054416224926L;

    private String corpId;

    private Long id;

    public CorpLongId(String corpid, Long id) {
        super();
        this.corpId = corpid;
        this.id = id;
    }

    public CorpLongId() {
        super();
    }

}
