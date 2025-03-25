<template>
    <div class="sales-receipt-manage-container">
      <div class="page-header">
        <h1>销售收款管理</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/finance/sales-receipt" class="btn btn-secondary">
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
              <label for="dateRange">收款日期范围</label>
              <div class="date-range-picker">
                <input type="date" v-model="filters.startDate">
                <span>至</span>
                <input type="date" v-model="filters.endDate">
              </div>
            </div>
            <div class="form-group">
              <label for="customer">客户</label>
              <select id="customer" v-model="filters.customer">
                <option value="">全部客户</option>
                <option value="客户A">客户A</option>
                <option value="客户B">客户B</option>
                <option value="客户C">客户C</option>
                <option value="客户D">客户D</option>
              </select>
            </div>
            <div class="form-group">
              <label for="paymentMethod">收款方式</label>
              <select id="paymentMethod" v-model="filters.paymentMethod">
                <option value="">全部方式</option>
                <option value="银行转账">银行转账</option>
                <option value="现金">现金</option>
                <option value="支票">支票</option>
                <option value="其他">其他</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="receiptId">收款单号</label>
              <input type="text" id="receiptId" v-model="filters.receiptId" placeholder="输入收款单号">
            </div>
            <div class="form-group">
              <label for="contractId">关联合同号</label>
              <input type="text" id="contractId" v-model="filters.contractId" placeholder="输入合同号">
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
          <div class="table-title">收款记录列表</div>
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
              <th>收款单号</th>
              <th>客户</th>
              <th>收款金额</th>
              <th>收款日期</th>
              <th>收款方式</th>
              <th>关联合同</th>
              <th>收款状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in displayedItems" :key="item.id">
              <td class="checkbox-cell">
                <input type="checkbox" v-model="selectedItems" :value="item.id">
              </td>
              <td>{{ item.receiptId }}</td>
              <td>{{ item.customer }}</td>
              <td class="amount-cell">{{ formatCurrency(item.amount) }}</td>
              <td>{{ item.receiptDate }}</td>
              <td>{{ item.paymentMethod }}</td>
              <td>{{ item.contractId }}</td>
              <td :class="getStatusClass(item.status)">
                {{ item.status }}
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
          <p>没有找到匹配的收款记录</p>
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
            <h2>收款详情</h2>
            <button class="close-btn" @click="closeDetailsModal">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="details-section">
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">收款单号</div>
                  <div class="details-value">{{ selectedItem.receiptId }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">收款日期</div>
                  <div class="details-value">{{ selectedItem.receiptDate }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">客户</div>
                  <div class="details-value">{{ selectedItem.customer }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">收款金额</div>
                  <div class="details-value amount-value">{{ formatCurrency(selectedItem.amount) }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">收款方式</div>
                  <div class="details-value">{{ selectedItem.paymentMethod }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">收款状态</div>
                  <div class="details-value" :class="getStatusClass(selectedItem.status)">
                    {{ selectedItem.status }}
                  </div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">关联合同号</div>
                  <div class="details-value">{{ selectedItem.contractId }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">关联结算单号</div>
                  <div class="details-value">{{ selectedItem.settlementId }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">收款账户</div>
                  <div class="details-value">{{ selectedItem.accountInfo }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">收款人</div>
                  <div class="details-value">{{ selectedItem.receiver }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item full-width">
                  <div class="details-label">备注</div>
                  <div class="details-value">{{ selectedItem.remarks || '无' }}</div>
                </div>
              </div>
            </div>
  
            <div class="related-documents" v-if="selectedItem.relatedDocuments && selectedItem.relatedDocuments.length > 0">
              <h3>相关单据</h3>
              <table class="related-table">
                <thead>
                  <tr>
                    <th>单据类型</th>
                    <th>单据编号</th>
                    <th>日期</th>
                    <th>金额</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(doc, index) in selectedItem.relatedDocuments" :key="index">
                    <td>{{ doc.type }}</td>
                    <td>{{ doc.id }}</td>
                    <td>{{ doc.date }}</td>
                    <td class="amount-cell">{{ formatCurrency(doc.amount) }}</td>
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
    name: 'SalesReceiptManage',
    data() {
      return {
        filters: {
          startDate: '',
          endDate: '',
          customer: '',
          paymentMethod: '',
          receiptId: '',
          contractId: ''
        },
        items: [
          {
            id: 1,
            receiptId: 'SR20230501001',
            customer: '客户A',
            amount: 150000,
            receiptDate: '2023-05-01',
            paymentMethod: '银行转账',
            contractId: 'SC20230401001',
            settlementId: 'SS20230430001',
            accountInfo: '中国银行 6225xxxxxxxxxxxx',
            receiver: '张经理',
            status: '已确认',
            remarks: '按时收款',
            relatedDocuments: [
              {
                type: '销售合同',
                id: 'SC20230401001',
                date: '2023-04-01',
                amount: 300000
              },
              {
                type: '销售结算单',
                id: 'SS20230430001',
                date: '2023-04-30',
                amount: 150000
              }
            ]
          },
          {
            id: 2,
            receiptId: 'SR20230502002',
            customer: '客户B',
            amount: 85000,
            receiptDate: '2023-05-02',
            paymentMethod: '支票',
            contractId: 'SC20230402002',
            settlementId: 'SS20230501002',
            accountInfo: '工商银行 6222xxxxxxxxxxxx',
            receiver: '李财务',
            status: '待确认',
            remarks: '支票待入账',
            relatedDocuments: [
              {
                type: '销售合同',
                id: 'SC20230402002',
                date: '2023-04-02',
                amount: 85000
              },
              {
                type: '销售结算单',
                id: 'SS20230501002',
                date: '2023-05-01',
                amount: 85000
              }
            ]
          },
          {
            id: 3,
            receiptId: 'SR20230503003',
            customer: '客户C',
            amount: 200000,
            receiptDate: '2023-05-03',
            paymentMethod: '银行转账',
            contractId: 'SC20230403003',
            settlementId: 'SS20230502003',
            accountInfo: '建设银行 6227xxxxxxxxxxxx',
            receiver: '王出纳',
            status: '已确认',
            remarks: '分期付款第一期',
            relatedDocuments: [
              {
                type: '销售合同',
                id: 'SC20230403003',
                date: '2023-04-03',
                amount: 500000
              },
              {
                type: '销售结算单',
                id: 'SS20230502003',
                date: '2023-05-02',
                amount: 200000
              }
            ]
          },
          {
            id: 4,
            receiptId: 'SR20230504004',
            customer: '客户A',
            amount: 120000,
            receiptDate: '2023-05-04',
            paymentMethod: '现金',
            contractId: 'SC20230404004',
            settlementId: 'SS20230503004',
            accountInfo: '现金交接',
            receiver: '赵会计',
            status: '已确认',
            remarks: '现金已入账',
            relatedDocuments: [
              {
                type: '销售合同',
                id: 'SC20230404004',
                date: '2023-04-04',
                amount: 120000
              },
              {
                type: '销售结算单',
                id: 'SS20230503004',
                date: '2023-05-03',
                amount: 120000
              }
            ]
          },
          {
            id: 5,
            receiptId: 'SR20230505005',
            customer: '客户D',
            amount: 300000,
            receiptDate: '2023-05-05',
            paymentMethod: '银行转账',
            contractId: 'SC20230405005',
            settlementId: 'SS20230504005',
            accountInfo: '农业银行 6228xxxxxxxxxxxx',
            receiver: '钱经理',
            status: '异常',
            remarks: '金额不符，需核实',
            relatedDocuments: [
              {
                type: '销售合同',
                id: 'SC20230405005',
                date: '2023-04-05',
                amount: 350000
              },
              {
                type: '销售结算单',
                id: 'SS20230504005',
                date: '2023-05-04',
                amount: 350000
              }
            ]
          }
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
            const itemDate = new Date(item.receiptDate)
            const startDate = new Date(this.filters.startDate)
            const endDate = new Date(this.filters.endDate)
            match = match && (itemDate >= startDate && itemDate <= endDate)
          }
          
          if (this.filters.customer) {
            match = match && item.customer === this.filters.customer
          }
          
          if (this.filters.paymentMethod) {
            match = match && item.paymentMethod === this.filters.paymentMethod
          }
          
          if (this.filters.receiptId) {
            match = match && item.receiptId.includes(this.filters.receiptId)
          }
          
          if (this.filters.contractId) {
            match = match && item.contractId.includes(this.filters.contractId)
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
      formatCurrency(value) {
        return new Intl.NumberFormat('zh-CN', {
          style: 'currency',
          currency: 'CNY',
          minimumFractionDigits: 2
        }).format(value)
      },
      getStatusClass(status) {
        switch (status) {
          case '已确认':
            return 'text-success'
          case '待确认':
            return 'text-warning'
          case '异常':
            return 'text-danger'
          default:
            return ''
        }
      },
      applyFilters() {
        this.currentPage = 1
        this.selectedItems = []
      },
      resetFilters() {
        this.filters = {
          startDate: '',
          endDate: '',
          customer: '',
          paymentMethod: '',
          receiptId: '',
          contractId: ''
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
        if (confirm(`确定要删除收款单 ${item.receiptId} 吗？`)) {
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
  .sales-receipt-manage-container {
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
  
  .amount-cell {
    text-align: right;
    font-family: monospace;
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
  
  .text-info {
    color: #2196f3;
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
  
  .amount-value {
    font-weight: bold;
    font-family: monospace;
  }
  
  .related-documents h3 {
    font-size: 16px;
    margin-bottom: 15px;
  }
  
  .related-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }
  
  .related-table th,
  .related-table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #eee;
  }
  
  .related-table th {
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
  
  