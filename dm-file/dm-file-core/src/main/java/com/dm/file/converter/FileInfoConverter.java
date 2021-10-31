package com.dm.file.converter;

import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;

public final class FileInfoConverter {
    private FileInfoConverter() {
    }

    public static FileInfoDto toDto(FileInfo model) {
        FileInfoDto dto = new FileInfoDto();
        dto.setId(model.getId());
        dto.setFilename(model.getFilename());
        model.getCreatedDate().ifPresent(dto::setCreateTime);
        model.getCreatedBy().ifPresent(dto::setCreatedBy);
        model.getLastModifiedBy().ifPresent(dto::setLastModifiedBy);
        dto.setPath(model.getPath());
        dto.setSize(model.getSize());
        return dto;
    }

}
