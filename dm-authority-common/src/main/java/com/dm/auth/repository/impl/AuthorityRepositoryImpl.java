package com.dm.auth.repository.impl;

import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.QAuthority;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class AuthorityRepositoryImpl {


    private final JPAQueryFactory queryFactory;

    private final QAuthority qAuthority = QAuthority.authority;

    public AuthorityRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 查找某个菜单的权限配置
     *
     * @param menu 要查找的菜单项目
     * @return 权限配置列表
     */
    public List<Authority> findByMenu(Menu menu) {
        JPAQuery<Authority> query = queryFactory.select(qAuthority).from(qAuthority)
            .where(qAuthority.menus.any().eq(menu));
        return query.fetch();
    }

}
