package com.dm.uap.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.dm.uap.entity.QUser;
import com.dm.uap.entity.User;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class UserRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QUser qUser = QUser.user;

	public Page<User> find(String key, Pageable pageable) {
		JPAQuery<User> query = new JPAQuery<User>(em);
		query.select(qUser).from(qUser);
		query.where(qUser.username.containsIgnoreCase(key)
				.or(qUser.fullname.containsIgnoreCase(key))
				.or(qUser.mobile.containsIgnoreCase(key))
				.or(qUser.email.containsIgnoreCase(key)));
		long total = query.fetchCount();
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());
		List<User> content = query.fetch();
		return new PageImpl<User>(content, pageable, total);
	}
}
