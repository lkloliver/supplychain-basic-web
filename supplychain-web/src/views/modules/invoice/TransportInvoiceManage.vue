<template>
    <div class="transport-invoice-manage-container">
      <div class="page-header">
        <h1>运输发票管理</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/invoice/transport" class="btn btn-secondary">
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
              <label for="dateRange">开票日期范围</label>
              <div class="date-range-picker">
                <input type="date" v-model="filters.startDate">
                <span>至</span>
                <input type="date" v-model="filters.endDate">
              </div>
            </div>
            <div class="form-group">
              <label for="transportCompany">运输公司</label>
              <select id="transportCompany" v-model="filters.transportCompany">
                <option value="">全部运输公司</option>
                <option value="运输公司A">运输公司A</option>
                <option value="运输公司B">运输公司B</option>
                <option value="运输公司C">运输公司C</option>
                <option value="运输公司D">运输公司D</option>
              </select>
            </div>
            <div class="form-group">
              <label for="invoiceType">发票类型</label>
              <select id="invoiceType" v-model="filters.invoiceType">
                <option value="">全部类型</option>
                <option value="增值税专用发票">增值税专用发票</option>
                <option value="增值税普通发票">增值税普通发票</option>
                <option value="电子发票">电子发票</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="invoiceId">发票号码</label>
              <input type="text" id="invoiceId" v-model="filters.invoiceId" placeholder="输入发票号码">
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
          <div class="table-title">运输发票列表</div>
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
              <th>发票号码</th>
              <th>运输公司</th>
              <th>发票类型</th>
              <th>开票日期</th>
              <th>发票金额</th>
              <th>税率</th>
              <th>关联合同</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in displayedItems" :key="item.id">
              <td class="checkbox-cell">
                <input type="checkbox" v-model="selectedItems" :value="item.id">
              </td>
              <td>{{ item.invoiceId }}</td>
              <td>{{ item.transportCompany }}</td>
              <td>{{ item.invoiceType }}</td>
              <td>{{ item.invoiceDate }}</td>
              <td class="amount-cell">{{ formatCurrency(item.amount) }}</td>
              <td>{{ item.taxRate }}%</td>
              <td>{{ item.contractId }}</td>
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
          <p>没有找到匹配的发票记录</p>
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
            <h2>发票详情</h2>
            <button class="close-btn" @click="closeDetailsModal">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="details-section">
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">发票号码</div>
                  <div class="details-value">{{ selectedItem.invoiceId }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">开票日期</div>
                  <div class="details-value">{{ selectedItem.invoiceDate }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">运输公司</div>
                  <div class="details-value">{{ selectedItem.transportCompany }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">发票类型</div>
                  <div class="details-value">{{ selectedItem.invoiceType }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">发票金额</div>
                  <div class="details-value amount-value">{{ formatCurrency(selectedItem.amount) }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">税率</div>
                  <div class="details-value">{{ selectedItem.taxRate }}%</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">税额</div>
                  <div class="details-value amount-value">{{ formatCurrency(selectedItem.taxAmount) }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">不含税金额</div>
                  <div class="details-value amount-value">{{ formatCurrency(selectedItem.amountBeforeTax) }}</div>
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
                  <div class="details-label">开票单位</div>
                  <div class="details-value">{{ selectedItem.issuer }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">收票单位</div>
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
  
            <div class="invoice-items" v-if="selectedItem.items && selectedItem.items.length > 0">
              <h3>发票明细</h3>
              <table class="related-table">
                <thead>
                  <tr>
                    <th>项目名称</th>
                    <th>规格型号</th>
                    <th>单位</th>
                    <th>数量</th>
                    <th>单价</th>
                    <th>金额</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in selectedItem.items" :key="index">
                    <td>{{ item.name }}</td>
                    <td>{{ item.spec }}</td>
                    <td>{{ item.unit }}</td>
                    <td>{{ item.quantity }}</td>
                    <td class="amount-cell">{{ formatCurrency(item.price) }}</td>
                    <td class="amount-cell">{{ formatCurrency(item.amount) }}</td>
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
    name: 'TransportInvoiceManage',
    data() {
      return {
        filters: {
          startDate: '',
          endDate: '',
          transportCompany: '',
          invoiceType: '',
          invoiceId: '',
          contractId: ''
        },
        items: [
          {
            id: 1,
            invoiceId: 'TI20230501001',
            transportCompany: '运输公司A',
            invoiceType: '增值税专用发票',
            invoiceDate: '2023-05-01',
            amount: 11300,
            taxRate: 9,
            taxAmount: 934.86,
            amountBeforeTax: 10365.14,
            contractId: 'TC20230401001',
            settlementId: 'TS20230430001',
            issuer: '运输公司A',
            receiver: '本公司',
            remarks: '运输服务发票',
            items: [
              {
                name: '运输服务',
                spec: '公路运输',
                unit: '次',
                quantity: 5,
                price: 2260,
                amount: 11300
              }
            ]
          },
          {
            id: 2,
            invoiceId: 'TI20230502002',
            transportCompany: '运输公司B',
            invoiceType: '增值税普通发票',
            invoiceDate: '2023-05-02',
            amount: 8500,
            taxRate: 3,
            taxAmount: 248.54,
            amountBeforeTax: 8251.46,
            contractId: 'TC20230402002',
            settlementId: 'TS20230501002',
            issuer: '运输公司B',
            receiver: '本公司',
            remarks: '短途运输服务',
            items: [
              {
                name: '短途运输',
                spec: '市内运输',
                unit: '次',
                quantity: 10,
                price: 850,
                amount: 8500
              }
            ]
          },
          {
            id: 3,
            invoiceId: 'TI20230503003',
            transportCompany: '运输公司C',
            invoiceType: '电子发票',
            invoiceDate: '2023-05-03',
            amount: 15000,
            taxRate: 6,
            taxAmount: 849.06,
            amountBeforeTax: 14150.94,
            contractId: 'TC20230403003',
            settlementId: 'TS20230502003',
            issuer: '运输公司C',
            receiver: '本公司',
            remarks: '长途运输服务',
            items: [
              {
                name: '长途运输',
                spec: '省际运输',
                unit: '次',
                quantity: 3,
                price: 5000,
                amount: 15000
              }
            ]
          },
          {
            id: 4,
            invoiceId: 'TI20230504004',
            transportCompany: '运输公司A',
            invoiceType: '增值税专用发票',
            invoiceDate: '2023-05-04',
            amount: 9800,
            taxRate: 9,
            taxAmount: 810.09,
            amountBeforeTax: 8989.91,
            contractId: 'TC20230404004',
            settlementId: 'TS20230503004',
            issuer: '运输公司A',
            receiver: '本公司',
            remarks: '货物运输服务',
            items: [
              {
                name: '货物运输',
                spec: '公路运输',
                unit: '次',
                quantity: 4,
                price: 2450,
                amount: 9800
              }
            ]
          },
          {
            id: 5,
            invoiceId: 'TI20230505005',
            transportCompany: '运输公司D',
            invoiceType: '增值税专用发票',
            invoiceDate: '2023-05-05',
            amount: 22000,
            taxRate: 9,
            taxAmount: 1818.35,
            amountBeforeTax: 20181.65,
            contractId: 'TC20230405005',
            settlementId: 'TS20230504005',
            issuer: '运输公司D',
            receiver: '本公司',
            remarks: '大宗货物运输',
            items: [
              {
                name: '大宗货物运输',
                spec: '铁路联运',
                unit: '次',
                quantity: 2,
                price: 11000,
                amount: 22000
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
            const itemDate = new Date(item.invoiceDate)
            const startDate = new Date(this.filters.startDate)
            const endDate = new Date(this.filters.endDate)
            match = match && (itemDate >= startDate && itemDate <= endDate)
          }
          
          if (this.filters.transportCompany) {
            match = match && item.transportCompany === this.filters.transportCompany
          }
          
          if (this.filters.invoiceType) {
            match = match && item.invoiceType === this.filters.invoiceType
          }
          
          if (this.filters.invoiceId) {
            match = match && item.invoiceId.includes(this.filters.invoiceId)
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
      applyFilters() {
        this.currentPage = 1
        this.selectedItems = []
      },
      resetFilters() {
        this.filters = {
          startDate: '',
          endDate: '',
          transportCompany: '',
          invoiceType: '',
          invoiceId: '',
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
        if (confirm(`确定要删除发票 ${item.invoiceId} 吗？`)) {
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
  .transport-invoice-manage-container {
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
  
  .invoice-items h3 {
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
  
  