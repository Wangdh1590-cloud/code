# Mini Mall 架构设计计划

## Context

从零构建微型电商项目 Mini Mall。技术栈：Spring Boot 2.7.x + Vue 3 + Element Plus + MyBatis-Plus + SQLite，基于本机 Java 8 开发。前后端分离，功能覆盖商品浏览、用户注册登录、购物车、下单支付（模拟）、后台管理。

---

## 1. 项目目录结构

```
MiniMall/
├── mini-mall-server/                    # Spring Boot 后端（端口 8080）
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/minimall/
│       │   ├── MiniMallApplication.java
│       │   ├── config/
│       │   │   ├── SecurityConfig.java        # Spring Security + JWT 过滤器
│       │   │   ├── MyBatisPlusConfig.java      # 分页插件
│       │   │   ├── CorsConfig.java              # 跨域配置
│       │   │   └── WebMvcConfig.java            # 静态资源、拦截器
│       │   ├── controller/
│       │   │   ├── AuthController.java           # /api/auth/*
│       │   │   ├── ProductController.java        # /api/products/*
│       │   │   ├── CategoryController.java       # /api/categories/*
│       │   │   ├── CartController.java           # /api/cart/*
│       │   │   ├── OrderController.java          # /api/orders/*
│       │   │   └── admin/
│       │   │       ├── AdminProductController.java
│       │   │       ├── AdminOrderController.java
│       │   │       └── AdminCategoryController.java
│       │   ├── service/
│       │   │   ├── UserService.java
│       │   │   ├── ProductService.java
│       │   │   ├── CategoryService.java
│       │   │   ├── CartService.java
│       │   │   └── OrderService.java
│       │   ├── service/impl/
│       │   │   ├── UserServiceImpl.java
│       │   │   ├── ProductServiceImpl.java
│       │   │   ├── CategoryServiceImpl.java
│       │   │   ├── CartServiceImpl.java
│       │   │   └── OrderServiceImpl.java
│       │   ├── mapper/
│       │   │   ├── UserMapper.java
│       │   │   ├── ProductMapper.java
│       │   │   ├── CategoryMapper.java
│       │   │   ├── CartItemMapper.java
│       │   │   ├── OrderMapper.java
│       │   │   └── OrderItemMapper.java
│       │   ├── entity/
│       │   │   ├── User.java
│       │   │   ├── Product.java
│       │   │   ├── Category.java
│       │   │   ├── CartItem.java
│       │   │   ├── Order.java
│       │   │   └── OrderItem.java
│       │   ├── dto/
│       │   │   ├── LoginRequest.java
│       │   │   ├── RegisterRequest.java
│       │   │   ├── CartAddRequest.java
│       │   │   └── ... (其他请求/响应 DTO)
│       │   ├── common/
│       │   │   ├── Result.java                  # 统一响应 { code, data, message }
│       │   │   ├── PageResult.java              # 分页响应包装
│       │   │   └── BusinessException.java        # 业务异常
│       │   ├── security/
│       │   │   ├── JwtTokenUtil.java             # JWT 签发/解析
│       │   │   ├── JwtAuthenticationFilter.java  # OncePerRequestFilter
│       │   │   └── UserDetailsServiceImpl.java
│       │   └── util/
│       │       └── PasswordUtil.java             # BCrypt 密码工具
│       └── resources/
│           ├── application.yml
│           ├── db/
│           │   └── schema.sql                    # 建表 DDL
│           └── data.sql                          # 种子数据
│
├── mini-mall-web/                        # Vue 3 前端（端口 5173）
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── main.js                        # 入口：挂载 App、注册路由/Pinia/ElementPlus
│       ├── App.vue                        # 根组件
│       ├── router/
│       │   └── index.js                   # Vue Router 路由定义 + 导航守卫
│       ├── stores/
│       │   ├── user.js                    # 用户登录状态、token 管理
│       │   └── cart.js                    # 购物车数量
│       ├── api/
│       │   ├── request.js                 # axios 实例（baseURL、拦截器、token 注入）
│       │   ├── auth.js                    # 登录/注册 API
│       │   ├── product.js                 # 商品 API
│       │   ├── category.js                # 分类 API
│       │   ├── cart.js                    # 购物车 API
│       │   └── order.js                   # 订单 API
│       ├── views/
│       │   ├── Home.vue                   # 首页：推荐商品
│       │   ├── ProductList.vue            # 商品列表（搜索+分类筛选+分页）
│       │   ├── ProductDetail.vue          # 商品详情
│       │   ├── Login.vue                  # 登录
│       │   ├── Register.vue               # 注册
│       │   ├── Cart.vue                   # 购物车
│       │   ├── OrderList.vue              # 订单列表
│       │   ├── OrderDetail.vue            # 订单详情
│       │   ├── PaySuccess.vue             # 支付成功
│       │   └── admin/
│       │       ├── Dashboard.vue          # 仪表盘
│       │       ├── ProductManage.vue      # 商品管理
│       │       ├── ProductForm.vue        # 新增/编辑商品
│       │       ├── OrderManage.vue        # 订单管理
│       │       └── CategoryManage.vue     # 分类管理
│       ├── components/
│       │   ├── layout/
│       │   │   ├── AppHeader.vue          # 全局头部（搜索框、购物车角标、用户菜单）
│       │   │   ├── AppFooter.vue          # 底部
│       │   │   └── AdminSidebar.vue      # 后台侧边导航
│       │   ├── product/
│       │   │   ├── ProductCard.vue        # 商品卡片
│       │   │   └── CategoryFilter.vue     # 分类筛选栏
│       │   ├── cart/
│       │   │   └── CartSummary.vue        # 购物车结算汇总
│       │   ├── order/
│       │   │   └── OrderStatusBadge.vue   # 订单状态标签
│       │   └── common/
│       │       ├── Pagination.vue         # 分页组件
│       │       └── EmptyState.vue         # 空状态占位
│       └── styles/
│           └── global.css                 # 全局样式
│
└── plan.md                                # 本文件
```

