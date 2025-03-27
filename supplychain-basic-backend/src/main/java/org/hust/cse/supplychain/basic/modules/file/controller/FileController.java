package org.hust.cse.supplychain.basic.modules.file.controller;

import org.hust.cse.supplychain.basic.common.response.ApiResponse;
import org.hust.cse.supplychain.basic.modules.file.entity.FileInfo;
import org.hust.cse.supplychain.basic.modules.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload.path}")
    private String uploadPath;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ApiResponse<FileInfo> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tenant") String tenant,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "fileName", required = false) String fileName) {
        logger.info("开始处理文件上传请求: fileName={}, tenant={}, type={}", 
            fileName != null ? fileName : file.getOriginalFilename(), tenant, type);
        FileInfo fileInfo = fileService.uploadFile(file, tenant, type, fileName);
        return ApiResponse.success(fileInfo);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileId, @RequestParam String tenant) {
        try {
            logger.info("开始处理文件下载请求: fileId={}, tenant={}", fileId, tenant);
            FileInfo fileInfo = fileService.getFileInfo(fileId, tenant);
            
            Path filePath = Paths.get(uploadPath, fileInfo.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (!resource.exists()) {
                logger.error("文件不存在: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            logger.info("文件下载成功: {}", filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + fileInfo.getFileName() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            logger.error("文件URL格式错误", e);
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            logger.error("文件读取失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 