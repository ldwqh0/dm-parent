package com.dm.file.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.dm.collections.Lists;
import com.dm.file.config.FileConfig;
import com.dm.file.service.FileStorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalFileStorageServiceImpl implements FileStorageService {

    private FileConfig config;

    @Autowired
    public void setConfig(FileConfig config) {
        this.config = config;
    }

    @Override
    public boolean save(MultipartFile file, String filename, String... path) {
        try {
            file.transferTo(createTargetFile(filename, path));
//            FileUtils.copyToFile(file.getInputStream(), createTargetFile(filename, path));
            return true;
        } catch (IOException e) {
            log.error("保存文件 {} 失败", filename, e);
            return false;
        }
    }

    @Override
    public boolean delete(String filename, String... path) {
        return FileUtils.deleteQuietly(getTargetFile(filename, path));
    }

    @Override
    public boolean exist(String filename, String... path) {
        return getTargetFile(filename, path).exists();
    }

    @Override
    public boolean save(File[] src, String filename, String... path) {
        try (OutputStream out = FileUtils.openOutputStream(createTargetFile(filename, path), false)) {
            for (File file : src) {
                FileUtils.copyFile(file, out);
            }
            return true;
        } catch (IOException e) {
            log.error("保存文件 {} 时发生错误", filename, e);
            return false;
        }
    }

    @Override
    public Resource getResource(String filename, Long start, Long end, String... path) {
        return new FileSystemResource(getTargetFile(filename, path));
    }

    @Override
    public Resource getResource(String filename, String... path) {
        return new FileSystemResource(getTargetFile(filename, path));
    }

    @Override
    public OutputStream getOutputStream(String filename, String... path) throws IOException {
        return new FileOutputStream(createTargetFile(filename, path), false);
    }

    private File getTargetFile(String filename, String... path) {
        List<String> pathList = Lists.arrayList(config.getPath());
        Lists.addAll(pathList, path);
        Lists.addAll(pathList, filename);
        return FileUtils.getFile(pathList.toArray(new String[pathList.size()]));
    }

    private File createTargetFile(String filename, String... path) throws IOException {
        File file = getTargetFile(filename, path);
        FileUtils.forceMkdirParent(file);
        return file;
    }

    /**
     * 初始化的时候，创建目录
     */
    @PostConstruct
    public void initStorage() throws Exception {
        String path = config.getPath();
        String tempPath = config.getTempPath();
        FileUtils.forceMkdir(new File(path));
        FileUtils.forceMkdir(new File(tempPath));
    }
}
