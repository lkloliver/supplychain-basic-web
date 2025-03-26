<template>
  <DocumentManageTemplate
    title="运输合同管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/transport/upload"
    manageRouteName="/dashboard/contract/transport/manage"
    :cardMainColumns="cardMainColumns"
    :detailGroups="detailGroups"
    :customDetailComponents="customDetailComponents"
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
import TransportDetailsTable from '@/components/detail-components/TransportDetailsTable.vue';
import { fetchTransportContracts, deleteTransportContract } from '@/mocks/contract/transport';
import type { TransportContract } from '@/mocks/contract/transport';

// 搜索查询
const searchQuery = ref('');
const startDate = ref('');
const endDate = ref('');
const statusFilter = ref('');

// 分页
const currentPage = ref(1);
const totalPages = ref(5);

// 数据列表
const documents = ref<TransportContract[]>([]);

// 加载数据
const loadDocuments = async () => {
  const response = await fetchTransportContracts(currentPage.value);
  documents.value = response.data;
  totalPages.value = Math.ceil(response.total / response.pageSize);
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
const viewContract = (contract: TransportContract) => {
  console.log('查看合同:', contract);
  console.log('运输明细:', contract.transportDetails);
};

// 编辑合同
const editContract = (contract: any) => {
  console.log('编辑合同:', contract);
};

// 删除合同
const deleteContract = async (contract: TransportContract) => {
  if (confirm(`确定要删除合同 ${contract.contractNo} 吗？`)) {
    const result = await deleteTransportContract(contract.id);
    if (result.success) {
      alert('删除成功');
      loadDocuments(); // 重新加载数据
    }
  }
};

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  // 实际应用中这里会加载对应页的数据
  console.log('切换到页面:', page);
};

// 初始加载
loadDocuments();

// 卡片主要显示列
const cardMainColumns = [
  { key: 'contractNo', label: '合同名称' },
  { key: 'carrier', label: '承运方' },
  { key: 'shipper', label: '托运方' }
];

// 详情分组
const detailGroups = [
  {
    title: '基本信息',
    fields: [
      { key: 'contractNo', label: '合同编号' },
      { key: 'carrier', label: '承运方信息' },
      { key: 'shipper', label: '托运方信息' },
      { key: 'startTime', label: '合同起始时间' },
      { key: 'endTime', label: '合同截止时间' },
      { key: 'signDate', label: '签订日期' },
      { key: 'handler', label: '经办人' },
      { key: 'transportMethod', label: '运输方式' }
    ]
  },
  {
    title: '运输明细',
    customComponent: 'transport-details-table',
    dataKey: 'transportDetails',
    columns: [
      { key: 'startLocation', label: '起运地', width: '25%' },
      { key: 'endLocation', label: '目的地', width: '25%' },
      { key: 'category', label: '货物品类', width: '25%' },
      { 
        key: 'unitPrice', 
        label: '含税运输单价', 
        width: '25%',
        formatter: formatCurrency
      }
    ],
    showActions: false,
    showFooter: true
  }
];

// 注册自定义组件
const customDetailComponents = {
  'transport-details-table': TransportDetailsTable
};

// 格式化货币
function formatCurrency(value) {
  if (value === undefined || value === null) return '';
  
  const numValue = parseFloat(value);
  if (isNaN(numValue)) return value;
  
  return new Intl.NumberFormat('zh-CN', { 
    style: 'currency', 
    currency: 'CNY',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(numValue);
}
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

