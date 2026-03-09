# 禅道项目管理系统 - Vue 前端

与 Java 后端 (zentaopms-java) 配套的 Vue 3 前端，实现与 PHP 版等价的核心用户模块页面。

## 环境要求

- Node.js >= 18

## 技术栈

- Vue 3
- Vue Router 4
- Vite 5
- Axios

## 开发

```bash
npm install
npm run dev
```

默认代理 `/api` 到 `http://localhost:8080`，请先启动 Java 后端。

## 构建

```bash
npm run build
```

## 已实现（与 PHP user 模块 UI 对齐）

- 登录
- 布局（顶栏、导航）
- 用户列表（分页）
- 用户详情页 + Tab：待办、任务、Bug、需求、测试单、测试用例、执行、动态、档案

## 后续可补

- 用户新建/编辑/批量操作
- 我的地盘、项目、产品、任务、Bug、需求等模块页面
