package com.dm.workflow.model;

import java.util.List;

/**
 * 流程定义
 * 
 * @author ldwqh0@outlook.com
 *
 */
public interface ProcessDefinition {

    /**
     * 获取流程定义的ID
     * 
     * @return
     */
    public Long getId();

    /**
     * 获取流程定义名称
     * 
     * @return
     */
    public String getName();

    /**
     * 获取流程节点
     * 
     * @return
     */
    public List<State> getSteps();

}
