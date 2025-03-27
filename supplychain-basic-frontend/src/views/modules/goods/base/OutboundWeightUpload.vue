<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="出库过磅单上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/goods/base/outbound-weight/upload"
      manageRouteName="/dashboard/goods/base/outbound-weight/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">计量号</label>
            <input 
              v-model="detailForm.measurementNo" 
              type="text" 
              placeholder="请输入计量号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">毛重(kg)</label>
            <input 
              v-model="detailForm.grossWeight" 
              type="number" 
              step="0.01"
              placeholder="请输入毛重"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">皮重(kg)</label>
            <input 
              v-model="detailForm.tareWeight" 
              type="number" 
              step="0.01"
              placeholder="请输入皮重"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">净重(kg)</label>
            <input 
              v-model="detailForm.netWeight" 
              type="number" 
              step="0.01"
              placeholder="请输入净重"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">放射性检测</label>
            <select v-model="detailForm.radiationTest" class="form-input">
              <option value="">请选择检测结果</option>
              <option value="pass">合格</option>
              <option value="fail">不合格</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">备注</label>
            <textarea 
              v-model="detailForm.remarks" 
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
import { ref, computed, watch } from 'vue';
import { SparklesIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import '@/assets/styles/form.css';

interface DetailForm {
  measurementNo: string;
  grossWeight: number;
  tareWeight: number;
  netWeight: number;
  radiationTest: string;
  remarks: string;
}

// 表单数据
const detailForm = ref<DetailForm>({
  measurementNo: '',
  grossWeight: 0,
  tareWeight: 0,
  netWeight: 0,
  radiationTest: '',
  remarks: ''
});

// 自动计算净重
const calculateNetWeight = computed(() => {
  return detailForm.value.grossWeight - detailForm.value.tareWeight;
});

// 监听毛重和皮重的变化，自动更新净重
watch(
  [() => detailForm.value.grossWeight, () => detailForm.value.tareWeight],
  () => {
    detailForm.value.netWeight = calculateNetWeight.value;
  }
);

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'outbound_weight');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'outbound_weight'
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
    
    