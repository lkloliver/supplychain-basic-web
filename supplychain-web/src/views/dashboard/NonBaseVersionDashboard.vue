<template>
  <div class="dashboard-content">
    <h2 class="dashboard-title">非基地版仪表盘</h2>
    
    <!-- 如果没有选择模块，显示所有模块卡片 -->
    <template v-if="!activeModule">
      <!-- 原有的模块内容保持不变 -->
      <!-- 合同模块 -->
      <div class="module">
        <h3 class="module-title">合同模块</h3>
        <div class="module-grid">
          <div class="module-card" @click="openOperation('purchase-contract')">
            <FileTextIcon class="card-icon" />
            <span class="card-title">采购合同</span>
          </div>
          <div class="module-card" @click="openOperation('sales-contract')">
            <FileTextIcon class="card-icon" />
            <span class="card-title">销售合同</span>
          </div>
          <div class="module-card" @click="openOperation('transport-contract')">
            <TruckIcon class="card-icon" />
            <span class="card-title">运输合同</span>
          </div>
        </div>
      </div>
      
      <!-- 货物模块 -->
      <div class="module">
        <h3 class="module-title">货物模块</h3>
        <div class="module-grid">
          <div class="module-card" @click="openOperation('factory-weight')">
            <ScaleIcon class="card-icon" />
            <span class="card-title">到货厂家过磅单</span>
          </div>
          <div class="module-card" @click="openOperation('factory-quality')">
            <ClipboardCheckIcon class="card-icon" />
            <span class="card-title">到货厂家质检单</span>
          </div>
          <div class="module-card" @click="openOperation('factory-image')">
            <ImageIcon class="card-icon" />
            <span class="card-title">货物进厂影像</span>
          </div>
          <div class="module-card" @click="openOperation('waybill')">
            <FileTextIcon class="card-icon" />
            <span class="card-title">运单</span>
          </div>
        </div>
      </div>
      
      <!-- 结算模块 -->
      <div class="module">
        <h3 class="module-title">结算模块</h3>
        <div class="module-grid">
          <!-- 结算模块内容 -->
        </div>
      </div>
      
      <!-- 资金模块 -->
      <div class="module">
        <h3 class="module-title">资金模块</h3>
        <div class="module-grid">
          <div class="module-card" @click="openOperation('purchase-payment')">
            <CreditCardIcon class="card-icon" />
            <span class="card-title">采购付款记录</span>
          </div>
          <div class="module-card" @click="openOperation('sales-receipt')">
            <DollarSignIcon class="card-icon" />
            <span class="card-title">销售收款记录</span>
          </div>
          <div class="module-card" @click="openOperation('transport-payment')">
            <TruckIcon class="card-icon" />
            <span class="card-title">运输付款记录</span>
          </div>
        </div>
      </div>
      
      <!-- 发票模块 -->
      <div class="module">
        <h3 class="module-title">发票模块</h3>
        <div class="module-grid">
          <div class="module-card" @click="openOperation('purchase-invoice-in')">
            <FileTextIcon class="card-icon" />
            <span class="card-title">采购收票</span>
          </div>
          <div class="module-card" @click="openOperation('purchase-invoice-out')">
            <FileTextIcon class="card-icon" />
            <span class="card-title">采购反向开票</span>
          </div>
          <div class="module-card" @click="openOperation('sales-invoice')">
            <FileTextIcon class="card-icon" />
            <span class="card-title">销售开票</span>
          </div>
          <div class="module-card" @click="openOperation('transport-invoice')">
            <FileTextIcon class="card-icon" />
            <span class="card-title">运输收票</span>
          </div>
        </div>
      </div>
      
      <!-- 台账模块 -->
      <div class="module">
        <h3 class="module-title">台账模块</h3>
        <div class="module-grid">
          <div class="module-card" @click="openOperation('purchase-ledger')">
            <BookIcon class="card-icon" />
            <span class="card-title">采购台账</span>
          </div>
          <div class="module-card" @click="openOperation('sales-ledger')">
            <BookIcon class="card-icon" />
            <span class="card-title">销售台账</span>
          </div>
        </div>
      </div>
    </template>
    
    <!-- 如果选择了模块，只显示该模块的操作界面 -->
    <template v-else>
      <div class="selected-module">
        <div class="module-header">
          <h3 class="module-title">{{ getModuleTitle(activeModule) }}</h3>
          <div class="module-actions">
            <button class="action-btn upload-btn" @click="openOperation(activeModule)">
              <UploadCloudIcon class="btn-icon" />
              <span>上传资料</span>
            </button>
            <button class="action-btn manage-btn" @click="openManagement(activeModule)">
              <ListIcon class="btn-icon" />
              <span>管理资料</span>
            </button>
          </div>
        </div>
        
        <div class="module-content">
          <!-- 这里可以根据不同的模块显示不同的内容 -->
          <div class="module-description">
            <p>您当前正在查看 <strong>{{ getModuleTitle(activeModule) }}</strong> 模块。</p>
            <p>请使用上方的按钮上传或管理资料。</p>
          </div>
        </div>
      </div>
    </template>
    
    <!-- 操作弹窗 -->
    <div v-if="showOperationModal" class="modal-overlay" @click="closeModal">
      <div class="modal-container" @click.stop>
        <div class="modal-header">
          <h3 class="modal-title">{{ currentOperation.title }}</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-tabs">
          <div 
            :class="['modal-tab', { active: activeTab === 'upload' }]" 
            @click="activeTab = 'upload'"
          >
            资料上传
          </div>
          <div 
            :class="['modal-tab', { active: activeTab === 'manage' }]" 
            @click="activeTab = 'manage'"
          >
            资料管理
          </div>
        </div>
        <div class="modal-content">
          <!-- 资料上传 -->
          <div v-if="activeTab === 'upload'" class="upload-form">
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
                <!-- 动态生成的字段，根据不同的操作类型显示不同的字段 -->
                <div v-for="(field, index) in currentOperation.fields" :key="index" class="detail-field">
                  <label :for="`field-${index}`">{{ field.label }}</label>
                  <input 
                    :type="field.type" 
                    :id="`field-${index}`" 
                    v-model="uploadForm.details[field.key]" 
                    :placeholder="`请输入${field.label}`" 
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
          
          <!-- 资料管理 -->
          <div v-else class="manage-content">
            <div class="search-bar">
              <input type="text" v-model="searchQuery" placeholder="搜索资料..." />
              <SearchIcon class="search-icon" />
            </div>
            
            <div class="document-list">
              <div v-if="documents.length === 0" class="empty-state">
                暂无资料，请先上传
              </div>
              <div v-else v-for="(doc, index) in filteredDocuments" :key="index" class="document-item">
                <div class="doc-info">
                  <FileTextIcon class="doc-icon" />
                  <div class="doc-details">
                    <div class="doc-name">{{ doc.name }}</div>
                    <div class="doc-meta">上传时间: {{ doc.uploadTime }}</div>
                  </div>
                </div>
                <div class="doc-actions">
                  <button class="action-btn view-btn" @click="viewDocument(doc)">
                    <EyeIcon class="action-icon" />
                  </button>
                  <button class="action-btn edit-btn" @click="editDocument(doc)">
                    <EditIcon class="action-icon" />
                  </button>
                  <button class="action-btn delete-btn" @click="deleteDocument(doc)">
                    <TrashIcon class="action-icon" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed } from 'vue';
