package com.dm.file.controller;

import com.dm.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    @ApiOperation("预览文件")
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
            @RequestHeader(value = "range", required = false) String range,
            WebRequest request) {
        // 统一下载接口和预览接口的实现
        return download(id, null, range, request);
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
                        .map(lastModify -> this.<InputStreamResource>buildNotModified())
                        .orElseGet(() -> this.buildThumbnailResponse(file, level)))
                .orElseGet(this::buildNotFount);
    }

    private ResponseEntity<InputStreamResource> buildThumbnailResponse(FileInfo file, int level) {
        try {
            return ResponseEntity.ok().lastModified(file.getLastModifiedDate().get())
                    .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(thumbnailService.getStream(file.getPath(), level)));
        } catch (FileNotFoundException e) {
            return this.buildNotFount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件下载
     *
     * @param id
     * @param request
     * @return
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
            WebRequest request) {
        // 如果文件不存在，直接返回404
        return fileService.findById(id).filter(file -> {
            try {
                return fileStorageService.exist(file.getPath());
            } catch (Exception e) {
                log.error("检查文件是否存在时发现错误", e);
                return false;
            }
        }).map(file -> { // 如果文件存在，执行读取文件的流程
            return file.getLastModifiedDate()
                    .filter(lastModify -> this.checkNotModified(request, lastModify))
                    .map(lastModify -> this.<InputStreamResource>buildNotModified()) // 入股检测到文件未被修改，返回未修改的响应体
                    .orElseGet(() -> buildDownloadBody(file, getRanges(range, file), userAgent));
        }).orElseGet(this::buildNotFount);
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
    private ResponseEntity<InputStreamResource> buildDownloadBody(FileInfo file, List<Range> ranges, String userAgent) {
        try {
            String fileName = file.getFilename();
            long fileSize = file.getSize();
            InputStreamResource body = null;
            int status = 200;
            long contentLength = fileSize; // 内容长度
            String contentRange = ""; // 响应范围
            String codeFileName = null;
            String path = file.getPath();
            String ext = StringUtils.substringAfterLast(fileName, ".").toLowerCase();
            String contentType = config.getMime(ext);
            if (CollectionUtils.isEmpty(ranges)) {
                body = new InputStreamResource(fileStorageService.getInputStream(path));
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

            BodyBuilder responseBuilder = ResponseEntity.status(status)
                    .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                    .header("Content-Type", contentType)
                    .header("Accept-Ranges", "bytes")
                    .contentLength(contentLength);

            // -----------------计算文件名开始-----------------------
            if (StringUtils.isNotBlank(userAgent)) {
                if (StringUtils.contains(userAgent, "Trident")
                        || StringUtils.contains(userAgent, "Edge")
                        || StringUtils.contains(userAgent, "MSIE")) {
                    codeFileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    codeFileName = new String(fileName.getBytes(), "ISO8859-1");
                }
            }
            if (StringUtils.isNotBlank(codeFileName)) {
                responseBuilder.header("Content-Disposition", "attachment; filename=" + codeFileName);
            }
            // -----------------计算文件名结束-----------------------

            if (StringUtils.isNotEmpty(contentRange)) {
                responseBuilder.header("Content-Range", contentRange);
            }
            file.getLastModifiedDate().ifPresent(responseBuilder::lastModified);
            return responseBuilder.body(body);
        } catch (Exception e) {
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
