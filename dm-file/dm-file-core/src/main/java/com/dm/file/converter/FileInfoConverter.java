package com.dm.file.converter;

import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;

public final class FileInfoConverter {
    private FileInfoConverter() {
    }

    public static FileInfoDto toDto(FileInfo model) {
        return FileInfoDto.builder()
            .id(model.getId())
            .filename(model.getFilename())
            .path(model.getPath())
            .size(model.getSize())
            .createdBy(model.getCreatedBy().orElse(null))
            .lastModifiedBy(model.getLastModifiedBy().orElse(null))
            .createTime(model.getCreatedDate().orElse(null))
            .build();
    }

}
