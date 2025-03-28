<template>
  <DocumentManageTemplate
    title="销售合同管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/sales/upload"
    manageRouteName="/dashboard/contract/sales/manage"
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
import DetailsTable from '@/components/detail-components/DetailsTable.vue';
import { fetchSalesContracts, deleteSalesContract, type SalesContract } from '@/mocks/contract/sales';

// 状态
const contracts = ref<SalesContract[]>([]);
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = 10;


// 卡片主要显示列
const cardMainColumns = [
  { key: 'contractNo', label: '合同名称' },
  { key: 'buyer', label: '买方' },
  { key: 'seller', label: '卖方' }
];

// 详情分组
const detailGroups = [
  {
    title: '基本信息',
    fields: [
      { key: 'contractNo', label: '销售合同编号' },
      { key: 'signDate', label: '签订时间' },
      { key: 'buyer', label: '买方信息' },
      { key: 'seller', label: '卖方信息' },
      { key: 'paymentAccount', label: '付款账户' },
      { key: 'receivingAccount', label: '收款账户' },
      { key: 'taxRate', label: '税率' },
      { key: 'invoiceType', label: '发票类型' },
      { key: 'paymentDate', label: '付款时间' },
      { key: 'paymentMethod', label: '付款方式' },
      { key: 'performancePeriod', label: '履约期限' },
      { key: 'performanceLocation', label: '履约地点' },
      { key: 'performanceMethod', label: '履约方式' },
      { key: 'handler', label: '经办人信息' },
      { key: 'totalAmount', label: '合同金额', formatter: formatCurrency },
      { key: 'remarks', label: '备注' }
    ]
  },
  {
    title: '货物信息',
    customComponent: 'details-table',
    dataKey: 'goods',
    columns: [
      { key: 'name', label: '货物名称', width: '25%' },
      { key: 'quantity', label: '货物数量', width: '20%' },
      { key: 'price', label: '货物价格', width: '20%', formatter: formatCurrency },
      { key: 'specification', label: '规格型号', width: '25%' }
    ],
    showActions: false,
    showFooter: true
  }
];

// 注册自定义组件
const customDetailComponents = {
  'details-table': DetailsTable
};

// 格式化货币
function formatCurrency(value: number): string {
  if (value === undefined || value === null) return '';
  
  return new Intl.NumberFormat('zh-CN', { 
    style: 'currency', 
    currency: 'CNY',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(value);
}

// 获取数据
const fetchData = async (page = 1) => {
  try {
    const response = await fetchSalesContracts(page, pageSize);
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

const handleView = (contract: SalesContract) => {
  console.log('查看合同:', contract);
};

const handleEdit = (contract: SalesContract) => {
  console.log('编辑合同:', contract);
};

const handleDelete = async (contract: SalesContract) => {
  try {
    await deleteSalesContract(contract.id);
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

