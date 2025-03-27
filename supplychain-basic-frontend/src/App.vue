<template>
  <router-view :key="$route.fullPath"></router-view>
</template>

<script lang="ts" setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 在应用启动时初始化用户状态
onMounted(() => {
  userStore.initState()
})
</script>

<style>
/* 全局样式 */
</style>

