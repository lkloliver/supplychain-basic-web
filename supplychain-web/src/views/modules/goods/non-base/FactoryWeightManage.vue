<template>
  <DocumentManageTemplate
    title="到货厂家过磅管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/factory-weight/upload"
    manageRouteName="/dashboard/goods/non-base/factory-weight/manage"
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
    key: 'weightNo',
    label: '过磅单号'
  },
  {
    key: 'contractNo',
    label: '合同编号'
  },
  {
    key: 'waybillNo',
    label: '运单编号'
  },
  {
    key: 'vehicleNo',
    label: '车牌号'
  },
  {
    key: 'factory',
    label: '厂家名称'
  },
  {
    key: 'grossWeight',
    label: '毛重(kg)',
    format: (value: number) => `${value.toLocaleString()}`
  },
  {
    key: 'tareWeight',
    label: '皮重(kg)',
    format: (value: number) => `${value.toLocaleString()}`
  },
  {
    key: 'netWeight',
    label: '净重(kg)',
    format: (value: number) => `${value.toLocaleString()}`
  },
  {
    key: 'weightTime',
    label: '过磅时间'
  },
  {
    key: 'weightType',
    label: '过磅类型',
    format: (value: string) => getWeightTypeText(value)
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
    name: '2023年1月厂家过磅单',
    type: 'factory-weight',
    uploadTime: '2023-01-20 14:30',
    weightNo: 'FW-2023001',
    contractNo: 'SC-2023001',
    waybillNo: 'SW-2023001',
    vehicleNo: '京A12345',
    factory: '北京钢铁厂',
    grossWeight: 35000,
    tareWeight: 5000,
    netWeight: 30000,
    weightTime: '2023-01-15 10:30',
    operator: '张三',
    weightType: 'first',
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月厂家过磅单',
    type: 'factory-weight',
    uploadTime: '2023-02-15 10:45',
    weightNo: 'FW-2023002',
    contractNo: 'SC-2023002',
    waybillNo: 'SW-2023002',
    vehicleNo: '京B12345',
    factory: '天津钢铁厂',
    grossWeight: 40000,
    tareWeight: 6000,
    netWeight: 34000,
    weightTime: '2023-02-15 09:30',
    operator: '李四',
    weightType: 'second',
    status: 'active'
  }
]);

// 获取过磅类型文本
const getWeightTypeText = (type: string): string => {
  switch (type) {
    case 'first': return '第一次过磅';
    case 'second': return '第二次过磅';
    case 'once': return '一次过磅';
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
  
  