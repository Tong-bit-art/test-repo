import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response && error.response.status === 401) {
      // 未授权，跳转到登录页
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 认证相关
export const authApi = {
  login: (data) => api.post('/auth/login', data)
}

// 人员信息相关
export const personnelApi = {
  list: (params) => api.get('/admin/personnel', { params }),
  save: (data) => api.post('/entry/personnel', data),
  delete: (id) => api.delete(`/admin/personnel/${id}`),
  importExcel: (formData) => api.post('/entry/personnel/import', formData),
  exportExcel: (params) => api.get('/admin/personnel/export', { params, responseType: 'blob' }),
  randomSelection: (data) => api.post('/admin/personnel/select', data)
}

// 专业类目相关
export const categoryApi = {
  list: () => api.get('/admin/categories'),
  save: (data) => api.post('/admin/categories', data),
  update: (id, data) => api.put(`/admin/categories/${id}`, data),
  delete: (id) => api.delete(`/admin/categories/${id}`)
}

// 用户管理相关
export const userApi = {
  list: () => api.get('/admin/users'),
  save: (data) => api.post('/admin/users', data),
  delete: (id) => api.delete(`/admin/users/${id}`)
}

// 抽取记录相关
export const selectionApi = {
  list: () => api.get('/admin/selection-records')
}

export default api
