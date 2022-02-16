package com.dm.file.stream;

import com.dm.file.config.FileConfig;
import com.dm.file.service.FileStorageService;
import com.dm.file.service.ThumbnailService;
import com.dm.file.service.impl.DefaultThumbnailServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 利用javacv库对文件进行缩略图创建
 */
public class JavaCvThumbnailServiceImpl extends DefaultThumbnailServiceImpl implements ThumbnailService {
    private static final Logger log = LoggerFactory.getLogger(JavaCvThumbnailServiceImpl.class);

    private final FileConfig config;

    private final String[] videoExts = new String[]{"mp4", "avi", "mov"};

    public JavaCvThumbnailServiceImpl(FileStorageService fileStorageService, FileConfig fileConfig) {
        super(fileStorageService);
        this.config = fileConfig;
    }

    @Override
    public void createThumbnail(String filename) throws IOException {
        String fileExt = FilenameUtils.getExtension(filename);
        if (ArrayUtils.contains(videoExts, fileExt)) {
            File tempFile = null;
            try {
                tempFile = File.createTempFile("ffmpeg", ".jpg");
                String in = getPath(filename);
                Process process = Runtime.getRuntime().exec(new String[]{
                    "ffmpeg",
                    "-i",
                    in,
                    "-ss",
                    "00:00:01",
                    "-frames:v",
                    "1",
                    "-y",
                    tempFile.getAbsolutePath(),
                });

                int result = process.waitFor();
                if (result != 0) {
                    throw new RuntimeException("抓取视频图像时发生错误");
                }
                BufferedImage image = ImageIO.read(tempFile);
                for (int i = 0; i < 4; i++) {
                    super.createThumbnail(image, filename, i);
                }
            } catch (Exception e) {
                log.error("对视频进行缩略图时出现错误");
                return;
            } finally {
                FileUtils.deleteQuietly(tempFile);
            }
        } else {
            super.createThumbnail(filename);
        }
    }

    private String getPath(String filename, String... path) {
        String director = FilenameUtils.concat(config.getPath(), StringUtils.join(path, File.separator));
        return FilenameUtils.concat(director, filename);
    }
}
