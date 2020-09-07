package com.dm.auth.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ResourceOperation {

    @ManyToOne
    private Resource resource;

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

}
