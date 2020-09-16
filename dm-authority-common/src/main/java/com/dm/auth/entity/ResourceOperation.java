package com.dm.auth.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ResourceOperation implements Serializable {

    private static final long serialVersionUID = -4460635653614468618L;

    @Column(name = "read_able_")
    private Boolean readable;

    @Column(name = "save_able_")
    private Boolean saveable;

    @Column(name = "update_able_")
    private Boolean updateable;

    @Column(name = "delete_able_")
    private Boolean deleteable;

    @Column(name = "patch_able_")
    private Boolean patchable;

    public static ResourceOperation accessAll() {
        ResourceOperation operation = new ResourceOperation();
        operation.setDeleteable(Boolean.TRUE);
        operation.setPatchable(Boolean.TRUE);
        operation.setReadable(Boolean.TRUE);
        operation.setSaveable(Boolean.TRUE);
        operation.setUpdateable(Boolean.TRUE);
        return operation;
    }

    public static ResourceOperation denyAll() {
        ResourceOperation operation = new ResourceOperation();
        operation.setDeleteable(Boolean.FALSE);
        operation.setPatchable(Boolean.FALSE);
        operation.setReadable(Boolean.FALSE);
        operation.setSaveable(Boolean.FALSE);
        operation.setUpdateable(Boolean.FALSE);
        return operation;
    }

}
