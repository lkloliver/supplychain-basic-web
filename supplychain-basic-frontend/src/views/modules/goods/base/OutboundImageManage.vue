<template>
  <DocumentManageTemplate
    title="出库影像管理"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/base/outbound-image/upload"
    manageRouteName="/dashboard/goods/base/outbound-image/manage"
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
    key: 'imageNo',
    label: '影像编号'
  },
  {
    key: 'weightNo',
    label: '过磅单号'
  },
  {
    key: 'contractNo',
    label: '合同编号'
  },
  {
    key: 'imageType',
    label: '影像类型',
    format: (value: string) => getImageTypeText(value)
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
    key: 'destination',
    label: '目的地'
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
    name: '2023年1月出库影像',
    type: 'outbound-image',
    uploadTime: '2023-01-20 14:30',
    imageNo: 'OI-2023001',
    weightNo: 'OW-2023001',
    contractNo: 'SC-2023001',
    imageType: 'vehicle',
    shootTime: '2023-01-15 10:30',
    location: '出库区域A',
    photographer: '张三',
    destination: '北京市朝阳区',
    status: 'active'
  },
  {
    id: 2,
    name: '2023年2月出库影像',
    type: 'outbound-image',
    uploadTime: '2023-02-15 10:45',
    imageNo: 'OI-2023002',
    weightNo: 'OW-2023002',
    contractNo: 'SC-2023002',
    imageType: 'goods',
    shootTime: '2023-02-15 09:30',
    location: '出库区域B',
    photographer: '李四',
    destination: '天津市南开区',
    status: 'active'
  },
  {
    id: 3,
    name: '2023年3月出库影像',
    type: 'outbound-image',
    uploadTime: '2023-03-10 09:30',
    imageNo: 'OI-2023003',
    weightNo: 'OW-2023003',
    contractNo: 'SC-2023003',
    imageType: 'weight',
    shootTime: '2023-03-10 08:30',
    location: '出库区域C',
    photographer: '王五',
    destination: '河北省廊坊市',
    status: 'completed'
  }
]);

// 获取影像类型文本
const getImageTypeText = (type: string): string => {
  switch (type) {
    case 'vehicle': return '车辆照片';
    case 'goods': return '货物照片';
    case 'weight': return '磅单照片';
    case 'other': return '其他照片';
    default: return '未知类型';
  }
};

// 获取状态文本
const getStatusText = (status: string): string => {
  switch (status) {
    case 'active': return '有效';
    case 'expired': return '已过期';
    case 'completed': return '已完成';
    default: return '未知';
  }
};

// 搜索影像
const searchImages = () => {
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

// 查看影像详情
const viewImage = (image: any) => {
  console.log('查看影像:', image);
};

// 编辑影像
const editImage = (image: any) => {
  console.log('编辑影像:', image);
};

// 删除影像
const deleteImage = (image: any) => {
  if (confirm(`确定要删除影像 ${image.imageNo} 吗？`)) {
    console.log('删除影像:', image);
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
  
  