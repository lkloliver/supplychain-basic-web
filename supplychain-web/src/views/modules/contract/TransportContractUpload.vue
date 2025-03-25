<template>
  <DocumentUploadTemplate
    title="运输合同上传"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/transport/upload"
    manageRouteName="/dashboard/contract/transport/manage"
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

// 定义详情字段
const detailFields = [
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'transporter',
    label: '运输商',
    type: 'text',
    placeholder: '请输入运输商'
  },
  {
    key: 'route',
    label: '运输路线',
    type: 'text',
    placeholder: '请输入运输路线'
  },
  {
    key: 'quantity',
    label: '运输量',
    type: 'number',
    placeholder: '请输入运输量'
  },
  {
    key: 'unitPrice',
    label: '单价',
    type: 'number',
    placeholder: '请输入单价'
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

// 处理AI识别
const handleRecognize = async (file) => {
  // 模拟AI识别过程
  console.log('正在识别文档...', file.name);
  
  return new Promise((resolve) => {
    setTimeout(() => {
      // 模拟识别结果
      const result = {
        contractNo: 'TC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        transporter: '自动识别运输商',
        route: '自动识别路线',
        quantity: Math.floor(Math.random() * 1000).toString(),
        unitPrice: Math.floor(Math.random() * 100).toString(),
        amount: Math.floor(Math.random() * 100000).toString(),
        startDate: new Date().toISOString().split('T')[0],
        endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

// 处理提交
const handleSubmit = (formData) => {
  console.log('提交运输合同数据', formData);
  
  // 模拟API调用
  setTimeout(() => {
    alert('运输合同上传成功');
    router.push({ name: 'TransportContractManage' });
  }, 1000);
};
</script>

