<template>
  <DocumentManageTemplate
    title="销售台账管理"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchLedgers"
    @reset="resetFilters"
    @view="viewLedger"
    @edit="editLedger"
    @delete="deleteLedger"
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
    key: 'ledgerNo',
    label: '台账编号'
  },
  {
    key: 'contractNo',
    label: '合同编号'
  },
  {
    key: 'customer',
    label: '客户名称'
  },
  {
    key: 'businessDate',
    label: '业务日期'
  },
  {
    key: 'businessType',
    label: '业务类型',
    format: (value: string) => getBusinessTypeText(value)
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
    key: 'receiptStatus',
    label: '收款状态',
    format: (value: string) => getReceiptStatusText(value),
    class: (value: string) => `receipt-status-badge ${value}`
  },
  {
    key: 'deliveryStatus',
    label: '交货状态',
    format: (value: string) => getDeliveryStatusText(value),
    class: (value: string) => `delivery-status-badge ${value}`
  }
];

const documents = ref([
  {
    id: 1,
    name: '2024年1月销售台账',
    type: 'sales-ledger',
    uploadTime: '2024-01-20 14:30',
    ledgerNo: 'SL-2024001',
    contractNo: 'SC-2024001',
    customer: '示例客户',
    businessDate: '2024-01-15',
    businessType: 'sales',
    goodsName: '钢材',
    specification: 'Q235 20*2000*6000',
    quantity: 500,
    unit: '吨',
    unitPrice: 3000,
    totalAmount: 1500000,
    taxRate: 13,
    taxAmount: 195000,
    receiptStatus: 'unpaid',
    deliveryStatus: 'undelivered'
  }
]);

const getBusinessTypeText = (type: string): string => {
  switch (type) {
    case 'sales': return '销售出库';
    case 'return': return '销售退货';
    case 'adjustment': return '价格调整';
    default: return '未知';
  }
};

const getReceiptStatusText = (status: string): string => {
  switch (status) {
    case 'unpaid': return '未收款';
    case 'partialPaid': return '部分收款';
    case 'paid': return '已收款';
    default: return '未知';
  }
};

const getDeliveryStatusText = (status: string): string => {
  switch (status) {
    case 'undelivered': return '未交货';
    case 'partialDelivered': return '部分交货';
    case 'delivered': return '已交货';
    default: return '未知';
  }
};

const searchLedgers = () => {
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

const viewLedger = (ledger: any) => {
  console.log('查看台账:', ledger);
};

const editLedger = (ledger: any) => {
  console.log('编辑台账:', ledger);
};

const deleteLedger = (ledger: any) => {
  if (confirm(`确定要删除台账 ${ledger.ledgerNo} 吗？`)) {
    console.log('删除台账:', ledger);
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  console.log('切换到页面:', page);
};
</script>

<style scoped>
:deep(.receipt-status-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.receipt-status-badge.unpaid) {
  background-color: #ffebee;
  color: #c62828;
}

:deep(.receipt-status-badge.partialPaid) {
  background-color: #fff3e0;
  color: #ef6c00;
}

:deep(.receipt-status-badge.paid) {
  background-color: #e8f5e9;
  color: #2e7d32;
}

:deep(.delivery-status-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.delivery-status-badge.undelivered) {
  background-color: #e3f2fd;
  color: #1565c0;
}

:deep(.delivery-status-badge.partialDelivered) {
  background-color: #fff3e0;
  color: #ef6c00;
}

:deep(.delivery-status-badge.delivered) {
  background-color: #e8f5e9;
  color: #2e7d32;
}
</style>
  
  