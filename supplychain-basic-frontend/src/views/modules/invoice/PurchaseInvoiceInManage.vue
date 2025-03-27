<template>
  <DocumentManageTemplate
    title="采购进项发票管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/invoice/purchase-in/upload"
    manageRouteName="/dashboard/invoice/purchase-in/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchInvoices"
    @reset="resetFilters"
    @view="viewInvoice"
    @edit="editInvoice"
    @delete="deleteInvoice"
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
    key: 'invoiceNo',
    label: '发票编号'
  },
  {
    key: 'supplier',
    label: '供应商'
  },
  {
    key: 'contractNo',
    label: '合同编号'
  },
  {
    key: 'amount',
    label: '发票金额',
    format: (value: number) => `¥${value.toFixed(2)}`
  },
  {
    key: 'taxRate',
    label: '税率'
  },
  {
    key: 'taxAmount',
    label: '税额',
    format: (value: number) => `¥${value.toFixed(2)}`
  },
  {
    key: 'invoiceDate',
    label: '开票日期'
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
    name: '2023年1月采购进项发票',
    type: 'purchase-invoice-in',
    uploadTime: '2023-01-20 14:30',
    invoiceNo: 'PI-2023001',
    supplier: '北京供应有限公司',
    contractNo: 'PC-2023001',
    amount: 25000,
    taxRate: '13%',
    taxAmount: 3250,
    invoiceDate: '2023-01-15',
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月采购进项发票',
    type: 'purchase-invoice-in',
    uploadTime: '2023-02-15 10:45',
    invoiceNo: 'PI-2023002',
    supplier: '河南供应集团',
    contractNo: 'PC-2023002',
    amount: 27000,
    taxRate: '13%',
    taxAmount: 3510,
    invoiceDate: '2023-02-20',
    status: 'active'
  },
  {
    id: 3,
    name: '2023年3月采购进项发票',
    type: 'purchase-invoice-in',
    uploadTime: '2023-03-10 09:30',
    invoiceNo: 'PI-2023003',
    supplier: '山东供应有限公司',
    contractNo: 'PC-2023003',
    amount: 33600,
    taxRate: '13%',
    taxAmount: 4368,
    invoiceDate: '2023-03-10',
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

// 搜索发票
const searchInvoices = () => {
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

// 查看发票详情
const viewInvoice = (invoice: any) => {
  console.log('查看发票:', invoice);
};

// 编辑发票
const editInvoice = (invoice: any) => {
  console.log('编辑发票:', invoice);
};

// 删除发票
const deleteInvoice = (invoice: any) => {
  if (confirm(`确定要删除发票 ${invoice.invoiceNo} 吗？`)) {
    console.log('删除发票:', invoice);
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
