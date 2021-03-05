package com.dm.auth.dto;

import com.dm.auth.entity.ResourceOperation;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 资源授权信息
 *
 * @author LiDong
 */
@Data
public class ResourceAuthorityDto implements Serializable {
    private static final long serialVersionUID = -5060713882698127082L;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色对应的资源操作权限
     */
    private Map<Long, ResourceOperation> resourceAuthorities;
}
