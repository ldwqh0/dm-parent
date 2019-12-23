package com.dm.file.converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dm.common.converter.Converter;
import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;

@Component
public class FileInfoConverter implements Converter<FileInfo, FileInfoDto> {

    private FileInfoDto toDtoActual(FileInfo model) {
        FileInfoDto file_ = new FileInfoDto();
        file_.setId(model.getId());
        file_.setFilename(model.getFilename());
        file_.setCreateTime(model.getCreatedDate().orElse(null));
        file_.setCreateUser(model.getCreatedBy().orElse(null));
        file_.setLastModifiedBy(model.getLastModifiedBy().orElse(null));
        file_.setPath(model.getPath());
        file_.setSize(model.getSize());
        return file_;
    }

    @Override
    public FileInfo copyProperties(FileInfo dest, FileInfoDto src) {
        dest.setFilename(src.getFilename());
        dest.setPath(src.getPath());
        dest.setSize(src.getSize());
        return dest;
    }

    @Override
    public FileInfoDto toDto(FileInfo model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
