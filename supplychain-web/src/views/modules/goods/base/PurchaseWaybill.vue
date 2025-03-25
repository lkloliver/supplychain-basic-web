<template>
    <div class="purchase-waybill">
      <h2>采购运单</h2>
      
      <div class="module-description">
        <p>本模块用于管理采购过程中的运输单据，记录货物从供应商到基地的运输信息。</p>
      </div>
      
      <div class="action-buttons">
        <router-link to="/dashboard/goods/base/purchase-waybill/upload" class="btn-upload">
          上传运单
        </router-link>
        <router-link to="/dashboard/goods/base/purchase-waybill/manage" class="btn-manage">
          管理运单
        </router-link>
      </div>
      
      <div class="statistics-cards">
        <div class="stat-card">
          <div class="stat-icon">📄</div>
          <div class="stat-info">
            <span class="stat-value">128</span>
            <span class="stat-label">本月运单总数</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🚚</div>
          <div class="stat-info">
            <span class="stat-value">45</span>
            <span class="stat-label">本月运输车次</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">⚖️</div>
          <div class="stat-info">
            <span class="stat-value">1,280</span>
            <span class="stat-label">本月运输吨数</span>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">💰</div>
          <div class="stat-info">
            <span class="stat-value">¥25,600</span>
            <span class="stat-label">本月运费总额</span>
          </div>
        </div>
      </div>
      
      <div class="recent-waybills">
        <h3>最近运单</h3>
        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>运单号</th>
                <th>关联合同号</th>
                <th>供应商</th>
                <th>货物名称</th>
                <th>运输日期</th>
                <th>运输数量</th>
                <th>运输状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(waybill, index) in recentWaybills" :key="index">
                <td>{{ waybill.waybillId }}</td>
                <td>{{ waybill.contractId }}</td>
                <td>{{ waybill.supplier }}</td>
                <td>{{ waybill.goodsName }}</td>
                <td>{{ waybill.transportDate }}</td>
                <td>{{ waybill.quantity }} {{ waybill.unit }}</td>
                <td>
                  <span :class="'status-' + waybill.status">{{ getStatusText(waybill.status) }}</span>
                </td>
                <td>
                  <div class="action-buttons">
                    <button class="btn-view" @click="viewWaybill(waybill)">查看</button>
                  </div>
                </td>
              </tr>
              <tr v-if="recentWaybills.length === 0">
                <td colspan="8" class="no-data">暂无最近运单</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      
      <!-- 运单详情弹窗 -->
      <div class="waybill-modal" v-if="showModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>运单详情</h3>
            <button class="btn-close" @click="closeModal">×</button>
          </div>
          <div class="modal-body">
            <div class="waybill-details">
              <div class="detail-group">
                <div class="detail-item">
                  <span class="detail-label">运单号:</span>
                  <span class="detail-value">{{ currentWaybill.waybillId }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">关联合同号:</span>
                  <span class="detail-value">{{ currentWaybill.contractId }}</span>
                </div>
              </div>
              <div class="detail-group">
                <div class="detail-item">
                  <span class="detail-label">供应商:</span>
                  <span class="detail-value">{{ currentWaybill.supplier }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">货物名称:</span>
                  <span class="detail-value">{{ currentWaybill.goodsName }}</span>
                </div>
              </div>
              <div class="detail-group">
                <div class="detail-item">
                  <span class="detail-label">运输日期:</span>
                  <span class="detail-value">{{ currentWaybill.transportDate }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">到达日期:</span>
                  <span class="detail-value">{{ currentWaybill.arrivalDate || '未到达' }}</span>
                </div>
              </div>
              <div class="detail-group">
                <div class="detail-item">
                  <span class="detail-label">运输数量:</span>
                  <span class="detail-value">{{ currentWaybill.quantity }} {{ currentWaybill.unit }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">运输状态:</span>
                  <span class="detail-value" :class="'status-' + currentWaybill.status">
                    {{ getStatusText(currentWaybill.status) }}
                  </span>
                </div>
              </div>
              <div class="detail-group">
                <div class="detail-item">
                  <span class="detail-label">车牌号:</span>
                  <span class="detail-value">{{ currentWaybill.vehicleNumber }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">司机姓名:</span>
                  <span class="detail-value">{{ currentWaybill.driverName }}</span>
                </div>
              </div>
              <div class="detail-group">
                <div class="detail-item">
                  <span class="detail-label">司机电话:</span>
                  <span class="detail-value">{{ currentWaybill.driverPhone }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">运费:</span>
                  <span class="detail-value">¥{{ currentWaybill.transportFee }}</span>
                </div>
              </div>
              <div class="detail-group full-width">
                <div class="detail-item">
                  <span class="detail-label">备注:</span>
                  <span class="detail-value">{{ currentWaybill.remarks || '无' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  // 最近运单数据
  const recentWaybills = ref([
    {
      waybillId: 'PW-20230501001',
      contractId: 'PC-20230401001',
      supplier: '河南农产品有限公司',
      goodsName: '小麦',
      transportDate: '2023-05-01',
      arrivalDate: '2023-05-02',
      quantity: 30,
      unit: '吨',
      status: 'completed',
      vehicleNumber: '豫A12345',
      driverName: '张三',
      driverPhone: '13800138000',
      transportFee: 1500,
      remarks: '货物完好，按时到达'
    },
    {
      waybillId: 'PW-20230502001',
      contractId: 'PC-20230402001',
      supplier: '山东粮油集团',
      goodsName: '玉米',
      transportDate: '2023-05-02',
      arrivalDate: null,
      quantity: 25,
      unit: '吨',
      status: 'in_transit',
      vehicleNumber: '鲁B54321',
      driverName: '李四',
      driverPhone: '13900139000',
      transportFee: 1200,
      remarks: ''
    },
    {
      waybillId: 'PW-20230503001',
      contractId: 'PC-20230403001',
      supplier: '黑龙江大豆专业合作社',
      goodsName: '大豆',
      transportDate: '2023-05-03',
      arrivalDate: '2023-05-04',
      quantity: 20,
      unit: '吨',
      status: 'completed',
      vehicleNumber: '黑C67890',
      driverName: '王五',
      driverPhone: '13700137000',
      transportFee: 1800,
      remarks: '途中遇雨，略有延迟'
    }
  ]);
  
  // 运单详情弹窗
  const showModal = ref(false);
  const currentWaybill = ref({});
  
  // 查看运单详情
  const viewWaybill = (waybill) => {
    currentWaybill.value = waybill;
    showModal.value = true;
  };
  
  // 关闭弹窗
  const closeModal = () => {
    showModal.value = false;
  };
  
  // 获取状态文本
  const getStatusText = (status) => {
    const statusMap = {
      'pending': '待发货',
      'in_transit': '运输中',
      'completed': '已完成',
      'cancelled': '已取消'
    };
    return statusMap[status] || status;
  };
  </script>
  
  <style scoped>
  .purchase-waybill {
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
  
  .statistics-cards {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
  }
  
  .stat-card {
    background-color: white;
    border-radius: 5px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  }
  
  .stat-icon {
    font-size: 2rem;
    margin-right: 15px;
  }
  
  .stat-info {
    display: flex;
    flex-direction: column;
  }
  
  .stat-value {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
  }
  
  .stat-label {
    color: #666;
    font-size: 0.9rem;
  }
  
  .recent-waybills h3 {
    margin-bottom: 15px;
    font-size: 1.2rem;
    color: #333;
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
  
  .status-pending {
    color: #faad14;
  }
  
  .status-in_transit {
    color: #1890ff;
  }
  
  .status-completed {
    color: #52c41a;
  }
  
  .status-cancelled {
    color: #ff4d4f;
  }
  
  .action-buttons {
    display: flex;
    gap: 5px;
  }
  
  .btn-view {
    padding: 5px 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.8rem;
    background-color: #1890ff;
    color: white;
  }
  
  .no-data {
    text-align: center;
    padding: 20px;
    color: #999;
  }
  
  /* 运单详情弹窗 */
  .waybill-modal {
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
    width: 700px;
    max-width: 90%;
    max-height: 90%;
    overflow: hidden;
    display: flex;
    flex-direction: column;
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
    padding: 20px;
    overflow-y: auto;
  }
  
  .waybill-details {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
  }
  
  .detail-group {
    display: flex;
    width: calc(50% - 7.5px);
    gap: 15px;
  }
  
  .detail-group.full-width {
    width: 100%;
  }
  
  .detail-item {
    flex: 1;
    background-color: #f5f5f5;
    padding: 10px;
    border-radius: 4px;
  }
  
  .detail-label {
    display: block;
    font-weight: 500;
    margin-bottom: 5px;
    color: #666;
  }
  
  .detail-value {
    font-weight: 500;
    color: #333;
  }
  
  @media (max-width: 768px) {
    .detail-group {
      width: 100%;
    }
  }
  </style>
  
  