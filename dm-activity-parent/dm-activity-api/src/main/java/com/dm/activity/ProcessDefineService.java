package com.dm.activity;

/**
 * 对流程定义的操作
 * 
 * @author ldwqh0@outlook.com
 *
 */
public interface ProcessDefineService<ID> {

    /**
     * 查找流程定义
     * 
     * @param id
     * @return
     */
    public ProcessDefinition findById(ID id);

}
