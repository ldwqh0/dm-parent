package com.dm.uap.dingtalk.service;

import com.dm.dingtalk.api.callback.Event;

public interface DingtalkCallbackService {

    void onUserAddOrg(Event event);

    void onUserModifyOrg(Event event);

    void onUserLeaveOrg(Event event);

    void onOrgDeptCreate(Event event);

    void onOrgDeptModify(Event event);

    void onOrgDeptRemove(Event event);

    void onRoleAdd(Event event);

    void onRoleDel(Event event);

    void onRoleModify(Event event);
}
