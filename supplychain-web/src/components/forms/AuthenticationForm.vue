<template>
  <div class="authentication-form">
    <!-- 认证类型选择 -->
    <div class="form-group">
      <label class="form-label">认证类型</label>
      <div class="auth-type-selector">
        <label class="type-option">
          <input 
            type="radio" 
            v-model="authType" 
            value="natural" 
            name="authType"
          />
          <span>自然人</span>
        </label>
        <label class="type-option">
          <input 
            type="radio" 
            v-model="authType" 
            value="individual" 
            name="authType"
          />
          <span>个体工商户</span>
        </label>
        <label class="type-option">
          <input 
            type="radio" 
            v-model="authType" 
            value="enterprise" 
            name="authType"
          />
          <span>企业法人</span>
        </label>
      </div>
    </div>

    <!-- 自然人认证表单 -->
    <div v-if="authType === 'natural'" class="auth-form">
      <div class="form-group">
        <label class="form-label">姓名</label>
        <input 
          v-model="naturalForm.name" 
          type="text" 
          placeholder="请输入姓名"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">身份证号码</label>
        <input 
          v-model="naturalForm.idCard" 
          type="text" 
          placeholder="请输入身份证号码"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">手机号码</label>
        <input 
          v-model="naturalForm.phone" 
          type="tel" 
          placeholder="请输入手机号码"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">银行卡号</label>
        <input 
          v-model="naturalForm.bankCard" 
          type="text" 
          placeholder="请输入银行卡号"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">自然人承诺书</label>
        <div class="file-upload">
          <input 
            type="file" 
            ref="promiseFileInput"
            @change="handlePromiseFileChange"
            class="file-input"
            accept=".pdf,.jpg,.jpeg,.png"
          />
          <div class="upload-area" @click="triggerPromiseFileInput">
            <UploadCloudIcon class="upload-icon" />
            <p class="upload-text">点击或拖拽文件到此处上传</p>
            <p class="upload-tip">支持 PDF、图片格式</p>
          </div>
        </div>
        <div v-if="naturalForm.promiseFile" class="file-info">
          <FileIcon class="file-icon" />
          <span class="file-name">{{ naturalForm.promiseFile.name }}</span>
          <button type="button" class="remove-file" @click="removePromiseFile">
            <XIcon class="remove-icon" />
          </button>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label">税费代办委托协议</label>
        <div class="file-upload">
          <input 
            type="file" 
            ref="delegateFileInput"
            @change="handleDelegateFileChange"
            class="file-input"
            accept=".pdf,.jpg,.jpeg,.png"
          />
          <div class="upload-area" @click="triggerDelegateFileInput">
            <UploadCloudIcon class="upload-icon" />
            <p class="upload-text">点击或拖拽文件到此处上传</p>
            <p class="upload-tip">支持 PDF、图片格式</p>
          </div>
        </div>
        <div v-if="naturalForm.delegateFile" class="file-info">
          <FileIcon class="file-icon" />
          <span class="file-name">{{ naturalForm.delegateFile.name }}</span>
          <button type="button" class="remove-file" @click="removeDelegateFile">
            <XIcon class="remove-icon" />
          </button>
        </div>
      </div>
    </div>

    <!-- 个体工商户认证表单 -->
    <div v-if="authType === 'individual'" class="auth-form">
      <div class="form-group">
        <label class="form-label">注册名称</label>
        <input 
          v-model="individualForm.name" 
          type="text" 
          placeholder="请输入注册名称"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">税号</label>
        <input 
          v-model="individualForm.taxNo" 
          type="text" 
          placeholder="请输入税号"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">注册地址</label>
        <input 
          v-model="individualForm.address" 
          type="text" 
          placeholder="请输入注册地址"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">营业执照</label>
        <div class="file-upload">
          <input 
            type="file" 
            ref="licenseFileInput"
            @change="handleLicenseFileChange"
            class="file-input"
            accept=".pdf,.jpg,.jpeg,.png"
          />
          <div class="upload-area" @click="triggerLicenseFileInput">
            <UploadCloudIcon class="upload-icon" />
            <p class="upload-text">点击或拖拽文件到此处上传</p>
            <p class="upload-tip">支持 PDF、图片格式</p>
          </div>
        </div>
        <div v-if="individualForm.licenseFile" class="file-info">
          <FileIcon class="file-icon" />
          <span class="file-name">{{ individualForm.licenseFile.name }}</span>
          <button type="button" class="remove-file" @click="removeLicenseFile">
            <XIcon class="remove-icon" />
          </button>
        </div>
      </div>
    </div>

    <!-- 企业法人认证表单 -->
    <div v-if="authType === 'enterprise'" class="auth-form">
      <div class="form-group">
        <label class="form-label">注册名称</label>
        <input 
          v-model="enterpriseForm.name" 
          type="text" 
          placeholder="请输入注册名称"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">税号</label>
        <input 
          v-model="enterpriseForm.taxNo" 
          type="text" 
          placeholder="请输入税号"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">注册地址</label>
        <input 
          v-model="enterpriseForm.address" 
          type="text" 
          placeholder="请输入注册地址"
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">营业执照</label>
        <div class="file-upload">
          <input 
            type="file" 
            ref="enterpriseLicenseFileInput"
            @change="handleEnterpriseLicenseFileChange"
            class="file-input"
            accept=".pdf,.jpg,.jpeg,.png"
          />
          <div class="upload-area" @click="triggerEnterpriseLicenseFileInput">
            <UploadCloudIcon class="upload-icon" />
            <p class="upload-text">点击或拖拽文件到此处上传</p>
            <p class="upload-tip">支持 PDF、图片格式</p>
          </div>
        </div>
        <div v-if="enterpriseForm.licenseFile" class="file-info">
          <FileIcon class="file-icon" />
          <span class="file-name">{{ enterpriseForm.licenseFile.name }}</span>
          <button type="button" class="remove-file" @click="removeEnterpriseLicenseFile">
            <XIcon class="remove-icon" />
          </button>
        </div>
      </div>
    </div>

    <!-- 认证方式选择 -->
    <div class="form-group">
      <label class="form-label">认证方式</label>
      <div class="auth-method-selector">
        <template v-if="authType === 'natural'">
          <label class="method-option">
            <input 
              type="radio" 
              v-model="authMethod" 
              value="biometric" 
              name="authMethod"
            />
            <span>生物特征识别</span>
          </label>
          <label class="method-option">
            <input 
              type="radio" 
              v-model="authMethod" 
              value="national" 
              name="authMethod"
            />
            <span>国家统一身份认证平台</span>
          </label>
        </template>
        <template v-else-if="authType === 'individual'">
          <label class="method-option">
            <input 
              type="radio" 
              v-model="authMethod" 
              value="biometric" 
              name="authMethod"
            />
            <span>生物特征识别</span>
          </label>
          <label class="method-option">
            <input 
              type="radio" 
              v-model="authMethod" 
              value="national" 
              name="authMethod"
            />
            <span>国家统一身份认证平台</span>
          </label>
          <label class="method-option">
            <input 
              type="radio" 
              v-model="authMethod" 
              value="document" 
              name="authMethod"
            />
            <span>证件证照对比</span>
          </label>
        </template>
        <template v-else>
          <label class="method-option">
            <input 
              type="radio" 
              v-model="authMethod" 
              value="national" 
              name="authMethod"
            />
            <span>国家统一身份认证平台</span>
          </label>
          <label class="method-option">
            <input 
              type="radio" 
              v-model="authMethod" 
              value="document" 
              name="authMethod"
            />
            <span>证件证照对比</span>
          </label>
        </template>
      </div>
    </div>

    <!-- 提交按钮 -->
    <div class="form-actions">
      <button type="button" class="action-btn secondary" @click="$emit('cancel')">
        取消
      </button>
      <button type="button" class="action-btn primary" @click="handleSubmit">
        提交认证
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { 
  UploadCloudIcon, 
  FileIcon, 
  XIcon 
} from 'lucide-vue-next';

