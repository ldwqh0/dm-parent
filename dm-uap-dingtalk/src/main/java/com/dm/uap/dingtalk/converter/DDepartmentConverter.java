package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dm.uap.dingtalk.entity.DDepartment;

@Component
public class DDepartmentConverter {

    public DDepartment copyProperties(DDepartment entity, Department department) {
        entity.setAutoAddUser(department.getAutoAddUser());
        entity.setCreateDeptGroup(department.getCreateDeptGroup());
        entity.setName(department.getName());
        entity.setParentid(department.getParentid());
        entity.setSourceIdentifier(department.getSourceIdentifier());
        return entity;
    }

    public com.dm.uap.entity.Department copyProperties(com.dm.uap.entity.Department uDepartment, DDepartment dep) {
        // uDepartment.setDescription();
        uDepartment.setFullname(dep.getName());
        // uDepartment.setOrder(order);
        // uDepartment.setParent();
        uDepartment.setShortname(dep.getName());
//        uDepartment
        return uDepartment;
    }

}
