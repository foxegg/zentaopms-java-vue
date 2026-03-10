# 禅道 Java/Vue 开发自查清单（精简版）

新增或修改接口时，按本清单自检，保证与 `PHP-FEATURE-LIST-AND-CHECKTASKS.md` 及现有模块一致。完整检查见该文档第二节、第三节。

---

## 一、Java 后端（每模块/每接口）

### 1. Controller 与路径
- [ ] 有对应 `XxxController`，`@RequestMapping("/api/xxx")` 与前端 `src/api/xxx.js` 的 base 一致（如 `/api/product`）。
- [ ] 列表路径一般为 `GET /api/xxx/list`，详情为 `GET /api/xxx/{id}`；REST 与现有模块保持一致。

### 2. 列表接口
- [ ] 支持与 PHP 一致的筛选参数（如 productID、projectID、status、branch、moduleID 等，按业务需要）。
- [ ] 分页参数：`pageID`（从 1）、`recPerPage`；当 `recPerPage > 0` 时返回 **pager**：`{ recTotal, recPerPage, pageID }`。
- [ ] 返回结构统一：`{ result: "success", data: [...] , pager?: { recTotal, recPerPage, pageID } }`。

### 3. 创建接口
- [ ] Service 内设置 openedBy/createdBy、openedDate/createdDate（或与 PHP 同名字段）。

### 4. 编辑接口
- [ ] 先 getById 再合并可编辑字段；设置 lastEditedBy/lastEditedDate（或与 PHP 一致）；记录 action `Edited`。

### 5. 删除接口
- [ ] 按 PHP 做软删除（deleted=1）或硬删（仅当无关联数据时）。
- [ ] 校验：如子部门、公司下用户、全局组不可删等；失败返回 `{ result: "fail", message: "..." }`。
- [ ] 记录 action（如 `deleted`）；若有级联（如模块下 story/task 归属更新）按 PHP 实现。

### 6. 参数约定
- [ ] 需要 **多个字段** 或 **复杂 body** 时用 `@RequestBody`（如 batchEdit、copyGroup、batchMoveDoc、confirmStoryChange）。
- [ ] 仅 **少量简单参数** 用 `@RequestParam`（如 unlinkStory 的 targetStoryID、createBranch 的 objectID/repoID）。
- [ ] 字段名与前端、PHP 一致：如 productIds/productIDs、storyIds、taskIds、docIds、lib、module、ids 等。

### 7. 业务规则（与 PHP 对齐）
- [ ] 关闭（close）时若 PHP 置 assignedTo=closed，则 Java 一致。
- [ ] 批量操作：跳过已关闭、未变更则跳过、父子任务等同列表只关父等，与 PHP 一致。
- [ ] 计划/模块等删除时的 parent 校验、changeParentField、shadow/build 清理等，按 PHP 实现。

---

## 二、Vue 前端（每 API 模块）

### 1. 文件与路径
- [ ] 有对应 `src/api/xxx.js`，请求 base 为 `/api/xxx`（与 Java Controller 一致）。
- [ ] 请求方法：GET/POST/PUT/DELETE 与后端注解一致。

### 2. 返回与错误
- [ ] 统一用 `.then((r) => r.data)` 取 body。
- [ ] 业务失败时后端返回 `result: "fail"` + `message`；前端需判断并提示（如 `res?.result === 'fail'` 则 `res.message`）。
- [ ] 网络/4xx/5xx 用 `err.response?.data?.message` 等展示。

### 3. 参数与 Body
- [ ] 后端 `@RequestBody` 的接口：前端传 **body**（如 confirmTaskStoryChange、batchEdit、copyGroup、batchMoveDoc、batchDeleteNotify）。
- [ ] 后端 `@RequestParam` 的接口：前端用 **params**（如 unlinkStory(targetStoryID)、createRepoBranch(objectID, repoID)、manageTaskTeam(executionID, taskID)）。
- [ ] 字段名与后端一致：productIds/productIDs、storyIds、taskIds、docIds、lib、module、ids 等。

### 4. 列表与分页
- [ ] 列表接口用 `getXxxList(params)`，params 传筛选与 pageID、recPerPage；若后端返回 pager，调用方用 `res.pager` 做分页 UI。

### 5. 导出 / 文件下载
- [ ] 导出类接口加 `responseType: 'blob'`，用 `Content-Disposition` 或约定文件名做下载。

---

## 三、响应约定（前后端统一）

| 场景         | 响应 |
|--------------|------|
| 成功         | `result: "success"` + `data` 或 `id`；分页时 + `pager` |
| 业务失败     | `result: "fail"` + `message` |
| 未登录/无权限 | 401/403 或 body 中 `result: "fail"` + `message` |

---

## 四、参考文档

- **完整功能与检查项**：`PHP-FEATURE-LIST-AND-CHECKTASKS.md`
- **回归/冒烟测试**：`REGRESSION-CHECKLIST.md`（用例表、冒烟顺序、负向用例、文档索引）
- **已修复差异记录**：`PHP-JAVA-SERVICE-COMPARISON.md`（若存在）
