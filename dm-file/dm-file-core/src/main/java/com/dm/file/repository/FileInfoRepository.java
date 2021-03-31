package com.dm.file.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.file.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID>, IdentifiableDtoRepository<FileInfo, UUID> {

    /**
     * 根据文件名，sha256和md5获取文件信息
     *
     * @param filename 要查找的文件名
     * @param sha256   要查找的文件的sha值
     * @param md5      哟啊查找的文件的md5值
     * @return 查找到的文件信息
     */
    Optional<FileInfo> findByFilenameAndSha256AndMd5(String filename, String sha256, String md5);

}
