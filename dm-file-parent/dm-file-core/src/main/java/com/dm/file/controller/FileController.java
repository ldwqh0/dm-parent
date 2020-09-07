package com.dm.file.controller;

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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping("files")
@RestController
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
        FileInfo file_ = fileService.save(file, infoDto);
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
            // 创建文件缩略图
            thumbnailService.createThumbnail(_result.getPath());
            for (File file : src) {
                try {
                    FileUtils.forceDelete(file);
                    // 删除临时文件
                } catch (Exception e) {
                    log.error("删除临时文件时发生异常", e);
                }
            }
            return fileInfoConverter.toDto(_result);
        } else {
            return null;
        }
    }

    /**
     * 包含X-Real-IP请求头的请求全部走这个地方<br >
     * 这代表是一个从nginx转发的请求
     * 
     * @return
     */
    @GetMapping(value = "{id}", headers = { "X-Real-IP" }, produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp",
            "image/*",
            "*/*",
            "!application/json", "!text/plain" })
    public ResponseEntity<?> preview(
            @PathVariable("id") UUID id,
            WebRequest request) {
        return fileService.findById(id)
                .map(FileInfo::getPath)
                .map(this::buildAccessRedirectResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 基于Nginx的缩略图下载
     * 
     * @param id
     * @param level
     * @return
     */
    @GetMapping(value = "thumbnails/{id}", headers = { "X-Real-IP" }, produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp",
            "image/*",
            "*/*",
            "!application/json", "!text/plain"
    })
    public ResponseEntity<?> preveiwThumbnails(@PathVariable("id") UUID id,
            @RequestParam(value = "level", defaultValue = "1") int level) {
        return fileService.findById(id)
                .map(FileInfo::getPath)
                // 构建缩略图路径
                .map(path -> StringUtils.join("th", level, "/", path, ".jpg"))
                .map(this::buildAccessRedirectResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 构建一个重定向，指定到前端配置的下载目录
     * 
     * @param path
     * @return
     */
    private ResponseEntity<?> buildAccessRedirectResponse(String path) {
        return ResponseEntity.ok()
                .header("X-Accel-Redirect", config.getAccelRedirectPath() + path)
                .build();
    }

    // 使用produces指定可以接受的accept类型，当accept中包含如下信息时，返回图片
    @ApiOperation("预览/下载文件")
    @GetMapping(value = "{id}", produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp",
            "image/*",
            "*/*",
            "!application/json",
            "!text/plain" })
    public ResponseEntity<? extends Object> preview(
            @PathVariable("id") UUID id,
            @RequestParam(value = "download", required = false) String download,
            @RequestHeader(value = "range", required = false) String range,
            @RequestHeader("user-agent") String userAgent,
            WebRequest request) {
        // 统一下载接口和预览接口的实现
        final String agentUse = download == null ? null : userAgent;
        return fileService.findById(id)
                // 判断文件是否存在
                .filter(item -> fileStorageService.exist(item.getPath()))
                .map(fileItem -> fileItem.getLastModifiedDate()
                        .filter(lastModified -> checkNotModified(request, lastModified))
                        .map(lastModified -> this.<Resource>buildNotModified())
                        .orElseGet(() -> this.buildDownloadBody(fileItem, getRanges(range, fileItem), agentUse)))
                .orElseGet(this::buildNotFount);
    }

    /**
     * 获取文件的缩略图
     *
     * @param id      文件的ID号
     * @param level   缩略图的级别
     * @param request 请求详情
     */
    @GetMapping(value = "thumbnails/{id}", produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp",
            "image/*",
            "*/*", "!application/json", "!text/plain" })
    public ResponseEntity<?> preview(
            @PathVariable("id") UUID id,
            @RequestParam(value = "level", defaultValue = "1") int level,
            @RequestHeader(value = "range", required = false) String range,
            WebRequest request) {
        return fileService.findById(id).map(
                file -> file.getLastModifiedDate()
                        .filter(lastModify -> this.checkNotModified(request, lastModify))
                        .map(lastModify -> this.<Resource>buildNotModified())
                        .orElseGet(() -> this.buildThumbnailResponse(file, level)))
                .orElseGet(this::buildNotFount);
    }

    private ResponseEntity<Resource> buildThumbnailResponse(FileInfo file, int level) {
        return ResponseEntity.ok().lastModified(file.getLastModifiedDate().get())
                .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                .contentType(MediaType.IMAGE_JPEG)
                .body(thumbnailService.getResource(file.getPath(), level));
    }

    /**
     * 根据Range Header获取Range
     *
     * @param ranges
     * @param file
     * @return
     * @throws RangeNotSatisfiableException
     */
    private List<Range> getRanges(String ranges, FileInfo file) {
        if (StringUtils.isNotBlank(ranges) && StringUtils.startsWith(ranges, "bytes=")) {
            String rangeStr = StringUtils.removeStart(ranges, "bytes=");
            String[] rangeStrings = rangeStr.split(",");
            return Range.of(rangeStrings, file.getSize());
        } else {
            return Collections.emptyList();
        }
    }

    // 构建下载响应体
    private ResponseEntity<Resource> buildDownloadBody(FileInfo file, List<Range> ranges, String userAgent) {
        try {
            String fileName = file.getFilename();
            long fileSize = file.getSize();
            Resource body = null;
            long contentLength = fileSize; // 内容长度
            String contentRange = ""; // 响应范围
            String codeFileName = null;
            String path = file.getPath();
            String ext = DmFileUtils.getExt(fileName);
            String contentType = config.getMime(ext);
            Range range = null;
            // 计算range
            if (ranges.size() < 1) {
                log.info("no range checked");
            } else if (ranges.size() == 1) {
                range = ranges.get(0);
                contentLength = range.getContentLength();
            } else {
                log.info("multi range not implement");
                // TODO 返回多个range待实现
            }
            // -----------------计算文件名开始-----------------------
            if (StringUtils.isNotBlank(userAgent)) {
                if (StringUtils.contains(userAgent, "Trident")
                        || StringUtils.contains(userAgent, "Edge")
                        || StringUtils.contains(userAgent, "MSIE")) {
                    codeFileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    codeFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                }
            }
            // -----------------计算文件名结束-----------------------
            BodyBuilder bodyBuilder = null;
            if (range == null) {
                body = fileStorageService.getResource(path);
            } else {
                body = fileStorageService.getResource(path, range.getStart(), range.getEnd() + 1);
            }

            if (body instanceof FileSystemResource) {
                bodyBuilder = ResponseEntity.ok().header("Content-Type", contentType);
            } else {
                if (range == null) {
                    bodyBuilder = ResponseEntity.ok();
                } else {
                    bodyBuilder = ResponseEntity.status(206);
                    contentRange = "bytes " + range.getStart() + "-" + range.getEnd() + "/" + fileSize;
                    bodyBuilder.header("Content-Range", contentRange);
                }
                bodyBuilder.header("Content-Type", contentType);
                bodyBuilder.header("Accept-Ranges", "bytes")
                        .contentLength(contentLength);
            }
            if (StringUtils.isNotBlank(codeFileName)) {
                bodyBuilder.header("Content-Disposition", "attachment; filename=" + codeFileName);
            }
            file.getLastModifiedDate().ifPresent(bodyBuilder::lastModified);
            return bodyBuilder.body(body);
        } catch (UnsupportedEncodingException e) {
            log.error("构建响应体时发生异常", e);
            throw new RuntimeException(e);
        }
    }

    private boolean checkNotModified(WebRequest request, ZonedDateTime lastModified) {
        return request.checkNotModified(lastModified.toInstant().toEpochMilli());
    }

    /**
     * 构建一个未找到的响应体
     */
    private <T> ResponseEntity<T> buildNotFount() {
        return ResponseEntity.notFound().build();
    }

    /**
     * 构建一个未修改的响应体
     *
     * @param any
     * @param <T>
     * @return
     */
    private <T> ResponseEntity<T> buildNotModified() {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}
