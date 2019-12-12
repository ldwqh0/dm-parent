package com.dm.uap.dingtalk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.uap.dingtalk.entity.DDepartment;

public interface DDepartmentRepository extends JpaRepository<DDepartment, Long> {

    public void deleteByIdNotIn(List<Long> ids);

    /**
     * 根据部门信息查找相关的钉钉部门信息
     * 
     * @param id
     * @return
     */
    public Optional<DDepartment> findByDepartmentId(Long id);

}
