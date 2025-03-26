<template>
  <DocumentUploadTemplate
    title="销售合同上传"
    detailsTitle="合同详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/sales/upload"
    manageRouteName="/dashboard/contract/sales/manage"
    :detailFields="detailFields"
    :onSubmit="handleSubmit"
    :onAIRecognize="handleRecognize"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import { mockSalesContractRecognition } from '@/mocks/contract/sales';

const router = useRouter();

// 详情字段配置
const detailFields = [
  {
    key: 'contractNo',
    label: '销售合同编号',
    type: 'text',
    placeholder: '请输入合同编号',
    required: true
  },
  {
    key: 'signDate',
    label: '签订时间',
    type: 'date',
    required: true
  },
  {
    key: 'buyer',
    label: '买方信息',
    type: 'text',
    required: true
  },
  {
    key: 'seller',
    label: '卖方信息',
    type: 'text',
    required: true
  },
  {
    key: 'paymentAccount',
    label: '付款账户',
    type: 'text'
  },
  {
    key: 'receivingAccount',
    label: '收款账户',
    type: 'text'
  },
  {
    key: 'taxRate',
    label: '税率',
    type: 'text'
  },
  {
    key: 'invoiceType',
    label: '发票类型',
    type: 'select',
    options: [
      { label: '增值税专用发票', value: '增值税专用发票' },
      { label: '增值税普通发票', value: '增值税普通发票' }
    ]
  },
  {
    key: 'paymentDate',
    label: '付款时间',
    type: 'date'
  },
  {
    key: 'paymentMethod',
    label: '付款方式',
    type: 'text'
  },
  {
    key: 'performancePeriod',
    label: '履约期限',
    type: 'text'
  },
  {
    key: 'performanceLocation',
    label: '履约地点',
    type: 'text'
  },
  {
    key: 'performanceMethod',
    label: '履约方式',
    type: 'text'
  },
  {
    key: 'handler',
    label: '经办人信息',
    type: 'text'
  },
  {
    key: 'totalAmount',
    label: '合同金额',
    type: 'number',
    required: true
  },
  {
    key: 'remarks',
    label: '备注',
    type: 'textarea',
    rows: 3
  },
  {
    key: 'goods',
    label: '货物明细',
    type: 'array',
    itemLabel: '货物',
    fields: [
      {
        key: 'name',
        label: '货物名称',
        type: 'text',
        required: true
      },
      {
        key: 'quantity',
        label: '货物数量',
        type: 'number',
        required: true
      },
      {
        key: 'price',
        label: '货物价格',
        type: 'number',
        required: true
      },
      {
        key: 'specification',
        label: '规格型号',
        type: 'text'
      }
    ],
    itemTemplate: {
      name: '',
      quantity: 0,
      price: 0,
      specification: ''
    }
  }
];

// 处理提交
const handleSubmit = async (formData: any) => {
  console.log('提交销售合同数据', formData);
  
  // 模拟API调用
  await new Promise(resolve => setTimeout(resolve, 1000));
  return true;
};

// 处理AI识别
const handleRecognize = async (file: File) => {
  console.log('识别销售合同文件', file);
  
  // 模拟AI识别过程
  await new Promise(resolve => setTimeout(resolve, 1500));
  return mockSalesContractRecognition;
};
</script>

