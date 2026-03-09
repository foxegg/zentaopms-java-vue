# ZenTao PMS - Spring Boot 版

禅道项目管理系统 Java 版，由 PHP 原版完整迁移，功能完全一致。

## 技术栈

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- Spring Security + JWT
- MySQL
- Thymeleaf

## 数据库

使用与 PHP 版相同的数据库结构，表前缀 `zt_`。可直接连接现有 ZenTao 数据库。

## 配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zentao?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

## 运行

```bash
mvn spring-boot:run
```

访问 http://localhost:8080

## API 接口（需登录）

| 模块 | 路径 | 说明 |
|------|------|------|
| 认证 | POST /api/auth/login | 登录 |
| 用户 | /api/user/* | 用户管理 |
| 产品 | /api/product/* | 产品管理 |
| 项目 | /api/project/* | 项目管理 |
| 执行 | /api/execution/* | 迭代/Sprint |
| 任务 | /api/task/* | 任务管理 |
| Bug | /api/bug/* | Bug 跟踪 |
| 需求 | /api/story/* | 需求/用户故事 |
| 待办 | /api/todo/* | 待办事项 |
| 构建 | /api/build/* | 版本构建 |
| 文档 | /api/doc/* | 文档管理 |
| 用例 | /api/testcase/* | 测试用例 |
| 计划 | /api/productplan/* | 产品计划 |
| 我的 | /api/my/* | 我的地盘 |
| 组织 | /api/company/* | 公司 |
| 部门 | /api/dept/* | 部门 |
| 权限组 | /api/group/* | 权限组 |
| 操作记录 | /api/action/* | 操作历史 |

## 认证

登录后获取 JWT token，请求 API 时在 Header 中添加：
```
Authorization: Bearer <token>
```

或使用参数：`?zentaosid=<token>`

## 实体映射

| PHP 表 | Java 实体 |
|--------|-----------|
| zt_user | User |
| zt_product | Product |
| zt_project | Project (含 execution) |
| zt_task | Task |
| zt_bug | Bug |
| zt_story | Story |
| zt_todo | Todo |
| zt_build | Build |
| zt_doc | Doc |
| zt_doclib | DocLib |
| zt_case | TestCase |
| zt_action | Action |
| zt_company | Company |
| zt_dept | Dept |
| zt_group | Group |
| zt_productplan | ProductPlan |
| zt_projectproduct | ProjectProduct |
| zt_module | Module |
| zt_file | File |
| zt_entry | Entry |

## 版本

与 ZenTao PHP 22.0 功能对应。
