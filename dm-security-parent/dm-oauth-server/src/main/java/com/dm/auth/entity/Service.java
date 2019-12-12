package com.dm.auth.entity;

import javax.persistence.Column;

import com.dm.common.entity.AbstractEntity;

public class Service extends AbstractEntity {
    private static final long serialVersionUID = 1244091886684502338L;

    @Column(name = "url_")
    private String url;

    @Column(name = "name_")
    private String urlMatcher;

    @Column(name = "memo_")
    private String memo;

}
