# 禅道 Java/Vue 回归测试 Checklist

本文档基于 `PHP-FEATURE-LIST-AND-CHECKTASKS.md` 提炼，用于**冒烟/回归**时快速验证核心接口与返回结构。打勾表示已测通过。

**环境与前置条件**：后端服务与前端已启动；数据库已初始化（含 zt_* 表）；存在可登录用户（如 admin）；接口需带有效登录态（如 Authorization），管理员相关接口需 ADMIN 权限。

**目录**：一、[通用约定](#一通用约定先确认) → 二、[按模块回归项](#二按模块回归项)（含[负向与边界用例](#负向与边界用例可选)）→ 三、[冒烟建议顺序](#三冒烟建议顺序最小路径) → 四、[使用说明](#四使用说明) → 五、[常见问题](#五常见问题) → 六、[相关文档索引](#六相关文档索引) → [变更记录](#变更记录)

---

## 一、通用约定（先确认）

- [ ] 成功响应：`result: "success"`，且含 `data` 或 `id`；分页接口含 `pager`（recTotal、recPerPage、pageID）。
- [ ] 业务失败：`result: "fail"` + `message`；前端需判断并提示。
- [ ] 未登录/无权限：401 或 403，或 body 中 `result: "fail"`。

---

## 二、按模块回归项

### 产品（product）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表（分页） | GET | /api/product/list?pageID=1&recPerPage=20 | 200，result+data+pager |
| 列表（筛选） | GET | /api/product/list?product=1 或 status=closed | 200，data 符合筛选 |
| 下拉 | GET | /api/product/pairs?mode=noclosed&programID=0 | 200，data 为 id→name |
| 详情 | GET | /api/product/{id} | 200，data 为单条产品 |
| 动态 | GET | /api/product/{id}/dynamic?pageID=1&recPerPage=20 | 200，data+pager |
| 路线图 | GET | /api/product/{id}/roadmap?branch=all | 200，data 含 plans、releases |
| 批量编辑 | POST | /api/product/batchEdit，body: productIds, fields | 200，result success |
| 更新排序 | PUT | /api/product/updateOrder，body: productIDs | 200，result success |

### 项目（project）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/project/list?status=all&type=all&pageID=1&recPerPage=20 | 200，data+pager |
| 全部 | GET | /api/project/all | 200，data 为数组 |
| 详情 | GET | /api/project/{id} | 200，data 为单条 |
| 动态/执行/摘要/团队/产品 | GET | /api/project/{id}/dynamic、executions、summary、team、products | 200，data |

### 执行（execution）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/execution/list?projectID=1&type=all&status=all | 200，data 数组 |
| 取消关联需求 | POST | /api/execution/{id}/unlinkStory，body: { storyID } | 200，result success |
| 批量取消关联 | POST | /api/execution/{id}/batchUnlinkStory，body: { storyIds } | 200，result success |

### 需求（story）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/story/list?product=1&moduleID=0&pageID=1&recPerPage=20 | 200，data+pager |
| pairs | GET | /api/story/pairs?product=1&planID=0&hasParent=false | 200，data 为 id→title |
| pairsByList | GET | /api/story/pairsByList?ids=1,2&withEmptyOption=true | 200，含 0→"" |
| 关联需求 | POST | /api/story/{id}/linkStory，body: { targetStoryID } | 200，result success |
| 取消关联 | POST | /api/story/{id}/unlinkStory?targetStoryID=2 | 200，result success |
| 关闭 | PUT | /api/story/{id}/close | 200，data.assignedTo 为 closed |

### 任务（task）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/task/list?project=1&execution=1&pageID=1&recPerPage=20 | 200，data+pager |
| 确认需求变更 | PUT | /api/task/{id}/confirmStoryChange，body: { storyID } | 200，result success |
| 创建分支 | POST | /api/task/{id}/createBranch，body: { repoID } | 200，result success |
| 解除分支 | POST | /api/task/{id}/unlinkBranch | 200，result success |
| 团队管理 | POST | /api/task/manageTeam?executionID=1&taskID=1，body: { members } | 200，result success |

### 缺陷（bug）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/bug/list?product=1&moduleID=0&pageID=1&recPerPage=20 | 200，data+pager |
| 关闭 | PUT | /api/bug/{id}/close | 200，data.assignedTo 为 closed |

### 用例（testcase）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表（分页） | GET | /api/testcase/list?product=1&pageID=1&recPerPage=20 | 200，data+pager |
| 由用例建缺陷 | POST | /api/testcase/{id}/createBug，body | 200，含 id |
| 确认需求变更 | PUT | /api/testcase/{id}/confirmStoryChange，body: { storyID } | 200，result success |

### 构建（build）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/build/list?project=1 或 product=1 或 execution=1 | 200，data 数组 |
| 构建下需求/缺陷 | GET | /api/build/{id}/stories、/api/build/{id}/bugs | 200，data 数组 |
| projectBuilds/productBuilds/executionBuilds | GET | /api/build/projectBuilds?projectID=1 等 | 200，data 数组 |

### 发布（release）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/release/list?product=1&branch=all&type=all | 200，data 数组 |
| 详情/需求/缺陷 | GET | /api/release/{id}、/stories、/bugs | 200，data |

### 计划（productplan）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/productplan/list?productID=1&branch=all | 200，data 数组 |
| 关闭 | PUT | /api/productplan/{id}/close，body: { closedReason } | 200，result success |
| 取消关联需求 | POST | /api/productplan/{id}/unlinkStory?storyID=1 | 200，result success |

### 项目需求（projectstory）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/projectstory/list?projectID=1 | 200，data 数组 |
| 关联/批量关联/取消/批量取消 | POST | linkStory、batchLinkStory、unlinkStory、batchUnlinkStory，body 含 projectID、storyID/storyIds | 200，result success |

### 用户（user）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/user/list?account=xxx&dept=1&pageID=1&recPerPage=20 | 200，data+pager |
| 按账号查 | GET | /api/user/view?account=admin | 200，data 为单用户 |
| pairs | GET | /api/user/pairs?mode=nodeleted | 200，data 为 id→realname 或 account→label |

### 权限组（group）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表/下拉 | GET | /api/group/list?projectID=0&vision=xxx、/api/group/pairs | 200，data |
| 删除全局组 | DELETE | /api/group/{id}（id 为 project=0 的组） | 400，result fail、message |

### 部门（dept）/公司（company）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 删除有子部门 | DELETE | /api/dept/{id}（有子部门或部门下有人） | 400，result fail |
| 删除有用户的公司 | DELETE | /api/company/{id}（公司下有用户） | 400，result fail |

### 文档（doc）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/doc/list?libID=1&moduleID=0 | 200，data 数组 |
| 批量移动 | POST | /api/doc/batchMoveDoc，body: docIds, lib, module | 200，result success |

### 树/模块（tree）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 浏览 | GET | /api/tree/browse?rootID=1&viewType= | 200，data 数组 |
| 选项菜单 | GET | /api/tree/optionMenu?rootID=1&type=story | 200，data |
| 修复路径 | POST | /api/tree/fix?rootID=1&type=story | 200，result success |

### 分支（branch）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/branch/list?productID=1 | 200，data 数组 |
| 创建/删除 | POST/DELETE | 创建记 action Opened，删除记 deleted | 200，result success |

### 待办（todo）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/todo/list?status=all 或 wait/done | 200，data 数组 |

### 操作记录（action）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/action/list?objectType=story&objectID=1 | 200，data 数组 |
| 列表分页 | GET | /api/action/list?objectType=story&objectID=1&pageID=1&recPerPage=20 | 200，data+pager |

### 通知（notify）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/notify/list?status=wait&createdBy=admin&pageID=1&recPerPage=20 | 200，data+pager（按 createdBy 时有 pager） |
| 批量删除 | DELETE | /api/notify/batchDelete，body: { ids } | 200，result success |

### 邮件（mail）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 配置 | GET/PUT | /api/mail/config | 200，data 为 key-value |
| 测试 | POST | /api/mail/test，body 可选 | 200，result success |
| 队列 | GET | /api/mail/browse?status=all&pageID=1&recPerPage=20 | 200，data+pager |

### 版本库（repo）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 创建分支 | POST | /api/repo/createBranch?objectID=1&repoID=1 | 200，result success |
| 解除分支 | POST | /api/repo/unlinkBranch?objectID=1 | 200，result success |

### 管理员（admin）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 同步需求关联 | POST | /api/admin/syncLinkStoriesToRelation（需 ADMIN） | 200，data.syncedPairs |

### 设置（setting）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 配置 | GET/PUT | /api/setting/config?owner=system&module=common&section=global；PUT body 为 key-value | 200，data |

### 报表（report）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 首页/年度/提醒 | GET | /api/report/index、/annualData?year=2025、/remind | 200，data |

### 看板（kanban）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 空间列表/详情 | GET | /api/kanban/space/list、/api/kanban/space/{id} | 200，data |
| 空间 CRUD | POST/PUT/DELETE | /api/kanban/space、/api/kanban/space/{id}，body 与 PHP 一致 | 200，result success 或 id |
| 看板列表 | GET | /api/kanban/list?spaceID=1（spaceID≤0 返回空） | 200，data 数组 |
| 看板详情/CRUD | GET/POST/PUT/DELETE | /api/kanban/{id}、/api/kanban、/api/kanban/{id} | 200，data 或 id |
| 卡片列表/详情 | GET | /api/kanban/{id}/cards、/api/kanban/card/{id} | 200，data |
| 卡片 CRUD | POST/PUT/DELETE | /api/kanban/card、/api/kanban/card/{id}，body 与 PHP 一致 | 200，result success 或 id |

### 我的（my）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 首页/待办/任务/缺陷/需求 | GET | /api/my/index、todo、task、bug、story | 200，data |
| 动态/工时 | GET | /api/my/dynamic、work | 200，data |
| 积分（分页） | GET | /api/my/score?pageID=1&recPerPage=20 | 200，data+pager |
| 日历（日期范围） | GET | /api/my/calendar?startDate=2025-01-01&endDate=2025-01-31 | 200，data.tasks、data.todos |
| 资料/偏好/改密 | GET/PUT | /api/my/profile、preference、changePassword；PUT body 对应 | 200，result success |

### 搜索（search）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 全局搜索 | GET | /api/search/query?words=xxx&module=bug&limit=20 | 200，data 数组 |
| 保存的查询 | GET/POST/DELETE | getQuery、saveQuery、deleteQuery/{id} | 200，data 或 id |

### 文件（file）

| 用例 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 列表 | GET | /api/file/list?objectType=story&objectID=1 | 200，data 数组 |
| 上传 | POST | /api/file/upload，FormData | 200，含 id |
| 下载 | GET | /api/file/{id}/download | 200，blob |

### 测试单（testtask）/测试套件（testsuite）/文档库（caselib）

| 模块 | 用例 | 方法 | 路径/参数 | 期望 |
|------|------|------|-----------|------|
| testtask | 列表 | GET | /api/testtask/list?product=1&project=1&execution=1&status=all | 200，data |
| testtask | pairs | GET | /api/testtask/pairs?product=1&execution=1 | 200，data |
| testsuite | 列表 | GET | /api/testsuite/list?product=1&project=1&type=all | 200，data |
| caselib | 列表 | GET | /api/caselib/list?product=1&project=1 | 200，data |
| caselib | pairs | GET | /api/caselib/pairs?type=all | 200，data |

### 负向与边界用例（可选）

以下用例用于验证未登录、无权限、非法参数时的返回是否符合约定：

| 场景 | 方法 | 路径/参数 | 期望 |
|------|------|-----------|------|
| 未登录访问需登录接口 | GET | /api/my/index（无 Authorization） | 200，body 中 result: "fail"，message 含未登录等 |
| 无效资源 ID | GET | /api/product/0 或 /api/project/99999（不存在） | 404 或 200+ result fail / data 空 |
| 删除全局组（project=0） | DELETE | /api/group/{id}（id 为全局组） | 400 或 200，body 中 result: "fail"，message 提示不可删 |
| 删除有子部门的部门 | DELETE | /api/dept/{id}（存在子部门或部门下有人） | 400 或 200，result: "fail"，message 提示 |
| 看板列表无空间 | GET | /api/kanban/list?spaceID=0 | 200，data 为空数组 |

---

## 三、冒烟建议顺序（最小路径）

按下列顺序可快速验证前后端与登录态，约 10 分钟内完成：

1. **登录** → 获取 token，后续请求带 Authorization。
2. **我的首页** → GET /api/my/index → 200，含 data.user、data.todos、data.tasks 等。
3. **产品列表（分页）** → GET /api/product/list?pageID=1&recPerPage=20 → 200，含 data、pager。
4. **项目列表** → GET /api/project/list?pageID=1&recPerPage=20 → 200，含 data、pager。
5. **待办列表** → GET /api/todo/list?status=all → 200，data 数组。
6. **操作记录（分页）** → GET /api/action/list?objectType=story&objectID=1&pageID=1&recPerPage=20 → 200，含 data、pager。
7. **任选一条写操作**：如 PUT /api/my/preference（body 为 key-value）、或 GET /api/report/annualData?year=2025。

若以上均通过，可认为基础链路与通用约定（第一节）满足，再按第二节按模块做完整回归。

---

## 四、使用说明

1. **冒烟**：每模块至少跑「列表 + 详情 + 一条写操作（创建/编辑/删除或关联）」；可优先按第三节顺序执行。
2. **回归**：按第二节表格逐条验证；发现失败记入缺陷并注明接口与参数。
3. **环境**：需已登录且具备相应权限（如管理员接口需 ADMIN）。
4. 与 `PHP-FEATURE-LIST-AND-CHECKTASKS.md` 配合：后者为完整检查清单，本文档为精简回归用例。

---

## 五、常见问题

- **请求返回 401 / 未登录**：确认请求头带 `Authorization: Bearer <token>` 或项目约定的鉴权方式；先调登录接口获取 token。
- **列表无 pager**：部分接口仅在传 `recPerPage>0` 或按创建人分页时返回 pager，见主文档第二节该模块说明；若约定应返回却未返回，记入缺陷。
- **业务失败但 HTTP 200**：正常约定为 body 内 `result: "fail"` + `message`；前端需判断并提示 `message`，勿仅依赖 HTTP 状态码。

---

## 六、相关文档索引

| 文档 | 用途 |
|------|------|
| `README.md`（本目录） | 文档导航入口，三份核心文档的用途与使用方式 |
| `PHP-FEATURE-LIST-AND-CHECKTASKS.md` | 完整功能列表与 Java/Vue 逐项检查任务（开发/对 PHP 对齐时使用） |
| `DEV-CHECKLIST.md` | 新增或修改接口时的精简自查清单（列表/创建/编辑/删除、参数与返回约定） |
| `REGRESSION-CHECKLIST.md` | 本文档，冒烟与回归用例及建议顺序 |
| `PHP-JAVA-SERVICE-COMPARISON.md` | 已修复的 PHP/Java Service 逻辑差异记录（若存在） |

---

**变更记录**：初版含按模块回归表、通用约定；后续补充环境与前置条件、冒烟建议顺序、负向与边界用例、相关文档索引。看板（kanban）回归项与「我的」积分/日历细化已并入。
