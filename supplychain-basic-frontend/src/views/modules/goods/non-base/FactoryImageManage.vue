<template>
  <DocumentManageTemplate
    title="厂家影像管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/factory-image/upload"
    manageRouteName="/dashboard/goods/non-base/factory-image/manage"
    :columns="columns"
    :documents="documents"
    :currentPage="currentPage"
    :totalPages="totalPages"
    :searchQuery="searchQuery"
    :startDate="startDate"
    :endDate="endDate"
    :statusFilter="statusFilter"
    @search="searchImages"
    @reset="resetFilters"
    @view="viewImage"
    @edit="editImage"
    @delete="deleteImage"
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
    key: 'imageNo',
    label: '影像编号'
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
    key: 'imageType',
    label: '影像类型',
    format: (value: string) => getImageTypeText(value),
    class: (value: string) => `image-type-badge ${value}`
  },
  {
    key: 'shootTime',
    label: '拍摄时间'
  },
  {
    key: 'location',
    label: '拍摄地点'
  },
  {
    key: 'photographer',
    label: '拍摄人员'
  },
  {
    key: 'deviceNo',
    label: '设备编号'
  },
  {
    key: 'uploadTime',
    label: '上传时间'
  }
];

const documents = ref([
  {
    id: 1,
    name: '2023年1月厂家影像',
    type: 'factory-image',
    uploadTime: '2023-01-20 14:30',
    imageNo: 'FI-2023001',
    contractNo: 'SC-2023001',
    waybillNo: 'SW-2023001',
    imageType: 'factory',
    shootTime: '2023-01-15 10:30',
    location: '厂家现场',
    photographer: '张三',
    deviceNo: 'CAM-001',
    status: 'active'
  }
]);

const getImageTypeText = (type: string): string => {
  switch (type) {
    case 'factory': return '厂家照片';
    case 'goods': return '货物照片';
    case 'weighing': return '过磅照片';
    case 'quality': return '质检照片';
    case 'other': return '其他照片';
    default: return '未知';
  }
};

const searchImages = () => {
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

const viewImage = (image: any) => {
  console.log('查看影像:', image);
};

const editImage = (image: any) => {
  console.log('编辑影像:', image);
};

const deleteImage = (image: any) => {
  if (confirm(`确定要删除影像 ${image.imageNo} 吗？`)) {
    console.log('删除影像:', image);
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  console.log('切换到页面:', page);
};
</script>

<style scoped>
:deep(.image-type-badge) {
  display: inline-block;
  padding: 0.25rem 0.5rem;
    border-radius: 4px;
  font-size: 0.75rem;
}

:deep(.image-type-badge.factory) {
  background-color: #e3f2fd;
  color: #1565c0;
}

:deep(.image-type-badge.goods) {
  background-color: #e8f5e9;
  color: #2e7d32;
}

:deep(.image-type-badge.weighing) {
  background-color: #fff3e0;
  color: #ef6c00;
}

:deep(.image-type-badge.quality) {
  background-color: #f3e5f5;
  color: #7b1fa2;
}

:deep(.image-type-badge.other) {
    background-color: #f5f5f5;
  color: #616161;
  }
  </style>
  
  