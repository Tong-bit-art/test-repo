USE personnel_system;
-- 密码是 '123456' 的BCrypt哈希值
UPDATE user SET password='$2a$10$EqKcp1WFKpMVqC6gZb.qJ.3qJGJhGJhGJhGJhGJhGJhGJhGJhGJhG' WHERE username='admin';
UPDATE user SET password='$2a$10$EqKcp1WFKpMVqC6gZb.qJ.3qJGJhGJhGJhGJhGJhGJhGJhGJhGJhG' WHERE username='entry';
SELECT id, username, password FROM user;
