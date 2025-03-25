import { defineStore } from "pinia"
import { ref, computed } from "vue"

interface User {
  username?: string
  phone?: string
  version: "base" | "non-base"
  role?: string // 添加角色字段
}

export const useUserStore = defineStore("user", () => {
  const user = ref<User>({
    version: "base",
  })

  const isLoggedIn = computed(() => {
    return !!(user.value.username || user.value.phone)
  })

  // 判断是否为管理员
  const isAdmin = computed(() => {
    return user.value.role === "admin"
  })

  function setUser(userData: User) {
    user.value = userData
    // 可以在这里将用户信息保存到本地存储
    localStorage.setItem("user", JSON.stringify(userData))
  }

  function clearUser() {
    user.value = {
      version: "base",
    }
    // 清除本地存储中的用户信息
    localStorage.removeItem("user")
  }

  // 初始化时从本地存储加载用户信息
  function loadUser() {
    const storedUser = localStorage.getItem("user")
    if (storedUser) {
      try {
        user.value = JSON.parse(storedUser)
      } catch (e) {
        console.error("Failed to parse stored user data", e)
      }
    }
  }

  // 立即加载用户信息
  loadUser()

  return {
    user,
    isLoggedIn,
    isAdmin,
    setUser,
    clearUser,
  }
})

