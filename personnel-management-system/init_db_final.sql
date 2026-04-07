USE personnel_system;
CREATE TABLE IF NOT EXISTS role (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100),
  phone VARCHAR(20),
  enabled BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS user_role (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS category (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS personnel (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  gender VARCHAR(10),
  age INT,
  category_id INT,
  department VARCHAR(100),
  position VARCHAR(100),
  phone VARCHAR(20),
  email VARCHAR(100),
  address VARCHAR(255),
  created_by INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
  FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_personnel_name ON personnel(name);
CREATE INDEX idx_personnel_category ON personnel(category_id);
CREATE INDEX idx_personnel_created_by ON personnel(created_by);
CREATE TABLE IF NOT EXISTS selection_record (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  selection_type VARCHAR(50) NOT NULL,
  criteria TEXT,
  selected_count INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS selection_result (
  id INT PRIMARY KEY AUTO_INCREMENT,
  record_id INT NOT NULL,
  personnel_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (record_id) REFERENCES selection_record(id) ON DELETE CASCADE,
  FOREIGN KEY (personnel_id) REFERENCES personnel(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO role (name, description) VALUES ('ADMIN', 'Administrator'), ('ENTRY', 'Data Entry');
INSERT INTO user (username, password, name, email, phone, enabled) VALUES ('admin', '$2a$10$eJ5e7y0X3y7Z8a9b0c1d2e3f4g5h6i7j8k9l0m1n2o3p4q5r6s7t8u9v0w', 'Admin', 'admin@example.com', '13800138000', TRUE);
INSERT INTO user (username, password, name, email, phone, enabled) VALUES ('entry', '$2a$10$eJ5e7y0X3y7Z8a9b0c1d2e3f4g5h6i7j8k9l0m1n2o3p4q5r6s7t8u9v0w', 'Entry', 'entry@example.com', '13800138001', TRUE);
INSERT INTO user_role (user_id, role_id) VALUES (1, 1), (2, 2);
INSERT INTO category (name, description) VALUES ('Computer Science', 'Computer related majors'), ('Electronic Engineering', 'Electronic engineering related majors'), ('Mechanical Engineering', 'Mechanical engineering related majors'), ('Business Administration', 'Business administration related majors'), ('Medicine', 'Medicine related majors');

-- 插入测试人员数据
INSERT INTO personnel (name, gender, age, category_id, department, position, phone, email, address, created_by) VALUES
('张三', '男', 25, 1, '技术部', '软件工程师', '13800138001', 'zhangsan@example.com', '北京市海淀区', 1),
('李四', '女', 28, 2, '电子部', '硬件工程师', '13800138002', 'lisi@example.com', '北京市朝阳区', 1),
('王五', '男', 30, 3, '机械部', '机械工程师', '13800138003', 'wangwu@example.com', '北京市丰台区', 1),
('赵六', '女', 26, 4, '市场部', '市场经理', '13800138004', 'zhaoliu@example.com', '北京市西城区', 1),
('钱七', '男', 32, 5, '医疗部', '医生', '13800138005', 'qianqi@example.com', '北京市东城区', 1),
('孙八', '女', 24, 1, '技术部', '前端工程师', '13800138006', 'sunba@example.com', '北京市海淀区', 1),
('周九', '男', 29, 2, '电子部', '嵌入式工程师', '13800138007', 'zhoujiu@example.com', '北京市朝阳区', 1),
('吴十', '女', 27, 3, '机械部', '设计师', '13800138008', 'wushi@example.com', '北京市丰台区', 1);
