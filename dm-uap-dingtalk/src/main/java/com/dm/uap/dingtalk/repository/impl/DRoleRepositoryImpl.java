package com.dm.uap.dingtalk.repository.impl;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.dingtalk.entity.QDRole;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class DRoleRepositoryImpl {
	@Autowired
	private EntityManager em;

	private final QDRole qDrole = QDRole.dRole;

	public void deleteByIdNotIn(Collection<Long> ids) {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		qf.delete(qDrole).where(qDrole.id.notIn(ids)).execute();
	}

}
