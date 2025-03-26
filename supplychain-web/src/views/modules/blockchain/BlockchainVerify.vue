<template>
    <div class="blockchain-verify">
      <div class="module-header">
        <h1 class="module-title">区块链验证</h1>
      </div>
  
      <div class="verify-container">
        <div class="verify-tabs">
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'file' }" 
            @click="activeTab = 'file'"
          >
            文件验证
          </button>
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'hash' }" 
            @click="activeTab = 'hash'"
          >
            哈希验证
          </button>
        </div>
  
        <div class="verify-content">
          <div v-if="activeTab === 'file'" class="file-verify">
            <div class="upload-area" @click="triggerFileInput" @dragover.prevent @drop.prevent="onFileDrop">
              <input 
                type="file" 
                ref="fileInput" 
                style="display: none" 
                @change="onFileSelected" 
              />
              <div class="upload-icon">
                <UploadCloudIcon class="icon large-icon" />
              </div>
              <div class="upload-text">
                <p>点击或拖拽文件到此处上传</p>
                <p class="upload-hint">支持PDF、Word、Excel、图片等常见文件格式</p>
              </div>
            </div>
  
            <div v-if="fileInfo" class="file-info">
              <div class="file-info-header">
                <div class="file-icon">
                  <FileIcon class="icon" />
                </div>
                <div class="file-details">
                  <div class="file-name">{{ fileInfo.name }}</div>
                  <div class="file-meta">{{ formatFileSize(fileInfo.size) }} | {{ fileInfo.type || '未知类型' }}</div>
                </div>
                <button class="btn-icon" @click="clearFile">
                  <XIcon class="icon" />
                </button>
              </div>
              <div class="file-hash">
                <div class="hash-label">文件哈希 (SHA-256):</div>
                <div class="hash-value">{{ fileHash }}</div>
                <button v-if="fileHash" class="copy-btn" @click="copyToClipboard(fileHash)">
                  <CopyIcon class="icon" />
                </button>
              </div>
            </div>
  
            <button 
              class="btn btn-primary verify-btn" 
              :disabled="!fileHash" 
              @click="verifyFile"
            >
              <ShieldIcon class="icon" />
              验证文件
            </button>
          </div>
  
          <div v-if="activeTab === 'hash'" class="hash-verify">
            <div class="form-group">
              <label for="hash-input">输入文件哈希值 (SHA-256)</label>
              <div class="hash-input-container">
                <input 
                  id="hash-input"
                  type="text" 
                  v-model="inputHash" 
                  class="form-control" 
                  placeholder="例如: 0x8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b"
                />
                <button v-if="inputHash" class="clear-btn" @click="inputHash = ''">
                  <XIcon class="icon" />
                </button>
              </div>
            </div>
  
            <button 
              class="btn btn-primary verify-btn" 
              :disabled="!inputHash" 
              @click="verifyHash"
            >
              <ShieldIcon class="icon" />
              验证哈希
            </button>
          </div>
        </div>
      </div>
  
      <!-- 验证结果 -->
      <div v-if="verificationResult" class="verification-result" :class="verificationResult.success ? 'success' : 'error'">
        <div class="result-icon">
          <CheckCircleIcon v-if="verificationResult.success" class="icon large-icon success-icon" />
          <XCircleIcon v-else class="icon large-icon error-icon" />
        </div>
        <div class="result-content">
          <h2 class="result-title">{{ verificationResult.success ? '验证成功' : '验证失败' }}</h2>
          <p class="result-message">{{ verificationResult.message }}</p>
          
          <div v-if="verificationResult.success && verificationResult.data" class="result-details">
            <div class="detail-group">
              <div class="detail-item">
                <div class="detail-label">文档类型</div>
                <div class="detail-value">{{ getDocumentTypeName(verificationResult.data.documentType) }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">文档编号</div>
                <div class="detail-value">{{ verificationResult.data.documentNumber }}</div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item">
                <div class="detail-label">上链时间</div>
                <div class="detail-value">{{ formatDate(verificationResult.data.timestamp, true) }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">区块高度</div>
                <div class="detail-value">{{ verificationResult.data.blockHeight }}</div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item full-width">
                <div class="detail-label">交易哈希</div>
                <div class="detail-value hash-container">
                  {{ verificationResult.data.txHash }}
                  <button class="copy-btn" @click="copyToClipboard(verificationResult.data.txHash)">
                    <CopyIcon class="icon" />
                  </button>
                </div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item">
                <div class="detail-label">区块链网络</div>
                <div class="detail-value">{{ verificationResult.data.network }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">确认数</div>
                <div class="detail-value">{{ verificationResult.data.confirmations }}</div>
              </div>
            </div>
            
            <div class="result-actions">
              <button class="btn btn-primary" @click="viewOnExplorer(verificationResult.data)">
                <ExternalLinkIcon class="icon" />
                在区块浏览器中查看
              </button>
              <button class="btn btn-outline" @click="clearResult">
                <RefreshCwIcon class="icon" />
                重新验证
              </button>
            </div>
          </div>
          
          <div v-else-if="!verificationResult.success" class="result-actions">
            <button class="btn btn-outline" @click="clearResult">
              <RefreshCwIcon class="icon" />
              重新验证
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { 
    UploadCloud as UploadCloudIcon,
    File as FileIcon,
    Copy as CopyIcon,
    Shield as ShieldIcon,
    CheckCircle as CheckCircleIcon,
    XCircle as XCircleIcon,
    ExternalLink as ExternalLinkIcon,
    RefreshCw as RefreshCwIcon,
    X as XIcon
  } from 'lucide-vue-next';
  
  interface VerificationResult {
    success: boolean;
    message: string;
    data?: {
      documentType: string;
      documentNumber: string;
      fileName: string;
      fileHash: string;
      timestamp: number;
      blockHeight: number;
      txHash: string;
      confirmations: number;
      network: string;
    };
  }
  
  const route = useRoute();
  const router = useRouter();
  
  const activeTab = ref('file');
  const fileInput = ref<HTMLInputElement | null>(null);
  const fileInfo = ref<File | null>(null);
  const fileHash = ref('');
  const inputHash = ref('');
  const verificationResult = ref<VerificationResult | null>(null);
  
  // 模拟数据库中的哈希记录
  const hashDatabase = [
    {
      fileHash: '0x8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b',
      documentType: 'purchase-contract',
      documentNumber: 'PC-2023-001',
      fileName: '采购合同-2023-001.pdf',
      timestamp: Date.now() - 86400000 * 2, // 2天前
      blockHeight: 12345678,
      txHash: '0x3a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2',
      confirmations: 1024,
      network: 'BSN 文昌链'
    },
    {
      fileHash: '0x7b35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19c',
      documentType: 'sales-contract',
      documentNumber: 'SC-2023-002',
      fileName: '销售合同-2023-002.pdf',
      timestamp: Date.now() - 86400000, // 1天前
      blockHeight: 12345679,
      txHash: '0x4a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b3',
      confirmations: 512,
      network: 'BSN 文昌链'
    }
  ];
  
  // 方法
  function triggerFileInput() {
    if (fileInput.value) {
      fileInput.value.click();
    }
  }
  
  function onFileSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      fileInfo.value = target.files[0];
      calculateFileHash(fileInfo.value);
    }
  }
  
  function onFileDrop(event: DragEvent) {
    if (event.dataTransfer && event.dataTransfer.files.length > 0) {
      fileInfo.value = event.dataTransfer.files[0];
      calculateFileHash(fileInfo.value);
    }
  }
  
  function clearFile() {
    fileInfo.value = null;
    fileHash.value = '';
    if (fileInput.value) {
      fileInput.value.value = '';
    }
    verificationResult.value = null;
  }
  
  function clearResult() {
    verificationResult.value = null;
  }
  
  async function calculateFileHash(file: File) {
    try {
      fileHash.value = '计算中...';
      
      // 在实际应用中，这里会使用Web Crypto API计算文件的SHA-256哈希值
      // 这里为了演示，我们使用setTimeout模拟异步计算过程
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // 生成一个模拟的哈希值
      const mockHash = '0x8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b';
      fileHash.value = mockHash;
    } catch (error) {
      console.error('计算文件哈希出错:', error);
      fileHash.value = '计算失败';
    }
  }
  
  function verifyFile() {
    if (!fileHash.value || fileHash.value === '计算中...' || fileHash.value === '计算失败') {
      return;
    }
    
    verifyHash(fileHash.value);
  }
  
  function verifyHash(hash = inputHash.value) {
    // 在实际应用中，这里会调用API验证哈希值
    const record = hashDatabase.find(item => item.fileHash === hash);
    
    if (record) {
      verificationResult.value = {
        success: true,
        message: '该文件已在区块链上存证，验证通过！',
        data: record
      };
    } else {
      verificationResult.value = {
        success: false,
        message: '未找到该文件的区块链存证记录，验证失败。'
      };
    }
  }
  
  function copyToClipboard(text: string) {
    navigator.clipboard.writeText(text).then(() => {
      alert('已复制到剪贴板');
    });
  }
  
  function formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }
  
  function formatDate(timestamp: number, includeTime = false): string {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    
    if (!includeTime) {
      return `${year}-${month}-${day}`;
    }
    
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }
  
  function getDocumentTypeName(type: string): string {
    const typeMap: Record<string, string> = {
      'purchase-contract': '采购合同',
      'sales-contract': '销售合同',
      'transport-contract': '运输合同',
      'inbound-weight': '入库过磅',
      'outbound-weight': '出库过磅',
      'settlement': '结算单',
      'invoice': '发票'
    };
    
    return typeMap[type] || type;
  }
  
  function viewOnExplorer(data: any) {
    // 在实际应用中，这里会打开区块链浏览器
    window.open(`https://explorer.bsnbase.com/tx/${data.txHash}`, '_blank');
  }
  
  onMounted(() => {
    // 如果URL中有hash参数，自动填入并切换到哈希验证标签
    const hashParam = route.query.hash as string;
    if (hashParam) {
      inputHash.value = hashParam;
      activeTab.value = 'hash';
      verifyHash();
    }
  });
  </script>
  
  <style scoped>
  .blockchain-verify {
    padding: 20px;
  }
  
  .module-header {
    margin-bottom: 20px;
  }
  
  .module-title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
  }
  
  .verify-container {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    margin-bottom: 20px;
  }
  
  .verify-tabs {
    display: flex;
    border-bottom: 1px solid #eee;
  }
  
  .tab-btn {
    flex: 1;
    padding: 15px;
    text-align: center;
    background: none;
    border: none;
    cursor: pointer;
    font-size: 1rem;
    font-weight: 500;
    color: #666;
    transition: all 0.2s;
  }
  
  .tab-btn:hover {
    background-color: #f9f9f9;
  }
  
  .tab-btn.active {
    color: #1e88e5;
    border-bottom: 2px solid #1e88e5;
  }
  
  .verify-content {
    padding: 20px;
  }
  
  .file-verify, .hash-verify {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .upload-area {
    border: 2px dashed #ddd;
    border-radius: 8px;
    padding: 40px 20px;
    text-align: center;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .upload-area:hover {
    border-color: #1e88e5;
    background-color: rgba(30, 136, 229, 0.05);
  }
  
  .upload-icon {
    margin-bottom: 15px;
  }
  
  .large-icon {
    width: 48px;
    height: 48px;
  }
  
  .upload-text p {
    margin: 5px 0;
    color: #666;
  }
  
  .upload-hint {
    font-size: 0.875rem;
    color: #999;
  }
  
  .file-info {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 15px;
  }
  
  .file-info-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
  }
  
  .file-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    background-color: #e3f2fd;
    border-radius: 8px;
    color: #1e88e5;
  }
  
  .file-details {
    flex: 1;
  }
  
  .file-name {
    font-weight: 500;
    margin-bottom: 5px;
    word-break: break-all;
  }
  
  .file-meta {
    font-size: 0.75rem;
    color: #666;
  }
  
  .file-hash {
    display: flex;
    align-items: center;
    gap: 5px;
    flex-wrap: wrap;
  }
  
  .hash-label {
    font-size: 0.875rem;
    color: #666;
    margin-right: 5px;
  }
  
  .hash-value {
    font-family: monospace;
    word-break: break-all;
    background-color: #f0f0f0;
    padding: 5px 8px;
    border-radius: 4px;
    flex: 1;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 5px;
  }
  
  .form-group label {
    font-size: 0.875rem;
    color: #666;
  }
  
  .hash-input-container {
    position: relative;
  }
  
  .form-control {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 0.875rem;
    font-family: monospace;
  }
  
  .clear-btn {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    cursor: pointer;
    color: #999;
  }
  
  .verify-btn {
    align-self: center;
    min-width: 150px;
  }
  
  .btn {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 5px;
    padding: 10px 20px;
    border-radius: 4px;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .btn-primary {
    background-color: #1e88e5;
    color: white;
    border: none;
  }
  
  .btn-primary:hover:not(:disabled) {
    background-color: #1976d2;
  }
  
  .btn-outline {
    background-color: transparent;
    color: #1e88e5;
    border: 1px solid #1e88e5;
  }
  
  .btn-outline:hover:not(:disabled) {
    background-color: rgba(30, 136, 229, 0.1);
  }
  
  .btn-icon {
    padding: 6px;
    border-radius: 4px;
    background-color: transparent;
    color: #666;
    border: none;
  }
  
  .btn-icon:hover {
    background-color: rgba(0, 0, 0, 0.05);
    color: #333;
  }
  
  .icon {
    width: 16px;
    height: 16px;
  }
  
  .copy-btn {
    background: none;
    border: none;
    cursor: pointer;
    color: #1e88e5;
    padding: 2px;
  }
  
  .copy-btn:hover {
    color: #1976d2;
  }
  
  .verification-result {
    display: flex;
    gap: 20px;
    padding: 20px;
    border-radius: 8px;
    margin-top: 20px;
  }
  
  .verification-result.success {
    background-color: rgba(76, 175, 80, 0.1);
  }
  
  .verification-result.error {
    background-color: rgba(244, 67, 54, 0.1);
  }
  
  .result-icon {
    display: flex;
    align-items: flex-start;
  }
  
  .success-icon {
    color: #4caf50;
  }
  
  .error-icon {
    color: #f44336;
  }
  
  .result-content {
    flex: 1;
  }
  
  .result-title {
    font-size: 1.25rem;
    font-weight: 600;
    margin: 0 0 10px 0;
  }
  
  .verification-result.success .result-title {
    color: #2e7d32;
  }
  
  .verification-result.error .result-title {
    color: #c62828;
  }
  
  .result-message {
    margin: 0 0 15px 0;
    color: #333;
  }
  
  .result-details {
    background-color: white;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 15px;
  }
  
  .detail-group {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 15px;
    gap: 20px;
  }
  
  .detail-item {
    flex: 1;
    min-width: 200px;
  }
  
  .detail-item.full-width {
    flex-basis: 100%;
  }
  
  .detail-label {
    font-size: 0.875rem;
    color: #666;
    margin-bottom: 5px;
  }
  
  .detail-value {
    font-size: 1rem;
    color: #333;
  }
  
  .hash-container {
    display: flex;
    align-items: center;
    gap: 5px;
    font-family: monospace;
    word-break: break-all;
    background-color: #f5f5f5;
    padding: 8px;
    border-radius: 4px;
  }
  
  .result-actions {
    display: flex;
    gap: 10px;
    margin-top: 15px;
  }
  </style>
  
  