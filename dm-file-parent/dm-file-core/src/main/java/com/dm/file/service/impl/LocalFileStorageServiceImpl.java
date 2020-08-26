package com.dm.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.dm.file.config.FileConfig;
import com.dm.file.service.FileStorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Autowired
    private FileConfig config;

    @Override
    public boolean save(String path, InputStream inputStream) {
        try {
            FileUtils.copyToFile(inputStream, new File(getPath(path)));
            return true;
        } catch (IOException e) {
            log.error("保存文件 {} 失败", path, e);
            return false;
        }
    }

    @Override
    public boolean save(String path, File file) {
        try {
            FileUtils.copyFile(file, new File(getPath(path)));
            return true;
        } catch (IOException e) {
            log.error("保存文件 {} 失败", path, e);
            return false;
        }
    }

    @Override
    public boolean delete(String path) {
        return FileUtils.deleteQuietly(new File(getPath(path)));
    }

    @Override
    public boolean exist(String path) {
        return new File(getPath(path)).exists();
    }

    private String getPath(String filename) {
        return config.getPath() + File.separator + filename;
    }

    @Override
    public boolean save(String path, File[] src) {
        File target = new File(getPath(path));
        try (OutputStream out = FileUtils.openOutputStream(target, false)) {
            for (File file : src) {
                FileUtils.copyFile(file, out);
            }
            return true;
        } catch (Exception e) {
            log.error("保存文件 {} 时发生错误", path, e);
            return false;
        }
    }

    /**
     * 初始化的时候，创建目录
     */
    @PostConstruct
    public void initStorage() throws Exception {
        String path = config.getPath();
        String tempPath = config.getTempPath();
        File file = new File(path);
        FileUtils.forceMkdir(file);
        FileUtils.forceMkdir(new File(tempPath));
    }

    @Override
    public Resource getResource(String filename, Long start, Long end) {
        return new FileSystemResource(new File(getPath(filename)));
    }

    @Override
    public Resource getResource(String path) {
        return new FileSystemResource(new File(getPath(path)));
    }
}
