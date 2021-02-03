package com.dm.uap.dingtalk.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.dm.uap.dingtalk.entity.DUser;
import com.dm.uap.dingtalk.entity.DUserId;
import com.dm.uap.dingtalk.entity.DdSyncLog;

public interface DUserService {

    /**
     * 同步用户信息到本地系统<br>
     * 
     * 在同步用户之前，会先同步组织机构信息和角色信息
     */
    void syncToUap(String corpid);

    /**
     * 启动异步同步信息，并返回日志
     * 
     * @param corpid 要同步用户的企业
     * 
     * @return
     */
    CompletableFuture<DdSyncLog> asyncToUap(String corpid);

    /**
     * 同步单个用户的信息到系统
     * 
     * @param userid
     * @return
     */
    DUser syncToUap(String corpid, String userid);

    /**
     * 
     * 
     * 异步的同步某个用户的信息
     * 
     * @param corpid 要同步的企业id
     * @param userid 要同步的用户id
     * @return
     */
    CompletableFuture<DUser> asyncToUap(String corpid, String userid);

    /**
     * 根据情况创建或更新一个钉钉用户信息,<br>
     * 
     * 如果在系统中不存在相关的用户信息，则创建一条记录，如果已经存在，则更新已经存在的记录<br>
     * 
     * 该方法还会同步的修改钉钉服务器上的相关信息，如果在服务器上不存在相关的用户记录，则创建一个新的用户，如果已经存在，则更新用户信息
     * 
     * @param dUser
     * @return
     */
    DUser save(DUser dUser);

    Optional<DUser> findByUserid(String corpid, String userid);

    Optional<DUser> findById(DUserId id);

    /**
     * 删除指定的用户信息 <br>
     * 
     * 如果删除失败，则将用户标记为删除，即逻辑删除
     * 
     * @param userId
     */
    void delete(String corpid, String unionid);

    /**
     * 清理数据，尝试物理删除标记为删除的用户
     * 
     * @param corpid
     */
    void clear(String corpid);

}
