package com.dm.dingtalk.api.response;

import java.util.List;

/**
 * TOP DingTalk-API: dingtalk.oapi.user.getDeptMember response.
 *
 * @author top auto create
 * @since 1.0, null
 */
public class OapiUserGetDeptMemberResponse extends TaobaoResponse {

    private static final long serialVersionUID = 3349255888911547973L;

    /**
     * errcode
     */
    private Long errcode;

    /**
     * errmsg
     */
    private String errmsg;

    /**
     * userIds
     */
    private List<String> userIds;

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

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserIds() {
        return this.userIds;
    }

    @Override
    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

}
