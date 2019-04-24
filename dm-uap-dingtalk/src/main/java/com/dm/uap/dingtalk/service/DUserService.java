package com.dm.uap.dingtalk.service;

import com.dm.uap.dingtalk.entity.DUser;

public interface DUserService {

	/**
	 * 同步用户信息到本地系统
	 */
	public void sync();

	/**
	 * 根据情况创建或跟新一个钉钉用户信息,<br>
	 * 
	 * 如果在系统中不存在相关的用户信息，则创建一条记录，如果已经存在，则更新已经存在的记录<br>
	 * 
	 * 该方法还会同步的修改钉钉服务器上的相关信息，如果在服务器上不存在相关的用户记录，则创建一个新的用户，如果已经存在，则更新用户信息
	 * 
	 * @param dUser
	 * @return
	 */
	public DUser save(DUser dUser);

//	public Duser save();

}
