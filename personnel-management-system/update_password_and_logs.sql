-- 更新密码为123456的BCrypt加密值
USE personnel_system;

-- 更新管理员密码为123456
UPDATE `user` SET `password` = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE `username` = 'admin';

-- 更新录入员密码为123456
UPDATE `user` SET `password` = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE `username` = 'entry';

-- 创建操作日志表（如果不存在）
CREATE TABLE IF NOT EXISTS `operation_log` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT NOT NULL COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
  `operation_description` VARCHAR(255) NOT NULL COMMENT '操作描述',
  `request_method` VARCHAR(10) COMMENT '请求方法',
  `request_url` VARCHAR(255) COMMENT '请求URL',
  `request_params` TEXT COMMENT '请求参数',
  `ip_address` VARCHAR(50) COMMENT 'IP地址',
  `status` VARCHAR(20) NOT NULL COMMENT '操作状态：SUCCESS/FAILURE',
  `error_message` TEXT COMMENT '错误信息',
  `execution_time` BIGINT COMMENT '执行时间（毫秒）',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 为操作日志表添加索引
CREATE INDEX IF NOT EXISTS idx_operation_log_user ON `operation_log`(`user_id`);
CREATE INDEX IF NOT EXISTS idx_operation_log_username ON `operation_log`(`username`);
CREATE INDEX IF NOT EXISTS idx_operation_log_type ON `operation_log`(`operation_type`);
CREATE INDEX IF NOT EXISTS idx_operation_log_created_at ON `operation_log`(`created_at`);

SELECT '密码更新和操作日志表创建完成' AS status;