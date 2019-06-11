package com.dm.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.file.converter.FileInfoConverter;
import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;
import com.dm.file.repository.FileInfoRepository;
import com.dm.file.service.FileInfoService;
import com.dm.file.service.FileStorageService;
import com.dm.file.util.DmFileUtils;

@Service
public class FileServiceImpl implements FileInfoService {

	@Autowired
	private FileInfoConverter fileInfoConverter;

	@Autowired
	private FileStorageService storageService;

	@Autowired
	private FileInfoRepository fileInfoRepository;

	@Override
	public Optional<FileInfo> get(UUID id) {
		return fileInfoRepository.findById(id);
	}

	@Override
	public FileInfo save(File file, FileInfoDto _info) throws Exception {
		FileInfo fileInfo = save(_info);
		storageService.save(fileInfo.getPath(), file);
		return fileInfo;
	}

	@Override
	@Transactional
	public void delete(UUID id) throws Exception {
		Optional<FileInfo> fileInfo = fileInfoRepository.findById(id);
		if (fileInfo.isPresent()) {
			storageService.delete(fileInfo.get().getPath());
		}
		fileInfoRepository.deleteById(id);
	}

	@Override
	@Transactional
	public FileInfo save(InputStream inputStream, FileInfoDto _info) throws Exception {
		FileInfo info = save(_info);
		storageService.save(info.getPath(), inputStream);
		return info;
	}

	private FileInfo save(FileInfoDto _info) {
		FileInfo fileInfo = new FileInfo();
		fileInfoConverter.copyProperties(fileInfo, _info);
		fileInfo = fileInfoRepository.save(fileInfo);
		String filename = _info.getFilename();
		String ext = DmFileUtils.getExt(filename);
		String storagePath = fileInfo.getId() + "." + ext;
		fileInfo.setPath(storagePath);
		return fileInfo;
	}

	@Override
	@Transactional
	public FileInfo save(File[] src, FileInfoDto fileInfo) throws IOException {
		FileInfo file = save(fileInfo);
		storageService.save(fileInfo.getPath(), src);
		return file;
	}

}
