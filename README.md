# 后台管理系统

培训机构教务 / 人事 **后台管理系统** 后端服务。

## 技术栈

Java 17 · Spring Boot 3 · MyBatis · MySQL · PageHelper · JWT · Redis · BCrypt

## 模块概览

- 登录鉴权（JWT + BCrypt 密码）
- 部门 / 员工 / 班级管理
- 员工报表统计
- 部门列表 Redis 缓存（Cache Aside，空值短 TTL，TTL 随机抖动）

## 本地运行

1. MySQL 创建库 `backend_management_system`（与项目名对应），按实体建表；必要时执行 `backend-management-system/src/main/resources/db/alter_emp_password.sql`
2. 启动 Redis：`docker run -d -p 6379:6379 redis:7`
3. 复制配置并填写本地密码：
   `copy backend-management-system\src\main\resources\application.yml.example backend-management-system\src\main\resources\application.yml`
4. 运行 `com.backend.BackendManagementApplication`

> `application.yml` 已加入 `.gitignore`，请勿提交含真实数据库密码的本地配置。
