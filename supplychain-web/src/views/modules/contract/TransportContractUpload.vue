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
const detailFields = ref([
  {
    id: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号',
    value: ''
  },
  {
    id: 'transporter',
    label: '运输商',
    type: 'text',
    placeholder: '请输入运输商',
    value: ''
  },
  {
    id: 'route',
    label: '运输路线',
    type: 'text',
    placeholder: '请输入运输路线',
    value: ''
  },
  {
    id: 'quantity',
    label: '运输量',
    type: 'number',
    placeholder: '请输入运输量',
    value: ''
  },
  {
    id: 'unitPrice',
    label: '单价',
    type: 'number',
    placeholder: '请输入单价',
    value: ''
  },
  {
    id: 'amount',
    label: '合同金额',
    type: 'number',
    placeholder: '请输入合同金额',
    value: ''
  },
  {
    id: 'startDate',
    label: '开始日期',
    type: 'date',
    value: ''
  },
  {
    id: 'endDate',
    label: '结束日期',
    type: 'date',
    value: ''
  }
]);

// 处理AI识别
const handleRecognize = (file: File) => {
  console.log('正在识别文档...', file.name);
  
  // 模拟AI识别过程
  setTimeout(() => {
    // 更新字段值
    detailFields.value = detailFields.value.map(field => {
      if (field.id === 'contractNo') {
        return { ...field, value: 'TC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0') };
      } else if (field.id === 'transporter') {
        return { ...field, value: '自动识别运输商' };
      } else if (field.id === 'route') {
        return { ...field, value: '北京-上海' };
      } else if (field.id === 'quantity') {
        return { ...field, value: Math.floor(Math.random() * 2000).toString() };
      } else if (field.id === 'unitPrice') {
        return { ...field, value: (Math.random() * 5 + 1).toFixed(2) };
      } else if (field.id === 'amount') {
        return { ...field, value: Math.floor(Math.random() * 500000).toString() };
      } else if (field.id === 'startDate') {
        return { ...field, value: new Date().toISOString().split('T')[0] };
      } else if (field.id === 'endDate') {
        return { ...field, value: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0] };
      }
      return field;
    });
    
    alert('文档识别完成');
  }, 1500);
};

// 处理表单提交
const handleSubmit = (formData: { name: string, file: File, details: Record<string, any> }) => {
  console.log('提交上传', formData);
  
  // 模拟上传成功
  alert('上传成功');
  
  // 上传成功后跳转到管理页面
  router.push({ name: 'TransportContractManage' });
};
</script>

