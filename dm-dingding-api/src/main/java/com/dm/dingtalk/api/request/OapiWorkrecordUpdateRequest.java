package com.dm.dingtalk.api.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * 更新待办事项请求模型
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class OapiWorkrecordUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1424859000661381346L;

    private String userid;

    private String recordId;

    public OapiWorkrecordUpdateRequest() {
    }

    public OapiWorkrecordUpdateRequest of(String userid, String recordId) {
        return new OapiWorkrecordUpdateRequest(userid, recordId);
    }

    public OapiWorkrecordUpdateRequest(String userid, String recordId) {
        this.userid = userid;
        this.recordId = recordId;
    }

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
