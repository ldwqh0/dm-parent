package com.dm.uap.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.uap.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends IdentifiableDtoRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Optional<User> findOneByUsernameIgnoreCase(String username);

    Optional<User> findByMobileIgnoreCase(String mobile);

}
