package com.dm.dingtalk.api.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TOP DingTalk-API: dingtalk.oapi.user.update request
 * 
 * @author top auto create
 * @since 1.0, 2018.07.25
 */
@Data
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

}