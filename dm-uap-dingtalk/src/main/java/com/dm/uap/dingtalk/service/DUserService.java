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
    public void syncToUap(String corpid);

    /**
     * 启动异步同步信息，并返回日志
     * 
     * @param corpid 要同步用户的企业
     * 
     * @return
     */
    public CompletableFuture<DdSyncLog> asyncToUap(String corpid);

    /**
     * 同步单个用户的信息到系统
     * 
     * @param userid
     * @return
     */
    public DUser syncToUap(String corpid, String userid);

    /**
     * 
     * 
     * 异步的同步某个用户的信息
     * 
     * @param corpid 要同步的企业id
     * @param userid 要同步的用户id
     * @return
     */
    public CompletableFuture<DUser> asyncToUap(String corpid, String userid);

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
    public DUser save(DUser dUser);

    public Optional<DUser> findByUserid(String corpid, String userid);

    public Optional<DUser> findById(DUserId id);

    /**
     * 将指定的用户标记为删除
     * 
     * @param userId
     * @return
     */
    public DUser markDeleted(String corpid, String userId);

    /**
     * 物理删除指定的用户信息
     * 
     * @param userId
     */
    public void delete(String corpid, String userId);

}
