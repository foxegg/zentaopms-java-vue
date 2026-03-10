/**
 * 与 PHP 禅道各 module 的 lang/zh-cn.php 保持一致的文案（仅前端展示用，接口仍调用 Java 服务）
 */
export const app = {
  title: '禅道项目管理系统',
  welcome: '欢迎使用禅道项目管理系统。从左侧导航进入用户、项目等模块。',
}

export const todo = {
  common: '待办',
  index: '待办一览',
  summaryLine: '待办汇总（任务 / Bug / 需求）',
  create: '添加待办',
  edit: '编辑待办',
  view: '待办详情',
  batchCreate: '批量添加',
  assignTo: '指派给',
  date: '日期',
  type: '类型',
  name: '待办名称',
  status: '状态',
  desc: '描述',
  pri: '优先级',
  id: '编号',
  confirmDelete: '您确定要删除这条待办吗？',
  noTodo: '暂无待办',
  namePlaceholder: '待办标题或内容',
  allStatus: '全部状态',
  finish: '完成',
  statusList: {
    wait: '未开始',
    doing: '进行中',
    done: '已完成',
    closed: '已关闭',
  },
  typeList: {
    custom: '自定义',
    bug: 'Bug',
    task: '任务',
    story: '需求',
    testtask: '测试单',
  },
}

export const bug = {
  common: 'Bug',
  browse: 'Bug列表',
  create: '提Bug',
  edit: '编辑Bug',
  product: '所属产品',
  project: '所属项目',
  title: 'Bug标题',
  openedBuild: '影响版本',
  selectBuild: '请选择版本',
  severity: '严重程度',
  pri: '优先级',
  steps: '重现步骤',
  assignTo: '指派给',
  status: '状态',
  selectProduct: '请选择产品',
  selectProject: '请选择项目',
  backDetail: '返回详情',
  all: '全部',
  legendSteps: '重现步骤',
  legendBasicInfo: '基本信息',
  openedBy: '由谁创建',
  resolvedBy: '解决者',
  resolution: '解决方案',
}

export const story = {
  create: '提需求',
  common: '需求',
  createStory: '添加需求',
  editStory: '编辑需求',
  browse: '需求列表',
  view: '需求详情',
  edit: '编辑',
  product: '所属产品',
  title: '需求名称',
  spec: '描述',
  pri: '优先级',
  selectProduct: '请选择产品',
  id: '编号',
  assignTo: '指派',
  status: '状态',
  all: '全部',
}

export const task = {
  common: '任务',
  index: '任务一览',
  browse: '任务列表',
  create: '建任务',
  edit: '编辑任务',
  view: '查看任务',
  project: '所属项目',
  execution: '所属迭代',
  name: '任务名称',
  type: '任务类型',
  estimate: '最初预计',
  estimateLabel: '预计（单位：小时）',
  deadline: '截止日期',
  desc: '任务描述',
  selectProject: '请选择项目',
  selectExecution: '请选择迭代',
  id: '编号',
  status: '状态',
  pri: '优先级',
  executionName: '迭代',
  all: '全部',
  typeList: {
    design: '设计',
    devel: '开发',
    request: '需求',
    test: '测试',
    study: '研究',
    discuss: '讨论',
    ui: '界面',
    affair: '事务',
    misc: '其他',
  },
}

export const branch = {
  common: '分支',
  create: '新建分支',
  edit: '编辑分支',
  name: '名称',
  product: '产品',
  notFound: '分支不存在',
  emptyTip: '暂无分支',
}

export const build = {
  common: '构建',
  browse: '构建列表',
  create: '创建构建',
  edit: '编辑构建',
  view: '构建详情',
  product: '所属产品',
  project: '所属项目',
  name: '名称',
  date: '打包日期',
  desc: '描述',
  selectProduct: '请选择产品',
  selectProject: '请选择项目',
  id: 'ID',
}

export const product = {
  common: '产品',
  index: '产品主页',
  browse: '产品列表',
  create: '添加产品',
  edit: '编辑产品',
  view: '产品概况',
  name: '产品名称',
  code: '产品代号',
  desc: '产品描述',
  status: '状态',
  type: '类型',
  typeList: { normal: '正常' },
  select: '请选择产品',
  all: '所有产品',
  plan: '计划',
}

export const productplan = {
  common: '产品计划',
  create: '新建产品计划',
  edit: '编辑产品计划',
  notFound: '计划不存在',
  emptyTip: '暂无计划',
  noStories: '暂无关联需求',
  relatedStory: '关联需求',
}

