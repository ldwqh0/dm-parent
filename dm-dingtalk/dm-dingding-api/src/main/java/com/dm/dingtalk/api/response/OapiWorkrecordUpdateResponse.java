package com.dm.dingtalk.api.response;

/**
 * 更新待办的响应
 *
 * @author ldwqh0@outlook.com
 */
public class OapiWorkrecordUpdateResponse extends TaobaoResponse {

    private static final long serialVersionUID = 3545696184364564299L;

    /**
     * dingOpenErrcode
     */
    private Long errcode;

    /**
     * 结果
     */
    private String errmsg;

    /**
     * result
     */
    private Boolean result;

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

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Boolean getResult() {
        return this.result;
    }

    @Override
    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

}
