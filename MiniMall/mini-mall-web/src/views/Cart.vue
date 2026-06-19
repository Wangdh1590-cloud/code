<template>
  <div class="cart-page">
    <h1>购物车</h1>

    <div v-if="cartStore.items.length === 0 && !cartStore.loading" class="empty">
      <el-empty description="购物车是空的">
        <el-button type="primary" @click="$router.push('/products')">去逛逛</el-button>
      </el-empty>
    </div>

    <div v-else v-loading="cartStore.loading">
      <el-table :data="cartStore.items" style="width:100%">
        <el-table-column label="商品" min-width="300">
          <template #default="{ row }">
            <div class="cart-product" @click="$router.push(`/products/${row.productId}`)">
              <img :src="row.product?.imageUrl || 'https://picsum.photos/80/80'" />
              <span>{{ row.product?.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="120">
          <template #default="{ row }">
            <span class="price">{{ formatPrice(row.product?.price || 0) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="140">
          <template #default="{ row }">
            <el-input-number
              v-model="row.quantity"
              :min="1"
              :max="99"
              size="small"
              @change="(val) => cartStore.updateQuantity(row.id, val)"
            />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="120">
          <template #default="{ row }">
            <span class="price">{{ formatPrice((row.product?.price || 0) * row.quantity) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="{ row }">
            <el-button type="danger" size="small" text @click="cartStore.removeItem(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="cart-footer">
        <div class="cart-summary">
          共 <strong>{{ cartStore.totalCount }}</strong> 件，合计：
          <span class="price" style="font-size:22px;">{{ formatPrice(cartStore.totalPrice) }}</span>
        </div>
        <el-button type="primary" size="large" @click="handleCheckout" :loading="checkingOut">
          去结算
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart'
import { orderAPI } from '../api/order'
import { ElMessage } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()
const checkingOut = ref(false)

function formatPrice(price) {
  return (price / 100).toFixed(2)
}

async function handleCheckout() {
  checkingOut.value = true
  try {
    const res = await orderAPI.create()
    ElMessage.success('下单成功')
    router.push(`/orders/${res.data.data.id}`)
  } catch (e) {
    // handled
  } finally {
    checkingOut.value = false
  }
}

onMounted(() => {
  cartStore.fetchCart()
})
</script>

<style scoped>
.cart-page h1 { margin-bottom: 24px; }
.empty { padding: 60px 0; }
.cart-product { display: flex; align-items: center; gap: 12px; cursor: pointer; }
.cart-product img { width: 60px; height: 60px; object-fit: cover; border-radius: 4px; }
.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
</style>
