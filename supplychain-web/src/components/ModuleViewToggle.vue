<template>
    <div class="view-toggle">
      <router-link :to="uploadRoute" class="toggle-btn" :class="{ active: isUploadView }">
        <UploadCloudIcon class="btn-icon" />
        <span>资料上传</span>
      </router-link>
      <router-link :to="manageRoute" class="toggle-btn" :class="{ active: isManageView }">
        <ListIcon class="btn-icon" />
        <span>资料管理</span>
      </router-link>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { computed } from 'vue';
  import { useRoute } from 'vue-router';
  import { UploadCloudIcon, ListIcon } from 'lucide-vue-next';
  
  const props = defineProps<{
    uploadRoute: string;
    manageRoute: string;
  }>();
  
  const route = useRoute();
  const isUploadView = computed(() => route.path === props.uploadRoute);
  const isManageView = computed(() => route.path === props.manageRoute);
  </script>
  
  <style scoped>
  .view-toggle {
    display: flex;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    overflow: hidden;
  }
  
  .toggle-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 1rem;
    background-color: #f5f5f5;
    color: #424242;
    text-decoration: none;
    transition: all 0.3s;
    font-size: 0.875rem;
  }
  
  .toggle-btn:first-child {
    border-right: 1px solid #e0e0e0;
  }
  
  .toggle-btn.active {
    background-color: #1e88e5;
    color: white;
  }
  
  .btn-icon {
    width: 16px;
    height: 16px;
  }
  </style>
  
  