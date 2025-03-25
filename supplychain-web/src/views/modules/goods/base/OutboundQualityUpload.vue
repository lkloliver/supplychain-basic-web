<template>
  <DocumentUploadTemplate
    title="出库质检上传"
    detailsTitle="质检详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/base/outbound-quality/upload"
    manageRouteName="/dashboard/goods/base/outbound-quality/manage"
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
    key: 'qualityNo',
    label: '质检单号',
    type: 'text',
    placeholder: '请输入质检单号'
  },
  {
    key: 'weightNo',
    label: '过磅单号',
    type: 'text',
    placeholder: '请输入关联过磅单号'
  },
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'moisture',
    label: '水分(%)',
    type: 'number',
    placeholder: '请输入水分含量'
  },
  {
    key: 'impurity',
    label: '杂质(%)',
    type: 'number',
    placeholder: '请输入杂质含量'
  },
  {
    key: 'protein',
    label: '蛋白质(%)',
    type: 'number',
    placeholder: '请输入蛋白质含量'
  },
  {
    key: 'qualityTime',
    label: '质检时间',
    type: 'datetime'
  },
  {
    key: 'inspector',
    label: '质检员',
    type: 'text',
    placeholder: '请输入质检员'
  },
  {
    key: 'result',
    label: '质检结果',
    type: 'select',
    options: [
      { value: 'pass', label: '合格' },
      { value: 'fail', label: '不合格' },
      { value: 'conditional', label: '有条件通过' }
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

// 处理AI识别
const handleRecognize = async (file: File) => {
  console.log('正在识别文档...', file.name);
  
  return new Promise((resolve) => {
    setTimeout(() => {
      // 模拟识别结果
      const result = {
        qualityNo: 'OQ-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        weightNo: 'OW-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        moisture: (Math.random() * 5 + 13).toFixed(2),
        impurity: (Math.random() * 2 + 1).toFixed(2),
        protein: (Math.random() * 5 + 30).toFixed(2),
        qualityTime: new Date().toISOString(),
        inspector: '自动识别质检员',
        result: 'pass',
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

// 处理提交
const handleSubmit = (formData: { name: string, file: File, details: Record<string, any> }) => {
  console.log('提交出库质检数据', formData);
  
  // 模拟上传成功
  alert('上传成功');
  
  // 上传成功后跳转到管理页面
  router.push({ name: 'OutboundQualityManage' });
};
</script>
    
    