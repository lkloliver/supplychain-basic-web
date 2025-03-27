CREATE TABLE IF NOT EXISTS t_file_info (
    id VARCHAR(36) PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL COMMENT '文件原始名称',
    file_type VARCHAR(50) NOT NULL COMMENT '文件类型',
    file_path VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    access_url VARCHAR(500) NOT NULL COMMENT '文件访问URL',
    file_size BIGINT COMMENT '文件大小(字节)',
    tenant VARCHAR(50) NOT NULL COMMENT '租户标识(BASE/NON_BASE)',
    business_type VARCHAR(100) COMMENT '业务类型(contract-purchase等)',
    upload_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    INDEX idx_tenant (tenant),
    INDEX idx_business_type (business_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表'; 