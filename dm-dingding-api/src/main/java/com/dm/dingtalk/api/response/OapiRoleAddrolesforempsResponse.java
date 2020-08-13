package com.dm.dingtalk.api.response;

/**
 * TOP DingTalk-API: dingtalk.oapi.role.addrolesforemps response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class OapiRoleAddrolesforempsResponse extends TaobaoResponse {

    private static final long serialVersionUID = 6132787715393265695L;

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