export const stage = {
  common: '阶段',
  create: '新建阶段',
  edit: '编辑阶段',
  notFound: '阶段不存在',
  emptyTip: '暂无阶段',
  workflowGroup: '工作流组',
  percent: '百分比',
  projectType: '项目类型',
}

export const chart = {
  common: '图表',
  create: '新建图表',
  edit: '编辑图表',
  code: '代码',
  stageDraft: '草稿',
  stageReleased: '已发布',
}

export const caselib = {
  common: '用例库',
  create: '新建用例库',
  edit: '编辑用例库',
  noProduct: '无',
  noProject: '无',
  emptyTip: '暂无用例库',
}

export const kanban = {
  common: '看板',
  create: '新建看板',
  edit: '编辑看板',
  space: '看板空间',
  spaceCreate: '新建看板空间',
  spaceEdit: '编辑看板空间',
  spaceNotFound: '空间不存在',
  cardCreate: '新建卡片',
  cardList: '卡片列表',
  noCards: '暂无卡片',
  notFound: '看板不存在',
  cardEditTip: '编辑卡片（可后续扩展弹窗编辑）',
  spaceListEmpty: '暂无看板空间',
  noBoards: '暂无看板',
  backSpaceList: '返回空间列表',
}

export const tree = {
  common: '树',
  browse: '树模块',
  browseHint: '（可传 productID/projectID 等参数）',
  parent: '父级',
  edit: '编辑树节点',
  nodeNotFound: '节点不存在',
}

export const project = {
  common: '项目',
  browse: '项目列表',
  create: '创建项目',
  edit: '编辑项目',
  view: '项目概况',
  name: '项目名称',
  code: '项目代号',
  desc: '项目描述',
  begin: '计划开始',
  end: '计划完成',
  status: '状态',
  id: '编号',
  team: '团队',
  products: '关联产品',
  type: '类型',
  typeList: { sprint: '冲刺', kanban: '看板', stage: '阶段' },
  backProject: '返回项目',
  teamTitle: '项目团队',
  productsEmpty: '暂无关联产品',
  selectProducts: '选择要关联的产品',
  linkedProducts: '当前已关联',
  selectedCount: '已选 {n} 个产品',
}

export const doc = {
  common: '文档',
  viewDetail: '文档详情',
  doclib: '文档库',
  selectProduct: '请选择产品',
  selectLib: '选择文档库',
  title: '标题',
  content: '内容',
  id: '编号',
}

export const execution = {
  common: '迭代',
  backExecution: '返回执行',
  dynamicTitle: '执行动态',
  list: '迭代列表',
  create: '添加迭代',
  edit: '编辑迭代',
  view: '迭代概况',
  project: '所属项目',
  name: '迭代名称',
  code: '代号',
  status: '状态',
  begin: '计划开始',
  end: '计划完成',
  dateRange: '计划起止',
  date: '日期',
  detail: '详情',
  object: '对象',
  action: '操作',
  selectProject: '请选择项目',
  noExecution: '暂无迭代',
  id: '编号',
  start: '开始',
  suspend: '挂起',
  activate: '激活',
  close: '关闭',
  delete: '删除迭代',
  team: '团队',
  teamTitle: '迭代团队',
  dynamic: '动态',
  backExecution: '返回迭代',
  currentMembers: '当前成员',
  noMembers: '暂无成员',
  manageMembers: '管理成员',
  account: '账号',
  role: '角色',
  days: '可用天数',
  hours: '可用工时',
  addMember: '添加成员',
  remove: '移除',
}

export const weekly = {
  common: '周报',
  create: '新建周报',
  edit: '编辑周报',
  view: '周报详情',
  backView: '返回查看',
  project: '项目',
  weekStart: '周开始日期',
  progress: '进度',
  workload: '工作量',
  emptyTip: '该项目暂无周报',
  selectProjectFirst: '请先选择项目',
  notFound: '未找到该周报',
}

export const repo = {
  common: '代码库',
  path: '路径',
  encoding: '编码',
  emptyTip: '暂无代码库',
  notFound: '代码库不存在',
}

export const release = {
  browse: '发布列表',
  create: '创建发布',
  edit: '编辑发布',
  product: '所属产品',
  name: '名称',
  date: '计划发布日期',
  desc: '描述',
  selectProduct: '请选择产品',
  id: 'ID',
}

