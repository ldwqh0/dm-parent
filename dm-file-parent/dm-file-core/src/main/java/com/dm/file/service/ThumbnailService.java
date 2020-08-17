package com.dm.file.service;

import java.io.IOException;

import org.springframework.core.io.Resource;

public interface ThumbnailService {
    /**
     * 创建缩略图
     * 
     * @param filename 给指定的文件创建缩略图
     */
    public void createThumbnail(String filename);

    /**
     * 根据文件ID和缩略图级别获取缩略图
     * 
     * @param fileId
     * @param level
     * @return
     * @throws IOException
     */
    public Resource getResource(String filename, int level);
}
