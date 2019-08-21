package com.dm.uap.dingtalk.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DDepartmentConverter;
import com.dm.uap.dingtalk.entity.DDepartment;
import com.dm.uap.dingtalk.repository.DDepartmentRepository;
import com.dm.uap.dingtalk.service.DDepartmentService;
import com.dm.uap.repository.DepartmentRepository;

@Service
public class DDepartmentServiceImpl implements DDepartmentService {

	@Autowired
	private DingTalkService dingTalkService;

	@Autowired
	private DDepartmentConverter dDepartmentConverter;

	@Autowired
	private DDepartmentRepository dDepartmentRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

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

	private List<DDepartment> fetchDDepartments() {
		List<Department> departments = dingTalkService.fetchDepartments();
		// 删除原有列表中，不存在于本次同步抓取到的部门列表中的数据
		// 如果原有数据中 有部门 [1,2,3],本次抓取的部门有 [2,3,6]
		// 需要先删除原有部门中不存在于本次抓取部门[2,3,6]中的部门[1]
		
		//TODO 不删除部门
//		List<Long> exists = departments.stream().map(Department::getId).collect(Collectors.toList());
//		dDepartmentRepository.deleteByIdNotIn(exists);

		// 将抓取到的数据映射为实体
		List<DDepartment> dDepartments_ = departments.stream()
				.map(_department -> {
					DDepartment result = dDepartmentRepository.existsById(_department.getId())
							? dDepartmentRepository.getOne(_department.getId())
							: dDepartmentRepository.save(new DDepartment(_department.getId()));
					return dDepartmentConverter.copyProperties(result, _department);
				})
				.collect(Collectors.toList());
		return dDepartmentRepository.saveAll(dDepartments_);
	}

	/**
	 * 同步钉钉机构数据库到系统机构数据库 该同步会修改想要的部门数据信息 <br>
	 * 但不会删除已经存在的部门信息
	 */
	private void syncLocalToUap(List<DDepartment> dDepartments) {
		dDepartments.forEach(dDep -> {
			com.dm.uap.entity.Department department = dDep.getDepartment();
			if (Objects.isNull(department)) {
				department = new com.dm.uap.entity.Department();
			}
			dDep.setDepartment(departmentRepository.save(dDepartmentConverter.copyProperties(department, dDep)));
		});
		// 构建系统组织机构的层级关系
		dDepartments.forEach(dDep -> {
			if (!Objects.isNull(dDep.getParentid())) {
				DDepartment dParent = dDepartmentRepository.getOne(dDep.getParentid());
				dDep.getDepartment().setParent(dParent.getDepartment());
			}
		});
	}

	@Override
	@Transactional
	public void syncToUap() {
		syncLocalToUap(fetchDDepartments());
	}
}
