package com.dm.file.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.file.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID>, IdentifiableDtoRepository<FileInfo, UUID> {

    // todo 需要测试是否有效
    @Query("select max(fi.id) from FileInfo fi")
    Optional<UUID> findMaxId();

    /**
     * 根据文件名，sha256和md5获取文件信息
     *
     * @param filename
     * @param sha256
     * @param md5
     * @return
     */
    Optional<FileInfo> findByFilenameAndSha256AndMd5(String filename, String sha256, String md5);

}
