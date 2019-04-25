package com.dm.uap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

	public Optional<User> findOneByUsernameIgnoreCase(String username);

	public List<User> findByDepartment(Department dp);

}
