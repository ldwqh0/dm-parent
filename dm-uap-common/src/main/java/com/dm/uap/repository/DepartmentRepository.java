package com.dm.uap.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.uap.entity.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends IdentifiableDtoRepository<Department, Long>, QuerydslPredicateExecutor<Department> {

    /**
     * 查询某个部门子部门的个数
     *
     * @param parent 要查询子部门的部门
     * @return 子部门的个数
     */
    @Query("select count (dp) from Department dp where dp.parent=:parent")
    Long childrenCounts(@Param("parent") Department parent);

    /**
     * 查询某个部门的人数
     *
     * @param department 要查询人数的部门
     * @return 该部门的人数
     */
    @Query("select count (u) from User u join u.posts p where key(p) =:department")
    Long userCounts(@Param("department") Department department);

}
