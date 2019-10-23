package com.dm.file.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dm.file.config.FileConfig;
import com.dm.file.converter.FileInfoConverter;
import com.dm.file.dto.FileInfoDto;
import com.dm.file.entity.FileInfo;
import com.dm.file.service.FileInfoService;
import com.dm.file.service.FileStorageService;
import com.dm.file.service.ThumbnailService;
import com.dm.file.util.DmFileUtils;

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

    @GetMapping(value = "{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_JSON_UTF8_VALUE,
    })
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
            if (!Objects.isNull(file)) {
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
            "image/webp",
            "image/*",
            "*/*" })
    public ResponseEntity<InputStreamResource> preview(@PathVariable("id") UUID id, WebRequest request) {
        try {
            FileInfo file = fileService.get(id).get();
            Optional<ZonedDateTime> lastModify = file.getLastModifiedDate();
            if (lastModify.isPresent() && request.checkNotModified(lastModify.get().toInstant().toEpochMilli())) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
            } else {
                String fileName = file.getFilename();
                String ext = StringUtils.substringAfterLast(fileName, ".").toLowerCase();
                return ResponseEntity.ok().lastModified(lastModify.get())
                        .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                        .contentType(MediaType.valueOf(config.getMime(ext)))
                        .body(new InputStreamResource(fileStorageService.getInputStream(file.getPath())));
            }
        } catch (Exception e) {
            log.error("预览文件时出错", e);
            return ResponseEntity.notFound().build();
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
    public ResponseEntity<InputStreamResource> preview(
            @PathVariable("id") UUID id,
            @RequestParam(value = "level", defaultValue = "1") int level,
            WebRequest request) {

        Optional<FileInfo> file = fileService.get(id);
        if (file.isPresent()) {
            Optional<ZonedDateTime> lastModify = file.get().getLastModifiedDate();
            if (lastModify.isPresent() && request.checkNotModified(lastModify.get().toInstant().toEpochMilli())) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
            } else {
                try {
                    return ResponseEntity.ok().lastModified(lastModify.get())
                            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(new InputStreamResource(thumbnailService.getStream(file.get().getPath(), level)));
                } catch (FileNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 文件下载
     * 
     * @param id
     * @param request
     * @return
     */
    @GetMapping(value = "{id}", params = { "download" })
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") UUID id,
            @RequestHeader("user-agent") String userAgent,
            WebRequest request) {
        try {
            FileInfo file = fileService.get(id).get();
            Optional<ZonedDateTime> lastModify = file.getLastModifiedDate();
            if (lastModify.isPresent() && request.checkNotModified(lastModify.get().toInstant().toEpochMilli())) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
            } else {
                String fileName = file.getFilename();
                String ext = StringUtils.substringAfterLast(fileName, ".").toLowerCase();
                String codeFileName = null;
                if (StringUtils.contains(userAgent, "Trident") || StringUtils.contains(userAgent, "Edge")
                        || StringUtils.contains(userAgent, "MSIE")) {
                    codeFileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    codeFileName = new String(fileName.getBytes(), "ISO8859-1");
                }

                return ResponseEntity.ok().lastModified(lastModify.get())
                        .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                        .header("Content-Disposition", "attachment; filename=" + codeFileName)
                        .contentType(MediaType.valueOf(config.getMime(ext)))
                        .body(new InputStreamResource(fileStorageService.getInputStream(file.getPath())));
            }
        } catch (Exception e) {
            log.error("预览文件时出错", e);
            return ResponseEntity.notFound().build();
        }
    }
}
