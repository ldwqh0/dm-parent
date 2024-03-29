package com.dm.workflow.service;

import com.dm.workflow.model.ProcessInstance;
import com.dm.workflow.model.State;

/**
 * 流程引擎
 *
 * @author ldwqh0@outlook.com
 */

public interface ProcessService {

    /**
     * 将流程跳转到指定步骤<br >
     * 该过程会检查流程定义，确定是否可以跳转到指定步骤，如果不能跳转，会抛出异常
     *
     * @param step
     * @return
     */
    ProcessInstance goTo(ProcessInstance instance, State state);

}
