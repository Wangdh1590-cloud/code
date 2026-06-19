<template>
  <div class="product-list-page">
    <div class="filters">
      <div class="category-tabs">
        <el-button
          :type="!currentCategory ? 'primary' : ''"
          @click="selectCategory('')"
          size="small"
        >全部</el-button>
        <el-button
          v-for="cat in categories"
          :key="cat.id"
          :type="currentCategory === cat.slug ? 'primary' : ''"
          @click="selectCategory(cat.slug)"
          size="small"
        >{{ cat.name }}</el-button>
      </div>

      <el-input
        v-model="searchText"
        placeholder="搜索商品..."
        size="large"
        clearable
        @input="onSearch"
        style="max-width: 300px;"
      >
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
    </div>

    <div class="product-grid" v-loading="loading">
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
          <p class="desc">{{ product.description?.slice(0, 50) }}</p>
          <p class="price">{{ formatPrice(product.price) }}</p>
        </div>
      </el-card>
    </div>

    <div v-if="!loading && products.length === 0" style="text-align:center;padding:60px;color:#999;">
      <p>没有找到相关商品</p>
    </div>

    <div class="pagination" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="fetchProducts"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productAPI } from '../api/product'
import { categoryAPI } from '../api/category'

const route = useRoute()
const router = useRouter()
const products = ref([])
const categories = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)
const currentCategory = ref('')
const searchText = ref('')

function formatPrice(price) {
  return (price / 100).toFixed(2)
}

let searchTimer = null
function onSearch() {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    page.value = 1
    router.replace({ query: { ...route.query, search: searchText.value || undefined, page: 1 } })
    fetchProducts()
  }, 400)
}

function selectCategory(slug) {
  currentCategory.value = slug
  page.value = 1
  router.replace({ query: { ...route.query, category: slug || undefined, page: 1 } })
  fetchProducts()
}

async function fetchProducts() {
  loading.value = true
  try {
    const res = await productAPI.getList({
      page: page.value,
      size: pageSize.value,
      search: searchText.value || undefined,
      category: currentCategory.value || undefined
    })
    const data = res.data.data
    products.value = data.records || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  currentCategory.value = route.query.category || ''
  searchText.value = route.query.search || ''
  page.value = Number(route.query.page) || 1
  const cRes = await categoryAPI.getList()
  categories.value = cRes.data.data || []
  fetchProducts()
})
</script>

<style scoped>
.filters {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}
.category-tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
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
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.desc { font-size: 12px; color: #999; margin-bottom: 6px; }
.pagination { display: flex; justify-content: center; margin-top: 30px; }

@media (max-width: 900px) {
  .product-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
