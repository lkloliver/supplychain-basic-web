<template>
  <DocumentManageTemplate
    title="采购合同管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/purchase/upload"
    manageRouteName="/dashboard/contract/purchase/manage"
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

<script setup>
import { ref, onMounted } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';
import GoodsTable from '@/components/detail-components/GoodsTable.vue';

// 模拟数据
const contracts = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);

// 卡片主要显示列
const cardMainColumns = [
  { key: 'contractDate', label: '签订日期' },
  { key: 'supplierName', label: '供应商' },
  { key: 'totalAmount', label: '合同金额', formatter: formatCurrency },
  { key: 'status', label: '状态' }
];

// 详情分组
const detailGroups = [
  {
    title: '基本信息',
    fields: [
      { key: 'contractNo', label: '合同编号' },
      { key: 'contractDate', label: '签订日期' },
      { key: 'supplierName', label: '供应商' },
      { key: 'supplierContact', label: '联系人' },
      { key: 'supplierPhone', label: '联系电话' },
      { key: 'totalAmount', label: '合同金额', formatter: formatCurrency },
      { key: 'status', label: '状态' }
    ]
  },
  {
    title: '货物信息',
    customComponent: 'goods-table',
    dataKey: 'goods',
    columns: [
      { key: 'name', label: '货物名称' },
      { key: 'specification', label: '规格' },
      { key: 'quantity', label: '数量' },
      { key: 'unit', label: '单位' },
      { key: 'unitPrice', label: '单价', formatter: formatCurrency },
      { key: 'total', label: '小计', formatter: formatCurrency, isTotal: true }
    ],
    showActions: true,
    showFooter: true
  },
  {
    title: '付款信息',
    fields: [
      { key: 'paymentMethod', label: '付款方式' },
      { key: 'paymentTerms', label: '付款条件' },
      { key: 'bankName', label: '开户银行' },
      { key: 'bankAccount', label: '银行账号' }
    ]
  },
  {
    title: '其他信息',
    fields: [
      { key: 'deliveryDate', label: '交货日期' },
      { key: 'deliveryAddress', label: '交货地点' },
      { key: 'remarks', label: '备注' }
    ]
  }
];

// 注册自定义组件
const customDetailComponents = {
  'goods-table': GoodsTable
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

// 模拟获取数据
const fetchData = async (page = 1) => {
  // 模拟API请求延迟
  await new Promise(resolve => setTimeout(resolve, 500));
  
  // 生成模拟数据
  const mockContracts = [
    {
      id: 1,
      contractNo: 'CT-2023-001',
      contractDate: '2023-05-15',
      supplierName: '北京供应商A有限公司',
      supplierContact: '张经理',
      supplierPhone: '13800138000',
      totalAmount: 125000,
      status: '已签署',
      paymentMethod: '银行转账',
      paymentTerms: '30%预付款，70%交货后付款',
      bankName: '中国工商银行北京分行',
      bankAccount: '6222021234567890123',
      deliveryDate: '2023-06-30',
      deliveryAddress: '北京市朝阳区某某路123号',
      remarks: '年度采购合同',
      goods: [
        { id: 101, name: '原材料A', specification: '标准型', quantity: 100, unit: '吨', unitPrice: 800, total: 80000 },
        { id: 102, name: '原材料B', specification: '高纯度', quantity: 50, unit: '吨', unitPrice: 900, total: 45000 }
      ]
    },
    {
      id: 2,
      contractNo: 'CT-2023-002',
      contractDate: '2023-05-20',
      supplierName: '上海供应商B有限公司',
      supplierContact: '李经理',
      supplierPhone: '13900139000',
      totalAmount: 86000,
      status: '执行中',
      paymentMethod: '银行转账',
      paymentTerms: '50%预付款，50%交货后付款',
      bankName: '中国建设银行上海分行',
      bankAccount: '6227001234567890123',
      deliveryDate: '2023-07-15',
      deliveryAddress: '上海市浦东新区某某路456号',
      remarks: '季度采购合同',
      goods: [
        { id: 201, name: '零部件A', specification: '标准型', quantity: 200, unit: '个', unitPrice: 150, total: 30000 },
        { id: 202, name: '零部件B', specification: '加强型', quantity: 100, unit: '个', unitPrice: 200, total: 20000 },
        { id: 203, name: '零部件C', specification: '特殊型', quantity: 120, unit: '个', unitPrice: 300, total: 36000 }
      ]
    }
  ];
  
  contracts.value = mockContracts;
  totalPages.value = 1;
  currentPage.value = page;
};

// 事件处理函数
const handleSearch = (query) => {
  console.log('搜索:', query);
  fetchData(1);
};

const handleView = (contract) => {
  console.log('查看合同:', contract);
};

const handleEdit = (contract) => {
  console.log('编辑合同:', contract);
};

const handleDelete = (contract) => {
  console.log('删除合同:', contract);
  fetchData(currentPage.value);
};

const handlePageChange = (page) => {
  fetchData(page);
};

// 初始化
onMounted(() => {
  fetchData();
});
</script>

