<template>
    <div class="factory-image-upload-container">
      <div class="page-header">
        <h1>上传货物进厂影像</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/goods/non-base/factory-image" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> 返回
          </router-link>
        </div>
      </div>
  
      <div class="upload-section">
        <div class="upload-form">
          <div class="form-row">
            <div class="form-group">
              <label for="title">标题</label>
              <input type="text" id="title" v-model="formData.title" placeholder="请输入影像标题" required>
            </div>
            <div class="form-group">
              <label for="date">拍摄日期</label>
              <input type="date" id="date" v-model="formData.date" required>
            </div>
          </div>
  
          <div class="form-row">
            <div class="form-group">
              <label for="supplier">供应商</label>
              <select id="supplier" v-model="formData.supplier" required>
                <option value="">请选择供应商</option>
                <option value="供应商A">供应商A</option>
                <option value="供应商B">供应商B</option>
                <option value="供应商C">供应商C</option>
                <option value="供应商D">供应商D</option>
                <option value="供应商E">供应商E</option>
              </select>
            </div>
            <div class="form-group">
              <label for="goodsName">货物名称</label>
              <input type="text" id="goodsName" v-model="formData.goodsName" placeholder="请输入货物名称" required>
            </div>
          </div>
  
          <div class="form-row">
            <div class="form-group">
              <label for="batchNumber">批次号</label>
              <input type="text" id="batchNumber" v-model="formData.batchNumber" placeholder="请输入批次号" required>
            </div>
            <div class="form-group">
              <label for="type">影像类型</label>
              <select id="type" v-model="formData.type" required>
                <option value="image">图片</option>
                <option value="video">视频</option>
              </select>
            </div>
          </div>
  
          <div class="form-group full-width">
            <label for="description">描述</label>
            <textarea id="description" v-model="formData.description" rows="3" placeholder="请输入影像描述"></textarea>
          </div>
  
          <div class="file-upload-section">
            <div class="upload-dropzone" @click="triggerFileInput" @drop.prevent="handleFileDrop" @dragover.prevent>
              <div v-if="!previewUrl">
                <i class="fas fa-cloud-upload-alt fa-3x"></i>
                <p>点击或拖拽文件到此处上传</p>
                <p class="file-types">
                  支持的文件类型: {{ formData.type === 'image' ? '.jpg, .jpeg, .png, .gif' : '.mp4, .mov, .avi' }}
                </p>
              </div>
              <div v-else class="preview-container">
                <img v-if="formData.type === 'image'" :src="previewUrl" alt="预览图">
                <video v-else controls>
                  <source :src="previewUrl" :type="selectedFile.type">
                  您的浏览器不支持视频预览
                </video>
                <button class="remove-preview-btn" @click.stop="removeFile">
                  <i class="fas fa-times"></i>
                </button>
              </div>
            </div>
            <input 
              type="file" 
              ref="fileInput" 
              style="display: none" 
              :accept="formData.type === 'image' ? 'image/*' : 'video/*'" 
              @change="handleFileSelect"
            >
          </div>
  
          <div class="related-data-section">
            <h3>关联业务数据</h3>
            <div class="form-row">
              <div class="form-group">
                <label for="relatedContract">关联合同</label>
                <select id="relatedContract" v-model="formData.relatedContract">
                  <option value="">请选择关联合同</option>
                  <option value="CT20230501001">CT20230501001</option>
                  <option value="CT20230502002">CT20230502002</option>
                  <option value="CT20230503003">CT20230503003</option>
                </select>
              </div>
              <div class="form-group">
                <label for="relatedWeight">关联重量单</label>
                <select id="relatedWeight" v-model="formData.relatedWeight">
                  <option value="">请选择关联重量单</option>
                  <option value="WT20230501001">WT20230501001</option>
                  <option value="WT20230502002">WT20230502002</option>
                  <option value="WT20230503003">WT20230503003</option>
                </select>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="relatedQuality">关联质检单</label>
                <select id="relatedQuality" v-model="formData.relatedQuality">
                  <option value="">请选择关联质检单</option>
                  <option value="QC20230501001">QC20230501001</option>
                  <option value="QC20230502002">QC20230502002</option>
                  <option value="QC20230503003">QC20230503003</option>
                </select>
              </div>
              <div class="form-group">
                <label for="relatedWaybill">关联运单</label>
                <select id="relatedWaybill" v-model="formData.relatedWaybill">
                  <option value="">请选择关联运单</option>
                  <option value="WB20230501001">WB20230501001</option>
                  <option value="WB20230502002">WB20230502002</option>
                  <option value="WB20230503003">WB20230503003</option>
                </select>
              </div>
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
    name: 'FactoryImageUpload',
    data() {
      return {
        formData: {
          title: '',
          date: '',
          supplier: '',
          goodsName: '',
          batchNumber: '',
          type: 'image',
          description: '',
          relatedContract: '',
          relatedWeight: '',
          relatedQuality: '',
          relatedWaybill: ''
        },
        selectedFile: null,
        previewUrl: null
      }
    },
    computed: {
      canUpload() {
        return this.formData.title && 
               this.formData.date && 
               this.formData.supplier && 
               this.formData.goodsName && 
               this.formData.batchNumber && 
               this.selectedFile
      }
    },
    methods: {
      triggerFileInput() {
        this.$refs.fileInput.click()
      },
      handleFileSelect(event) {
        const files = event.target.files
        if (files.length > 0) {
          this.processFile(files[0])
        }
      },
      handleFileDrop(event) {
        const files = event.dataTransfer.files
        if (files.length > 0) {
          const file = files[0]
          const isImage = file.type.startsWith('image/')
          const isVideo = file.type.startsWith('video/')
          
          if ((this.formData.type === 'image' && isImage) || (this.formData.type === 'video' && isVideo)) {
            this.processFile(file)
          } else {
            alert(`请上传${this.formData.type === 'image' ? '图片' : '视频'}格式的文件`)
          }
        }
      },
      processFile(file) {
        this.selectedFile = file
        
        // 创建预览URL
        if (this.previewUrl) {
          URL.revokeObjectURL(this.previewUrl)
        }
        this.previewUrl = URL.createObjectURL(file)
      },
      removeFile() {
        if (this.previewUrl) {
          URL.revokeObjectURL(this.previewUrl)
        }
        this.selectedFile = null
        this.previewUrl = null
        this.$refs.fileInput.value = ''
      },
      uploadData() {
        // 这里可以添加文件上传到后端的逻辑
        console.log('上传数据:', this.formData)
        console.log('文件:', this.selectedFile)
        
        // 模拟上传成功
        alert('影像上传成功！')
        this.$router.push('/dashboard/goods/non-base/factory-image')
      },
      cancel() {
        // 取消操作，返回列表页
        this.$router.push('/dashboard/goods/non-base/factory-image')
      }
    },
    beforeUnmount() {
      // 清理预览URL
      if (this.previewUrl) {
        URL.revokeObjectURL(this.previewUrl)
      }
    }
  }
  </script>
  
  <style scoped>
  .factory-image-upload-container {
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
    padding: 20px;
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
  
  .file-upload-section {
    margin-top: 20px;
    margin-bottom: 20px;
  }
  
  .upload-dropzone {
    border: 2px dashed #ccc;
    border-radius: 4px;
    padding: 30px;
    text-align: center;
    cursor: pointer;
    transition: border-color 0.3s;
    min-height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .upload-dropzone:hover {
    border-color: #2196f3;
  }
  
  .file-types {
    font-size: 12px;
    color: #999;
    margin-top: 5px;
  }
  
  .preview-container {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
  }
  
  .preview-container img,
  .preview-container video {
    max-width: 100%;
    max-height: 300px;
    object-fit: contain;
  }
  
  .remove-preview-btn {
    position: absolute;
    top: -10px;
    right: -10px;
    background-color: #f44336;
    color: white;
    border: none;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }
  
  .related-data-section {
    margin-top: 20px;
    border-top: 1px solid #eee;
    padding-top: 20px;
  }
  
  .related-data-section h3 {
    font-size: 16px;
    margin-bottom: 15px;
  }
  
  .action-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 20px;
  }
  </style>
  
  