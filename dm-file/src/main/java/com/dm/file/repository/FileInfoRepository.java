package com.dm.file.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.file.entity.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {

}
