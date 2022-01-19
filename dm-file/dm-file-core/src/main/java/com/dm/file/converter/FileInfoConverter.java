package com.dm.file.converter;

import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;

public final class FileInfoConverter {
    private FileInfoConverter() {
    }

    public static FileInfoDto toDto(FileInfo model) {
        return new FileInfoDto(
            model.getId(),
            model.getFilename(),
            model.getPath(),
            model.getSize(),
            model.getCreatedBy().orElse(null),
            model.getLastModifiedBy().orElse(null),
            model.getCreatedDate().orElse(null)
        );
    }

}