import { 
  FileTextIcon, 
  TruckIcon, 
  ScaleIcon, 
  ClipboardCheckIcon,
  ImageIcon,
  CreditCardIcon,
  DollarSignIcon,
  BookIcon,
  UploadCloudIcon,
  SearchIcon,
  EyeIcon,
  EditIcon,
  TrashIcon,
  ZapIcon,
  ListIcon
} from 'lucide-vue-next';

// 添加在 script 标签下方，import 语句之后
const props = defineProps<{
  activeModule?: string;
}>();

// 操作弹窗
const showOperationModal = ref(false);
const activeTab = ref('upload');

// 当前操作
const currentOperation = ref({
  key: '',
  title: '',
  fields: [] as { key: string; label: string; type: string }[]
});

// 上传表单
const uploadForm = reactive({
  name: '',
  file: null as File | null,
  details: {} as Record<string, string>
});

// 搜索查询
const searchQuery = ref('');

// 模拟文档数据
const documents = ref([
  {
    id: 1,
    name: '2023年Q1采购合同',
    type: 'purchase-contract',
    uploadTime: '2023-01-15 14:30',
    details: {
      contractNo: 'PC-2023-001',
      supplier: '供应商A',
      amount: '100000',
      startDate: '2023-01-01',
      endDate: '2023-03-31'
    }
  },
  {
    id: 2,
    name: '2023年Q1销售合同',
    type: 'sales-contract',
    uploadTime: '2023-01-20 10:15',
    details: {
      contractNo: 'SC-2023-001',
      customer: '客户B',
      amount: '150000',
      startDate: '2023-01-01',
      endDate: '2023-03-31'
    }
  }
]);

