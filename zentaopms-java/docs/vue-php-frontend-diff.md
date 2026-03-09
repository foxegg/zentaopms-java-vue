# Vue 前端与 PHP 前端全模块差异（步骤 2）

## 一、PHP 前端结构

- 各模块视图：`module/{module}/view/*.html.php`，对应 control 的 `display()`。
- 主入口：index/index、my/index、project/browse、product/index、task/browse、bug/browse 等。
- 约 263+ 视图文件，覆盖 98 个模块。

## 二、Vue 已实现页面（zentaopms-web）

| 分类 | Vue 页面/路由 | 对应 PHP |
|------|---------------|----------|
| 登录 | Login.vue | user/login |
| 首页 | Home.vue | index/index |
| 用户 | UserList, UserView, UserCreate, UserEdit, UserBatchCreate, UserBatchEdit | user/browse, view, create, edit |
| 用户子 | UserTask, UserBug, UserStory, UserTodo, UserTesttask, UserTestcase, UserExecution, UserProfile, UserDynamic | user/task, bug, story, todo, testtask, testcase, execution, profile, dynamic |
| 我的地盘 | MyIndex, MyTodo, MyTask, MyBug, MyStory, MyDynamic, MyProfile, MyPreference | my/index, todo, task, bug, story, dynamic, profile, preference |
| 项目 | ProjectList, ProjectView, ProjectCreate, ProjectEdit, ProjectTeam, ProjectProducts | project/browse, view, create, edit, team, manageProducts |
| 产品 | ProductList, ProductView, ProductCreate, ProductEdit | product/browse, view, create, edit |
| 任务 | TaskList, TaskView, TaskCreate, TaskEdit | task/browse, view, create, edit |
| 需求 | StoryList, StoryView, StoryCreate, StoryEdit | story/browse, view, create, edit |
| Bug | BugList, BugView, BugCreate, BugEdit | bug/browse, view, create, edit |
| 用例 | TestCaseList, TestCaseView | testcase/browse, view |
| 测试任务 | TestTaskList, TestTaskView | testtask/browse, view |
| 版本 | ReleaseList, ReleaseView | release/browse, view |
| 构建 | BuildList, BuildView | build/browse, view |
| 组织 | CompanyList, CompanyView, GroupList, GroupView, DeptTree | company, group, dept |
| 文档 | DocList, DocView | doc/zentaoList, view |
| 扩展 | ExtensionList | extension/browse |
| 节假日 | HolidayList, HolidayCreate, HolidayEdit | holiday/browse, create, edit |
| 主机 | HostList, HostView, HostCreate, HostEdit | host/browse, view, create, edit |
| Jenkins | JenkinsList, JenkinsView, JenkinsCreate, JenkinsEdit | jenkins/browse, view, create, edit |
| SonarQube | SonarqubeList, SonarqubeView, SonarqubeCreate, SonarqubeEdit | sonarqube/browse, view, create, edit |
| 设计 | DesignList | design/browse |
| 合并请求 | MrList | mr/browse |
| 反馈 | FeedbackList | feedback/browse |
| **执行** | ExecutionList, ExecutionView, ExecutionCreate, ExecutionEdit | execution/browse, view, create, edit |
| **待办** | TodoList, TodoView, TodoCreate, TodoEdit | todo/browse, view, create, edit |
| **计划** | ProductPlanList, ProductPlanView, ProductPlanCreate, ProductPlanEdit | productplan/list, view, create, edit |
| **项目集** | ProgramList, ProgramView, ProgramCreate, ProgramEdit | program/list, view, create, edit |
| **看板** | KanbanSpaceList, KanbanSpaceView, KanbanSpaceCreate, KanbanSpaceEdit, KanbanCreate, KanbanBoard（含 createCard） | kanban/space, view, create, edit, createCard |
| **报表** | ReportIndex, ReportRemind | report/index, remind, annualData |
| **统计** | MetricList, MetricView | metric/list, view |
| **搜索** | SearchIndex | search/query, index |
| **测试报告** | TestReportList, TestReportView, TestReportCreate, TestReportEdit | testreport/browse, view, create, edit |
| **测试套件** | TestSuiteList, TestSuiteView, TestSuiteCreate, TestSuiteEdit | testsuite/list, view, create, edit |
| **分支** | BranchList, BranchView, BranchCreate, BranchEdit | branch/list, view, create, edit |
| **树** | TreeBrowse, TreeEdit | tree/browse, edit |
| **后台** | AdminIndex, AdminConfig, AdminSafe, AdminLog | admin/index, config, safe, log |
| **系统** | SystemInfo | system/info |
| **设置** | SettingConfig | setting/config |

## 三、Vue 缺失/占位（相对 PHP 全模块）

- **已补齐**：执行、计划（productplan、program）、看板、报表、统计、搜索、待办、测试报告、测试套件、分支、树、后台、系统、设置均已提供对应 Vue 页面（见上表）。**任务、需求、Bug、构建、版本** 已补 Create/Edit 页面及列表/详情页的「新建」「编辑」入口；**项目、产品** 已补 Create/Edit/团队/关联产品；**代码库** 已补 RepoList、RepoView；**我的地盘** 已补积分、工作、日历页。
- **文件**：file/download, buildForm → 多通过接口调用，无独立文件管理页（可按需后续补）。
- **扩展/集成（未做独立前端页）**：git, gitea, gitlab, gogs, serverroom, ci, instance, upgrade, tutorial, sso, store, space, ai, aiapp, bi 等 → 仅扩展列表中有入口说明，无独立 Vue 页面（host、jenkins、sonarqube、design、mr、feedback 已实现）。

## 四、步骤 2 结论与迭代建议

- **步骤 1（Java vs PHP）**：已无差异，见 php-java-all-modules-diff.md、java-php-method-diff.md。
- **已对齐（步骤 2）**：用户、我的地盘、项目、产品、任务、需求、Bug、用例、测试任务、版本、构建、组织、文档、节假日、扩展列表、主机、Jenkins、SonarQube、设计、合并请求、反馈；**执行、计划、待办、看板、报表、统计、搜索、测试报告、测试套件、分支、树、后台、系统、设置** 均已补全对应 Vue 页面。
- **可选后续**：文件管理独立页、各扩展/集成模块的独立 Vue 页面（按需补）；其余与 PHP 前端在功能形状上已对齐。
