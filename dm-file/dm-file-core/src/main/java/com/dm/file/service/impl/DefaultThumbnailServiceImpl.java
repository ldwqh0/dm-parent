package com.dm.file.service.impl;

import com.dm.file.service.FileStorageService;
import com.dm.file.service.ThumbnailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class DefaultThumbnailServiceImpl implements ThumbnailService {
    private static final Logger log = LoggerFactory.getLogger(DefaultThumbnailServiceImpl.class);

    private final FileStorageService storageService;

    private final int[][] levelScales = {{128, 128}, {256, 256}, {512, 512}, {1080, 1920}};

    public DefaultThumbnailServiceImpl(FileStorageService storageService) {
        this.storageService = storageService;
    }

    protected void createThumbnail(Image image, String filename, int level) throws IOException {
        try (OutputStream oStream = storageService.getOutputStream(filename, "th" + level)) {
            int o_w = image.getWidth(null);
            int o_h = image.getHeight(null);
            int max_width = levelScales[level][0];
            int max_height = levelScales[level][1];
            double scale = Math.max(o_w * 1.0 / max_width, o_h * 1.0 / max_height);
            int n_w = Double.valueOf(o_w / scale).intValue();
            int n_h = Double.valueOf(o_h / scale).intValue();
            BufferedImage bi = new BufferedImage(n_w, n_h, BufferedImage.TYPE_INT_RGB);
            bi.getGraphics().drawImage(image, 0, 0, n_w, n_h, null);
            ImageIO.write(bi, "jpg", oStream);
        }
    }

    @Override
    public void createThumbnail(String filename) throws IOException {
        try (InputStream iStream = storageService.getResource(filename).getInputStream()) {
            Image image = ImageIO.read(iStream);
            if (Objects.isNull(image)) {
                log.debug("不支持的文件内容，创建文件缩略图失败{}", filename);
            } else {
                log.debug("为文件{}创建缩略图", filename);
                for (int i = 0; i < levelScales.length; i++) {
                    createThumbnail(image, filename, i);
                }
            }
        } catch (IOException e) {
            log.error("创建文件缩略图时出错", e);
        }
    }

    @Override
    public void deleteThumbnail(String filename) {
        // todo 删除文件缩略图待实现
    }

    @Override
    public boolean exists(String filename, int level) {
        return storageService.exist(filename, "th" + level);
    }

    @Override
    public Resource getResource(String filename, int level) throws IOException {
        return storageService.getResource(filename, "th" + level);
    }

}
