package com.dm.uap.dingtalk.service;

import java.util.concurrent.CompletableFuture;

import com.dm.uap.dingtalk.entity.DRole;

public interface DRoleService {
    /**
     * 尝试物理删除被删除的角色信息
     *
     * @param corpid
     */
    void clear(String corpid);

    void delete(String corpid, Long id);

    /**
     * 异步的同步指定角色到本地存储和本地用户
     *
     * @param corpid
     * @param roleId
     * @return
     */
    CompletableFuture<DRole> asyncToUap(String corpid, Long roleId);
}
