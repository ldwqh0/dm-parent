package com.dm.uap.dingtalk.service;

public interface DRoleService {
    /**
     * 尝试物理删除被删除的角色信息
     * 
     * @param corpid
     */
    public void clear(String corpid);
}
