package com.dm.dingtalk.api.request;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * TOP DingTalk-API: dingtalk.oapi.user.update request
 *
 * @author top auto create
 * @since 1.0, 2018.07.25
 */
public class OapiUserUpdateRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 部门列表
     */
    private List<Long> department;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 扩展属性
     */
    private String extattr;

    /**
     * 是否号码隐藏
     */
    private Boolean isHide;

    /**
     * 是否高管模式
     */
    private Boolean isSenior;

    /**
     * 工号
     */
    private String jobnumber;

    /**
     * 通讯录语言(默认zh_CN另外支持en_US)
     */
    private String lang;

    /**
     * 主管
     */
    private String managerUserId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 名字
     */
    private String name;

    /**
     * 实际是Map的序列化字符串
     */
    private String orderInDepts;

    /**
     * 公司邮箱
     */
    private String orgEmail;

    /**
     * 职位
     */
    private String position;

    /**
     * 备注
     */
    private String remark;

    /**
     * 分机号，长度为0~50个字符
     */
    private String tel;

    /**
     * 用户id
     */
    private final String userid;

    /**
     * 工作地点
     */
    private String workPlace;

    public OapiUserUpdateRequest(String userid) {
        this.userid = userid;
    }

    public List<Long> getDepartment() {
        return department;
    }

    public void setDepartment(List<Long> department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExtattr() {
        return extattr;
    }

    public void setExtattr(String extattr) {
        this.extattr = extattr;
    }

    public Boolean getHide() {
        return isHide;
    }

    public void setHide(Boolean hide) {
        isHide = hide;
    }

    public Boolean getSenior() {
        return isSenior;
    }

    public void setSenior(Boolean senior) {
        isSenior = senior;
    }

    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(String managerUserId) {
        this.managerUserId = managerUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderInDepts() {
        return orderInDepts;
    }

    public void setOrderInDepts(String orderInDepts) {
        this.orderInDepts = orderInDepts;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserid() {
        return userid;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OapiUserUpdateRequest that = (OapiUserUpdateRequest) o;
        return Objects.equals(department, that.department) && Objects.equals(email, that.email) && Objects.equals(extattr, that.extattr) && Objects.equals(isHide, that.isHide) && Objects.equals(isSenior, that.isSenior) && Objects.equals(jobnumber, that.jobnumber) && Objects.equals(lang, that.lang) && Objects.equals(managerUserId, that.managerUserId) && Objects.equals(mobile, that.mobile) && Objects.equals(name, that.name) && Objects.equals(orderInDepts, that.orderInDepts) && Objects.equals(orgEmail, that.orgEmail) && Objects.equals(position, that.position) && Objects.equals(remark, that.remark) && Objects.equals(tel, that.tel) && Objects.equals(userid, that.userid) && Objects.equals(workPlace, that.workPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, email, extattr, isHide, isSenior, jobnumber, lang, managerUserId, mobile, name, orderInDepts, orgEmail, position, remark, tel, userid, workPlace);
    }
}
