<template>
  <DocumentUploadTemplate
    title="销售结算上传"
    detailsTitle="结算详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/settlement/sales/upload"
    manageRouteName="/dashboard/settlement/sales/manage"
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
    key: 'customer',
    label: '客户名称',
    type: 'text',
    placeholder: '请输入客户名称'
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
    key: 'receivableAccount',
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
    label: '收款方式',
    type: 'select',
    options: [
      { value: 'bank', label: '银行转账' },
      { value: 'cash', label: '现金收款' },
      { value: 'check', label: '支票收款' }
    ]
  },
  {
    key: 'paymentStatus',
    label: '收款状态',
    type: 'select',
    options: [
      { value: 'unpaid', label: '未收款' },
      { value: 'partialPaid', label: '部分收款' },
      { value: 'paid', label: '已收款' }
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
        settlementNo: 'SS-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        customer: '自动识别客户',
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
        receivableAccount: '622848' + Math.floor(Math.random() * 10000000000).toString().padStart(10, '0'),
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
    console.log('提交销售结算数据', formData);
    await new Promise(resolve => setTimeout(resolve, 1000));
    alert('上传成功');
    router.push('/settlement/sales/manage');
  } catch (error: any) {
    console.error('上传失败:', error);
    alert('上传失败: ' + error.message);
  }
};
</script>
  
  