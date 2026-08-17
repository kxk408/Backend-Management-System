-- BCrypt 哈希长度约 60，原 varchar(32) 不够，需先执行本脚本
ALTER TABLE emp MODIFY COLUMN password varchar(100) COMMENT '密码(BCrypt哈希)';
