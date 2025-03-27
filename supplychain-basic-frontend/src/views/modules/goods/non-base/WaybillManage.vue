<template>
  <DocumentManageTemplate
    title="运单管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/waybill/upload"
    manageRouteName="/dashboard/goods/non-base/waybill/manage"
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

const searchQuery = ref('');
const startDate = ref('');
const endDate = ref('');
const statusFilter = ref('');
const currentPage = ref(1);
const totalPages = ref(5);

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
    key: 'vehicleNo',
    label: '车牌号'
  },
  {
    key: 'driver',
    label: '司机姓名'
  },
  {
    key: 'transportCompany',
    label: '运输公司'
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
    key: 'goodsWeight',
    label: '货物重量(kg)',
    format: (value: number) => value.toLocaleString()
  },
  {
    key: 'freightCost',
    label: '运费(元)',
    format: (value: number) => value.toLocaleString()
  },
  {
    key: 'status',
    label: '状态',
    format: (value: string) => getStatusText(value),
    class: (value: string) => `status-badge ${value}`
  }
];

const documents = ref([
  {
    id: 1,
    name: '2023年1月运单',
    type: 'waybill',
    uploadTime: '2023-01-20 14:30',
    waybillNo: 'WB-2023001',
    contractNo: 'SC-2023001',
    vehicleNo: '京A12345',
    driver: '张三',
    driverPhone: '13800138000',
    transportCompany: '示例运输公司',
    loadingPlace: '北京',
    unloadingPlace: '上海',
    loadingTime: '2023-01-15 10:30',
    estimatedArrivalTime: '2023-01-16 10:30',
    goodsName: '钢材',
    goodsWeight: 25000,
    freightCost: 3500,
    status: 'inTransit'
  }
]);

const getStatusText = (status: string): string => {
  switch (status) {
    case 'inTransit': return '运输中';
    case 'delivered': return '已送达';
    case 'cancelled': return '已取消';
    case 'completed': return '已完成';
    default: return '未知';
  }
};

const searchWaybills = () => {
  console.log('搜索条件:', {
    query: searchQuery.value,
    startDate: startDate.value,
    endDate: endDate.value,
    status: statusFilter.value
  });
};

const resetFilters = () => {
  searchQuery.value = '';
  startDate.value = '';
  endDate.value = '';
  statusFilter.value = '';
};

const viewWaybill = (waybill: any) => {
  console.log('查看运单:', waybill);
};

const editWaybill = (waybill: any) => {
  console.log('编辑运单:', waybill);
};

const deleteWaybill = (waybill: any) => {
  if (confirm(`确定要删除运单 ${waybill.waybillNo} 吗？`)) {
    console.log('删除运单:', waybill);
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  console.log('切换到页面:', page);
};
</script>

<style scoped>
:deep(.status-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
    border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.status-badge.inTransit) {
  background-color: #fff3e0;
  color: #ef6c00;
}

:deep(.status-badge.delivered) {
  background-color: #e8f5e9;
  color: #2e7d32;
}

:deep(.status-badge.cancelled) {
  background-color: #ffebee;
  color: #c62828;
}

:deep(.status-badge.completed) {
  background-color: #e3f2fd;
  color: #1565c0;
  }
  </style>
  
  