---

## 2. 数据库设计

### 2.1 DDL (`schema.sql`)

```sql
-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(200) NOT NULL,          -- BCrypt 密文
    name        VARCHAR(50)  NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',  -- CUSTOMER / ADMIN
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 分类表
CREATE TABLE IF NOT EXISTS category (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    slug        VARCHAR(50)  NOT NULL UNIQUE,
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(200) NOT NULL,
    slug        VARCHAR(200) NOT NULL UNIQUE,
    description TEXT,
    price       INTEGER      NOT NULL,           -- 以分为单位
    image_url   VARCHAR(500),
    stock       INTEGER      NOT NULL DEFAULT 999,
    is_active   TINYINT      NOT NULL DEFAULT 1,
    category_id INTEGER      NOT NULL,
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- 购物车明细表
CREATE TABLE IF NOT EXISTS cart_item (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     INTEGER      NOT NULL,
    product_id  INTEGER      NOT NULL,
    quantity    INTEGER      NOT NULL DEFAULT 1,
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (user_id)    REFERENCES user(id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    UNIQUE(user_id, product_id)
);

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (               -- "order" 是 SQL 关键字，加 t_ 前缀
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     INTEGER      NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING',  -- PENDING/PAID/SHIPPED/CANCELLED
    total_amount INTEGER     NOT NULL,             -- 以分为单位
    created_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at  DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id      INTEGER      NOT NULL,
    product_id    INTEGER      NOT NULL,
    product_name  VARCHAR(200) NOT NULL,           -- 下单时快照
    product_price INTEGER      NOT NULL,           -- 下单时快照（分）
    quantity      INTEGER      NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES t_order(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id)
);
```

### 2.2 Entity 类映射

MyBatis-Plus 通过注解映射：

```
User        → user           (id, email, password, name, role, createdAt, updatedAt)
Category    → category       (id, name, slug, createdAt)
Product     → product        (id, name, slug, description, price, imageUrl, stock, isActive, categoryId, createdAt, updatedAt)
CartItem    → cart_item      (id, userId, productId, quantity, createdAt, updatedAt)
Order       → t_order        (id, userId, status, totalAmount, createdAt, updatedAt)
OrderItem   → order_item     (id, orderId, productId, productName, productPrice, quantity)
```

关键设计决策：
- **价格 INTEGER（分）**：SQLite 无 DECIMAL，整数回避浮点精度
- **cart_item 复合唯一约束**：同一用户同一商品自动合并数量
- **order_item 快照**：product_name + product_price 固化，后续商品编辑不影响历史订单
- **表名 t_order**："order" 是 SQL 保留字，加前缀避免冲突

---

## 3. API 接口设计

基础路径：`http://localhost:8080/api`

统一响应格式：
```json
{
  "code": 200,
  "data": {},
  "message": "success"
}
```

### 3.1 公开接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/register` | 注册。Body: `{ email, password, name }`。返回用户信息 + token |
| POST | `/api/auth/login` | 登录。Body: `{ email, password }`。返回用户信息 + token |
| GET | `/api/products` | 商品列表。Params: `search`, `category`(slug), `page`(默认1), `size`(默认12) |
| GET | `/api/products/{id}` | 商品详情 |
| GET | `/api/categories` | 全部分类列表 |

