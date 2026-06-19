import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/products',
    name: 'ProductList',
    component: () => import('../views/ProductList.vue')
  },
  {
    path: '/products/:id',
    name: 'ProductDetail',
    component: () => import('../views/ProductDetail.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guest: true }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/Cart.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'OrderList',
    component: () => import('../views/OrderList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/orders/:id',
    name: 'OrderDetail',
    component: () => import('../views/OrderDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/orders/:id/success',
    name: 'PaySuccess',
    component: () => import('../views/PaySuccess.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    redirect: '/admin/dashboard',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requiresAdmin: true },
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'products', name: 'AdminProducts', component: () => import('../views/admin/ProductManage.vue') },
      { path: 'products/new', name: 'ProductNew', component: () => import('../views/admin/ProductForm.vue') },
      { path: 'products/:id/edit', name: 'ProductEdit', component: () => import('../views/admin/ProductForm.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrderManage.vue') },
      { path: 'categories', name: 'AdminCategories', component: () => import('../views/admin/CategoryManage.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null')

  // 需要登录
  if (to.matched.some(r => r.meta.requiresAuth)) {
    if (!token) {
      next('/login')
      return
    }
  }

  // 需要管理员
  if (to.matched.some(r => r.meta.requiresAdmin)) {
    if (!token) {
      next('/login')
      return
    }
    if (userInfo?.role !== 'ADMIN') {
      next('/')
      return
    }
  }

  // 已登录用户访问登录/注册页
  if (to.matched.some(r => r.meta.guest) && token) {
    next('/')
    return
  }

  next()
})

export default router
