package com.dm.workflow.model;

import java.util.List;

/**
 * 一个流程实例
 * 
 * @author ldwqh0@outlook.com
 *
 */
public interface ProcessInstance {

    /**
     * 流程实例ID
     * 
     * @return
     */
    Long getId();

    /**
     * 获取该流程实例对应的流程定义
     * 
     * @return
     */
    ProcessDefinition getProcessDefinition();

    List<History> getHistories();

    /**
     * 获取流程当前节点
     * 
     * @return
     */
    State getState();

}
