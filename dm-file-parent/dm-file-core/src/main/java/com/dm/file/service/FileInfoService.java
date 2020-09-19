package com.dm.file.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;

public interface FileInfoService {
    /**
     * 获取一个文件信息
     * 
     * @param id
     * @return
     */
    Optional<FileInfo> findById(UUID id);

    /**
     * 保存文件，并返回文件信息
     * 
     * @param fileInfo
     * @return
     * @throws Exception
     */
    FileInfo save(MultipartFile file, FileInfoDto fileInfo) throws Exception;

    /**
     * 删除指定的文件
     * 
     * @param id
     * @return
     * @throws Exception
     */
    void delete(UUID id) throws Exception;

    /**
     * 保存文件
     * 
     * @param inputStream
     * 
     * @param fileInfo
     * @return
     * @throws Exception
     */
//    public FileInfo save(InputStream inputStream, FileInfoDto fileInfo) throws Exception;

    /**
     * 保存分块文件
     * 
     * @param src
     * @param fileInfo
     * @return
     * @throws IOException
     */
    FileInfo save(Path[] src, FileInfoDto fileInfo) throws IOException;

}
