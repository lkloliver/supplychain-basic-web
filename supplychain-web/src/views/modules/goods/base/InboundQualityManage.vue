<template>
  <DocumentManageTemplate
    title="入库质检管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/base/inbound-quality/upload"
    manageRouteName="/dashboard/goods/base/inbound-quality/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchQualities"
    @reset="resetFilters"
    @view="viewQuality"
    @edit="editQuality"
    @delete="deleteQuality"
    @page-change="handlePageChange"
  />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

// 搜索查询
const searchQuery = ref('');
const startDate = ref('');
const endDate = ref('');
const statusFilter = ref('');

// 分页
const currentPage = ref(1);
const totalPages = ref(5);

// 定义表格列
const columns = [
  {
    key: 'qualityNo',
    label: '质检单号'
  },
  {
    key: 'weightNo',
    label: '过磅单号'
  },
  
  {
    key: 'impurity',
    label: '杂质(%)',
    format: (value: number) => `${value}%`
  },
  
  {
    key: 'qualityTime',
    label: '质检时间'
  },
  {
    key: 'inspector',
    label: '质检员'
  },
  {
    key: 'result',
    label: '质检结果',
    format: (value: string) => getResultText(value),
    class: (value: string) => `result-badge ${value}`
  },
  {
    key: 'status',
    label: '状态',
    format: (value: string) => getStatusText(value),
    class: (value: string) => `status-badge ${value}`
  }
];

// 模拟数据
const documents = ref([
  {
    id: 1,
    name: '2023年1月入库质检单',
    type: 'inbound-quality',
    uploadTime: '2023-01-20 14:30',
    qualityNo: 'Q-2023001',
    weightNo: 'W-2023001',
    moisture: 14.5,
    impurity: 1.8,
    protein: 32.5,
    qualityTime: '2023-01-15 10:30',
    inspector: '张三',
    result: 'pass',
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月入库质检单',
    type: 'inbound-quality',
    uploadTime: '2023-02-15 10:45',
    qualityNo: 'Q-2023002',
    weightNo: 'W-2023002',
    moisture: 15.2,
    impurity: 2.1,
    protein: 31.8,
    qualityTime: '2023-02-15 09:30',
    inspector: '李四',
    result: 'conditional',
    status: 'active'
  },
  {
    id: 3,
    name: '2023年3月入库质检单',
    type: 'inbound-quality',
    uploadTime: '2023-03-10 09:30',
    qualityNo: 'Q-2023003',
    weightNo: 'W-2023003',
    moisture: 16.1,
    impurity: 2.5,
    protein: 30.5,
    qualityTime: '2023-03-10 08:30',
    inspector: '王五',
    result: 'fail',
    status: 'completed'
  }
]);

// 获取质检结果文本
const getResultText = (result: string): string => {
  switch (result) {
    case 'pass': return '合格';
    case 'fail': return '不合格';
    case 'conditional': return '有条件通过';
    default: return '未知';
  }
};

// 获取状态文本
const getStatusText = (status: string): string => {
  switch (status) {
    case 'active': return '有效';
    case 'expired': return '已过期';
    case 'completed': return '已完成';
    default: return '未知';
  }
};

// 搜索质检单
const searchQualities = () => {
  console.log('搜索条件:', {
    query: searchQuery.value,
    startDate: startDate.value,
    endDate: endDate.value,
    status: statusFilter.value
  });
};

// 重置筛选条件
const resetFilters = () => {
  searchQuery.value = '';
  startDate.value = '';
  endDate.value = '';
  statusFilter.value = '';
};

// 查看质检单详情
const viewQuality = (quality: any) => {
  console.log('查看质检单:', quality);
};

// 编辑质检单
const editQuality = (quality: any) => {
  console.log('编辑质检单:', quality);
};

// 删除质检单
const deleteQuality = (quality: any) => {
  if (confirm(`确定要删除质检单 ${quality.qualityNo} 吗？`)) {
    console.log('删除质检单:', quality);
  }
};

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  console.log('切换到页面:', page);
};
</script>

<style scoped>
/* 状态标签样式 */
:deep(.status-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.status-badge.active) {
  background-color: #e8f5e9;
  color: #2e7d32;
}

:deep(.status-badge.expired) {
  background-color: #ffebee;
  color: #c62828;
}

:deep(.status-badge.completed) {
  background-color: #e3f2fd;
  color: #1565c0;
}

/* 质检结果标签样式 */
:deep(.result-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.result-badge.pass) {
  background-color: #e8f5e9;
  color: #2e7d32;
}

:deep(.result-badge.fail) {
  background-color: #ffebee;
  color: #c62828;
}

:deep(.result-badge.conditional) {
  background-color: #fff3e0;
  color: #ef6c00;
}
</style>
    
    