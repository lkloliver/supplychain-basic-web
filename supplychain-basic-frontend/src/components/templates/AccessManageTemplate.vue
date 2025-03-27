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
      
    </div>
  
    <div class="operation-content">
      <div class="manage-toolbar">
        <div class="search-box">
          <input 
            type="text" 
            v-model="searchQuery" 
            class="search-input" 
            :placeholder="searchPlaceholder" 
            @input="handleSearch"
          />
          <SearchIcon class="search-icon" />
        </div>
        
        <div class="filter-actions">
          <button v-if="showFilterActions" class="action-btn filter-btn" @click="$emit('filter')">
            <FilterIcon class="btn-icon" />
            筛选
          </button>
          
          <button v-if="showFilterActions" class="action-btn sort-btn" @click="$emit('sort')">
            <SortAscIcon class="btn-icon" />
            排序
          </button>
          
          <button v-if="showFilterActions" class="action-btn export-btn" @click="$emit('export')">
            <DownloadIcon class="btn-icon" />
            导出
          </button>
        </div>
      </div>
      
      <div class="document-container">
        <div v-if="documents.length === 0" class="empty-placeholder">
          <InboxIcon class="empty-icon" />
          <p class="empty-text">{{ emptyText }}</p>
        </div>
        
        <!-- 列表视图 -->
        <div v-else class="list-view">
          <table class="list-table">
            <thead>
              <tr>
                <th v-if="showCheckbox" class="checkbox-column">
                  <input 
                    type="checkbox" 
                    :checked="allSelected" 
                    @change="toggleSelectAll" 
                    class="checkbox-input"
                  />
                </th>
                <th class="title-column">{{ titleColumnName }}</th>
                <th v-for="column in columns" :key="column.key" class="data-column">
                  {{ column.label }}
                </th>
                <th v-if="showUploadTime" class="date-column">上传时间</th>
                <th class="actions-column">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr 
                v-for="(doc, index) in documents" 
                :key="doc.id || index" 
                :class="{ 'selected-row': isSelected(doc) }"
              >
                <td v-if="showCheckbox" class="checkbox-column">
                  <input 
                    type="checkbox" 
                    :checked="isSelected(doc)" 
                    @change="toggleSelect(doc)" 
                    class="checkbox-input"
                  />
                </td>
                <td class="title-column">
                  <div class="document-title">
                    <component :is="documentIcon" class="document-icon" />
                    <span :title="getDocumentTitle(doc)">{{ getDocumentTitle(doc) }}</span>
                  </div>
                </td>
                <td v-for="column in columns" :key="column.key" class="data-column">
                  <span :class="getValueClass(column, getFieldValue(doc, column.key))">
                    {{ getFormattedValue(doc, column) }}
                  </span>
                </td>
                <td v-if="showUploadTime" class="date-column">
                  {{ getDocumentUploadTime(doc) }}
                </td>
                <td class="actions-column">
                  <div class="row-actions">
                    <button class="row-action-btn view" @click="$emit('view', doc)" title="查看">
                      <EyeIcon class="action-icon" />
                    </button>
                    <button class="row-action-btn edit" @click="$emit('edit', doc)" title="编辑">
                      <EditIcon class="action-icon" />
                    </button>
                    <button class="row-action-btn delete" @click="$emit('delete', doc)" title="删除">
                      <TrashIcon class="action-icon" />
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <div class="pagination" v-if="documents.length > 0 && showPagination">
          <button 
            class="pagination-btn" 
            :disabled="currentPage === 1"
            @click="$emit('page-change', currentPage - 1)"
          >
            <ChevronLeftIcon class="pagination-icon" />
          </button>
          <span class="pagination-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
          <button 
            class="pagination-btn" 
            :disabled="currentPage === totalPages || totalPages === 0"
            @click="$emit('page-change', currentPage + 1)"
          >
            <ChevronRightIcon class="pagination-icon" />
          </button>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, watch } from 'vue';
  import { 
    ArrowLeftIcon, 
    SearchIcon, 
    FilterIcon,
    SortAscIcon,
    DownloadIcon,
    FileTextIcon, 
    InboxIcon,
    EyeIcon, 
    EditIcon, 
    TrashIcon,
    ChevronLeftIcon,
    ChevronRightIcon
  } from 'lucide-vue-next';
  import ModuleViewToggle from '@/components/ModuleViewToggle.vue';
  
  const props = defineProps({
    title: {
      type: String,
      required: true
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
    documents: {
      type: Array,
      required: true
    },
    titleField: {
      type: String,
      default: 'name'
    },
    titleColumnName: {
      type: String,
      default: '资料名称'
    },
    uploadTimeField: {
      type: String,
      default: 'uploadTime'
    },
    showUploadTime: {
      type: Boolean,
      default: true
    },
    columns: {
      type: Array,
      default: () => []
    },
    documentIcon: {
      type: Object,
      default: () => FileTextIcon
    },
    searchPlaceholder: {
      type: String,
      default: '搜索资料...'
    },
    emptyText: {
      type: String,
      default: '暂无资料，请先上传'
    },
    showFilterActions: {
      type: Boolean,
      default: true
    },
    showCheckbox: {
      type: Boolean,
      default: false
    },
    showPagination: {
      type: Boolean,
      default: true
    },
    currentPage: {
      type: Number,
      default: 1
    },
    totalPages: {
      type: Number,
      default: 1
    }
  });
  
  const emit = defineEmits([
    'search', 'filter', 'sort', 'export', 
    'view', 'edit', 'delete', 
    'select', 'select-all',
    'page-change'
  ]);
  
  const searchQuery = ref('');
  const selectedDocs = ref([]);
  
  // 处理搜索
  const handleSearch = () => {
    emit('search', searchQuery.value);
  };
  
  // 获取文档标题
  const getDocumentTitle = (doc) => {
    return doc[props.titleField] || '未命名文档';
  };
  
  // 获取上传时间
  const getDocumentUploadTime = (doc) => {
    return doc[props.uploadTimeField] || '未知时间';
  };
  
  // 获取字段值
  const getFieldValue = (doc, fieldKey) => {
    if (!fieldKey || !doc) return '';
  
    if (!fieldKey.includes('.')) {
      return doc[fieldKey] !== undefined ? doc[fieldKey] : '';
    }
  
    // 处理嵌套字段，如 'details.contractNo'
    const keys = fieldKey.split('.');
    let value = doc;
  
    for (const key of keys) {
      if (value && typeof value === 'object') {
        value = value[key];
      } else {
        return '';
      }
    }
  
    return value !== undefined ? value : '';
  };
  
  // 获取格式化后的值
  const getFormattedValue = (doc, field) => {
    const value = getFieldValue(doc, field.key);
  
    // 如果字段有格式化函数，使用它
    if (field.formatter && typeof field.formatter === 'function') {
      return field.formatter(value, doc);
    }
  
    return value;
  };
  
  // 选择相关
  const isSelected = (doc) => {
    return selectedDocs.value.some(selected => selected.id === doc.id);
  };
  
  const toggleSelect = (doc) => {
    if (isSelected(doc)) {
      selectedDocs.value = selectedDocs.value.filter(selected => selected.id !== doc.id);
    } else {
      selectedDocs.value.push(doc);
    }
    emit('select', selectedDocs.value);
  };
  
  const allSelected = computed(() => {
    return props.documents.length > 0 && selectedDocs.value.length === props.documents.length;
  });
  
  const toggleSelectAll = () => {
    if (allSelected.value) {
      selectedDocs.value = [];
    } else {
      selectedDocs.value = [...props.documents];
    }
    emit('select-all', selectedDocs.value);
  };
  
  // 获取值的样式类
  const getValueClass = (column, value) => {
    if (!column || value === undefined || value === null) return '';
  
    // 金额字段使用金额样式
    if (column.key.toLowerCase().includes('amount') || 
        column.key.toLowerCase().includes('price') || 
        column.key.toLowerCase().includes('cost')) {
      return 'amount-value';
    }
  
    // 状态字段使用状态样式
    if (column.key.toLowerCase().includes('status')) {
      // Convert value to string to safely use includes
      const strValue = String(value);
      
      if (strValue.includes('已') || strValue.includes('成功') || value === 'active' || value === 1) return 'status-success';
      if (strValue.includes('待') || strValue.includes('处理中')) return 'status-warning';
      if (strValue.includes('取消') || strValue.includes('失败') || strValue.includes('异常') || value === 'inactive' || value === 0) return 'status-danger';
      return 'status-info';
    }
  
    return '';
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
    font-size: 24px; /* Increased from 20px */
    font-weight: 600;
    color: #333;
    margin: 0;
  }
  
  .operation-content {
    margin-top: 24px;
  }
  
  .manage-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .search-box {
    position: relative;
    width: 240px; /* Reduced from 300px */
  }
  
  .search-input {
    width: 100%;
    padding: 8px 12px 8px 36px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
  }
  
  .search-icon {
    position: absolute;
    left: 12px;
    top: 50%;
    transform: translateY(-50%);
    width: 16px;
    height: 16px;
    color: #999;
  }
  
  .filter-actions {
    display: flex;
    gap: 8px;
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
    background-color: #f5f5f5;
    color: #333;
  }
  
  .action-btn:hover {
    background-color: #e0e0e0;
  }
  
  .btn-icon {
    width: 16px;
    height: 16px;
  }
  
  .document-container {
    position: relative;
    min-height: 300px;
  }
  
  .empty-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 0;
    color: #999;
  }
  
  .empty-icon {
    width: 48px;
    height: 48px;
    margin-bottom: 16px;
    color: #ccc;
  }
  
  .empty-text {
    font-size: 16px;
  }
  
  /* 列表视图样式 */
  .list-view {
    width: 100%;
    overflow-x: auto;
  }
  
  .list-table {
    width: 100%;
    border-collapse: separate;
    border-spacing: 0;
    font-size: 14px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    border-radius: 8px;
    overflow: hidden;
  }
  
  .list-table th {
    background-color: #f7f7f7;
    padding: 14px 16px;
    text-align: left;
    font-weight: 600;
    color: #333;
    border-bottom: 1px solid #e0e0e0;
  }
  
  .list-table td {
    padding: 14px 16px;
    border-bottom: 1px solid #eee;
    vertical-align: middle;
  }
  
  .list-table tr:last-child td {
    border-bottom: none;
  }
  
  .list-table tr:hover {
    background-color: #f5f9ff;
  }
  
  .selected-row {
    background-color: #e6f7ff;
  }
  
  .selected-row:hover {
    background-color: #d6f0ff;
  }
  
  .checkbox-column {
    width: 40px;
    text-align: center;
  }
  
  .checkbox-input {
    cursor: pointer;
  }
  
  .title-column {
    min-width: 200px;
  }
  
  .document-title {
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .document-icon {
    width: 16px;
    height: 16px;
    color: #666;
  }
  
  .data-column {
    min-width: 120px;
  }
  
  .date-column {
    min-width: 150px;
    white-space: nowrap;
  }
  
  .actions-column {
    width: 120px;
    text-align: center;
  }
  
  .row-actions {
    display: flex;
    justify-content: center;
    gap: 8px;
  }
  
  .row-action-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    border: none;
    border-radius: 4px;
    background-color: transparent;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .row-action-btn:hover {
    background-color: #f0f0f0;
  }
  
  .row-action-btn.view {
    color: #1e88e5;
  }
  
  .row-action-btn.edit {
    color: #ffa000;
  }
  
  .row-action-btn.delete {
    color: #e53935;
  }
  
  .action-icon {
    width: 16px;
    height: 16px;
  }
  
  /* Pagination styles */
  .pagination {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 24px;
    gap: 12px;
  }
  
  .pagination-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    border: 1px solid #ddd;
    border-radius: 4px;
    background-color: white;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .pagination-btn:hover:not(:disabled) {
    border-color: #1e88e5;
    color: #1e88e5;
    background-color: #f0f7ff;
  }
  
  .pagination-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .pagination-icon {
    width: 18px;
    height: 18px;
  }
  
  .pagination-info {
    font-size: 14px;
    color: #555;
    font-weight: 500;
  }
  
  .amount-value {
    font-family: monospace;
    font-weight: 600;
  }
  
  .status-success {
    color: #4caf50;
  }
  
  .status-warning {
    color: #ff9800;
  }
  
  .status-danger {
    color: #f44336;
  }
  
  .status-info {
    color: #2196f3;
  }
  
  @media (max-width: 768px) {
    .manage-toolbar {
      flex-direction: column;
      align-items: flex-start;
      gap: 12px;
    }
  
    .search-box {
      width: 100%;
    }
  
    .filter-actions {
      width: 100%;
      justify-content: space-between;
    }
    
    .list-table {
      display: block;
      overflow-x: auto;
    }
  }
  </style>
  
  