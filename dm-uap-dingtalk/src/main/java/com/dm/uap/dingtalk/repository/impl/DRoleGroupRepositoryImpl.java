package com.dm.uap.dingtalk.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.dingtalk.entity.QDRoleGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class DRoleGroupRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QDRoleGroup qDRoleGroup = QDRoleGroup.dRoleGroup;

	public void deleteByIdNotIn(List<Long> collect) {
		JPAQueryFactory jf = new JPAQueryFactory(em);
		jf.delete(qDRoleGroup).where(qDRoleGroup.id.notIn(collect)).execute();
	}
}
