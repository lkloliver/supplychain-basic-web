<template>
  <DocumentUploadTemplate
    title="采购合同"
    detailsTitle="合同详情"
    backRouteName="DashboardHome"
    uploadRouteName="/dashboard/contract/purchase/upload"
    manageRouteName="/dashboard/contract/purchase/manage"
    :detailFields="detailFields"
    :onSubmit="handleSubmit"
    :onAIRecognize="handleAIRecognize"
    :customComponents="customComponents"
  />
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
  import GoodsTableInput from '@/components/upload-components/GoodsTableInput.vue';
  
  const router = useRouter();
  
  // 注册自定义组件
  const customComponents = {
    'goods-table-input': GoodsTableInput
  };
  
  // 定义表单字段
  const detailFields = [
    {
      key: 'contractNo',
      label: '合同编号',
      type: 'text',
      placeholder: '请输入合同编号'
    },
    {
      key: 'supplier',
      label: '供应商',
      type: 'text',
      placeholder: '请输入供应商'
    },
    {
      key: 'amount',
      label: '合同金额',
      type: 'number',
      placeholder: '请输入合同金额'
    },
    {
      key: 'startDate',
      label: '开始日期',
      type: 'date'
    },
    {
      key: 'endDate',
      label: '结束日期',
      type: 'date'
    },
    // 使用自定义表格组件输入货物信息
    {
      key: 'goods',
      label: '货物信息',
      customComponent: 'goods-table-input',
      props: {
        columns: [
          { key: 'name', label: '货物名称', width: '20%' },
          { key: 'specification', label: '规格', width: '15%' },
          { key: 'unit', label: '单位', width: '10%' },
          { key: 'quantity', label: '数量', type: 'number', width: '15%', sum: true },
          { key: 'unitPrice', label: '单价', type: 'number', width: '15%' },
          { key: 'amount', label: '金额', type: 'number', width: '15%', sum: true }
        ],
        rowTemplate: {
          name: '',
          specification: '',
          unit: '',
          quantity: 0,
          unitPrice: 0,
          amount: 0
        },
        showFooter: true,
        showImportBtn: true
      }
    },
    // 可以添加备注等其他字段
    {
      key: 'remarks',
      label: '备注',
      type: 'textarea',
      rows: 3,
      placeholder: '请输入合同备注信息'
    }
  ];
  
  // AI自动识别文档
  const handleAIRecognize = async (file) => {
    // 模拟AI识别过程
    console.log('正在识别文档...', file.name);
  
    return new Promise((resolve) => {
      setTimeout(() => {
        // 模拟识别结果
        const result = {
          contractNo: 'PC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
          supplier: '自动识别供应商',
          amount: Math.floor(Math.random() * 1000000).toString(),
          startDate: new Date().toISOString().split('T')[0],
          endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
          // 识别的货物数据
          goods: [
            {
              name: '自动识别货物1',
              specification: '规格A',
              unit: '个',
              quantity: 100,
              unitPrice: 50,
              amount: 5000
            },
            {
              name: '自动识别货物2',
              specification: '规格B',
              unit: '箱',
              quantity: 20,
              unitPrice: 200,
              amount: 4000
            }
          ],
          remarks: '此合同由AI自动识别生成，请核对信息准确性。'
        };
        
        alert('文档识别完成');
        resolve(result);
      }, 1500);
    });
  };
  
  // 提交上传
  const handleSubmit = async (formData) => {
    // 这里可以调用API上传文件和数据
    console.log('提交上传', formData);
  
    // 模拟上传成功
    return new Promise((resolve) => {
      setTimeout(() => {
        alert('上传成功');
        // 上传成功后跳转到管理页面
        router.push({ name: 'PurchaseContractManage' });
        resolve(true);
      }, 500);
    });
  };
  </script>
  
  