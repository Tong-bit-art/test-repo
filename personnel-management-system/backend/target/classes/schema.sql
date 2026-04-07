-- MySQL建表脚本
-- 人员库管理系统

-- 创建数据库
CREATE DATABASE IF NOT EXISTS personnel_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE personnel_system;

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `description` VARCHAR(255) COMMENT '角色描述',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `email` VARCHAR(100) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '电话',
  `enabled` BOOLEAN DEFAULT TRUE COMMENT '是否启用',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 专业类目表
CREATE TABLE IF NOT EXISTS `category` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '类目名称',
  `description` VARCHAR(255) COMMENT '类目描述',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 人员信息表
CREATE TABLE IF NOT EXISTS `personnel` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(10) COMMENT '性别',
  `age` INT COMMENT '年龄',
  `category_id` INT COMMENT '专业类目ID',
  `department` VARCHAR(100) COMMENT '部门',
  `position` VARCHAR(100) COMMENT '职位',
  `phone` VARCHAR(20) COMMENT '电话',
  `email` VARCHAR(100) COMMENT '邮箱',
  `address` VARCHAR(255) COMMENT '地址',
  `created_by` INT COMMENT '创建人ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE SET NULL,
  FOREIGN KEY (`created_by`) REFERENCES `user`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 为人员信息表添加索引
CREATE INDEX idx_personnel_name ON `personnel`(`name`);
CREATE INDEX idx_personnel_category ON `personnel`(`category_id`);
CREATE INDEX idx_personnel_created_by ON `personnel`(`created_by`);

-- 抽取记录表
CREATE TABLE IF NOT EXISTS `selection_record` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT NOT NULL COMMENT '操作人ID',
  `selection_type` VARCHAR(50) NOT NULL COMMENT '抽取类型：纯随机、指定范围、指定人员',
  `criteria` TEXT COMMENT '筛选条件',
  `selected_count` INT NOT NULL COMMENT '抽取人数',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 抽取结果表
CREATE TABLE IF NOT EXISTS `selection_result` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `record_id` INT NOT NULL COMMENT '抽取记录ID',
  `personnel_id` INT NOT NULL COMMENT '人员ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`record_id`) REFERENCES `selection_record`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`personnel_id`) REFERENCES `personnel`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 操作日志表
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
CREATE INDEX idx_operation_log_user ON `operation_log`(`user_id`);
CREATE INDEX idx_operation_log_username ON `operation_log`(`username`);
CREATE INDEX idx_operation_log_type ON `operation_log`(`operation_type`);
CREATE INDEX idx_operation_log_created_at ON `operation_log`(`created_at`);

-- 插入默认角色数据
INSERT INTO `role` (`name`, `description`) VALUES
('ADMIN', '管理员'),
('ENTRY', '录入员');

-- 插入默认管理员账号
INSERT INTO `user` (`username`, `password`, `name`, `email`, `phone`, `enabled`) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '超级管理员', 'admin@example.com', '13800138000', TRUE);

-- 插入默认录入员账号
INSERT INTO `user` (`username`, `password`, `name`, `email`, `phone`, `enabled`) VALUES
('entry', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '录入员', 'entry@example.com', '13800138001', TRUE);

-- 关联默认角色
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- 管理员关联管理员角色
(2, 2); -- 录入员关联录入员角色

-- 插入默认专业类目
INSERT INTO `category` (`name`, `description`) VALUES
('计算机科学', '计算机相关专业'),
('电子工程', '电子工程相关专业'),
('机械工程', '机械工程相关专业'),
('工商管理', '工商管理相关专业'),
('医学', '医学相关专业');
