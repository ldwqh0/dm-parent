package com.dm.file.service.impl;

import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;
import com.dm.file.exception.FileStorageException;
import com.dm.file.repository.FileInfoRepository;
import com.dm.file.service.FileInfoService;
import com.dm.file.service.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileServiceImpl implements FileInfoService {

    private final FileStorageService storageService;

    private final FileInfoRepository fileInfoRepository;

    public FileServiceImpl(FileStorageService storageService, FileInfoRepository fileInfoRepository) {
        this.storageService = storageService;
        this.fileInfoRepository = fileInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileInfo> findById(UUID id) {
        return fileInfoRepository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo save(MultipartFile file, FileInfoDto _info) {
        FileInfo fileInfo = save(_info);
        if (storageService.save(file, fileInfo.getPath())) {
            return fileInfo;
        } else {
            throw new FileStorageException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(UUID id) {
        fileInfoRepository.findById(id).ifPresent(file -> {
            storageService.delete(file.getPath());
            fileInfoRepository.deleteById(id);
        });
    }

    private FileInfo save(FileInfoDto _info) {
        FileInfo fileInfo = fileInfoRepository.save(copyProperties(new FileInfo(), _info));
        String filename = _info.getFilename();
        String ext = FilenameUtils.getExtension(filename);
        String storagePath = fileInfo.getId() + "." + ext;
        fileInfo.setPath(storagePath);
        return fileInfo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public FileInfo save(Path[] src, FileInfoDto fileInfo) {
        FileInfo file = save(fileInfo);
        if (storageService.save(src, file.getPath())) {
            return file;
        } else {
            throw new FileStorageException();
        }
    }

    @Override
    public Optional<FileInfo> findByNameAndHash(String filename, String sha256, String md5) {
        return fileInfoRepository.findByFilenameAndSha256AndMd5(filename, sha256, md5);
    }

    @Override
    public List<FileInfo> findById(List<UUID> files) {
        return fileInfoRepository.findAllById(files);
    }


    private FileInfo copyProperties(FileInfo dest, FileInfoDto src) {
        dest.setFilename(src.getFilename());
        dest.setPath(src.getPath());
        dest.setSize(src.getSize());
        return dest;
    }
}
