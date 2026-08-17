# Project_Management

Tlias 智能学习辅助系统后端（培训机构教务/人事后台）。

## 技术栈

Java 17 · Spring Boot 3 · MyBatis · MySQL · PageHelper · JWT · Redis · BCrypt

## 模块概览

- 登录鉴权（JWT + BCrypt 密码）
- 部门 / 员工 / 班级管理
- 员工报表统计
- 部门列表 Redis 缓存（Cache Aside，空值短 TTL，TTL 随机抖动）

## 本地运行

1. MySQL 创建库 `tlias`，按实体建表；必要时执行 `tlias-web-management/src/main/resources/db/alter_emp_password.sql`
2. 启动 Redis：`docker run -d -p 6379:6379 redis:7`
3. 复制配置并填写本地密码：
   `copy tlias-web-management\src\main\resources\application.yml.example tlias-web-management\src\main\resources\application.yml`
4. 运行 `com.itheima.TliasWebManagementApplication`

> `application.yml` 已加入 `.gitignore`，请勿提交含真实数据库密码的本地配置。
