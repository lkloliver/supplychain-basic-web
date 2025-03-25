<template>
  <DocumentUploadTemplate
    title="销售发票上传"
    detailsTitle="发票详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/invoice/sales/upload"
    manageRouteName="/dashboard/invoice/sales/manage"
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
    key: 'invoiceNo',
    label: '发票编号',
    type: 'text',
    placeholder: '请输入发票编号'
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
    label: '发票金额',
    type: 'number',
    placeholder: '请输入发票金额'
  },
  {
    key: 'taxRate',
    label: '税率',
    type: 'number',
    placeholder: '请输入税率'
  },
  {
    key: 'taxAmount',
    label: '税额',
    type: 'number',
    placeholder: '请输入税额'
  },
  {
    key: 'invoiceDate',
    label: '开票日期',
    type: 'date'
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
        invoiceNo: 'SI-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        customer: '自动识别客户',
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        amount: Math.floor(Math.random() * 100000).toString(),
        taxRate: '13%',
        taxAmount: Math.floor(Math.random() * 10000).toString(),
        invoiceDate: new Date().toISOString().split('T')[0],
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

// 处理提交
const handleSubmit = (formData: { name: string, file: File, details: Record<string, any> }) => {
  console.log('提交销售发票数据', formData);
  
  // 模拟上传成功
  alert('上传成功');
  
  // 上传成功后跳转到管理页面
  router.push({ name: 'SalesInvoiceManage' });
};
</script>
  
  