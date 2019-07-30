package com.dm.file.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "file")
public class FileConfig {
	private String path;
	private Map<String, String> mime;
	private String tempPath;

	public FileConfig() {
		this.path = System.getProperty("user.home") + File.separator + "uploads";
		tempPath = System.getProperty("java.io.tmpdir") + File.separator + System.currentTimeMillis() + File.separator;
		this.mime = new HashMap<String, String>();
		this.mime.put("jpg", "image/jpeg");
		this.mime.put("jpeg", "image/jpeg");
		this.mime.put("png", "image/png");
		this.mime.put("bmp", "image/bmp");
		this.mime.put("gif", "image/gif");
		this.mime.put("txt", "text/plain");
		this.mime.put("pdf", "application/pdf");
		this.mime.put("doc", "application/msword");
		this.mime.put("xls", "application/vnd.ms-excel");
		this.mime.put("zip", "application/zip");
		this.mime.put("html", "text/xml");
		this.mime.put("xhtml", "application/xhtml+xml");
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMime(String ext) {
		String result = this.mime.get(ext);
		return StringUtils.isEmpty(result) ? "application/octet-stream" : result;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public void setMime(Map<String, String> mime) {
		if (MapUtils.isNotEmpty(mime)) {
			for (Map.Entry<String, String> entity : mime.entrySet()) {
				this.mime.put(entity.getKey(), entity.getValue());
			}
		}
	}
}
