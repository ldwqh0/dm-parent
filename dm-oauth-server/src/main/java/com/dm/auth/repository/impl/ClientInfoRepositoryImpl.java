package com.dm.auth.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.dm.auth.entity.ClientInfo;
import com.dm.auth.entity.QClientInfo;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class ClientInfoRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QClientInfo qClient = QClientInfo.clientInfo;

	public Page<ClientInfo> find(String key, Pageable pageable) {
		JPAQuery<ClientInfo> query = new JPAQuery<ClientInfo>(em);
		query.from(qClient).where(qClient.name.containsIgnoreCase(key));
		Long count = query.fetchCount();
		long offset = pageable.getOffset();
		long limit = pageable.getPageSize();
		query.offset(offset).limit(limit);
		return new PageImpl<ClientInfo>(query.fetch(), pageable, count);
	}

	public Set<String> listScopes() {
		JPAQuery<ClientInfo> query = new JPAQuery<>(em);
		StringPath qScope = Expressions.stringPath("scope");
		List<String> scopes = query
				.select(qScope)
				.from(qClient)
				.join(qClient.scope, qScope)
				.distinct()
				.fetch();
		return new HashSet<>(scopes);
	}

}
