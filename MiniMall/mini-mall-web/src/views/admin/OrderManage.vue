<template>
  <div class="order-manage">
    <h1>订单管理</h1>

    <div class="filter-bar">
      <el-radio-group v-model="statusFilter" @change="fetchOrders">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="PENDING">待支付</el-radio-button>
        <el-radio-button label="PAID">已支付</el-radio-button>
        <el-radio-button label="SHIPPED">已发货</el-radio-button>
        <el-radio-button label="CANCELLED">已取消</el-radio-button>
      </el-radio-group>
    </div>

    <el-table :data="orders" v-loading="loading" border>
      <el-table-column prop="id" label="订单号" width="80" />
      <el-table-column label="金额" width="120">
        <template #default="{ row }"><span class="price">{{ formatPrice(row.totalAmount) }}</span></template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" width="180">
        <template #default="{ row }">{{ row.createdAt?.slice(0, 19) }}</template>
      </el-table-column>
      <el-table-column label="商品" min-width="200">
        <template #default="{ row }">
          <span v-for="item in row.items" :key="item.id" style="margin-right:8px;">
            {{ item.productName }}×{{ item.quantity }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-select
            :model-value="row.status"
            size="small"
            placeholder="变更状态"
            @change="(val) => updateStatus(row.id, val)"
            :disabled="row.status === 'CANCELLED'"
          >
            <el-option label="待支付" value="PENDING" />
            <el-option label="已支付" value="PAID" />
            <el-option label="已发货" value="SHIPPED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination" v-if="total > pageSize">
      <el-pagination v-model:current-page="page" :page-size="pageSize" :total="total" layout="prev, pager, next" @current-change="fetchOrders" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { orderAPI } from '../../api/order'
import { ElMessage } from 'element-plus'

const orders = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const statusFilter = ref('')

function formatPrice(p) { return (p / 100).toFixed(2) }
function statusType(s) { return { PENDING: 'warning', PAID: 'success', SHIPPED: 'primary', CANCELLED: 'info' }[s] || 'info' }
function statusLabel(s) { return { PENDING: '待支付', PAID: '已支付', SHIPPED: '已发货', CANCELLED: '已取消' }[s] || s }

async function fetchOrders() {
  loading.value = true
  try {
    const res = await orderAPI.getAll({
      page: page.value,
      size: pageSize.value,
      status: statusFilter.value || undefined
    })
    const data = res.data.data
    orders.value = data.records || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

async function updateStatus(id, status) {
  try {
    await orderAPI.updateStatus(id, status)
    ElMessage.success('状态已更新')
    fetchOrders()
  } catch (e) {}
}

onMounted(fetchOrders)
</script>

<style scoped>
.order-manage h1 { margin-bottom: 20px; }
.filter-bar { margin-bottom: 20px; }
.pagination { display: flex; justify-content: center; margin-top: 20px; }
</style>
