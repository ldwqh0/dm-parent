package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.util.List;

/**
 * TOP DingTalk-API: dingtalk.oapi.department.list response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class OapiDepartmentListResponse extends TaobaoResponse {

    private static final long serialVersionUID = 1651454826438456217L;

    /**
     * department
     */
    private List<Department> department;

    /**
     * errcode
     */
    private Long errcode;

    /**
     * errmsg
     */
    private String errmsg;

    public void setDepartment(List<Department> department) {
        this.department = department;
    }

    public List<Department> getDepartment() {
        return this.department;
    }

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

    /**
     * department
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class Department implements Serializable {
        private static final long serialVersionUID = 5792686412978143124L;
        /**
         * autoAddUser
         */
        private Boolean autoAddUser;
        /**
         * createDeptGroup
         */
        private Boolean createDeptGroup;
        /**
         * id
         */
        private Long id;
        /**
         * name
         */
        private String name;
        /**
         * parentid
         */
        private Long parentid;
        /**
         * sourceIdentifier
         */
        private String sourceIdentifier;

        public Boolean getAutoAddUser() {
            return this.autoAddUser;
        }

        public void setAutoAddUser(Boolean autoAddUser) {
            this.autoAddUser = autoAddUser;
        }

        public Boolean getCreateDeptGroup() {
            return this.createDeptGroup;
        }

        public void setCreateDeptGroup(Boolean createDeptGroup) {
            this.createDeptGroup = createDeptGroup;
        }

        public Long getId() {
            return this.id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getParentid() {
            return this.parentid;
        }

        public void setParentid(Long parentid) {
            this.parentid = parentid;
        }

        public String getSourceIdentifier() {
            return this.sourceIdentifier;
        }

        public void setSourceIdentifier(String sourceIdentifier) {
            this.sourceIdentifier = sourceIdentifier;
        }

        @Override
        public String toString() {
            return "Department [autoAddUser=" + autoAddUser + ", createDeptGroup=" + createDeptGroup + ", id=" + id
                    + ", name=" + name + ", parentid=" + parentid + ", sourceIdentifier=" + sourceIdentifier + "]";
        }

    }

}
