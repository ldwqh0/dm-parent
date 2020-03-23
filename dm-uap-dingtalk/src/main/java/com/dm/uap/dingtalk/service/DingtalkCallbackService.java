package com.dm.uap.dingtalk.service;

import com.dm.dingtalk.api.callback.Event;

public interface DingtalkCallbackService {

    public void onUserAddOrg(Event event);

    public void onUserModifyOrg(Event event);

    public void onUserLeaveOrg(Event event);

    public void onOrgDeptCreate(Event event);

    public void onOrgDeptModify(Event event);

    public void onOrgDeptRemove(Event event);
}
