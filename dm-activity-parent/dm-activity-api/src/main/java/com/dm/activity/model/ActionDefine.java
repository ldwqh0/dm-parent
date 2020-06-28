package com.dm.activity.model;

import java.security.Principal;
import java.util.List;

/**
 * 表示动作定义
 *
 * @author ldwqh0@outlook.com
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
    public <ID> StepDefine<ID> getNextStep();

    public <ID> ProcessDefinition getProcessDefine();

    public <ID> StepDefine<ID> getStep();

    // 制定可以执行该动作的人员
    public List<Principal> getPrincipal();
}
