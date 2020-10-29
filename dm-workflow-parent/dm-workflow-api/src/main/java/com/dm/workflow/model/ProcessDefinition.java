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
    Long getId();

    /**
     * 获取流程定义名称
     * 
     * @return
     */
    String getName();

    /**
     * 获取流程节点
     * 
     * @return
     */
    List<State> getSteps();

}