const emit = defineEmits<{
  (e: 'submit', data: any): void;
  (e: 'cancel'): void;
}>();

// 认证类型
const authType = ref<'natural' | 'individual' | 'enterprise'>('natural');
// 认证方式
const authMethod = ref<'biometric' | 'national' | 'document'>('biometric');

// 文件输入引用
const promiseFileInput = ref<HTMLInputElement | null>(null);
const delegateFileInput = ref<HTMLInputElement | null>(null);
const licenseFileInput = ref<HTMLInputElement | null>(null);
const enterpriseLicenseFileInput = ref<HTMLInputElement | null>(null);

// 自然人表单数据
const naturalForm = ref({
  name: '',
  idCard: '',
  phone: '',
  bankCard: '',
  promiseFile: null as File | null,
  delegateFile: null as File | null
});

// 个体工商户表单数据
const individualForm = ref({
  name: '',
  taxNo: '',
  address: '',
  licenseFile: null as File | null
});

// 企业法人表单数据
const enterpriseForm = ref({
  name: '',
  taxNo: '',
  address: '',
  licenseFile: null as File | null
});

// 触发文件选择
const triggerPromiseFileInput = () => promiseFileInput.value?.click();
const triggerDelegateFileInput = () => delegateFileInput.value?.click();
const triggerLicenseFileInput = () => licenseFileInput.value?.click();
const triggerEnterpriseLicenseFileInput = () => enterpriseLicenseFileInput.value?.click();

