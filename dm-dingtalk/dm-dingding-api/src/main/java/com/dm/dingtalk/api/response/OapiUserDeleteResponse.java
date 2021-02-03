package com.dm.dingtalk.api.response;

public class OapiUserDeleteResponse extends TaobaoResponse {

    private static final long serialVersionUID = 8552848446317928931L;

    /**
     * errcode
     */
    private Long errcode;

    /**
     * errmsg
     */
    private String errmsg;

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

    @Override
    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

}