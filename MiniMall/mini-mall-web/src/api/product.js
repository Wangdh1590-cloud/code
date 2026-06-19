import request from './request'

export const productAPI = {
  getList(params) {
    return request.get('/products', { params })
  },
  getById(id) {
    return request.get(`/products/${id}`)
  },
  // 管理员
  create(data) {
    return request.post('/admin/products', data)
  },
  update(id, data) {
    return request.put(`/admin/products/${id}`, data)
  },
  delete(id) {
    return request.delete(`/admin/products/${id}`)
  }
}
