package com.dm.fileserver.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.fileserver.entity.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {

}
