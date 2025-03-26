<template>
  <DocumentManageTemplate
    title="运输合同管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/transport/upload"
    manageRouteName="/dashboard/contract/transport/manage"
    :documents="contracts"
    titleField="contractNo"
    titleColumnName="合同编号"
    :cardMainColumns="cardMainColumns"
    :detailGroups="detailGroups"
    :customDetailComponents="customDetailComponents"
    searchPlaceholder="搜索合同..."
    :currentPage="currentPage"
    :totalPages="totalPages"
    @search="handleSearch"
    @view="handleView"
    @edit="handleEdit"
    @delete="handleDelete"
    @page-change="handlePageChange"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';
import { fetchTransportContracts, deleteTransportContract } from '@/mocks/contract/transport';
import DetailsTable from '@/components/detail-components/DetailsTable.vue';

// 状态
const contracts = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = 10;

// 注册自定义组件
const customDetailComponents = {
  'details-table': DetailsTable
};

// 卡片主要显示列
const cardMainColumns = [
  { key: 'contractNo', label: '合同编号' },
  { key: 'carrier', label: '承运方' },
  { key: 'shipper', label: '托运方' }
];

// 详情分组
const detailGroups = [
  {
    title: '基本信息',
    fields: [
      { key: 'contractNo', label: '运输合同编号' },
      { key: 'signDate', label: '签订日期' },
      { key: 'startDate', label: '合同起始时间' },
      { key: 'endDate', label: '合同截止时间' },
      { key: 'transportMethod', label: '运输方式' },
      { key: 'carrier', label: '承运方名称' },
      { key: 'carrierContact', label: '承运方联系人' },
      { key: 'carrierPhone', label: '承运方联系电话' },
      { key: 'shipper', label: '托运方名称' },
      { key: 'shipperContact', label: '托运方联系人' },
      { key: 'shipperPhone', label: '托运方联系电话' },
      { key: 'handler', label: '经办人' }
    ]
  },
  {
    title: '运输明细',
    customComponent: 'details-table',
    dataKey: 'transportDetails',
    columns: [
      { key: 'startLocation', label: '起运地', width: '25%' },
      { key: 'endLocation', label: '目的地', width: '25%' },
      { key: 'goodsCategory', label: '名称/品类', width: '25%' },
      { key: 'unitPrice', label: '含税运输单价', width: '25%', formatter: formatCurrency }
    ],
    showActions: false,
    showFooter: true
  }
];


// 格式化货币
function formatCurrency(value: number | string | undefined | null): string {
  if (value === undefined || value === null) return '';
  
  const numValue = parseFloat(String(value));
  if (isNaN(numValue)) return String(value);
  
  return new Intl.NumberFormat('zh-CN', { 
    style: 'currency', 
    currency: 'CNY',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(numValue);
}

// 获取数据
const fetchData = async (page = 1) => {
  try {
    const response = await fetchTransportContracts(page, pageSize);
    contracts.value = response.data;
    totalPages.value = Math.ceil(response.total / pageSize);
    currentPage.value = page;
  } catch (error) {
    console.error('获取合同列表失败:', error);
  }
};

// 事件处理函数
const handleSearch = (query: string) => {
  console.log('搜索:', query);
  fetchData(1);
};

const handleView = (contract: any) => {
  console.log('查看合同:', contract);
};

const handleEdit = (contract: any) => {
  console.log('编辑合同:', contract);
};

const handleDelete = async (contract: any) => {
  try {
    await deleteTransportContract(contract.id);
    await fetchData(currentPage.value);
  } catch (error) {
    console.error('删除合同失败:', error);
  }
};

const handlePageChange = (page: number) => {
  fetchData(page);
};

// 初始化
onMounted(() => {
  fetchData();
});
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

