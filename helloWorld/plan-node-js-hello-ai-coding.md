# Plan: Node.js Hello AI Coding 前端页面

## Context

在空项目目录 `d:\Aidata\code\helloWorld` 中创建一个基于 Node.js 的简单 HTTP 服务器，提供前端页面显示 "hello ai coding"，监听 8088 端口。

## 实现方案

创建单个文件 `server.js`，包含：

1. **HTTP 服务器** — 使用 Node.js 内置 `http` 模块创建服务器，监听 8088 端口
2. **HTML 响应** — 返回一个包含 "hello ai coding" 文字的 HTML 页面，设置合适的 Content-Type
3. **控制台日志** — 服务器启动时打印访问地址

### 文件变更

- **新建**: `server.js` — 唯一的项目文件，包含服务器完整逻辑

### 关键设计

- 使用 Node.js 内置模块（`http`），无需安装任何依赖
- HTML 页面包含基本样式，文字居中显示
- 响应 `Content-Type: text/html; charset=utf-8` 确保中文正常显示

## 验证

1. 运行 `node server.js`
2. 浏览器访问 `http://localhost:8088`
3. 确认页面显示 "hello ai coding"
