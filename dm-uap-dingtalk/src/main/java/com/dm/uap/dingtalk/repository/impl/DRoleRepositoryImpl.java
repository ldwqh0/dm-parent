package com.dm.uap.dingtalk.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.dingtalk.entity.QDRole;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class DRoleRepositoryImpl {

    @Autowired
    private JPAQueryFactory jqf;

    private final QDRole qDRole = QDRole.dRole;

    public List<Long> findRoleIdByDRoleDeleted(boolean deleted) {
        return jqf.select(qDRole.role.id).from(qDRole).where(qDRole.deleted.eq(deleted)).fetch();
    }
}
