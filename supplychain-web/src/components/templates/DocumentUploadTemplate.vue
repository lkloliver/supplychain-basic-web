<template>
    <div class="operation-wrapper">
      <div class="operation-header">
        <div class="header-left">
          <router-link :to="{ name: backRouteName }" class="back-button">
            <ArrowLeftIcon class="back-icon" />
          </router-link>
          <h2 class="operation-title">{{ title }}</h2>
        </div>
        
        <div class="header-right">
          <ModuleViewToggle 
            :uploadRoute="uploadRouteName" 
            :manageRoute="manageRouteName" 
          />
        </div>
      </div>
      
      <div class="operation-content">
        <form @submit.prevent="handleSubmit" class="upload-form">
          <div class="form-card">
            <h3 class="form-card-title">基本信息</h3>
            
            <div class="form-group">
              <label for="doc-name" class="form-label">资料名称</label>
              <input 
                type="text" 
                id="doc-name" 
                v-model="formData.name" 
                class="form-input" 
                placeholder="请输入资料名称" 
                required
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">资料文件</label>
              <div class="file-upload-box">
                <input 
                  type="file" 
                  id="doc-file" 
                  @change="handleFileChange" 
                  class="file-input" 
                />
                <label for="doc-file" class="file-upload-label">
                  <UploadCloudIcon class="upload-icon" />
                  <span>{{ formData.file ? formData.file.name : '点击上传文件' }}</span>
                </label>
              </div>
            </div>
          </div>
          
          <div class="form-card">
            <h3 class="form-card-title">{{ detailsTitle }}</h3>
            
            <div class="form-grid">
              <template v-for="(field, index) in detailFields" :key="index">
                <div class="form-group" v-if="field.type !== 'textarea'">
                  <label :for="`field-${field.key}`" class="form-label">{{ field.label }}</label>
                  
                  <input 
                    v-if="field.type === 'text' || field.type === 'number' || field.type === 'date'"
                    :type="field.type" 
                    :id="`field-${field.key}`" 
                    v-model="formData.details[field.key]" 
                    class="form-input" 
                    :placeholder="field.placeholder || `请输入${field.label}`" 
                  />
                  
                  <select 
                    v-else-if="field.type === 'select'"
                    :id="`field-${field.key}`" 
                    v-model="formData.details[field.key]"
                    class="form-select"
                  >
                    <option 
                      v-for="(option, optIndex) in field.options" 
                      :key="optIndex" 
                      :value="option.value"
                    >
                      {{ option.label }}
                    </option>
                  </select>
                </div>
              </template>
            </div>
            
            <template v-for="(field, index) in detailFields" :key="`textarea-${index}`">
              <div class="form-group" v-if="field.type === 'textarea'">
                <label :for="`field-${field.key}`" class="form-label">{{ field.label }}</label>
                <textarea 
                  :id="`field-${field.key}`" 
                  v-model="formData.details[field.key]" 
                  class="form-textarea" 
                  :placeholder="field.placeholder || `请输入${field.label}`"
                  :rows="field.rows || 3"
                ></textarea>
              </div>
            </template>
          </div>
          
          <div class="form-actions">
            <button type="button" class="action-btn secondary" @click="handleAIRecognize">
              <ZapIcon class="btn-icon" />
              AI自动识别
            </button>
            <button type="submit" class="action-btn primary">提交</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { 
    ArrowLeftIcon, 
    UploadCloudIcon, 
    ZapIcon 
  } from 'lucide-vue-next';
  import ModuleViewToggle from '@/components/ModuleViewToggle.vue';
  
  const props = defineProps({
    title: {
      type: String,
      required: true
    },
    detailsTitle: {
      type: String,
      default: '资料详情'
    },
    backRouteName: {
      type: String,
      required: true
    },
    uploadRouteName: {
      type: String,
      required: true
    },
    manageRouteName: {
      type: String,
      required: true
    },
    detailFields: {
      type: Array,
      required: true
    },
    initialDetails: {
      type: Object,
      default: () => ({})
    },
    onSubmit: {
      type: Function,
      required: true
    },
    onAIRecognize: {
      type: Function,
      default: null
    }
  });
  
  const router = useRouter();
  
  // 表单数据
  const formData = reactive({
    name: '',
    file: null,
    details: { ...props.initialDetails }
  });
  
  // 处理文件选择
  const handleFileChange = (event) => {
    const input = event.target;
    if (input.files && input.files.length > 0) {
      formData.file = input.files[0];
    }
  };
  
  // AI自动识别文档
  const handleAIRecognize = async () => {
    if (!formData.file) {
      alert('请先上传文件');
      return;
    }
    
    if (props.onAIRecognize) {
      const recognizedData = await props.onAIRecognize(formData.file);
      if (recognizedData) {
        Object.assign(formData.details, recognizedData);
      }
    } else {
      // 默认的AI识别行为
      alert('正在识别文档...');
      
      // 模拟识别过程
      setTimeout(() => {
        alert('文档识别完成');
      }, 1500);
    }
  };
  
  // 提交表单
  const handleSubmit = async () => {
    if (!formData.name) {
      alert('请输入资料名称');
      return;
    }
    
    if (!formData.file) {
      alert('请上传文件');
      return;
    }
    
    try {
      await props.onSubmit(formData);
      // 提交成功后的操作由调用组件处理
    } catch (error) {
      console.error('提交失败', error);
      alert('提交失败: ' + error.message);
    }
  };
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
  
  .operation-content {
    margin-top: 24px;
  }
  
  .upload-form {
    display: flex;
    flex-direction: column;
    gap: 24px;
  }
  
  .form-card {
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 20px;
  }
  
  .form-card-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-top: 0;
    margin-bottom: 16px;
  }
  
  .form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .form-group {
    margin-bottom: 16px;
  }
  
  .form-label {
    display: block;
    font-size: 14px;
    font-weight: 500;
    color: #555;
    margin-bottom: 8px;
  }
  
  .form-input,
  .form-select,
  .form-textarea {
    width: 100%;
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
    color: #333;
  }
  
  .form-input:focus,
  .form-select:focus,
  .form-textarea:focus {
    outline: none;
    border-color: #1e88e5;
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
  
  .form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 8px;
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
  
  .secondary {
    background-color: #f5f5f5;
    color: #333;
  }
  
  .secondary:hover {
    background-color: #e0e0e0;
  }
  
  .btn-icon {
    width: 16px;
    height: 16px;
  }
  </style>
  
  