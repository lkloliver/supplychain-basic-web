<template>
  <DocumentUploadTemplate
    title="销售收款上传"
    detailsTitle="收款详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/finance/sales-receipt/upload"
    manageRouteName="/dashboard/finance/sales-receipt/manage"
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
    key: 'receiptNo',
    label: '收款单号',
    type: 'text',
    placeholder: '请输入收款单号'
  },
  {
    key: 'customer',
    label: '客户',
    type: 'text',
    placeholder: '请输入客户'
  },
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'amount',
    label: '收款金额',
    type: 'number',
    placeholder: '请输入收款金额'
  },
  {
    key: 'receiptDate',
    label: '收款日期',
    type: 'date'
  },
  {
    key: 'receiptMethod',
    label: '收款方式',
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
        receiptNo: 'SR-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        customer: '自动识别客户',
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        amount: Math.floor(Math.random() * 1000000).toString(),
        receiptDate: new Date().toISOString().split('T')[0],
        receiptMethod: '银行转账',
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
      router.push('/finance/sales-receipt/manage');
      resolve(true);
    }, 500);
  });
};
</script>

