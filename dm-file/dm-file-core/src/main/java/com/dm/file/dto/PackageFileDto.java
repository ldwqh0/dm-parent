package com.dm.file.dto;

import com.dm.collections.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.*;

import static java.util.Collections.unmodifiableList;

/**
 * 打包下载文件的文件信息
 */
public class PackageFileDto implements Serializable {
    private static final long serialVersionUID = -2699580418259308496L;
    /**
     * 打包下载的id,这个在请求的时候自动创建
     */
    private final String id;
    /**
     * 压缩包类型
     */
    private final String type;
    /**
     * 压缩包的文件名，可以手动指定文件名称，下载时会优先使用此文件名
     */
    private final String filename;
    /**
     * 要打包下载的文件的列表
     */
    private final List<UUID> files;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getFilename() {
        return filename;
    }

    public List<UUID> getFiles() {
        return unmodifiableList(files);
    }

    public PackageFileDto(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("filename") String filename,
        @JsonProperty("files") List<UUID> files) {
        if (Objects.isNull(id)) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        this.type = type;
        this.filename = filename;
        if (CollectionUtils.isNotEmpty(files)) {
            this.files = new ArrayList<>(files);
        } else {
            this.files = Collections.emptyList();
        }
    }
}
