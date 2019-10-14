package com.dm.uap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.uap.entity.LoginLog;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

}
