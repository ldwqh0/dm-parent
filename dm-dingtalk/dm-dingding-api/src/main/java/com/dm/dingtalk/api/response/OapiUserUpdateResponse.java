package com.dm.dingtalk.api.response;

/**
 * TOP DingTalk-API: dingtalk.oapi.user.update response.
 *
 * @author top auto create
 * @since 1.0, null
 */
public class OapiUserUpdateResponse extends TaobaoResponse {

    private static final long serialVersionUID = 7549359597426592137L;

    /**
     * errcode
     */
    private Long errcode;

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public Long getErrcode() {
        return this.errcode;
    }

    @Override
    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

}
