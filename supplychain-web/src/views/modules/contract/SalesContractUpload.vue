<template>
    <div class="module-page">
      <div class="module-header">
        <div class="header-left">
          <router-link :to="{ name: 'DashboardHome' }" class="back-btn">
            <ArrowLeftIcon class="back-icon" />
            <span>返回</span>
          </router-link>
          <h2 class="module-title">销售合同</h2>
        </div>
        <ModuleViewToggle 
          :uploadRoute="'/dashboard/contract/sales/upload'" 
          :manageRoute="'/dashboard/contract/sales/manage'" 
        />
      </div>
      
      <div class="module-content">
        <div class="upload-form">
          <div class="section-header">
            <h3 class="section-title">上传销售合同</h3>
          </div>
          
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
                <label for="field-contractNo">合同编号</label>
                <input 
                  type="text" 
                  id="field-contractNo" 
                  v-model="uploadForm.details.contractNo" 
                  placeholder="请输入合同编号" 
                />
              </div>
              <div class="detail-field">
                <label for="field-customer">客户</label>
                <input 
                  type="text" 
                  id="field-customer" 
                  v-model="uploadForm.details.customer" 
                  placeholder="请输入客户" 
                />
              </div>
              <div class="detail-field">
                <label for="field-amount">合同金额</label>
                <input 
                  type="number" 
                  id="field-amount" 
                  v-model="uploadForm.details.amount" 
                  placeholder="请输入合同金额" 
                />
              </div>
              <div class="detail-field">
                <label for="field-startDate">开始日期</label>
                <input 
                  type="date" 
                  id="field-startDate" 
                  v-model="uploadForm.details.startDate" 
                />
              </div>
              <div class="detail-field">
                <label for="field-endDate">结束日期</label>
                <input 
                  type="date" 
                  id="field-endDate" 
                  v-model="uploadForm.details.endDate" 
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
  import ModuleViewToggle from '@/components/ModuleViewToggle.vue';
  
  const router = useRouter();
  
  // 上传表单
  const uploadForm = reactive({
    name: '',
    file: null as File | null,
    details: {
      contractNo: '',
      customer: '',
      amount: '',
      startDate: '',
      endDate: ''
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
      uploadForm.details = {
        contractNo: 'SC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        customer: '自动识别客户',
        amount: Math.floor(Math.random() * 1000000).toString(),
        startDate: new Date().toISOString().split('T')[0],
        endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
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
    router.push({ name: 'SalesContractManage' });
  };
  </script>
  
  <style scoped>
  .module-page {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 1.5rem;
  }
  
  .module-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
    border-bottom: 1px solid #e0e0e0;
    padding-bottom: 1rem;
  }
  
  .header-left {
    display: flex;
    align-items: center;
    gap: 1rem;
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
    text-decoration: none;
  }
  
  .back-btn:hover {
    background-color: #e0e0e0;
  }
  
  .back-icon {
    width: 16px;
    height: 16px;
  }
  
  .module-title {
    font-size: 1.5rem;
    font-weight: 600;
    margin: 0;
    color: #0a2463;
  }
  
  .module-content {
    min-height: 400px;
  }
  
  .section-header {
    margin-bottom: 1.5rem;
  }
  
  .section-title {
    font-size: 1.25rem;
    font-weight: 500;
    color: #333;
    margin: 0;
    padding-left: 0.5rem;
    border-left: 3px solid #1e88e5;
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
  
  