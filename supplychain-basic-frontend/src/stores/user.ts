import { defineStore } from "pinia"
import { ref, computed } from "vue"

export type Version = 'BASE' | 'NON_BASE'

export interface UserInfo {
  id: number
  username: string
  phone: string
}

export const useUserStore = defineStore("user", () => {
  // 用户信息
  const userInfo = ref<UserInfo | null>(null)
  // 是否是管理员
  const isAdmin = ref(false)
  // 版本信息
  const version = ref<Version>('BASE')

  // 计算属性：是否已登录
  const isLoggedIn = computed(() => {
    return !!localStorage.getItem('token') && !!userInfo.value
  })

  // 初始化状态
  const initState = () => {
    // 从localStorage获取用户信息
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedUserInfo) {
      userInfo.value = JSON.parse(storedUserInfo)
    }
    
    // 从localStorage获取管理员状态
    const storedIsAdmin = localStorage.getItem('isAdmin')
    if (storedIsAdmin) {
      isAdmin.value = storedIsAdmin === 'true'
    }
    
    // 从localStorage获取版本信息
    const storedVersion = localStorage.getItem('version') as Version
    if (storedVersion && (storedVersion === 'BASE' || storedVersion === 'NON_BASE')) {
      version.value = storedVersion
    }
  }

  // 设置用户信息
  const setUserInfo = (info: UserInfo | null) => {
    userInfo.value = info
    if (info) {
      localStorage.setItem('userInfo', JSON.stringify(info))
    } else {
      localStorage.removeItem('userInfo')
    }
  }

  // 设置管理员状态
  const setIsAdmin = (admin: boolean) => {
    isAdmin.value = admin
    localStorage.setItem('isAdmin', String(admin))
  }

  // 设置版本信息
  const setVersion = (ver: Version) => {
    version.value = ver
    localStorage.setItem('version', ver)
  }

  // 清除所有状态
  const clearState = () => {
    userInfo.value = null
    isAdmin.value = false
    version.value = 'BASE'
    localStorage.removeItem('userInfo')
    localStorage.removeItem('isAdmin')
    localStorage.removeItem('version')
    localStorage.removeItem('token')
  }

  return {
    userInfo,
    isAdmin,
    version,
    isLoggedIn,
    initState,
    setUserInfo,
    setIsAdmin,
    setVersion,
    clearState
  }
})

