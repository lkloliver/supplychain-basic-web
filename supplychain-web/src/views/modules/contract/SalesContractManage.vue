<template>
  <DocumentManageTemplate
    title="销售合同管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/sales/upload"
    manageRouteName="/dashboard/contract/sales/manage"
    :documents="contracts"
    :tableColumns="tableColumns"
    :tableData="contracts"
    :totalPages="totalPages"
    :currentPage="currentPage"
    @page-change="handlePageChange"
    @search="handleSearch"
    @view="handleView"
    @edit="handleEdit"
    @delete="handleDelete"
  />
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

// 表格列配置
const tableColumns = [
  { id: 'number', label: '合同编号', sortable: true },
  { id: 'date', label: '签订日期', sortable: true },
  { id: 'customer', label: '客户', sortable: true },
  { id: 'product', label: '产品', sortable: false },
  { id: 'quantity', label: '数量', sortable: true },
  { id: 'unitPrice', label: '单价', sortable: true, formatter: (val) => `¥${val.toFixed(2)}` },
  { id: 'totalAmount', label: '总金额', sortable: true, formatter: (val) => `¥${val.toFixed(2)}` },
  { 
    id: 'status', 
    label: '状态', 
    sortable: true,
    formatter: (val) => {
      const statusMap = {
        'active': '有效',
        'expired': '已过期',
        'completed': '已完成'
      };
      return statusMap[val] || '未知';
    },
    cellClass: (val) => `status-${val}`
  }
];

// 分页
const currentPage = ref(1);
const totalPages = ref(5);

// 模拟数据
const contracts = ref([
  {
    id: '1',
    number: 'SC-2023001',
    date: '2023-01-15',
    customer: '上海食品有限公司',
    product: '小麦',
    quantity: 1000,
    unitPrice: 3.2,
    totalAmount: 3200,
    status: 'active'
  },
  {
    id: '2',
    number: 'SC-2023002',
    date: '2023-02-20',
    customer: '北京餐饮集团',
    product: '玉米',
    quantity: 1500,
    unitPrice: 2.5,
    totalAmount: 3750,
    status: 'active'
  },
  {
    id: '3',
    number: 'SC-2023003',
    date: '2023-03-10',
    customer: '广州食品加工厂',
    product: '大豆',
    quantity: 800,
    unitPrice: 5.0,
    totalAmount: 4000,
    status: 'completed'
  },
  {
    id: '4',
    number: 'SC-2023004',
    date: '2023-04-05',
    customer: '深圳贸易有限公司',
    product: '水稻',
    quantity: 1200,
    unitPrice: 3.8,
    totalAmount: 4560,
    status: 'expired'
  },
  {
    id: '5',
    number: 'SC-2023005',
    date: '2023-05-18',
    customer: '杭州食品企业',
    product: '小麦',
    quantity: 2000,
    unitPrice: 3.1,
    totalAmount: 6200,
    status: 'active'
  }
]);

// 处理页面变化
const handlePageChange = (page) => {
  currentPage.value = page;
  // 在实际应用中，这里会调用API获取对应页的数据
  console.log('切换到页面:', page);
};

// 处理搜索
const handleSearch = (searchParams) => {
  console.log('搜索参数:', searchParams);
  // 在实际应用中，这里会调用API进行搜索
};

// 处理查看
const handleView = (item) => {
  console.log('查看合同:', item);
  // 在实际应用中，这里会跳转到详情页或打开详情弹窗
};

// 处理编辑
const handleEdit = (item) => {
  console.log('编辑合同:', item);
  // 在实际应用中，这里会跳转到编辑页或打开编辑弹窗
};

// 处理删除
const handleDelete = (item) => {
  console.log('删除合同:', item);
  // 在实际应用中，这里会调用API删除数据
  if (confirm(`确定要删除合同 ${item.number} 吗？`)) {
    // 模拟删除操作
    contracts.value = contracts.value.filter(contract => contract.id !== item.id);
  }
};
</script>

