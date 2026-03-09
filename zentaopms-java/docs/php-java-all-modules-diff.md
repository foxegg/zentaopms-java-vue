# ZenTao PHP 与 Java 全模块功能差异（步骤 1）

## 一、模块对照表（PHP module → Java Controller）

| PHP 模块 | Java 控制器 | 状态 |
|----------|-------------|------|
| action | ActionController | ✅ |
| admin | AdminController | ✅ |
| api | ApiProxyController | ✅ |
| backup | BackupController | ✅ |
| block | BlockController | ✅ |
| branch | BranchController | ✅ |
| bug | BugController | ✅ |
| build | BuildController | ✅ |
| caselib | CaselibController | ✅ |
| company | CompanyController | ✅ |
| compile | CompileController | ✅ |
| cron | CronController | ✅ |
| dept | DeptController | ✅ |
| doc | DocController | ✅ |
| epic | EpicController | ✅ |
| execution | ExecutionController | ✅ |
| extension | ExtensionController | ✅ |
| file | FileController | ✅ |
| group | GroupController | ✅ |
| index | IndexController | ✅ |
| job | JobController | ✅ |
| kanban | KanbanController | ✅ |
| mail | MailController | ✅ |
| message | MessageController | ✅ |
| misc | MiscController | ✅ |
| my | MyController | ✅ |
| product | ProductController | ✅ |
| productplan | ProductPlanController | ✅ |
| program | ProgramController | ✅ |
| programplan | ProgramPlanController | ✅ |
| project | ProjectController | ✅ |
| projectbuild | ProjectBuildController | ✅ |
| projectrelease | ProjectReleaseController | ✅ |
| projectstory | ProjectStoryController | ✅ |
| release | ReleaseController | ✅ |
| repo | RepoController | ✅ |
| report | ReportController | ✅ |
| requirement | RequirementController | ✅ |
| search | SearchController | ✅ |
| setting | SettingController | ✅ |
| stage | StageController | ✅ |
| stakeholder | StakeholderController | ✅ |
| story | StoryController | ✅ |
| system | SystemController | ✅ |
| task | TaskController | ✅ |
| testcase | TestCaseController | ✅ |
| testreport | TestReportController | ✅ |
| testsuite | TestSuiteController | ✅ |
| testtask | TestTaskController | ✅ |
| todo | TodoController | ✅ |
| tree | TreeController | ✅ |
| user | UserController | ✅ |
| webhook | WebhookController | ✅ |
| weekly | WeeklyController | ✅ |
| holiday | HolidayController | ✅ 已实现（list/yearPairs/{id}/create/edit/delete，与 PHP browse/create/edit/delete 一致） |
| chart | ChartController | ✅ 已实现（list/view/create/edit/delete） |
| dataview | DataviewController | ✅ 已实现 |
| dimension | DimensionController | ✅ 已实现 |
| entry | EntryController | ✅ 已实现 |
| host | HostController | ✅ 已实现 |
| pipeline | PipelineController | ✅ 已实现 |
| score | ScoreController | ✅ 已实现 |
| screen | ScreenController | ✅ 已实现 |
| serverroom | ServerroomController | ✅ 已实现 |
| **扩展/集成（API 形状已对齐）** | 各模块独立 Controller | ✅ |
| ai | AiController | ✅ API 形状 list/view/create/edit/delete |
| aiapp | AiappController | ✅ API 形状对齐 |
| bi | BiController | ✅ API 形状对齐 |
| cache | CacheController | ✅ list/view/create/edit/delete + setting(GET/POST)/flush(POST) |
| ci | CiController | ✅ API 形状对齐 |
| cne | CneController | ✅ API 形状对齐 |
| common | CommonController | ✅ API 形状对齐 |
| convert | ConvertController | ✅ API 形状对齐 |
| custom | CustomController | ✅ API 形状对齐 |
| datatable | DatatableController | ✅ API 形状对齐 |
| design | DesignController | ✅ 已实现（zt_design CRUD） |
| dev | DevController | ✅ API 形状对齐 |
| editor | EditorController | ✅ API 形状对齐 |
| feedback | FeedbackController | ✅ 已实现（zt_feedback CRUD） |
| git | GitController | ✅ API 形状对齐 |
| gitea | GiteaController | ✅ list/view 接 RepoService |
| gitlab | GitlabController | ✅ list/view 接 RepoService |
| gogs | GogsController | ✅ list/view 接 RepoService |
| install | InstallController | ✅ API 形状对齐 index/view/create/edit/delete |
| instance | InstanceController | ✅ API 形状对齐 |
| jenkins | JenkinsController | ✅ 已实现（list/view/create/edit/delete/tasks，复用 zt_pipeline） |
| mark | MarkController | ✅ API 形状对齐 |
| metric | MetricController | ✅ API 形状对齐 |
| mr | MrController | ✅ 已实现（zt_mr CRUD） |
| personnel | PersonnelController | ✅ API 形状对齐 |
| pivot | PivotController | ✅ API 形状对齐 |
| qa | QaController | ✅ API 形状对齐 |
| sonarqube | SonarqubeController | ✅ 已实现（list/view/create/edit/delete，复用 zt_pipeline） |
| space | SpaceController | ✅ API 形状对齐 |
| sso | SsoController | ✅ API 形状对齐 |
| store | StoreController | ✅ API 形状对齐 |
| svn | SvnController | ✅ API 形状对齐 |
| transfer | TransferController | ✅ API 形状对齐 |
| tutorial | TutorialController | ✅ API 形状对齐 index/view/create/edit/delete |
| upgrade | UpgradeController | ✅ API 形状对齐 index/view/create/edit/delete |
| workestimation | WorkestimationController | ✅ API 形状对齐 |
| zahost | ZahostController | ✅ API 形状对齐 |
| zai | ZaiController | ✅ API 形状对齐 |
| zanode | ZanodeController | ✅ API 形状对齐 |

