import request from './request'

export const cartAPI = {
  getCart() {
    return request.get('/cart')
  },
  addItem(data) {
    return request.post('/cart', data)
  },
  updateQuantity(cartItemId, data) {
    return request.put(`/cart/${cartItemId}`, data)
  },
  removeItem(cartItemId) {
    return request.delete(`/cart/${cartItemId}`)
  },
  clearCart() {
    return request.delete('/cart')
  }
}
