package com.dm.activity;

import java.util.List;

public interface TaskRepository<ID> {

    /**
     * 获取所有的任务
     * @return
     */
    public List<Task> listTask();
    
    
    
}
