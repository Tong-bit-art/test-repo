USE personnel_system;

-- Insert test personnel data
INSERT INTO personnel (name, gender, age, category_id, department, position, phone, email, address, created_by) VALUES
('Zhang San', 'Male', 25, 1, 'Technical Dept', 'Software Engineer', '13800138001', 'zhangsan@example.com', 'Haidian District, Beijing', 1),
('Li Si', 'Female', 28, 2, 'Electronic Dept', 'Hardware Engineer', '13800138002', 'lisi@example.com', 'Chaoyang District, Beijing', 1),
('Wang Wu', 'Male', 30, 3, 'Mechanical Dept', 'Mechanical Engineer', '13800138003', 'wangwu@example.com', 'Fengtai District, Beijing', 1),
('Zhao Liu', 'Female', 26, 4, 'Marketing Dept', 'Marketing Manager', '13800138004', 'zhaoliu@example.com', 'Xicheng District, Beijing', 1),
('Qian Qi', 'Male', 32, 5, 'Medical Dept', 'Doctor', '13800138005', 'qianqi@example.com', 'Dongcheng District, Beijing', 1);