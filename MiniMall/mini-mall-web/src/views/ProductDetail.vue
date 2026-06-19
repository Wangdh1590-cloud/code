<template>
  <div class="product-detail" v-loading="loading">
    <el-card v-if="product">
      <div class="detail-grid">
        <img :src="product.imageUrl || 'https://picsum.photos/400/400'" :alt="product.name" class="detail-img" />
        <div class="detail-info">
          <h1>{{ product.name }}</h1>
          <p class="category-tag">
            <el-tag size="small">{{ product.category?.name }}</el-tag>
          </p>
          <p class="price-large">{{ formatPrice(product.price) }}</p>
          <p class="stock">库存：{{ product.stock }}</p>
          <p class="desc">{{ product.description }}</p>
          <div class="actions">
            <el-input-number v-model="quantity" :min="1" :max="product.stock" size="large" />
            <el-button type="primary" size="large" @click="handleAddToCart" :loading="adding">
              <el-icon><ShoppingCart /></el-icon>
              加入购物车
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productAPI } from '../api/product'
import { useCartStore } from '../stores/cart'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const product = ref(null)
const loading = ref(false)
const adding = ref(false)
const quantity = ref(1)

function formatPrice(price) {
  return (price / 100).toFixed(2)
}

async function handleAddToCart() {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  adding.value = true
  try {
    await cartStore.addToCart(product.value.id, quantity.value)
    ElMessage.success('已加入购物车')
  } finally {
    adding.value = false
  }
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await productAPI.getById(route.params.id)
    product.value = res.data.data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.product-detail { max-width: 900px; margin: 0 auto; }
.detail-grid { display: flex; gap: 40px; }
.detail-img { width: 400px; height: 400px; object-fit: cover; border-radius: 8px; }
.detail-info { flex: 1; }
.detail-info h1 { font-size: 24px; margin-bottom: 12px; }
.category-tag { margin-bottom: 12px; }
.price-large { font-size: 28px; color: #e4393c; margin-bottom: 12px; }
.price-large::before { content: '¥'; font-size: 0.7em; }
.stock { font-size: 14px; color: #999; margin-bottom: 12px; }
.desc { color: #666; line-height: 1.8; margin-bottom: 24px; }
.actions { display: flex; gap: 12px; align-items: center; }

@media (max-width: 768px) {
  .detail-grid { flex-direction: column; }
  .detail-img { width: 100%; height: 300px; }
}
</style>
