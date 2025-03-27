<template>
  <DocumentManageTemplate
    title="采购付款管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/finance/purchase-payment/upload"
    manageRouteName="/dashboard/finance/purchase-payment/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    searchPlaceholder="搜索付款单号或供应商"
    @page-change="handlePageChange"
    @search="handleSearch"
    @view="handleView"
    @edit="handleEdit"
    @delete="handleDelete"
  />
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

// 初始化文档数据为空数组
const documents = ref([]);

// 表格列配置
const columns = [
  {
    title: '付款单号',
    key: 'details.paymentNo',
    width: '120px'
  },
  {
    title: '供应商',
    key: 'details.supplier',
    width: '120px'
  },
  {
    title: '合同编号',
    key: 'details.contractNo',
    width: '120px'
  },
  {
    title: '付款金额',
    key: 'details.amount',
    width: '120px',
    format: (value) => `¥${value}`,
    align: 'right',
    className: 'amount-column'
  },
  {
    title: '付款日期',
    key: 'details.paymentDate',
    width: '120px'
  },
  {
    title: '付款方式',
    key: 'details.paymentMethod',
    width: '100px'
  },
  {
    title: '状态',
    key: 'status',
    width: '100px',
    render: (value) => ({
      text: value,
      className: `status-${value === '已确认' ? 'success' : value === '待确认' ? 'warning' : 'danger'}`
    })
  }
];

// 分页相关
const currentPage = ref(1);
const totalPages = ref(1);

// 处理函数
const handleSearch = (query: string) => {
  console.log('搜索:', query);
};

const handleView = (item: any) => {
  console.log('查看:', item);
};

const handleEdit = (item: any) => {
  console.log('编辑:', item);
};

const handleDelete = (item: any) => {
  console.log('删除:', item);
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
};

// 可以在这里添加获取数据的方法
const fetchDocuments = async () => {
  try {
    // 这里添加实际的API调用
    // const response = await api.getPayments();
    // documents.value = response.data;
  } catch (error) {
    console.error('获取付款数据失败:', error);
  }
};

// 组件挂载时获取数据
onMounted(() => {
  fetchDocuments();
});
</script>


