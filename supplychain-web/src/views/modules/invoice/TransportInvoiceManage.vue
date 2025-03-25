<template>
  <DocumentManageTemplate
    title="运输发票管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/invoice/transport/upload"
    manageRouteName="/dashboard/invoice/transport/manage"
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
    key: 'transporter',
    label: '运输商'
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
    name: '2023年1月运输发票',
    type: 'transport-invoice',
    uploadTime: '2023-01-20 14:30',
    invoiceNo: 'TI-2023001',
    transporter: '北京物流有限公司',
    contractNo: 'TC-2023001',
    amount: 2500,
    taxRate: '9%',
    taxAmount: 225,
    invoiceDate: '2023-01-15',
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月运输发票',
    type: 'transport-invoice',
    uploadTime: '2023-02-15 10:45',
    invoiceNo: 'TI-2023002',
    transporter: '河南运输集团',
    contractNo: 'TC-2023002',
    amount: 2700,
    taxRate: '9%',
    taxAmount: 243,
    invoiceDate: '2023-02-20',
    status: 'active'
  },
  {
    id: 3,
    name: '2023年3月运输发票',
    type: 'transport-invoice',
    uploadTime: '2023-03-10 09:30',
    invoiceNo: 'TI-2023003',
    transporter: '山东物流发展有限公司',
    contractNo: 'TC-2023003',
    amount: 3360,
    taxRate: '9%',
    taxAmount: 302.4,
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
  
  