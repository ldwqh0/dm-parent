package com.dm.uap.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.uap.entity.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends IdentifiableDtoRepository<Department, Long>, QuerydslPredicateExecutor<Department> {

    @Query("select count (dp) from Department dp where dp.parent=:parent")
    Long childrenCounts(@Param("parent") Department parent);

}
