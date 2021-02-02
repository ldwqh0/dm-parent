package com.dm.file.listener;


import com.dm.file.config.FileConfig;
import com.dm.file.entity.FileInfo;
import com.dm.file.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostRemove;

/**
 * 文件处理侦听器
 */
@Slf4j
public class FileListener {

    private FileStorageService storageService;

    private FileConfig fileConfig;

    @Autowired(required = false)
    public void setStorageService(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    /**
     * 当从数据库删除文件时，一并从存储中删除文件
     *
     * @param file
     */
    @PostRemove
    public void postDelete(FileInfo file) {
        if (this.storageService != null && this.fileConfig.isDeleteFromStorage()) {
            log.debug("从存储中删除文件，文件存储名称是{}", file.getPath());
            storageService.delete(file.getPath());
        }
    }
}
