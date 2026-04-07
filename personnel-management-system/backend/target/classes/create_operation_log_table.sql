-- 创建操作日志表
CREATE TABLE IF NOT EXISTS `operation_log` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `user_id` INT NOT NULL COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `operation_type` VARCHAR(20) NOT NULL COMMENT '操作类型',
  `operation_description` VARCHAR(200) NOT NULL COMMENT '操作描述',
  `request_method` VARCHAR(10) COMMENT '请求方法',
  `request_url` VARCHAR(500) COMMENT '请求URL',
  `request_params` TEXT COMMENT '请求参数',
  `ip_address` VARCHAR(50) COMMENT 'IP地址',
  `status` VARCHAR(20) NOT NULL COMMENT '操作状态',
  `error_message` TEXT COMMENT '错误信息',
  `execution_time` BIGINT COMMENT '执行时间（毫秒）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_username` (`username`),
  INDEX `idx_operation_type` (`operation_type`),
  INDEX `idx_created_at` (`created_at`),
  CONSTRAINT `fk_operation_log_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';