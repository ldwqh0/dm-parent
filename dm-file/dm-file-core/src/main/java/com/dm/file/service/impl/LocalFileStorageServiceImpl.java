package com.dm.file.service.impl;

import com.dm.file.config.FileConfig;
import com.dm.file.service.FileStorageService;
import com.dm.file.util.DmFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final FileConfig config;

    public LocalFileStorageServiceImpl(FileConfig config) {
        this.config = config;
    }

    @Override
    public boolean save(MultipartFile file, String filename, String... parents) {
        try {
            Path target = getPath(filename, parents);
            createParentIfNotExists(target);
            file.transferTo(target);
            return true;
        } catch (IOException e) {
            log.error("保存文件 {} 失败", filename, e);
            return false;
        }
    }

    @Override
    public boolean delete(String filename, String... parents) {
        try {
            return Files.deleteIfExists(getPath(filename, parents));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean exist(String filename, String... parents) {
        return Files.exists(getPath(filename, parents));
    }

    /**
     * 将多个文件合并保存为一个文件
     *
     * @param paths    源文件的路径集合
     * @param filename 目标文件名
     * @param parents  目录的前缀路径
     * @return 保存成功返还true, 保存失败返还false
     */
    @Override
    public boolean save(Path[] paths, String filename, String... parents) {
        Path target = getPath(filename, parents);
        if (createParentIfNotExists(target)) {
            return DmFileUtils.concatFile(getPath(filename, parents), paths);
        } else {
            return false;
        }
    }

    @Override
    public Resource getResource(String filename, Long start, Long end, String... parents) {
        return getResource(filename, parents);
    }

    @Override
    public Resource getResource(String filename, String... parents) {
        return new FileSystemResource(getPath(filename, parents));
    }

    @Override
    public OutputStream getOutputStream(String filename, String... parents) throws IOException {
        Path target = getPath(filename, parents);
        createParentIfNotExists(target);
        return new FileOutputStream(target.toFile(), false);
    }

    /**
     * 初始化的时候，创建目录
     */
    @PostConstruct
    public void init() throws Exception {
        String path = config.getPath();
        String tempPath = config.getTempPath();
        Files.createDirectories(Paths.get(path));
        Files.createDirectories(Paths.get(tempPath));
    }

    private Path getPath(String filename, String... path) {
        return Paths.get(config.getPath(), StringUtils.join(path, File.separator), filename);
    }

    private boolean createParentIfNotExists(Path path) {
        Path parent = path.getParent();
        if (Objects.isNull(parent)) {
            return false;
        } else {
            if (Files.exists(parent)) {
                if (Files.isDirectory(parent)) {
                    log.debug("指定的目录[ {} ]已经存在", parent);
                    return true;
                } else {
                    log.error("指定的目录[ {} ]已经存在，并且不是一个目录", parent);
                    return false;
                }
            } else {
                try {
                    Files.createDirectories(parent);
                    log.info("创建文件夹，在{}", parent);
                    return true;
                } catch (IOException e) {
                    log.error("创建文件夹时出错", e);
                    return false;
                }
            }
        }
    }
}
