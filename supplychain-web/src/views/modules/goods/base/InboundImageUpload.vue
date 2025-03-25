<template>
  <DocumentUploadTemplate
    title="入库影像上传"
    detailsTitle="影像详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/base/inbound-image/upload"
    manageRouteName="/dashboard/goods/base/inbound-image/manage"
    :detailFields="detailFields"
    @submit="handleSubmit"
    @recognize="handleRecognize"
  />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';

const router = useRouter();

// 定义表单字段
const detailFields = [
  {
    key: 'imageNo',
    label: '影像编号',
    type: 'text',
    placeholder: '请输入影像编号'
  },
  {
    key: 'weightNo',
    label: '过磅单号',
    type: 'text',
    placeholder: '请输入关联过磅单号'
  },
  {
    key: 'imageType',
    label: '影像类型',
    type: 'select',
    options: [
      { value: 'vehicle', label: '车辆照片' },
      { value: 'goods', label: '货物照片' },
      { value: 'weight', label: '磅单照片' },
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
    placeholder: '请输入拍摄人员'
  },
  {
    key: 'remark',
    label: '备注',
    type: 'textarea',
    placeholder: '请输入备注信息',
    rows: 3
  }
];

// 处理AI识别
const handleRecognize = async (file: File) => {
  console.log('正在识别文档...', file.name);
  
  return new Promise((resolve) => {
    setTimeout(() => {
      // 模拟识别结果
      const result = {
        imageNo: 'IMG-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        weightNo: 'W-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        imageType: 'vehicle',
        shootTime: new Date().toISOString(),
        location: '入库区域A',
        photographer: '自动识别拍摄人员',
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

// 处理提交
const handleSubmit = (formData: { name: string, file: File, details: Record<string, any> }) => {
  console.log('提交入库影像数据', formData);
  
  // 模拟上传成功
  alert('上传成功');
  
  // 上传成功后跳转到管理页面
  router.push({ name: 'InboundImageManage' });
};
</script>
    
    