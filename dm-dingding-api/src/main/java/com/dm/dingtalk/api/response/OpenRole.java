package com.dm.dingtalk.api.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class OpenRole implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7034570754033834272L;

    /**
     * 角色id
     */
    private Long id;

    private String corpId;

    /**
     * 角色组id
     */
    private Long groupId;
    /**
     * 角色名称
     */
    private String name;

}
