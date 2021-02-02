package com.dm.auth.repository.impl;

import java.util.List;

import com.dm.auth.entity.Menu;
import com.dm.auth.entity.QRole;
import com.dm.auth.entity.Role;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class RoleRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    private final QRole qRole = QRole.role;

    public RoleRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 查找某个菜单的权限配置
     *
     * @param menu 要查找的菜单项目
     * @return 权限配置列表
     */
    public List<Role> findByMenu(Menu menu) {
        JPAQuery<Role> query = queryFactory.select(qRole).from(qRole)
                .where(qRole.menus.any().eq(menu));
        return query.fetch();
    }

}
