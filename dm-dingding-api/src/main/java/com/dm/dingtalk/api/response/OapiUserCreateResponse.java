package com.dm.dingtalk.api.response;

/**
 * TOP DingTalk-API: dingtalk.oapi.user.create response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class OapiUserCreateResponse extends TaobaoResponse {

    private static final long serialVersionUID = 3451156399655321663L;

    /**
     * errcode
     */
    private Long errcode;

    /**
     * userid
     */
    private String userid;

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public Long getErrcode() {
        return this.errcode;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return this.userid;
    }

    @Override
    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

}
