package com.dm.fileserver.converter;

import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.fileserver.dto.FileInfoDto;
import com.dm.fileserver.entity.FileInfo;

@Component
public class FileInfoConverter extends AbstractConverter<FileInfo, FileInfoDto> {

	@Override
	protected FileInfoDto toDtoActual(FileInfo model) {
		FileInfoDto file_ = new FileInfoDto();
		file_.setId(model.getId());
		file_.setFilename(model.getFilename());
		file_.setCreateTime(model.getCreatedDate().orElse(null));
		file_.setCreateUser(model.getCreatedBy().orElse(null));
		file_.setPath(model.getPath());
		file_.setSize(model.getSize());
		return file_;
	}

	@Override
	public void copyProperties(FileInfo dest, FileInfoDto src) {
		dest.setFilename(src.getFilename());
		dest.setPath(src.getPath());
		dest.setSize(src.getSize());
	}

}
