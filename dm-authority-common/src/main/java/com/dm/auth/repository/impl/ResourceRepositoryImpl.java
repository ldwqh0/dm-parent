package com.dm.auth.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.auth.entity.QAuthResource;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ResourceRepositoryImpl {

    private final QAuthResource qResource = QAuthResource.authResource;

    @Autowired
    private JPAQueryFactory jpf;

    public List<String> listScopes() {
        StringPath scope = Expressions.stringPath("scopes");
        return jpf.select(scope)
                .from(qResource)
                .join(qResource.scope, scope)
                .distinct()
                .fetch();
    }
}
