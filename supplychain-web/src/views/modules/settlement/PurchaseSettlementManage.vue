<template>
  <DocumentManageTemplate
    title="采购结算管理"
    :columns="columns"
    :documents="documents"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/settlement/purchase/upload"
    manageRouteName="/dashboard/settlement/purchase/manage"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchSettlements"
    @reset="resetFilters"
    @view="viewSettlement"
    @edit="editSettlement"
    @delete="deleteSettlement"
    @page-change="handlePageChange"
  />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

const searchQuery = ref('');
const startDate = ref('');
const endDate = ref('');
const statusFilter = ref('');
const currentPage = ref(1);
const totalPages = ref(5);

const columns = [
  {
    key: 'settlementNo',
    label: '结算单号'
  },
  {
    key: 'contractNo',
    label: '合同编号'
  },
  {
    key: 'supplier',
    label: '供应商名称'
  },
  {
    key: 'settlementDate',
    label: '结算日期'
  },
  {
    key: 'settlementPeriod',
    label: '结算期间'
  },
  {
    key: 'goodsName',
    label: '货物名称'
  },
  {
    key: 'specification',
    label: '规格型号'
  },
  {
    key: 'quantity',
    label: '数量',
    format: (value: number) => value.toLocaleString()
  },
  {
    key: 'unit',
    label: '单位'
  },
  {
    key: 'unitPrice',
    label: '单价(元)',
    format: (value: number) => value.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
  },
  {
    key: 'totalAmount',
    label: '总金额(元)',
    format: (value: number) => value.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
  },
  {
    key: 'taxAmount',
    label: '税额(元)',
    format: (value: number) => value.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
  },
  {
    key: 'paymentMethod',
    label: '付款方式',
    format: (value: string) => getPaymentMethodText(value)
  },
  {
    key: 'paymentStatus',
    label: '付款状态',
    format: (value: string) => getPaymentStatusText(value),
    class: (value: string) => `payment-status-badge ${value}`
  }
];

const documents = ref([
  {
    id: 1,
    name: '2024年1月采购结算单',
    type: 'purchase-settlement',
    uploadTime: '2024-01-20 14:30',
    settlementNo: 'PS-2024001',
    contractNo: 'SC-2024001',
    supplier: '示例供应商',
    settlementDate: '2024-01-15',
    settlementPeriod: '2024年1月',
    goodsName: '钢材',
    specification: 'Q235 20*2000*6000',
    quantity: 500,
    unit: '吨',
    unitPrice: 3000,
    totalAmount: 1500000,
    taxRate: 13,
    taxAmount: 195000,
    bankAccount: '6228480000000000000',
    bankName: '中国银行北京分行',
    paymentMethod: 'bank',
    paymentStatus: 'unpaid'
  }
]);

const getPaymentMethodText = (method: string): string => {
  switch (method) {
    case 'bank': return '银行转账';
    case 'cash': return '现金支付';
    case 'check': return '支票支付';
    default: return '未知';
  }
};

const getPaymentStatusText = (status: string): string => {
  switch (status) {
    case 'unpaid': return '未付款';
    case 'partialPaid': return '部分付款';
    case 'paid': return '已付款';
    default: return '未知';
  }
};

const searchSettlements = () => {
  console.log('搜索条件:', {
    query: searchQuery.value,
    startDate: startDate.value,
    endDate: endDate.value,
    status: statusFilter.value
  });
};

const resetFilters = () => {
  searchQuery.value = '';
  startDate.value = '';
  endDate.value = '';
  statusFilter.value = '';
};

const viewSettlement = (settlement: any) => {
  console.log('查看结算单:', settlement);
};

const editSettlement = (settlement: any) => {
  console.log('编辑结算单:', settlement);
};

const deleteSettlement = (settlement: any) => {
  if (confirm(`确定要删除结算单 ${settlement.settlementNo} 吗？`)) {
    console.log('删除结算单:', settlement);
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  console.log('切换到页面:', page);
};
</script>

<style scoped>
:deep(.payment-status-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.payment-status-badge.unpaid) {
  background-color: #ffebee;
  color: #c62828;
}

:deep(.payment-status-badge.partialPaid) {
  background-color: #fff3e0;
  color: #ef6c00;
}

:deep(.payment-status-badge.paid) {
  background-color: #e8f5e9;
  color: #2e7d32;
}
</style>
  
  