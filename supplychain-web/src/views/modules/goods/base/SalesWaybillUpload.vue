<template>
  <DocumentUploadTemplate
    title="销售运单上传"
    detailsTitle="运单详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/base/sales-waybill/upload"
    manageRouteName="/dashboard/goods/base/sales-waybill/manage"
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
    key: 'waybillNo',
    label: '运单编号',
    type: 'text',
    placeholder: '请输入运单编号'
  },
  {
    key: 'contractNo',
    label: '合同编号',
    type: 'text',
    placeholder: '请输入合同编号'
  },
  {
    key: 'weightNo',
    label: '过磅单号',
    type: 'text',
    placeholder: '请输入过磅单号'
  },
  {
    key: 'vehicleNo',
    label: '车牌号',
    type: 'text',
    placeholder: '请输入车牌号'
  },
  {
    key: 'driver',
    label: '司机姓名',
    type: 'text',
    placeholder: '请输入司机姓名'
  },
  {
    key: 'driverPhone',
    label: '司机电话',
    type: 'text',
    placeholder: '请输入司机电话'
  },
  {
    key: 'customer',
    label: '客户',
    type: 'text',
    placeholder: '请输入客户'
  },
  {
    key: 'loadingPlace',
    label: '装货地点',
    type: 'text',
    placeholder: '请输入装货地点'
  },
  {
    key: 'unloadingPlace',
    label: '卸货地点',
    type: 'text',
    placeholder: '请输入卸货地点'
  },
  {
    key: 'loadingTime',
    label: '装货时间',
    type: 'datetime'
  },
  {
    key: 'unloadingTime',
    label: '卸货时间',
    type: 'datetime'
  },
  {
    key: 'freightRate',
    label: '运费单价',
    type: 'number',
    placeholder: '请输入运费单价'
  },
  {
    key: 'freightAmount',
    label: '运费总额',
    type: 'number',
    placeholder: '请输入运费总额'
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
        waybillNo: 'SW-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        weightNo: 'W-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        vehicleNo: '京A' + Math.floor(Math.random() * 10000).toString().padStart(5, '0'),
        driver: '自动识别司机',
        driverPhone: '13' + Math.floor(Math.random() * 1000000000).toString().padStart(9, '0'),
        customer: '自动识别客户',
        loadingPlace: '自动识别装货地',
        unloadingPlace: '自动识别卸货地',
        loadingTime: new Date().toISOString(),
        unloadingTime: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
        freightRate: Math.floor(Math.random() * 100 + 50),
        freightAmount: Math.floor(Math.random() * 10000 + 5000),
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

// 处理提交
const handleSubmit = (formData: { name: string, file: File, details: Record<string, any> }) => {
  console.log('提交销售运单数据', formData);
  
  // 模拟上传成功
  alert('上传成功');
  
  // 上传成功后跳转到管理页面
  router.push({ name: 'SalesWaybillManage' });
};
</script>
  
  