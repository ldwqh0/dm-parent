package com.dm.uap.dingtalk.service;

public interface DRoleGroupService {

    public void syncToUap(String corpid);

    public void deleteById(String corpid, Long id);

}
