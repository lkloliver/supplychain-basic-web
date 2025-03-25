<template>
    <div class="outbound-quality-manage">
      <h2>出库质检单管理</h2>
      
      <div class="search-section">
        <div class="search-form">
          <div class="form-group">
            <label>质检单号</label>
            <input type="text" v-model="searchParams.qualityId" placeholder="请输入质检单号" />
          </div>
          <div class="form-group">
            <label>关联合同号</label>
            <input type="text" v-model="searchParams.contractId" placeholder="请输入合同号" />
          </div>
          <div class="form-group">
            <label>质检日期</label>
            <div class="date-range">
              <input type="date" v-model="searchParams.startDate" />
              <span>至</span>
              <input type="date" v-model="searchParams.endDate" />
            </div>
          </div>
          <div class="form-group">
            <label>质检状态</label>
            <select v-model="searchParams.status">
              <option value="">全部</option>
              <option value="passed">合格</option>
              <option value="failed">不合格</option>
              <option value="pending">待复检</option>
            </select>
          </div>
          <div class="form-actions">
            <button class="btn-search" @click="searchRecords">查询</button>
            <button class="btn-reset" @click="resetSearch">重置</button>
          </div>
        </div>
      </div>
      
      <div class="table-actions">
        <button class="btn-export" @click="exportData">导出数据</button>
        <button class="btn-delete" @click="batchDelete" :disabled="!selectedRows.length">批量删除</button>
      </div>
      
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll" /></th>
              <th>质检单号</th>
              <th>关联合同号</th>
              <th>货物名称</th>
              <th>质检日期</th>
              <th>质检人员</th>
              <th>质检结果</th>
              <th>质检状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in qualityRecords" :key="item.id">
              <td><input type="checkbox" v-model="selectedRows" :value="item.id" /></td>
              <td>{{ item.qualityId }}</td>
              <td>{{ item.contractId }}</td>
              <td>{{ item.goodsName }}</td>
              <td>{{ item.inspectionDate }}</td>
              <td>{{ item.inspector }}</td>
              <td>{{ item.qualityResult }}</td>
              <td>
                <span :class="'status-' + item.status">{{ getStatusText(item.status) }}</span>
              </td>
              <td>
                <div class="action-buttons">
                  <button class="btn-view" @click="viewRecord(item)">查看</button>
                  <button class="btn-edit" @click="editRecord(item)">编辑</button>
                  <button class="btn-delete" @click="deleteRecord(item)">删除</button>
                </div>
              </td>
            </tr>
            <tr v-if="qualityRecords.length === 0">
              <td colspan="9" class="no-data">暂无数据</td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <div class="pagination">
        <button :disabled="currentPage === 1" @click="changePage(currentPage - 1)">上一页</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">下一页</button>
      </div>
      
      <!-- 查看/编辑弹窗 -->
      <div class="modal" v-if="showModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>{{ modalType === 'view' ? '查看质检单' : '编辑质检单' }}</h3>
            <button class="btn-close" @click="closeModal">×</button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>质检单号</label>
              <input type="text" v-model="currentRecord.qualityId" :disabled="modalType === 'view'" />
            </div>
            <div class="form-group">
              <label>关联合同号</label>
              <input type="text" v-model="currentRecord.contractId" :disabled="modalType === 'view'" />
            </div>
            <div class="form-group">
              <label>货物名称</label>
              <input type="text" v-model="currentRecord.goodsName" :disabled="modalType === 'view'" />
            </div>
            <div class="form-group">
              <label>质检日期</label>
              <input type="date" v-model="currentRecord.inspectionDate" :disabled="modalType === 'view'" />
            </div>
            <div class="form-group">
              <label>质检人员</label>
              <input type="text" v-model="currentRecord.inspector" :disabled="modalType === 'view'" />
            </div>
            <div class="form-group">
              <label>质检结果</label>
              <textarea v-model="currentRecord.qualityResult" :disabled="modalType === 'view'"></textarea>
            </div>
            <div class="form-group">
              <label>质检状态</label>
              <select v-model="currentRecord.status" :disabled="modalType === 'view'">
                <option value="passed">合格</option>
                <option value="failed">不合格</option>
                <option value="pending">待复检</option>
              </select>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn-cancel" @click="closeModal">取消</button>
            <button v-if="modalType === 'edit'" class="btn-save" @click="saveRecord">保存</button>
          </div>
        </div>
      </div>
      
      <!-- 确认删除弹窗 -->
      <div class="confirm-modal" v-if="showConfirmModal">
        <div class="confirm-content">
          <p>确定要删除选中的记录吗？</p>
          <div class="confirm-buttons">
            <button class="btn-cancel" @click="cancelDelete">取消</button>
            <button class="btn-confirm" @click="confirmDelete">确定</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, computed, onMounted } from 'vue';
  
  // 搜索参数
  const searchParams = reactive({
    qualityId: '',
    contractId: '',
    startDate: '',
    endDate: '',
    status: ''
  });
  
  // 表格数据
  const qualityRecords = ref([]);
  const selectedRows = ref([]);
  const selectAll = ref(false);
  
  // 分页
  const currentPage = ref(1);
  const pageSize = ref(10);
  const totalRecords = ref(0);
  const totalPages = computed(() => Math.ceil(totalRecords.value / pageSize.value));
  
  // 模态框
  const showModal = ref(false);
  const modalType = ref('view'); // 'view' 或 'edit'
  const currentRecord = ref({});
  
  // 确认删除弹窗
  const showConfirmModal = ref(false);
  const recordToDelete = ref(null);
  
  // 模拟数据加载
  onMounted(() => {
    loadData();
  });
  
  // 加载数据
  const loadData = () => {
    // 模拟API调用
    setTimeout(() => {
      qualityRecords.value = [
        {
          id: 1,
          qualityId: 'QL-OUT-20230501001',
          contractId: 'SC-20230401001',
          goodsName: '小麦',
          inspectionDate: '2023-05-01',
          inspector: '张三',
          qualityResult: '水分14.2%，杂质0.8%，容重780g/L',
          status: 'passed'
        },
        {
          id: 2,
          qualityId: 'QL-OUT-20230502001',
          contractId: 'SC-20230402001',
          goodsName: '玉米',
          inspectionDate: '2023-05-02',
          inspector: '李四',
          qualityResult: '水分15.1%，杂质1.2%，容重720g/L',
          status: 'failed'
        },
        {
          id: 3,
          qualityId: 'QL-OUT-20230503001',
          contractId: 'SC-20230403001',
          goodsName: '大豆',
          inspectionDate: '2023-05-03',
          inspector: '王五',
          qualityResult: '水分13.5%，杂质0.5%，容重750g/L',
          status: 'pending'
        }
      ];
      totalRecords.value = 3;
    }, 500);
  };
  
  // 搜索记录
  const searchRecords = () => {
    currentPage.value = 1;
    loadData();
  };
  
  // 重置搜索
  const resetSearch = () => {
    Object.keys(searchParams).forEach(key => {
      searchParams[key] = '';
    });
    searchRecords();
  };
  
  // 切换全选
  const toggleSelectAll = () => {
    if (selectAll.value) {
      selectedRows.value = qualityRecords.value.map(item => item.id);
    } else {
      selectedRows.value = [];
    }
  };
  
  // 导出数据
  const exportData = () => {
    alert('导出数据功能待实现');
  };
  
  // 批量删除
  const batchDelete = () => {
    if (selectedRows.value.length === 0) return;
    showConfirmModal.value = true;
  };
  
  // 查看记录
  const viewRecord = (item) => {
    currentRecord.value = { ...item };
    modalType.value = 'view';
    showModal.value = true;
  };
  
  // 编辑记录
  const editRecord = (item) => {
    currentRecord.value = { ...item };
    modalType.value = 'edit';
    showModal.value = true;
  };
  
  // 删除单条记录
  const deleteRecord = (item) => {
    recordToDelete.value = item.id;
    showConfirmModal.value = true;
  };
  
  // 保存记录
  const saveRecord = () => {
    // 模拟API调用
    setTimeout(() => {
      const index = qualityRecords.value.findIndex(item => item.id === currentRecord.value.id);
      if (index !== -1) {
        qualityRecords.value[index] = { ...currentRecord.value };
      }
      closeModal();
      alert('保存成功');
    }, 500);
  };
  
  // 关闭模态框
  const closeModal = () => {
    showModal.value = false;
    currentRecord.value = {};
  };
  
  // 取消删除
  const cancelDelete = () => {
    showConfirmModal.value = false;
    recordToDelete.value = null;
  };
  
  // 确认删除
  const confirmDelete = () => {
    // 模拟API调用
    setTimeout(() => {
      if (recordToDelete.value) {
        // 单条删除
        qualityRecords.value = qualityRecords.value.filter(item => item.id !== recordToDelete.value);
      } else {
        // 批量删除
        qualityRecords.value = qualityRecords.value.filter(item => !selectedRows.value.includes(item.id));
      }
      selectedRows.value = [];
      selectAll.value = false;
      showConfirmModal.value = false;
      recordToDelete.value = null;
      alert('删除成功');
    }, 500);
  };
  
  // 切换页码
  const changePage = (page) => {
    currentPage.value = page;
    loadData();
  };
  
  // 获取状态文本
  const getStatusText = (status) => {
    const statusMap = {
      'passed': '合格',
      'failed': '不合格',
      'pending': '待复检'
    };
    return statusMap[status] || status;
  };
  </script>
  
  <style scoped>
  .outbound-quality-manage {
    padding: 20px;
  }
  
  h2 {
    margin-bottom: 20px;
    font-size: 1.5rem;
    color: #333;
  }
  
  .search-section {
    background-color: #f5f5f5;
    padding: 15px;
    border-radius: 5px;
    margin-bottom: 20px;
  }
  
  .search-form {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 15px;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
  }
  
  .form-group label {
    margin-bottom: 5px;
    font-weight: 500;
  }
  
  .form-group input,
  .form-group select {
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .date-range {
    display: flex;
    align-items: center;
  }
  
  .date-range span {
    margin: 0 5px;
  }
  
  .form-actions {
    display: flex;
    gap: 10px;
    align-items: flex-end;
  }
  
  .btn-search,
  .btn-reset {
    padding: 8px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .btn-search {
    background-color: #1890ff;
    color: white;
  }
  
  .btn-reset {
    background-color: #f0f0f0;
    color: #333;
  }
  
  .table-actions {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15px;
  }
  
  .btn-export,
  .btn-delete {
    padding: 8px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .btn-export {
    background-color: #52c41a;
    color: white;
  }
  
  .btn-delete {
    background-color: #ff4d4f;
    color: white;
  }
  
  .btn-delete:disabled {
    background-color: #ffccc7;
    cursor: not-allowed;
  }
  
  .table-container {
    overflow-x: auto;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  th {
    background-color: #f5f5f5;
    font-weight: 600;
  }
  
  .status-passed {
    color: #52c41a;
  }
  
  .status-failed {
    color: #ff4d4f;
  }
  
  .status-pending {
    color: #faad14;
  }
  
  .action-buttons {
    display: flex;
    gap: 5px;
  }
  
  .btn-view,
  .btn-edit,
  .btn-delete {
    padding: 5px 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.8rem;
  }
  
  .btn-view {
    background-color: #1890ff;
    color: white;
  }
  
  .btn-edit {
    background-color: #faad14;
    color: white;
  }
  
  .btn-delete {
    background-color: #ff4d4f;
    color: white;
  }
  
  .no-data {
    text-align: center;
    padding: 20px;
    color: #999;
  }
  
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
    gap: 10px;
  }
  
  .pagination button {
    padding: 5px 10px;
    border: 1px solid #d9d9d9;
    background-color: white;
    cursor: pointer;
    border-radius: 4px;
  }
  
  .pagination button:disabled {
    color: #d9d9d9;
    cursor: not-allowed;
  }
  
  /* 模态框样式 */
  .modal,
  .confirm-modal {
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
  
  .modal-content {
    background-color: white;
    border-radius: 5px;
    width: 500px;
    max-width: 90%;
  }
  
  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid #ddd;
  }
  
  .modal-header h3 {
    margin: 0;
  }
  
  .btn-close {
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
  }
  
  .modal-body {
    padding: 15px;
    max-height: 60vh;
    overflow-y: auto;
  }
  
  .modal-footer {
    padding: 15px;
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    border-top: 1px solid #ddd;
  }
  
  .btn-cancel,
  .btn-save,
  .btn-confirm {
    padding: 8px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .btn-cancel {
    background-color: #f0f0f0;
    color: #333;
  }
  
  .btn-save {
    background-color: #1890ff;
    color: white;
  }
  
  .btn-confirm {
    background-color: #ff4d4f;
    color: white;
  }
  
  .confirm-content {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    text-align: center;
  }
  
  .confirm-buttons {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-top: 15px;
  }
  
  textarea {
    resize: vertical;
    min-height: 80px;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  </style>
  
  