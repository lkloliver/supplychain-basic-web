<template>
  <DocumentManageTemplate
    title="采购运单管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/base/purchase-waybill/upload"
    manageRouteName="/dashboard/goods/base/purchase-waybill/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchWaybills"
    @reset="resetFilters"
    @view="viewWaybill"
    @edit="editWaybill"
    @delete="deleteWaybill"
    @page-change="handlePageChange"
  />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import DocumentManageTemplate from '@/components/templates/DocumentManageTemplate.vue';

// 搜索查询
const searchQuery = ref('');
const startDate = ref('');
const endDate = ref('');
const statusFilter = ref('');

// 分页
const currentPage = ref(1);
const totalPages = ref(5);

// 定义表格列
const columns = [
  {
    key: 'waybillNo',
    label: '运单编号'
  },
  {
    key: 'contractNo',
    label: '合同编号'
  },
  {
    key: 'weightNo',
    label: '过磅单号'
  },
  {
    key: 'vehicleNo',
    label: '车牌号'
  },
  {
    key: 'driver',
    label: '司机姓名'
  },
  {
    key: 'supplier',
    label: '供应商'
  },
  {
    key: 'loadingPlace',
    label: '装货地点'
  },
  {
    key: 'unloadingPlace',
    label: '卸货地点'
  },
  {
    key: 'loadingTime',
    label: '装货时间'
  },
  {
    key: 'freightAmount',
    label: '运费总额',
    format: (value: number) => `¥${value.toFixed(2)}`
  },
  {
    key: 'status',
    label: '状态',
    format: (value: string) => getStatusText(value),
    class: (value: string) => `status-badge ${value}`
  }
];

// 模拟数据
const documents = ref([
  {
    id: 1,
    name: '2023年1月采购运单',
    type: 'purchase-waybill',
    uploadTime: '2023-01-20 14:30',
    waybillNo: 'PW-2023001',
    contractNo: 'PC-2023001',
    weightNo: 'W-2023001',
    vehicleNo: '京A12345',
    driver: '张三',
    driverPhone: '13812345678',
    supplier: '北京供应有限公司',
    loadingPlace: '河北省石家庄市',
    unloadingPlace: '北京市朝阳区',
    loadingTime: '2023-01-15 10:30',
    unloadingTime: '2023-01-16 10:30',
    freightRate: 80,
    freightAmount: 8000,
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月采购运单',
    type: 'purchase-waybill',
    uploadTime: '2023-02-15 10:45',
    waybillNo: 'PW-2023002',
    contractNo: 'PC-2023002',
    weightNo: 'W-2023002',
    vehicleNo: '京B12345',
    driver: '李四',
    driverPhone: '13912345678',
    supplier: '河南供应集团',
    loadingPlace: '河南省郑州市',
    unloadingPlace: '北京市海淀区',
    loadingTime: '2023-02-15 09:30',
    unloadingTime: '2023-02-16 09:30',
    freightRate: 85,
    freightAmount: 8500,
    status: 'active'
  }
]);

// 获取状态文本
const getStatusText = (status: string): string => {
  switch (status) {
    case 'active': return '有效';
    case 'expired': return '已过期';
    case 'completed': return '已完成';
    default: return '未知';
  }
};

// 搜索运单
const searchWaybills = () => {
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

// 查看运单详情
const viewWaybill = (waybill: any) => {
  console.log('查看运单:', waybill);
};

// 编辑运单
const editWaybill = (waybill: any) => {
  console.log('编辑运单:', waybill);
};

// 删除运单
const deleteWaybill = (waybill: any) => {
  if (confirm(`确定要删除运单 ${waybill.waybillNo} 吗？`)) {
    console.log('删除运单:', waybill);
  }
};

// 处理页面变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  console.log('切换到页面:', page);
};
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
  
  