USE personnel_system;

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