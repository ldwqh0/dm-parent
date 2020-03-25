package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dm.uap.dingtalk.entity.DDepartment;

public interface DDepartmentRepository extends JpaRepository<DDepartment, Long> {

    /**
     * 根据部门信息查找相关的钉钉部门信息
     * 
     * @param id
     * @return
     */
    public Optional<DDepartment> findByDepartmentId(Long id);

    @Query("update DDepartment set deleted=true where deleted !=true or deleted is null and id not in (?1)")
    @Modifying
    public int setDeletedByIdNotIn(Collection<Long> ids);

}
