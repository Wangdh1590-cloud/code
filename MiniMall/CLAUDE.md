<!-- superpowers-zh:begin (do not edit between these markers) -->
# Superpowers-ZH 中文增强版

本项目已安装 superpowers-zh 技能框架（20 个 skills）。

## 核心规则

1. **收到任务时，先检查是否有匹配的 skill** — 哪怕只有 1% 的可能性也要检查
2. **设计先于编码** — 收到功能需求时，先用 brainstorming skill 做需求分析
3. **测试先于实现** — 写代码前先写测试（TDD）
4. **验证先于完成** — 声称完成前必须运行验证命令

## 可用 Skills

Skills 位于 `.claude/skills/` 目录，每个 skill 有独立的 `SKILL.md` 文件。

- **brainstorming**: 在任何创造性工作之前必须使用此技能——创建功能、构建组件、添加功能或修改行为。在实现之前先探索用户意图、需求和设计。
- **chinese-code-review**: 中文 review 沟通参考——话术模板、分级标注（必须修复/建议修改/仅供参考）、国内团队常见反模式应对。仅在用户显式 /chinese-code-review 时调用，不要根据上下文自动触发。
- **chinese-commit-conventions**: 中文 commit 与 changelog 配置参考——Conventional Commits 中文适配、commitlint/husky/commitizen 中文模板、conventional-changelog 中文配置。仅在用户显式 /chinese-commit-conventions 时调用，不要根据上下文自动触发。
- **chinese-documentation**: 中文文档排版参考——中英文空格、全半角标点、术语保留、链接格式、中文文案排版指北约定。仅在用户显式 /chinese-documentation 时调用，不要根据上下文自动触发。
- **chinese-git-workflow**: 国内 Git 平台配置参考——Gitee、Coding.net、极狐 GitLab、CNB 的 SSH/HTTPS/凭据/CI 接入差异与镜像同步配置。仅在用户显式 /chinese-git-workflow 时调用，不要根据上下文自动触发。
- **dispatching-parallel-agents**: 当面对 2 个以上可以独立进行、无共享状态或顺序依赖的任务时使用
- **executing-plans**: 当你有一份书面实现计划需要在单独的会话中执行，并设有审查检查点时使用
- **finishing-a-development-branch**: 当实现完成、所有测试通过、需要决定如何集成工作时使用——通过提供合并、PR 或清理等结构化选项来引导开发工作的收尾
- **mcp-builder**: MCP 服务器构建方法论 — 系统化构建生产级 MCP 工具，让 AI 助手连接外部能力
- **receiving-code-review**: 收到代码审查反馈后、实施建议之前使用，尤其当反馈不明确或技术上有疑问时——需要技术严谨性和验证，而非敷衍附和或盲目执行
- **requesting-code-review**: 完成任务、实现重要功能或合并前使用，用于验证工作成果是否符合要求
- **subagent-driven-development**: 当在当前会话中执行包含独立任务的实现计划时使用
- **systematic-debugging**: 遇到任何 bug、测试失败或异常行为时使用，在提出修复方案之前执行
- **test-driven-development**: 在实现任何功能或修复 bug 时使用，在编写实现代码之前
- **using-git-worktrees**: 当需要开始与当前工作区隔离的功能开发，或在执行实现计划之前使用——通过原生工具或 git worktree 回退机制确保隔离工作区存在
- **using-superpowers**: 在开始任何对话时使用——确立如何查找和使用技能，要求在任何响应（包括澄清性问题）之前调用 Skill 工具
- **verification-before-completion**: 在宣称工作完成、已修复或测试通过之前使用，在提交或创建 PR 之前——必须运行验证命令并确认输出后才能声称成功；始终用证据支撑断言
- **workflow-runner**: 在 Claude Code / OpenClaw / Cursor 中直接运行 agency-orchestrator YAML 工作流——无需 API key，使用当前会话的 LLM 作为执行引擎。当用户提供 .yaml 工作流文件或要求多角色协作完成任务时触发。
- **writing-plans**: 当你有规格说明或需求用于多步骤任务时使用，在动手写代码之前
- **writing-skills**: 当创建新技能、编辑现有技能或在部署前验证技能是否有效时使用

## 如何使用

当任务匹配某个 skill 时，使用 `Skill` 工具加载对应 skill 并严格遵循其流程。绝不要用 Read 工具读取 SKILL.md 文件。

