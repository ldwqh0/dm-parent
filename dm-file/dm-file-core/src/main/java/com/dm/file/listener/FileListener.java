package com.dm.file.listener;

import com.dm.file.config.FileConfig;
import com.dm.file.controller.FileController;
import com.dm.file.entity.FileInfo;
import com.dm.file.service.FileStorageService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostRemove;

/**
 * 文件处理侦听器
 */

public class FileListener {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private FileStorageService storageService;

    private FileConfig fileConfig;

    public void setStorageService(FileStorageService storageService) {
        this.storageService = storageService;
    }

    public void setFileConfig(FileConfig fileConfig) {
        this.fileConfig = ObjectUtils.clone(fileConfig);
    }

    /**
     * 当从数据库删除文件时，一并从存储中删除文件
     *
     * @param file 要删除的文件
     */
    @PostRemove
    public void postDelete(FileInfo file) {
        if (this.storageService != null && this.fileConfig.isDeleteFromStorage()) {
            if (storageService.delete(file.getPath())) {
                log.info("从存储中删除文件，文件存储名称是{}", file.getPath());
            } else {
                log.info("从存储中删除文件时失败，文件存储名称是{}", file.getPath());
            }
        }
    }
}
