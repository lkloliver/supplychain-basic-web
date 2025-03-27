<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="运输收票上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/invoice/transport/upload"
      manageRouteName="/dashboard/invoice/transport/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <!-- 基本信息 -->
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">托运方名称</label>
            <input 
              v-model="detailForm.shipperName" 
              type="text" 
              placeholder="请输入托运方名称"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">托运方纳税人识别号</label>
            <input 
              v-model="detailForm.shipperTaxNo" 
              type="text" 
              placeholder="请输入托运方纳税人识别号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">承运方名称</label>
            <input 
              v-model="detailForm.carrierName" 
              type="text" 
              placeholder="请输入承运方名称"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">承运方纳税人识别号</label>
            <input 
              v-model="detailForm.carrierTaxNo" 
              type="text" 
              placeholder="请输入承运方纳税人识别号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">金额(元)</label>
            <input 
              v-model="detailForm.amount" 
              type="number" 
              step="0.01"
              placeholder="请输入金额"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">税率(%)</label>
            <input 
              v-model="detailForm.taxRate" 
              type="number" 
              step="0.01"
              placeholder="请输入税率"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">税额(元)</label>
            <input 
              v-model="detailForm.taxAmount" 
              type="number" 
              step="0.01"
              placeholder="请输入税额"
              class="form-input"
            />
          </div>
        </div>

        <!-- 按钮组 -->
        <div class="form-actions">
          <button type="button" class="action-btn secondary" @click="handleAIAutoFill">
            <SparklesIcon class="btn-icon" />
            AI自动识别
          </button>
          <button type="submit" class="action-btn primary">
            提交详情信息
          </button>
        </div>
      </form>
    </DocumentUploadTemplate>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { SparklesIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import '@/assets/styles/form.css';

interface DetailForm {
  shipperName: string;
  shipperTaxNo: string;
  carrierName: string;
  carrierTaxNo: string;
  amount: number;
  taxRate: number;
  taxAmount: number;
}

// 表单数据
const detailForm = ref<DetailForm>({
  shipperName: '',
  shipperTaxNo: '',
  carrierName: '',
  carrierTaxNo: '',
  amount: 0,
  taxRate: 0,
  taxAmount: 0
});

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'transport_invoice');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'transport_invoice'
      }
    };
    return result;
  } catch (error) {
    throw error;
  }
}

// 处理上传成功
const handleUploadSuccess = (result: any) => {
  if (result.success && result.fileId) {
    // 文件上传成功后，模板组件会自动切换到下一步
  }
}

// 处理取消上传
const handleCancelUpload = async () => {
  // TODO: 调用删除接口
  console.log('取消上传');
}

// AI自动识别
const handleAIAutoFill = async () => {
  try {
    // TODO: 调用AI识别接口
    console.log('开始AI自动识别...');
  } catch (error) {
    console.error('AI识别失败：', error);
  }
}

// 提交详情信息
const handleDetailSubmit = async () => {
  try {
    // TODO: 调用API保存详情信息
    console.log('提交详情信息：', detailForm.value);
  } catch (error) {
    console.error('提交详情信息失败：', error);
  }
};
</script>
  
  