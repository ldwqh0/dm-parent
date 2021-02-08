package com.dm.uap.repository;

import com.dm.uap.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long>, QuerydslPredicateExecutor<LoginLog> {

}
