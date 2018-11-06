package com.dm.auth.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dm.auth.entity.QUserApproval;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ClientInfoRepositoryImpl {

	@Autowired
	private EntityManager em;

	private QUserApproval qUserApproval = QUserApproval.userApproval;

	public void deleteByClientId(String clientId) {
		JPAQueryFactory fac = new JPAQueryFactory(em);
		fac.delete(qUserApproval).where(qUserApproval.clientId.eq(clientId)).execute();
	}

}
