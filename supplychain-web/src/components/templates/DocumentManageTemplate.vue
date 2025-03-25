<template>
    <div class="operation-wrapper">
      <div class="operation-header">
        <div class="header-left">
          <router-link :to="{ name: backRouteName }" class="back-button">
            <ArrowLeftIcon class="back-icon" />
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
          
          <div class="filter-actions" v-if="showFilterActions">
            <button class="action-btn filter-btn" @click="$emit('filter')">
              <FilterIcon class="btn-icon" />
              筛选
            </button>
            <button class="action-btn sort-btn" @click="$emit('sort')">
              <SortAscIcon class="btn-icon" />
              排序
            </button>
            <button class="action-btn export-btn" @click="$emit('export')">
              <DownloadIcon class="btn-icon" />
              导出
            </button>
          </div>
        </div>
        
        <div class="document-table-container">
          <div v-if="documents.length === 0" class="empty-placeholder">
            <InboxIcon class="empty-icon" />
            <p class="empty-text">{{ emptyText }}</p>
          </div>
          
          <div v-else class="table-responsive">
            <table class="document-table">
              <thead>
                <tr>
                  <th v-if="showCheckbox" class="checkbox-column">
                    <input 
                      type="checkbox" 
                      :checked="allSelected" 
                      @change="toggleSelectAll" 
                      class="checkbox"
                    />
                  </th>
                  <th>{{ titleColumnName || '资料名称' }}</th>
                  <th v-if="showUploadTime">上传时间</th>
                  <th v-for="column in columns" :key="column.key">
                    {{ column.label }}
                  </th>
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
                      class="checkbox"
                    />
                  </td>
                  <td class="title-column">
                    <div class="document-title-cell">
                      <component :is="documentIcon" class="document-icon" />
                      <span>{{ getDocumentTitle(doc) }}</span>
                    </div>
                  </td>
                  <td v-if="showUploadTime">{{ getDocumentUploadTime(doc) }}</td>
                  <td v-for="column in columns" :key="`${doc.id}-${column.key}`">
                    {{ getFieldValue(doc, column.key) }}
                  </td>
                  <td class="actions-column">
                    <div class="document-actions">
                      <button 
                        class="document-action-btn view" 
                        @click="$emit('view', doc)" 
                        title="查看"
                      >
                        <EyeIcon class="action-icon" />
                      </button>
                      <button 
                        class="document-action-btn edit" 
                        @click="$emit('edit', doc)" 
                        title="编辑"
                      >
                        <EditIcon class="action-icon" />
                      </button>
                      <button 
                        class="document-action-btn delete" 
                        @click="$emit('delete', doc)" 
                        title="删除"
                      >
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
              :disabled="currentPage === totalPages"
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
  import { ref, computed } from 'vue';
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
    if (!fieldKey.includes('.')) {
      return doc[fieldKey];
    }
    
    // 处理嵌套字段，如 'details.contractNo'
    const keys = fieldKey.split('.');
    let value = doc;
    
    for (const key of keys) {
      if (value && typeof value === 'object') {
        value = value[key];
      } else {
        return undefined;
      }
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
  
  .manage-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .search-box {
    position: relative;
    width: 300px;
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
  
  .document-table-container {
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
  
  .table-responsive {
    overflow-x: auto;
  }
  
  .document-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 14px;
  }
  
  .document-table th,
  .document-table td {
    padding: 12px 16px;
    text-align: left;
    border-bottom: 1px solid #eee;
  }
  
  .document-table th {
    font-weight: 600;
    color: #555;
    background-color: #f9f9f9;
  }
  
  .document-table tbody tr:hover {
    background-color: #f5f5f5;
  }
  
  .selected-row {
    background-color: #e3f2fd;
  }
  
  .selected-row:hover {
    background-color: #bbdefb !important;
  }
  
  .checkbox-column {
    width: 40px;
    text-align: center;
  }
  
  .checkbox {
    cursor: pointer;
  }
  
  .title-column {
    min-width: 200px;
  }
  
  .document-title-cell {
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .document-icon {
    width: 16px;
    height: 16px;
    color: #1e88e5;
  }
  
  .actions-column {
    width: 120px;
    text-align: center;
  }
  
  .document-actions {
    display: flex;
    justify-content: center;
    gap: 8px;
  }
  
  .document-action-btn {
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
  
  .document-action-btn:hover {
    background-color: #f0f0f0;
  }
  
  .action-icon {
    width: 16px;
    height: 16px;
  }
  
  .view .action-icon {
    color: #1e88e5;
  }
  
  .edit .action-icon {
    color: #ffa000;
  }
  
  .delete .action-icon {
    color: #e53935;
  }
  
  .pagination {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 20px;
    gap: 12px;
  }
  
  .pagination-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border: 1px solid #ddd;
    border-radius: 4px;
    background-color: white;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .pagination-btn:hover:not(:disabled) {
    border-color: #1e88e5;
    color: #1e88e5;
  }
  
  .pagination-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .pagination-icon {
    width: 16px;
    height: 16px;
  }
  
  .pagination-info {
    font-size: 14px;
    color: #666;
  }
  </style>
  
  