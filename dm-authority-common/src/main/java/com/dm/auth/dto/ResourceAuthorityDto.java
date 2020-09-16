package com.dm.auth.dto;

import java.io.Serializable;
import java.util.Map;
import com.dm.auth.entity.ResourceOperation;

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
    private String roleName;
    private Map<Long, ResourceOperation> resourceAuthorities;
}
