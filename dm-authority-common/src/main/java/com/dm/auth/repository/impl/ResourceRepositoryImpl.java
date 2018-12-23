package com.dm.auth.repository.impl;

import java.util.List;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.swing.JApplet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dm.auth.entity.QResource;
import com.dm.auth.entity.Resource;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class ResourceRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QResource qResource = QResource.resource;

	public List<String> listScopes() {
		JPAQuery<?> query = new JPAQuery<>(em);
		StringPath scope = Expressions.stringPath("scopes");
		return query.select(scope)
				.from(qResource)
				.join(qResource.scope, scope)
				.distinct()
				.fetch();
	}
}
