<template>
  <div class="home">
    <div class="hero">
      <h1>欢迎来到 Mini Mall</h1>
      <p>微型电商，轻松购物</p>
    </div>

    <div class="categories-section">
      <h2>商品分类</h2>
      <div class="category-grid">
        <div
          v-for="cat in categories"
          :key="cat.id"
          class="category-card"
          @click="$router.push({ path: '/products', query: { category: cat.slug } })"
        >
          <h3>{{ cat.name }}</h3>
        </div>
      </div>
    </div>

    <div class="products-section">
      <h2>热门商品</h2>
      <div class="product-grid">
        <el-card
          v-for="product in products"
          :key="product.id"
          class="product-card"
          shadow="hover"
          @click="$router.push(`/products/${product.id}`)"
        >
          <img :src="product.imageUrl || 'https://picsum.photos/400/400'" :alt="product.name" />
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p class="price">{{ formatPrice(product.price) }}</p>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { productAPI } from '../api/product'
import { categoryAPI } from '../api/category'

const products = ref([])
const categories = ref([])

function formatPrice(price) {
  return (price / 100).toFixed(2)
}

onMounted(async () => {
  try {
    const [pRes, cRes] = await Promise.all([
      productAPI.getList({ page: 1, size: 8 }),
      categoryAPI.getList()
    ])
    products.value = pRes.data.data.records || []
    categories.value = cRes.data.data || []
  } catch (e) {
    // handled by interceptor
  }
})
</script>

<style scoped>
.hero {
  text-align: center;
  padding: 60px 0 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: #fff;
  margin-bottom: 40px;
}
.hero h1 { font-size: 32px; margin-bottom: 10px; }
.hero p { font-size: 16px; opacity: 0.9; }

h2 { margin-bottom: 20px; font-size: 22px; }

.category-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  margin-bottom: 40px;
}
.category-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #eee;
}
.category-card:hover {
  border-color: #409eff;
  color: #409eff;
  transform: translateY(-2px);
}
.category-card h3 { font-size: 16px; }

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.product-card {
  cursor: pointer;
  transition: transform 0.2s;
}
.product-card:hover { transform: translateY(-4px); }
.product-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}
.product-info { padding-top: 10px; }
.product-info h3 {
  font-size: 14px;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
