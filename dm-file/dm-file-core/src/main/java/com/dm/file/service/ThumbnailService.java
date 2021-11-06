package com.dm.file.service;

import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 文件缩缩略图服务
 */
public interface ThumbnailService {
    /**
     * 创建缩略图
     *
     * @param filename 给指定的文件创建缩略图
     */
    void createThumbnail(String filename) throws IOException;

    void deleteThumbnail(String filename);

    /**
     * 检测某个文件的缩略图是否存在
     *
     * @param filename 文件名称
     * @param level    缩略图等级
     */
    boolean exists(String filename, int level);

    /**
     * 根据文件ID和缩略图级别获取缩略图
     *
     * @param filename 文件存储名称
     * @param level    缩略图等级
     */
    Resource getResource(String filename, int level) throws IOException;
}
