<template>
  <div class="order-page">
    <h1>我的订单</h1>
    <div v-loading="loading">
      <div v-if="orders.length === 0 && !loading" class="empty">
        <el-empty description="暂无订单" />
      </div>
      <div v-else>
        <el-card v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-header">
            <span>订单号：#{{ order.id }}</span>
            <el-tag :type="statusType(order.status)">{{ statusLabel(order.status) }}</el-tag>
            <span class="order-time">{{ order.createdAt?.slice(0, 10) }}</span>
          </div>
          <div class="order-items">
            <div v-for="item in order.items" :key="item.id" class="order-item">
              <span>{{ item.productName }} × {{ item.quantity }}</span>
              <span class="price">{{ formatPrice(item.productPrice * item.quantity) }}</span>
            </div>
          </div>
          <div class="order-footer">
            <span>合计：<strong class="price">{{ formatPrice(order.totalAmount) }}</strong></span>
            <div>
              <el-button v-if="order.status === 'PENDING'" type="primary" size="small" @click="goPay(order.id)">去支付</el-button>
              <el-button size="small" @click="$router.push(`/orders/${order.id}`)">查看详情</el-button>
            </div>
          </div>
        </el-card>
        <div class="pagination" v-if="total > pageSize">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="fetchOrders"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { orderAPI } from '../api/order'
import { ElMessage } from 'element-plus'

const router = useRouter()
const orders = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

function formatPrice(p) { return (p / 100).toFixed(2) }
function statusType(s) { return { PENDING: 'warning', PAID: 'success', SHIPPED: 'primary', CANCELLED: 'info' }[s] || 'info' }
function statusLabel(s) { return { PENDING: '待支付', PAID: '已支付', SHIPPED: '已发货', CANCELLED: '已取消' }[s] || s }

async function goPay(id) {
  try {
    await orderAPI.pay(id)
    ElMessage.success('支付成功')
    fetchOrders()
  } catch (e) {}
}

async function fetchOrders() {
  loading.value = true
  try {
    const res = await orderAPI.getMyOrders({ page: page.value, size: pageSize.value })
    const data = res.data.data
    orders.value = data.records || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(fetchOrders)
</script>

<style scoped>
.order-page h1 { margin-bottom: 24px; }
.order-card { margin-bottom: 16px; }
.order-header { display: flex; align-items: center; gap: 16px; margin-bottom: 12px; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0; }
.order-time { color: #999; font-size: 13px; margin-left: auto; }
.order-item {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 14px;
  color: #666;
}
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}
.pagination { display: flex; justify-content: center; margin-top: 24px; }
</style>
