<template>
    <div class="factory-quality-upload-container">
      <div class="page-header">
        <h1>上传到货厂家质检单</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/goods/non-base/factory-quality" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> 返回
          </router-link>
        </div>
      </div>
  
      <div class="upload-section">
        <div class="upload-methods">
          <div class="method-tabs">
            <button 
              :class="['tab-btn', activeTab === 'file' ? 'active' : '']" 
              @click="activeTab = 'file'"
            >
              <i class="fas fa-file-excel"></i> 文件上传
            </button>
            <button 
              :class="['tab-btn', activeTab === 'manual' ? 'active' : '']" 
              @click="activeTab = 'manual'"
            >
              <i class="fas fa-keyboard"></i> 手动录入
            </button>
          </div>
  
          <div class="tab-content">
            <!-- 文件上传 -->
            <div v-if="activeTab === 'file'" class="file-upload-tab">
              <div class="upload-instructions">
                <h3>文件上传说明</h3>
                <p>请上传Excel格式的质检单数据文件，文件大小不超过10MB。</p>
                <p>您可以下载模板文件，按照模板格式填写后上传。</p>
                <a href="#" class="template-link">
                  <i class="fas fa-download"></i> 下载模板文件
                </a>
              </div>
  
              <div class="upload-area">
                <div class="upload-dropzone" @click="triggerFileInput" @drop.prevent="handleFileDrop" @dragover.prevent>
                  <i class="fas fa-cloud-upload-alt fa-3x"></i>
                  <p>点击或拖拽文件到此处上传</p>
                  <p class="file-types">支持的文件类型: .xlsx, .xls</p>
                </div>
                <input 
                  type="file" 
                  ref="fileInput" 
                  style="display: none" 
                  accept=".xlsx,.xls" 
                  @change="handleFileSelect"
                >
                <div v-if="selectedFile" class="selected-file">
                  <i class="fas fa-file-excel"></i>
                  <span>{{ selectedFile.name }}</span>
                  <button class="remove-file-btn" @click.stop="removeFile">
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </div>
            </div>
  
            <!-- 手动录入 -->
            <div v-if="activeTab === 'manual'" class="manual-entry-tab">
              <form @submit.prevent="submitManualEntry">
                <div class="form-row">
                  <div class="form-group">
                    <label for="inspectionId">质检单号</label>
                    <input type="text" id="inspectionId" v-model="manualEntry.inspectionId" required>
                  </div>
                  <div class="form-group">
                    <label for="inspectionDate">检验日期</label>
                    <input type="date" id="inspectionDate" v-model="manualEntry.inspectionDate" required>
                  </div>
                </div>
  
                <div class="form-row">
                  <div class="form-group">
                    <label for="supplier">供应商</label>
                    <select id="supplier" v-model="manualEntry.supplier" required>
                      <option value="">请选择供应商</option>
                      <option value="供应商A">供应商A</option>
                      <option value="供应商B">供应商B</option>
                      <option value="供应商C">供应商C</option>
                      <option value="供应商D">供应商D</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label for="goodsName">货物名称</label>
                    <input type="text" id="goodsName" v-model="manualEntry.goodsName" required>
                  </div>
                </div>
  
                <div class="form-row">
                  <div class="form-group">
                    <label for="batchNumber">批次号</label>
                    <input type="text" id="batchNumber" v-model="manualEntry.batchNumber" required>
                  </div>
                  <div class="form-group">
                    <label for="quantity">数量</label>
                    <input type="number" id="quantity" v-model="manualEntry.quantity" min="0" required>
                  </div>
                </div>
  
                <div class="form-row">
                  <div class="form-group">
                    <label for="inspector">检验员</label>
                    <input type="text" id="inspector" v-model="manualEntry.inspector" required>
                  </div>
                  <div class="form-group">
                    <label for="result">检验结果</label>
                    <select id="result" v-model="manualEntry.result" required>
                      <option value="">请选择检验结果</option>
                      <option value="合格">合格</option>
                      <option value="不合格">不合格</option>
                      <option value="部分合格">部分合格</option>
                    </select>
                  </div>
                </div>
  
                <div class="form-group full-width">
                  <label for="remarks">备注</label>
                  <textarea id="remarks" v-model="manualEntry.remarks" rows="3"></textarea>
                </div>
  
                <div class="quality-parameters">
                  <h3>质量参数</h3>
                  <div class="parameters-table">
                    <div class="parameter-row header">
                      <div class="parameter-cell">参数名称</div>
                      <div class="parameter-cell">标准值</div>
                      <div class="parameter-cell">实测值</div>
                      <div class="parameter-cell">是否合格</div>
                      <div class="parameter-cell">操作</div>
                    </div>
                    <div v-for="(param, index) in manualEntry.parameters" :key="index" class="parameter-row">
                      <div class="parameter-cell">
                        <input type="text" v-model="param.name" placeholder="参数名称">
                      </div>
                      <div class="parameter-cell">
                        <input type="text" v-model="param.standard" placeholder="标准值">
                      </div>
                      <div class="parameter-cell">
                        <input type="text" v-model="param.actual" placeholder="实测值">
                      </div>
                      <div class="parameter-cell">
                        <select v-model="param.passed">
                          <option value="true">合格</option>
                          <option value="false">不合格</option>
                        </select>
                      </div>
                      <div class="parameter-cell">
                        <button type="button" class="remove-param-btn" @click="removeParameter(index)">
                          <i class="fas fa-trash"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                  <button type="button" class="add-param-btn" @click="addParameter">
                    <i class="fas fa-plus"></i> 添加参数
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
  
      <div class="action-footer">
        <button class="btn btn-secondary" @click="cancel">取消</button>
        <button class="btn btn-primary" @click="uploadData" :disabled="!canUpload">
          <i class="fas fa-upload"></i> 上传数据
        </button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'FactoryQualityUpload',
    data() {
      return {
        activeTab: 'file',
        selectedFile: null,
        manualEntry: {
          inspectionId: '',
          inspectionDate: '',
          supplier: '',
          goodsName: '',
          batchNumber: '',
          quantity: '',
          inspector: '',
          result: '',
          remarks: '',
          parameters: [
            { name: '', standard: '', actual: '', passed: 'true' }
          ]
        }
      }
    },
    computed: {
      canUpload() {
        if (this.activeTab === 'file') {
          return this.selectedFile !== null
        } else {
          return this.manualEntry.inspectionId && 
                 this.manualEntry.inspectionDate && 
                 this.manualEntry.supplier && 
                 this.manualEntry.goodsName
        }
      }
    },
    methods: {
      triggerFileInput() {
        this.$refs.fileInput.click()
      },
      handleFileSelect(event) {
        const files = event.target.files
        if (files.length > 0) {
          this.selectedFile = files[0]
        }
      },
      handleFileDrop(event) {
        const files = event.dataTransfer.files
        if (files.length > 0) {
          const file = files[0]
          if (file.name.endsWith('.xlsx') || file.name.endsWith('.xls')) {
            this.selectedFile = file
          } else {
            alert('请上传Excel格式的文件(.xlsx, .xls)')
          }
        }
      },
      removeFile() {
        this.selectedFile = null
        this.$refs.fileInput.value = ''
      },
      addParameter() {
        this.manualEntry.parameters.push({ name: '', standard: '', actual: '', passed: 'true' })
      },
      removeParameter(index) {
        this.manualEntry.parameters.splice(index, 1)
      },
      submitManualEntry() {
        // 处理手动录入表单提交
        console.log('提交手动录入数据:', this.manualEntry)
        // 这里可以添加提交到后端的逻辑
      },
      uploadData() {
        if (this.activeTab === 'file') {
          // 处理文件上传
          console.log('上传文件:', this.selectedFile)
          // 这里可以添加文件上传到后端的逻辑
          alert('文件上传成功！')
        } else {
          // 处理手动录入数据上传
          this.submitManualEntry()
          alert('数据提交成功！')
        }
        // 上传成功后返回列表页
        this.$router.push('/dashboard/goods/non-base/factory-quality')
      },
      cancel() {
        // 取消操作，返回列表页
        this.$router.push('/dashboard/goods/non-base/factory-quality')
      }
    }
  }
  </script>
  
  <style scoped>
  .factory-quality-upload-container {
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
  
  .btn-primary {
    background-color: #4caf50;
    color: white;
  }
  
  .btn-secondary {
    background-color: #2196f3;
    color: white;
  }
  
  .btn:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .upload-section {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
  }
  
  .method-tabs {
    display: flex;
    border-bottom: 1px solid #eee;
  }
  
  .tab-btn {
    padding: 12px 20px;
    background: none;
    border: none;
    cursor: pointer;
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .tab-btn.active {
    border-bottom: 2px solid #2196f3;
    color: #2196f3;
    font-weight: bold;
  }
  
  .tab-content {
    padding: 20px;
  }
  
  .upload-instructions {
    margin-bottom: 20px;
  }
  
  .upload-instructions h3 {
    font-size: 16px;
    margin-bottom: 10px;
  }
  
  .template-link {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    color: #2196f3;
    text-decoration: none;
    margin-top: 10px;
  }
  
  .upload-area {
    margin-top: 20px;
  }
  
  .upload-dropzone {
    border: 2px dashed #ccc;
    border-radius: 4px;
    padding: 30px;
    text-align: center;
    cursor: pointer;
    transition: border-color 0.3s;
  }
  
  .upload-dropzone:hover {
    border-color: #2196f3;
  }
  
  .file-types {
    font-size: 12px;
    color: #999;
    margin-top: 5px;
  }
  
  .selected-file {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-top: 15px;
    padding: 10px;
    background-color: #f5f5f5;
    border-radius: 4px;
  }
  
  .remove-file-btn {
    background: none;
    border: none;
    color: #f44336;
    cursor: pointer;
    margin-left: auto;
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
    width: 100%;
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
  
  .quality-parameters {
    margin-top: 20px;
  }
  
  .quality-parameters h3 {
    font-size: 16px;
    margin-bottom: 10px;
  }
  
  .parameters-table {
    border: 1px solid #eee;
    border-radius: 4px;
    overflow: hidden;
    margin-bottom: 15px;
  }
  
  .parameter-row {
    display: flex;
  }
  
  .parameter-row.header {
    background-color: #f5f5f5;
    font-weight: bold;
  }
  
  .parameter-cell {
    flex: 1;
    padding: 10px;
    border-bottom: 1px solid #eee;
    display: flex;
    align-items: center;
  }
  
  .parameter-cell input,
  .parameter-cell select {
    width: 100%;
    padding: 6px 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .remove-param-btn {
    background: none;
    border: none;
    color: #f44336;
    cursor: pointer;
  }
  
  .add-param-btn {
    background: none;
    border: 1px solid #2196f3;
    color: #2196f3;
    padding: 6px 12px;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 5px;
  }
  
  .action-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #eee;
  }
  </style>
  
  