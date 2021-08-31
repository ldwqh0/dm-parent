package com.dm.file.converter;

import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;

import java.util.Optional;

public final class FileInfoConverter {
    private FileInfoConverter() {
    }

    public static FileInfoDto toDto(FileInfo model) {
        return Optional.ofNullable(model).map(it -> {
            FileInfoDto file_ = new FileInfoDto();
            file_.setId(it.getId());
            file_.setFilename(it.getFilename());
            it.getCreatedDate().ifPresent(file_::setCreateTime);
            it.getCreatedBy().ifPresent(file_::setCreatedBy);
            it.getLastModifiedBy().ifPresent(file_::setLastModifiedBy);
            file_.setPath(it.getPath());
            file_.setSize(it.getSize());
            return file_;
        }).orElse(null);
    }

}
