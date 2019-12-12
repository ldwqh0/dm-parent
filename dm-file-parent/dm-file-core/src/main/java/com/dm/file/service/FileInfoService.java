package com.dm.file.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;

public interface FileInfoService {
    /**
     * 获取一个文件信息
     * 
     * @param id
     * @return
     */
    public Optional<FileInfo> findById(UUID id);

    /**
     * 保存文件，并返回文件信息
     * 
     * @param fileInfo
     * @return
     * @throws Exception
     */
    public FileInfo save(File file, FileInfoDto fileInfo) throws Exception;

    /**
     * 删除指定的文件
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public void delete(UUID id) throws Exception;

    /**
     * 保存文件
     * 
     * @param inputStream
     * 
     * @param fileInfo
     * @return
     * @throws Exception
     */
    public FileInfo save(InputStream inputStream, FileInfoDto fileInfo) throws Exception;

    /**
     * 保存分块文件
     * 
     * @param src
     * @param fileInfo
     * @return
     * @throws IOException
     */
    public FileInfo save(File[] src, FileInfoDto fileInfo) throws IOException;

}
