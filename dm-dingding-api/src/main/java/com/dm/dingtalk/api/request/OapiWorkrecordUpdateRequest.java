package com.dm.dingtalk.api.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * 更新待办事项请求模型
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class OapiWorkrecordUpdateRequest {

    private String userid;

    private String recordId;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @JsonGetter("record_id")
    public String getRecordId() {
        return recordId;
    }

    @JsonSetter("recordId")
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

}
