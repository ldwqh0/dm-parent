package com.dm.fileserver.service.impl;

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

import com.dm.fileserver.config.FileConfig;
import com.dm.fileserver.service.ThumbnailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LocalThumbnailServiceImpl implements ThumbnailService {

	@Autowired
	private FileConfig fileConfig;

	private String l1;

	private String l2;

	private static final String[] imgExt = { "jpg", "png", "bmp" };

	@Override
	public void createThumbnail(String filename) {
		String ext = com.dm.fileserver.util.DmFileUtils.getExt(filename);
		if (ArrayUtils.contains(imgExt, ext)) {
			try (InputStream iStream = new FileInputStream(getPath(filename));
					OutputStream oStream = getOutputStream(1, filename);
					OutputStream oStream2 = getOutputStream(2, filename)) {
				Image image = ImageIO.read(iStream);
				int o_w = image.getWidth(null);
				int o_h = image.getHeight(null);
				double scale = Math.max(o_w * 1.0 / 512, o_h * 1.0 / 512);
				double scale2 = Math.max(o_w * 1.0 / 256, o_h * 1.0 / 256);
				int n_w = Double.valueOf(o_w / scale).intValue();
				int n_h = Double.valueOf(o_h / scale).intValue();
				int n2_w = Double.valueOf(o_w / scale2).intValue();
				int n2_h = Double.valueOf(o_h / scale2).intValue();
				BufferedImage bi = new BufferedImage(n_w, n_h, BufferedImage.TYPE_INT_RGB);
				BufferedImage bi2 = new BufferedImage(n2_w, n2_h, BufferedImage.TYPE_INT_RGB);
				bi.getGraphics().drawImage(image, 0, 0, n_w, n_h, null);
				bi2.getGraphics().drawImage(image, 0, 0, n2_w, n2_h, null);
				ImageIO.write(bi, "jpg", oStream);
				ImageIO.write(bi2, "jpg", oStream2);
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
		l1 = path + File.separator + "th1" + File.separator;
		l2 = path + File.separator + "th2" + File.separator;
		FileUtils.forceMkdir(new File(l1));
		FileUtils.forceMkdir(new File(l2));
	}

	private OutputStream getOutputStream(int level, String path) throws FileNotFoundException {
		switch (level) {
		case 1:
			return new FileOutputStream(l1 + path + ".jpg");
		case 2:
			return new FileOutputStream(l2 + path + ".jpg");
		}
		return null;
	}

	private String getPath(String filename) {
		return fileConfig.getPath() + File.separator + filename;
	}

	private String getPath(String filename, int level) {
		return fileConfig.getPath() + File.separator + "th" + level + File.separator + filename + ".jpg";
	}

}
