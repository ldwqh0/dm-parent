package com.dm.auth.repository.impl;

import com.dm.auth.entity.Menu;
import com.dm.auth.entity.QMenu;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class MenuRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    private final QMenu qMenu = QMenu.menu;

    public List<Menu> findByEnabledAndParentId(Boolean enabled, Long parentId, Sort sort) {
        BooleanBuilder query = new BooleanBuilder();
        if (!Objects.isNull(enabled)) {
            query.and(qMenu.enabled.eq(enabled));
        }
        if (!Objects.isNull(parentId)) {
            query.and(qMenu.parent.id.eq(parentId));
        }
        return queryFactory.select(qMenu).from(qMenu).where(query).fetch();
    }
}
