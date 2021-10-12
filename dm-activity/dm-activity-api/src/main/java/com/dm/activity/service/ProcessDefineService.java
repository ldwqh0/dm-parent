package com.dm.activity.service;

import com.dm.activity.model.ProcessDefinition;

/**
 * 对流程定义的操作
 *
 * @author ldwqh0@outlook.com
 */
public interface ProcessDefineService<ID> {

    /**
     * 查找流程定义
     *
     * @param id
     * @return
     */
    ProcessDefinition findById(ID id);

}