### 3.2 需登录接口（Header: `Authorization: Bearer <token>`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/cart` | 当前用户购物车（含商品详情） |
| POST | `/api/cart` | 添加商品。Body: `{ productId, quantity }`。已存在则合并 |
| PUT | `/api/cart/{cartItemId}` | 修改数量。Body: `{ quantity }` |
| DELETE | `/api/cart/{cartItemId}` | 删除单项 |
| DELETE | `/api/cart` | 清空购物车 |
| GET | `/api/orders` | 当前用户订单列表（含明细）。Params: `page`, `size` |
| POST | `/api/orders` | 从购物车创建订单（事务：创建订单+清空购物车） |
| GET | `/api/orders/{id}` | 订单详情（校验归属） |
| POST | `/api/orders/{id}/pay` | 模拟支付 → status 改为 PAID |

### 3.3 管理员接口（需 ADMIN 角色）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/admin/products` | 新增商品 |
| PUT | `/api/admin/products/{id}` | 更新商品 |
| DELETE | `/api/admin/products/{id}` | 删除商品 |
| GET | `/api/admin/orders` | 全部订单。Params: `status`(可选), `page`, `size` |
| PUT | `/api/admin/orders/{id}/status` | 变更订单状态。Body: `{ status }` |
| POST | `/api/admin/categories` | 新增分类。Body: `{ name, slug }` |
| PUT | `/api/admin/categories/{id}` | 更新分类 |
| DELETE | `/api/admin/categories/{id}` | 删除分类 |

---

## 4. 鉴权方案

### 4.1 JWT 流程

```
登录/注册成功 → 后端生成 JWT → 返回 { token } 给前端
前端存 token 到 localStorage
每次请求：axios 拦截器自动在 Header 加 "Authorization: Bearer <token>"
后端 JwtAuthenticationFilter 解析 token → 设置 SecurityContext
Controller 通过 @PreAuthorize 或手动判断角色
```

### 4.2 关键实现

