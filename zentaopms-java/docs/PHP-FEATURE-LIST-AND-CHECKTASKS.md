# 禅道 PHP 功能列表与 Java/Vue 逐项检查任务

本文档提供：**禅道 PHP 项目（zentaopms）的完整功能列表**（尽量细化），以及针对 **zentaopms-java（后端重构）** 与 **zentaopms-web（前端重构）** 的**每一处细节检查任务**。

**目录**：[一、PHP 功能列表](#一php-项目功能列表按模块细分) | [二、Java 后端检查](#二java-后端逐项检查任务) | [三、Vue 前端检查](#三vue-前端逐项检查任务) | [四、使用说明](#四使用说明) | [五、执行结论](#五执行结论检查完成记录)。开发新增接口时另见 `DEV-CHECKLIST.md`；回归/冒烟见 `REGRESSION-CHECKLIST.md`。

---

## 一、PHP 项目功能列表（按模块细分）

### 1. 产品（product）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 产品列表 | 分页、筛选（status/branch）、排序 | browse, list |
| 产品详情 | 查看单个产品 | view |
| 创建产品 | 新建产品 | create |
| 编辑产品 | 修改产品信息 | edit |
| 关闭/激活产品 | 状态变更 | close, activate |
| 删除产品 | 删除产品 | delete |
| 产品下拉 pairs | 供下拉选择，mode=all/noclosed，programID | getPairs |
| 产品动态 | 产品下动态列表 | dynamic |
| 产品路线图 | 计划与发布展示 | roadmap |
| 批量编辑产品 | 批量修改字段 | batchEdit |
| 产品排序 | 更新产品顺序 | updateOrder |
| 导出产品 | Excel 导出 | export |

### 2. 项目（project）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 项目列表 | 分页、筛选、排序 | browse, list |
| 全部项目 | 不分页全部 | all |
| 项目详情 | 查看单个项目 | view |
| 创建/编辑/删除项目 | CRUD | create, edit, delete |
| 启动/挂起/关闭/激活项目 | 状态流转 | start, suspend, close, activate |
| 项目下拉 pairs | mode, programID | getPairs, pairsByList |
| 项目动态 | 项目下动态 | dynamic |
| 项目下的执行列表 | executions | executions |
| 项目摘要 | summary | summary |
| 项目团队 | 成员列表 | team |
| 管理项目成员 | 增删改成员 | manageMembers |
| 项目关联产品 | 产品列表与关联管理 | products, manageProducts |
| 批量编辑项目 | 批量改字段 | batchEdit |
| 导出项目 | Excel | export |

### 3. 执行（execution）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 执行列表 | 按项目/类型/状态 | browse, list |
| 执行详情 | view | view |
| 创建/编辑/删除执行 | CRUD | create, edit, delete |
| 启动/挂起/关闭/激活/延期 | 状态与延期 | start, suspend, close, activate, putoff |
| 执行下拉 pairs | projectID, mode | getPairs, pairsByList |
| 执行下的任务 | task 列表 | task |
| 执行团队 | team | team |
| 执行摘要 | summary | summary |
| 执行动态 | dynamic | dynamic |
| 管理执行成员 | manageMembers | manageMembers |
| 执行关联需求 | linkStory, batchLinkStory, unlinkStory, batchUnlinkStory | 对应方法 |
| 批量编辑执行 | batchEdit | batchEdit |

### 4. 需求（story）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 需求列表 | 分页、product/status/branch/module | browse, list |
| 需求详情 | view, storyView | view |
| 创建/编辑/删除需求 | CRUD | create, edit, delete |
| 关闭/激活需求 | close, activate（close 时 assignedTo=closed） | close, activate |
| 指派需求 | assignTo | assignTo |
| 需求下拉 pairs | product, planID, hasParent | getPairs |
| 需求 pairsByList | ids，可选 withEmptyOption（0→''） | getPairsByList |
| 需求关联需求 | zt_relation 双向，linkStory/unlinkStory | linkStory, unlinkStory |
| 批量指派/关闭/编辑/改计划/改模块/改分支 | batchAssignTo, batchClose, batchEdit, batchChangePlan, batchChangeModule, batchChangeBranch | 对应方法 |
| 批量创建需求 | batchCreate | batchCreate |
| 需求报表/导出 | report, export | report, export |
| 需求下的任务/缺陷/用例 | tasks, bugs, cases | tasks, bugs, cases |
| 确认需求变更（任务/用例侧） | processStoryChange | 由 task/testcase 调 |

### 5. 任务（task）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 任务列表 | 按执行/项目，分页筛选 | browse, list |
| 任务详情 | view | view |
| 创建/编辑/删除任务 | CRUD | create, edit, delete |
| 指派/开始/完成/暂停/重启/关闭/取消/激活 | 状态与操作 | assignTo, start, finish, pause, restart, close, cancel, activate |
| 关闭时 assignedTo=closed | 与 PHP 一致 | close |
| 任务下拉 pairs | 按 ids 等 | getPairs, getPairsByList |
| 记录工时 | recordWorkhour | recordWorkhour |
| 工时列表/编辑/删除 | efforts, editEffort, deleteEffort | 对应方法 |
| 确认需求变更 | confirmStoryChange，body.storyID | confirmStoryChange |
| 创建/解除分支 | createBranch, unlinkBranch | createBranch, unlinkBranch |
| 批量指派/关闭/编辑/改模块/取消 | batchAssignTo, batchClose, batchEdit, batchChangeModule, batchCancel | 对应方法 |
| 批量创建任务 | batchCreate | batchCreate |
| 团队管理 | manageTeam，params: executionID, taskID | manageTeam |
| 任务报表/导出 | report, export | report, export |

### 6. 缺陷（bug）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 缺陷列表 | 分页、product/project/status 等 | browse, list |
| 缺陷详情 | view | view |
| 创建/编辑/删除缺陷 | CRUD | create, edit, delete |
| 解决/关闭/激活缺陷 | resolve, close, activate（close 时 assignedTo=closed） | 对应方法 |
| 指派/确认缺陷 | assignTo, confirm | assignTo, confirm |
| 关联需求/确认需求变更 | setStory, confirmStoryChange | setStory, confirmStoryChange |
| 关联缺陷/取消关联 | linkBugs, unlinkBug | linkBugs, unlinkBug |
| 批量指派/解决/关闭/改模块/改计划/确认/激活/改分支 | batchAssignTo, batchResolve, batchClose, batchChangeModule, batchChangePlan, batchConfirm, batchActivate, batchChangeBranch | 对应方法 |
| 批量创建/编辑 | batchCreate, batchEdit | 对应方法 |
| 缺陷报表/导出 | report, export | report, export |

### 7. 用例（testcase）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 用例列表 | 分页、product/module 等 | browse, list |
| 用例详情 | view | view |
| 创建/编辑/删除用例 | CRUD | create, edit, delete |
| 由用例创建缺陷 | createBug | createBug |
| 确认需求变更 | confirmStoryChange | confirmStoryChange |
| 评审/批量评审 | review, batchReview | review, batchReview |
| 批量创建/编辑/删除 | batchCreate, batchEdit, batchDelete | 对应方法 |
| 批量改模块/改分支/改类型 | batchChangeModule, batchChangeBranch, batchChangeType | 对应方法 |
| 导出用例 | export | export |
| pairs/pairsByList | product, ids | getPairs, getPairsByList |

### 8. 构建（build）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 构建列表 | list，project/product/execution | list |
| 构建详情 | view | view |
| 创建/编辑/删除构建 | CRUD | create, edit, delete |
| 构建关联需求/缺陷 | linkStory, linkBug | linkStory, linkBug |
| 取消关联/批量取消 | unlinkStory, unlinkBug, batchUnlinkStory, batchUnlinkBug | 对应方法 |
| 构建下的需求/缺陷列表 | stories, bugs | stories, bugs |
| pairs/productBuilds/projectBuilds/executionBuilds | getPairs, getByProduct, getByProject, getByExecution | 对应方法 |
| 导出构建 | export | export |

### 9. 发布（release）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 发布列表 | product/project/branch/type | list |
| 发布详情 | view | view |
| 创建/编辑/删除发布 | CRUD；删除时 shadow/build 清理 | create, edit, delete |
| 发布下的需求/缺陷 | stories, bugs | stories, bugs |
| 关联/取消关联需求与缺陷 | linkStory, unlinkStory, linkBug, unlinkBug, batchUnlink | 对应方法 |
| 变更状态/发布/通知 | changeStatus, publish, notify | 对应方法 |
| 导出发布 | export | export |

### 10. 计划（productplan）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 计划列表 | productID, branch | list |
| 计划详情 | view | view |
| 创建/编辑/删除计划 | CRUD；删除时 parent 校验、changeParentField | create, edit, delete |
| 启动/完成/关闭/激活 | start, finish, close, activate | 对应方法 |
| 计划下的需求/缺陷 | stories, bugs | stories, bugs |
| 关联/取消关联需求与缺陷 | linkStory, unlinkStory, linkBug, unlinkBug, batchLink/batchUnlink | 对应方法 |
| pairs/pairsByList | productID, branch, ids | getPairs, getPairsByList |

### 11. 项目需求（projectstory）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 项目下需求列表 | list(projectID) | list |
| 执行已关联需求 | executionStories(projectID, storyIds) | executionStories |
| 关联/批量关联需求 | linkStory, batchLinkStory | 对应方法 |
| 取消关联/批量取消 | unlinkStory, batchUnlinkStory | 对应方法 |

### 12. 用户（user）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 用户列表 | list，分页筛选 | browse, list |
| 用户详情/按 account 查 | view(id), view(account) | view |
| 创建/编辑/删除用户 | CRUD | create, edit, delete |
| 用户下拉 pairs | mode(nodeleted/noclosed/withguest 等) | getPairs |
| 用户待办/任务/缺陷/需求/测试单/用例/执行 | todo, task, bug, story, testtask, testcase, execution | 对应方法 |
| 用户动态/个人资料 | dynamic, profile | dynamic, profile |
| 批量创建/编辑 | batchCreate, batchEdit | 对应方法 |
| 解锁/重置密码/解绑 | unlock, resetPassword, unbind | 对应方法 |

### 13. 权限组（group）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 组列表/下拉 | list(projectID), pairs(projectID)；projectID=0 为全局组；vision 过滤 | list, pairs |
| 组详情 | view | view |
| 创建/编辑组 | create, edit | create, edit |
| 删除组 | 仅可删项目组；全局组返回错误 | delete |
| 组成员/添加/移除 | members, addMember, removeMember | 对应方法 |
| 复制组 | copy(groupID, options) | copy |
| 组权限 get/set | getPrivs, setPrivs | 对应方法 |

### 14. 部门（dept）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 部门列表/树/浏览 | list(parentID), tree, browse | 对应方法 |
| 部门详情 | view | view |
| 创建/编辑/删除部门 | 删除时校验：无子部门、部门下无用户 | create, edit, delete |
| 部门排序 | updateOrder | updateOrder |
| 部门下用户 | users | users |

### 15. 公司（company）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 公司列表/浏览/详情 | list, browse, view | 对应方法 |
| 创建/编辑/删除公司 | 删除时校验：公司下无用户 | create, edit, delete |
| 公司动态 | dynamic | dynamic |

### 16. 文档（doc）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 文档首页/我的空间 | index, mySpace | index, mySpace |
| 文档库下拉/按产品/按项目 | libPairs, libs/product, libs/project | 对应方法 |
| 文档列表 | list(libID, moduleID) | list |
| 文档详情 | view | view |
| 创建/编辑/删除文档 | 删除时记 action deleted | create, edit, delete |
| 批量移动文档 | batchMoveDoc(docIds, lib, module) | batchMoveDoc |

### 17. 文档库（caselib）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 用例库列表 | list(product, project)；无 type | list |
| 用例库下拉 | pairs(type) | pairs |
| 用例库详情/浏览 | view, browse | view, browse |
| 创建/编辑/删除用例库 | CRUD | create, edit, delete |

### 18. 测试套件（testsuite）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 套件列表/下拉 | list, pairs, pairsByList | 对应方法 |
| 套件详情/套件下用例 | view, cases | view, cases |
| 创建/编辑/删除套件 | CRUD | create, edit, delete |
| 添加/关联/移除用例 | addCase, linkCases, removeCase, batchUnlinkCases | 对应方法 |

### 19. 测试单（testtask）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 测试单列表/下拉 | list, pairs, pairsByList | 对应方法 |
| 测试单详情 | view | view |
| 创建/编辑/删除测试单 | CRUD | create, edit, delete |
| 开始/关闭/阻塞/激活 | start, close, block, activate | 对应方法 |
| 测试单结果/用例列表 | results, cases | results, cases |
| 关联/取消关联用例 | linkCase, unlinkCase, batchUnlinkCases | 对应方法 |
| 批量指派/执行用例 | batchAssign, runCase | 对应方法 |

### 20. 测试报告（testreport）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 报告列表/下拉 | list, pairs, pairsByList | 对应方法 |
| 报告详情 | view | view |
| 创建/编辑/删除报告 | 创建/删除时记 action | create, edit, delete |

### 21. 树/模块（tree）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 模块浏览 | browse(rootID, viewType) | browse |
| 模块选项菜单 | optionMenu(rootID, type) | optionMenu |
| 模块节点 CRUD | view, create, edit, delete | 对应方法 |
| 模块排序 | updateOrder | updateOrder |
| 修复模块路径 | fix(rootID, type) | fix |
| 删除时级联子模块、归属更新、记 action | delete(remove) | remove |

### 22. 分支（branch）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 分支列表/下拉 | list(productID), pairs, pairsByList | 对应方法 |
| 分支详情 | view | view |
| 创建/删除分支 | 创建记 Opened，删除记 deleted | create, delete |

### 23. 待办（todo）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 待办列表 | list | list |
| 待办 CRUD | create, edit, delete | 对应方法 |
| 开始/完成/关闭/指派 | start, finish, close, assignTo | 对应方法 |
| 批量创建/完成/关闭/编辑 | batchCreate, batchFinish, batchClose, batchEdit | 对应方法 |

### 24. 动态/操作（action）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 操作记录列表 | list(objectType, objectID) | getList |
| 添加/编辑评论 | comment, edit | comment, edit |
| 单条记录 | view | view |

### 25. 通知（notify）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 通知列表 | list(status, createdBy 等) | list |
| 通知详情/删除 | view, delete | 对应方法 |
| 批量删除 | batchDelete(ids) | batchDelete |

### 26. 邮件（mail）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 邮件配置 get/set | config | getConfig, saveConfig |
| 测试发信 | test | test |
| 邮件队列浏览 | browse | browse |

### 27. 文件（file）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 文件列表 | list(objectType, objectID) | list |
| 文件详情/上传/下载/预览/删除 | view, upload, download, preview, delete | 对应方法 |

### 28. 设计（design）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 设计列表/详情 | list, view | 对应方法 |
| 创建/编辑/删除 | 创建记 created，删除记 deleted | create, edit, delete |

### 29. 看板（kanban）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 看板空间 list/CRUD | space/list, space CRUD | 对应方法 |
| 看板 list/CRUD | list(spaceID), kanban CRUD | 对应方法 |
| 卡片 list/CRUD | cards, card CRUD | 对应方法 |

### 30. 需求类型（requirement）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 需求列表（storyType=requirement） | list, product | 同 story |
| CRUD/关闭/激活 | create, edit, delete, close, activate | 对应方法 |
| 批量指派/关闭 | batchAssignTo, batchClose | 对应方法 |

### 31. 项目集（program）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 项目集列表/下拉 | list, pairs, pairsByList | 对应方法 |
| 项目集详情 | view | view |
| 创建/编辑/删除 | CRUD | create, edit, delete |

### 32. 项目计划（programplan）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 计划列表 | list(projectID, executionID) | list |
| 计划详情 | view | view |

### 33. 项目发布（projectrelease）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 列表/详情 | list(projectID), view | 对应方法 |
| 创建/编辑/删除 | CRUD | create, edit, delete |

### 34. 项目构建（projectbuild）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 列表/详情 | list(projectID), view | 对应方法 |
| 创建/编辑/删除 | CRUD | create, edit, delete |

### 35. 史诗（epic）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 史诗列表/详情 | list, view, product | 对应方法 |
| 创建/编辑/删除 | CRUD | create, edit, delete |
| 批量指派/关闭 | batchAssignTo, batchClose | 对应方法 |

### 36. 版本库（repo）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 版本库列表/详情 | list, view | 对应方法 |
| 创建/编辑/删除 | CRUD | create, edit, delete |
| 创建分支/解除分支 | createBranch(objectID, repoID), unlinkBranch(objectID) | 对应方法 |
| 日志/维护 | log, maintain | 对应方法 |

### 37. 管理员（admin）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 首页/配置/安全/日志 | index, config, safe, log | 对应方法 |
| 同步需求关联到 zt_relation | syncLinkStoriesToRelation | syncLinkStoriesToRelation |

### 38. 备份（backup）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 备份列表/执行备份/还原 | list, backup, restore | 对应方法 |
| 删除备份/磁盘空间/进度/设置 | delete, diskSpace, progress, setting | 对应方法 |

### 39. 缓存（cache）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 缓存列表/配置 get/set/清空 | list, setting, flush | 对应方法 |

### 40. 设置（setting）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 配置 get/set | config(owner, module, section) | getConfig, saveConfig |

### 41. 我的（my）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 首页/待办/任务/缺陷/需求/动态/积分/工时/日历 | index, todo, task, bug, story, dynamic, score, work, calendar | 对应方法 |
| 个人资料/偏好/修改密码 | profile, preference, changePassword | 对应方法 |

### 42. 报表（report）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 首页/年度数据/提醒 | index, annualData, remind | 对应方法 |

### 43. 搜索（search）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 全局搜索 | query(words, module, limit) | query |
| 保存的查询 get/save/delete | getQuery, saveQuery, deleteQuery | 对应方法 |

### 44. 认证（auth）
| 功能点 | 说明 | PHP 入口/方法 |
|--------|------|----------------|
| 登录/登出 | login, logout | 对应方法 |

### 45. 其他模块（简要）
| 模块 | 主要功能 |
|------|----------|
| holiday | 节假日 list/yearPairs/CRUD |
| stage | 阶段 list/CRUD（groupID） |
| stakeholder | 干系人 list/CRUD（objectID, objectType） |
| feedback | 反馈 list/CRUD |
| serverroom | 机房 list/CRUD |
| host | 主机 list/CRUD，changeStatus，treemap，os |
| workestimation | 工时估算 list/budget/CRUD |
| block | 区块 list/CRUD（dashboard） |
| cron | 定时任务 list/CRUD，toggle，run |
| pipeline | 流水线 list/CRUD |
| job | 任务 list/CRUD |
| weekly | 周报 list/CRUD（projectID） |
| chart | 图表 list/CRUD |
| dimension | 维度 list/CRUD |
| dataview | 数据视图 list/CRUD |
| screen | 大屏 list/CRUD |
| webhook | Webhook list/CRUD，log |
| metric | 指标 list/CRUD |
| score | 积分 list/view（只读） |
| entry | 入口 list/CRUD |
| transfer | 导入导出 index/create |
| upgrade | 升级 index/create |
| tutorial | 向导 index/create |
| sso | SSO setting/list/create |
| message | 站内信（与 mail 区分） |
| design | 设计（见上） |
| sonarqube | Sonar list/CRUD |
| jenkins | Jenkins list/CRUD，tasks(id, depth) |
| git/gitlab/gitea/gogs | 代码库配置 list/CRUD |
| mr | Merge Request list/CRUD |
| compile | 编译 list/CRUD |
| dev/editor/qa/bi/ai 等 | 各入口 index/list 等 |

---

## 二、Java 后端逐项检查任务

对上述**每个功能点**，在 zentaopms-java 中执行以下检查（在对应 Controller/Service 下打勾或记录结论）：

### 2.1 通用检查（每个模块）
- [x] 是否存在对应 `XxxController`，且 `@RequestMapping("/api/xxx")` 与前端约定一致。
- [x] 列表接口：是否支持与 PHP 一致的筛选参数（如 productID、projectID、status、branch、moduleID、pageID、recPerPage）。
- [x] 列表返回结构：是否统一为 `{ result, data, pager? }`，与 PHP 分页形状一致。
- [x] 创建接口：是否在 Service 内设置 openedBy/createdBy、openedDate/createdDate（与 PHP 一致）。
- [x] 编辑接口：是否先 getById 再合并可编辑字段，并设置 lastEditedBy/lastEditedDate，记录 action `Edited`（与 PHP 一致）。
- [x] 删除接口：是否按 PHP 做软删除、校验（如子部门、公司下用户、全局组）、并记录 action（如 deleted）及级联（如 Module）。

### 2.2 按模块的细节检查

#### 产品（product）
- [x] GET /api/product/list 参数：product, status, branch, pageID, recPerPage。
- [x] GET /api/product/pairs：mode, programID。
- [x] GET /api/product/{id}/dynamic：pageID, recPerPage。
- [x] GET /api/product/{id}/roadmap：branch。
- [x] POST /api/product/batchEdit：body.productIds, body.fields。
- [x] PUT /api/product/updateOrder：body.productIDs。

#### 项目（project）
- [x] GET /api/project/{id}/dynamic、executions、summary、team、products。
- [x] POST /api/project/{id}/manageMembers：body.members。
- [x] POST /api/project/{id}/manageProducts：body 与 PHP manageProducts 一致。

#### 执行（execution）
- [x] GET /api/execution/list：projectID, type, status。
- [x] GET /api/execution/pairs：projectID, mode。
- [x] POST /api/execution/{id}/unlinkStory：body.storyID（与前端 body 一致）。
- [x] POST /api/execution/{id}/batchUnlinkStory：body.storyIds。

#### 需求（story）
- [x] GET /api/story/pairs：product, planID, hasParent。
- [x] GET /api/story/pairsByList：ids, withEmptyOption。
- [x] POST /api/story/{id}/linkStory：body.targetStoryID。
- [x] POST /api/story/{id}/unlinkStory：RequestParam targetStoryID。
- [x] StoryService.close：assignedTo 置为 closed。
- [x] StoryService.batchAssignTo：跳过 closed、assignedTo 未变。
- [x] StoryService.batchChangeModule：模块未变更则跳过。
- [x] linkStory/unlinkStory：zt_relation 双向、action 为 linkrelatedstory/unlinkrelatedstory。

#### 任务（task）
- [x] PUT /api/task/{id}/confirmStoryChange：body.storyID。
- [x] POST /api/task/{id}/createBranch：body.repoID。
- [x] POST /api/task/{id}/unlinkBranch：无 body。
- [x] POST /api/task/manageTeam：RequestParam executionID, taskID；body.members。
- [x] TaskService.batchClose：跳过 closed、父子同列表只关父。
- [x] TaskService.batchChangeModule：模块未变更则跳过并记 Edited。

#### 缺陷（bug）
- [x] BugService.close：assignedTo 置为 closed。
- [x] batchAssignTo/batchClose/batchChangeModule：与 Story/Task 同逻辑（跳过 closed、未变更等）。

#### 用例（testcase）
- [x] POST /api/testcase/{id}/createBug：body。
- [x] PUT /api/testcase/{id}/confirmStoryChange：body.storyID。
- [x] TestCaseService.batchChangeModule：moduleID 范围、未变更跳过。

#### 构建（build）
- [x] GET /api/build/{id}/stories、/bugs。
- [x] GET /api/build/projectBuilds、productBuilds、executionBuilds 参数名与前端一致。

#### 发布（release）
- [x] ReleaseService.delete：shadow、build 清理与 PHP 一致。

#### 计划（productplan）
- [x] ProductPlanService.delete：parent<0 禁止删；parent>0 时 changeParentField。

#### 项目需求（projectstory）
- [x] POST /api/projectstory/linkStory、batchLinkStory、unlinkStory、batchUnlinkStory 参数与 PHP 一致。

#### 权限组（group）
- [x] GET /api/group/list、pairs：projectID，可选 vision。
- [x] GroupService.getList/getPairs：projectID=0 为全局组；vision 过滤。
- [x] DELETE /api/group/{id}：全局组返回 400、result: fail、message。

#### 用户（user）
- [x] GET /api/user/list：account, dept, pageID, recPerPage；GET /api/user/view：RequestParam account（按账号查）；pairs(mode)。

#### 部门（dept）
- [x] DELETE：有子部门或部门下有用户时返回 fail。

#### 公司（company）
- [x] DELETE：公司下有用户时返回 fail。

#### 文档（doc）
- [x] POST /api/doc/batchMoveDoc：body.docIds, body.lib, body.module。
- [x] DocService.delete：记 action deleted。

#### 树/模块（tree）
- [x] ModuleService.delete：级联子模块、story/task/bug/case 归属更新、fix、记 action。

#### 分支（branch）
- [x] BranchService.create/delete：记 action Opened/deleted。

#### 通知（notify）
- [x] DELETE /api/notify/batchDelete：body.ids。

#### 邮件（mail）
- [x] GET/PUT /api/mail/config；POST /api/mail/test；GET /api/mail/browse 参数与 PHP 一致。

#### 版本库（repo）
- [x] POST /api/repo/createBranch：RequestParam objectID, repoID。
- [x] POST /api/repo/unlinkBranch：RequestParam objectID。

#### 动态/操作（action）
- [x] GET /api/action/list：objectType, objectID；可选 pageID, recPerPage，分页时返回 pager。

#### 管理员（admin）
- [x] POST /api/admin/syncLinkStoriesToRelation：同步需求关联到 zt_relation，需 ADMIN。

#### 待办（todo）
- [x] GET /api/todo/list：可选 status（all/wait/done 等）。

#### 设置（setting）
- [x] GET/PUT /api/setting/config：RequestParam owner, module, section；PUT body 为 key-value 配置。

#### 报表（report）
- [x] GET /api/report/index、annualData(year)、remind。

#### 看板（kanban）
- [x] GET /api/kanban/space/list、GET /api/kanban/space/{id}；POST/PUT/DELETE /api/kanban/space、/api/kanban/space/{id}。
- [x] GET /api/kanban/list：spaceID（≤0 时返回空列表）。
- [x] GET /api/kanban/{id}；POST/PUT/DELETE /api/kanban、/api/kanban/{id}。
- [x] GET /api/kanban/{id}/cards、GET /api/kanban/card/{id}；POST/PUT/DELETE /api/kanban/card、/api/kanban/card/{id}。

#### 我的（my）
- [x] GET /api/my/index、todo、task、bug、story、dynamic、score、work、calendar、profile、preference；PUT changePassword、profile、preference（body 与 PHP 一致）。

#### 文件（file）
- [x] GET /api/file/list：objectType, objectID；upload/download/preview/delete 与 PHP 一致。

#### 测试单（testtask）
- [x] GET /api/testtask/list：product, project, execution, status；pairs(product, execution)；results、cases、linkCase、unlinkCase 等。

#### 测试套件（testsuite）
- [x] GET /api/testsuite/list：product, project, type；view、cases、addCase、linkCases、removeCase 等。

#### 文档库（caselib）
- [x] GET /api/caselib/list：product, project（无 type）；pairs(type)。

---

## 三、Vue 前端逐项检查任务

对上述**每个功能点**，在 zentaopms-web 中执行以下检查（在对应 `src/api/xxx.js` 及调用处）：

### 3.1 通用检查（每个 API 模块）
- [x] 是否存在对应 `src/api/xxx.js`，且 base 路径为 `/api/xxx`（与 Java Controller 一致）。
- [x] 请求方法：GET/POST/PUT/DELETE 与后端 @GetMapping/@PostMapping/@PutMapping/@DeleteMapping 一致。
- [x] 返回处理：`.then((r) => r.data)` 统一取 body；调用方若需判断业务失败，检查 `res?.result === 'fail'` 并展示 `res.message`。
- [x] 错误处理：网络错误或 4xx/5xx 时使用 `err.response?.data?.message` 等展示。

### 3.2 参数与 Body 约定
- [x] 后端 @RequestBody 的接口：前端必须传 body（如 confirmTaskStoryChange、batchEdit、copyGroup、batchMoveDoc、batchDeleteNotify 等）。
- [x] 后端 @RequestParam 的接口：前端使用 `params` 传参（如 unlinkStory(targetStoryID)、createRepoBranch(objectID, repoID)、manageTaskTeam(executionID, taskID) 等）。
- [x] 字段名与后端一致：如 productIds/productIDs、storyIds、taskIds、groupID、docIds、lib、module、ids 等。

### 3.3 按模块的细节检查

#### 产品（product.js）
- [x] getProductList(params)、getProductPairs(params)、getProductPairsByList(ids)。
- [x] getProductDynamic(id, params)、getProductRoadmap(id, params)。
- [x] batchEditProduct(body)、updateProductOrder(body)。
- [x] exportProduct(params) 使用 responseType: 'blob' 并处理 Content-Disposition。

#### 项目（project.js）
- [x] getProjectDynamic、getProjectExecutions、getProjectSummary、getProjectTeam、getProjectProducts。
- [x] manageProjectMembers(projectId, members)、manageProjectProducts(projectId, body)。

#### 执行（execution.js）
- [x] unlinkExecutionStory(executionId, storyID) 传 body { storyID }（与 Java @RequestBody 一致）。
- [x] batchUnlinkExecutionStory(executionId, storyIds) 传 body { storyIds }。

#### 需求（story.js）
- [x] getStoryPairs(product, planID, hasParent)、getStoryPairsByList(ids, withEmptyOption)。
- [x] linkStory(id, body)、unlinkStory(id, targetStoryID) 用 params。

#### 任务（task.js）
- [x] confirmTaskStoryChange(id, body)、createTaskBranch(id, body)、unlinkTaskBranch(id)。
- [x] manageTaskTeam(params, body) 中 params 含 executionID、taskID。

#### 缺陷（bug.js）
- [x] confirmBugStoryChange(id, body)。
- [x] setBugStory、linkBugs、unlinkBug(id, relatedBugID) 参数与后端一致。

#### 用例（testcase.js）
- [x] createBugFromTestCase(id, body)、confirmTestCaseStoryChange(id, body)。

#### 构建（build.js）
- [x] getBuildStories(id)、getBuildBugs(id)。
- [x] getBuildByProject(projectID)、getBuildByProduct(productID)、getBuildByExecution(executionID) 参数名与后端一致。

#### 发布（release.js）
- [x] linkReleaseStory、unlinkReleaseStory、linkReleaseBug、unlinkReleaseBug 等 body/params 与后端一致。

#### 计划（productplan.js）
- [x] closePlan(id, closedReason) 传 body { closedReason }。

#### 项目需求（projectstory.js）
- [x] linkProjectStory、batchLinkProjectStory、unlinkProjectStory、batchUnlinkProjectStory 的 data 与后端 body 一致。

#### 权限组（group.js）
- [x] getGroupList(projectID, vision)、getGroupPairs(projectID, vision)。
- [x] copyGroup(body) 含 groupID、options。
- [x] 删除全局组时调用方检查 res.result === 'fail' 并提示 res.message。

#### 文档（doc.js）
- [x] batchMoveDoc(body) 含 docIds、lib、module。

#### 通知（notify.js）
- [x] batchDeleteNotify(body) 使用 DELETE 且 `{ data: body }`，body 含 ids。

#### 邮件（message.js）
- [x] getMessageConfig、saveMessageConfig、testMessage(body)、getMessageBrowse(params)。

#### 版本库（repo.js）
- [x] createRepoBranch(params) 传 params.objectID、params.repoID。
- [x] unlinkRepoBranch(objectID) 传 params.objectID。

#### 用户（user.js）
- [x] getUserList(params)、getUserByAccount(account)、getUserPairs(mode) 与后端一致。

#### 我的（my.js）
- [x] getMyIndex、getMyTodo/task/bug/story、getMyDynamic、getMyScore、getMyWork、getMyCalendar、getMyProfile、getMyPreference、updateMyPreference、changePassword、updateMyProfile 与后端一致。

#### 搜索（search.js）
- [x] getSavedQueries、saveQuery、deleteQuery 路径与参数与后端一致。

#### 动态/操作（action.js）
- [x] getActionList(objectType, objectID, params) 支持 pageID、recPerPage，分页时返回 pager。

#### 管理员（admin.js）
- [x] syncLinkStoriesToRelation() 对应 POST /api/admin/syncLinkStoriesToRelation。

#### 待办（todo.js）
- [x] getTodoList(params) 支持 status，与后端 list(status) 一致。

#### 设置（setting.js）
- [x] getSettingConfig(params)、saveSettingConfig(params, configs) 的 owner、module、section 与后端一致。

#### 报表（report.js）
- [x] getReportIndex、getAnnualData(year)、getRemind 路径与后端一致。

#### 看板（kanban.js）
- [x] getSpaceList、getSpaceById、createSpace、updateSpace、deleteSpace。
- [x] getKanbanList(spaceID)、getKanbanById、createKanban、updateKanban、deleteKanban。
- [x] getKanbanCards(kanbanId)、getCardById、createCard、updateCard、deleteCard 与后端一致。

#### 文件（file.js）
- [x] getFileList(objectType, objectID)、uploadFile(formData)、downloadFile、deleteFile 与后端一致。

#### 测试单（testtask.js）
- [x] getTestTaskList(params)、getTestTaskPairs(product, execution)、linkCase/unlinkCase/runCase 等 body/params 与后端一致。

#### 测试套件（testsuite.js）
- [x] getTestSuiteList(params)、getTestSuitePairs、addCase、linkCases、removeCase 等与后端一致。

#### 文档库（caselib.js）
- [x] getCaselibList(params)、getCaselibPairs(type) 与后端一致。

#### 分支（branch.js）
- [x] getBranchList(params)、getBranchPairs(productID)、createBranch/updateBranch/deleteBranch 与后端一致。

---

## 四、使用说明

1. **PHP 功能列表**：按模块逐条对照 PHP `module/*/control.php` 与 `model.php`，可补充更多子方法（如 ajax*、export 参数）。
2. **Java 检查**：在实现或回归时，按第二节逐项打勾；发现不一致则修复 Java 或记录到 `PHP-JAVA-SERVICE-COMPARISON.md`。
3. **Vue 检查**：在联调或回归时，按第三节逐项打勾；发现缺失或参数错误则修复 `zentaopms-web/src/api/*.js` 或调用方。
4. **响应约定**：成功 `result: "success"` + `data`/`pager`/`id`；业务失败 `result: "fail"` + `message`；前端 4xx 用 `err.response?.data?.message`。

本文档可与 `PHP-JAVA-SERVICE-COMPARISON.md` 配合使用：后者记录已修复的 Service 逻辑差异，本文档提供按功能点的完整检查清单。**新增/修改接口时**可先看同目录下的 `DEV-CHECKLIST.md`（精简版自查清单）；回归/冒烟测试可参考 `REGRESSION-CHECKLIST.md`。

---

## 五、执行结论（检查完成记录）

**执行时间**：按文档逐项执行并打勾完成。

**Java 后端**：第二节 2.1 通用检查、2.2 按模块细节检查已逐项核对代码，结论如下。
- 各模块均存在对应 `XxxController` 且 `@RequestMapping("/api/xxx")` 与前端一致。
- 列表/创建/编辑/删除的返回结构、软删除、校验（部门/公司/全局组）、action 记录（Branch/Doc/Module/ProductPlan/Release 等）、Service 逻辑（Story/Bug close 置 assignedTo=closed、batchAssignTo 跳过 closed、ProductPlan delete 与 changeParentField、Release delete 清理 shadow/build、Module 级联与 fix）均已与文档描述一致。
- 产品/需求/缺陷/用例 list 已支持文档所列筛选与分页 pager；用户 list 已支持 account、dept 筛选；通知 list 按 createdBy 分页时返回 pager；操作记录 list 支持分页并返回 pager。

**Vue 前端**：第三节 3.1～3.3 已逐项核对 `zentaopms-web/src/api/*.js`。
- 各 API 模块 base 路径、HTTP 方法、返回取 `r.data`、RequestBody/RequestParam 与字段名（productIds/productIDs、storyIds、docIds、lib、module、ids 等）均与后端一致。
- 2.2/3.3 已覆盖产品、项目、执行、需求、任务、缺陷、用例、构建、发布、计划、项目需求、权限组、部门、公司、文档、树、分支、通知、邮件、版本库、操作、管理员、待办、设置、报表、**看板**、文件、测试单、测试套件、文档库、用户、我的、搜索等模块；batchDeleteNotify 使用 DELETE 且 `{ data: body }`，closePlan 传 body `{ closedReason }`，createRepoBranch/unlinkRepoBranch 使用 params。
- **深度核对**：报表（report）、看板（kanban）、我的（my）已逐接口对照 Java Controller 与 Vue `report.js`/`kanban.js`/`my.js`，路径、参数、返回与文档一致；看板已补充至 2.2/3.3 及 `REGRESSION-CHECKLIST.md`。

**已修复不一致**：① 产品 list：ProductController.list 现支持 product、status、branch、pageID、recPerPage，分页时返回 pager；前端 getProductList(params) 已注明。② 需求 list：StoryController.list 增加 moduleID 筛选，与 PHP story list（product/status/branch/module）一致；前端 getStoryList(params) 已注明支持 moduleID。③ 缺陷 list：BugController.list 增加 moduleID 筛选，与 PHP bug list（product/project/status 等）一致；前端 getBugList(params) 已注明支持 moduleID。④ 用例 list：TestCaseController.list 增加 pageID、recPerPage 分页，分页时返回 pager，与 PHP testcase list（分页、product/module 等）一致；前端 getTestCaseList(params) 已注明。⑤ 用户 list：UserController.list 增加 account、dept 筛选，与 PHP user list（分页筛选）一致；前端 getUserList(params) 已注明。⑥ 通知 list：NotifyController.list 按 createdBy 分页时返回 pager，与 PHP notify list（status、createdBy 等）一致；前端 getNotifyList(params) 已注明。⑦ 操作记录 list：ActionController.list 增加 pageID、recPerPage 分页，分页时返回 pager，与通用列表 pager 约定一致；前端 getActionList(objectType, objectID, params) 已注明。
