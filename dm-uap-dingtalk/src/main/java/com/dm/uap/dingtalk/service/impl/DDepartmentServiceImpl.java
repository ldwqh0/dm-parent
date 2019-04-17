package com.dm.uap.dingtalk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dm.dingding.model.response.OapiDepartmentListResponse.Department;
import com.dm.dingding.service.DingTalkService;
import com.dm.uap.dingtalk.entity.DDepartment;
import com.dm.uap.dingtalk.service.DDepartmentService;

@Service
public class DDepartmentServiceImpl implements DDepartmentService {

	@Autowired
	private DingTalkService dingTalkService;

	@Async
	public void sync() {

	}

	@Override
	public DDepartment save(Department dDepartment) {
		// TODO Auto-generated method stub
		return null;
	}
}
