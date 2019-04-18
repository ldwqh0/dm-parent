package com.dm.uap.dingtalk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.uap.dingtalk.entity.DDepartment;

public interface DDepartmentRepository extends JpaRepository<DDepartment, Long> {

	
	public void deleteByIdNotIn(List<Long> ids);

}
