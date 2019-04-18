package com.dm.uap.dingtalk.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.dingding.model.response.OapiDepartmentListResponse.Department;
import com.dm.dingding.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DDepartmentConverter;
import com.dm.uap.dingtalk.entity.DDepartment;
import com.dm.uap.dingtalk.repository.DDepartmentRepository;
import com.dm.uap.dingtalk.service.DDepartmentService;

@Service
public class DDepartmentServiceImpl implements DDepartmentService {

	@Autowired
	private DingTalkService dingTalkService;

	@Autowired
	private DDepartmentConverter dDepartmentConverter;

	@Autowired
	private DDepartmentRepository dDepartmentRepository;

	@Autowired
	private DDepartmentService dDepartmentService;

	@Override
	@Transactional
	public DDepartment save(Department dDepartment) {
		DDepartment dep_ = new DDepartment();
		dDepartmentConverter.copyProperties(dep_, dDepartment);
		return dDepartmentRepository.save(dep_);
	}

	@Override
	@Transactional
	public Collection<DDepartment> save(Collection<Department> dDepartments) {
		return dDepartments.stream().map(this::save).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void fetchDDepartments() {
		List<Department> departments = dingTalkService.fetchDepartments();
		dDepartmentService.save(departments);
	}

	@Async
	@Override
	@Transactional
	public void syncToUap() {
		dDepartmentService.fetchDDepartments();
		// TODO Auto-generated method stub

	}
}
