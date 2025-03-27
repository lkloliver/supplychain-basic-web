package org.hust.cse.supplychain.basic.modules.file.service.impl;

import org.hust.cse.supplychain.basic.modules.file.entity.FileInfo;
import org.hust.cse.supplychain.basic.modules.file.mapper.FileMapper;
import org.hust.cse.supplychain.basic.modules.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.url}")
    private String accessUrl;

    private final FileMapper fileMapper;

    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    @Transactional
    public FileInfo uploadFile(MultipartFile file, String tenant, String type, String fileName) {
        try {
            // 检查上传路径是否存在
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                logger.info("创建上传目录: {}", uploadPath);
                Files.createDirectories(uploadDir);
            }

            // 生成存储路径
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String storageFileName = UUID.randomUUID().toString() + fileExtension;
            
            // 按业务类型和日期构建存储路径
            String businessPath = type != null ? type : "other";
            String relativePath = businessPath + "/" + datePath + "/" + storageFileName;
            Path fullPath = Paths.get(uploadPath, relativePath);

            // 创建目录
            logger.info("创建文件目录: {}", fullPath.getParent());
            Files.createDirectories(fullPath.getParent());

            // 保存文件
            logger.info("开始保存文件: {}", fullPath);
            Files.copy(file.getInputStream(), fullPath);
            logger.info("文件保存成功: {}", fullPath);

            // 保存文件信息
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName != null ? fileName + fileExtension : file.getOriginalFilename());
            fileInfo.setFileType(fileExtension);
            fileInfo.setFilePath(relativePath);
            fileInfo.setAccessUrl(accessUrl + "/" + relativePath);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setTenant(tenant);
            fileInfo.setBusinessType(type); // 保存业务类型

            // 使用MyBatis插入数据
            logger.info("开始保存文件信息到数据库");
            fileMapper.insert(fileInfo);
            logger.info("文件信息保存成功");

            return fileInfo;
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public FileInfo getFileInfo(String fileId, String tenant) {
        FileInfo fileInfo = fileMapper.selectById(fileId);
        if (fileInfo == null || !fileInfo.getTenant().equals(tenant)) {
            throw new RuntimeException("文件不存在");
        }
        return fileInfo;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex);
    }
} 