<template>
    <div class="operation-page">
      <div class="operation-header">
        <router-link :to="{ name: 'InboundWeight' }" class="back-btn">
          <ArrowLeftIcon class="back-icon" />
          <span>返回</span>
        </router-link>
        <h2 class="operation-title">入库过磅单</h2>
      </div>
      
      <div class="operation-tabs">
        <router-link 
          :to="{ name: 'InboundWeightUpload' }" 
          custom
          v-slot="{ isActive }"
        >
          <div class="operation-tab active">
            资料上传
          </div>
        </router-link>
        <router-link 
          :to="{ name: 'InboundWeightManage' }" 
          custom
          v-slot="{ navigate, isActive }"
        >
          <div 
            class="operation-tab" 
            :class="{ active: isActive }"
            @click="navigate"
          >
            资料管理
          </div>
        </router-link>
      </div>
      
      <div class="operation-content">
        <div class="upload-form">
          <div class="form-group">
            <label for="doc-name">资料名称</label>
            <input type="text" id="doc-name" v-model="uploadForm.name" placeholder="请输入资料名称" />
          </div>
          
          <div class="form-group">
            <label>资料文件</label>
            <div class="file-upload">
              <input type="file" id="doc-file" @change="handleFileChange" class="file-input" />
              <label for="doc-file" class="file-label">
                <UploadCloudIcon class="upload-icon" />
                <span>{{ uploadForm.file ? uploadForm.file.name : '点击上传文件' }}</span>
              </label>
            </div>
          </div>
          
          <div class="form-group">
            <label>资料详情</label>
            <div class="detail-fields">
              <div class="detail-field">
                <label for="field-weightNo">过磅单号</label>
                <input 
                  type="text" 
                  id="field-weightNo" 
                  v-model="uploadForm.details.weightNo" 
                  placeholder="请输入过磅单号" 
                />
              </div>
              <div class="detail-field">
                <label for="field-supplier">供应商</label>
                <input 
                  type="text" 
                  id="field-supplier" 
                  v-model="uploadForm.details.supplier" 
                  placeholder="请输入供应商" 
                />
              </div>
              <div class="detail-field">
                <label for="field-grossWeight">毛重(kg)</label>
                <input 
                  type="number" 
                  id="field-grossWeight" 
                  v-model="uploadForm.details.grossWeight" 
                  placeholder="请输入毛重" 
                />
              </div>
              <div class="detail-field">
                <label for="field-tareWeight">皮重(kg)</label>
                <input 
                  type="number" 
                  id="field-tareWeight" 
                  v-model="uploadForm.details.tareWeight" 
                  placeholder="请输入皮重" 
                />
              </div>
              <div class="detail-field">
                <label for="field-netWeight">净重(kg)</label>
                <input 
                  type="number" 
                  id="field-netWeight" 
                  v-model="uploadForm.details.netWeight" 
                  placeholder="请输入净重" 
                />
              </div>
              <div class="detail-field">
                <label for="field-weightDate">过磅日期</label>
                <input 
                  type="date" 
                  id="field-weightDate" 
                  v-model="uploadForm.details.weightDate" 
                />
              </div>
            </div>
          </div>
          
          <div class="form-actions">
            <button class="ai-btn" @click="recognizeDocument">
              <ZapIcon class="btn-icon" />
              AI自动识别
            </button>
            <button class="submit-btn" @click="submitUpload">提交</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { useRouter } from 'vue-router';
  import { 
    ArrowLeftIcon, 
    UploadCloudIcon, 
    ZapIcon 
  } from 'lucide-vue-next';
  
  const router = useRouter();
  
  // 上传表单
  const uploadForm = reactive({
    name: '',
    file: null as File | null,
    details: {
      weightNo: '',
      supplier: '',
      grossWeight: '',
      tareWeight: '',
      netWeight: '',
      weightDate: ''
    }
  });
  
  // 处理文件选择
  const handleFileChange = (event: Event) => {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      uploadForm.file = input.files[0];
    }
  };
  
  // AI自动识别文档
  const recognizeDocument = () => {
    if (!uploadForm.file) {
      alert('请先上传文件');
      return;
    }
    
    // 模拟AI识别过程
    console.log('正在识别文档...');
    
    // 模拟识别结果
    setTimeout(() => {
      const grossWeight = Math.floor(Math.random() * 50000);
      const tareWeight = Math.floor(Math.random() * 10000);
      
      uploadForm.details = {
        weightNo: 'IW-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        supplier: '自动识别供应商',
        grossWeight: grossWeight.toString(),
        tareWeight: tareWeight.toString(),
        netWeight: (grossWeight - tareWeight).toString(),
        weightDate: new Date().toISOString().split('T')[0]
      };
      
      alert('文档识别完成');
    }, 1500);
  };
  
  // 提交上传
  const submitUpload = () => {
    if (!uploadForm.name) {
      alert('请输入资料名称');
      return;
    }
    
    if (!uploadForm.file) {
      alert('请上传文件');
      return;
    }
    
    // 这里可以调用API上传文件和数据
    console.log('提交上传', {
      name: uploadForm.name,
      file: uploadForm.file,
      details: uploadForm.details
    });
    
    // 模拟上传成功
    alert('上传成功');
    
    // 上传成功后跳转到管理页面
    router.push({ name: 'InboundWeightManage' });
  };
  </script>
  
  <style scoped>
  .operation-page {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 1.5rem;
  }
  
  .operation-header {
    display: flex;
    align-items: center;
    margin-bottom: 1.5rem;
  }
  
  .back-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 0.75rem;
    background-color: #f5f7fa;
    border: none;
    border-radius: 4px;
    font-size: 0.875rem;
    color: #424242;
    cursor: pointer;
    transition: background-color 0.3s;
    margin-right: 1rem;
    text-decoration: none;
  }
  
  .back-btn:hover {
    background-color: #e0e0e0;
  }
  
  .back-icon {
    width: 16px;
    height: 16px;
  }
  
  .operation-title {
    font-size: 1.5rem;
    font-weight: 600;
    margin: 0;
    color: #0a2463;
  }
  
  .operation-tabs {
    display: flex;
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 1.5rem;
  }
  
  .operation-tab {
    padding: 0.75rem 1.5rem;
    font-size: 0.875rem;
    font-weight: 500;
    color: #757575;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .operation-tab.active {
    color: #1e88e5;
    border-bottom: 2px solid #1e88e5;
  }
  
  .operation-content {
    min-height: 400px;
  }
  
  /* 上传表单样式 */
  .upload-form {
    display: flex;
    flex-direction: column;
    gap: 1.25rem;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .form-group label {
    font-size: 0.875rem;
    font-weight: 500;
    color: #424242;
  }
  
  .form-group input {
    padding: 0.625rem;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    font-size: 0.875rem;
  }
  
  .file-upload {
    position: relative;
  }
  
  .file-input {
    position: absolute;
    width: 0;
    height: 0;
    opacity: 0;
  }
  
  .file-label {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 1rem;
    border: 2px dashed #e0e0e0;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .file-label:hover {
    border-color: #1e88e5;
  }
  
  .upload-icon {
    width: 24px;
    height: 24px;
    color: #1e88e5;
  }
  
  .detail-fields {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 1rem;
  }
  
  .detail-field {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1rem;
  }
  
  .ai-btn,
  .submit-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.625rem 1rem;
    border: none;
    border-radius: 4px;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .ai-btn {
    background-color: #f5f5f5;
    color: #424242;
  }
  
  .ai-btn:hover {
    background-color: #e0e0e0;
  }
  
  .submit-btn {
    background-color: #1e88e5;
    color: white;
  }
  
  .submit-btn:hover {
    background-color: #1976d2;
  }
  
  .btn-icon {
    width: 16px;
    height: 16px;
  }
  </style>
  
  