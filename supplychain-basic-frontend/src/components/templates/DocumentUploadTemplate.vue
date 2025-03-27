<template>
  <div class="operation-wrapper">
    <div class="operation-header">
      <!-- 返回按钮 -->
      <div class="header-left">
        <router-link :to="{ name: backRouteName }" class="back-button">
          <ArrowLeftIcon class="back-icon" />
          <span>返回</span>
        </router-link>
        <h2 class="operation-title">{{ title }}</h2>
      </div>
      <!-- 模块视图切换 -->
      <div class="header-right">
        <ModuleViewToggle 
          :uploadRoute="uploadRouteName" 
          :manageRoute="manageRouteName" 
        />
      </div>
    </div>

    <!-- 步骤条 -->
    <div class="steps">
      <div 
        v-for="(step, index) in steps" 
        :key="index"
        :class="['step', { active: currentStep >= index }]"
        @click="goToStep(index)"
      >
        <div class="step-number">{{ index + 1 }}</div>
        <div class="step-title">{{ step.title }}</div>
      </div>
    </div>

    <div class="operation-content">
      <!-- 文件上传表单 -->
      <div v-show="currentStep === 0" class="form-card">
        <form @submit.prevent="handleSubmit" class="upload-form">
          <div class="form-group">
            <label class="form-label">文件名称</label>
            <input 
              v-model="formData.name" 
              type="text" 
              placeholder="请输入文件名称"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">上传文件</label>
            <div class="file-upload">
              <input 
                type="file" 
                ref="fileInput"
                @change="handleFileChange"
                class="file-input"
                accept=".pdf,.doc,.docx"
              />
              <div class="upload-area" @click="triggerFileInput">
                <UploadCloudIcon class="upload-icon" />
                <p class="upload-text">点击或拖拽文件到此处上传</p>
                <p class="upload-tip">支持 PDF、Word 格式</p>
              </div>
            </div>
            <div v-if="formData.file" class="file-info">
              <FileIcon class="file-icon" />
              <span class="file-name">{{ formData.file.name }}</span>
              <button type="button" class="remove-file" @click="removeFile">
                <XIcon class="remove-icon" />
              </button>
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" class="action-btn primary">
              <UploadCloudIcon class="btn-icon" />
              上传文件
            </button>
          </div>
        </form>
      </div>

      <!-- 详情信息 -->
      <div v-show="currentStep === 1" class="form-card">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { 
  ArrowLeftIcon, 
  UploadCloudIcon, 
  FileIcon, 
  XIcon,
  SparklesIcon 
} from 'lucide-vue-next';
import ModuleViewToggle from '@/components/ModuleViewToggle.vue';
import '@/assets/styles/form.css';

const props = defineProps<{
  title: string;
  backRouteName: string;
  uploadRouteName: string;
  manageRouteName: string;
  onSubmit: (formData: { name: string; file: File }) => Promise<any>;
}>();

const emit = defineEmits<{
  (e: 'upload-success', result: any): void;
  (e: 'ai-auto-fill'): void;
  (e: 'cancel-upload'): void;
}>();

const fileInput = ref<HTMLInputElement | null>(null);
const currentStep = ref(0);

// 步骤配置
const steps = [
  { title: '基本信息' },
  { title: '详情信息' }
];

const formData = ref({
  name: '',
  file: null as File | null
});

// 触发文件选择
const triggerFileInput = () => {
  fileInput.value?.click();
};

// 处理文件选择
const handleFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    formData.value.file = input.files[0];
  }
};

// 移除文件
const removeFile = () => {
  formData.value.file = null;
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};

// 处理表单提交
const handleSubmit = async () => {
  if (!formData.value.name) {
    alert('请输入文件名称');
    return;
  }
  if (!formData.value.file) {
    alert('请选择要上传的文件');
    return;
  }

  try {
    const result = await props.onSubmit({
      name: formData.value.name,
      file: formData.value.file
    });
    emit('upload-success', result);
    nextStep();
  } catch (error) {
    console.error('上传失败：', error);
  }
};

// 下一步
const nextStep = () => {
  if (currentStep.value < steps.length - 1) {
    currentStep.value++;
  }
}

// 上一步
const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--;
  }
}

// AI自动识别
const handleAIAutoFill = async () => {
  try {
    emit('ai-auto-fill');
  } catch (error) {
    console.error('AI识别失败：', error);
  }
};

// 跳转到指定步骤
const goToStep = (index: number) => {
  // 如果是从详情信息返回到基本信息，触发取消上传事件
  if (index === 0 && currentStep.value === 1) {
    emit('cancel-upload');
  }
  // 只允许向前跳转，不允许向后跳转
  if (index <= currentStep.value) {
    currentStep.value = index;
  }
}

// 暴露方法给父组件
defineExpose({
  nextStep,
  prevStep
});
</script>

<style scoped>
.operation-wrapper {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.operation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-left {
  display: flex;
  align-items: center;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  text-decoration: none;
  font-size: 14px;
  margin-right: 16px;
}

.back-icon {
  width: 16px;
  height: 16px;
}

.operation-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 步骤条样式 */
.steps {
  display: flex;
  justify-content: space-between;
  margin-bottom: 32px;
  padding: 0 20px;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  flex: 1;
  cursor: pointer;
  transition: all 0.2s;
}

.step:not(:last-child)::after {
  content: '';
  position: absolute;
  top: 16px;
  left: 50%;
  width: 100%;
  height: 2px;
  background-color: #ddd;
  z-index: 1;
}

.step-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #fff;
  border: 2px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
  color: #999;
  position: relative;
  z-index: 2;
}

.step.active .step-number {
  background-color: #1e88e5;
  border-color: #1e88e5;
  color: #fff;
}

.step-title {
  margin-top: 8px;
  font-size: 14px;
  color: #666;
}

.step.active .step-title {
  color: #1e88e5;
  font-weight: 500;
}

.step:hover .step-number {
  border-color: #1e88e5;
}

.step:hover .step-title {
  color: #1e88e5;
}

.operation-content {
  margin-top: 24px;
}

.form-card {
  background-color: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 24px;
}

.upload-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.file-upload {
  position: relative;
}

.file-input {
  display: none;
}

.upload-area {
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 32px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
}

.upload-area:hover {
  border-color: #1e88e5;
}

.upload-icon {
  width: 48px;
  height: 48px;
  color: #999;
  margin-bottom: 16px;
}

.upload-text {
  font-size: 16px;
  color: #666;
  margin: 0 0 8px;
}

.upload-tip {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
  padding: 8px 12px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.file-icon {
  width: 20px;
  height: 20px;
  color: #666;
}

.file-name {
  flex: 1;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.remove-file {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background-color: transparent;
  border: none;
  border-radius: 4px;
  color: #999;
  cursor: pointer;
  transition: all 0.2s;
}

.remove-file:hover {
  background-color: #e0e0e0;
  color: #d32f2f;
}

.remove-icon {
  width: 16px;
  height: 16px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.primary {
  background-color: #1e88e5;
  color: white;
}

.primary:hover {
  background-color: #1976d2;
}

.btn-icon {
  width: 16px;
  height: 16px;
}

.secondary {
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #ddd;
}

.secondary:hover {
  background-color: #e0e0e0;
}
</style>
  
  