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
import { useRoute } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';
import 'element-plus/dist/index.css';
import { 
  ArrowLeftIcon, 
  UploadCloudIcon, 
  FileIcon, 
  XIcon
} from 'lucide-vue-next';
import ModuleViewToggle from '@/components/ModuleViewToggle.vue';
import request from '@/utils/request';
import '@/assets/styles/form.css';

interface FileInfo {
  id: string;
  fileName: string;
  fileType: string;
  filePath: string;
  accessUrl: string;
  fileSize: number;
  tenant: string;
  uploadTime: string;
}

interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

const route = useRoute();
const userStore = useUserStore();

// 获取业务类型
const getBusinessType = () => {
  const path = route.path;
  
  // 合同模块
  if (path.includes('/contract/purchase')) {
    return 'contract-purchase';
  } else if (path.includes('/contract/sales')) {
    return 'contract-sales';
  } else if (path.includes('/contract/transport')) {
    return 'contract-transport';
  }
  
  // 货物模块-基地版
  else if (path.includes('/goods/base/inbound-weight')) {
    return 'goods-base-inbound-weight';
  } else if (path.includes('/goods/base/inbound-quality')) {
    return 'goods-base-inbound-quality';
  } else if (path.includes('/goods/base/inbound-image')) {
    return 'goods-base-inbound-image';
  } else if (path.includes('/goods/base/outbound-weight')) {
    return 'goods-base-outbound-weight';
  } else if (path.includes('/goods/base/outbound-quality')) {
    return 'goods-base-outbound-quality';
  } else if (path.includes('/goods/base/outbound-image')) {
    return 'goods-base-outbound-image';
  } else if (path.includes('/goods/base/purchase-waybill')) {
    return 'goods-base-purchase-waybill';
  } else if (path.includes('/goods/base/sales-waybill')) {
    return 'goods-base-sales-waybill';
  }
  
  // 货物模块-非基地版
  else if (path.includes('/goods/non-base/factory-weight')) {
    return 'goods-nonbase-factory-weight';
  } else if (path.includes('/goods/non-base/factory-quality')) {
    return 'goods-nonbase-factory-quality';
  } else if (path.includes('/goods/non-base/factory-image')) {
    return 'goods-nonbase-factory-image';
  } else if (path.includes('/goods/non-base/waybill')) {
    return 'goods-nonbase-waybill';
  }
  
  // 结算模块
  else if (path.includes('/settlement/purchase')) {
    return 'settlement-purchase';
  } else if (path.includes('/settlement/sales')) {
    return 'settlement-sales';
  }
  
  // 资金模块
  else if (path.includes('/finance/purchase-payment')) {
    return 'finance-purchase-payment';
  } else if (path.includes('/finance/sales-receipt')) {
    return 'finance-sales-receipt';
  } else if (path.includes('/finance/transport-payment')) {
    return 'finance-transport-payment';
  }
  
  // 发票模块
  else if (path.includes('/invoice/purchase-in')) {
    return 'invoice-purchase-in';
  } else if (path.includes('/invoice/purchase-out')) {
    return 'invoice-purchase-out';
  } else if (path.includes('/invoice/sales')) {
    return 'invoice-sales';
  } else if (path.includes('/invoice/transport')) {
    return 'invoice-transport';
  }
  
  // 台账模块
  else if (path.includes('/ledger/purchase')) {
    return 'ledger-purchase';
  } else if (path.includes('/ledger/sales')) {
    return 'ledger-sales';
  }
  
  return 'unknown';
};

const props = defineProps<{
  title: string;
  backRouteName: string;
  uploadRouteName: string;
  manageRouteName: string;
}>();

const emit = defineEmits<{
  (e: 'upload-success', result: any): void;
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
    // 自动填充文件名（去掉扩展名）
    const fileName = input.files[0].name;
    const nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
    formData.value.name = nameWithoutExt;
  }
};

// 移除文件
const removeFile = () => {
  formData.value.file = null;
  formData.value.name = ''; // 清空文件名
  if (fileInput.value) {
    fileInput.value.value = '';
  }
};

// 处理表单提交
const handleSubmit = async () => {
  try {
    if (!formData.value.file) {
      ElMessage.warning('请选择要上传的文件');
      return;
    }

    if (!formData.value.name) {
      ElMessage.warning('请输入文件名称');
      return;
    }

    const formDataToSend = new FormData();
    formDataToSend.append('file', formData.value.file);
    formDataToSend.append('tenant', userStore.version);
    formDataToSend.append('type', getBusinessType());
    formDataToSend.append('fileName', formData.value.name);

    console.log('开始上传文件:', {
      fileName: formData.value.name,
      originalFileName: formData.value.file.name,
      fileSize: formData.value.file.size,
      tenant: userStore.version,
      type: getBusinessType()
    });

    console.log('发送请求...');
    const response = await request.post<ApiResponse<FileInfo>>('/files/upload', formDataToSend, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    console.log('收到响应:', response);
    
    if (response.data.code === 200 && response.data.data) {
      console.log('文件上传成功:', response.data.data);
      ElMessage.success('上传成功');
      emit('upload-success', {
        ...response.data.data,
        name: formData.value.name,
        type: getBusinessType()
      });
      nextStep();
    } else {
      console.error('响应数据为空或状态码错误:', response.data);
      throw new Error(response.data.message || '上传失败：服务器返回数据为空');
    }
  } catch (error: any) {
    console.error('上传失败，错误详情:', {
      message: error.message,
      response: error.response,
      request: error.request
    });
    // 错误消息已经在request.ts中处理，这里不需要再显示
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
  
  