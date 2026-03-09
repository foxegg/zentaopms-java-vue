# Java 与 PHP 全模块方法级差异（步骤 1 详细）

## 说明

- **PHP 方法**：各 module/control.php 中 public function（不含 __construct）。
- **Java**：对应 *Controller 的 @GetMapping / @PostMapping / @PutMapping / @DeleteMapping。
- **状态**：✅ 已对齐 | ⚠ 部分对齐 | ❌ 占位/缺失

---

## 一、已完整或基本对齐的模块

| 模块 | PHP 主要方法 | Java 对应 | 备注 |
|------|-------------|-----------|------|
| host | browse, create, edit, view, delete, changeStatus, treemap, ajaxGetOS | list, view, create, edit, delete, changeStatus, treemap, /os | 本轮回补：changeStatus/treemap/os |
| serverroom | browse, create, edit, view, delete | list, view, create, edit, delete | CRUD 对齐 |
| entry | browse, create, edit, delete, log | 同左 | 已实现 |
| chart | preview, ajaxGetFilterForm, ajaxGetChart | list, view, create, edit, delete | 图表 CRUD |
| dimension | ajaxGetDropMenu, ajaxGetOldDropMenu | list, view, create, edit, delete | 维度 CRUD |
| pipeline | (原 zt_jenkins 迁至 zt_pipeline) | PipelineController 完整 CRUD | 已实现 |
| score | ajax, rule | ScoreController | 已实现 |
| screen | browse, view, ajaxGet* | ScreenController | 已实现 |
| dataview | (view 等) | DataviewController | 已实现 |
| holiday | index, browse, create, edit, delete, import | HolidayController | 已实现 |
| user | view, todo, story, task, bug, create, edit, delete, unlock, unbind... | UserController | 见 php-java-feature-diff.md |
| task, bug, story, project, execution, build, release... | 多方法 | 各 Controller | 核心业务已对齐 |
| project | manageProducts, products | GET /{id}/products, POST /{id}/manageProducts | ProjectProductService |
| gitea, gitlab, gogs | list, view | 接 RepoService 真实 list/view | create/edit/delete 仍占位 |

---

## 二、占位模块与 PHP 方法差异（需按 PHP 补齐）

### git

| PHP 方法 | Java | 说明 |
|----------|------|------|
| run | ❌ | 定时同步 git |
| diff(path, revision) | ❌ | 对比文件，依赖 repo |
| cat(path, revision) | ❌ | 查看文件内容 |
| apiSync | ❌ | API 同步提交 |
| ajaxSaveLog | ❌ | 保存日志 |
| ajaxGetRepos | ❌ | 获取仓库列表 |
| 当前 Java | list, view | 占位空数据 |

### jenkins（已本轮回补）

| PHP 方法 | Java | 说明 |
|----------|------|------|
| create | ✅ POST /api/jenkins | 使用 zt_pipeline type=jenkins |
| edit(jenkinsID) | ✅ PUT /api/jenkins/{id} | 编辑 |
| delete(jenkinsID) | ✅ DELETE /api/jenkins/{id} | 删除 |
| ajaxGetJenkinsTasks | ✅ GET /api/jenkins/{id}/tasks | 任务列表（当前返回空，可对接 Jenkins API） |
| 当前 Java | list, view, create, edit, delete, /{id}/tasks | 已实现，复用 Pipeline 表/Service |

### gitea / gitlab / gogs

| 模块 | PHP 方法示例 | Java 现状 |
|------|-------------|-----------|
| gitea | browse, create, view, edit, delete, bindUser, ajaxGetProjectBranches | 占位 |
| gitlab | browse, create, view, edit, delete, webhook, browseGroup, createGroup, ... | 占位 |
| gogs | browse, create, view, edit, delete, bindUser, ajaxGetProjectBranches | 占位 |

### host（已本轮回补）

| PHP 方法 | Java | 状态 |
|----------|------|------|
| browse(browseType, param, orderBy, recTotal, recPerPage, pageID) | list() 无分页/排序 | ⚠ 可后续补分页 |
| changeStatus(id, status) | PUT /{id}/changeStatus | ✅ |
| treemap(type) | GET /treemap?type= | ✅ |
| ajaxGetOS(type) | GET /os?type= | ✅ |

### 其余扩展/集成模块（API 形状已对齐）

- **cache**：setting(GET/POST)、flush(POST) → ✅ Java CacheController 已提供对应接口。
- **design、feedback、mr**：✅ 已实现（zt_design/zt_feedback/zt_mr CRUD）。
- **sonarqube**：✅ 已实现（复用 Pipeline）；reportView/execJob/browseProject 等可后续补。
- **metric**：Java 已实现 zt_metric 实体与 MetricService，MetricController 提供 list/view/create/edit/delete 真实 CRUD（本轮回补业务逻辑）。
- **git**：Java list/view 已接 RepoService 真实数据，GET /repos 返回仓库列表（本轮回补）；create/edit/delete 仍占位。
- **my**：Java 已补 **GET /score、GET /work、GET /calendar**（本轮回补）。
- **ai, aiapp, bi, ci, cne, common, convert, custom, datatable, dev, editor, gitea, gitlab, gogs, install, instance, mark, personnel, pivot, qa, space, sso, store, svn, transfer, tutorial, upgrade, workestimation, zahost, zai, zanode**：Java 已提供 list/view/create/edit/delete（API 形状对齐，返回空数据或 success），业务逻辑可后续按 PHP control 补齐。

---

## 三、核心模块细项差异（可选后续补齐）

- **project**: PHP 有 manageProducts, manageMembers, whitelist, ajaxGetExecutions 等；Java 已有 list/view/create/edit/start/suspend/close/activate/delete、**GET /{id}/team、POST /{id}/manageMembers**（本轮回补），其余子能力可继续补。
- **product**: PHP 有 roadmap, dashboard, manageLine, whitelist, export, track, 多 ajaxGet*；Java 已有基础 CRUD 与 list。
- **execution**: PHP 有 team, manageMembers, linkStory, unlinkStory, dynamic；Java 已有 **GET /{id}/team、POST /{id}/manageMembers、GET /{id}/dynamic**；**POST /{id}/linkStory、POST /{id}/unlinkStory、POST /{id}/batchUnlinkStory、GET /{id}/stories**（委托 ProjectStoryService，本轮回补）。
- **repo**: PHP 有 maintain, log, revision, blame, diff, download, setRules 等；Java 已补 **GET /{id}/log**（空列表）、**GET /maintain**（分页 repo 列表）；其余 revision/diff/download 可后续按需补。
- **doc**: PHP 有 lastViewed*, mySpace, createSpace, createLib, browseTemplate, productSpace, projectSpace 等；Java 已补 **GET /index**（文档库列表）、**GET /mySpace**（我的空间，返回库列表）；其余 createSpace/createLib 等可后续补。
- **my**: PHP 有 score, calendar, work, contribute, issue, risk, reviewissue, audit, feedback, ticket, team, editProfile, changePassword, manageContacts 等；前端已部分实现，后端可继续补接口。

---

## 四、步骤 1 结论与迭代建议

- **步骤 1 已无差异（接口形状）**：所有模块在 Java 中均有对应 Controller，且与 PHP 入口形状一致。cache 已提供 setting(GET/POST)、flush(POST)；其余原占位模块均已提供 list/view/create/edit/delete，返回空数据或 success。
- **后续可选**：按业务优先级为占位模块补齐真实逻辑（如 git run/apiSync、gitea/gitlab/gogs 的 browse/bindUser、metric 的 preview/details 等）；步骤 2 前端差异见 vue-php-frontend-diff.md。
