package com.dm.uap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>, QuerydslPredicateExecutor<Department> {

}
