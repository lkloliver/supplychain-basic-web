<template>
  <DocumentManageTemplate
    title="销售收款管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/finance/sales-receipt/upload"
    manageRouteName="/dashboard/finance/sales-receipt/manage"
    :documents="documents"
    :columns="columns"
    :pagination="pagination"
    searchPlaceholder="搜索收款单号或客户"
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
    title: '收款单号',
    key: 'details.receiptNo',
    width: '120px'
  },
  {
    title: '客户',
    key: 'details.customer',
    width: '120px'
  },
  {
    title: '合同编号',
    key: 'details.contractNo',
    width: '120px'
  },
  {
    title: '收款金额',
    key: 'details.amount',
    width: '120px',
    format: (value) => `¥${value}`,
    align: 'right',
    className: 'amount-column'
  },
  {
    title: '收款日期',
    key: 'details.receiptDate',
    width: '120px'
  },
  {
    title: '收款方式',
    key: 'details.receiptMethod',
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
    name: '2023年5月销售收款记录',
    type: 'sales-receipt',
    uploadTime: '2023-05-01 14:30',
    status: '已确认',
    details: {
      receiptNo: 'SR20230501001',
      customer: '客户A',
      contractNo: 'SC20230401001',
      amount: '150,000',
      receiptDate: '2023-05-01',
      receiptMethod: '银行转账',
      remark: '按时收款'
    }
  },
  {
    id: 2,
    name: '2023年5月销售收款记录',
    type: 'sales-receipt',
    uploadTime: '2023-05-02 10:45',
    status: '待确认',
    details: {
      receiptNo: 'SR20230502002',
      customer: '客户B',
      contractNo: 'SC20230402002',
      amount: '85,000',
      receiptDate: '2023-05-02',
      receiptMethod: '支票',
      remark: '支票待入账'
    }
  },
  {
    id: 3,
    name: '2023年5月销售收款记录',
    type: 'sales-receipt',
    uploadTime: '2023-05-03 09:30',
    status: '已确认',
    details: {
      receiptNo: 'SR20230503003',
      customer: '客户C',
      contractNo: 'SC20230403003',
      amount: '200,000',
      receiptDate: '2023-05-03',
      receiptMethod: '银行转账',
      remark: '分期付款第一期'
    }
  },
  {
    id: 4,
    name: '2023年5月销售收款记录',
    type: 'sales-receipt',
    uploadTime: '2023-05-04 11:15',
    status: '已确认',
    details: {
      receiptNo: 'SR20230504004',
      customer: '客户A',
      contractNo: 'SC20230404004',
      amount: '120,000',
      receiptDate: '2023-05-04',
      receiptMethod: '现金',
      remark: '现金已入账'
    }
  },
  {
    id: 5,
    name: '2023年5月销售收款记录',
    type: 'sales-receipt',
    uploadTime: '2023-05-05 13:45',
    status: '异常',
    details: {
      receiptNo: 'SR20230505005',
      customer: '客户D',
      contractNo: 'SC20230405005',
      amount: '300,000',
      receiptDate: '2023-05-05',
      receiptMethod: '银行转账',
      remark: '金额不符，需核实'
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
  if (confirm(`确定要删除收款单 ${record.details.receiptNo} 吗？`)) {
    console.log('删除记录:', record);
    // 在实际应用中，这里会调用API删除记录
    documents.value = documents.value.filter(item => item.id !== record.id);
    pagination.total = documents.value.length;
  }
};
</script>


