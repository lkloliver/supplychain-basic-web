package org.hust.cse.supplychain.basic.modules.file.service;

import org.hust.cse.supplychain.basic.modules.file.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 上传文件
     * @param file 文件
     * @param tenant 租户标识
     * @param type 文件业务类型
     * @param fileName 文件名
     * @return 文件信息
     */
    FileInfo uploadFile(MultipartFile file, String tenant, String type, String fileName);

    /**
     * 根据ID获取文件信息
     * @param fileId 文件ID
     * @param tenant 租户标识
     * @return 文件信息
     */
    FileInfo getFileInfo(String fileId, String tenant);
} 