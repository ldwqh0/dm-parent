package com.dm.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dm.file.converter.FileInfoConverter;
import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;
import com.dm.file.exception.FileStorageException;
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
    @Transactional(readOnly = true)
    public Optional<FileInfo> findById(UUID id) {
        return fileInfoRepository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo save(MultipartFile file, FileInfoDto _info) throws Exception {
        FileInfo fileInfo = save(_info);
        if (storageService.save(file, fileInfo.getPath())) {
            return fileInfo;
        } else {
            throw new FileStorageException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(UUID id) throws Exception {
        Optional<FileInfo> fileInfo = fileInfoRepository.findById(id);
        if (fileInfo.isPresent()) {
            storageService.delete(fileInfo.get().getPath());
        }
        fileInfoRepository.deleteById(id);
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
    @Transactional(rollbackFor = Throwable.class)
    public FileInfo save(Path[] src, FileInfoDto fileInfo) throws IOException {
        FileInfo file = save(fileInfo);
        if (storageService.save(src, file.getPath())) {
            return file;
        } else {
            throw new FileStorageException();
        }
    }

}
