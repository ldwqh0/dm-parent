package com.dm.uap.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 资源授权信息
 * 
 * @author LiDong
 *
 */
@Data
public class ResourceAuthorityDto implements Serializable {

	private static final long serialVersionUID = -5060713882698127082L;

	private Long roleId;
	private List<ResourceOperationDto> resourceAuthorities = new ArrayList<ResourceOperationDto>();
}
