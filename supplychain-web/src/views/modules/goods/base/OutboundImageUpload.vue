<template>
    <div class="outbound-image-upload-container">
      <div class="page-header">
        <h1>出库图片上传</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/goods/base/outbound-image" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> 返回
          </router-link>
        </div>
      </div>
      
      <div class="operation-tabs">
        <router-link 
          :to="{ name: 'OutboundImageUpload' }" 
          custom
          v-slot="{ isActive }"
        >
          <div class="operation-tab active">
            资料上传
          </div>
        </router-link>
        <router-link 
          :to="{ name: 'OutboundImageManage' }" 
          custom
          v-slot="{ navigate, isActive }"
        >
          <div 
            class="operation-tab" 
            :class="{ active: isActive }"
            @click="navigate"
          >
            资料管理
          </div>
        </router-link>
      </div>
      
      <div class="upload-content">
        <div class="upload-form">
          <div class="form-section">
            <h2 class="section-title">基本信息</h2>
            <div class="form-row">
              <div class="form-group">
                <label for="outboundId">出库单号</label>
                <div class="input-with-button">
                  <input type="text" id="outboundId" v-model="formData.outboundId" placeholder="输入或选择出库单号">
                  <button class="btn btn-secondary" @click="showOutboundSelector = true">
                    <i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
              <div class="form-group">
                <label for="outboundDate">出库日期</label>
                <input type="date" id="outboundDate" v-model="formData.outboundDate">
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="customer">客户</label>
                <input type="text" id="customer" v-model="formData.customer" placeholder="客户名称" readonly>
              </div>
              <div class="form-group">
                <label for="goodsName">货物名称</label>
                <input type="text" id="goodsName" v-model="formData.goodsName" placeholder="货物名称" readonly>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="quantity">数量(吨)</label>
                <input type="number" id="quantity" v-model="formData.quantity" placeholder="数量" readonly>
              </div>
              <div class="form-group">
                <label for="warehouseLocation">仓库位置</label>
                <input type="text" id="warehouseLocation" v-model="formData.warehouseLocation" placeholder="仓库位置" readonly>
              </div>
            </div>
          </div>
          
          <div class="form-section">
            <h2 class="section-title">图片上传</h2>
            <div class="image-upload-area">
              <div class="upload-categories">
                <div 
                  v-for="(category, index) in imageCategories" 
                  :key="index"
                  :class="['category-item', selectedCategory === index ? 'active' : '']"
                  @click="selectedCategory = index"
                >
                  <div class="category-name">{{ category.name }}</div>
                  <div class="category-count">{{ getUploadedCount(index) }}/{{ category.required ? '必传' : '选传' }}</div>
                </div>
              </div>
              
              <div class="upload-preview">
                <div class="category-info">
                  <h3>{{ imageCategories[selectedCategory].name }}</h3>
                  <p>{{ imageCategories[selectedCategory].description }}</p>
                  <p class="requirement">
                    {{ imageCategories[selectedCategory].required ? '必传' : '选传' }} · 
                    已上传 {{ getUploadedCount(selectedCategory) }} 张
                  </p>
                </div>
                
                <div class="image-list">
                  <div 
                    v-for="(image, imgIndex) in formData.images[selectedCategory]" 
                    :key="imgIndex"
                    class="image-item"
                  >
                    <div class="image-preview">
                      <img :src="image.url" alt="预览图">
                    </div>
                    <div class="image-actions">
                      <button class="btn btn-sm btn-info" @click="previewImage(image)">
                        <i class="fas fa-eye"></i>
                      </button>
                      <button class="btn btn-sm btn-danger" @click="removeImage(selectedCategory, imgIndex)">
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </div>
                  
                  <div class="image-upload-button" @click="triggerFileInput">
                    <i class="fas fa-plus"></i>
                    <span>上传图片</span>
                    <input 
                      type="file" 
                      ref="fileInput" 
                      accept="image/*" 
                      multiple 
                      @change="handleFileUpload" 
                      style="display: none"
                    >
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="form-section">
            <h2 class="section-title">备注信息</h2>
            <div class="form-row">
              <div class="form-group full-width">
                <label for="remarks">备注</label>
                <textarea id="remarks" v-model="formData.remarks" placeholder="请输入备注信息"></textarea>
              </div>
            </div>
          </div>
          
          <div class="form-actions">
            <button class="btn btn-secondary" @click="resetForm">重置</button>
            <button class="btn btn-primary" @click="submitForm" :disabled="isSubmitting">
              {{ isSubmitting ? '提交中...' : '提交' }}
            </button>
          </div>
        </div>
      </div>
      
      <!-- 出库单选择弹窗 -->
      <div v-if="showOutboundSelector" class="modal-overlay" @click="showOutboundSelector = false">
        <div class="modal-container" @click.stop>
          <div class="modal-header">
            <h2>选择出库单</h2>
            <button class="close-btn" @click="showOutboundSelector = false">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="search-bar">
              <input type="text" v-model="outboundSearch" placeholder="搜索出库单号或客户名称">
              <button class="btn btn-primary" @click="searchOutbound">
                <i class="fas fa-search"></i> 搜索
              </button>
            </div>
            
            <table class="data-table">
              <thead>
                <tr>
                  <th>出库单号</th>
                  <th>出库日期</th>
                  <th>客户</th>
                  <th>货物名称</th>
                  <th>数量(吨)</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in filteredOutbounds" :key="item.id">
                  <td>{{ item.outboundId }}</td>
                  <td>{{ item.outboundDate }}</td>
                  <td>{{ item.customer }}</td>
                  <td>{{ item.goodsName }}</td>
                  <td class="number-cell">{{ item.quantity }}</td>
                  <td>
                    <button class="btn btn-sm btn-primary" @click="selectOutbound(item)">
                      选择
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
            
            <div v-if="filteredOutbounds.length === 0" class="no-data">
              <i class="fas fa-search fa-3x"></i>
              <p>没有找到匹配的出库单</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 图片预览弹窗 -->
      <div v-if="showImagePreview" class="modal-overlay" @click="showImagePreview = false">
        <div class="image-preview-container" @click.stop>
          <button class="close-btn" @click="showImagePreview = false">
            <i class="fas fa-times"></i>
          </button>
          <img :src="previewImageUrl" alt="图片预览">
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'OutboundImageUpload',
    data() {
      return {
        formData: {
          outboundId: '',
          outboundDate: '',
          customer: '',
          goodsName: '',
          quantity: '',
          warehouseLocation: '',
          remarks: '',
          images: [[], [], [], []]
        },
        imageCategories: [
          {
            name: '货物装车图',
            description: '记录货物装车过程的图片，需清晰显示货物和车辆信息',
            required: true
          },
          {
            name: '车辆全景图',
            description: '记录装载货物的车辆全景，需清晰显示车牌号',
            required: true
          },
          {
            name: '过磅单图',
            description: '记录出库过磅单据，需清晰显示重量和日期',
            required: true
          },
          {
            name: '其他图片',
            description: '其他与出库相关的图片资料',
            required: false
          }
        ],
        selectedCategory: 0,
        isSubmitting: false,
        showOutboundSelector: false,
        outboundSearch: '',
        outbounds: [
          {
            id: 1,
            outboundId: 'OB20230501001',
            outboundDate: '2023-05-01',
            customer: '客户A',
            goodsName: '小麦',
            quantity: 30,
            warehouseLocation: 'A区-01-01'
          },
          {
            id: 2,
            outboundId: 'OB20230502002',
            outboundDate: '2023-05-02',
            customer: '客户B',
            goodsName: '玉米',
            quantity: 25,
            warehouseLocation: 'B区-02-03'
          },
          {
            id: 3,
            outboundId: 'OB20230503003',
            outboundDate: '2023-05-03',
            customer: '客户C',
            goodsName: '大豆',
            quantity: 20,
            warehouseLocation: 'C区-03-02'
          },
          {
            id: 4,
            outboundId: 'OB20230504004',
            outboundDate: '2023-05-04',
            customer: '客户A',
            goodsName: '稻谷',
            quantity: 35,
            warehouseLocation: 'A区-02-04'
          },
          {
            id: 5,
            outboundId: 'OB20230505005',
            outboundDate: '2023-05-05',
            customer: '客户D',
            goodsName: '高粱',
            quantity: 15,
            warehouseLocation: 'D区-01-05'
          }
        ],
        showImagePreview: false,
        previewImageUrl: ''
      }
    },
    computed: {
      filteredOutbounds() {
        if (!this.outboundSearch) return this.outbounds
        
        const search = this.outboundSearch.toLowerCase()
        return this.outbounds.filter(item => 
          item.outboundId.toLowerCase().includes(search) || 
          item.customer.toLowerCase().includes(search)
        )
      }
    },
    methods: {
      getUploadedCount(categoryIndex) {
        return this.formData.images[categoryIndex].length
      },
      triggerFileInput() {
        this.$refs.fileInput.click()
      },
      handleFileUpload(event) {
        const files = event.target.files
        if (!files.length) return
        
        Array.from(files).forEach(file => {
          if (!file.type.startsWith('image/')) return
          
          const reader = new FileReader()
          reader.onload = e => {
            this.formData.images[this.selectedCategory].push({
              name: file.name,
              url: e.target.result,
              file: file
            })
          }
          reader.readAsDataURL(file)
        })
        
        // 重置文件输入，以便可以再次选择相同的文件
        event.target.value = null
      },
      removeImage(categoryIndex, imageIndex) {
        this.formData.images[categoryIndex].splice(imageIndex, 1)
      },
      previewImage(image) {
        this.previewImageUrl = image.url
        this.showImagePreview = true
      },
      searchOutbound() {
        // 搜索功能已通过计算属性实现
      },
      selectOutbound(outbound) {
        this.formData.outboundId = outbound.outboundId
        this.formData.outboundDate = outbound.outboundDate
        this.formData.customer = outbound.customer
        this.formData.goodsName = outbound.goodsName
        this.formData.quantity = outbound.quantity
        this.formData.warehouseLocation = outbound.warehouseLocation
        
        this.showOutboundSelector = false
      },
      resetForm() {
        this.formData = {
          outboundId: '',
          outboundDate: '',
          customer: '',
          goodsName: '',
          quantity: '',
          warehouseLocation: '',
          remarks: '',
          images: [[], [], [], []]
        }
        this.selectedCategory = 0
      },
      validateForm() {
        if (!this.formData.outboundId) {
          alert('请选择出库单号')
          return false
        }
        
        if (!this.formData.outboundDate) {
          alert('请选择出库日期')
          return false
        }
        
        // 检查必传图片类别是否有上传
        for (let i = 0; i < this.imageCategories.length; i++) {
          if (this.imageCategories[i].required && this.formData.images[i].length === 0) {
            alert(`请上传${this.imageCategories[i].name}`)
            this.selectedCategory = i
            return false
          }
        }
        
        return true
      },
      submitForm() {
        if (!this.validateForm()) return
        
        this.isSubmitting = true
        
        // 模拟API请求
        setTimeout(() => {
          this.isSubmitting = false
          alert('出库图片上传成功！')
          this.resetForm()
        }, 1500)
      }
    }
  }
  </script>
  
  <style scoped>
  .outbound-image-upload-container {
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
  
  .upload-content {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  
  .upload-form {
    max-width: 1000px;
    margin: 0 auto;
  }
  
  .form-section {
    margin-bottom: 30px;
  }
  
  .section-title {
    font-size: 18px;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 1px solid #eee;
  }
  
  .form-row {
    display: flex;
    gap: 20px;
    margin-bottom: 15px;
  }
  
  .form-group {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
  
  .form-group.full-width {
    flex: 2;
  }
  
  .form-group label {
    margin-bottom: 5px;
    font-size: 14px;
  }
  
  .form-group input,
  .form-group select,
  .form-group textarea {
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
  }
  
  .form-group textarea {
    min-height: 100px;
    resize: vertical;
  }
  
  .input-with-button {
    display: flex;
    gap: 10px;
  }
  
  .input-with-button input {
    flex: 1;
  }
  
  .image-upload-area {
    display: flex;
    gap: 20px;
    margin-top: 15px;
  }
  
  .upload-categories {
    width: 200px;
    border-right: 1px solid #eee;
  }
  
  .category-item {
    padding: 12px 15px;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s;
    margin-bottom: 10px;
  }
  
  .category-item:hover {
    background-color: #f5f5f5;
  }
  
  .category-item.active {
    background-color: #e3f2fd;
    border-left: 3px solid #2196f3;
  }
  
  .category-name {
    font-weight: 500;
    margin-bottom: 5px;
  }
  
  .category-count {
    font-size: 12px;
    color: #757575;
  }
  
  .upload-preview {
    flex: 1;
  }
  
  .category-info {
    margin-bottom: 15px;
  }
  
  .category-info h3 {
    font-size: 16px;
    margin-bottom: 5px;
  }
  
  .category-info p {
    font-size: 14px;
    color: #666;
    margin-bottom: 5px;
  }
  
  .requirement {
    font-size: 12px;
    color: #757575;
  }
  
  .image-list {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
  }
  
  .image-item {
    width: 150px;
    border: 1px solid #ddd;
    border-radius: 4px;
    overflow: hidden;
  }
  
  .image-preview {
    height: 150px;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f5f5f5;
  }
  
  .image-preview img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
  }
  
  .image-actions {
    display: flex;
    justify-content: space-around;
    padding: 8px;
    background-color: #f9f9f9;
  }
  
  .image-upload-button {
    width: 150px;
    height: 150px;
    border: 2px dashed #ddd;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .image-upload-button:hover {
    border-color: #2196f3;
    background-color: #f5f5f5;
  }
  
  .image-upload-button i {
    font-size: 24px;
    margin-bottom: 10px;
    color: #757575;
  }
  
  .form-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 30px;
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
  
  .search-bar {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
  }
  
  .search-bar input {
    flex: 1;
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
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
  
  .number-cell {
    text-align: right;
    font-family: monospace;
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
  
  .image-preview-container {
    position: relative;
    max-width: 90%;
    max-height: 90%;
    background-color: #fff;
    border-radius: 4px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .image-preview-container img {
    display: block;
    max-width: 100%;
    max-height: 90vh;
    margin: 0 auto;
  }
  
  .image-preview-container .close-btn {
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
  
  