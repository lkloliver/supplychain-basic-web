<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="出库图片上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/goods/base/outbound-image/upload"
      manageRouteName="/dashboard/goods/base/outbound-image/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <div class="form-grid">
              <div class="form-group">
            <label class="form-label">车牌号</label>
            <input 
              v-model="detailForm.vehicleNo" 
              type="text" 
              placeholder="请输入车牌号"
              class="form-input"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">司机姓名</label>
                    <input 
              v-model="detailForm.driverName" 
              type="text" 
              placeholder="请输入司机姓名"
              class="form-input"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">司机电话</label>
            <input 
              v-model="detailForm.driverPhone" 
              type="tel" 
              placeholder="请输入司机电话"
              class="form-input"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">司机身份证号</label>
            <input 
              v-model="detailForm.driverIdCard" 
              type="text" 
              placeholder="请输入司机身份证号"
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
  vehicleNo: string;
  driverName: string;
  driverPhone: string;
  driverIdCard: string;
}

// 表单数据
const detailForm = ref<DetailForm>({
  vehicleNo: '',
  driverName: '',
  driverPhone: '',
  driverIdCard: ''
});

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'outbound_image');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'outbound_image'
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
  
  