package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.dingding.model.response.OapiDepartmentListResponse.Department;
import com.dm.uap.dingtalk.entity.DDepartment;

@Component
public class DDepartmentConverter {

	public void copyProperties(DDepartment entity, Department department) {
	}

}
