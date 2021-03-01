package com.dm.file.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * 打包下载文件的文件信息
 */
@Data
public class PackageFileDto implements Serializable {
    private static final long serialVersionUID = -2699580418259308496L;
    /**
     * 打包下载的id,这个在请求的时候自动创建
     *
     * @ignore
     */
    private String id;
    /**
     * 压缩包类型
     */
    private String type;
    /**
     * 压缩包的文件名，可以手动指定文件名称，下载时会优先使用此文件名
     */
    private String filename;
    /**
     * 要打包下载的文件的列表
     */
    private List<UUID> files;
}
