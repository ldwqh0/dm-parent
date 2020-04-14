package com.dm.auth.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class ResourceOperationDto implements Serializable {

    private static final long serialVersionUID = 363494892979011485L;

    private ResourceDto resource;

    private Boolean readable;

    private Boolean saveable;

    private Boolean updateable;

    private Boolean deleteable;

    private Boolean patchable;

    @JsonIgnoreProperties({ "description" })
    public ResourceDto getResource() {
        return resource;
    }

}
