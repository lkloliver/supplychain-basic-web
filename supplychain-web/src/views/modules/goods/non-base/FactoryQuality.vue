<template>
    <div class="factory-quality-container">
      <div class="page-header">
        <h1>到货厂家质检单</h1>
        <div class="action-buttons">
          <router-link to="/dashboard/goods/non-base/factory-quality/upload" class="btn btn-primary">
            <i class="fas fa-upload"></i> 上传质检单
          </router-link>
          <router-link to="/dashboard/goods/non-base/factory-quality/manage" class="btn btn-secondary">
            <i class="fas fa-cog"></i> 管理质检单
          </router-link>
        </div>
      </div>
  
      <div class="content-section">
        <div class="info-card">
          <div class="card-header">
            <i class="fas fa-info-circle"></i> 模块说明
          </div>
          <div class="card-body">
            <p>到货厂家质检单模块用于记录和管理货物到达厂家后的质量检验信息。</p>
            <p>您可以通过此模块：</p>
            <ul>
              <li>上传厂家质检单数据</li>
              <li>查看和管理已上传的质检单</li>
              <li>导出质检数据进行分析</li>
            </ul>
          </div>
        </div>
  
        <div class="stats-section">
          <div class="stat-card">
            <div class="stat-value">{{ recentUploads }}</div>
            <div class="stat-label">近期上传</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ totalRecords }}</div>
            <div class="stat-label">总记录数</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ passRate }}%</div>
            <div class="stat-label">合格率</div>
          </div>
        </div>
      </div>
  
      <div class="recent-uploads">
        <h2>最近上传的质检单</h2>
        <div v-if="recentRecords.length > 0">
          <table class="data-table">
            <thead>
              <tr>
                <th>质检单号</th>
                <th>供应商</th>
                <th>货物名称</th>
                <th>检验日期</th>
                <th>检验结果</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="record in recentRecords" :key="record.id">
                <td>{{ record.inspectionId }}</td>
                <td>{{ record.supplier }}</td>
                <td>{{ record.goodsName }}</td>
                <td>{{ record.inspectionDate }}</td>
                <td :class="record.result === '合格' ? 'text-success' : 'text-danger'">
                  {{ record.result }}
                </td>
                <td>
                  <button class="btn btn-sm btn-info">
                    <i class="fas fa-eye"></i> 查看
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else class="no-data">
          <i class="fas fa-file-alt fa-3x"></i>
          <p>暂无最近上传的质检单数据</p>
          <router-link to="/dashboard/goods/non-base/factory-quality/upload" class="btn btn-primary">
            立即上传
          </router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'FactoryQuality',
    data() {
      return {
        recentUploads: 12,
        totalRecords: 156,
        passRate: 92,
        recentRecords: [
          {
            id: 1,
            inspectionId: 'QC20230501001',
            supplier: '供应商A',
            goodsName: '原材料X',
            inspectionDate: '2023-05-01',
            result: '合格'
          },
          {
            id: 2,
            inspectionId: 'QC20230502002',
            supplier: '供应商B',
            goodsName: '原材料Y',
            inspectionDate: '2023-05-02',
            result: '不合格'
          },
          {
            id: 3,
            inspectionId: 'QC20230503003',
            supplier: '供应商C',
            goodsName: '原材料Z',
            inspectionDate: '2023-05-03',
            result: '合格'
          },
          {
            id: 4,
            inspectionId: 'QC20230504004',
            supplier: '供应商A',
            goodsName: '原材料X',
            inspectionDate: '2023-05-04',
            result: '合格'
          },
          {
            id: 5,
            inspectionId: 'QC20230505005',
            supplier: '供应商D',
            goodsName: '原材料W',
            inspectionDate: '2023-05-05',
            result: '合格'
          }
        ]
      }
    }
  }
  </script>
  
  <style scoped>
  .factory-quality-container {
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
  }
  
  .btn-primary {
    background-color: #4caf50;
    color: white;
    border: none;
  }
  
  .btn-secondary {
    background-color: #2196f3;
    color: white;
    border: none;
  }
  
  .btn-info {
    background-color: #03a9f4;
    color: white;
    border: none;
  }
  
  .btn-sm {
    padding: 4px 8px;
    font-size: 12px;
  }
  
  .content-section {
    display: flex;
    gap: 20px;
    margin-bottom: 30px;
  }
  
  .info-card {
    flex: 1;
    border: 1px solid #ddd;
    border-radius: 4px;
    overflow: hidden;
  }
  
  .card-header {
    background-color: #f5f5f5;
    padding: 12px 15px;
    font-weight: bold;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .card-body {
    padding: 15px;
  }
  
  .card-body ul {
    padding-left: 20px;
    margin-top: 10px;
  }
  
  .stats-section {
    display: flex;
    gap: 15px;
  }
  
  .stat-card {
    background-color: #f9f9f9;
    border: 1px solid #eee;
    border-radius: 4px;
    padding: 15px;
    text-align: center;
    min-width: 120px;
  }
  
  .stat-value {
    font-size: 24px;
    font-weight: bold;
    color: #333;
  }
  
  .stat-label {
    font-size: 14px;
    color: #666;
    margin-top: 5px;
  }
  
  .recent-uploads {
    margin-top: 20px;
  }
  
  .recent-uploads h2 {
    font-size: 18px;
    margin-bottom: 15px;
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
  
  .text-success {
    color: #4caf50;
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
  </style>
  
  