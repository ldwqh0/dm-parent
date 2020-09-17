package com.dm.file.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.file.entity.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID>, IdentifiableDtoRepository<FileInfo, UUID> {

    Optional<UUID> findMaxId();

}
