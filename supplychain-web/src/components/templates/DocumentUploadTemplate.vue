<template>
  <div class="operation-wrapper">
    <div class="operation-header">
      <div class="header-left">
        <router-link :to="{ name: backRouteName }" class="back-button">
          <ArrowLeftIcon class="back-icon" />
          <span>返回</span>
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
              <div class="form-group" v-if="field.type !== 'textarea' && field.type !== 'array' && !field.customComponent">
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
          
          <template v-for="(field, index) in detailFields" :key="`file-${index}`">
            <div class="form-group" v-if="field.type === 'file'">
              <label :for="`field-${field.key}`" class="form-label">{{ field.label }}</label>
              <div class="file-upload-box">
                <input 
                  type="file" 
                  :id="`field-${field.key}`" 
                  :accept="field.accept"
                  @change="e => handleDetailFileChange(e, field.key)" 
                  class="file-input" 
                />
                <label :for="`field-${field.key}`" class="file-upload-label">
                  <UploadCloudIcon class="upload-icon" />
                  <span>{{ getDetailFileName(field.key) || field.placeholder || '点击上传文件' }}</span>
                </label>
              </div>
            </div>
          </template>
          
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
          
          <!-- 多组数据输入 -->
          <template v-for="(field, index) in detailFields" :key="`array-${index}`">
            <div class="form-group form-array-group" v-if="field.type === 'array'">
              <div class="array-header">
                <label class="form-label">{{ field.label }}</label>
                <button 
                  type="button" 
                  class="add-item-btn"
                  @click="addArrayItem(field.key, field.itemTemplate)"
                >
                  <PlusIcon class="btn-icon" />
                  添加{{ field.itemLabel || '项目' }}
                </button>
              </div>
              
              <div class="array-items">
                <div 
                  v-for="(item, itemIndex) in getArrayItems(field.key)" 
                  :key="`${field.key}-${itemIndex}`"
                  class="array-item"
                >
                  <div class="array-item-header">
                    <span class="array-item-title">{{ field.itemLabel || '项目' }} #{{ itemIndex + 1 }}</span>
                    <button 
                      type="button" 
                      class="remove-item-btn"
                      @click="removeArrayItem(field.key, itemIndex)"
                    >
                      <XIcon class="btn-icon" />
                    </button>
                  </div>
                  
                  <div class="array-item-content">
                    <div class="form-grid">
                      <template v-for="(subField, subIndex) in field.fields" :key="`${field.key}-${itemIndex}-${subIndex}`">
                        <div class="form-group">
                          <label :for="`${field.key}-${itemIndex}-${subField.key}`" class="form-label">
                            {{ subField.label }}
                          </label>
                          
                          <input 
                            v-if="subField.type === 'text' || subField.type === 'number' || subField.type === 'date'"
                            :type="subField.type" 
                            :id="`${field.key}-${itemIndex}-${subField.key}`" 
                            v-model="getArrayItems(field.key)[itemIndex][subField.key]" 
                            class="form-input" 
                            :placeholder="subField.placeholder || `请输入${subField.label}`" 
                          />
                          
                          <select 
                            v-else-if="subField.type === 'select'"
                            :id="`${field.key}-${itemIndex}-${subField.key}`" 
                            v-model="getArrayItems(field.key)[itemIndex][subField.key]"
                            class="form-select"
                          >
                            <option 
                              v-for="(option, optIndex) in subField.options" 
                              :key="optIndex" 
                              :value="option.value"
                            >
                              {{ option.label }}
                            </option>
                          </select>
                          
                          <textarea 
                            v-else-if="subField.type === 'textarea'"
                            :id="`${field.key}-${itemIndex}-${subField.key}`" 
                            v-model="getArrayItems(field.key)[itemIndex][subField.key]" 
                            class="form-textarea" 
                            :placeholder="subField.placeholder || `请输入${subField.label}`"
                            :rows="subField.rows || 3"
                          ></textarea>
                        </div>
                      </template>
                    </div>
                  </div>
                </div>
                
                <div v-if="getArrayItems(field.key).length === 0" class="empty-array-message">
                  暂无{{ field.itemLabel || '项目' }}，请点击上方按钮添加
                </div>
              </div>
            </div>
          </template>
          
          <!-- 自定义组件 -->
          <template v-for="(field, index) in detailFields" :key="`custom-${index}`">
            <div class="form-group" v-if="field.customComponent && customComponents[field.customComponent]">
              <label class="form-label">{{ field.label }}</label>
              <component 
                :is="customComponents[field.customComponent]"
                v-bind="field.props || {}"
                :value="formData.details[field.key]"
                @input="value => updateCustomComponentValue(field.key, value)"
              />
            </div>
          </template>
        </div>
        
        <!-- 添加新的插槽 -->
        <slot name="after-details"></slot>
        
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
import { ref, reactive, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { 
  ArrowLeftIcon, 
  UploadCloudIcon, 
  ZapIcon,
  PlusIcon,
  XIcon
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
  },
  customComponents: {
    type: Object,
    default: () => ({})
  }
});

const router = useRouter();

// 表单数据
const formData = reactive({
  name: '',
  file: null,
  details: { ...props.initialDetails }
});

// 初始化数组字段
onMounted(() => {
  props.detailFields.forEach(field => {
    if (field.type === 'array' && !formData.details[field.key]) {
      formData.details[field.key] = [];
    }
  });
});

// 处理文件选择
const handleFileChange = (event) => {
  const input = event.target;
  if (input.files && input.files.length > 0) {
    formData.file = input.files[0];
  }
};

// 获取数组字段的项目
const getArrayItems = (key) => {
  if (!formData.details[key]) {
    formData.details[key] = [];
  }
  return formData.details[key];
};

// 添加数组项目
const addArrayItem = (key, template = {}) => {
  if (!formData.details[key]) {
    formData.details[key] = [];
  }
  formData.details[key].push({...template});
};

// 删除数组项目
const removeArrayItem = (key, index) => {
  if (formData.details[key] && index >= 0 && index < formData.details[key].length) {
    formData.details[key].splice(index, 1);
  }
};

// 更新自定义组件的值
const updateCustomComponentValue = (key, value) => {
  formData.details[key] = value;
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
      // 合并识别的数据到表单数据
      Object.keys(recognizedData).forEach(key => {
        // 处理数组类型的字段
        if (Array.isArray(recognizedData[key])) {
          formData.details[key] = [...recognizedData[key]];
        } else {
          formData.details[key] = recognizedData[key];
        }
      });
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

// 处理详情文件上传
const handleDetailFileChange = (event, key) => {
  const input = event.target;
  if (input.files && input.files.length > 0) {
    formData.details[key] = input.files[0];
  }
};

// 获取详情文件名
const getDetailFileName = (key) => {
  const file = formData.details[key];
  return file ? file.name : '';
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

.form-array-group {
  margin-top: 24px;
  border-top: 1px solid #eee;
  padding-top: 16px;
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

.array-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.add-item-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s;
}

.add-item-btn:hover {
  background-color: #e0e0e0;
}

.array-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.array-item {
  border: 1px solid #eee;
  border-radius: 6px;
  overflow: hidden;
}

.array-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #eee;
}

.array-item-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.remove-item-btn {
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

.remove-item-btn:hover {
  background-color: #f1f1f1;
  color: #d32f2f;
}

.array-item-content {
  padding: 16px;
}

.empty-array-message {
  padding: 16px;
  text-align: center;
  color: #999;
  font-size: 14px;
  background-color: #f9f9f9;
  border-radius: 4px;
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
  
  