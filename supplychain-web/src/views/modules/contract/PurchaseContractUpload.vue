<template>
  <div class="contract-upload">
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
    >
      <!-- 添加反向开票同意书卡片 -->
      <template #after-details>
        <div class="form-card">
          <h3 class="form-card-title">反向开票同意书</h3>
          <div class="form-group">
            <div class="file-upload-box">
              <input 
                type="file" 
                id="invoice-agreement" 
                @change="handleInvoiceAgreementChange" 
                accept=".pdf,.doc,.docx"
                class="file-input" 
              />
              <label for="invoice-agreement" class="file-upload-label">
                <UploadCloudIcon class="upload-icon" />
                <span>{{ invoiceAgreementName || '请上传反向开票同意书' }}</span>
              </label>
            </div>
          </div>
        </div>
      </template>
    </DocumentUploadTemplate>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { UploadCloudIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import GoodsTableInput from '@/components/upload-components/GoodsTableInput.vue';

const router = useRouter();

// 反向开票同意书文件
const invoiceAgreement = ref<File | null>(null);
const invoiceAgreementName = ref('');

// 处理反向开票同意书上传
const handleInvoiceAgreementChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files.length > 0) {
    invoiceAgreement.value = input.files[0];
    invoiceAgreementName.value = input.files[0].name;
  }
};

// 注册自定义组件
const customComponents = {
  'goods-table-input': GoodsTableInput
};

// 定义表单字段
const detailFields = [
  
  {
    key: 'contractNo',
    label: '采购合同编号',
    type: 'text',
    placeholder: '请输入合同编号',
    required: true
  },
  {
    key: 'signDate', 
    label: '签订时间',
    type: 'date',
    required: true
  },
  {
    key: 'buyer',
    label: '买方信息',
    type: 'text',
    placeholder: '请输入买方信息',
    required: true
  },
  {
    key: 'seller',
    label: '卖方信息', 
    type: 'text',
    placeholder: '请输入卖方信息',
    required: true
  },
  {
    key: 'paymentAccount',
    label: '付款账户',
    type: 'text', 
    placeholder: '请输入付款账户'
  },
  {
    key: 'receivingAccount',
    label: '收款账户',
    type: 'text',
    placeholder: '请输入收款账户'
  },
  {
    key: 'taxRate',
    label: '税率',
    type: 'number',
    placeholder: '请输入税率(%)'
  },
  {
    key: 'invoiceType',
    label: '发票类型',
    type: 'select',
    options: [
      { label: '增值税专用发票', value: 'special' },
      { label: '增值税普通发票', value: 'normal' }
    ]
  },
  {
    key: 'paymentDate',
    label: '付款时间',
    type: 'date'
  },
  {
    key: 'paymentMethod',
    label: '付款方式',
    type: 'select',
    options: [
      { label: '现金', value: 'cash' },
      { label: '银行转账', value: 'transfer' },
      { label: '支票', value: 'check' }
    ]
  },
  {
    key: 'performancePeriod',
    label: '履约期限',
    type: 'text',
    placeholder: '请输入履约期限'
  },
  {
    key: 'performanceLocation',
    label: '履约地点',
    type: 'text',
    placeholder: '请输入履约地点'
  },
  {
    key: 'performanceMethod',
    label: '履约方式',
    type: 'text',
    placeholder: '请输入履约方式'
  },
  {
    key: 'handler',
    label: '经办人信息',
    type: 'text',
    placeholder: '请输入经办人信息'
  },
  {
    key: 'totalAmount',
    label: '合同金额',
    type: 'number',
    placeholder: '请输入合同金额',
    required: true
  },
 
  {
    key: 'goods',
    label: '货物明细',
    customComponent: 'goods-table-input',
    props: {
      columns: [
        { key: 'name', label: '货物名称', width: '25%' },
        { key: 'quantity', label: '货物数量', type: 'number', width: '20%' },
        { key: 'price', label: '货物价格', type: 'number', width: '20%' },
        { key: 'specification', label: '规格型号', width: '25%' }
      ],
      rowTemplate: {
        name: '',
        quantity: 0,
        price: 0,
        specification: ''
      }
    },
    required: true
  }, 
    // 可以添加备注等其他字段
    {
      key: 'remarks',
      label: '备注',
      type: 'textarea',
      rows: 3,
      placeholder: '请输入合同备注信息'
    },
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
    if (!invoiceAgreement.value) {
      alert('请上传反向开票同意书');
      return;
    }

    // 将反向开票同意书添加到表单数据中
    const submitData = {
      ...formData,
      invoiceAgreement: invoiceAgreement.value
    };

    // 这里可以调用API上传文件和数据
    console.log('提交上传', submitData);
  
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

<style scoped>
.contract-upload {
  /* 如果需要额外的样式 */
}

/* 复用 DocumentUploadTemplate 的样式 */
.form-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 20px;
  margin-top: 24px;
}

.form-card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-top: 0;
  margin-bottom: 16px;
}

.file-upload-box {
  position: relative;
}

.file-input {
  position: absolute;
  width: 0;
  height: 0;
  opacity: 0;
}

.file-upload-label {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border: 1px dashed #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.file-upload-label:hover {
  border-color: #1e88e5;
}

.upload-icon {
  width: 20px;
  height: 20px;
  color: #1e88e5;
}
</style>
  
  