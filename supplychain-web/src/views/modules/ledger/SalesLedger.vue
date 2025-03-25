<template>
    <div class="module-page">
      <div class="module-header">
        <h2 class="module-title">销售台账</h2>
        <div class="module-actions">
          <router-link :to="{ name: 'SalesLedgerUpload' }" class="action-btn upload-btn">
            <UploadCloudIcon class="btn-icon" />
            <span>上传资料</span>
          </router-link>
          <router-link :to="{ name: 'SalesLedgerManage' }" class="action-btn manage-btn">
            <ListIcon class="btn-icon" />
            <span>管理资料</span>
          </router-link>
        </div>
      </div>
      
      <div class="module-content">
        <div class="module-description">
          <p>您当前正在查看 <strong>销售台账</strong> 模块。</p>
          <p>请使用上方的按钮上传或管理资料。</p>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { UploadCloudIcon, ListIcon } from 'lucide-vue-next';
  </script>
  
  <style scoped>
  .module-page {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 1.5rem;
  }
  
  .module-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
  }
  
  .module-title {
    font-size: 1.5rem;
    font-weight: 600;
    margin: 0;
    color: #0a2463;
  }
  
  .module-actions {
    display: flex;
    gap: 1rem;
  }
  
  .action-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.625rem 1rem;
    border: none;
    border-radius: 4px;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.3s;
    text-decoration: none;
  }
  
  .upload-btn {
    background-color: #1e88e5;
    color: white;
  }
  
  .upload-btn:hover {
    background-color: #1976d2;
  }
  
  .manage-btn {
    background-color: #f5f5f5;
    color: #424242;
  }
  
  .manage-btn:hover {
    background-color: #e0e0e0;
  }
  
  .btn-icon {
    width: 16px;
    height: 16px;
  }
  
  .module-content {
    min-height: 300px;
  }
  
  .module-description {
    padding: 2rem;
    text-align: center;
    color: #757575;
    background-color: #f5f7fa;
    border-radius: 8px;
  }
  </style>
  
  