// 过滤后的文档
const filteredDocuments = computed(() => {
  if (!searchQuery.value) return documents.value;
  return documents.value.filter(doc => 
    doc.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 操作类型定义
const operationTypes = {
  'purchase-contract': {
    title: '采购合同',
    fields: [
      { key: 'contractNo', label: '合同编号', type: 'text' },
      { key: 'supplier', label: '供应商', type: 'text' },
      { key: 'amount', label: '合同金额', type: 'number' },
      { key: 'startDate', label: '开始日期', type: 'date' },
      { key: 'endDate', label: '结束日期', type: 'date' }
    ]
  },
  'sales-contract': {
    title: '销售合同',
    fields: [
      { key: 'contractNo', label: '合同编号', type: 'text' },
      { key: 'customer', label: '客户', type: 'text' },
      { key: 'amount', label: '合同金额', type: 'number' },
      { key: 'startDate', label: '开始日期', type: 'date' },
      { key: 'endDate', label: '结束日期', type: 'date' }
    ]
  },
  'transport-contract': {
    title: '运输合同',
    fields: [
      { key: 'contractNo', label: '合同编号', type: 'text' },
      { key: 'transporter', label: '运输商', type: 'text' },
      { key: 'amount', label: '合同金额', type: 'number' },
      { key: 'startDate', label: '开始日期', type: 'date' },
      { key: 'endDate', label: '结束日期', type: 'date' }
    ]
  },
  'factory-weight': {
    title: '到货厂家过磅单',
    fields: [
      { key: 'weightNo', label: '过磅单号', type: 'text' },
      { key: 'factory', label: '到货厂家', type: 'text' },
      { key: 'grossWeight', label: '毛重(kg)', type: 'number' },
      { key: 'tareWeight', label: '皮重(kg)', type: 'number' },
      { key: 'netWeight', label: '净重(kg)', type: 'number' },
      { key: 'weightDate', label: '过磅日期', type: 'date' }
    ]
  },
  // 其他操作类型定义...
};

// 获取模块标题
const getModuleTitle = (moduleKey: string) => {
  const operation = operationTypes[moduleKey as keyof typeof operationTypes];
  return operation ? operation.title : moduleKey;
};

// 打开操作弹窗
const openOperation = (key: string) => {
  const operation = operationTypes[key as keyof typeof operationTypes];
  if (operation) {
    currentOperation.value = {
      key,
      title: operation.title,
      fields: operation.fields
    };
    
    // 重置表单
    uploadForm.name = '';
    uploadForm.file = null;
    uploadForm.details = {};
    
    showOperationModal.value = true;
    activeTab.value = 'upload';
  }
};

// 打开管理界面
const openManagement = (key: string) => {
  const operation = operationTypes[key as keyof typeof operationTypes];
  if (operation) {
    currentOperation.value = {
      key,
      title: operation.title,
      fields: operation.fields
    };
    
    showOperationModal.value = true;
    activeTab.value = 'manage';
  }
};

// 关闭弹窗
const closeModal = () => {
  showOperationModal.value = false;
};

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
    // 根据当前操作类型填充不同的模拟数据
    if (currentOperation.value.key === 'purchase-contract') {
      uploadForm.details = {
        contractNo: 'PC-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        supplier: '自动识别供应商',
        amount: Math.floor(Math.random() * 1000000).toString(),
        startDate: new Date().toISOString().split('T')[0],
        endDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
      };
    } else if (currentOperation.value.key === 'factory-weight') {
      uploadForm.details = {
        weightNo: 'W-' + new Date().getFullYear() + '-' + Math.floor(Math.random() * 1000).toString().padStart(3, '0'),
        factory: '自动识别厂家',
        grossWeight: Math.floor(Math.random() * 50000).toString(),
        tareWeight: Math.floor(Math.random() * 10000).toString(),
        netWeight: Math.floor(Math.random() * 40000).toString(),
        weightDate: new Date().toISOString().split('T')[0]
      };
    }
    
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
    details: uploadForm.details,
    type: currentOperation.value.key
  });
  
  // 模拟上传成功
  alert('上传成功');
  closeModal();
};

