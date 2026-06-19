import request from './request'

export const orderAPI = {
  // 用户
  getMyOrders(params) {
    return request.get('/orders', { params })
  },
  create() {
    return request.post('/orders')
  },
  getById(id) {
    return request.get(`/orders/${id}`)
  },
  pay(id) {
    return request.post(`/orders/${id}/pay`)
  },
  // 管理员
  getAll(params) {
    return request.get('/admin/orders', { params })
  },
  updateStatus(id, status) {
    return request.put(`/admin/orders/${id}/status`, { status })
  }
}
