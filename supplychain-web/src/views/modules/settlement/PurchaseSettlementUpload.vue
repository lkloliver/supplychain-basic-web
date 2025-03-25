<template>
  <DocumentUploadTemplate
    title="采购结算上传"
    detailsTitle="结算详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/settlement/purchase/upload"
    manageRouteName="/dashboard/settlement/purchase/manage"
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
    key: 'settlementNo',
    label: '结算单号',
    type: 'text',
    placeholder: '请输入结算单号'
  },
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'supplier',
    label: '供应商名称',
    type: 'text',
    placeholder: '请输入供应商名称'
  },
  {
    key: 'settlementDate',
    label: '结算日期',
    type: 'date'
  },
  {
    key: 'settlementPeriod',
    label: '结算期间',
    type: 'text',
    placeholder: '请输入结算期间'
  },
  {
    key: 'goodsName',
    label: '货物名称',
    type: 'text',
    placeholder: '请输入货物名称'
  },
  {
    key: 'specification',
    label: '规格型号',
    type: 'text',
    placeholder: '请输入规格型号'
  },
  {
    key: 'quantity',
    label: '数量',
    type: 'number',
    placeholder: '请输入数量'
  },
  {
    key: 'unit',
    label: '单位',
    type: 'text',
    placeholder: '请输入单位'
  },
  {
    key: 'unitPrice',
    label: '单价(元)',
    type: 'number',
    placeholder: '请输入单价'
  },
  {
    key: 'totalAmount',
    label: '总金额(元)',
    type: 'number',
    placeholder: '请输入总金额'
  },
  {
    key: 'taxRate',
    label: '税率(%)',
    type: 'number',
    placeholder: '请输入税率'
  },
  {
    key: 'taxAmount',
    label: '税额(元)',
    type: 'number',
    placeholder: '请输入税额'
  },
  {
    key: 'bankAccount',
    label: '收款账号',
    type: 'text',
    placeholder: '请输入收款账号'
  },
  {
    key: 'bankName',
    label: '开户行',
    type: 'text',
    placeholder: '请输入开户行'
  },
  {
    key: 'paymentMethod',
    label: '付款方式',
    type: 'select',
    options: [
      { value: 'bank', label: '银行转账' },
      { value: 'cash', label: '现金支付' },
      { value: 'check', label: '支票支付' }
    ]
  },
  {
    key: 'paymentStatus',
    label: '付款状态',
    type: 'select',
    options: [
      { value: 'unpaid', label: '未付款' },
      { value: 'partialPaid', label: '部分付款' },
      { value: 'paid', label: '已付款' }
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

const handleRecognize = async (file: File) => {
  console.log('正在识别文档...', file.name);
  
  return new Promise((resolve) => {
    setTimeout(() => {
      const result = {
        settlementNo: 'PS-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        supplier: '自动识别供应商',
        settlementDate: new Date().toISOString().split('T')[0],
        settlementPeriod: '2024年1月',
        goodsName: '钢材',
        specification: 'Q235 20*2000*6000',
        quantity: Math.floor(Math.random() * 1000 + 100),
        unit: '吨',
        unitPrice: Math.floor(Math.random() * 5000 + 2000),
        totalAmount: Math.floor(Math.random() * 5000000 + 1000000),
        taxRate: 13,
        taxAmount: Math.floor(Math.random() * 500000 + 100000),
        bankAccount: '622848' + Math.floor(Math.random() * 10000000000).toString().padStart(10, '0'),
        bankName: '中国银行北京分行',
        paymentMethod: 'bank',
        paymentStatus: 'unpaid',
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

const handleSubmit = async (formData: { name: string, file: File, details: Record<string, any> }) => {
  try {
    console.log('提交采购结算数据', formData);
    await new Promise(resolve => setTimeout(resolve, 1000));
    alert('上传成功');
    router.push('/settlement/purchase/manage');
  } catch (error: any) {
    console.error('上传失败:', error);
    alert('上传失败: ' + error.message);
  }
};
</script>
  
  