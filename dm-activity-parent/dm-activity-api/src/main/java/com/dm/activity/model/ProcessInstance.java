package com.dm.activity.model;

/**
 * 一个流程实例
 *
 * @author ldwqh0@outlook.com
 *
 */
public interface ProcessInstance<ID> {

    /**
     * 获取该流程实例对应的流程定义
     *
     * @return
     */
    public ProcessDefinition getProcessDefinition();

    /**
     * 获取流程当前节点
     *
     * @return
     */
    public <StepId> StepDefine<StepId> getStep();

}
