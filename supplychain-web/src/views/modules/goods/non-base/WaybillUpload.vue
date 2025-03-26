<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="运单上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/goods/non-base/waybill/upload"
      manageRouteName="/dashboard/goods/non-base/waybill/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">运单号</label>
            <input 
              v-model="detailForm.waybillNo" 
              type="text" 
              placeholder="请输入运单号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">起运地</label>
            <input 
              v-model="detailForm.origin" 
              type="text" 
              placeholder="请输入起运地"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">目的地</label>
            <input 
              v-model="detailForm.destination" 
              type="text" 
              placeholder="请输入目的地"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">品名/种类</label>
            <input 
              v-model="detailForm.productName" 
              type="text" 
              placeholder="请输入品名/种类"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">运输重量(kg)</label>
            <input 
              v-model="detailForm.transportWeight" 
              type="number" 
              step="0.01"
              placeholder="请输入运输重量"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">承运者</label>
            <input 
              v-model="detailForm.carrier" 
              type="text" 
              placeholder="请输入承运者"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">运输方式</label>
            <select v-model="detailForm.transportMethod" class="form-input">
              <option value="">请选择运输方式</option>
              <option value="road">公路运输</option>
              <option value="railway">铁路运输</option>
              <option value="waterway">水路运输</option>
              <option value="air">航空运输</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">运单日期</label>
            <input 
              v-model="detailForm.waybillDate" 
              type="date" 
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
  waybillNo: string;
  origin: string;
  destination: string;
  productName: string;
  transportWeight: number;
  carrier: string;
  transportMethod: string;
  waybillDate: string;
}

// 表单数据
const detailForm = ref<DetailForm>({
  waybillNo: '',
  origin: '',
  destination: '',
  productName: '',
  transportWeight: 0,
  carrier: '',
  transportMethod: '',
  waybillDate: ''
});

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'waybill');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'waybill'
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
  
  