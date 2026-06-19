<template>
  <div class="dashboard">
    <h1>仪表盘</h1>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card><h3>商品数量</h3><p class="num">{{ stats.products }}</p></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><h3>订单数量</h3><p class="num">{{ stats.orders }}</p></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><h3>分类数量</h3><p class="num">{{ stats.categories }}</p></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><h3>用户数量</h3><p class="num">{{ stats.users }}</p></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { productAPI } from '../../api/product'
import { categoryAPI } from '../../api/category'
import { orderAPI } from '../../api/order'

const stats = ref({ products: 0, orders: 0, categories: 0, users: 0 })

onMounted(async () => {
  try {
    const [pRes, cRes, oRes] = await Promise.all([
      productAPI.getList({ page: 1, size: 1 }),
      categoryAPI.getList(),
      orderAPI.getAll({ page: 1, size: 1 })
    ])
    stats.value.products = pRes.data.data?.total || 0
    stats.value.categories = (cRes.data.data || []).length
    stats.value.orders = oRes.data.data?.total || 0
  } catch (e) {}
})
</script>

<style scoped>
.dashboard h1 { margin-bottom: 24px; }
.num { font-size: 32px; color: #409eff; font-weight: bold; margin-top: 10px; }
</style>
