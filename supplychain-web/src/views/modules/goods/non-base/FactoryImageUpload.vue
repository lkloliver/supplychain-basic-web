<template>
  <DocumentUploadTemplate
    title="厂家影像上传"
    detailsTitle="影像详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/factory-image/upload"
    manageRouteName="/dashboard/goods/non-base/factory-image/manage"
    :detailFields="detailFields"
    :onSubmit="handleSubmit"
    :onAIRecognize="handleRecognize"
  />
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';

const router = useRouter();

const detailFields = [
  {
    key: 'imageNo',
    label: '影像编号',
    type: 'text',
    placeholder: '请输入影像编号'
  },
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'waybillNo',
    label: '运单编号',
    type: 'text',
    placeholder: '请输入运单编号'
  },
  {
    key: 'imageType',
    label: '影像类型',
    type: 'select',
    options: [
      { value: 'factory', label: '厂家照片' },
      { value: 'goods', label: '货物照片' },
      { value: 'weighing', label: '过磅照片' },
      { value: 'quality', label: '质检照片' },
      { value: 'other', label: '其他照片' }
    ]
  },
  {
    key: 'shootTime',
    label: '拍摄时间',
    type: 'datetime'
  },
  {
    key: 'location',
    label: '拍摄地点',
    type: 'text',
    placeholder: '请输入拍摄地点'
  },
  {
    key: 'photographer',
    label: '拍摄人员',
    type: 'text',
    placeholder: '请输入拍摄人员姓名'
  },
  {
    key: 'deviceNo',
    label: '设备编号',
    type: 'text',
    placeholder: '请输入拍摄设备编号'
  },
  {
    key: 'remark',
    label: '备注',
    type: 'textarea',
    placeholder: '请输入备注信息',
    rows: 3
  }
];

const handleRecognize = async (file: File) => {
  console.log('正在识别文档...', file.name);
  
  return new Promise((resolve) => {
    setTimeout(() => {
      const result = {
        imageNo: 'FI-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        waybillNo: 'SW-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        imageType: 'factory',
        shootTime: new Date().toISOString(),
        location: '厂家现场',
        photographer: '自动识别拍摄员',
        deviceNo: 'CAM-' + Math.floor(Math.random() * 100).toString().padStart(3, '0'),
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

const handleSubmit = async (formData: { name: string, file: File, details: Record<string, any> }) => {
  try {
    console.log('提交厂家影像数据', formData);
    await new Promise(resolve => setTimeout(resolve, 1000));
    alert('上传成功');
    router.push('/dashboard/goods/non-base/factory-image/manage');
  } catch (error: any) {
    console.error('上传失败:', error);
    alert('上传失败: ' + error.message);
  }
};
</script>
  
  