如果你认为哪怕只有 1% 的可能性某个 skill 适用于你正在做的事情，你必须调用该 skill 检查。
<!-- superpowers-zh:end -->

# Mini Mall — 微型电商项目

## 项目结构

```
MiniMall/
├── mini-mall-server/                # Spring Boot 2.7.18 后端 (端口 8080)
│   ├── src/main/java/com/minimall/
│   │   ├── MiniMallApplication.java # 启动入口
│   │   ├── config/                  # SecurityConfig, CorsConfig, MyBatisPlusConfig, InitDataRunner
│   │   ├── controller/              # 公开接口
│   │   │   └── admin/               # 管理员接口
│   │   ├── service/ + impl/         # 业务层
│   │   ├── mapper/                  # MyBatis-Plus Mapper
│   │   ├── entity/                  # User, Product, Category, CartItem, Order, OrderItem
│   │   ├── dto/                     # 请求/响应 DTO
│   │   ├── common/                  # Result, BusinessException, GlobalExceptionHandler
│   │   └── security/                # JwtTokenUtil, JwtAuthenticationFilter, SecurityUtils
│   └── src/main/resources/
│       ├── application.yml
│       └── db/                      # schema.sql, data.sql
├── mini-mall-web/                   # Vue 3 前端 (端口 5173)
│   ├── src/
│   │   ├── router/index.js          # 路由 + 导航守卫
│   │   ├── stores/                  # Pinia: user.js (登录状态), cart.js (购物车)
│   │   ├── api/                     # axios 封装: request.js, auth.js, product.js, cart.js, order.js, category.js
│   │   ├── views/                   # 页面组件 (含 admin/ 子目录)
│   │   ├── components/layout/       # AppHeader.vue, AppFooter.vue
│   │   └── styles/global.css
│   └── vite.config.js               # 代理 /api → localhost:8080
└── plan.md                          # 架构设计文档
```

## 技术栈

- **后端**: Java 8, Spring Boot 2.7.18, Spring Security, MyBatis-Plus 3.5.5, SQLite, jjwt 0.9.1
- **前端**: Node 18, Vue 3.4, Vite 5, Element Plus 2.7, Vue Router 4, Pinia 2, axios
- **工具**: Lombok, Maven Wrapper (mvnw)

## 数据库

- SQLite 文件: `mini-mall-server/data/minimall.db` (项目第一次启动时自动创建)
- DDL: `src/main/resources/db/schema.sql` (6 张表)
- 种子数据: `src/main/resources/db/data.sql` (5 分类 + 20 商品)
- 管理员/用户账号: `InitDataRunner.java` 在启动时自动创建

## 启动方式

```bash
# 后端 — IDE 打开 mini-mall-server，运行 MiniMallApplication.main()
# 或命令行: cd mini-mall-server && ./mvnw spring-boot:run

# 前端 — cd mini-mall-web && npm run dev
```

## 内置账号

| 角色 | 邮箱 | 密码 |
|------|------|------|
| 管理员 | admin@minimall.com | admin123 |
| 用户 | user@minimall.com | 123456 |

## 关键配置

- `application.yml`: 端口 8080, SQLite 数据源, JWT secret/expiration, `spring.sql.init.encoding: UTF-8`
- `vite.config.js`: 端口 5173, `/api` 代理至 `localhost:8080`
- **SQL 脚本编码**: 必须 `encoding: UTF-8`，否则中文乱码
- **SQLite JDBC URL**: `jdbc:sqlite:data/minimall.db`，启动前确保 `data/` 目录存在

## API 接口

- 公开: `GET /api/products`, `GET /api/categories`, `POST /api/auth/*`
- 需登录: `/api/cart/*`, `/api/orders/*` (Header: `Authorization: Bearer <token>`)
- 管理员: `/api/admin/*` (需 ADMIN 角色)
- 统一响应: `{ code: 200, data: ..., message: "..." }`

## 关键设计决策

- **价格 Integer（分）**: SQLite 无 DECIMAL，避免浮点精度
- **CartItem @@unique([userId, productId])**: 同一商品自动合并数量
- **OrderItem 快照**: productName + productPrice 固化，后续商品编辑不影响历史订单
- **Entity 日期字段用 String**: SQLite JDBC 对 LocalDateTime 兼容性差，统一用 String 映射
- **前端 token**: 存 localStorage，axios 拦截器自动注入 Authorization header
- **导航守卫**: router.beforeEach 检查 requiresAuth / requiresAdmin / guest meta 字段
