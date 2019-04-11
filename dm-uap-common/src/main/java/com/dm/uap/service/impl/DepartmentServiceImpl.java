package com.dm.uap.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.DepartmentConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QDepartment;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.service.DepartmentService;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final QDepartment qDepartment = QDepartment.department;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DepartmentConverter departmentConverter;

	@Override
	@Transactional
	public Department save(DepartmentDto data) {
		Department department = new Department();
		departmentConverter.copyProperties(department, data);
		DepartmentDto parent = data.getParent();
		if (!Objects.isNull(parent)) {
			department.setParent(departmentRepository.getOne(parent.getId()));
		}
		return departmentRepository.save(department);
	}

	@Override
	public Optional<Department> findById(Long id) {
		return departmentRepository.findById(id);
	}

	@Override
	@Transactional
	public Department update(Long id, DepartmentDto data) {
		Department department = departmentRepository.getOne(id);
		departmentConverter.copyProperties(department, data);
		DepartmentDto parent = data.getParent();
		if (!Objects.isNull(parent)) {
			department.setParent(departmentRepository.getOne(parent.getId()));
		}
		return departmentRepository.save(department);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		departmentRepository.deleteById(id);
	}

	@Override
	public Page<Department> find(String key, Pageable pageable) {
		if (StringUtils.isBlank(key)) {
			return departmentRepository.findAll(pageable);
		} else {
			BooleanExpression query = qDepartment.name.containsIgnoreCase(key)
					.or(qDepartment.description.containsIgnoreCase(key));
			return departmentRepository.findAll(query, pageable);
		}
	}

}
