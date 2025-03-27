<template>
  <DocumentManageTemplate
    title="到货厂家质检管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/factory-quality/upload"
    manageRouteName="/dashboard/goods/non-base/factory-quality/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchQuality"
    @reset="resetFilters"
    @view="viewQuality"
    @edit="editQuality"
    @delete="deleteQuality"
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
    key: 'qualityNo',
    label: '质检单号'
  },
  {
    key: 'contractNo',
    label: '合同编号'
  },
  {
    key: 'waybillNo',
    label: '运单编号'
  },
  {
    key: 'factory',
    label: '厂家名称'
  },
  {
    key: 'material',
    label: '材料名称'
  },
  {
    key: 'specification',
    label: '规格型号'
  },
  {
    key: 'quantity',
    label: '检验数量',
    format: (value: number) => `${value.toLocaleString()}`
  },
  {
    key: 'qualityResult',
    label: '质检结果',
    format: (value: string) => getQualityResultText(value),
    class: (value: string) => `quality-badge ${value}`
  },
  {
    key: 'inspector',
    label: '质检员'
  },
  {
    key: 'inspectTime',
    label: '检验时间'
  }
];

const documents = ref([
  {
    id: 1,
    name: '2023年1月厂家质检单',
    type: 'factory-quality',
    uploadTime: '2023-01-20 14:30',
    qualityNo: 'FQ-2023001',
    contractNo: 'SC-2023001',
    waybillNo: 'SW-2023001',
    factory: '北京钢铁厂',
    material: '钢材',
    specification: 'Q235 20*2000*6000',
    quantity: 500,
    qualityResult: 'pass',
    inspector: '张三',
    inspectTime: '2023-01-15 10:30',
    status: 'active'
  }
]);

const getQualityResultText = (result: string): string => {
  switch (result) {
    case 'pass': return '合格';
    case 'fail': return '不合格';
    case 'conditional': return '有条件通过';
    default: return '未知';
  }
};

const searchQuality = () => {
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

const viewQuality = (quality: any) => {
  console.log('查看质检单:', quality);
};

const editQuality = (quality: any) => {
  console.log('编辑质检单:', quality);
};

const deleteQuality = (quality: any) => {
  if (confirm(`确定要删除质检单 ${quality.qualityNo} 吗？`)) {
    console.log('删除质检单:', quality);
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  console.log('切换到页面:', page);
};
</script>

<style scoped>
:deep(.quality-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
    border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.quality-badge.pass) {
  background-color: #e8f5e9;
  color: #2e7d32;
}

:deep(.quality-badge.fail) {
  background-color: #ffebee;
  color: #c62828;
}

:deep(.quality-badge.conditional) {
  background-color: #fff3e0;
  color: #ef6c00;
  }
  </style>
  
  