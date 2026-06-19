<template>
  <header class="app-header">
    <div class="header-inner">
      <router-link to="/" class="logo">Mini Mall</router-link>

      <div class="search-bar">
        <el-input
          v-model="keyword"
          placeholder="搜索商品..."
          size="large"
          @keyup.enter="doSearch"
        >
          <template #append>
            <el-button @click="doSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>

      <div class="header-actions">
        <router-link to="/cart" class="cart-link">
          <el-badge :value="cartCount" :hidden="cartCount === 0" :max="99">
            <el-icon :size="22"><ShoppingCart /></el-icon>
          </el-badge>
          <span>购物车</span>
        </router-link>

        <template v-if="userStore.isLoggedIn">
          <el-dropdown trigger="click">
            <span class="user-dropdown">
              <el-icon><User /></el-icon>
              {{ userStore.userInfo?.name }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="$router.push('/orders')">我的订单</el-dropdown-item>
                <el-dropdown-item v-if="userStore.isAdmin" @click="$router.push('/admin')">后台管理</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>

        <template v-else>
          <router-link to="/login" class="btn-login">登录</router-link>
          <router-link to="/register" class="btn-register">注册</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { useCartStore } from '../../stores/cart'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const keyword = ref('')
const cartCount = ref(0)

function doSearch() {
  if (keyword.value.trim()) {
    router.push({ path: '/products', query: { search: keyword.value.trim() } })
  }
}

async function handleLogout() {
  userStore.logout()
  cartCount.value = 0
  router.push('/')
}

async function refreshCartCount() {
  if (userStore.isLoggedIn) {
    await cartStore.fetchCart()
    cartCount.value = cartStore.totalCount
  } else {
    cartCount.value = 0
  }
}

onMounted(refreshCartCount)
watch(() => userStore.isLoggedIn, refreshCartCount)
</script>

<style scoped>
.app-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 60px;
  padding: 0 20px;
  gap: 20px;
}
.logo {
  font-size: 22px;
  font-weight: bold;
  color: #409eff;
  white-space: nowrap;
}
.search-bar {
  flex: 1;
  max-width: 500px;
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  white-space: nowrap;
}
.cart-link {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}
.cart-link:hover { color: #409eff; }
.user-dropdown {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #333;
}
.btn-login, .btn-register {
  font-size: 14px;
  padding: 6px 14px;
  border-radius: 4px;
}
.btn-login { color: #409eff; }
.btn-register {
  background: #409eff;
  color: #fff;
}
.btn-register:hover { background: #337ecc; }
</style>
