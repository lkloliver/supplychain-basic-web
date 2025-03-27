-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `phone` varchar(20) NOT NULL COMMENT '手机号',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入测试数据（密码为：123456）
INSERT INTO `user` (`phone`, `username`, `password`)
VALUES ('13800138000', 'admin', '$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKYpxwQh.LfXi');

-- 创建文件信息表
CREATE TABLE IF NOT EXISTS t_file_info (
    id VARCHAR(36) PRIMARY KEY COMMENT '文件ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名称',
    file_type VARCHAR(50) NOT NULL COMMENT '文件类型',
    file_path VARCHAR(255) NOT NULL COMMENT '文件存储路径',
    access_url VARCHAR(255) NOT NULL COMMENT '文件访问URL',
    file_size BIGINT COMMENT '文件大小(字节)',
    tenant VARCHAR(20) NOT NULL COMMENT '租户标识(BASE/NON_BASE)',
    upload_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    INDEX idx_tenant (tenant) COMMENT '租户索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表';

-- 添加唯一约束，防止重复上传
ALTER TABLE t_file_info ADD CONSTRAINT uk_file_path_tenant UNIQUE (file_path, tenant); 
ALTER TABLE t_file_info ADD COLUMN business_type VARCHAR(50) COMMENT '业务类型';