// 处理文件选择
const handlePromiseFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    naturalForm.value.promiseFile = input.files[0];
  }
};

const handleDelegateFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    naturalForm.value.delegateFile = input.files[0];
  }
};

const handleLicenseFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    individualForm.value.licenseFile = input.files[0];
  }
};

const handleEnterpriseLicenseFileChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    enterpriseForm.value.licenseFile = input.files[0];
  }
};

// 移除文件
const removePromiseFile = () => {
  naturalForm.value.promiseFile = null;
  if (promiseFileInput.value) {
    promiseFileInput.value.value = '';
  }
};

const removeDelegateFile = () => {
  naturalForm.value.delegateFile = null;
  if (delegateFileInput.value) {
    delegateFileInput.value.value = '';
  }
};

const removeLicenseFile = () => {
  individualForm.value.licenseFile = null;
  if (licenseFileInput.value) {
    licenseFileInput.value.value = '';
  }
};

const removeEnterpriseLicenseFile = () => {
  enterpriseForm.value.licenseFile = null;
  if (enterpriseLicenseFileInput.value) {
    enterpriseLicenseFileInput.value.value = '';
  }
};

// 表单验证
const validateForm = () => {
  if (authType.value === 'natural') {
    if (!naturalForm.value.name) {
      alert('请输入姓名');
      return false;
    }
    if (!naturalForm.value.idCard) {
      alert('请输入身份证号码');
      return false;
    }
    if (!naturalForm.value.phone) {
      alert('请输入手机号码');
      return false;
    }
    if (!naturalForm.value.bankCard) {
      alert('请输入银行卡号');
      return false;
    }
    if (!naturalForm.value.promiseFile) {
      alert('请上传自然人承诺书');
      return false;
    }
    if (!naturalForm.value.delegateFile) {
      alert('请上传税费代办委托协议');
      return false;
    }
  } else if (authType.value === 'individual') {
    if (!individualForm.value.name) {
      alert('请输入注册名称');
      return false;
    }
    if (!individualForm.value.taxNo) {
      alert('请输入税号');
      return false;
    }
    if (!individualForm.value.address) {
      alert('请输入注册地址');
      return false;
    }
    if (!individualForm.value.licenseFile) {
      alert('请上传营业执照');
      return false;
    }
  } else {
    if (!enterpriseForm.value.name) {
      alert('请输入注册名称');
      return false;
    }
    if (!enterpriseForm.value.taxNo) {
      alert('请输入税号');
      return false;
    }
    if (!enterpriseForm.value.address) {
      alert('请输入注册地址');
      return false;
    }
    if (!enterpriseForm.value.licenseFile) {
      alert('请上传营业执照');
      return false;
    }
  }
  return true;
};

// 处理表单提交
const handleSubmit = () => {
  if (!validateForm()) {
    return;
  }

  const submitData = {
    authType: authType.value,
    authMethod: authMethod.value,
    formData: authType.value === 'natural' ? naturalForm.value :
              authType.value === 'individual' ? individualForm.value :
              enterpriseForm.value
  };

  emit('submit', submitData);
};
</script>

<style scoped>
.authentication-form {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
}

.form-group {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.form-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s;
}

.form-input:focus {
  border-color: #1e88e5;
  outline: none;
  box-shadow: 0 0 0 2px rgba(30, 136, 229, 0.1);
}

.auth-type-selector,
.auth-method-selector {
  display: flex;
  gap: 24px;
  margin-top: 8px;
}

.type-option,
.method-option {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.type-option input,
.method-option input {
  margin: 0;
}

.file-upload {
  margin-top: 8px;
}

.file-input {
  display: none;
}

.upload-area {
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.upload-area:hover {
  border-color: #1e88e5;
}

.upload-icon {
  width: 32px;
  height: 32px;
  color: #999;
  margin-bottom: 8px;
}

.upload-text {
  font-size: 14px;
  color: #666;
  margin: 0 0 4px;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
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
  margin-top: 32px;
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
  border: 1px solid #ddd;
}

.secondary:hover {
  background-color: #e0e0e0;
}
</style> 