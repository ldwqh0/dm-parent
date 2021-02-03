package com.dm.uap.dingtalk.service;

import java.util.Collection;

import com.dm.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dm.uap.dingtalk.entity.DDepartment;

public interface DDepartmentService {
    /**
     * 保存一条钉钉部门信息
     * 
     * @param dDepartment
     * @return
     */
    DDepartment save(Department dDepartment);

    /**
     * 保存一批钉钉部门信息
     * 
     * @param dDepartments
     * @return
     */
    Collection<DDepartment> save(Collection<Department> dDepartments);

    /**
     * 将钉钉用户信息同步到系统中
     */
    void syncToUap(String corpid);

    /**
     * 尝试清理
     * 
     * @param corpid
     */
    void clear(String corpid);

}
