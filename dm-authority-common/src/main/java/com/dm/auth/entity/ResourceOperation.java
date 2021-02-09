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

    @Column(name = "get_able_")
    private Boolean getAble;

    @Column(name = "post_able_")
    private Boolean postAble;

    @Column(name = "put_able_")
    private Boolean putAble;

    @Column(name = "delete_able_")
    private Boolean deleteAble;

    @Column(name = "patch_able_")
    private Boolean patchAble;

    @Column(name = "head_able_")
    private Boolean headAble;

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
