package com.dm.uap.dingtalk.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.dingtalk.entity.DUser;

public interface DUserRepository extends JpaRepository<DUser, String>, QuerydslPredicateExecutor<DUser> {

	public long deleteByIdNotIn(Set<String> userIds);

}
