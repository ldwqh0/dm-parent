package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.dingding.model.response.OapiDepartmentListResponse.Department;
import com.dm.uap.dingtalk.entity.DDepartment;

@Component
public class DDepartmentConverter {

	public void copyProperties(DDepartment entity, Department department) {
		entity.setAutoAddUser(department.getAutoAddUser());
		entity.setCreateDeptGroup(department.getCreateDeptGroup());
		entity.setId(department.getId());
		entity.setName(department.getName());
		entity.setParentid(department.getParentid());
		entity.setSourceIdentifier(department.getSourceIdentifier());
	}

	public void copyProperties(com.dm.uap.entity.Department uDepartment, DDepartment dep) {
		// uDepartment.setDescription();
		uDepartment.setFullname(dep.getName());
		// uDepartment.setOrder(order);
		// uDepartment.setParent();
		uDepartment.setShortName(dep.getName());
	}

}
