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
        
        <!-- 卡片视图 -->
        <div v-else class="card-view">
          <div 
            v-for="(doc, index) in documents" 
            :key="doc.id || index" 
            class="document-card"
            :class="{ 'selected-card': isSelected(doc) }"
          >
            <div class="card-header">
              <div class="card-title">
                <component :is="documentIcon" class="document-icon" />
                <h3 :title="getDocumentTitle(doc)">{{ getDocumentTitle(doc) }}</h3>
              </div>
              <div class="card-actions">
                <button v-if="showCheckbox" @click="toggleSelect(doc)" class="card-select-btn">
                  <component 
                    :is="isSelected(doc) ? CheckSquareIcon : SquareIcon" 
                    class="select-icon"
                    :class="{ 'selected': isSelected(doc) }"
                  />
                </button>
                <button @click="toggleCardExpand(doc.id)" class="card-expand-btn">
                  <ChevronDownIcon 
                    class="expand-icon" 
                    :class="{ 'expanded': expandedCards.includes(doc.id) }"
                  />
                </button>
              </div>
            </div>
            
            <div class="card-content">
              <div class="card-main-info">
                <div v-if="showUploadTime" class="card-info-item">
                  <span class="info-label">上传时间</span>
                  <span class="info-value">{{ getDocumentUploadTime(doc) }}</span>
                </div>
                <div 
                  v-for="column in cardMainColumns" 
                  :key="column.key" 
                  class="card-info-item"
                >
                  <span class="info-label">{{ column.label }}</span>
                  <span class="info-value" :class="getValueClass(column, getFieldValue(doc, column.key))">
                    {{ getFormattedValue(doc, column) }}
                  </span>
                </div>
              </div>
              
              <div v-if="expandedCards.includes(doc.id)" class="card-details">
                <div v-for="group in detailGroups" :key="group.title" class="card-detail-group">
                  <h4 class="card-group-title">{{ group.title }}</h4>
                  
                  <!-- 自定义组件渲染 -->
                  <component 
                    v-if="group.customComponent && customDetailComponents[group.customComponent]"
                    :is="customDetailComponents[group.customComponent]"
                    :data="getFieldValue(doc, group.dataKey)"
                    :document="doc"
                    :group="group"
                    class="custom-detail-component"
                  />
                  
                  <!-- 标准字段渲染 -->
                  <div v-else-if="group.fields" class="card-detail-fields">
                    <div 
                      v-for="field in group.fields" 
                      :key="field.key" 
                      class="card-detail-field"
                      v-show="!isFieldInCardMain(field.key) && getFieldValue(doc, field.key)"
                    >
                      <span class="detail-label">{{ field.label }}</span>
                      <span class="detail-value">{{ getFormattedValue(doc, field) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="card-footer">
              <button class="card-action-btn view" @click="$emit('view', doc)" title="查看">
                <EyeIcon class="action-icon" /> 查看
              </button>
              <button class="card-action-btn edit" @click="$emit('edit', doc)" title="编辑">
                <EditIcon class="action-icon" /> 编辑
              </button>
              <button class="card-action-btn delete" @click="$emit('delete', doc)" title="删除">
                <TrashIcon class="action-icon" /> 删除
              </button>
            </div>
          </div>
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
    ChevronRightIcon,
    ChevronDownIcon,
    CheckSquareIcon,
    SquareIcon
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
    },
    expandable: {
      type: Boolean,
      default: true
    },
    detailGroups: {
      type: Array,
      default: () => []
    },
    cardMainColumns: {
      type: Array,
      default: () => []
    },
    // 新增：自定义详情组件
    customDetailComponents: {
      type: Object,
      default: () => ({})
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
  const expandedCards = ref([]);
  
  // 处理搜索
  const handleSearch = () => {
    emit('search', searchQuery.value);
  };
  
  // 切换卡片展开状态
  const toggleCardExpand = (docId) => {
    if (expandedCards.value.includes(docId)) {
      expandedCards.value = expandedCards.value.filter(id => id !== docId);
    } else {
      // 如果有详情组，则展开当前卡片，并关闭其他卡片
      if (props.detailGroups && props.detailGroups.length > 0) {
        expandedCards.value = [docId];
      } else {
        expandedCards.value.push(docId);
      }
    }
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
  
  // 检查字段是否在卡片主要信息中
  const isFieldInCardMain = (fieldKey) => {
    return props.cardMainColumns.some(col => col.key === fieldKey);
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
  
  // 监听文档变化，重置展开状态
  watch(() => props.documents, () => {
    expandedCards.value = [];
  }, { deep: true });
  
  // 获取值的样式类
  const getValueClass = (column, value) => {
    if (!column || !value) return '';
    
    // 金额字段使用金额样式
    if (column.key.toLowerCase().includes('amount') || 
        column.key.toLowerCase().includes('price') || 
        column.key.toLowerCase().includes('cost')) {
      return 'amount-value';
    }
    
    // 状态字段使用状态样式
    if (column.key.toLowerCase().includes('status')) {
      if (value.includes('已') || value.includes('成功')) return 'status-success';
      if (value.includes('待') || value.includes('处理中')) return 'status-warning';
      if (value.includes('取消') || value.includes('失败') || value.includes('异常')) return 'status-danger';
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
  
  /* 卡片视图样式 */
  .card-view {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 20px;
  }
  
  .document-card {
    border: 1px solid #eee;
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.2s;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    background-color: #fff;
  }
  
  .document-card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
  
  .selected-card {
    border-color: #1e88e5;
    box-shadow: 0 0 0 2px rgba(30, 136, 229, 0.2);
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    border-bottom: 1px solid #eee;
    background-color: #f9f9f9;
  }
  
  .card-title {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .card-title h3 {
    font-size: 16px;
    font-weight: 600;
    margin: 0;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 200px;
  }
  
  .card-actions {
    display: flex;
    gap: 8px;
  }
  
  .card-select-btn,
  .card-expand-btn {
    background: none;
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    border-radius: 4px;
  }
  
  .card-select-btn:hover,
  .card-expand-btn:hover {
    background-color: #f0f0f0;
  }
  
  .select-icon {
    width: 18px;
    height: 18px;
    color: #999;
  }
  
  .select-icon.selected {
    color: #1e88e5;
  }
  
  .expand-icon {
    width: 16px;
    height: 16px;
    transition: transform 0.2s;
  }
  
  .expand-icon.expanded {
    transform: rotate(180deg);
  }
  
  .card-content {
    padding: 20px;
    transition: all 0.3s ease-in-out;
  }
  
  .card-main-info {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .card-info-item {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }
  
  .info-label {
    font-size: 12px;
    color: #666;
    font-weight: 500;
  }
  
  .info-value {
    font-size: 14px;
    color: #333;
    font-weight: 500;
  }
  
  .card-details {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #eee;
  }
  
  .card-detail-group {
    margin-bottom: 20px;
  }
  
  .card-group-title {
    font-size: 14px;
    font-weight: 600;
    color: #333;
    margin: 0 0 12px 0;
    padding-bottom: 8px;
    border-bottom: 1px dashed #eee;
  }
  
  .card-detail-fields {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .card-detail-field {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }
  
  .detail-label {
    font-size: 12px;
    color: #666;
  }
  
  .detail-value {
    font-size: 14px;
    color: #333;
  }
  
  /* 自定义组件容器 */
  .custom-detail-component {
    width: 100%;
    margin-bottom: 16px;
  }
  
  .card-footer {
    display: flex;
    justify-content: space-between;
    padding: 12px 20px;
    border-top: 1px solid #eee;
    background-color: #f9f9f9;
  }
  
  .card-action-btn {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 12px;
    border: none;
    border-radius: 4px;
    font-size: 13px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
    background-color: #f0f0f0;
  }
  
  .card-action-btn.view {
    color: #1e88e5;
  }
  
  .card-action-btn.edit {
    color: #ffa000;
  }
  
  .card-action-btn.delete {
    color: #e53935;
  }
  
  .card-action-btn:hover {
    background-color: #e0e0e0;
  }
  
  /* 分页样式 */
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
    
    .card-view {
      grid-template-columns: 1fr;
    }
    
    .card-main-info,
    .card-detail-fields {
      grid-template-columns: 1fr;
    }
  }
  </style>
  
  