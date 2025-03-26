<template>
    <div class="blockchain-record">
      <div class="module-header">
        <h1 class="module-title">区块链存证记录</h1>
        <div class="module-actions">
          <button class="btn btn-primary" @click="refreshData">
            <RefreshCwIcon class="icon" />
            刷新
          </button>
        </div>
      </div>
  
      <div class="filter-section">
        <div class="filter-row">
          <div class="filter-item">
            <label>文档类型</label>
            <select v-model="filters.documentType" class="form-control">
              <option value="">全部</option>
              <option value="purchase-contract">采购合同</option>
              <option value="sales-contract">销售合同</option>
              <option value="transport-contract">运输合同</option>
              <option value="inbound-weight">入库过磅</option>
              <option value="outbound-weight">出库过磅</option>
              <option value="settlement">结算单</option>
              <option value="invoice">发票</option>
            </select>
          </div>
          <div class="filter-item">
            <label>上链时间</label>
            <div class="date-range">
              <input type="date" v-model="filters.startDate" class="form-control" />
              <span>至</span>
              <input type="date" v-model="filters.endDate" class="form-control" />
            </div>
          </div>
          <div class="filter-item">
            <label>状态</label>
            <select v-model="filters.status" class="form-control">
              <option value="">全部</option>
              <option value="confirmed">已确认</option>
              <option value="pending">确认中</option>
              <option value="failed">失败</option>
            </select>
          </div>
          <div class="filter-item">
            <button class="btn btn-primary" @click="applyFilters">
              <SearchIcon class="icon" />
              查询
            </button>
            <button class="btn btn-outline" @click="resetFilters">
              <RefreshCwIcon class="icon" />
              重置
            </button>
          </div>
        </div>
      </div>
  
      <div class="data-table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>序号</th>
              <th>文档类型</th>
              <th>文档编号</th>
              <th>文件名</th>
              <th>文件哈希</th>
              <th>上链时间</th>
              <th>区块高度</th>
              <th>交易哈希</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(record, index) in paginatedRecords" :key="record.id">
              <td>{{ (currentPage - 1) * pageSize + index + 1 }}</td>
              <td>{{ getDocumentTypeName(record.documentType) }}</td>
              <td>{{ record.documentNumber }}</td>
              <td>{{ record.fileName }}</td>
              <td class="hash-cell">
                <div class="hash-value">{{ truncateHash(record.fileHash) }}</div>
                <button class="copy-btn" @click="copyToClipboard(record.fileHash)">
                  <CopyIcon class="icon" />
                </button>
              </td>
              <td>{{ formatDate(record.timestamp) }}</td>
              <td>{{ record.blockHeight }}</td>
              <td class="hash-cell">
                <div class="hash-value">{{ truncateHash(record.txHash) }}</div>
                <button class="copy-btn" @click="copyToClipboard(record.txHash)">
                  <CopyIcon class="icon" />
                </button>
              </td>
              <td>
                <span class="status-badge" :class="getStatusClass(record.status)">
                  {{ getStatusName(record.status) }}
                </span>
              </td>
              <td>
                <div class="action-buttons">
                  <button class="btn btn-icon" @click="viewDetails(record)">
                    <EyeIcon class="icon" />
                  </button>
                  <button class="btn btn-icon" @click="verifyRecord(record)">
                    <ShieldIcon class="icon" />
                  </button>
                  <button class="btn btn-icon" @click="viewOnExplorer(record)">
                    <ExternalLinkIcon class="icon" />
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="paginatedRecords.length === 0">
              <td colspan="10" class="empty-table">暂无数据</td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <div class="pagination-container">
        <div class="pagination-info">
          共 {{ totalRecords }} 条记录，每页显示
          <select v-model="pageSize" class="page-size-select">
            <option :value="10">10</option>
            <option :value="20">20</option>
            <option :value="50">50</option>
            <option :value="100">100</option>
          </select>
          条
        </div>
        <div class="pagination">
          <button 
            class="pagination-btn" 
            :disabled="currentPage === 1" 
            @click="currentPage--"
          >
            <ChevronLeftIcon class="icon" />
          </button>
          <div class="page-numbers">
            <button 
              v-for="page in displayedPages" 
              :key="page" 
              class="page-number" 
              :class="{ active: currentPage === page }"
              @click="currentPage = page"
            >
              {{ page }}
            </button>
          </div>
          <button 
            class="pagination-btn" 
            :disabled="currentPage === totalPages" 
            @click="currentPage++"
          >
            <ChevronRightIcon class="icon" />
          </button>
        </div>
      </div>
  
      <!-- 详情弹窗 -->
      <div class="modal" v-if="showDetailModal">
        <div class="modal-content">
          <div class="modal-header">
            <h2>区块链存证详情</h2>
            <button class="close-btn" @click="showDetailModal = false">
              <XIcon class="icon" />
            </button>
          </div>
          <div class="modal-body">
            <div class="detail-group">
              <div class="detail-item">
                <div class="detail-label">文档类型</div>
                <div class="detail-value">{{ getDocumentTypeName(selectedRecord.documentType) }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">文档编号</div>
                <div class="detail-value">{{ selectedRecord.documentNumber }}</div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item">
                <div class="detail-label">文件名</div>
                <div class="detail-value">{{ selectedRecord.fileName }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">文件大小</div>
                <div class="detail-value">{{ formatFileSize(selectedRecord.fileSize) }}</div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item full-width">
                <div class="detail-label">文件哈希 (SHA-256)</div>
                <div class="detail-value hash-container">
                  {{ selectedRecord.fileHash }}
                  <button class="copy-btn" @click="copyToClipboard(selectedRecord.fileHash)">
                    <CopyIcon class="icon" />
                  </button>
                </div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item">
                <div class="detail-label">上链时间</div>
                <div class="detail-value">{{ formatDate(selectedRecord.timestamp, true) }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">状态</div>
                <div class="detail-value">
                  <span class="status-badge" :class="getStatusClass(selectedRecord.status)">
                    {{ getStatusName(selectedRecord.status) }}
                  </span>
                </div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item">
                <div class="detail-label">区块高度</div>
                <div class="detail-value">{{ selectedRecord.blockHeight }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">确认数</div>
                <div class="detail-value">{{ selectedRecord.confirmations }}</div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item full-width">
                <div class="detail-label">交易哈希</div>
                <div class="detail-value hash-container">
                  {{ selectedRecord.txHash }}
                  <button class="copy-btn" @click="copyToClipboard(selectedRecord.txHash)">
                    <CopyIcon class="icon" />
                  </button>
                </div>
              </div>
            </div>
            <div class="detail-group">
              <div class="detail-item full-width">
                <div class="detail-label">区块链网络</div>
                <div class="detail-value">{{ selectedRecord.network }}</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-outline" @click="showDetailModal = false">关闭</button>
            <button class="btn btn-primary" @click="verifyRecord(selectedRecord)">验证</button>
            <button class="btn btn-primary" @click="viewOnExplorer(selectedRecord)">在区块浏览器中查看</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, onMounted, reactive } from 'vue';
  import { useRouter } from 'vue-router';
  import { 
    RefreshCw as RefreshCwIcon, 
    Search as SearchIcon, 
    Copy as CopyIcon,
    Eye as EyeIcon,
    Shield as ShieldIcon,
    ExternalLink as ExternalLinkIcon,
    ChevronLeft as ChevronLeftIcon,
    ChevronRight as ChevronRightIcon,
    X as XIcon
  } from 'lucide-vue-next';
  
  interface BlockchainRecord {
    id: string;
    documentType: string;
    documentNumber: string;
    fileName: string;
    fileHash: string;
    fileSize: number;
    timestamp: number;
    blockHeight: number;
    txHash: string;
    status: 'confirmed' | 'pending' | 'failed';
    confirmations: number;
    network: string;
  }
  
  const router = useRouter();
  
  // 模拟数据
  const mockRecords: BlockchainRecord[] = [
    {
      id: '1',
      documentType: 'purchase-contract',
      documentNumber: 'PC-2023-001',
      fileName: '采购合同-2023-001.pdf',
      fileHash: '0x8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b',
      fileSize: 1024 * 1024 * 2.5, // 2.5MB
      timestamp: Date.now() - 86400000 * 2, // 2天前
      blockHeight: 12345678,
      txHash: '0x3a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2',
      status: 'confirmed',
      confirmations: 1024,
      network: 'BSN 文昌链'
    },
    {
      id: '2',
      documentType: 'sales-contract',
      documentNumber: 'SC-2023-002',
      fileName: '销售合同-2023-002.pdf',
      fileHash: '0x7b35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19c',
      fileSize: 1024 * 1024 * 1.8, // 1.8MB
      timestamp: Date.now() - 86400000, // 1天前
      blockHeight: 12345679,
      txHash: '0x4a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b3',
      status: 'confirmed',
      confirmations: 512,
      network: 'BSN 文昌链'
    },
    {
      id: '3',
      documentType: 'inbound-weight',
      documentNumber: 'IW-2023-003',
      fileName: '入库过磅单-2023-003.pdf',
      fileHash: '0x6c35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19d',
      fileSize: 1024 * 512, // 512KB
      timestamp: Date.now() - 3600000 * 5, // 5小时前
      blockHeight: 12345680,
      txHash: '0x5a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b4',
      status: 'pending',
      confirmations: 10,
      network: 'BSN 文昌链'
    },
    {
      id: '4',
      documentType: 'invoice',
      documentNumber: 'INV-2023-004',
      fileName: '发票-2023-004.pdf',
      fileHash: '0x5d35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19e',
      fileSize: 1024 * 256, // 256KB
      timestamp: Date.now() - 3600000 * 2, // 2小时前
      blockHeight: 0,
      txHash: '0x6a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b5',
      status: 'failed',
      confirmations: 0,
      network: 'BSN 文昌链'
    }
  ];
  
  // 状态变量
  const records = ref<BlockchainRecord[]>([...mockRecords]);
  const filters = reactive({
    documentType: '',
    startDate: '',
    endDate: '',
    status: ''
  });
  const currentPage = ref(1);
  const pageSize = ref(10);
  const showDetailModal = ref(false);
  const selectedRecord = ref<BlockchainRecord>({
    id: '',
    documentType: '',
    documentNumber: '',
    fileName: '',
    fileHash: '',
    fileSize: 0,
    timestamp: 0,
    blockHeight: 0,
    txHash: '',
    status: 'pending',
    confirmations: 0,
    network: ''
  });
  
  // 计算属性
  const filteredRecords = computed(() => {
    return records.value.filter(record => {
      let match = true;
      
      if (filters.documentType && record.documentType !== filters.documentType) {
        match = false;
      }
      
      if (filters.status && record.status !== filters.status) {
        match = false;
      }
      
      if (filters.startDate) {
        const startTimestamp = new Date(filters.startDate).getTime();
        if (record.timestamp < startTimestamp) {
          match = false;
        }
      }
      
      if (filters.endDate) {
        const endTimestamp = new Date(filters.endDate).getTime() + 86400000; // 加一天
        if (record.timestamp > endTimestamp) {
          match = false;
        }
      }
      
      return match;
    });
  });
  
  const totalRecords = computed(() => filteredRecords.value.length);
  const totalPages = computed(() => Math.ceil(totalRecords.value / pageSize.value));
  
  const paginatedRecords = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value;
    const end = start + pageSize.value;
    return filteredRecords.value.slice(start, end);
  });
  
  const displayedPages = computed(() => {
    const pages = [];
    const maxPages = 5;
    
    if (totalPages.value <= maxPages) {
      for (let i = 1; i <= totalPages.value; i++) {
        pages.push(i);
      }
    } else {
      let startPage = Math.max(currentPage.value - Math.floor(maxPages / 2), 1);
      let endPage = startPage + maxPages - 1;
      
      if (endPage > totalPages.value) {
        endPage = totalPages.value;
        startPage = Math.max(endPage - maxPages + 1, 1);
      }
      
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
    }
    
    return pages;
  });
  
  // 方法
  function refreshData() {
    // 在实际应用中，这里会从API获取数据
    records.value = [...mockRecords];
  }
  
  function applyFilters() {
    currentPage.value = 1;
  }
  
  function resetFilters() {
    filters.documentType = '';
    filters.startDate = '';
    filters.endDate = '';
    filters.status = '';
    currentPage.value = 1;
  }
  
  function truncateHash(hash: string): string {
    if (!hash) return '';
    return hash.substring(0, 8) + '...' + hash.substring(hash.length - 8);
  }
  
  function copyToClipboard(text: string) {
    navigator.clipboard.writeText(text).then(() => {
      alert('已复制到剪贴板');
    });
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
  
  function formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
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
  
  function getStatusName(status: string): string {
    const statusMap: Record<string, string> = {
      'confirmed': '已确认',
      'pending': '确认中',
      'failed': '失败'
    };
    
    return statusMap[status] || status;
  }
  
  function getStatusClass(status: string): string {
    const classMap: Record<string, string> = {
      'confirmed': 'status-success',
      'pending': 'status-warning',
      'failed': 'status-danger'
    };
    
    return classMap[status] || '';
  }
  
  function viewDetails(record: BlockchainRecord) {
    selectedRecord.value = { ...record };
    showDetailModal.value = true;
  }
  
  function verifyRecord(record: BlockchainRecord) {
    router.push({
      name: 'BlockchainVerify',
      query: { hash: record.fileHash }
    });
  }
  
  function viewOnExplorer(record: BlockchainRecord) {
    // 在实际应用中，这里会打开区块链浏览器
    window.open(`https://explorer.bsnbase.com/tx/${record.txHash}`, '_blank');
  }
  
  onMounted(() => {
    refreshData();
  });
  </script>
  
  <style scoped>
  .blockchain-record {
    padding: 20px;
  }
  
  .module-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .module-title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
  }
  
  .module-actions {
    display: flex;
    gap: 10px;
  }
  
  .filter-section {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 20px;
  }
  
  .filter-row {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
  }
  
  .filter-item {
    display: flex;
    flex-direction: column;
    gap: 5px;
    min-width: 200px;
  }
  
  .filter-item label {
    font-size: 0.875rem;
    color: #666;
  }
  
  .date-range {
    display: flex;
    align-items: center;
    gap: 5px;
  }
  
  .date-range span {
    color: #666;
  }
  
  .form-control {
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 0.875rem;
  }
  
  .btn {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 5px;
    padding: 8px 16px;
    border-radius: 4px;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .btn-primary {
    background-color: #1e88e5;
    color: white;
    border: none;
  }
  
  .btn-primary:hover {
    background-color: #1976d2;
  }
  
  .btn-outline {
    background-color: transparent;
    color: #1e88e5;
    border: 1px solid #1e88e5;
  }
  
  .btn-outline:hover {
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
  
  .data-table-container {
    overflow-x: auto;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }
  
  .data-table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .data-table th,
  .data-table td {
    padding: 12px 16px;
    text-align: left;
    border-bottom: 1px solid #eee;
  }
  
  .data-table th {
    background-color: #f5f5f5;
    font-weight: 600;
    color: #333;
  }
  
  .data-table tr:last-child td {
    border-bottom: none;
  }
  
  .hash-cell {
    display: flex;
    align-items: center;
    gap: 5px;
  }
  
  .hash-value {
    font-family: monospace;
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
  
  .status-badge {
    display: inline-block;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 0.75rem;
    font-weight: 500;
  }
  
  .status-success {
    background-color: rgba(76, 175, 80, 0.1);
    color: #4caf50;
  }
  
  .status-warning {
    background-color: rgba(255, 152, 0, 0.1);
    color: #ff9800;
  }
  
  .status-danger {
    background-color: rgba(244, 67, 54, 0.1);
    color: #f44336;
  }
  
  .action-buttons {
    display: flex;
    gap: 5px;
  }
  
  .pagination-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
  }
  
  .pagination-info {
    display: flex;
    align-items: center;
    gap: 5px;
    color: #666;
    font-size: 0.875rem;
  }
  
  .page-size-select {
    padding: 4px 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .pagination {
    display: flex;
    align-items: center;
    gap: 5px;
  }
  
  .pagination-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border-radius: 4px;
    background-color: white;
    border: 1px solid #ddd;
    cursor: pointer;
  }
  
  .pagination-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .page-numbers {
    display: flex;
    gap: 5px;
  }
  
  .page-number {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border-radius: 4px;
    background-color: white;
    border: 1px solid #ddd;
    cursor: pointer;
  }
  
  .page-number.active {
    background-color: #1e88e5;
    color: white;
    border-color: #1e88e5;
  }
  
  .empty-table {
    text-align: center;
    color: #999;
    padding: 40px 0;
  }
  
  .modal {
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
  
  .modal-content {
    background-color: white;
    border-radius: 8px;
    width: 90%;
    max-width: 800px;
    max-height: 90vh;
    overflow-y: auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
  
  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    border-bottom: 1px solid #eee;
  }
  
  .modal-header h2 {
    font-size: 1.25rem;
    font-weight: 600;
    color: #333;
    margin: 0;
  }
  
  .close-btn {
    background: none;
    border: none;
    cursor: pointer;
    color: #666;
  }
  
  .modal-body {
    padding: 20px;
  }
  
  .modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding: 16px 20px;
    border-top: 1px solid #eee;
  }
  
  .detail-group {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 15px;
    gap: 20px;
  }
  
  .detail-item {
    flex: 1;
    min-width: 250px;
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
  </style>
  
  