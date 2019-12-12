package com.dm.uap.dingtalk.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.dingtalk.entity.QDDepartment;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class DDepartmentRepositoryImpl {

    @Autowired
    public EntityManager em;
    private final QDDepartment qdDepartment = QDDepartment.dDepartment;

    public void deleteByIdNotIn(List<Long> ids) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory.delete(qdDepartment).where(qdDepartment.id.notIn(ids)).execute();
    }
}
