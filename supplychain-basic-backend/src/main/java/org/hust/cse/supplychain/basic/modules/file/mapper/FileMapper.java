package org.hust.cse.supplychain.basic.modules.file.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hust.cse.supplychain.basic.modules.file.entity.FileInfo;
import java.util.List;

@Mapper
public interface FileMapper {
    // 插入文件信息
    int insert(FileInfo fileInfo);
    
    // 根据ID查询文件信息
    FileInfo selectById(@Param("id") String id);
    
    // 根据租户查询文件列表
    List<FileInfo> selectByTenant(@Param("tenant") String tenant);
    
    // 根据业务类型查询文件列表
    List<FileInfo> selectByBusinessType(@Param("businessType") String businessType);
    
    // 根据ID删除文件信息
    int deleteById(@Param("id") String id);
    
    // 更新文件信息
    int update(FileInfo fileInfo);
} 