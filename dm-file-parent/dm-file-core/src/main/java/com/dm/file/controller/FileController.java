package com.dm.file.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.dm.common.exception.DataNotExistException;
import com.dm.file.config.FileConfig;
import com.dm.file.converter.FileInfoConverter;
import com.dm.file.dto.FileInfoDto;
import com.dm.file.dto.Range;
import com.dm.file.entity.FileInfo;
import com.dm.file.exception.RangeNotSatisfiableException;
import com.dm.file.service.FileInfoService;
import com.dm.file.service.FileStorageService;
import com.dm.file.service.ThumbnailService;
import com.dm.file.util.DmFileUtils;

import io.swagger.annotations.ApiOperation;
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
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public FileInfoDto get(@PathVariable("id") UUID id) {
        return fileInfoConverter.toDto(fileService.findById(id).orElseThrow(DataNotExistException::new));
    }

    @PostMapping
    @ApiOperation("上传文件")
    public FileInfoDto upload(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        FileInfoDto infoDto = new FileInfoDto();
        if (StringUtils.isNotBlank(originalFilename)) {
            infoDto.setFilename(DmFileUtils.getOriginalFilename(originalFilename));
        }
        infoDto.setSize(file.getSize());
        FileInfo file_ = fileService.save(file.getInputStream(), infoDto);
        thumbnailService.createThumbnail(file_.getPath());
        return fileInfoConverter.toDto(file_);
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
    @ApiOperation("文件分块上传文件")
    public FileInfoDto upload(
            @RequestHeader("chunk-index") Long chunkIndex,
            @RequestHeader("file-id") String tempId,
            @RequestHeader("chunk-count") int chunkCount,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile mFile) throws Exception {
        String tempName = config.getTempPath() + tempId + "." + chunkIndex;
        mFile.transferTo(new File(tempName));
        if (chunkIndex == chunkCount - 1) {
            long length = 0;
            File[] src = new File[chunkCount];
            for (int i = 0; i < chunkCount; i++) {
                src[i] = new File(config.getTempPath() + tempId + "." + i);
                length += src[i].length();
            }
            FileInfoDto fileInfo = new FileInfoDto();
            fileInfo.setFilename(filename);
            fileInfo.setSize(length);
            FileInfo _result = fileService.save(src, fileInfo);
            for (File file : src) {
                try {
                    FileUtils.forceDelete(file);
                    // 删除临时文件
                } catch (Exception e) {
                }
            }
            return fileInfoConverter.toDto(_result);
        } else {
            return null;
        }
    }

    // 使用produces指定可以接受的accept类型，当accept中包含如下信息时，返回图片
    @ApiOperation("预览文件")
    @GetMapping(value = "{id}", produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp",
            "image/*",
            "*/*" })
    public ResponseEntity<InputStreamResource> preview(@PathVariable("id") UUID id, WebRequest request) {
        try {
            FileInfo file = fileService.findById(id).get();
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
    @GetMapping(value = "thumbnails/{id}", produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp",
            "image/*",
            "*/*" })
    public ResponseEntity<?> preview(
            @PathVariable("id") UUID id,
            @RequestParam(value = "level", defaultValue = "1") int level,
            WebRequest request) {
        Optional<FileInfo> fileOptional = fileService.findById(id);
        return fileOptional.map((file) -> {
            Optional<ZonedDateTime> lastModify = file.getLastModifiedDate();
            if (lastModify.isPresent() && request.checkNotModified(lastModify.get().toInstant().toEpochMilli())) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
            } else {
                try {
                    return ResponseEntity.ok().lastModified(lastModify.get())
                            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(new InputStreamResource(thumbnailService.getStream(file.getPath(), level)));
                } catch (FileNotFoundException e) {
                    return ResponseEntity.notFound().build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 文件下载
     * 
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    // http 断点续传，需要处理的头包括If-Range，
    // 请求头 Range，单次请求的下载范围，包括 xx-xx指定范围，xx-从某个位置开始到结尾，-xx最后的位置,可以包含多个块
    // 响应 如果一次返回了所有的内容，需要返回200状态码
    // 响应 http 206状态码，如果不断点，
    // 响应 Content-Range，如果头包括Range,这里返回 格式如下 0-1024/5353522
    // 响应 Content-Length，本次响应的长度
    // 响应 Last-Modified ,最后修改时间
    // 响应 Content-Type, 内容类型，如果是多个范围请求(多个Range)的响应
    // 响应 Accept-Ranges, bytes,表示以字节为单位进行续传
    @GetMapping(value = "{id}", params = { "download" })
    public ResponseEntity<? extends Object> download(
            @PathVariable("id") UUID id,
            @RequestHeader("user-agent") String userAgent,
            @RequestHeader(value = "range", required = false) String range,
            WebRequest request) throws Exception {
        Optional<FileInfo> fileOptional = fileService.findById(id);
        BodyBuilder responseBuilder = null;
        // 如果文件存在，且文件路径存在，才进行下面的操作
        if (fileOptional.isPresent() && fileStorageService.exist(fileOptional.get().getPath())) {
            FileInfo file = fileOptional.get();
            String fileName = file.getFilename();
            String codeFileName = null;
            long fileSize = file.getSize();
            long contentLength = fileSize; // 内容长度
            String contentRange = ""; // 响应范围
            int status = 200;
            String ext = StringUtils.substringAfterLast(fileName, ".").toLowerCase();
            String path = file.getPath();
            String contentType = config.getMime(ext);
            Object body = null;
            List<Range> ranges = getRanges(range, file);
            Optional<ZonedDateTime> lastModify = file.getLastModifiedDate();
            // 如果没有分块需求
            if (CollectionUtils.isEmpty(ranges)) {
                // 检查缓存
                if (lastModify.isPresent() && request.checkNotModified(lastModify.get().toInstant().toEpochMilli())) {
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
                } else { // 如果有分块需求,直接返回文件体
                    body = new InputStreamResource(fileStorageService.getInputStream(file.getPath()));
                }
            } else if (ranges.size() == 1) {
                status = 206; // 将响应设置为206
                Range r = ranges.get(0);
                contentLength = r.getContentLength();
                // 将一个流的最大读取值限定在一定的范围之内
                BoundedInputStream bis = new BoundedInputStream(fileStorageService.getInputStream(path),
                        r.getEnd() + 1);
                long start = bis.skip(r.getStart());
                contentRange = "bytes " + start + "-" + r.getEnd() + "/" + fileSize;
                body = new InputStreamResource(bis);
            } else if (ranges.size() > 1) {
                log.info("multi range not implement");
                // TODO 返回多个range待实现
            }
            // 计算文件名
            if (StringUtils.contains(userAgent, "Trident")
                    || StringUtils.contains(userAgent, "Edge")
                    || StringUtils.contains(userAgent, "MSIE")) {
                codeFileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                codeFileName = new String(fileName.getBytes(), "ISO8859-1");
            }
            responseBuilder = ResponseEntity.status(status)
                    .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                    .header("Content-Disposition", "attachment; filename=" + codeFileName)
                    .header("Content-Type", contentType)
                    .header("Accept-Ranges", "bytes")
                    .contentLength(contentLength);
            if (StringUtils.isNotEmpty(contentRange)) {
                responseBuilder.header("Content-Range", contentRange);
            }
            lastModify.ifPresent(responseBuilder::lastModified);
            return responseBuilder.body(body);
            // 如果文件不存在，或者路径不存在，直接返回404
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据Range Header获取Range
     *
     * @param ranges
     * @param file
     * @return
     * @throws RangeNotSatisfiableException
     */
    private List<Range> getRanges(String ranges, FileInfo file) throws RangeNotSatisfiableException {
        if (StringUtils.isNotBlank(ranges) && StringUtils.startsWith(ranges, "bytes=")) {
            String rangeStr = StringUtils.removeStart(ranges, "bytes=");
            String[] rangeStrs = rangeStr.split(",");
            return Range.of(rangeStrs, file.getSize());
        } else {
            return Collections.emptyList();
        }
    }

}
