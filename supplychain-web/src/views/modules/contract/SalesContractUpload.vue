<template>
  <DocumentUploadTemplate
    title="销售合同上传"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/sales/upload"
    manageRouteName="/dashboard/contract/sales/manage"
    :detailFields="detailFields"
    @submit="handleSubmit"
    @recognize="handleRecognize"
  />
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';

const router = useRouter();

// 详情字段配置
const detailFields = [
  {
    id: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    id: 'customer',
    label: '客户',
    type: 'text',
    placeholder: '请输入客户'
  },
  {
    id: 'amount',
    label: '合同金额',
    type: 'number',
    placeholder: '请输入合同金额'
  },
  {
    id: 'startDate',
    label: '开始日期',
    type: 'date',
    placeholder: ''
  },
  {
    id: 'endDate',
    label: '结束日期',
    type: 'date',
    placeholder: ''
  }
];

// 处理提交
const handleSubmit = (formData) => {
  console.log('提交销售合同数据', formData);
  
  // 模拟API调用
  setTimeout(() => {
    alert('销售合同上传成功');
    router.push({ name: manageRouteName });
  }, 1000);
};

// 处理AI识别
const handleRecognize = (file) => {
  console.log('识别销售合同文件', file);
  
  // 模拟AI识别过程
  return new Promise((resolve) => {
    setTimeout(() => {
      // 返回识别结果
      resolve({
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        customer: '自动识别客户',
        amount: Math.floor(Math.random() * 1000000).toString(),
        startDate: new Date().toISOString().split('T')[0],
        endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
      });
    }, 1500);
  });
};
</script>

