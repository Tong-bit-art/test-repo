-- ============================================
-- 更新专业类目为中文名称
-- ============================================

USE personnel_system;

-- 更新专业类目名称为中文
UPDATE `category` SET 
    `name` = '计算机科学',
    `description` = '软件开发、网络工程、数据分析等计算机相关专业'
WHERE `id` = 1;

UPDATE `category` SET 
    `name` = '电子信息',
    `description` = '电子工程、通信工程、集成电路等电子信息专业'
WHERE `id` = 2;

UPDATE `category` SET 
    `name` = '机械工程',
    `description` = '机械设计、自动化、车辆工程等机械相关专业'
WHERE `id` = 3;

UPDATE `category` SET 
    `name` = '工商管理',
    `description` = '企业管理、市场营销、人力资源等管理类专业'
WHERE `id` = 4;

UPDATE `category` SET 
    `name` = '医学护理',
    `description` = '临床医学、护理学、药学等医学相关专业'
WHERE `id` = 5;

UPDATE `category` SET 
    `name` = '财务会计',
    `description` = '会计学、财务管理、审计等财经类专业'
WHERE `id` = 6;

UPDATE `category` SET 
    `name` = '教育学',
    `description` = '学前教育、小学教育、教育技术学等教育专业'
WHERE `id` = 7;

UPDATE `category` SET 
    `name` = '法学',
    `description` = '法律、知识产权、国际法等法学专业'
WHERE `id` = 8;

UPDATE `category` SET 
    `name` = '建筑工程',
    `description` = '土木工程、建筑设计、工程管理等建筑专业'
WHERE `id` = 9;

UPDATE `category` SET 
    `name` = '外语翻译',
    `description` = '英语、日语、法语等外语及翻译专业'
WHERE `id` = 10;

-- 验证更新结果
SELECT * FROM category ORDER BY id;
