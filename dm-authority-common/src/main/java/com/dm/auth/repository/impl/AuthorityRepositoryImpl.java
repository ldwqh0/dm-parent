package com.dm.auth.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.QAuthority;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class AuthorityRepositoryImpl {

    @Autowired
    private JPAQueryFactory qf;

    private final QAuthority qAuthroity = QAuthority.authority;

    /**
     * 查找某个菜单的权限配置
     * 
     * @param menu
     * @return
     */
    public List<Authority> findByMenu(Menu menu) {
        JPAQuery<Authority> authrityQuery = qf.select(qAuthroity).from(qAuthroity)
                .where(qAuthroity.menus.any().eq(menu));
        return authrityQuery.fetch();
    }

}
