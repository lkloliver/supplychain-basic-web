<template>
  <DocumentManageTemplate
    title="运输合同管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/transport/upload"
    manageRouteName="/dashboard/contract/transport/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchContracts"
    @reset="resetFilters"
    @view="viewContract"
    @edit="editContract"
    @delete="deleteContract"
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
    key: 'number',
    label: '合同编号'
  },
  {
    key: 'date',
    label: '签订日期'
  },
  {
    key: 'transporter',
    label: '运输商'
  },
  {
    key: 'route',
    label: '路线'
  },
  {
    key: 'quantity',
    label: '运输量'
  },
  {
    key: 'unitPrice',
    label: '单价',
    format: (value: number) => `¥${value.toFixed(2)}`
  },
  {
    key: 'totalAmount',
    label: '总金额',
    format: (value: number) => `¥${value.toFixed(2)}`
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
    name: '2023年1月运输合同',
    type: 'transport-contract',
    uploadTime: '2023-01-20 14:30',
    number: 'TC-2023001',
    date: '2023-01-15',
    transporter: '北京物流有限公司',
    route: '北京-上海',
    quantity: 1000,
    unitPrice: 2.5,
    totalAmount: 2500,
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月运输合同',
    type: 'transport-contract',
    uploadTime: '2023-02-15 10:45',
    number: 'TC-2023002',
    date: '2023-02-20',
    transporter: '河南运输集团',
    route: '郑州-广州',
    quantity: 1500,
    unitPrice: 1.8,
    totalAmount: 2700,
    status: 'active'
  },
  {
    id: 3,
    name: '2023年3月运输合同',
    type: 'transport-contract',
    uploadTime: '2023-03-10 09:30',
    number: 'TC-2023003',
    date: '2023-03-10',
    transporter: '山东物流发展有限公司',
    route: '济南-武汉',
    quantity: 800,
    unitPrice: 4.2,
    totalAmount: 3360,
    status: 'completed'
  }
]);

// 获取状态文本
const getStatusText = (status: string): string => {
  switch (status) {
    case 'active': return '有效';
    case 'expired': return '已过期';
    case 'completed': return '已完成';
    default: return '未知';
  }
};

// 搜索合同
const searchContracts = () => {
  // 实际应用中这里会调用API进行搜索
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

// 查看合同详情
const viewContract = (contract: any) => {
  console.log('查看合同:', contract);
};

// 编辑合同
const editContract = (contract: any) => {
  console.log('编辑合同:', contract);
};

// 删除合同
const deleteContract = (contract: any) => {
  if (confirm(`确定要删除合同 ${contract.number} 吗？`)) {
    console.log('删除合同:', contract);
  }
};

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  // 实际应用中这里会加载对应页的数据
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

