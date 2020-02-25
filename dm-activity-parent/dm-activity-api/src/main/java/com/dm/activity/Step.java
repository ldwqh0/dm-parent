package com.dm.activity;

import java.util.List;

/**
 * 表示流程节点
 * 
 * @author ldwqh0@outlook.com
 *
 */
public interface Step<ID> {

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

    /**
     * 获取节点的可跳转列表
     * 
     * @return
     */
    public List<Step<ID>> getNext();

}
