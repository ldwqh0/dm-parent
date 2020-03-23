package com.dm.uap.dingtalk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.dingtalk.api.callback.Event;
import com.dm.uap.dingtalk.service.DUserService;
import com.dm.uap.dingtalk.service.DingtalkCallbackService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DingtalkCallbackServiceImpl implements DingtalkCallbackService {

    private DUserService dUserService;

    @Autowired
    public void setdUserService(DUserService dUserService) {
        this.dUserService = dUserService;
    }

    @Override
    public void onUserAddOrg(Event event) {
        log.info("新增用户 {}", event);
        // 每次新增一定会触发修改时间，这里暂时不处理
    }

    @Override
    public void onUserModifyOrg(Event event) {
        log.info("编辑用户 {}", event);
        @SuppressWarnings("unchecked")
        List<String> users = (List<String>) event.get("UserId");
        users.forEach(userid -> {
            dUserService.asyncToUap(userid);
        });
    }

    @Override
    public void onUserLeaveOrg(Event event) {
        log.info("删除用户", event);
        @SuppressWarnings("unchecked")
        List<String> users = (List<String>) event.get("UserId");
        users.forEach(user -> {
            try {
//                dUserService.delete(user);
            } catch (Exception e) {
                log.info("物理删除{}失败，改用逻辑删除", user);
            }
        });
        System.out.println(event);
    }

    @Override
    public void onOrgDeptCreate(Event event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOrgDeptModify(Event event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOrgDeptRemove(Event event) {
        // TODO Auto-generated method stub

    }

}
