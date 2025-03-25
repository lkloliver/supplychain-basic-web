<template>
    <div class="operation-page">
      <div class="operation-header">
        <router-link :to="{ name: 'SalesInvoice' }" class="back-btn">
          <ArrowLeftIcon class="back-icon" />
          <span>返回</span>
        </router-link>
        <h2 class="operation-title">销售开票</h2>
      </div>
      
      <div class="operation-tabs">
        <router-link 
          :to="{ name: 'SalesInvoiceUpload' }" 
          custom
          v-slot="{ navigate, isActive }"
        >
          <div 
            class="operation-tab" 
            :class="{ active: isActive }"
            @click="navigate"
          >
            资料上传
          </div>
        </router-link>
        <router-link 
          :to="{ name: 'SalesInvoiceManage' }" 
          custom
          v-slot="{ isActive }"
        >
          <div class="operation-tab active">
            资料管理
          </div>
        </router-link>
      </div>
      
      <div class="operation-content">
        <div class="manage-content">
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
  </template>
  
  <script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { 
    ArrowLeftIcon, 
    SearchIcon, 
    FileTextIcon, 
    EyeIcon, 
    EditIcon, 
    TrashIcon 
  } from 'lucide-vue-next';
  
  // 搜索查询
  const searchQuery = ref('');
  
  // 模拟文档数据
  const documents = ref([
    {
      id: 1,
      name: '2023年1月销售开票',
      type: 'sales-invoice',
      uploadTime: '2023-01-25 14:30',
      details: {
        invoiceNo: '1234567890',
        customer: '客户A',
        contractNo: 'SC-2023-001',
        amount: '145000',
        invoiceDate: '2023-01-25',
        taxRate: '13',
        remark: '产品销售'
      }
    },
    {
      id: 2,
      name: '2023年2月销售开票',
      type: 'sales-invoice',
      uploadTime: '2023-02-28 10:45',
      details: {
        invoiceNo: '0987654321',
        customer: '客户B',
        contractNo: 'SC-2023-002',
        amount: '195000',
        invoiceDate: '2023-02-28',
        taxRate: '13',
        remark: '产品销售'
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
  </style>
  
  