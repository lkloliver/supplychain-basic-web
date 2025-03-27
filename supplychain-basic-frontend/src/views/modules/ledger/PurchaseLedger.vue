<template>
  <div class="purchase-ledger">
    <!-- 顶部操作栏 -->
    <div class="top-actions">
      <div class="page-title">采购台账</div>
      <div class="action-buttons">
        <button 
          class="action-btn"
          :class="{ active: currentPage === 'upload' }"
          @click="switchPage('upload')"
        >
          台账上传
        </button>
        <button 
          class="action-btn"
          :class="{ active: currentPage === 'manage' }"
          @click="switchPage('manage')"
        >
          台账管理
        </button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <router-view></router-view>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

// 当前页面
const currentPage = computed(() => {
  return route.path.includes('manage') ? 'manage' : 'upload';
});

// 切换页面
const switchPage = (page: string) => {
  if (page === 'upload') {
    router.push('/dashboard/ledger/purchase/upload');
  } else {
    router.push('/dashboard/ledger/purchase/manage');
  }
};
</script>

<style scoped>
.purchase-ledger {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.top-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
}

.page-title {
  font-size: 20px;
  font-weight: 500;
  color: #333;
}

.action-buttons {
  display: flex;
  gap: 15px;
}

.action-btn {
  padding: 8px 16px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background: #fff;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.action-btn:hover {
  color: #1890ff;
  border-color: #1890ff;
}

.action-btn.active {
  background: #1890ff;
  color: #fff;
  border-color: #1890ff;
}

.content-area {
  flex: 1;
  padding: 20px;
  background: #f0f2f5;
  overflow: auto;
}
</style>

