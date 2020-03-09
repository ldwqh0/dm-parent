package com.dm.dingtalk.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 待办事项响应模型
 * 
 * @author ??????
 *
 */
public class OapiWorkrecordAddResponse extends TaobaoResponse {
    private static final long serialVersionUID = 6127843317528511781L;

    /**
     * dingOpenErrcode
     */
    private Long errcode;

    /**
     * errorMsg
     */
    private String errmsg;

    /**
     * 待办唯一id
     */
    @JsonProperty("record_id")
    private String recordId;

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public Long getErrcode() {
        return this.errcode;
    }

    @Override
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String getErrmsg() {
        return this.errmsg;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordId() {
        return this.recordId;
    }

    @Override
    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

}
