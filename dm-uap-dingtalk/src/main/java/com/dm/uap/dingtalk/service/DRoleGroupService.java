package com.dm.uap.dingtalk.service;

public interface DRoleGroupService {

    public void syncToUap(String corpid);

    public void deleteById(String corpid, Long id);

    /**
     * 尝试物理删除角色组信息
     * 
     * @param corpid
     */
    public void clear(String corpid);

}
