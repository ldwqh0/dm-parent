package com.dm.fileserver.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dm.fileserver.config.FileConfig;
import com.dm.fileserver.service.FileStorageService;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

	@Autowired
	private FileConfig config;

	@Override
	public boolean save(String path, InputStream inputStream) throws IOException {
		FileUtils.copyToFile(inputStream, new File(getPath(path)));
		return true;
	}

	@Override
	public boolean save(String path, File file) throws IOException {
		FileUtils.copyFile(file, new File(getPath(path)));
		return true;
	}

	@Override
	public boolean delete(String path) throws IOException {
		return FileUtils.deleteQuietly(new File(getPath(path)));
	}

	@Override
	public boolean exist(String path) throws Exception {
		return new File(getPath(path)).exists();
	}

	@Override
	public InputStream getInputStream(String path) throws Exception {
		return new FileInputStream(getPath(path));
	}

	private String getPath(String filename) {
		return config.getPath() + File.separator + filename;
	}

	@Override
	public boolean save(String path, File[] src) throws IOException {
		File target = new File(getPath(path));
		try (OutputStream out = FileUtils.openOutputStream(target, true)) {
			for (File file : src) {
				FileUtils.copyFile(file, out);
			}
		}
		return true;
	}

	/**
	 * 初始化的时候，创建目录
	 */
	@PostConstruct
	public void initStorage() throws Exception {
		String path = config.getPath();
		File file = new File(path);
		FileUtils.forceMkdir(file);
	}
}