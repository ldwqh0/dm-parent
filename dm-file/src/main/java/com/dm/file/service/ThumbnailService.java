package com.dm.file.service;

import java.io.IOException;
import java.io.InputStream;

public interface ThumbnailService {
	/**
	 * 创建缩略图
	 * 
	 * @param file
	 */
	public void createThumbnail(String path);

	/**
	 * 根据文件ID和缩略图级别获取缩略图
	 * 
	 * @param fileId
	 * @param level
	 * @return
	 * @throws IOException
	 */
	public InputStream getStream(String path, int level) throws IOException;
}
