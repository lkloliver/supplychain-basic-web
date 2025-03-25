<template>
  <DocumentUploadTemplate
    title="采购台账上传"
    detailsTitle="台账详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/ledger/purchase/upload"
    manageRouteName="/dashboard/ledger/purchase/manage"
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
    key: 'ledgerNo',
    label: '台账编号',
    type: 'text',
    placeholder: '请输入台账编号'
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
    key: 'businessDate',
    label: '业务日期',
    type: 'date'
  },
  {
    key: 'businessType',
    label: '业务类型',
    type: 'select',
    options: [
      { value: 'purchase', label: '采购入库' },
      { value: 'return', label: '采购退货' },
      { value: 'adjustment', label: '价格调整' }
    ]
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
    key: 'deliveryStatus',
    label: '交货状态',
    type: 'select',
    options: [
      { value: 'undelivered', label: '未交货' },
      { value: 'partialDelivered', label: '部分交货' },
      { value: 'delivered', label: '已交货' }
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
        ledgerNo: 'PL-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'PC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        supplier: '自动识别供应商',
        businessDate: new Date().toISOString().split('T')[0],
        businessType: 'purchase',
        goodsName: '钢材',
        specification: 'Q235 20*2000*6000',
        quantity: Math.floor(Math.random() * 1000 + 100),
        unit: '吨',
        unitPrice: Math.floor(Math.random() * 5000 + 2000),
        totalAmount: Math.floor(Math.random() * 5000000 + 1000000),
        taxRate: 13,
        taxAmount: Math.floor(Math.random() * 500000 + 100000),
        paymentStatus: 'unpaid',
        deliveryStatus: 'undelivered',
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

const handleSubmit = async (formData: { name: string, file: File, details: Record<string, any> }) => {
  try {
    console.log('提交采购台账数据', formData);
    await new Promise(resolve => setTimeout(resolve, 1000));
    alert('上传成功');
    router.push('/ledger/purchase/manage');
  } catch (error: any) {
    console.error('上传失败:', error);
    alert('上传失败: ' + error.message);
  }
};
</script>
  
  