// 查看文档
const viewDocument = (doc: any) => {
  console.log('查看文档', doc);
  // 这里可以打开文档预览
};

// 编辑文档
const editDocument = (doc: any) => {
  console.log('编辑文档', doc);
  // 这里可以打开编辑界面
};

// 删除文档
const deleteDocument = (doc: any) => {
  if (confirm('确定要删除该文档吗？')) {
    console.log('删除文档', doc);
    // 这里可以调用API删除文档
    documents.value = documents.value.filter(item => item.id !== doc.id);
  }
};
</script>

<style scoped>
.dashboard-content {
  padding: 1rem;
}

.dashboard-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  color: #0a2463;
}

.module {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.module-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-top: 0;
  margin-bottom: 1.25rem;
  color: #0a2463;
}

.module-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 1rem;
}

.module-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.3s;
}

.module-card:hover {
  background-color: #e3f2fd;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-icon {
  width: 32px;
  height: 32px;
  color: #1e88e5;
  margin-bottom: 0.75rem;
}

.card-title {
  font-size: 0.875rem;
  font-weight: 500;
  text-align: center;
  color: #424242;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-container {
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e0e0e0;
}

.modal-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  color: #0a2463;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #757575;
  cursor: pointer;
}

.modal-tabs {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
}

.modal-tab {
  padding: 0.75rem 1.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: #757575;
  cursor: pointer;
  transition: all 0.3s;
}

.modal-tab.active {
  color: #1e88e5;
  border-bottom: 2px solid #1e88e5;
}

.modal-content {
  padding: 1.5rem;
  overflow-y: auto;
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

/* 资料管理样式 */
.manage-content {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.search-bar {
  position: relative;
}

.search-bar input {
  width: 100%;
  padding: 0.625rem 2.5rem 0.625rem 0.625rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 0.875rem;
}

.search-icon {
  position: absolute;
  right: 0.625rem;
  top: 50%;
  transform: translateY(-50%);
  width: 18px;
  height: 18px;
  color: #757575;
}

.document-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.empty-state {
  padding: 2rem;
  text-align: center;
  color: #757575;
}

.document-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background-color: #f5f7fa;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.document-item:hover {
  background-color: #e3f2fd;
}

.doc-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.doc-icon {
  width: 24px;
  height: 24px;
  color: #1e88e5;
}

.doc-name {
  font-weight: 500;
  color: #424242;
}

.doc-meta {
  font-size: 0.75rem;
  color: #757575;
}

.doc-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 4px;
  background-color: transparent;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.action-icon {
  width: 16px;
  height: 16px;
}

.view-btn .action-icon {
  color: #1e88e5;
}

.edit-btn .action-icon {
  color: #ffa000;
}

.delete-btn .action-icon {
  color: #e53935;
}

/* 选中模块样式 */
.selected-module {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.module-actions {
  display: flex;
  gap: 1rem;
}

.action-btn {
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

.upload-btn {
  background-color: #1e88e5;
  color: white;
}

.upload-btn:hover {
  background-color: #1976d2;
}

.manage-btn {
  background-color: #f5f5f5;
  color: #424242;
}

.manage-btn:hover {
  background-color: #e0e0e0;
}

.btn-icon {
  width: 16px;
  height: 16px;
}

.module-content {
  min-height: 300px;
}

.module-description {
  padding: 2rem;
  text-align: center;
  color: #757575;
  background-color: #f5f7fa;
  border-radius: 8px;
}
</style>

