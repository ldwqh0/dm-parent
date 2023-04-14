package com.dm.file.service;

import com.dm.file.dto.FileInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileInfoService {
    /**
     * 获取一个文件信息
     *
     * @param id 文件id
     * @return 获取到的文件信息
     */
    Optional<FileInfoDto> findById(UUID id);

    /**
     * 保存文件，并返回文件信息
     *
     * @param fileInfo 文件的数据库信息
     * @return 保存后的文件信息
     * @throws Exception 保存失败时抛出异常
     */
    FileInfoDto save(MultipartFile file, FileInfoDto fileInfo) throws Exception;

    /**
     * 删除指定的文件
     *
     * @param id 要删除的文件的id
     * @throws Exception 删除失败时抛出异常
     */
    void delete(UUID id) throws Exception;

    /**
     * 保存分块文件
     *
     * @param src      要保存的文件的分块
     * @param fileInfo 文件信息
     * @return 保存之后的文件信息
     * @throws IOException 保存失败时抛出异常
     */
    FileInfoDto save(Path[] src, FileInfoDto fileInfo) throws IOException;

    Optional<FileInfoDto> findByNameAndHash(String filename, String sha256, String md5);

    List<FileInfoDto> findById(List<UUID> files);
}
