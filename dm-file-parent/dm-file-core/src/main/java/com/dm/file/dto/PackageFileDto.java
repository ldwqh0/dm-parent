package com.dm.file.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class PackageFileDto implements Serializable {
    private static final long serialVersionUID = -2699580418259308496L;
    private String id;
    private String type;
    private String filename;
    private List<UUID> files;
}
