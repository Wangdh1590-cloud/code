import request from './request'

export const categoryAPI = {
  getList() {
    return request.get('/categories')
  },
  // 管理员
  getAdminList() {
    return request.get('/admin/categories')
  },
  create(data) {
    return request.post('/admin/categories', data)
  },
  update(id, data) {
    return request.put(`/admin/categories/${id}`, data)
  },
  delete(id) {
    return request.delete(`/admin/categories/${id}`)
  }
}
