package com.dm.file.service;

import com.dm.file.dto.PackageFileDto;

import java.util.Optional;

public interface PackageFileService {
    /**
     * 保存一个打包文件请求
     *
     * @param request 打包文件的请求
     * @return 文件请求详情
     */
    PackageFileDto save(PackageFileDto request);

    Optional<PackageFileDto> findById(String id);

    Optional<PackageFileDto> findAndRemoveById(String id);

    void deleteById(String id);
}
