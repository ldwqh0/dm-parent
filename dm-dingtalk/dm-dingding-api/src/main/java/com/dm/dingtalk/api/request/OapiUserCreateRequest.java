package com.dm.dingtalk.api.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TOP DingTalk-API: dingtalk.oapi.user.create request
 * 
 * @author top auto create
 * @since 1.0, 2018.07.25
 */
@Data
public class OapiUserCreateRequest implements Serializable {

    private static final long serialVersionUID = 3692666649698119807L;

    /**
     * 员工唯一标识ID（不可修改），企业内必须唯一。长度为1~64个字符，如果不传，服务器将自动生成一个userid
     */
    private String userid;

    /**
     * 成员名称。长度为1~64个字符
     */
    private String name;

    /**
     * 数组类型，数组里面值为整型，成员所属部门id列表
     */
    private List<Long> department;

    /**
     * 在对应的部门中的排序, Map结构的json字符串, key是部门的Id, value是人员在这个部门的排序值
     */
    private String orderInDepts;

    /**
     * 邮箱。长度为0~64个字符。企业内必须唯一，不可重复
     */
    private String email;

    /**
     * 扩展属性，可以设置多种属性(但手机上最多只能显示10个扩展属性，具体显示哪些属性，请到OA管理后台->设置->通讯录信息设置和OA管理后台->设置->手机端显示信息设置)
     */
    private String extattr;

    /**
     * 是否号码隐藏, true表示隐藏, false表示不隐藏。隐藏手机号后，手机号在个人资料页隐藏，但仍可对其发DING、发起钉钉免费商务电话。
     */
    private Boolean isHide;

    /**
     * 是否高管模式，true表示是，false表示不是。开启后，手机号码对所有员工隐藏。普通员工无法对其发DING、发起钉钉免费商务电话。高管之间不受影响。
     */
    private Boolean isSenior;

    /**
     * 员工工号。对应显示到OA后台和客户端个人资料的工号栏目。长度为0~64个字符
     */
    private String jobnumber;

    /**
     * 手机号码，企业内必须唯一，不可重复
     */
    private String mobile;

    /**
     * 员工的企业邮箱，员工的企业邮箱已开通，才能增加此字段， 否则会报错
     */
    private String orgEmail;

    /**
     * 职位信息。长度为0~64个字符
     */
    private String position;

    /**
     * 备注，长度为0~1000个字符
     */
    private String remark;

    /**
     * 分机号，长度为0~50个字符，企业内必须唯一，不可重复
     */
    private String tel;

    /**
     * 办公地点，长度为0~50个字符
     */
    private String workPlace;

}