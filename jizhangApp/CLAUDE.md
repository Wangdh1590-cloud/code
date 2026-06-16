# 记账本 (jizhangApp)

基于 Python + Streamlit + SQLite3 的个人记账 Web 应用。

## 运行方式

```bash
pip install -r requirements.txt
streamlit run app.py
```

浏览器打开 http://localhost:8501

## 项目结构

| 文件 | 职责 |
|---|---|
| `app.py` | 主入口，编排所有 UI 组件 |
| `config.py` | 分类常量、数据库路径、页面配置 |
| `database.py` | 所有 SQLite 操作（建表、CRUD、统计） |
| `ui/sidebar.py` | 添加账目表单（侧边栏） |
| `ui/list_view.py` | 筛选 + 表格 |
| `ui/stats_view.py` | 柱状图 + 分类统计表 |
| `ui/delete_view.py` | 按 ID 删除 |

## 架构约定

- **配置层** `config.py`：所有可调常量集中在此，改配置不改逻辑
- **数据层** `database.py`：纯 SQL 操作，返回 DataFrame 或基本类型，不涉及 Streamlit
- **界面层** `ui/`：每个模块暴露一个 `render_xxx()` 函数，只负责渲染
- **编排层** `app.py`：组装调用，不含业务逻辑
- 分类列表统一从 `config.CATEGORIES` 引用，不硬编码
- 日期格式统一 `YYYY-MM-DD`，月份格式 `YYYY-MM`
- 数据库文件在 `data/` 目录，运行时自动创建

## 数据库

- SQLite3 单文件 `data/jizhang.db`
- 表 `records`：id, amount, category, date, note, created_at
- 分类预设：餐饮、交通、购物、娱乐、居住、其他
- 索引：date、category

## 常见开发任务

### 新增分类
编辑 `config.py` 中的 `CATEGORIES` 列表即可，UI 和筛选自动更新。

### 新增功能页面
1. 在 `ui/` 下新建模块文件，实现 `render_xxx()` 函数
2. 在 `app.py` 中 import 并调用

### 修改数据库结构
编辑 `database.py` 的 `init_db()` 函数。如需迁移旧数据，在 `init_db()` 中添加 ALTER TABLE 逻辑。
