package com.dm.uap.dto;

import com.dm.common.entity.Audit;

import lombok.Data;

@Data
public class AuditDto implements Audit {

	private final Long userid;
	private final String username;

	private static final long serialVersionUID = 7129258376953572142L;

}
