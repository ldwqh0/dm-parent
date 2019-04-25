package com.dm.uap.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.entity.Department;
import com.dm.uap.entity.QUser;
import com.dm.uap.entity.User;
import com.querydsl.jpa.impl.JPAQuery;

public class UserRepositoryImpl {

	@Autowired
	private EntityManager em;

	private final QUser qUser = QUser.user;

	public List<User> findByDepartment(Department department) {
		JPAQuery<User> query = new JPAQuery<>(em);
		return query.select(qUser).from(qUser).where(qUser.posts.containsKey(department)).fetch();
	}
}