export const my = {
  index: '首页',
  indexAction: '地盘仪表盘',
  todo: '我的待办',
  task: '我的任务',
  bug: '我的Bug',
  story: '我的需求',
  noTodo: '暂时没有待办。',
  noData: '暂无',
  dynamic: '我的动态',
  score: '我的积分',
  work: '待处理',
  calendar: '日程',
  profile: '我的档案',
  preference: '个性化设置',
  recPerPage: '每页条数',
  viewAll: '查看全部',
  workAction: '我的待处理',
  noTask: '暂无任务',
  noBug: '暂无Bug',
  calendarTitle: '我的日历',
  calendarDesc: '按任务与待办的日期展示（可扩展为日历视图）',
  taskSection: '任务',
  todoSection: '待办',
  name: '名称',
  beginDeadline: '开始/截止',
  date: '日期',
}

export const user = {
  common: '用户',
  browse: '用户列表',
  create: '添加用户',
  edit: '编辑用户',
  batchCreate: '批量添加',
  batchEdit: '批量编辑',
  account: '用户名',
  realname: '姓名',
  dept: '部门',
  password: '密码',
  password2: '重复密码',
  email: '邮箱',
  role: '职位',
  newPassword: '新密码',
  originalPassword: '原密码',
  changePassword: '修改密码',
  passwordChanged: '密码已修改',
  unlock: '解锁',
  unbind: '解绑',
  resetPassword: '重置密码',
  confirmUnbind: '确定解绑该用户？',
  confirmDelete: '您确定删除该用户吗？删除后不可恢复。',
  newPasswordPlaceholder: '不填则不修改',
  login: '登录',
  loginTitle: '用户登录',
  loginFailed: '登录失败，请检查您的用户名或密码是否填写正确。',
  placeholderAccount: '请输入用户名',
  placeholderPassword: '请输入密码',
  type: '类型',
  sort: '排序',
  assignedTo: '指派给我',
  openedBy: '由我创建',
  resolvedBy: '由我解决',
  closedBy: '由我关闭',
  reviewedBy: '由我评审',
  finishedBy: '由我完成',
  idDesc: 'ID 降序',
  idAsc: 'ID 升序',
  storyType: '需求类型',
  storyTypeStory: '需求',
  storyTypeRequirement: '用户需求',
  storyTypeEpic: '史诗',
  dynamicTime: '时间',
  dynamicObject: '对象',
  dynamicAction: '动作',
  totalDynamic: '共 {n} 条动态',
  batchCreateTitle: '批量创建用户',
  batchCreateHint: '每行一个用户，账号、姓名、密码为必填。',
  addRow: '+ 增加一行',
  clear: '清空',
  emptyBatchCreateHint: '请点击「增加一行」后填写用户信息。',
  batchEditTitle: '批量编辑用户',
  selectUsersPrompt: '选择用户（勾选要批量编辑的用户）',
  batchModifyFields: '批量修改的字段（仅填写需要修改的项）',
  deptId: '部门ID',
  leaveEmptyNoChange: '不修改留空',
  rolePlaceholder: '如 dev, admin',
  case2Him: '指派给我的',
  caseByHim: '我创建的',
}

export const score = {
  time: '时间',
  type: '类型',
  score: '积分',
  desc: '描述',
  noRecord: '暂无积分记录',
}

export const testsuite = {
  common: '测试套件',
  create: '新建测试套件',
  edit: '编辑测试套件',
  notFound: '套件不存在',
  emptyTip: '暂无测试套件',
  caseList: '用例列表',
  noCases: '暂无用例',
}

export const testreport = {
  common: '测试报告',
  create: '新建报告',
  edit: '编辑报告',
  emptyTip: '暂无测试报告',
}

export const common = {
  saveSuccess: '保存成功',
  loadError: '加载失败',
  operateFail: '操作失败',
  notFound: '记录不存在',
  noData: '暂无数据',
  loading: '加载中...',
  feedback: '反馈',
  status: '状态',
  name: '名称',
  desc: '描述',
  account: '账号',
  design: '设计',
  backList: '返回列表',
  backAdmin: '返回后台',
  create: '新建',
  backDetail: '返回详情',
  view: '查看',
  edit: '编辑',
  delete: '删除',
  confirm: '确定',
  confirmClose: '确定关闭？',
  confirmDelete: '确定删除？',
  confirmRestore: '确定要恢复该备份吗？',
  confirmDeleteBackup: '确定删除该备份？',
  cancel: '取消',
  save: '保存',
  actions: '操作',
  pleaseSelect: '请选择',
  saved: '已保存',
  cacheCleared: '缓存已清除',
  pleaseAddMember: '请至少添加一名成员',
  dateRangeError: '开始日期不能大于结束日期',
  passwordNotMatch: '两次输入的密码不一致',
  leaveEmptyToKeep: '不修改留空',
  testtask: '测试单',
  testcase: '测试用例',
  prevPage: '上一页',
  nextPage: '下一页',
  totalCount: '共 {total} 条',
  pageNum: '第 {n} 页',
  search: '搜索',
  keywordPlaceholder: '输入关键词',
  all: '全部',
  noResult: '无结果',
  titleName: '标题/名称',
}

