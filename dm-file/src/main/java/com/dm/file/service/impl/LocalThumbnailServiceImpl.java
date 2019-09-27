package com.dm.file.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dm.file.config.FileConfig;
import com.dm.file.service.ThumbnailService;
import com.dm.file.util.DmFileUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LocalThumbnailServiceImpl implements ThumbnailService {

	@Autowired
	private FileConfig fileConfig;

	private static final String[] imgExt = { "jpg", "png", "bmp" };
	private final int[][] levelScales = { { 128, 128 }, { 256, 256 }, { 512, 512 }, { 1080, 1920 } };

	private void createThumbnail(Image image, String filename, int level) throws FileNotFoundException, IOException {
		try (OutputStream oStream = new FileOutputStream(getPath(filename, level))) {
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
	public void createThumbnail(String filename) {
		String ext = DmFileUtils.getExt(filename);
		if (ArrayUtils.contains(imgExt, ext)) {
			try (InputStream iStream = new FileInputStream(getPath(filename))) {
				Image image = ImageIO.read(iStream);
				for (int i = 0; i < levelScales.length; i++) {
					createThumbnail(image, filename, i);
				}
			} catch (Exception e) {
				log.error("创建文件缩略图时出错", e);
			}
		}
	}

	@Override
	public InputStream getStream(String filename, int level) throws IOException {
		return new FileInputStream(getPath(filename, level));
	}

	@PostConstruct
	public void initService() throws Exception {
		String path = fileConfig.getPath();
		for (int i = 0; i < levelScales.length; i++) {
			String levelPath = path + File.separator + "th" + i + File.separator;
			FileUtils.forceMkdir(new File(levelPath));
		}
	}

	private String getPath(String filename) {
		return fileConfig.getPath() + File.separator + filename;
	}

	private String getPath(String filename, int level) {
		return fileConfig.getPath() + File.separator + "th" + level + File.separator + filename + ".jpg";
	}

}
