# 禅道 Java/Vue 文档导航

本目录为 **zentaopms-java**（后端）与 **zentaopms-web**（前端）的检查清单与回归测试文档入口。

## 核心文档（推荐阅读顺序）

| 文档 | 用途 | 何时使用 |
|------|------|----------|
| [PHP-FEATURE-LIST-AND-CHECKTASKS.md](PHP-FEATURE-LIST-AND-CHECKTASKS.md) | PHP 功能列表 + Java/Vue 逐项检查任务 | 对 PHP 对齐、全量检查、查某模块接口约定 |
| [DEV-CHECKLIST.md](DEV-CHECKLIST.md) | 新增/修改接口时的精简自查清单 | 开发新 API 或改现有接口前快速自检 |
| [REGRESSION-CHECKLIST.md](REGRESSION-CHECKLIST.md) | 冒烟与回归用例、建议顺序、负向用例 | 发版前冒烟、回归测试执行与打勾 |

## 典型使用方式

- **开发新接口**：先看 `DEV-CHECKLIST.md`，再按 `PHP-FEATURE-LIST-AND-CHECKTASKS.md` 第二节/第三节核对对应模块。
- **发版前测试**：按 `REGRESSION-CHECKLIST.md` 第三节「冒烟建议顺序」跑最小路径，再按第二节按模块回归。
- **与 PHP 行为对齐**：以 `PHP-FEATURE-LIST-AND-CHECKTASKS.md` 第一节功能列表为基准，逐项在 Java/Vue 中打勾；差异可记录到 `PHP-JAVA-SERVICE-COMPARISON.md`（若存在）。

## 其他文档

- **PHP-JAVA-SERVICE-COMPARISON.md**、**java-php-business-logic.md** 等：若存在，用于记录已修复的 PHP/Java 逻辑差异或业务对照。
- **sql/**：数据库相关脚本（如 zt_relation.sql 等）。

各文档内的「参考文档」「相关文档索引」可反向跳转到上述文档。
