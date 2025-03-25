<template>
    <div class="user-center">
      <h1 class="page-title">个人中心</h1>
      
      <!-- 导航标签 -->
      <div class="nav-tabs">
        <router-link 
          v-for="tab in tabs" 
          :key="tab.path" 
          :to="tab.path"
          custom
          v-slot="{ navigate, isActive }"
        >
          <div 
            class="tab" 
            :class="{ active: isActive }"
            @click="navigate"
          >
            <component :is="tab.icon" class="tab-icon" />
            <span>{{ tab.name }}</span>
          </div>
        </router-link>
      </div>
      
      <!-- 内容区域 -->
      <div class="content-area">
        <router-view></router-view>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { computed } from 'vue';
  import { useRoute } from 'vue-router';
  import { KeyIcon, UsersIcon, ShieldIcon } from 'lucide-vue-next';
  
  const route = useRoute();
  
  const tabs = [
    { 
      name: '区块链账户', 
      path: '/dashboard/user-center/blockchain-account',
      icon: KeyIcon
    },
    { 
      name: '用户角色与权限', 
      path: '/dashboard/user-center/role-permission',
      icon: UsersIcon
    },
    { 
      name: '存证主体认证', 
      path: '/dashboard/user-center/authentication',
      icon: ShieldIcon
    }
  ];
  </script>
  
  <style scoped>
  .user-center {
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  
  .page-title {
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 24px;
    color: #333;
  }
  
  .nav-tabs {
    display: flex;
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 24px;
  }
  
  .tab {
    display: flex;
    align-items: center;
    padding: 12px 20px;
    cursor: pointer;
    border-bottom: 2px solid transparent;
    transition: all 0.3s;
    color: #666;
  }
  
  .tab:hover {
    color: #1890ff;
  }
  
  .tab.active {
    color: #1890ff;
    border-bottom-color: #1890ff;
  }
  
  .tab-icon {
    width: 18px;
    height: 18px;
    margin-right: 8px;
  }
  
  .content-area {
    min-height: 400px;
  }
  </style>
  
  