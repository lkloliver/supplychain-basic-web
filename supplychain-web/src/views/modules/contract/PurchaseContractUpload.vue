<template>
  <DocumentUploadTemplate
    title="采购合同上传"
    detailsTitle="合同详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/purchase/upload"
    manageRouteName="/dashboard/contract/purchase/manage"
    :detailFields="detailFields"
    :onSubmit="handleSubmit"
    :onAIRecognize="handleAIRecognize"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';

const router = useRouter();

// 定义表单字段
const detailFields = [
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'supplier',
    label: '供应商',
    type: 'text',
    placeholder: '请输入供应商'
  },
  {
    key: 'amount',
    label: '合同金额',
    type: 'number',
    placeholder: '请输入合同金额'
  },
  {
    key: 'startDate',
    label: '开始日期',
    type: 'date'
  },
  {
    key: 'endDate',
    label: '结束日期',
    type: 'date'
  }
];

// AI自动识别文档
const handleAIRecognize = async (file) => {
  // 模拟AI识别过程
  console.log('正在识别文档...', file.name);
  
  return new Promise((resolve) => {
    setTimeout(() => {
      // 模拟识别结果
      const result = {
        contractNo: 'PC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        supplier: '自动识别供应商',
        amount: Math.floor(Math.random() * 1000000).toString(),
        startDate: new Date().toISOString().split('T')[0],
        endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

// 提交上传
const handleSubmit = async (formData) => {
  // 这里可以调用API上传文件和数据
  console.log('提交上传', formData);
  
  // 模拟上传成功
  return new Promise((resolve) => {
    setTimeout(() => {
      alert('上传成功');
      // 上传成功后跳转到管理页面
      router.push({ name: 'PurchaseContractManage' });
      resolve(true);
    }, 500);
  });
};
</script>

