package com.dm.workflow.service;

import com.dm.workflow.model.ProcessDefinition;

/**
 * 对流程定义的操作
 *
 * @author ldwqh0@outlook.com
 */
public interface ProcessDefineService {

    /**
     * 查找流程定义
     *
     * @param id
     * @return
     */
    ProcessDefinition findById(Long id);

}
