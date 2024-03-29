package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.util.List;

/**
 * TOP DingTalk-API: dingtalk.oapi.role.list response.
 *
 * @author top auto create
 * @since 1.0, null
 */
public class OapiRoleListResponse extends TaobaoResponse {

    private static final long serialVersionUID = 2829222498139478487L;

    /**
     * errcode
     */
    private Long errcode;

    /**
     * errmsg
     */
    private String errmsg;

    /**
     * result
     */
    private PageVo<OpenRoleGroup> result;

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

    public void setResult(PageVo<OpenRoleGroup> result) {
        this.result = result;
    }

    public PageVo<OpenRoleGroup> getResult() {
        return this.result;
    }

    @Override
    public boolean isSuccess() {
        return getErrcode() == null || getErrcode().equals(0L);
    }

    /**
     * list
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class OpenRoleGroup implements Serializable {
        private static final long serialVersionUID = 5231897893516396395L;

        private String corpid;

        /**
         * 角色组id
         */
        private Long groupId;
        /**
         * 角色组名称
         */
        private String name;
        /**
         * roles
         */
        private List<OpenRole> roles;

        public String getCorpid() {
            return corpid;
        }

        public void setCorpid(String corpid) {
            this.corpid = corpid;
        }

        public Long getGroupId() {
            return this.groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<OpenRole> getRoles() {
            return this.roles;
        }

        public void setRoles(List<OpenRole> roles) {
            this.roles = roles;
        }
    }

}
