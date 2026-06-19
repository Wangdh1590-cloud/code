<template>
  <div class="product-manage">
    <div class="page-header">
      <h1>商品管理</h1>
      <el-button type="primary" @click="$router.push('/admin/products/new')">新增商品</el-button>
    </div>

    <el-table :data="products" v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="图片" width="80">
        <template #default="{ row }">
          <img :src="row.imageUrl || 'https://picsum.photos/40/40'" style="width:40px;height:40px;object-fit:cover;" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" />
      <el-table-column label="价格" width="100">
        <template #default="{ row }"><span class="price">{{ formatPrice(row.price) }}</span></template>
      </el-table-column>
      <el-table-column label="分类" width="100">
        <template #default="{ row }">{{ row.category?.name }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">{{ row.isActive ? '上架' : '下架' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/admin/products/${row.id}/edit`)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination" v-if="total > pageSize">
      <el-pagination v-model:current-page="page" :page-size="pageSize" :total="total" layout="prev, pager, next" @current-change="fetchProducts" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { productAPI } from '../../api/product'

const products = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)

function formatPrice(p) { return (p / 100).toFixed(2) }

async function fetchProducts() {
  loading.value = true
  try {
    const res = await productAPI.getAdminList({ page: page.value, size: pageSize.value })
    const data = res.data.data
    products.value = data.records || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

async function handleDelete(id) {
  try {
    await productAPI.delete(id)
    fetchProducts()
  } catch (e) {}
}

onMounted(fetchProducts)
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h1 { font-size: 20px; }
.pagination { display: flex; justify-content: center; margin-top: 20px; }
</style>
