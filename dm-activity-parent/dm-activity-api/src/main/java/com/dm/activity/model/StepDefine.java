package com.dm.activity.model;

import java.util.List;

/**
 * 表示流程节点
 *
 * @author ldwqh0@outlook.com
 */
public interface StepDefine<ID> {

    /**
     * 获取节点的ID
     *
     * @return
     */
    public ID getId();

    /**
     * 获取节点的名称
     *
     * @return
     */
    public String getName();

    public List<ActionDefine> getActions();
}
