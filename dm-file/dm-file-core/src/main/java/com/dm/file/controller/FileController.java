package com.dm.file.controller;

import com.dm.common.exception.DataNotExistException;
import com.dm.file.config.FileConfig;
import com.dm.file.converter.FileInfoConverter;
import com.dm.file.dto.FileInfoDto;
import com.dm.file.dto.PackageFileDto;
import com.dm.file.dto.Range;
import com.dm.file.entity.FileInfo;
import com.dm.file.exception.RangeNotSatisfiableException;
import com.dm.file.service.FileInfoService;
import com.dm.file.service.FileStorageService;
import com.dm.file.service.PackageFileService;
import com.dm.file.service.ThumbnailService;
import com.dm.file.util.DmFileUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequestMapping("files")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileInfoService fileService;

    private final ThumbnailService thumbnailService;

    private final FileInfoConverter fileInfoConverter;

    private final FileConfig config;

    private final FileStorageService fileStorageService;

    private final PackageFileService packageFileService;


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

    @GetMapping(params = {"filename", "sha256", "md5"})
    public FileInfoDto findByNameAndHash(@RequestParam("filename") String filename,
                                         @RequestParam("sha256") String sha256,
                                         @RequestParam("md5") String md5) {
        return fileService.findByNameAndHash(filename, sha256, md5).map(fileInfoConverter::toDto).orElse(null);
    }

    @PostMapping(params = {"filename", "sha256", "md5"})
    public FileInfoDto upload(@RequestParam("md5") String md5,
                              @RequestParam("sha256") String sha256,
                              @RequestParam("filename") String filename,
                              @RequestParam("file") MultipartFile chunkFile) {
        // TODO 待处理
        Optional<FileInfo> file = fileService.findByNameAndHash(filename, sha256, md5);
        if (file.isPresent()) {
            // TODO 如果文件已经存在，将上传的信息添加到文件的尾部
        } else {
            // TODO　如果文件不存在，保存新的文件
        }
        return null;
    }

    /**
     * 分块上传文件
     *
     * @param chunkIndex 当前块的索引
     * @param tempId     临时文件ID
     * @param chunkCount 文件分块总数
     * @param filename   文件名
     * @return 保存后的文件信息
     * @throws Exception 保存异常
     */
    @PostMapping(headers = {"chunk-index"})
    @ApiOperation("文件分块上传文件")
    public FileInfoDto upload(
        @RequestHeader("chunk-index") int chunkIndex,
        @RequestHeader("file-id") String tempId,
        @RequestHeader("chunk-count") int chunkCount,
        @RequestParam("filename") String filename,
        @RequestParam("file") MultipartFile chunkFile) throws Exception {
        // 保存临时文件
        chunkFile.transferTo(getTempChunkPath(tempId, chunkIndex));
        // 如果临时文件上传完成，组装所有的文件
        if (chunkIndex == chunkCount - 1) {
            long length = 0;
            Path[] tempFiles = new Path[chunkCount];
            for (int i = 0; i < chunkCount; i++) {
                Path filePath = getTempChunkPath(tempId, i);
                length += Files.size(filePath);
                tempFiles[i] = filePath;
            }
            FileInfoDto fileInfo = new FileInfoDto();
            fileInfo.setFilename(filename);
            fileInfo.setSize(length);
            FileInfo _result = fileService.save(tempFiles, fileInfo);
            // 创建文件缩略图
            thumbnailService.createThumbnail(_result.getPath());
            for (Path file : tempFiles) {
                try {
                    // 删除临时文件
                    Files.deleteIfExists(file);
                } catch (Exception e) {
                    log.error("删除临时文件时发生异常", e);
                }
            }
            return fileInfoConverter.toDto(_result);
        } else {
            return null;
        }
    }

    private Path getTempChunkPath(String tempId, int chunkIndex) {
        Path target = Paths.get(config.getTempPath(), StringUtils.join(tempId, ".", chunkIndex));
        Path parent = target.getParent();
        if (Objects.nonNull(parent) && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                log.error("建立临时文件夹出错", e);
            }
        }
        return target;
    }

    // X-Forwarded-For: <client>, <proxy1>, <proxy2> 获取到请求发起的最初ip地址
    // X-Forwarded-Host: <host> The X-Forwarded-Host (XFH) 是一个事实上的标准首部，用来确定客户端发起的请求中使用  Host  指定的初始域名。
    // X-Forwarded-Proto: <protocol> 是一个事实上的标准首部，用来确定客户端与代理服务器或者负载均衡服务器之间的连接所采用的传输协议（HTTP 或 HTTPS）

    /**
     * 包含X-Real-IP请求头的请求全部走这个地方<br >
     * 这代表是一个从nginx转发的请求
     *
     * @return 重定向的响应信息
     */
    @GetMapping(value = "{id}", headers = {"X-Real-IP"}, produces = {
        MediaType.IMAGE_GIF_VALUE,
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        "image/webp",
        "image/*",
        "*/*",
        "!application/json",
        "!text/plain"})
    public ResponseEntity<?> preview(
        @PathVariable("id") UUID id) {
        return fileService.findById(id)
            .map(FileInfo::getPath)
            .map(this::buildAccessRedirectResponse)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 基于Nginx的缩略图下载
     *
     * @param id    文件ID
     * @param level 缩略图等级
     * @return 响应体
     */
    @GetMapping(value = "thumbnails/{id}", headers = {"X-Real-IP"}, produces = {
        MediaType.IMAGE_GIF_VALUE,
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        "image/webp",
        "image/*",
        "*/*",
        "!application/json", "!text/plain"
    })
    public ResponseEntity<?> previewThumbnails(@PathVariable("id") UUID id,
                                               @RequestParam(value = "level", defaultValue = "1") int level) {
        return fileService.findById(id)
            .map(FileInfo::getPath)
            // 构建缩略图路径
            .map(path -> StringUtils.join("th", level, "/", path, ".jpg"))
            .map(this::buildAccessRedirectResponse)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 文件zip打包下载
     *
     * @param files     要下载的文件的id
     * @param userAgent 浏览器de user agent
     * @param filename  下载的默认保存文件名
     * @throws IOException IO错误
     */
    @GetMapping(params = {"type=zip", "file"})
    public ResponseEntity<StreamingResponseBody> zip(@RequestParam("file") List<UUID> files,
                                                     @RequestHeader(value = "user-agent") String userAgent,
                                                     @RequestParam(value = "filename", required = false, defaultValue = "package.zip") String filename) throws IOException {
        // 如果没有找到所有文件，返回404
        List<FileInfo> fileInfos = fileService.findById(files);
        if (fileInfos.size() < files.size()) {
            return ResponseEntity.notFound().build();
        } else {
            StreamingResponseBody body = outputStream -> {
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                    Map<String, Integer> fileCountMap = new HashMap<>();
                    for (FileInfo file : fileInfos) {
                        if (fileStorageService.exist(file.getPath())) {
                            // 进行文件重名计数
                            String entryName = file.getFilename();
                            int count = fileCountMap.getOrDefault(entryName, 0);
                            fileCountMap.put(entryName, count + 1);
                            if (count > 0) {
                                int extIndex = entryName.lastIndexOf('.');
                                if (extIndex < 0) {
                                    extIndex = entryName.length();
                                }
                                String originName = entryName.substring(0, extIndex);
                                String ext = entryName.substring(extIndex);
                                entryName = originName + "(" + count + ")" + ext;
                            }
                            ZipEntry entry = new ZipEntry(entryName);
                            zipOutputStream.putNextEntry(entry);
                            try (InputStream input = fileStorageService.getResource(file.getPath()).getInputStream()) {
                                StreamUtils.copy(input, zipOutputStream);
                            } catch (Exception e) {
                                log.error("文件打包下载时发生错误", e);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("文件打包下载时发生错误", e);
                }
            };
            return ResponseEntity.ok()
                .header("Content-Type", config.getMime("zip"))
                .header("Content-Disposition", generateAttachmentFilename(userAgent, filename))
                .body(body);
        }
    }

    @PostMapping(value = "zip", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public PackageFileDto save(@RequestBody PackageFileDto request) {
        return packageFileService.save(request);
    }

    @GetMapping("zip/{id}")
    public ResponseEntity<StreamingResponseBody> zip(@PathVariable("id") String id,
                                                     @RequestHeader(value = "user-agent", required = false) String userAgent) {
        return packageFileService.findAndRemoveById(id)
            .map(request -> {
                try {
                    return zip(request.getFiles(), userAgent, request.getFilename());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 构建一个重定向，指定到前端配置的下载目录
     *
     * @param path 文件名称
     * @return 重定向信息
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
        "!text/plain"})
    public ResponseEntity<?> preview(
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
        "*/*", "!application/json", "!text/plain"})
    public ResponseEntity<?> previewThumbnails(
        @PathVariable("id") UUID id,
        @RequestParam(value = "level", defaultValue = "1") int level,
        WebRequest request) {
        return fileService.findById(id).filter(fileInfo -> thumbnailService.exists(fileInfo.getPath(), level)).map(
            file -> file.getLastModifiedDate()
                .filter(lastModify -> this.checkNotModified(request, lastModify))
                .map(lastModify -> this.<Resource>buildNotModified())
                .orElseGet(() -> this.buildThumbnailResponse(file, level)))
            .orElseGet(this::buildNotFount);
    }

    private ResponseEntity<Resource> buildThumbnailResponse(FileInfo file, int level) {
        BodyBuilder builder = ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG);
        file.getLastModifiedDate().ifPresent(lastModifiedDate -> {
            builder.lastModified(lastModifiedDate);
            builder.cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
        });
        return builder.body(thumbnailService.getResource(file.getPath(), level));
    }

    /**
     * 根据Range Header获取Range
     *
     * @param ranges 文件分块的字符串
     * @param file   文件信息
     * @return 解析后的分块信息
     * @throws RangeNotSatisfiableException 请求异常
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
            String filename = file.getFilename();
            long fileSize = file.getSize();
            Resource body;
            long contentLength = fileSize; // 内容长度
            String contentRange; // 响应范围
            String path = file.getPath();
            String ext = DmFileUtils.getExt(filename);
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
            BodyBuilder bodyBuilder;
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
            if (StringUtils.isNotBlank(userAgent)) {
                bodyBuilder.header("Content-Disposition", generateAttachmentFilename(userAgent, filename));
            }
            file.getLastModifiedDate().ifPresent(bodyBuilder::lastModified);
            return bodyBuilder.body(body);
        } catch (UnsupportedEncodingException e) {
            log.error("构建响应体时发生异常", e);
            throw new RuntimeException(e);
        }
    }

    private String generateAttachmentFilename(String userAgent, String filename) throws UnsupportedEncodingException {
        return "attachment; filename=" + generateFilename(userAgent, filename) + "; filename*=utf-8''" + URLEncoder.encode(filename, "UTF-8");
    }

    private String generateFilename(String userAgent, String filename) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(userAgent) && (StringUtils.contains(userAgent, "Trident")
            || StringUtils.contains(userAgent, "Edge")
            || StringUtils.contains(userAgent, "MSIE"))) {
            return URLEncoder.encode(filename, "UTF-8");
        } else {
            return new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
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
     * @param <T> 响应体的类型
     * @return 一个304响应体
     */
    private <T> ResponseEntity<T> buildNotModified() {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}
