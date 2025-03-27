import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request = axios.create({
  baseURL: '/api', // 使用相对路径，会被Vite代理
  timeout: 30000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 不需要token的白名单接口
const whiteList = ['/auth/login', '/register', '/sms/code']

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 直接返回响应数据
    return response
  },
  error => {
    console.error('响应错误:', error)
    // 处理错误响应
    if (error.response) {
      // 服务器返回了错误响应
      const message = error.response.data?.message || '服务器错误'
      ElMessage.error(message)
    } else if (error.request) {
      // 请求已发出但没有收到响应
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      // 请求配置出错
      ElMessage.error('请求配置错误')
    }
    return Promise.reject(error)
  }
)

export default request 