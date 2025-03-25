<template>
  <DocumentUploadTemplate
    title="到货厂家过磅上传"
    detailsTitle="过磅详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/factory-weight/upload"
    manageRouteName="/dashboard/goods/non-base/factory-weight/manage"
    :detailFields="detailFields"
    :onSubmit="handleSubmit"
    :onAIRecognize="handleRecognize"
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
    key: 'weightNo',
    label: '过磅单号',
    type: 'text',
    placeholder: '请输入过磅单号'
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
    key: 'vehicleNo',
    label: '车牌号',
    type: 'text',
    placeholder: '请输入车牌号'
  },
  {
    key: 'factory',
    label: '厂家名称',
    type: 'text',
    placeholder: '请输入厂家名称'
  },
  {
    key: 'grossWeight',
    label: '毛重(kg)',
    type: 'number',
    placeholder: '请输入毛重'
  },
  {
    key: 'tareWeight',
    label: '皮重(kg)',
    type: 'number',
    placeholder: '请输入皮重'
  },
  {
    key: 'netWeight',
    label: '净重(kg)',
    type: 'number',
    placeholder: '请输入净重'
  },
  {
    key: 'weightTime',
    label: '过磅时间',
    type: 'datetime'
  },
  {
    key: 'operator',
    label: '操作员',
    type: 'text',
    placeholder: '请输入操作员'
  },
  {
    key: 'weightType',
    label: '过磅类型',
    type: 'select',
    options: [
      { value: 'first', label: '第一次过磅' },
      { value: 'second', label: '第二次过磅' },
      { value: 'once', label: '一次过磅' }
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
        weightNo: 'FW-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        waybillNo: 'SW-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        vehicleNo: '京A' + Math.floor(Math.random() * 10000).toString().padStart(5, '0'),
        factory: '自动识别厂家',
        grossWeight: Math.floor(Math.random() * 50000 + 30000),
        tareWeight: Math.floor(Math.random() * 10000 + 5000),
        netWeight: Math.floor(Math.random() * 40000 + 20000),
        weightTime: new Date().toISOString(),
        operator: '自动识别操作员',
        weightType: 'first',
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

// 处理提交
const handleSubmit = async (formData: { name: string, file: File, details: Record<string, any> }) => {
  try {
    console.log('提交厂家过磅数据', formData);
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // 模拟上传成功
    alert('上传成功');
    
    // 上传成功后跳转到管理页面
    router.push('/dashboard/goods/non-base/factory-weight/manage');
  } catch (error) {
    console.error('上传失败:', error);
    alert('上传失败: ' + error.message);
  }
};
</script>
  
  