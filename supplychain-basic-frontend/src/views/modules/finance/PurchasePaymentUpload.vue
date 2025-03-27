<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="采购付款上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/finance/purchase-payment/upload"
      manageRouteName="/dashboard/finance/purchase-payment/manage"
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
            <label class="form-label">转账记录编号</label>
            <input 
              v-model="detailForm.paymentNo" 
              type="text" 
              placeholder="请输入转账记录编号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">付款方式</label>
            <select 
              v-model="detailForm.paymentMethod" 
              class="form-input"
            >
              <option value="">请选择付款方式</option>
              <option value="bank">银行转账</option>
              <option value="cash">现金</option>
              <option value="check">支票</option>
              <option value="other">其他</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">付款人姓名</label>
            <input 
              v-model="detailForm.payerName" 
              type="text" 
              placeholder="请输入付款人姓名"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">付款人联系方式</label>
            <input 
              v-model="detailForm.payerContact" 
              type="text" 
              placeholder="请输入付款人联系方式"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">收款人姓名</label>
            <input 
              v-model="detailForm.payeeName" 
              type="text" 
              placeholder="请输入收款人姓名"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">收款人联系方式</label>
            <input 
              v-model="detailForm.payeeContact" 
              type="text" 
              placeholder="请输入收款人联系方式"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">付款金额(元)</label>
            <input 
              v-model="detailForm.amount" 
              type="number" 
              step="0.01"
              placeholder="请输入付款金额"
              class="form-input"
            />
          </div>

          <div class="form-group full-width">
            <label class="form-label">备注</label>
            <textarea 
              v-model="detailForm.remark" 
              placeholder="请输入备注信息"
              class="form-input"
              rows="3"
            ></textarea>
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
  paymentNo: string;
  paymentMethod: string;
  payerName: string;
  payerContact: string;
  payeeName: string;
  payeeContact: string;
  amount: number;
  remark: string;
}

// 表单数据
const detailForm = ref<DetailForm>({
  paymentNo: '',
  paymentMethod: '',
  payerName: '',
  payerContact: '',
  payeeName: '',
  payeeContact: '',
  amount: 0,
  remark: ''
});

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'purchase_payment');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'purchase_payment'
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

