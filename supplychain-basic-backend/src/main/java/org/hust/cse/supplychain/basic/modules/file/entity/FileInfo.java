package org.hust.cse.supplychain.basic.modules.file.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileInfo {
    private String id;              // 文件ID
    private String fileName;        // 文件原始名称
    private String fileType;        // 文件类型
    private String filePath;        // 文件存储路径
    private String accessUrl;       // 文件访问URL
    private Long fileSize;          // 文件大小(字节)
    private String tenant;          // 租户标识(BASE/NON_BASE)
    private String businessType;    // 业务类型(contract-purchase等)
    private LocalDateTime uploadTime; // 上传时间
} 