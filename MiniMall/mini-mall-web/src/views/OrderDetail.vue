<template>
  <div class="order-detail" v-loading="loading">
    <h1>订单详情 #{{ order?.id }}</h1>
    <el-card v-if="order">
      <div class="detail-header">
        <span>状态：<el-tag :type="statusType(order.status)">{{ statusLabel(order.status) }}</el-tag></span>
        <span>下单时间：{{ order.createdAt?.slice(0, 19) }}</span>
      </div>

      <el-table :data="order.items" style="width:100%;margin:20px 0;">
        <el-table-column label="商品" prop="productName" />
        <el-table-column label="单价" width="120">
          <template #default="{ row }"><span class="price">{{ formatPrice(row.productPrice) }}</span></template>
        </el-table-column>
        <el-table-column label="数量" width="80" prop="quantity" />
        <el-table-column label="小计" width="120">
          <template #default="{ row }"><span class="price">{{ formatPrice(row.productPrice * row.quantity) }}</span></template>
        </el-table-column>
      </el-table>

      <div class="detail-footer">
        <span class="total">合计：<strong class="price" style="font-size:22px;">{{ formatPrice(order.totalAmount) }}</strong></span>
        <div>
          <el-button v-if="order.status === 'PENDING'" type="primary" size="large" @click="handlePay" :loading="paying">模拟支付</el-button>
          <el-button v-if="order.status === 'PAID'" @click="$router.push(`/orders/${order.id}/success`)">查看支付结果</el-button>
          <el-button @click="$router.push('/orders')">返回列表</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderAPI } from '../api/order'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)
const paying = ref(false)

function formatPrice(p) { return (p / 100).toFixed(2) }
function statusType(s) { return { PENDING: 'warning', PAID: 'success', SHIPPED: 'primary', CANCELLED: 'info' }[s] || 'info' }
function statusLabel(s) { return { PENDING: '待支付', PAID: '已支付', SHIPPED: '已发货', CANCELLED: '已取消' }[s] || s }

async function handlePay() {
  paying.value = true
  try {
    await orderAPI.pay(order.value.id)
    ElMessage.success('支付成功')
    router.push(`/orders/${order.value.id}/success`)
  } finally {
    paying.value = false
  }
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await orderAPI.getById(route.params.id)
    order.value = res.data.data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.order-detail { max-width: 800px; margin: 0 auto; }
.order-detail h1 { margin-bottom: 24px; }
.detail-header { display: flex; justify-content: space-between; align-items: center; color: #999; font-size: 14px; }
.detail-footer { display: flex; justify-content: space-between; align-items: center; padding-top: 20px; border-top: 1px solid #eee; }
</style>
