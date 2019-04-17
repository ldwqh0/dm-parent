package com.dm.uap.dingtalk.service;

import com.dm.dingding.model.response.OapiDepartmentListResponse.Department;
import com.dm.uap.dingtalk.entity.DDepartment;

public interface DDepartmentService {

	public DDepartment save(Department dDepartment);

}
