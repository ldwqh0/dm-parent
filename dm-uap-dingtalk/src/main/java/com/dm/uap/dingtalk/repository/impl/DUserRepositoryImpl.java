package com.dm.uap.dingtalk.repository.impl;

import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.dingtalk.entity.QDUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class DUserRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QDUser qDUser = QDUser.dUser;

	public long deleteByIdNotIn(Set<String> userIds) {
		JPAQueryFactory fac = new JPAQueryFactory(em);
		return fac.delete(qDUser).where(
				qDUser.userid.notIn(userIds)).execute();
	}
}
