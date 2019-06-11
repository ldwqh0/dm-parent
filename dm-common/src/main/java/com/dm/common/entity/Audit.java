package com.dm.common.entity;

import java.io.Serializable;

/**
 * 审计信息对象模型，包含用户ID和用户姓名
 * 
 * @author LiDong
 *
 */
public interface Audit extends Serializable {

	public Long getUserid();

	public String getUsername();
}
