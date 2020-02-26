package com.dm.activity.model;

/**
 * 表示动作定义
 * 
 * @author ldwqh0@outlook.com
 *
 */
public interface ActionDefine {
    /**
     * 动作
     * 
     * @return
     */
    public String getName();

    /**
     * 动作的下一部
     * 
     * @param <ID>
     * @return
     */
    public <ID> Step<ID> getNextStep();

    public <ID> ProcessDefinition getProcessDefine();

    public <ID> Step<ID> getStep();
}
