import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authAPI } from '../api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 'ADMIN')

  async function login(email, password) {
    const res = await authAPI.login({ email, password })
    const user = res.data.data
    token.value = user.token
    userInfo.value = { id: user.id, email: user.email, name: user.name, role: user.role }
    localStorage.setItem('token', user.token)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    return user
  }

  async function register(email, password, name) {
    const res = await authAPI.register({ email, password, name })
    const user = res.data.data
    token.value = user.token
    userInfo.value = { id: user.id, email: user.email, name: user.name, role: user.role }
    localStorage.setItem('token', user.token)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    return user
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return { token, userInfo, isLoggedIn, isAdmin, login, register, logout }
})
