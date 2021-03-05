package com.dm.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class ResourceOperation implements Serializable {

    private static final long serialVersionUID = -4460635653614468618L;

    /*
     * 是否可以接受GET请求
     */
    @Column(name = "get_able_")
    private Boolean getAble;
    /*
     * 是否可以接受POST请求
     */
    @Column(name = "post_able_")
    private Boolean postAble;

    /*
     * 是否可以接受PUT请求
     */
    @Column(name = "put_able_")
    private Boolean putAble;

    /*
     * 是否可以接受DELETE请求
     */
    @Column(name = "delete_able_")
    private Boolean deleteAble;

    /*
     * 是否可以接受PATCH请求
     */
    @Column(name = "patch_able_")
    private Boolean patchAble;

    /*
     * 是否可以接受HEAD请求
     */
    @Column(name = "head_able_")
    private Boolean headAble;

    /*
     * 是否可以接受OPTIONS请求
     */
    @Column(name = "options_able_")
    private Boolean optionsAble;

    public static ResourceOperation accessAll() {
        return all(Boolean.TRUE);
    }

    public static ResourceOperation denyAll() {
        return all(Boolean.FALSE);
    }

    public static ResourceOperation all(Boolean v) {
        ResourceOperation operation = new ResourceOperation();
        operation.setDeleteAble(v);
        operation.setPatchAble(v);
        operation.setGetAble(v);
        operation.setPostAble(v);
        operation.setPutAble(v);
        operation.setHeadAble(v);
        operation.setOptionsAble(v);
        return operation;
    }

}
