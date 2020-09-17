package com.dm.uap.dingtalk.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
public class CorpLongId implements Serializable {

    private static final long serialVersionUID = 2683841054416224926L;

    private final String corpId;

    private final Long id;

    public CorpLongId(String corpId, Long id) {
        this.corpId = corpId;
        this.id = id;
    }

}
