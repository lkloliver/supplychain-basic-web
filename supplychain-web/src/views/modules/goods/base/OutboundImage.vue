<template>
    <div class="outbound-image">
      <h2>货物出库影像</h2>
      
      <div class="module-description">
        <p>本模块用于管理货物出库时的影像资料，包括货物照片、视频等，以便于后续查询和追溯。</p>
      </div>
      
      <div class="action-buttons">
        <router-link to="/dashboard/goods/base/outbound-image/upload" class="btn-upload">
          上传影像
        </router-link>
        <router-link to="/dashboard/goods/base/outbound-image/manage" class="btn-manage">
          管理影像
        </router-link>
      </div>
      
      <div class="recent-uploads">
        <h3>最近上传</h3>
        <div class="image-grid">
          <div v-for="(image, index) in recentImages" :key="index" class="image-card">
            <div class="image-container">
              <img :src="image.url" :alt="image.description" />
              <div class="image-overlay">
                <button class="btn-view" @click="viewImage(image)">查看</button>
              </div>
            </div>
            <div class="image-info">
              <p class="image-title">{{ image.title }}</p>
              <p class="image-date">上传时间: {{ image.uploadDate }}</p>
            </div>
          </div>
          <div v-if="recentImages.length === 0" class="no-data">
            暂无最近上传的影像
          </div>
        </div>
      </div>
      
      <!-- 图片查看器 -->
      <div class="image-viewer" v-if="showViewer">
        <div class="viewer-content">
          <div class="viewer-header">
            <h3>{{ currentImage.title }}</h3>
            <button class="btn-close" @click="closeViewer">×</button>
          </div>
          <div class="viewer-body">
            <img :src="currentImage.url" :alt="currentImage.description" />
            <div class="image-details">
              <p><strong>描述:</strong> {{ currentImage.description }}</p>
              <p><strong>上传时间:</strong> {{ currentImage.uploadDate }}</p>
              <p><strong>关联单号:</strong> {{ currentImage.relatedId }}</p>
              <p><strong>上传人员:</strong> {{ currentImage.uploader }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  // 最近上传的影像
  const recentImages = ref([
    {
      id: 1,
      title: '小麦出库照片',
      description: '2023年5月15日小麦出库装车照片',
      url: 'https://via.placeholder.com/300x200?text=小麦出库照片',
      uploadDate: '2023-05-15',
      relatedId: 'OUT-20230515001',
      uploader: '张三'
    },
    {
      id: 2,
      title: '玉米出库照片',
      description: '2023年5月14日玉米出库装车照片',
      url: 'https://via.placeholder.com/300x200?text=玉米出库照片',
      uploadDate: '2023-05-14',
      relatedId: 'OUT-20230514001',
      uploader: '李四'
    },
    {
      id: 3,
      title: '大豆出库照片',
      description: '2023年5月13日大豆出库装车照片',
      url: 'https://via.placeholder.com/300x200?text=大豆出库照片',
      uploadDate: '2023-05-13',
      relatedId: 'OUT-20230513001',
      uploader: '王五'
    },
    {
      id: 4,
      title: '水稻出库照片',
      description: '2023年5月12日水稻出库装车照片',
      url: 'https://via.placeholder.com/300x200?text=水稻出库照片',
      uploadDate: '2023-05-12',
      relatedId: 'OUT-20230512001',
      uploader: '赵六'
    }
  ]);
  
  // 图片查看器
  const showViewer = ref(false);
  const currentImage = ref({});
  
  // 查看图片
  const viewImage = (image) => {
    currentImage.value = image;
    showViewer.value = true;
  };
  
  // 关闭查看器
  const closeViewer = () => {
    showViewer.value = false;
  };
  </script>
  
  <style scoped>
  .outbound-image {
    padding: 20px;
  }
  
  h2 {
    margin-bottom: 20px;
    font-size: 1.5rem;
    color: #333;
  }
  
  .module-description {
    background-color: #f5f5f5;
    padding: 15px;
    border-radius: 5px;
    margin-bottom: 20px;
  }
  
  .module-description p {
    margin: 0;
    color: #666;
  }
  
  .action-buttons {
    display: flex;
    gap: 15px;
    margin-bottom: 30px;
  }
  
  .btn-upload,
  .btn-manage {
    display: inline-block;
    padding: 10px 20px;
    background-color: #1890ff;
    color: white;
    text-decoration: none;
    border-radius: 4px;
    font-weight: 500;
    transition: background-color 0.3s;
  }
  
  .btn-upload:hover,
  .btn-manage:hover {
    background-color: #40a9ff;
  }
  
  .recent-uploads h3 {
    margin-bottom: 15px;
    font-size: 1.2rem;
    color: #333;
  }
  
  .image-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
  }
  
  .image-card {
    border: 1px solid #ddd;
    border-radius: 5px;
    overflow: hidden;
    transition: transform 0.3s, box-shadow 0.3s;
  }
  
  .image-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  }
  
  .image-container {
    position: relative;
    height: 180px;
  }
  
  .image-container img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    opacity: 0;
    transition: opacity 0.3s;
  }
  
  .image-container:hover .image-overlay {
    opacity: 1;
  }
  
  .btn-view {
    padding: 8px 15px;
    background-color: #1890ff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .image-info {
    padding: 10px;
  }
  
  .image-title {
    margin: 0 0 5px;
    font-weight: 500;
  }
  
  .image-date {
    margin: 0;
    color: #666;
    font-size: 0.9rem;
  }
  
  .no-data {
    grid-column: 1 / -1;
    text-align: center;
    padding: 30px;
    color: #999;
    background-color: #f5f5f5;
    border-radius: 5px;
  }
  
  /* 图片查看器 */
  .image-viewer {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.8);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .viewer-content {
    background-color: white;
    border-radius: 5px;
    width: 80%;
    max-width: 900px;
    max-height: 90%;
    overflow: hidden;
    display: flex;
    flex-direction: column;
  }
  
  .viewer-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid #ddd;
  }
  
  .viewer-header h3 {
    margin: 0;
  }
  
  .btn-close {
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
  }
  
  .viewer-body {
    padding: 20px;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .viewer-body img {
    max-width: 100%;
    max-height: 500px;
    object-fit: contain;
    margin: 0 auto;
  }
  
  .image-details {
    background-color: #f5f5f5;
    padding: 15px;
    border-radius: 5px;
  }
  
  .image-details p {
    margin: 5px 0;
  }
  </style>
  
  