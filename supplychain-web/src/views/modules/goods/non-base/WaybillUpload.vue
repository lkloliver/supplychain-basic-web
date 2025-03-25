<template>
  <DocumentUploadTemplate
    title="运单上传"
    detailsTitle="运单详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/goods/non-base/waybill/upload"
    manageRouteName="/dashboard/goods/non-base/waybill/manage"
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
    key: 'transportCompany',
    label: '运输公司',
    type: 'text',
    placeholder: '请输入运输公司'
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
    key: 'estimatedArrivalTime',
    label: '预计到达时间',
    type: 'datetime'
  },
  {
    key: 'goodsName',
    label: '货物名称',
    type: 'text',
    placeholder: '请输入货物名称'
  },
  {
    key: 'goodsWeight',
    label: '货物重量(kg)',
    type: 'number',
    placeholder: '请输入货物重量'
  },
  {
    key: 'freightCost',
    label: '运费(元)',
    type: 'number',
    placeholder: '请输入运费'
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
        waybillNo: 'WB-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        vehicleNo: '京A' + Math.floor(Math.random() * 10000).toString().padStart(5, '0'),
        driver: '自动识别司机',
        driverPhone: '13' + Math.floor(Math.random() * 1000000000).toString().padStart(9, '0'),
        transportCompany: '自动识别运输公司',
        loadingPlace: '自动识别装货地',
        unloadingPlace: '自动识别卸货地',
        loadingTime: new Date().toISOString(),
        estimatedArrivalTime: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
        goodsName: '钢材',
        goodsWeight: Math.floor(Math.random() * 30000 + 10000),
        freightCost: Math.floor(Math.random() * 5000 + 2000),
        remark: '自动识别备注'
      };
      
      alert('文档识别完成');
      resolve(result);
    }, 1500);
  });
};

const handleSubmit = async (formData: { name: string, file: File, details: Record<string, any> }) => {
  try {
    console.log('提交运单数据', formData);
    await new Promise(resolve => setTimeout(resolve, 1000));
    alert('上传成功');
    router.push('/dashboard/goods/non-base/waybill/manage');
  } catch (error: any) {
    console.error('上传失败:', error);
    alert('上传失败: ' + error.message);
  }
};
</script>
  
  