package com.dm.uap.dingtalk.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.dingtalk.entity.QDUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class DUserRepositoryImpl {
    @Autowired
    private JPAQueryFactory jqf;

    private final QDUser qDuser = QDUser.dUser;

    public List<Long> findUserIdsByDUserDeleted(boolean b) {
        return jqf.select(qDuser.user.id).from(qDuser).where(qDuser.deleted.eq(b)).fetch();
    }
}
