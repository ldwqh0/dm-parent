package com.dm.fileserver.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dm.fileserver.config.FileConfig;
import com.dm.fileserver.converter.FileInfoConverter;
import com.dm.fileserver.dto.FileInfoDto;
import com.dm.fileserver.entity.FileInfo;
import com.dm.fileserver.service.FileInfoService;
import com.dm.fileserver.service.FileStorageService;
import com.dm.fileserver.service.ThumbnailService;
import com.dm.fileserver.util.DmFileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("files")
public class FileController {

	@Autowired
	private FileInfoService fileService;

	@Autowired
	@Lazy
	private ThumbnailService thumbnailService;

	@Autowired
	private FileInfoConverter fileInfoConverter;

	@Autowired
	private FileConfig config;

	@Autowired
	private FileStorageService fileStorageService;

	@GetMapping(value = "{id}")
	public FileInfoDto get(@PathVariable("id") UUID id) {
		return fileInfoConverter.toDto(fileService.get(id));
	}

	@PostMapping
//	@ApiOperation("上传文件")
	public List<FileInfoDto> upload(MultipartHttpServletRequest request) throws Exception {
		List<FileInfo> result = new ArrayList<FileInfo>();
		Iterator<String> filenames = request.getFileNames();
		while (filenames.hasNext()) {
			MultipartFile file = request.getFile(filenames.next());
			FileInfoDto infoDto = new FileInfoDto();
			if (StringUtils.isNotBlank(file.getOriginalFilename())) {
				infoDto.setFilename(DmFileUtils.getOriginalFilename(file.getOriginalFilename()));
			}
			infoDto.setSize(file.getSize());
			FileInfo file_ = fileService.save(file.getInputStream(), infoDto);
			result.add(file_);
			// 创建缩略图
			thumbnailService.createThumbnail(file_.getPath());
		}
		return fileInfoConverter.toDto(result);
	}

	/**
	 * 分块上传文件
	 * 
	 * @param chunkIndex
	 * @param tempId
	 * @param chunkCount
	 * @param filename
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping(headers = { "chunk-index" })
//	@ApiOperation("文件分块上传文件")''
	public FileInfoDto upload(
			@RequestHeader("chunk-index") Long chunkIndex, // 块
			@RequestHeader("file-id") String tempId,
			@RequestHeader("chunk-count") int chunkCount,
			@RequestParam("filename") String filename,
			MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Collection<MultipartFile> files = request.getFileMap().values();
		if (CollectionUtils.isNotEmpty(files)) {
			MultipartFile mFile = files.iterator().next();
			String tempName = config.getTempPath() + tempId + "." + chunkIndex;
			mFile.transferTo(new File(tempName));
		}
		// 这个标记代表块完成
		if (chunkIndex == chunkCount - 1) {
			File target = new File(config.getTempPath() + tempId + "." + StringUtils.substringAfter(filename, "."));
			File[] src = new File[chunkCount];
			for (int i = 0; i < chunkCount; i++) {
				src[i] = new File(config.getTempPath() + tempId + "." + i);
			}
			FileInfoDto fileInfo = new FileInfoDto();
			fileInfo.setFilename(filename);
			fileInfo.setSize(target.length());
			FileInfo _result = fileService.save(src, fileInfo);
			FileUtils.forceDelete(target);
			return fileInfoConverter.toDto(_result);
		} else {
			return null;
		}

	}

//	@ApiOperation("预览文件")
	// 使用produces指定可以接受的accept类型，当accept中包含如下信息时，返回图片
	@GetMapping(value = "{id}", produces = {
			MediaType.IMAGE_GIF_VALUE,
			MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE,
			"image/*" })
	public void preview(@PathVariable("id") UUID id, HttpServletResponse response) {
		try {
			FileInfo file = fileService.get(id).get();
			String fileName = file.getFilename();
			String ext = StringUtils.substringAfterLast(fileName, ".").toLowerCase();
			if (MapUtils.isNotEmpty(config.getMime()) && StringUtils.isNotBlank(ext)) {
				response.setContentType(config.getMime().get(ext));
			}
			try (InputStream is = fileStorageService.getInputStream(file.getPath());
					OutputStream os = response.getOutputStream();) {
				IOUtils.copy(is, os);
			}
		} catch (Exception e) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			log.error("预览文件时出错", e);
		}
	}

	/**
	 * 获取文件的缩略图
	 * 
	 * @param id       文件的ID号
	 * @param level    缩略图的级别
	 * @param response
	 */
	@GetMapping(value = "thumbnails/{id}")
	public void preview(
			@PathVariable("id") UUID id,
			@RequestParam(value = "level", defaultValue = "1") int level,
			HttpServletResponse response) {
		Optional<FileInfo> file = fileService.get(id);
		if (file.isPresent()) {
			try (InputStream iStream = thumbnailService.getStream(file.get().getPath(), level);
					OutputStream oStream = response.getOutputStream()) {
				IOUtils.copy(iStream, oStream);
				response.setContentType("image/jpeg");
			} catch (FileNotFoundException e) {
				response.setStatus(404);
			} catch (IOException e) {
				response.setStatus(500);
				log.error("预览文件失败", e);
			}
		} else {
			response.setStatus(404);
		}
	}

}
