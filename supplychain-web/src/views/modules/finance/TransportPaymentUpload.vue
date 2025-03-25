<template>
  <DocumentUploadTemplate
    title="运输付款上传"
    detailsTitle="付款详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/finance/transport-payment/upload"
    manageRouteName="/dashboard/finance/transport-payment/manage"
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
    key: 'paymentNo',
    label: '付款单号',
    type: 'text',
    placeholder: '请输入付款单号'
  },
  {
    key: 'transporter',
    label: '运输商',
    type: 'text',
    placeholder: '请输入运输商'
  },
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'amount',
    label: '付款金额',
    type: 'number',
    placeholder: '请输入付款金额'
  },
  {
    key: 'paymentDate',
    label: '付款日期',
    type: 'date'
  },
  {
    key: 'paymentMethod',
    label: '付款方式',
    type: 'select',
    options: [
      { value: '银行转账', label: '银行转账' },
      { value: '现金', label: '现金' },
      { value: '支票', label: '支票' },
      { value: '其他', label: '其他' }
    ]
  },
  {
    key: 'remark',
    label: '备注',
    type: 'textarea',
    placeholder: '请输入备注信息',
    rows: 3
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
        paymentNo: 'TP-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        transporter: '自动识别运输商',
        contractNo: 'TC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        amount: Math.floor(Math.random() * 100000).toString(),
        paymentDate: new Date().toISOString().split('T')[0],
        paymentMethod: '银行转账',
        remark: '系统自动识别'
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
      router.push('/finance/transport-payment/manage');
      resolve(true);
    }, 500);
  });
};
</script>

