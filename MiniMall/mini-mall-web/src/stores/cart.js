import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartAPI } from '../api/cart'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])
  const loading = ref(false)

  const totalCount = computed(() => items.value.reduce((sum, item) => sum + item.quantity, 0))
  const totalPrice = computed(() => items.value.reduce((sum, item) => {
    return sum + (item.product?.price || 0) * item.quantity
  }, 0))

  async function fetchCart() {
    loading.value = true
    try {
      const res = await cartAPI.getCart()
      items.value = res.data.data || []
    } finally {
      loading.value = false
    }
  }

  async function addToCart(productId, quantity = 1) {
    await cartAPI.addItem({ productId, quantity })
    await fetchCart()
  }

  async function updateQuantity(cartItemId, quantity) {
    await cartAPI.updateQuantity(cartItemId, { quantity })
    await fetchCart()
  }

  async function removeItem(cartItemId) {
    await cartAPI.removeItem(cartItemId)
    await fetchCart()
  }

  async function clearCart() {
    await cartAPI.clearCart()
    items.value = []
  }

  return { items, loading, totalCount, totalPrice, fetchCart, addToCart, updateQuantity, removeItem, clearCart }
})
