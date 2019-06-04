package com.dm.uap.entity.audit;

import java.io.Serializable;

/**
 * 审计信息
 * 
 * @author LiDong
 *
 */
public interface Audit extends Serializable {

	public Long getUserid();

	public String getUsername();
}
