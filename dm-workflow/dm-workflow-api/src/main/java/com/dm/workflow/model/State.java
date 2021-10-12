package com.dm.workflow.model;

/**
 * 表示流程节点
 *
 * @author ldwqh0@outlook.com
 */
public interface State {

    /**
     * 获取节点的ID
     *
     * @return
     */
    Long getId();

    /**
     * 获取节点的名称
     *
     * @return
     */
    String getName();

}
