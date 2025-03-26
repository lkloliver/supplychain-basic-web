<template>
  <DocumentUploadTemplate
    title="运输合同上传"
    detailsTitle="合同详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/transport/upload"
    manageRouteName="/dashboard/contract/transport/manage"
    :detailFields="detailFields"
    :onSubmit="handleSubmit"
    :onAIRecognize="handleRecognize"
    :customComponents="customComponents"
  />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import { mockTransportContractRecognition } from '@/mocks/contract/transport';
import TransportDetailsTable from '@/components/detail-components/TransportDetailsTable.vue';
import TransportDetailsInput from '@/components/upload-components/TransportDetailsInput.vue';

const router = useRouter();

const customComponents = {
  'transport-details-table': TransportDetailsTable,
  'transport-details-input': TransportDetailsInput
};

// 详情字段配置
const detailFields = [
  {
    key: 'carrier',
    label: '承运方信息',
    type: 'text',
    required: true
  },
  {
    key: 'shipper',
    label: '托运方信息',
    type: 'text',
    required: true
  },
  {
    key: 'startTime',
    label: '合同起始时间',
    type: 'date',
    required: true
  },
  {
    key: 'endTime',
    label: '合同截止时间',
    type: 'date',
    required: true
  },
  {
    key: 'signDate',
    label: '签订日期',
    type: 'date',
    required: true
  },
  {
    key: 'handler',
    label: '经办人',
    type: 'text',
    required: true
  },
  {
    key: 'transportMethod',
    label: '运输方式',
    type: 'select',
    options: [
      { label: '公路运输', value: '公路运输' },
      { label: '铁路运输', value: '铁路运输' },
      { label: '水路运输', value: '水路运输' },
      { label: '航空运输', value: '航空运输' }
    ],
    required: true
  },
  {
    key: 'transportDetails',
    label: '运输明细',
    type: 'array',
    itemLabel: '运输路线',
    fields: [
      {
        key: 'startLocation',
        label: '起运地',
        type: 'text',
        required: true
      },
      {
        key: 'endLocation',
        label: '目的地',
        type: 'text',
        required: true
      },
      {
        key: 'category',
        label: '名称/品类',
        type: 'text',
        required: true
      },
      {
        key: 'unitPrice',
        label: '含税运输单价',
        type: 'number',
        required: true
      }
    ]
  }
];

// 处理提交
const handleSubmit = async (formData: any) => {
  console.log('提交运输合同数据', formData);
  return true;
};

// 处理AI识别
const handleRecognize = async (file: File) => {
  console.log('识别运输合同文件', file);
  await new Promise(resolve => setTimeout(resolve, 1500));
  return mockTransportContractRecognition;
};
</script>

