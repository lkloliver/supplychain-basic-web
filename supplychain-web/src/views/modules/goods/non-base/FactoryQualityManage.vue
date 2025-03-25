<template>
    <div class="factory-quality-manage-container">
      <div class="page-header">
        <h1>管理到货厂家质检单</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/goods/non-base/factory-quality" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> 返回
          </router-link>
          <button class="btn btn-primary" @click="exportData">
            <i class="fas fa-download"></i> 导出数据
          </button>
        </div>
      </div>
  
      <div class="filter-section">
        <div class="filter-form">
          <div class="form-row">
            <div class="form-group">
              <label for="dateRange">日期范围</label>
              <div class="date-range-picker">
                <input type="date" v-model="filters.startDate">
                <span>至</span>
                <input type="date" v-model="filters.endDate">
              </div>
            </div>
            <div class="form-group">
              <label for="supplier">供应商</label>
              <select id="supplier" v-model="filters.supplier">
                <option value="">全部供应商</option>
                <option value="供应商A">供应商A</option>
                <option value="供应商B">供应商B</option>
                <option value="供应商C">供应商C</option>
                <option value="供应商D">供应商D</option>
              </select>
            </div>
            <div class="form-group">
              <label for="result">检验结果</label>
              <select id="result" v-model="filters.result">
                <option value="">全部结果</option>
                <option value="合格">合格</option>
                <option value="不合格">不合格</option>
                <option value="部分合格">部分合格</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="inspectionId">质检单号</label>
              <input type="text" id="inspectionId" v-model="filters.inspectionId" placeholder="输入质检单号">
            </div>
            <div class="form-group">
              <label for="goodsName">货物名称</label>
              <input type="text" id="goodsName" v-model="filters.goodsName" placeholder="输入货物名称">
            </div>
            <div class="form-group filter-actions">
              <button class="btn btn-primary" @click="applyFilters">
                <i class="fas fa-search"></i> 查询
              </button>
              <button class="btn btn-secondary" @click="resetFilters">
                <i class="fas fa-redo"></i> 重置
              </button>
            </div>
          </div>
        </div>
      </div>
  
      <div class="data-table-section">
        <div class="table-header">
          <div class="table-title">质检单列表</div>
          <div class="table-actions">
            <div class="batch-actions" v-if="selectedItems.length > 0">
              <span class="selected-count">已选择 {{ selectedItems.length }} 项</span>
              <button class="btn btn-danger btn-sm" @click="batchDelete">
                <i class="fas fa-trash"></i> 批量删除
              </button>
            </div>
            <div class="pagination-info">
              共 {{ totalItems }} 条记录，当前显示 {{ startItem }}-{{ endItem }}
            </div>
          </div>
        </div>
  
        <table class="data-table">
          <thead>
            <tr>
              <th class="checkbox-cell">
                <input type="checkbox" @change="toggleSelectAll" :checked="allSelected">
              </th>
              <th>质检单号</th>
              <th>供应商</th>
              <th>货物名称</th>
              <th>批次号</th>
              <th>检验日期</th>
              <th>检验员</th>
              <th>检验结果</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in displayedItems" :key="item.id">
              <td class="checkbox-cell">
                <input type="checkbox" v-model="selectedItems" :value="item.id">
              </td>
              <td>{{ item.inspectionId }}</td>
              <td>{{ item.supplier }}</td>
              <td>{{ item.goodsName }}</td>
              <td>{{ item.batchNumber }}</td>
              <td>{{ item.inspectionDate }}</td>
              <td>{{ item.inspector }}</td>
              <td :class="item.result === '合格' ? 'text-success' : (item.result === '不合格' ? 'text-danger' : 'text-warning')">
                {{ item.result }}
              </td>
              <td class="actions-cell">
                <button class="btn btn-info btn-sm" @click="viewDetails(item)">
                  <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-warning btn-sm" @click="editItem(item)">
                  <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-danger btn-sm" @click="deleteItem(item)">
                  <i class="fas fa-trash"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
  
        <div v-if="displayedItems.length === 0" class="no-data">
          <i class="fas fa-search fa-3x"></i>
          <p>没有找到匹配的质检单数据</p>
        </div>
  
        <div class="pagination">
          <button 
            class="pagination-btn" 
            :disabled="currentPage === 1" 
            @click="changePage(currentPage - 1)"
          >
            <i class="fas fa-chevron-left"></i>
          </button>
          <div class="page-numbers">
            <button 
              v-for="page in displayedPages" 
              :key="page" 
              :class="['page-number', currentPage === page ? 'active' : '']"
              @click="changePage(page)"
            >
              {{ page }}
            </button>
          </div>
          <button 
            class="pagination-btn" 
            :disabled="currentPage === totalPages" 
            @click="changePage(currentPage + 1)"
          >
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
  
      <!-- 详情弹窗 -->
      <div v-if="showDetailsModal" class="modal-overlay" @click="closeDetailsModal">
        <div class="modal-container" @click.stop>
          <div class="modal-header">
            <h2>质检单详情</h2>
            <button class="close-btn" @click="closeDetailsModal">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="details-section">
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">质检单号</div>
                  <div class="details-value">{{ selectedItem.inspectionId }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">检验日期</div>
                  <div class="details-value">{{ selectedItem.inspectionDate }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">供应商</div>
                  <div class="details-value">{{ selectedItem.supplier }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">货物名称</div>
                  <div class="details-value">{{ selectedItem.goodsName }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">批次号</div>
                  <div class="details-value">{{ selectedItem.batchNumber }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">数量</div>
                  <div class="details-value">{{ selectedItem.quantity }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">检验员</div>
                  <div class="details-value">{{ selectedItem.inspector }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">检验结果</div>
                  <div class="details-value" :class="selectedItem.result === '合格' ? 'text-success' : (selectedItem.result === '不合格' ? 'text-danger' : 'text-warning')">
                    {{ selectedItem.result }}
                  </div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item full-width">
                  <div class="details-label">备注</div>
                  <div class="details-value">{{ selectedItem.remarks || '无' }}</div>
                </div>
              </div>
            </div>
  
            <div class="parameters-section">
              <h3>质量参数</h3>
              <table class="parameters-table">
                <thead>
                  <tr>
                    <th>参数名称</th>
                    <th>标准值</th>
                    <th>实测值</th>
                    <th>是否合格</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(param, index) in selectedItem.parameters" :key="index">
                    <td>{{ param.name }}</td>
                    <td>{{ param.standard }}</td>
                    <td>{{ param.actual }}</td>
                    <td :class="param.passed === 'true' ? 'text-success' : 'text-danger'">
                      {{ param.passed === 'true' ? '合格' : '不合格' }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeDetailsModal">关闭</button>
            <button class="btn btn-primary" @click="printDetails">打印</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'FactoryQualityManage',
    data() {
      return {
        filters: {
          startDate: '',
          endDate: '',
          supplier: '',
          result: '',
          inspectionId: '',
          goodsName: ''
        },
        items: [
          {
            id: 1,
            inspectionId: 'QC20230501001',
            supplier: '供应商A',
            goodsName: '原材料X',
            batchNumber: 'BN001',
            inspectionDate: '2023-05-01',
            inspector: '张三',
            quantity: 1000,
            result: '合格',
            remarks: '所有参数均符合标准',
            parameters: [
              { name: '含水量', standard: '≤5%', actual: '3.5%', passed: 'true' },
              { name: '纯度', standard: '≥98%', actual: '99.1%', passed: 'true' },
              { name: '颗粒大小', standard: '2-5mm', actual: '3mm', passed: 'true' }
            ]
          },
          {
            id: 2,
            inspectionId: 'QC20230502002',
            supplier: '供应商B',
            goodsName: '原材料Y',
            batchNumber: 'BN002',
            inspectionDate: '2023-05-02',
            inspector: '李四',
            quantity: 800,
            result: '不合格',
            remarks: '纯度不达标',
            parameters: [
              { name: '含水量', standard: '≤3%', actual: '2.8%', passed: 'true' },
              { name: '纯度', standard: '≥99%', actual: '97.5%', passed: 'false' },
              { name: '颗粒大小', standard: '1-3mm', actual: '2mm', passed: 'true' }
            ]
          },
          // 更多测试数据...
        ],
        currentPage: 1,
        itemsPerPage: 10,
        selectedItems: [],
        showDetailsModal: false,
        selectedItem: {}
      }
    },
    computed: {
      filteredItems() {
        return this.items.filter(item => {
          let match = true
          
          if (this.filters.startDate && this.filters.endDate) {
            const itemDate = new Date(item.inspectionDate)
            const startDate = new Date(this.filters.startDate)
            const endDate = new Date(this.filters.endDate)
            match = match && (itemDate >= startDate && itemDate <= endDate)
          }
          
          if (this.filters.supplier) {
            match = match && item.supplier === this.filters.supplier
          }
          
          if (this.filters.result) {
            match = match && item.result === this.filters.result
          }
          
          if (this.filters.inspectionId) {
            match = match && item.inspectionId.includes(this.filters.inspectionId)
          }
          
          if (this.filters.goodsName) {
            match = match && item.goodsName.includes(this.filters.goodsName)
          }
          
          return match
        })
      },
      totalItems() {
        return this.filteredItems.length
      },
      totalPages() {
        return Math.ceil(this.totalItems / this.itemsPerPage)
      },
      displayedItems() {
        const start = (this.currentPage - 1) * this.itemsPerPage
        const end = start + this.itemsPerPage
        return this.filteredItems.slice(start, end)
      },
      startItem() {
        return this.totalItems === 0 ? 0 : (this.currentPage - 1) * this.itemsPerPage + 1
      },
      endItem() {
        return Math.min(this.currentPage * this.itemsPerPage, this.totalItems)
      },
      allSelected() {
        return this.displayedItems.length > 0 && this.selectedItems.length === this.displayedItems.length
      },
      displayedPages() {
        const pages = []
        const maxVisiblePages = 5
        
        if (this.totalPages <= maxVisiblePages) {
          for (let i = 1; i <= this.totalPages; i++) {
            pages.push(i)
          }
        } else {
          let startPage = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2))
          let endPage = startPage + maxVisiblePages - 1
          
          if (endPage > this.totalPages) {
            endPage = this.totalPages
            startPage = Math.max(1, endPage - maxVisiblePages + 1)
          }
          
          for (let i = startPage; i <= endPage; i++) {
            pages.push(i)
          }
        }
        
        return pages
      }
    },
    methods: {
      applyFilters() {
        this.currentPage = 1
        this.selectedItems = []
      },
      resetFilters() {
        this.filters = {
          startDate: '',
          endDate: '',
          supplier: '',
          result: '',
          inspectionId: '',
          goodsName: ''
        }
        this.currentPage = 1
        this.selectedItems = []
      },
      changePage(page) {
        this.currentPage = page
        this.selectedItems = []
      },
      toggleSelectAll(event) {
        if (event.target.checked) {
          this.selectedItems = this.displayedItems.map(item => item.id)
        } else {
          this.selectedItems = []
        }
      },
      viewDetails(item) {
        this.selectedItem = { ...item }
        this.showDetailsModal = true
      },
      closeDetailsModal() {
        this.showDetailsModal = false
      },
      editItem(item) {
        // 编辑功能实现
        console.log('编辑项目:', item)
      },
      deleteItem(item) {
        if (confirm(`确定要删除质检单 ${item.inspectionId} 吗？`)) {
          this.items = this.items.filter(i => i.id !== item.id)
          this.selectedItems = this.selectedItems.filter(id => id !== item.id)
        }
      },
      batchDelete() {
        if (confirm(`确定要删除选中的 ${this.selectedItems.length} 项记录吗？`)) {
          this.items = this.items.filter(item => !this.selectedItems.includes(item.id))
          this.selectedItems = []
        }
      },
      exportData() {
        // 导出功能实现
        console.log('导出数据')
        alert('数据导出成功！')
      },
      printDetails() {
        // 打印功能实现
        console.log('打印详情')
        window.print()
      }
    }
  }
  </script>
  
  <style scoped>
  .factory-quality-manage-container {
    padding: 20px;
  }
  
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #eee;
  }
  
  .page-header h1 {
    font-size: 24px;
    margin: 0;
  }
  
  .action-buttons {
    display: flex;
    gap: 10px;
  }
  
  .btn {
    padding: 8px 16px;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    display: inline-flex;
    align-items: center;
    gap: 5px;
    text-decoration: none;
    border: none;
  }
  
  .btn-sm {
    padding: 4px 8px;
    font-size: 12px;
  }
  
  .btn-primary {
    background-color: #4caf50;
    color: white;
  }
  
  .btn-secondary {
    background-color: #2196f3;
    color: white;
  }
  
  .btn-info {
    background-color: #03a9f4;
    color: white;
  }
  
  .btn-warning {
    background-color: #ff9800;
    color: white;
  }
  
  .btn-danger {
    background-color: #f44336;
    color: white;
  }
  
  .filter-section {
    background-color: #f9f9f9;
    border-radius: 4px;
    padding: 15px;
    margin-bottom: 20px;
  }
  
  .form-row {
    display: flex;
    gap: 15px;
    margin-bottom: 15px;
  }
  
  .form-row:last-child {
    margin-bottom: 0;
  }
  
  .form-group {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
  
  .form-group label {
    margin-bottom: 5px;
    font-size: 14px;
  }
  
  .form-group input,
  .form-group select {
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
  }
  
  .date-range-picker {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .date-range-picker input {
    flex: 1;
  }
  
  .filter-actions {
    justify-content: flex-end;
    flex-direction: row;
    align-items: flex-end;
    gap: 10px;
  }
  
  .data-table-section {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid #eee;
  }
  
  .table-title {
    font-size: 16px;
    font-weight: bold;
  }
  
  .table-actions {
    display: flex;
    align-items: center;
    gap: 15px;
  }
  
  .batch-actions {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .selected-count {
    font-size: 14px;
    color: #666;
  }
  
  .data-table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .data-table th,
  .data-table td {
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid #eee;
  }
  
  .data-table th {
    background-color: #f5f5f5;
    font-weight: bold;
  }
  
  .checkbox-cell {
    width: 40px;
    text-align: center;
  }
  
  .actions-cell {
    width: 120px;
    text-align: center;
    white-space: nowrap;
  }
  
  .actions-cell button {
    margin: 0 3px;
  }
  
  .text-success {
    color: #4caf50;
  }
  
  .text-warning {
    color: #ff9800;
  }
  
  .text-danger {
    color: #f44336;
  }
  
  .no-data {
    text-align: center;
    padding: 40px 0;
    color: #999;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }
  
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 15px;
    border-top: 1px solid #eee;
  }
  
  .pagination-btn {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #ddd;
    background-color: #fff;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .pagination-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .page-numbers {
    display: flex;
    margin: 0 10px;
  }
  
  .page-number {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #ddd;
    background-color: #fff;
    margin: 0 5px;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .page-number.active {
    background-color: #2196f3;
    color: white;
    border-color: #2196f3;
  }
  
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
    background-color: #fff;
    border-radius: 4px;
    width: 80%;
    max-width: 800px;
    max-height: 90vh;
    overflow-y: auto;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 20px;
    border-bottom: 1px solid #eee;
  }
  
  .modal-header h2 {
    margin: 0;
    font-size: 18px;
  }
  
  .close-btn {
    background: none;
    border: none;
    font-size: 18px;
    cursor: pointer;
    color: #999;
  }
  
  .modal-body {
    padding: 20px;
  }
  
  .modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding: 15px 20px;
    border-top: 1px solid #eee;
  }
  
  .details-section {
    margin-bottom: 20px;
  }
  
  .details-row {
    display: flex;
    gap: 20px;
    margin-bottom: 15px;
  }
  
  .details-item {
    flex: 1;
  }
  
  .details-item.full-width {
    flex: 2;
  }
  
  .details-label {
    font-size: 14px;
    color: #666;
    margin-bottom: 5px;
  }
  
  .details-value {
    font-size: 16px;
  }
  
  .parameters-section h3 {
    font-size: 16px;
    margin-bottom: 10px;
  }
  
  .parameters-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #eee;
  }
  
  .parameters-table th,
  .parameters-table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #eee;
  }
  
  .parameters-table th {
    background-color: #f5f5f5;
    font-weight: bold;
  }
  
  @media print {
    .page-header,
    .filter-section,
    .table-header,
    .pagination,
    .modal-footer,
    .close-btn {
      display: none;
    }
    
    .modal-container {
      box-shadow: none;
      width: 100%;
      max-width: 100%;
    }
    
    .modal-body {
      padding: 0;
    }
  }
  </style>
  
  