<template>
  <DocumentManageTemplate
    title="采购合同管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/purchase/upload"
    manageRouteName="/dashboard/contract/purchase/manage"
    :documents="contracts"
    titleField="name"
    uploadTimeField="uploadTime"
    :columns="columns"
    :documentIcon="FileTextIcon"
    searchPlaceholder="搜索合同名称、编号或供应商..."
    emptyText="暂无采购合同，请先上传"
    :showCheckbox="true"
    :showPagination="true"
    :currentPage="currentPage"
    :totalPages="totalPages"
    @search="handleSearch"
    @filter="handleFilter"
    @sort="handleSort"
    @export="handleExport"
    @view="handleView"
    @edit="handleEdit"
    @delete="handleDelete"
    @select="handleSelect"
    @select-all="handleSelectAll"
    @page-change="handlePageChange"
  />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { FileTextIcon } from 'lucide-vue-next';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

const router = useRouter();

// 表格列配置
const columns = [
  { key: 'details.contractNo', label: '合同编号' },
  { key: 'details.supplier', label: '供应商' },
  { key: 'details.amount', label: '合同金额' },
  { key: 'details.signDate', label: '签订日期' },
  { key: 'details.status', label: '状态' }
];

// 模拟数据
const contracts = ref([
  {
    id: 1,
    name: '2023年度煤炭采购合同',
    uploadTime: '2023-01-15 14:30',
    details: {
      contractNo: 'PC-2023-001',
      supplier: '山西煤炭集团',
      amount: '¥1,500,000',
      signDate: '2023-01-10',
      status: '已生效'
    }
  },
  {
    id: 2,
    name: '铁矿石季度采购合同',
    uploadTime: '2023-02-20 09:15',
    details: {
      contractNo: 'PC-2023-002',
      supplier: '鞍钢集团',
      amount: '¥2,300,000',
      signDate: '2023-02-15',
      status: '执行中'
    }
  },
  {
    id: 3,
    name: '物流运输服务合同',
    uploadTime: '2023-03-05 16:45',
    details: {
      contractNo: 'PC-2023-003',
      supplier: '中铁物流',
      amount: '¥800,000',
      signDate: '2023-03-01',
      status: '待审批'
    }
  }
]);

// 分页相关
const currentPage = ref(1);
const totalPages = ref(3);

// 处理搜索
const handleSearch = (query) => {
  console.log('搜索:', query);
  // 实际应用中这里会调用API进行搜索
};

// 处理筛选
const handleFilter = () => {
  console.log('打开筛选面板');
  // 实际应用中这里会打开筛选面板
};

// 处理排序
const handleSort = () => {
  console.log('打开排序面板');
  // 实际应用中这里会打开排序面板
};

// 处理导出
const handleExport = () => {
  console.log('导出数据');
  // 实际应用中这里会导出数据
};

// 处理查看
const handleView = (contract) => {
  console.log('查看合同:', contract);
  // 实际应用中这里会跳转到详情页或打开详情弹窗
};

// 处理编辑
const handleEdit = (contract) => {
  console.log('编辑合同:', contract);
  // 实际应用中这里会跳转到编辑页面
};

// 处理删除
const handleDelete = (contract) => {
  console.log('删除合同:', contract);
  if (confirm(`确定要删除"${contract.name}"吗？`)) {
    // 实际应用中这里会调用API删除数据
    contracts.value = contracts.value.filter(item => item.id !== contract.id);
  }
};

// 处理选择
const handleSelect = (selectedItems) => {
  console.log('选中的合同:', selectedItems);
  // 实际应用中这里会更新选中状态
};

// 处理全选
const handleSelectAll = (selectedItems) => {
  console.log('全选/取消全选:', selectedItems);
  // 实际应用中这里会更新全选状态
};

// 处理页码变化
const handlePageChange = (page) => {
  console.log('切换到页码:', page);
  currentPage.value = page;
  // 实际应用中这里会加载对应页的数据
};

// 组件挂载时加载数据
onMounted(() => {
  // 实际应用中这里会从API加载数据
  console.log('组件已挂载，加载数据');
});
</script>

