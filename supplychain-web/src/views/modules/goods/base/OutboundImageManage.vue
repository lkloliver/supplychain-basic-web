<template>
    <div class="outbound-image-manage-container">
      <div class="page-header">
        <h1>出库图片管理</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/goods/base/outbound-image" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> 返回
          </router-link>
          <button class="btn btn-primary" @click="exportData">
            <i class="fas fa-download"></i> 导出数据
          </button>
        </div>
      </div>
      
      <div class="operation-tabs">
        <router-link 
          :to="{ name: 'OutboundImageUpload' }" 
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
            :class="{ active: isActive }"
            @click="navigate"
          >
            资料上传
          </div>
        </router-link>
        <router-link 
          :to="{ name: 'OutboundImageManage' }" 
          custom
          v-slot="{ isActive }"
        >
          <div class="operation-tab active">
            资料管理
          </div>
        </router-link>
      </div>
      
      <div class="filter-section">
        <div class="filter-form">
          <div class="form-row">
            <div class="form-group">
              <label for="dateRange">出库日期范围</label>
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
              <label for="goodsName">货物名称</label>
              <input type="text" id="goodsName" v-model="filters.goodsName" placeholder="输入货物名称">
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="outboundId">出库单号</label>
              <input type="text" id="outboundId" v-model="filters.outboundId" placeholder="输入出库单号">
            </div>
            <div class="form-group">
              <label for="warehouseLocation">仓库位置</label>
              <input type="text" id="warehouseLocation" v-model="filters.warehouseLocation" placeholder="输入仓库位置">
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
          <div class="table-title">出库图片记录</div>
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
              <th>出库单号</th>
              <th>出库日期</th>
              <th>客户</th>
              <th>货物名称</th>
              <th>数量(吨)</th>
              <th>仓库位置</th>
              <th>图片数量</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in displayedItems" :key="item.id">
              <td class="checkbox-cell">
                <input type="checkbox" v-model="selectedItems" :value="item.id">
              </td>
              <td>{{ item.outboundId }}</td>
              <td>{{ item.outboundDate }}</td>
              <td>{{ item.customer }}</td>
              <td>{{ item.goodsName }}</td>
              <td class="number-cell">{{ item.quantity }}</td>
              <td>{{ item.warehouseLocation }}</td>
              <td class="number-cell">{{ getTotalImageCount(item) }}</td>
              <td class="actions-cell">
                <button class="btn btn-info btn-sm" @click="viewImages(item)">
                  <i class="fas fa-images"></i>
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
          <p>没有找到匹配的出库图片记录</p>
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
      
      <!-- 图片查看弹窗 -->
      <div v-if="showImageViewer" class="modal-overlay" @click="closeImageViewer">
        <div class="modal-container image-viewer-container" @click.stop>
          <div class="modal-header">
            <h2>出库图片查看 - {{ selectedItem.outboundId }}</h2>
            <button class="close-btn" @click="closeImageViewer">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="image-categories">
              <div 
                v-for="(category, index) in imageCategories" 
                :key="index"
                :class="['category-tab', viewerCategory === index ? 'active' : '']"
                @click="viewerCategory = index"
              >
                {{ category.name }} ({{ selectedItem.images[index].length }})
              </div>
            </div>
            
            <div class="image-gallery">
              <div v-if="selectedItem.images[viewerCategory].length === 0" class="no-images">
                <i class="fas fa-image fa-3x"></i>
                <p>该类别下暂无图片</p>
              </div>
              <div 
                v-else
                v-for="(image, imgIndex) in selectedItem.images[viewerCategory]" 
                :key="imgIndex"
                class="gallery-item"
                @click="openFullImage(image)"
              >
                <img :src="image.url" :alt="image.name">
                <div class="image-name">{{ image.name }}</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeImageViewer">关闭</button>
            <button class="btn btn-primary" @click="downloadAllImages">下载全部图片</button>
          </div>
        </div>
      </div>
      
      <!-- 全屏图片查看 -->
      <div v-if="showFullImage" class="modal-overlay full-image-overlay" @click="showFullImage = false">
        <div class="full-image-container">
          <button class="close-btn" @click="showFullImage = false">
            <i class="fas fa-times"></i>
          </button>
          <img :src="fullImageUrl" alt="图片全屏查看">
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'OutboundImageManage',
    data() {
      return {
        filters: {
          startDate: '',
          endDate: '',
          customer: '',
          goodsName: '',
          outboundId: '',
          warehouseLocation: ''
        },
        items: [
          {
            id: 1,
            outboundId: 'OB20230501001',
            outboundDate: '2023-05-01',
            customer: '客户A',
            goodsName: '小麦',
            quantity: 30,
            warehouseLocation: 'A区-01-01',
            remarks: '正常出库',
            images: [
              [
                { name: '装车图1.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '装车图2.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '装车图3.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '车辆全景1.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '车辆全景2.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '过磅单.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              []
            ]
          },
          {
            id: 2,
            outboundId: 'OB20230502002',
            outboundDate: '2023-05-02',
            customer: '客户B',
            goodsName: '玉米',
            quantity: 25,
            warehouseLocation: 'B区-02-03',
            remarks: '正常出库',
            images: [
              [
                { name: '装车图1.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '装车图2.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '车辆全景.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '过磅单.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '其他资料.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ]
            ]
          },
          {
            id: 3,
            outboundId: 'OB20230503003',
            outboundDate: '2023-05-03',
            customer: '客户C',
            goodsName: '大豆',
            quantity: 20,
            warehouseLocation: 'C区-03-02',
            remarks: '正常出库',
            images: [
              [
                { name: '装车图1.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '装车图2.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '装车图3.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '装车图4.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '车辆全景1.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '车辆全景2.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '过磅单.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              []
            ]
          },
          {
            id: 4,
            outboundId: 'OB20230504004',
            outboundDate: '2023-05-04',
            customer: '客户A',
            goodsName: '稻谷',
            quantity: 35,
            warehouseLocation: 'A区-02-04',
            remarks: '正常出库',
            images: [
              [
                { name: '装车图1.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '装车图2.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '车辆全景.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '过磅单.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              []
            ]
          },
          {
            id: 5,
            outboundId: 'OB20230505005',
            outboundDate: '2023-05-05',
            customer: '客户D',
            goodsName: '高粱',
            quantity: 15,
            warehouseLocation: 'D区-01-05',
            remarks: '正常出库',
            images: [
              [
                { name: '装车图.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '车辆全景.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '过磅单.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ],
              [
                { name: '其他资料1.jpg', url: '/placeholder.svg?height=800&width=1200' },
                { name: '其他资料2.jpg', url: '/placeholder.svg?height=800&width=1200' }
              ]
            ]
          }
        ],
        imageCategories: [
          { name: '货物装车图' },
          { name: '车辆全景图' },
          { name: '过磅单图' },
          { name: '其他图片' }
        ],
        currentPage: 1,
        itemsPerPage: 10,
        selectedItems: [],
        showImageViewer: false,
        viewerCategory: 0,
        selectedItem: {
          images: [[], [], [], []]
        },
        showFullImage: false,
        fullImageUrl: ''
      }
    },
    computed: {
      filteredItems() {
        return this.items.filter(item => {
          let match = true
          
          if (this.filters.startDate && this.filters.endDate) {
            const itemDate = new Date(item.outboundDate)
            const startDate = new Date(this.filters.startDate)
            const endDate = new Date(this.filters.endDate)
            match = match && (itemDate >= startDate && itemDate <= endDate)
          }
          
          if (this.filters.customer) {
            match = match && item.customer === this.filters.customer
          }
          
          if (this.filters.goodsName) {
            match = match && item.goodsName.includes(this.filters.goodsName)
          }
          
          if (this.filters.outboundId) {
            match = match && item.outboundId.includes(this.filters.outboundId)
          }
          
          if (this.filters.warehouseLocation) {
            match = match && item.warehouseLocation.includes(this.filters.warehouseLocation)
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
      getTotalImageCount(item) {
        return item.images.reduce((total, category) => total + category.length, 0)
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
          goodsName: '',
          outboundId: '',
          warehouseLocation: ''
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
      viewImages(item) {
        this.selectedItem = { ...item }
        this.viewerCategory = 0
        this.showImageViewer = true
      },
      closeImageViewer() {
        this.showImageViewer = false
      },
      openFullImage(image) {
        this.fullImageUrl = image.url
        this.showFullImage = true
      },
      editItem(item) {
        // 编辑功能实现
        console.log('编辑项目:', item)
      },
      deleteItem(item) {
        if (confirm(`确定要删除出库单 ${item.outboundId} 的图片记录吗？`)) {
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
      downloadAllImages() {
        // 下载功能实现
        console.log('下载全部图片')
        alert('图片下载已开始，请稍候...')
      }
    }
  }
  </script>
  
  <style scoped>
  .outbound-image-manage-container {
    padding: 20px;
  }
  
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
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
  
  .operation-tabs {
    display: flex;
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 20px;
  }
  
  .operation-tab {
    padding: 10px 20px;
    font-size: 14px;
    font-weight: 500;
    color: #757575;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .operation-tab.active {
    color: #2196f3;
    border-bottom: 2px solid #2196f3;
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
    max-width: 900px;
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
  
  .image-categories {
    display: flex;
    border-bottom: 1px solid #eee;
    margin-bottom: 20px;
  }
  
  .category-tab {
    padding: 10px 20px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .category-tab.active {
    color: #2196f3;
    border-bottom: 2px solid #2196f3;
  }
  
  .image-gallery {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
  }
  
  .gallery-item {
    width: 200px;
    border: 1px solid #ddd;
    border-radius: 4px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .gallery-item:hover {
    transform: scale(1.03);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .gallery-item img {
    width: 100%;
    height: 150px;
    object-fit: cover;
    display: block;
  }
  
  .image-name {
    padding: 8px;
    font-size: 12px;
    background-color: #f9f9f9;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .no-images {
    width: 100%;
    padding: 50px 0;
    text-align: center;
    color: #999;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }
  
  .full-image-overlay {
    background-color: rgba(0, 0, 0, 0.8);
    z-index: 1100;
  }
  
  .full-image-container {
    position: relative;
    max-width: 90%;
    max-height: 90%;
  }
  
  .full-image-container img {
    max-width: 100%;
    max-height: 90vh;
    display: block;
  }
  
  .full-image-container .close-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    width: 30px;
    height: 30px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  </style>
  
  