- **JwtTokenUtil**：`io.jsonwebtoken:jjwt:0.9.1`，HS256，7天过期。Payload: `{ sub: userId, email, role }`
- **PasswordUtil**：`BCryptPasswordEncoder`，注册时加密，登录时比对
- **JwtAuthenticationFilter**：继承 `OncePerRequestFilter`，从 Header 提取 token，解析后注入 SecurityContext
- **SecurityConfig**：公开接口放行、其余需要认证、/api/admin/** 需要 ADMIN 角色

### 4.3 前端路由守卫

```javascript
// router/index.js
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.requiresAdmin && userStore.role !== 'ADMIN') {
    next('/')
  } else {
    next()
  }
})
```

---

## 5. 前端路由与页面

| 路由 | 页面组件 | 权限 |
|------|---------|------|
| `/` | Home.vue | 公开 |
| `/products` | ProductList.vue | 公开 |
| `/products/:id` | ProductDetail.vue | 公开 |
| `/login` | Login.vue | 公开（已登录跳首页） |
| `/register` | Register.vue | 公开（已登录跳首页） |
| `/cart` | Cart.vue | 需登录 |
| `/orders` | OrderList.vue | 需登录 |
| `/orders/:id` | OrderDetail.vue | 需登录 |
| `/orders/:id/success` | PaySuccess.vue | 需登录 |
| `/admin` | admin/Dashboard.vue | ADMIN |
| `/admin/products` | admin/ProductManage.vue | ADMIN |
| `/admin/products/new` | admin/ProductForm.vue | ADMIN |
| `/admin/products/:id/edit` | admin/ProductForm.vue | ADMIN |
| `/admin/orders` | admin/OrderManage.vue | ADMIN |
| `/admin/categories` | admin/CategoryManage.vue | ADMIN |

---

## 6. 关键组件

### 后端分层

```
Controller → Service → Mapper (MyBatis-Plus BaseMapper)
     ↓
DTO 入参校验（@Valid / Hibernate Validator）
     ↓
Service 业务逻辑 + 事务（@Transactional）
     ↓
Mapper 数据访问（MyBatis-Plus 自带 CRUD，复杂查询用 @Select 或 XML）
```

### 前端关键交互

- **AppHeader**：根据 `userStore.token` 切换"登录/注册"和"用户菜单"；根据 `userStore.role === 'ADMIN'` 显示"后台管理"入口；购物车角标从 `cartStore.count` 读取
- **ProductList**：URL 参数同步搜索关键词和分类（watch route query → 重新请求）；Element Plus 分页组件
- **Cart**：加减数量实时调 PUT 接口更新后端；勾选结算（亦可全量结算）
- **下单流程**：购物车 → POST /api/orders → 跳转 /orders/:id → 点击"去支付" → POST /api/orders/:id/pay → 跳转 success 页

---

## 7. 技术依赖

### 后端 (pom.xml 关键依赖)

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>
</parent>
<properties>
    <java.version>1.8</java.version>
</properties>

<!-- Spring Boot Starters -->
spring-boot-starter-web
spring-boot-starter-security

<!-- MyBatis-Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.5</version>
</dependency>

<!-- SQLite -->
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.42.0.0</version>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

### 前端 (package.json 关键依赖)

```json
{
  "dependencies": {
    "vue": "^3.4",
    "vue-router": "^4.3",
    "pinia": "^2.1",
    "element-plus": "^2.7",
    "axios": "^1.7",
    "@element-plus/icons-vue": "^2.3"
  },
  "devDependencies": {
    "vite": "^5.4",
    "@vitejs/plugin-vue": "^5.0"
  }
}
```

---

## 8. 实施顺序（分 6 个阶段）

### Phase 1 — 后端骨架 + 数据库
1. 创建 Spring Boot 2.7.18 Maven 项目（spring-boot-starter-web + Lombok）
2. 配置 `application.yml`（SQLite 数据源、MyBatis-Plus、端口 8080）
3. 编写 `schema.sql`（全部 6 张表）→ 启动时自动建表
4. 创建 Entity 类（User、Category、Product、CartItem、Order、OrderItem）
5. 创建 Mapper 接口（继承 `BaseMapper<T>`）
6. 验证：启动项目，确认 SQLite 文件生成 + 表结构正确

### Phase 2 — 认证系统
7. `JwtTokenUtil` + `PasswordUtil`
8. `JwtAuthenticationFilter` + `SecurityConfig`
9. `UserService` + `UserServiceImpl`（注册、登录）
10. `AuthController`（POST register、POST login）
11. `Result` 统一响应类
12. 验证：curl 测试注册/登录，确认返回 token

### Phase 3 — 前端骨架 + 路由 + 登录
13. `npm create vite@latest mini-mall-web -- --template vue`
14. 安装 Element Plus + Vue Router + Pinia + axios
15. 搭建 `App.vue` + `AppHeader.vue` + 路由配置
16. `api/request.js`（axios 拦截器 + token 注入）
17. Login.vue + Register.vue + userStore
18. 前后端联调登录/注册 → 存储 token → Header 状态切换

### Phase 4 — 商品 + 分类
19. 后端：CategoryService + CategoryController + AdminCategoryController
20. 后端：ProductService + ProductController + AdminProductController
21. 前端：Home.vue + ProductList.vue（搜索+分类筛选+分页）
22. 前端：ProductDetail.vue
23. 前端管理：CategoryManage.vue + ProductManage.vue + ProductForm.vue
24. 种子数据：data.sql（1管理员 + 5分类 + 20商品）

### Phase 5 — 购物车
25. 后端：CartService + CartController
26. 前端：cartStore + Cart.vue（CartSummary 结算）
27. AppHeader 购物车角标（从 cartStore 取 count）

### Phase 6 — 订单
28. 后端：OrderService（创建订单事务、支付、状态变更）+ OrderController + AdminOrderController
29. 前端：OrderList.vue + OrderDetail.vue + PaySuccess.vue
30. 前端管理：OrderManage.vue

---

## 9. 待确认事项

1. **商品图片**：建议用占位图 URL（如 `https://picsum.photos/400/400?random=N`），不引入文件上传
2. **SQLite 驱动方言**：MyBatis-Plus 对 SQLite 支持需配置 `spring.datasource.driver-class-name=org.sqlite.JDBC` + 一个简单的 config 设置
3. **前端开发代理**：`vite.config.js` 配 proxy 将 `/api` 转发到 `http://localhost:8080`

---

## 验证方式

- **Phase 1-2**：后端启动 → `curl` 测试注册/登录 API → 获得 token
- **Phase 3**：前后端联调登录 → token 存储 → Header 变化
- **Phase 4**：浏览器逐页验证：首页 → 商品列表（搜索+筛选+分页）→ 商品详情
- **Phase 5**：登录 → 添加购物车 → 修改数量 → 角标变化
- **Phase 6**：下单 → 支付 → 订单列表 → 管理员变更状态
- **最终全流程**：注册 → 浏览 → 搜索 → 加入购物车 → 下单 → 模拟支付 → 查看订单 → 管理员管理商品/分类/订单
