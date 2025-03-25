import { createRouter, createWebHistory } from "vue-router"
import { useUserStore } from "@/stores/user"

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/auth/LoginRegister.vue"),
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: () => import("@/views/dashboard/DashboardLayout.vue"),
    meta: { requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 导航守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // 检查路由是否需要认证
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    // 如果需要认证但用户未登录，重定向到登录页面
    if (!userStore.isLoggedIn) {
      next({ name: "Login" })
    } else {
      next()
    }
  } else {
    // 如果用户已登录且尝试访问登录页面，重定向到仪表盘
    if (userStore.isLoggedIn && to.name === "Login") {
      next({ name: "Dashboard" })
    } else {
      next()
    }
  }
})

export default router

