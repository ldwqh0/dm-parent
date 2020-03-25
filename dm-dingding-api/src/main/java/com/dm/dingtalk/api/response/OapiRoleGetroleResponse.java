package com.dm.dingtalk.api.response;

public class OapiRoleGetroleResponse extends TaobaoResponse {

    private static final long serialVersionUID = -8843171350692490790L;

    /**
     * dingOpenErrcode
     */
    private Long errcode;

    /**
     * errorMsg
     */
    private String errmsg;

    /**
     * result
     */
    private OpenRole role;

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public Long getErrcode() {
        return this.errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getErrmsg() {
        return this.errmsg;
    }

    public void setRole(OpenRole role) {
        this.role = role;
    }

    public OpenRole getRole() {
        return this.role;
    }

    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

}
