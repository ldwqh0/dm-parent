package com.dm.auth.repository.impl;

import com.dm.auth.entity.QAuthResource;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class ResourceRepositoryImpl {

    private final QAuthResource qResource = QAuthResource.authResource;

    private final JPAQueryFactory queryFactory;

    public ResourceRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
// todo 需要测试一下是不是可以注释
//    public List<String> listScopes() {
//        StringPath scope = Expressions.stringPath("scopes");
//        return queryFactory.select(scope)
//            .from(qResource)
//            .join(qResource.scope, scope)
//            .distinct()
//            .fetch();
//    }
}
