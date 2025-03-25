<template>
    <div class="sales-waybill-manage-container">
      <div class="page-header">
        <h1>销售运单管理</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/goods/base/sales-waybill" class="btn btn-secondary">
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
              <label for="dateRange">运单日期范围</label>
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
              <label for="transportCompany">运输公司</label>
              <select id="transportCompany" v-model="filters.transportCompany">
                <option value="">全部运输公司</option>
                <option value="运输公司A">运输公司A</option>
                <option value="运输公司B">运输公司B</option>
                <option value="运输公司C">运输公司C</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="waybillId">运单号</label>
              <input type="text" id="waybillId" v-model="filters.waybillId" placeholder="输入运单号">
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
          <div class="table-title">销售运单列表</div>
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
              <th>运单号</th>
              <th>客户</th>
              <th>运输公司</th>
              <th>发货日期</th>
              <th>货物名称</th>
              <th>数量(吨)</th>
              <th>运输状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in displayedItems" :key="item.id">
              <td class="checkbox-cell">
                <input type="checkbox" v-model="selectedItems" :value="item.id">
              </td>
              <td>{{ item.waybillId }}</td>
              <td>{{ item.customer }}</td>
              <td>{{ item.transportCompany }}</td>
              <td>{{ item.shipDate }}</td>
              <td>{{ item.goodsName }}</td>
              <td class="number-cell">{{ item.quantity }}</td>
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
          <p>没有找到匹配的运单记录</p>
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
            <h2>运单详情</h2>
            <button class="close-btn" @click="closeDetailsModal">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="details-section">
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">运单号</div>
                  <div class="details-value">{{ selectedItem.waybillId }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">发货日期</div>
                  <div class="details-value">{{ selectedItem.shipDate }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">客户</div>
                  <div class="details-value">{{ selectedItem.customer }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">运输公司</div>
                  <div class="details-value">{{ selectedItem.transportCompany }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">货物名称</div>
                  <div class="details-value">{{ selectedItem.goodsName }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">数量(吨)</div>
                  <div class="details-value">{{ selectedItem.quantity }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">发货地址</div>
                  <div class="details-value">{{ selectedItem.fromAddress }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">收货���址</div>
                  <div class="details-value">{{ selectedItem.toAddress }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">车牌号</div>
                  <div class="details-value">{{ selectedItem.vehicleNumber }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">司机姓名</div>
                  <div class="details-value">{{ selectedItem.driverName }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item">
                  <div class="details-label">司机电话</div>
                  <div class="details-value">{{ selectedItem.driverPhone }}</div>
                </div>
                <div class="details-item">
                  <div class="details-label">运输状态</div>
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
                  <div class="details-label">关联出库单号</div>
                  <div class="details-value">{{ selectedItem.outboundId }}</div>
                </div>
              </div>
              <div class="details-row">
                <div class="details-item full-width">
                  <div class="details-label">备注</div>
                  <div class="details-value">{{ selectedItem.remarks || '无' }}</div>
                </div>
              </div>
            </div>
  
            <div class="waybill-tracking" v-if="selectedItem.trackingInfo && selectedItem.trackingInfo.length > 0">
              <h3>运输跟踪</h3>
              <div class="tracking-timeline">
                <div 
                  v-for="(track, index) in selectedItem.trackingInfo" 
                  :key="index" 
                  class="tracking-item"
                >
                  <div class="tracking-time">{{ track.time }}</div>
                  <div class="tracking-status">{{ track.status }}</div>
                  <div class="tracking-location">{{ track.location }}</div>
                  <div class="tracking-desc">{{ track.description }}</div>
                </div>
              </div>
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
    name: 'SalesWaybillManage',
    data() {
      return {
        filters: {
          startDate: '',
          endDate: '',
          customer: '',
          transportCompany: '',
          waybillId: '',
          contractId: ''
        },
        items: [
          {
            id: 1,
            waybillId: 'SW20230501001',
            customer: '客户A',
            transportCompany: '运输公司A',
            shipDate: '2023-05-01',
            goodsName: '小麦',
            quantity: 30,
            fromAddress: '河南省郑州市金水区粮食仓库',
            toAddress: '河北省石家庄市桥西区面粉厂',
            vehicleNumber: '豫A12345',
            driverName: '张师傅',
            driverPhone: '13800138000',
            status: '已送达',
            contractId: 'SC20230401001',
            outboundId: 'OB20230501001',
            remarks: '按时送达',
            trackingInfo: [
              {
                time: '2023-05-01 08:00',
                status: '已装车',
                location: '河南省郑州市金水区粮食仓库',
                description: '货物已装车，准备发运'
              },
              {
                time: '2023-05-01 12:30',
                status: '运输中',
                location: '河南省新乡市',
                description: '货物运输中，一切正常'
              },
              {
                time: '2023-05-01 18:45',
                status: '已送达',
                location: '河北省石家庄市桥西区面粉厂',
                description: '货物已送达目的地，客户已签收'
              }
            ]
          },
          {
            id: 2,
            waybillId: 'SW20230502002',
            customer: '客户B',
            transportCompany: '运输公司B',
            shipDate: '2023-05-02',
            goodsName: '玉米',
            quantity: 25,
            fromAddress: '河南省周口市粮食仓库',
            toAddress: '山东省济南市历下区饲料厂',
            vehicleNumber: '豫B54321',
            driverName: '李师傅',
            driverPhone: '13900139000',
            status: '运输中',
            contractId: 'SC20230402002',
            outboundId: 'OB20230502002',
            remarks: '预计明天到达',
            trackingInfo: [
              {
                time: '2023-05-02 09:15',
                status: '已装车',
                location: '河南省周口市粮食仓库',
                description: '货物已装车，准备发运'
              },
              {
                time: '2023-05-02 14:20',
                status: '运输中',
                location: '河南省商丘市',
                description: '货物运输中，一切正常'
              }
            ]
          },
          {
            id: 3,
            waybillId: 'SW20230503003',
            customer: '客户C',
            transportCompany: '运输公司C',
            shipDate: '2023-05-03',
            goodsName: '大豆',
            quantity: 20,
            fromAddress: '黑龙江省哈尔滨市粮食仓库',
            toAddress: '辽宁省大连市中山区油厂',
            vehicleNumber: '黑A67890',
            driverName: '王师傅',
            driverPhone: '13700137000',
            status: '已装车',
            contractId: 'SC20230403003',
            outboundId: 'OB20230503003',
            remarks: '长途运输，预计3天后到达',
            trackingInfo: [
              {
                time: '2023-05-03 10:30',
                status: '已装车',
                location: '黑龙江省哈尔滨市粮食仓库',
                description: '货物已装车，准备发运'
              }
            ]
          },
          {
            id: 4,
            waybillId: 'SW20230504004',
            customer: '客户A',
            transportCompany: '运输公司A',
            shipDate: '2023-05-04',
            goodsName: '稻谷',
            quantity: 35,
            fromAddress: '江苏省南京市江宁区粮食仓库',
            toAddress: '浙江省杭州市西湖区米厂',
            vehicleNumber: '苏A98765',
            driverName: '赵师傅',
            driverPhone: '13600136000',
            status: '已送达',
            contractId: 'SC20230404004',
            outboundId: 'OB20230504004',
            remarks: '客户已确认收货',
            trackingInfo: [
              {
                time: '2023-05-04 07:45',
                status: '已装车',
                location: '江苏省南京市江宁区粮食仓库',
                description: '货物已装车，准备发运'
              },
              {
                time: '2023-05-04 11:30',
                status: '运输中',
                location: '江苏省苏州市',
                description: '货物运输中，一切正常'
              },
              {
                time: '2023-05-04 16:15',
                status: '已送达',
                location: '浙江省杭州市西湖区米厂',
                description: '货物已送达目的地，客户已签收'
              }
            ]
          },
          {
            id: 5,
            waybillId: 'SW20230505005',
            customer: '客户D',
            transportCompany: '运输公司B',
            shipDate: '2023-05-05',
            goodsName: '高粱',
            quantity: 15,
            fromAddress: '内蒙古呼和浩特市粮食仓库',
            toAddress: '北京市朝阳区酒厂',
            vehicleNumber: '蒙A24680',
            driverName: '钱师傅',
            driverPhone: '13500135000',
            status: '异常',
            contractId: 'SC20230405005',
            outboundId: 'OB20230505005',
            remarks: '车辆故障，正在处理',
            trackingInfo: [
              {
                time: '2023-05-05 08:30',
                status: '已装车',
                location: '内蒙古呼和浩特市粮食仓库',
                description: '货物已装车，准备发运'
              },
              {
                time: '2023-05-05 13:45',
                status: '运输中',
                location: '内蒙古集宁市',
                description: '货物运输中，一切正常'
              },
              {
                time: '2023-05-05 17:20',
                status: '异常',
                location: '河北省张家口市',
                description: '车辆发生故障，正在维修'
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
            const itemDate = new Date(item.shipDate)
            const startDate = new Date(this.filters.startDate)
            const endDate = new Date(this.filters.endDate)
            match = match && (itemDate >= startDate && itemDate <= endDate)
          }
          
          if (this.filters.customer) {
            match = match && item.customer === this.filters.customer
          }
          
          if (this.filters.transportCompany) {
            match = match && item.transportCompany === this.filters.transportCompany
          }
          
          if (this.filters.waybillId) {
            match = match && item.waybillId.includes(this.filters.waybillId)
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
      getStatusClass(status) {
        switch (status) {
          case '已送达':
            return 'text-success'
          case '运输中':
            return 'text-info'
          case '已装车':
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
          transportCompany: '',
          waybillId: '',
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
        if (confirm(`确定要删除运单 ${item.waybillId} 吗？`)) {
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
  .sales-waybill-manage-container {
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
  
  .number-cell {
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
  
  .waybill-tracking {
    margin-top: 20px;
  }
  
  .waybill-tracking h3 {
    font-size: 16px;
    margin-bottom: 15px;
  }
  
  .tracking-timeline {
    position: relative;
    padding-left: 30px;
  }
  
  .tracking-timeline::before {
    content: '';
    position: absolute;
    top: 0;
    bottom: 0;
    left: 10px;
    width: 2px;
    background-color: #e0e0e0;
  }
  
  .tracking-item {
    position: relative;
    padding-bottom: 20px;
  }
  
  .tracking-item::before {
    content: '';
    position: absolute;
    top: 0;
    left: -30px;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #2196f3;
  }
  
  .tracking-time {
    font-size: 14px;
    color: #666;
    margin-bottom: 5px;
  }
  
  .tracking-status {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .tracking-location {
    font-size: 14px;
    margin-bottom: 5px;
  }
  
  .tracking-desc {
    font-size: 14px;
    color: #666;
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
  
  