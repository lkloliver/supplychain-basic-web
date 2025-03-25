<template>
    <div class="purchase-waybill-upload">
      <h2>采购运单上传</h2>
      
      <div class="upload-form">
        <div class="form-section">
          <h3>基本信息</h3>
          <div class="form-grid">
            <div class="form-group">
              <label for="contractId">关联采购合同 <span class="required">*</span></label>
              <select 
                id="contractId" 
                v-model="formData.contractId"
                :class="{ 'error': errors.contractId }"
              >
                <option value="">请选择关联采购合同</option>
                <option v-for="contract in contracts" :key="contract.id" :value="contract.id">
                  {{ contract.id }} - {{ contract.supplier }}
                </option>
              </select>
              <span class="error-message" v-if="errors.contractId">{{ errors.contractId }}</span>
            </div>
            <div class="form-group">
              <label for="waybillId">运单号 <span class="required">*</span></label>
              <input 
                type="text" 
                id="waybillId" 
                v-model="formData.waybillId" 
                placeholder="请输入运单号"
                :class="{ 'error': errors.waybillId }"
              />
              <span class="error-message" v-if="errors.waybillId">{{ errors.waybillId }}</span>
            </div>
            <div class="form-group">
              <label for="goodsName">货物名称 <span class="required">*</span></label>
              <input 
                type="text" 
                id="goodsName" 
                v-model="formData.goodsName" 
                placeholder="请输入货物名称"
                :class="{ 'error': errors.goodsName }"
              />
              <span class="error-message" v-if="errors.goodsName">{{ errors.goodsName }}</span>
            </div>
            <div class="form-group">
              <label for="quantity">运输数量 <span class="required">*</span></label>
              <div class="input-with-unit">
                <input 
                  type="number" 
                  id="quantity" 
                  v-model="formData.quantity" 
                  placeholder="请输入数量"
                  :class="{ 'error': errors.quantity }"
                />
                <select v-model="formData.unit">
                  <option value="吨">吨</option>
                  <option value="千克">千克</option>
                  <option value="件">件</option>
                </select>
              </div>
              <span class="error-message" v-if="errors.quantity">{{ errors.quantity }}</span>
            </div>
          </div>
        </div>
        
        <div class="form-section">
          <h3>运输信息</h3>
          <div class="form-grid">
            <div class="form-group">
              <label for="transportDate">运输日期 <span class="required">*</span></label>
              <input 
                type="date" 
                id="transportDate" 
                v-model="formData.transportDate"
                :class="{ 'error': errors.transportDate }"
              />
              <span class="error-message" v-if="errors.transportDate">{{ errors.transportDate }}</span>
            </div>
            <div class="form-group">
              <label for="estimatedArrival">预计到达日期</label>
              <input 
                type="date" 
                id="estimatedArrival" 
                v-model="formData.estimatedArrival"
              />
            </div>
            <div class="form-group">
              <label for="vehicleNumber">车牌号 <span class="required">*</span></label>
              <input 
                type="text" 
                id="vehicleNumber" 
                v-model="formData.vehicleNumber" 
                placeholder="请输入车牌号"
                :class="{ 'error': errors.vehicleNumber }"
              />
              <span class="error-message" v-if="errors.vehicleNumber">{{ errors.vehicleNumber }}</span>
            </div>
            <div class="form-group">
              <label for="driverName">司机姓名 <span class="required">*</span></label>
              <input 
                type="text" 
                id="driverName" 
                v-model="formData.driverName" 
                placeholder="请输入司机姓名"
                :class="{ 'error': errors.driverName }"
              />
              <span class="error-message" v-if="errors.driverName">{{ errors.driverName }}</span>
            </div>
            <div class="form-group">
              <label for="driverPhone">司机电话 <span class="required">*</span></label>
              <input 
                type="text" 
                id="driverPhone" 
                v-model="formData.driverPhone" 
                placeholder="请输入司机电话"
                :class="{ 'error': errors.driverPhone }"
              />
              <span class="error-message" v-if="errors.driverPhone">{{ errors.driverPhone }}</span>
            </div>
            <div class="form-group">
              <label for="transportFee">运费(元) <span class="required">*</span></label>
              <input 
                type="number" 
                id="transportFee" 
                v-model="formData.transportFee" 
                placeholder="请输入运费"
                :class="{ 'error': errors.transportFee }"
              />
              <span class="error-message" v-if="errors.transportFee">{{ errors.transportFee }}</span>
            </div>
          </div>
        </div>
        
        <div class="form-section">
          <h3>其他信息</h3>
          <div class="form-group">
            <label for="remarks">备注</label>
            <textarea 
              id="remarks" 
              v-model="formData.remarks" 
              placeholder="请输入备注信息"
              rows="3"
            ></textarea>
          </div>
          
          <div class="form-group">
            <label>运单附件</label>
            <div class="file-upload">
              <input 
                type="file" 
                ref="fileInput" 
                @change="handleFileChange" 
                multiple 
                accept=".pdf,.jpg,.jpeg,.png"
                style="display: none"
              />
              <div class="upload-button" @click="triggerFileInput">
                <span>选择文件</span>
              </div>
              <div class="file-list" v-if="selectedFiles.length > 0">
                <div v-for="(file, index) in selectedFiles" :key="index" class="file-item">
                  <span class="file-name">{{ file.name }}</span>
                  <span class="file-size">({{ formatFileSize(file.size) }})</span>
                  <button class="btn-remove" @click="removeFile(index)">×</button>
                </div>
              </div>
              <div class="upload-hint">
                支持PDF、JPG、PNG格式，单个文件不超过10MB
              </div>
            </div>
          </div>
        </div>
        
        <div class="form-actions">
          <button class="btn-cancel" @click="cancel">取消</button>
          <button class="btn-submit" @click="submitForm" :disabled="isSubmitting">
            {{ isSubmitting ? '提交中...' : '提交' }}
          </button>
        </div>
      </div>
      
      <!-- 提交成功弹窗 -->
      <div class="success-modal" v-if="showSuccess">
        <div class="success-content">
          <i class="success-icon">✓</i>
          <h3>提交成功</h3>
          <p>采购运单已成功上传到系统</p>
          <div class="success-actions">
            <button class="btn-back" @click="goBack">返回列表</button>
            <button class="btn-continue" @click="continueUpload">继续上传</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  // 模拟合同数据
  const contracts = ref([]);
  
  // 加载合同数据
  onMounted(() => {
    // 模拟API调用
    setTimeout(() => {
      contracts.value = [
        { id: 'PC-20230401001', supplier: '河南农产品有限公司' },
        { id: 'PC-20230402001', supplier: '山东粮油集团' },
        { id: 'PC-20230403001', supplier: '黑龙江大豆专业合作社' }
      ];
    }, 500);
  });
  
  // 表单数据
  const formData = reactive({
    contractId: '',
    waybillId: '',
    goodsName: '',
    quantity: '',
    unit: '吨',
    transportDate: '',
    estimatedArrival: '',
    vehicleNumber: '',
    driverName: '',
    driverPhone: '',
    transportFee: '',
    remarks: ''
  });
  
  // 错误信息
  const errors = reactive({
    contractId: '',
    waybillId: '',
    goodsName: '',
    quantity: '',
    transportDate: '',
    vehicleNumber: '',
    driverName: '',
    driverPhone: '',
    transportFee: ''
  });
  
  // 文件上传
  const fileInput = ref(null);
  const selectedFiles = ref([]);
  const isSubmitting = ref(false);
  const showSuccess = ref(false);
  
  // 触发文件选择
  const triggerFileInput = () => {
    fileInput.value.click();
  };
  
  // 处理文件选择
  const handleFileChange = (event) => {
    const files = Array.from(event.target.files);
    
    // 验证文件类型和大小
    const validFiles = files.filter(file => {
      const isValidSize = file.size <= 10 * 1024 * 1024; // 10MB
      const isValidType = ['.pdf', '.jpg', '.jpeg', '.png'].some(ext => 
        file.name.toLowerCase().endsWith(ext) || 
        ['application/pdf', 'image/jpeg', 'image/jpg', 'image/png'].includes(file.type)
      );
      
      if (!isValidSize || !isValidType) {
        alert(`文件 ${file.name} 不符合要求，请确保文件类型正确且大小不超过10MB`);
        return false;
      }
      
      return true;
    });
    
    selectedFiles.value = [...selectedFiles.value, ...validFiles];
  };
  
  // 移除文件
  const removeFile = (index) => {
    selectedFiles.value.splice(index, 1);
  };
  
  // 格式化文件大小
  const formatFileSize = (size) => {
    if (size < 1024) {
      return size + ' B';
    } else if (size < 1024 * 1024) {
      return (size / 1024).toFixed(2) + ' KB';
    } else {
      return (size / (1024 * 1024)).toFixed(2) + ' MB';
    }
  };
  
  // 验证表单
  const validateForm = () => {
    let isValid = true;
    
    // 重置错误
    Object.keys(errors).forEach(key => {
      errors[key] = '';
    });
    
    // 验证必填字段
    if (!formData.contractId) {
      errors.contractId = '请选择关联采购合同';
      isValid = false;
    }
    
    if (!formData.waybillId.trim()) {
      errors.waybillId = '请输入运单号';
      isValid = false;
    }
    
    if (!formData.goodsName.trim()) {
      errors.goodsName = '请输入货物名称';
      isValid = false;
    }
    
    if (!formData.quantity || formData.quantity <= 0) {
      errors.quantity = '请输入有效的运输数量';
      isValid = false;
    }
    
    if (!formData.transportDate) {
      errors.transportDate = '请选择运输日期';
      isValid = false;
    }
    
    if (!formData.vehicleNumber.trim()) {
      errors.vehicleNumber = '请输入车牌号';
      isValid = false;
    }
    
    if (!formData.driverName.trim()) {
      errors.driverName = '请输入司机姓名';
      isValid = false;
    }
    
    if (!formData.driverPhone.trim()) {
      errors.driverPhone = '请输入司机电话';
      isValid = false;
    } else if (!/^1[3-9]\d{9}$/.test(formData.driverPhone)) {
      errors.driverPhone = '请输入有效的手机号码';
      isValid = false;
    }
    
    if (!formData.transportFee || formData.transportFee <= 0) {
      errors.transportFee = '请输入有效的运费';
      isValid = false;
    }
    
    return isValid;
  };
  
  // 提交表单
  const submitForm = async () => {
    if (!validateForm()) {
      // 滚动到第一个错误
      const firstError = document.querySelector('.error-message');
      if (firstError) {
        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
      return;
    }
    
    isSubmitting.value = true;
    
    // 模拟API调用
    setTimeout(() => {
      isSubmitting.value = false;
      showSuccess.value = true;
    }, 1500);
  };
  
  // 取消上传
  const cancel = () => {
    if (confirm('确定要取消上传吗？已填写的内容将不会保存。')) {
      router.go(-1);
    }
  };
  
  // 返回列表
  const goBack = () => {
    router.push('/dashboard/goods/base/purchase-waybill');
  };
  
  // 继续上传
  const continueUpload = () => {
    // 重置表单
    Object.keys(formData).forEach(key => {
      if (key !== 'unit') {
        formData[key] = '';
      }
    });
    selectedFiles.value = [];
    showSuccess.value = false;
  };
  </script>
  
  <style scoped>
  .purchase-waybill-upload {
    padding: 20px;
  }
  
  h2 {
    margin-bottom: 20px;
    font-size: 1.5rem;
    color: #333;
  }
  
  .upload-form {
    background-color: white;
    border-radius: 5px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  }
  
  .form-section {
    padding: 20px;
    border-bottom: 1px solid #eee;
  }
  
  .form-section h3 {
    margin-bottom: 15px;
    font-size: 1.2rem;
    color: #333;
  }
  
  .form-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
  }
  
  .form-group label {
    margin-bottom: 5px;
    font-weight: 500;
  }
  
  .required {
    color: #ff4d4f;
  }
  
  input, select, textarea {
    padding: 10px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    transition: border-color 0.3s;
  }
  
  input:focus, select:focus, textarea:focus {
    outline: none;
    border-color: #1890ff;
  }
  
  input.error, select.error {
    border-color: #ff4d4f;
  }
  
  .error-message {
    color: #ff4d4f;
    font-size: 0.8rem;
    margin-top: 5px;
  }
  
  textarea {
    resize: vertical;
  }
  
  .input-with-unit {
    display: flex;
  }
  
  .input-with-unit input {
    flex: 1;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
  }
  
  .input-with-unit select {
    width: 80px;
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    border-left: none;
  }
  
  .file-upload {
    margin-top: 5px;
  }
  
  .upload-button {
    display: inline-block;
    padding: 8px 15px;
    background-color: #f0f0f0;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .upload-button:hover {
    background-color: #e0e0e0;
  }
  
  .file-list {
    margin-top: 10px;
  }
  
  .file-item {
    display: flex;
    align-items: center;
    padding: 5px 10px;
    background-color: #f5f5f5;
    border-radius: 4px;
    margin-bottom: 5px;
  }
  
  .file-name {
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .file-size {
    color: #999;
    margin-left: 5px;
    font-size: 0.8rem;
  }
  
  .btn-remove {
    background: none;
    border: none;
    color: #999;
    cursor: pointer;
    font-size: 1.2rem;
    margin-left: 5px;
  }
  
  .upload-hint {
    margin-top: 5px;
    color: #999;
    font-size: 0.8rem;
  }
  
  .form-actions {
    padding: 20px;
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  .btn-cancel, .btn-submit {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-weight: 500;
  }
  
  .btn-cancel {
    background-color: #f0f0f0;
    color: #333;
  }
  
  .btn-submit {
    background-color: #1890ff;
    color: white;
  }
  
  .btn-submit:disabled {
    background-color: #91caff;
    cursor: not-allowed;
  }
  
  /* 提交成功弹窗 */
  .success-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .success-content {
    background-color: white;
    border-radius: 5px;
    padding: 30px;
    text-align: center;
    width: 400px;
    max-width: 90%;
  }
  
  .success-icon {
    display: block;
    width: 60px;
    height: 60px;
    line-height: 60px;
    font-size: 30px;
    color: white;
    background-color: #52c41a;
    border-radius: 50%;
    margin: 0 auto 20px;
  }
  
  .success-actions {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-top: 20px;
  }
  
  .btn-back, .btn-continue {
    padding: 8px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .btn-back {
    background-color: #f0f0f0;
    color: #333;
  }
  
  .btn-continue {
    background-color: #1890ff;
    color: white;
  }
  </style>
  
  