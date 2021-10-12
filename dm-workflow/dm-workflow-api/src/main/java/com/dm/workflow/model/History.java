package com.dm.workflow.model;

import java.time.ZonedDateTime;

/**
 * 一次变流程历史
 *
 * @author ldwqh0@outlook.com
 */
public interface History {

    /**
     * 获取该操作对应的流程实例
     *
     * @return
     */
    ProcessInstance getProcessInstance();

    /**
     * 获取该操作的流程开始节点
     *
     * @return
     */
    String getFrom();

    /**
     * 获取该操作的流程结束节点
     *
     * @return
     */
    String getTo();

    /**
     * 操作产生的时间
     *
     * @return
     */
    ZonedDateTime getCreatedDate();

}
