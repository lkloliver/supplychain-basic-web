<template>
  <DocumentManageTemplate
    title="采购付款管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/finance/purchase-payment/upload"
    manageRouteName="/dashboard/finance/purchase-payment/manage"
    :documents="documents"
    :columns="columns"
    :pagination="pagination"
    searchPlaceholder="搜索付款单号或供应商"
    @page-change="handlePageChange"
    @search="handleSearch"
    @view="handleView"
    @edit="handleEdit"
    @delete="handleDelete"
  />
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

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

// 模拟文档数据
const documents = ref([
  {
    id: 1,
    name: '2023年1月采购付款记录',
    type: 'purchase-payment',
    uploadTime: '2023-01-20 14:30',
    status: '已确认',
    details: {
      paymentNo: 'PP-2023-001',
      supplier: '供应商A',
      contractNo: 'PC-2023-001',
      amount: '95,000',
      paymentDate: '2023-01-20',
      paymentMethod: '银行转账',
      remark: '首期付款'
    }
  },
  {
    id: 2,
    name: '2023年2月采购付款记录',
    type: 'purchase-payment',
    uploadTime: '2023-02-15 10:45',
    status: '待确认',
    details: {
      paymentNo: 'PP-2023-002',
      supplier: '供应商B',
      contractNo: 'PC-2023-002',
      amount: '120,000',
      paymentDate: '2023-02-15',
      paymentMethod: '银行转账',
      remark: '首期付款'
    }
  },
  {
    id: 3,
    name: '2023年3月采购付款记录',
    type: 'purchase-payment',
    uploadTime: '2023-03-10 09:30',
    status: '异常',
    details: {
      paymentNo: 'PP-2023-003',
      supplier: '供应商C',
      contractNo: 'PC-2023-003',
      amount: '75,000',
      paymentDate: '2023-03-10',
      paymentMethod: '支票',
      remark: '首期付款'
    }
  }
]);

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: documents.value.length
});

// 处理页码变化
const handlePageChange = (page) => {
  pagination.current = page;
  // 在实际应用中，这里会调用API获取对应页码的数据
  console.log('切换到页码:', page);
};

// 处理搜索
const handleSearch = (query) => {
  console.log('搜索:', query);
  // 在实际应用中，这里会调用API进行搜索
};

// 处理查看
const handleView = (record) => {
  console.log('查看记录:', record);
  // 在实际应用中，这里会打开详情弹窗或跳转到详情页
};

// 处理编辑
const handleEdit = (record) => {
  console.log('编辑记录:', record);
  // 在实际应用中，这里会打开编辑弹窗或跳转到编辑页
};

// 处理删除
const handleDelete = (record) => {
  if (confirm(`确定要删除付款单 ${record.details.paymentNo} 吗？`)) {
    console.log('删除记录:', record);
    // 在实际应用中，这里会调用API删除记录
    documents.value = documents.value.filter(item => item.id !== record.id);
    pagination.total = documents.value.length;
  }
};
</script>