export const holiday = {
  common: '节假日',
  createAction: '创建节假日',
  editAction: '编辑节假日',
  name: '名称',
  type: '类型',
  begin: '开始日期',
  end: '结束日期',
  desc: '描述',
  checkYear: '年份',
  typeList: { holiday: '假期', working: '补班' },
  beginShort: '开始',
  endShort: '结束',
  emptyTip: '暂时没有节假日。',
  confirmDelete: '确认删除节假日？',
}

export const program = {
  common: '项目集',
  create: '新建项目集',
  edit: '编辑项目集',
  name: '项目集名称',
  notFound: '项目集不存在',
  emptyTip: '暂无项目集',
  selectFirst: '请选择项目或项目集',
}

export const host = {
  common: '主机',
  create: '添加主机',
  edit: '编辑主机',
  name: '名称',
  type: '类型',
  status: '状态',
  os: '操作系统',
  intranet: '内网',
  extranet: '外网',
}

export const admin = {
  title: '后台管理',
  config: '系统配置',
  safe: '安全',
  log: '日志',
  backup: '备份',
  backupTitle: '数据备份',
  runBackup: '执行备份',
  backupFile: '备份文件',
  cron: '定时任务',
  webhook: 'Webhook',
  webhookDetail: 'Webhook 详情',
  domain: '域名',
  sendType: '发送方式',
  message: '消息配置',
  cache: '缓存',
  cacheTitle: '缓存管理',
  cacheConfig: '缓存配置',
  flushCache: '清除缓存',
  upgrade: '升级',
  upgradeTitle: '系统升级',
  tutorial: '教程',
  tutorialTitle: '使用教程',
  sso: 'SSO',
  ssoTitle: '单点登录 (SSO)',
  ssoDetail: 'SSO 配置详情',
  ssoTurnon: '开启',
  ssoAddr: 'SSO 地址',
  ssoCode: '应用代码',
  ssoKey: '密钥',
  restore: '恢复',
  remark: '备注',
  lastRun: '上次执行',
  runBtn: '执行',
  toggleBtn: '开关',
}

export const system = {
  info: '系统信息',
}

export const company = {
  common: '组织',
  viewDetail: '组织详情',
}

export const group = {
  common: '权限组',
  viewDetail: '权限组详情',
}

export const stakeholder = {
  common: '干系人',
  viewDetail: '干系人详情',
  relatedObjectType: '关联对象类型',
  project: '项目',
  program: '项目集',
  selectProjectProgram: '选择项目/项目集',
  user: '用户',
}

export const testtask = {
  common: '测试单',
  viewDetail: '测试单详情',
  list: '测试单列表',
}

export const testcase = {
  common: '测试用例',
  viewDetail: '测试用例详情',
  module: '模块',
  steps: '步骤',
}

export const screen = {
  common: '大屏',
}

export const serverroom = {
  common: '机房',
}

export const entry = {
  common: '应用入口',
}

export const pipeline = {
  common: '流水线',
}

export const extension = {
  title: '扩展与集成',
  module: '模块',
  desc: '说明',
  descTip: '以下模块与 PHP 端一一对应，当前为占位实现，功能待后续完善。',
}

export const report = {
  title: '报表',
  remind: '每日提醒',
  backReport: '返回报表',
  year: '年度',
  yearSuffix: '年',
  bugs: 'Bug 数',
  tasks: '任务数',
  todos: '待办数',
  testTasks: '测试单数',
}

export const dataview = {
  common: '数据视图',
}

export const dept = {
  common: '部门',
}

export const mr = {
  common: '合并请求',
}

export const metric = {
  common: '统计',
  viewDetail: '统计详情',
}

export const setting = {
  title: '系统设置',
}

export const dimension = {
  common: '维度',
}

export const programplan = {
  title: '项目计划 / 阶段',
  viewTitle: '项目计划',
  emptyTip: '该项目暂无计划/阶段',
}

export default { app, todo, bug, story, task, branch, build, product, productplan, project, program, programplan, stage, chart, caselib, kanban, tree, doc, execution, release, repo, weekly, my, user, score, holiday, host, testsuite, testreport, common, admin, system, company, group, stakeholder, testtask, testcase, screen, serverroom, entry, pipeline, extension, report, dataview, dept, metric, mr, setting, dimension }