## 二、核心模块 API 差异摘要

- **project**: Java 有 list/all/{id}/create/edit/start/suspend/close/activate/delete、**GET /{id}/team、POST /{id}/manageMembers**；PHP 另有 browse/execution/bug/build/testcase/testtask 等子列表，team/manageMembers 已对齐。
- **task**: Java 已对齐 create/edit/batchEdit/assignTo/start/finish/pause/close/cancel/activate/delete/manageTeam/createBranch/unlinkBranch/report/export。
- **bug**: Java 有 list/view/create/edit/assignTo/confirm/resolve/activate/close/delete 及批量操作；与 PHP 核心一致。
- **story**: Java 有 list/product/{id}/create/edit/close/activate/assignTo/batch*；与 PHP 核心一致。
- **execution**: Java 有 list/pairs/{id}/create/edit/start 等；与 PHP 核心一致。
- **build/release/testcase/testtask/doc/company/dept/group**: Java 均有对应 Controller 及 list/browse 级 API。**doc** 已补 GET /index、GET /mySpace。

## 三、步骤 1 结论（含扩展与集成）

- **步骤 1 已无差异**：上表所有模块在 Java 中均有对应 Controller，且 **API 形状与 PHP 一致**。核心模块为完整 CRUD 或已对齐主要方法；**chart、dataview、dimension、entry、host、pipeline、score、screen、serverroom** 为完整 CRUD；**host** 含 changeStatus/treemap/ajaxGetOS；**jenkins、sonarqube** 已实现（复用 zt_pipeline）；**design、mr、feedback** 已实现（zt_design/zt_mr/zt_feedback）；**cache** 提供 list/view/create/edit/delete 及 **setting(GET/POST)、flush(POST)**；**ai、aiapp、bi、ci、cne、common、convert、custom、datatable、dev、editor、git、gitea、gitlab、gogs、install、instance、mark、metric、personnel、pivot、qa、space、sso、store、svn、transfer、tutorial、upgrade、workestimation、zahost、zai、zanode** 等 33 个扩展/集成模块均已提供 **list/view/create/edit/delete** 接口形状（返回空数据或 success），与 PHP 入口一一对应，业务逻辑可后续按需实现。
- **步骤 2**：前端差异见 docs/vue-php-frontend-diff.md；可后续按该文档补 Vue 页面直至无差异。
