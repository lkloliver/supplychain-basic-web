<template>
  <DocumentManageTemplate
    title="入库过磅管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/base/inbound-weight/upload"
    manageRouteName="/dashboard/goods/base/inbound-weight/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchWeights"
    @reset="resetFilters"
    @view="viewWeight"
    @edit="editWeight"
    @delete="deleteWeight"
    @page-change="handlePageChange"
  />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

// 卡片主要显示列
const cardMainColumns = [
  {
    key: 'weightNo',
    label: '计量号'
  },
  {
    key: 'name', 
    label: '过磅单名称'
  }
];



// 搜索查询
const searchQuery = ref('');
const startDate = ref('');
const endDate = ref('');
const statusFilter = ref('');

// 分页
const currentPage = ref(1);
const totalPages = ref(5);



// 模拟数据
const documents = ref([
  {
    id: 1,
    name: '2023年1月入库过磅单',
    type: 'inbound-weight',
    uploadTime: '2023-01-20 14:30',
    weightNo: 'W-2023001',
    vehicleNo: '京A12345',
    grossWeight: 35000,
    tareWeight: 5000,
    netWeight: 30000,
    weightTime: '2023-01-15 10:30',
    operator: '张三',
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月入库过磅单',
    type: 'inbound-weight',
    uploadTime: '2023-02-15 10:45',
    weightNo: 'W-2023002',
    vehicleNo: '京B12345',
    grossWeight: 40000,
    tareWeight: 6000,
    netWeight: 34000,
    weightTime: '2023-02-15 09:30',
    operator: '李四',
    status: 'active'
  },
  {
    id: 3,
    name: '2023年3月入库过磅单',
    type: 'inbound-weight',
    uploadTime: '2023-03-10 09:30',
    weightNo: 'W-2023003',
    vehicleNo: '京C12345',
    grossWeight: 38000,
    tareWeight: 5500,
    netWeight: 32500,
    weightTime: '2023-03-10 08:30',
    operator: '王五',
    status: 'completed'
  }
]);

// 搜索过磅单
const searchWeights = () => {
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

// 查看过磅单详情
const viewWeight = (weight: any) => {
  console.log('查看过磅单:', weight);
};

// 编辑过磅单
const editWeight = (weight: any) => {
  console.log('编辑过磅单:', weight);
};

// 删除过磅单
const deleteWeight = (weight: any) => {
  if (confirm(`确定要删除过磅单 ${weight.weightNo} 吗？`)) {
    console.log('删除过磅单:', weight);
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
</style>
  
  