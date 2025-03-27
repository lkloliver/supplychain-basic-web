<template>
  <div class="ledger-container">
    <!-- 顶部操作栏 -->
    <div class="ledger-top-actions">
      <div class="ledger-page-title">销售台账</div>
      <div class="ledger-action-buttons">
        <button 
          class="ledger-action-btn"
          :class="{ active: currentPage === 'upload' }"
          @click="switchPage('upload')"
        >
          台账上传
        </button>
        <button 
          class="ledger-action-btn"
          :class="{ active: currentPage === 'manage' }"
          @click="switchPage('manage')"
        >
          台账管理
        </button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="ledger-content-area">
      <router-view></router-view>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
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
    router.push('/dashboard/ledger/sales/upload');
  } else {
    router.push('/dashboard/ledger/sales/manage');
  }
};
</script>

