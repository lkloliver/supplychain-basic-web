<template>
  <DocumentUploadTemplate
    title="到货厂家质检上传"
    detailsTitle="质检详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/factory-quality/upload"
    manageRouteName="/dashboard/goods/non-base/factory-quality/manage"
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
    key: 'qualityNo',
    label: '质检单号',
    type: 'text',
    placeholder: '请输入质检单号'
  },
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'waybillNo',
    label: '运单编号',
    type: 'text',
    placeholder: '请输入运单编号'
  },
  {
    key: 'factory',
    label: '厂家名称',
    type: 'text',
    placeholder: '请输入厂家名称'
  },
  {
    key: 'material',
    label: '材料名称',
    type: 'text',
    placeholder: '请输入材料名称'
  },
  {
    key: 'specification',
    label: '规格型号',
    type: 'text',
    placeholder: '请输入规格型号'
  },
  {
    key: 'quantity',
    label: '检验数量',
    type: 'number',
    placeholder: '请输入检验数量'
  },
  {
    key: 'qualityResult',
    label: '质检结果',
    type: 'select',
    options: [
      { value: 'pass', label: '合格' },
      { value: 'fail', label: '不合格' },
      { value: 'conditional', label: '有条件通过' }
    ]
  },
  {
    key: 'inspector',
    label: '质检员',
    type: 'text',
    placeholder: '请输入质检员姓名'
  },
  {
    key: 'inspectTime',
    label: '检验时间',
    type: 'datetime'
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
        qualityNo: 'FQ-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        waybillNo: 'SW-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        factory: '自动识别厂家',
        material: '钢材',
        specification: 'Q235 20*2000*6000',
        quantity: Math.floor(Math.random() * 1000 + 100),
        qualityResult: 'pass',
        inspector: '自动识别质检员',
        inspectTime: new Date().toISOString(),
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

const handleSubmit = async (formData: { name: string, file: File, details: Record<string, any> }) => {
  try {
    console.log('提交厂家质检数据', formData);
    await new Promise(resolve => setTimeout(resolve, 1000));
    alert('上传成功');
    router.push('/dashboard/goods/non-base/factory-quality/manage');
  } catch (error: any) {
    console.error('上传失败:', error);
    alert('上传失败: ' + error.message);
  }
};
</script>
  
  