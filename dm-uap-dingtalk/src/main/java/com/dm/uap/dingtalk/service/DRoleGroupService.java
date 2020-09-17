package com.dm.uap.dingtalk.service;

public interface DRoleGroupService {

    void syncToUap(String corpid);

    void deleteById(String corpid, Long id);

    /**
     * 尝试物理删除角色组信息
     * 
     * @param corpid
     */
    void clear(String corpid);

}
