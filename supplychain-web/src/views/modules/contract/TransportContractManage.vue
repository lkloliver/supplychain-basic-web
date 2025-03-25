<template>
    <div class="module-page">
      <div class="module-header">
        <div class="header-left">
          <router-link :to="{ name: 'DashboardHome' }" class="back-btn">
            <ArrowLeftIcon class="back-icon" />
            <span>返回</span>
          </router-link>
          <h2 class="module-title">运输合同</h2>
        </div>
        <ModuleViewToggle 
          :uploadRoute="'/dashboard/contract/transport/upload'" 
          :manageRoute="'/dashboard/contract/transport/manage'" 
        />
      </div>
      
      <div class="module-content">
        <div class="manage-section">
          <div class="section-header">
            <h3 class="section-title">管理运输合同</h3>
          </div>
          
          <div class="search-filters">
            <div class="filter-group">
              <input type="text" v-model="searchQuery" placeholder="搜索合同编号或运输商" class="search-input">
              <button class="search-btn" @click="searchContracts">搜索</button>
            </div>
            
            <div class="filter-group">
              <label>日期范围：</label>
              <input type="date" v-model="startDate" class="date-input">
              <span>至</span>
              <input type="date" v-model="endDate" class="date-input">
            </div>
            
            <div class="filter-group">
              <label>状态：</label>
              <select v-model="statusFilter" class="status-select">
                <option value="">全部</option>
                <option value="active">有效</option>
                <option value="expired">已过期</option>
                <option value="completed">已完成</option>
              </select>
            </div>
            
            <button class="reset-filters-btn" @click="resetFilters">重置筛选</button>
          </div>
          
          <div class="data-table">
            <table>
              <thead>
                <tr>
                  <th>合同编号</th>
                  <th>签订日期</th>
                  <th>运输商</th>
                  <th>路线</th>
                  <th>运输量</th>
                  <th>单价</th>
                  <th>总金额</th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(contract, index) in contracts" :key="index">
                  <td>{{ contract.number }}</td>
                  <td>{{ contract.date }}</td>
                  <td>{{ contract.transporter }}</td>
                  <td>{{ contract.route }}</td>
                  <td>{{ contract.quantity }}</td>
                  <td>¥{{ contract.unitPrice.toFixed(2) }}</td>
                  <td>¥{{ contract.totalAmount.toFixed(2) }}</td>
                  <td>
                    <span class="status-badge" :class="contract.status">
                      {{ getStatusText(contract.status) }}
                    </span>
                  </td>
                  <td>
                    <div class="action-buttons">
                      <button class="view-btn" @click="viewContract(contract)">查看</button>
                      <button class="edit-btn" @click="editContract(contract)">编辑</button>
                      <button class="delete-btn" @click="deleteContract(contract)">删除</button>
                    </div>
                  </td>
                </tr>
                <tr v-if="contracts.length === 0">
                  <td colspan="9" class="no-data">暂无数据</td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div class="pagination">
            <button class="page-btn" :disabled="currentPage === 1" @click="currentPage--">上一页</button>
            <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
            <button class="page-btn" :disabled="currentPage === totalPages" @click="currentPage++">下一页</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue';
  import { ArrowLeftIcon } from 'lucide-vue-next';
  import ModuleViewToggle from '@/components/ModuleViewToggle.vue';
  
  // 搜索查询
  const searchQuery = ref('');
  const startDate = ref('');
  const endDate = ref('');
  const statusFilter = ref('');
  
  // 分页
  const currentPage = ref(1);
  const totalPages = ref(5);
  
  // 模拟数据
  const contracts = ref([
    {
      number: 'TC-2023001',
      date: '2023-01-15',
      transporter: '北京物流有限公司',
      route: '北京-上海',
      quantity: 1000,
      unitPrice: 2.5,
      totalAmount: 2500,
      status: 'active'
    },
    {
      number: 'TC-2023002',
      date: '2023-02-20',
      transporter: '河南运输集团',
      route: '郑州-广州',
      quantity: 1500,
      unitPrice: 1.8,
      totalAmount: 2700,
      status: 'active'
    },
    {
      number: 'TC-2023003',
      date: '2023-03-10',
      transporter: '山东物流发展有限公司',
      route: '济南-武汉',
      quantity: 800,
      unitPrice: 4.2,
      totalAmount: 3360,
      status: 'completed'
    },
    {
      number: 'TC-2023004',
      date: '2023-04-05',
      transporter: '黑龙江运输集团',
      route: '哈尔滨-北京',
      quantity: 1200,
      unitPrice: 3.0,
      totalAmount: 3600,
      status: 'expired'
    },
    {
      number: 'TC-2023005',
      date: '2023-05-18',
      transporter: '安徽物流贸易有限公司',
      route: '合肥-南京',
      quantity: 2000,
      unitPrice: 2.6,
      totalAmount: 5200,
      status: 'active'
    }
  ]);
  
  // 获取状态文本
  const getStatusText = (status: string) => {
    switch (status) {
      case 'active': return '有效';
      case 'expired': return '已过期';
      case 'completed': return '已完成';
      default: return '未知';
    }
  };
  
  // 搜索合同
  const searchContracts = () => {
    // 实际应用中这里会调用API进行搜索
    console.log('搜索条件:', {
      query: searchQuery.value,
      startDate: startDate.value,
      endDate: endDate.value,
      status: statusFilter.value
    });
  };
  
  // 重置筛选条件
  const resetFilters = () => {
    searchQuery.value = '';
    startDate.value = '';
    endDate.value = '';
    statusFilter.value = '';
  };
  
  // 查看合同详情
  const viewContract = (contract: any) => {
    console.log('查看合同:', contract);
  };
  
  // 编辑合同
  const editContract = (contract: any) => {
    console.log('编辑合同:', contract);
  };
  
  // 删除合同
  const deleteContract = (contract: any) => {
    if (confirm(`确定要删除合同 ${contract.number} 吗？`)) {
      console.log('删除合同:', contract);
    }
  };
  </script>
  
  <style scoped>
  .module-page {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 1.5rem;
  }
  
  .module-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
    border-bottom: 1px solid #e0e0e0;
    padding-bottom: 1rem;
  }
  
  .header-left {
    display: flex;
    align-items: center;
    gap: 1rem;
  }
  
  .back-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 0.75rem;
    background-color: #f5f7fa;
    border: none;
    border-radius: 4px;
    font-size: 0.875rem;
    color: #424242;
    cursor: pointer;
    transition: background-color 0.3s;
    text-decoration: none;
  }
  
  .back-btn:hover {
    background-color: #e0e0e0;
  }
  
  .back-icon {
    width: 16px;
    height: 16px;
  }
  
  .module-title {
    font-size: 1.5rem;
    font-weight: 600;
    margin: 0;
    color: #0a2463;
  }
  
  .module-content {
    min-height: 400px;
  }
  
  .section-header {
    margin-bottom: 1.5rem;
  }
  
  .section-title {
    font-size: 1.25rem;
    font-weight: 500;
    color: #333;
    margin: 0;
    padding-left: 0.5rem;
    border-left: 3px solid #1e88e5;
  }
  
  .manage-section {
    display: flex;
    flex-direction: column;
    gap: 1.25rem;
  }
  
  .search-filters {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
    padding: 1rem;
    background-color: #f5f7fa;
    border-radius: 8px;
  }
  
  .filter-group {
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }
  
  .search-input {
    width: 250px;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .date-input {
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .status-select {
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  .search-btn, .reset-filters-btn {
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.875rem;
  }
  
  .search-btn {
    background-color: #1e88e5;
    color: white;
  }
  
  .reset-filters-btn {
    background-color: #f5f5f5;
    color: #424242;
  }
  
  .data-table {
    overflow-x: auto;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    padding: 0.75rem;
    text-align: left;
    border-bottom: 1px solid #eee;
  }
  
  th {
    background-color: #f5f7fa;
    font-weight: 600;
    color: #333;
  }
  
  .status-badge {
    display: inline-block;
    padding: 0.25rem 0.5rem;
    border-radius: 4px;
    font-size: 0.75rem;
  }
  
  .status-badge.active {
    background-color: #e8f5e9;
    color: #2e7d32;
  }
  
  .status-badge.expired {
    background-color: #ffebee;
    color: #c62828;
  }
  
  .status-badge.completed {
    background-color: #e3f2fd;
    color: #1565c0;
  }
  
  .action-buttons {
    display: flex;
    gap: 0.5rem;
  }
  
  .view-btn, .edit-btn, .delete-btn {
    padding: 0.25rem 0.5rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.75rem;
  }
  
  .view-btn {
    background-color: #e3f2fd;
    color: #1565c0;
  }
  
  .edit-btn {
    background-color: #fff8e1;
    color: #f57f17;
  }
  
  .delete-btn {
    background-color: #ffebee;
    color: #c62828;
  }
  
  .no-data {
    text-align: center;
    color: #757575;
    padding: 2rem;
  }
  
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 1rem;
    margin-top: 1rem;
  }
  
  .page-btn {
    padding: 0.5rem 1rem;
    border: 1px solid #ddd;
    background-color: white;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .page-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .page-info {
    color: #666;
  }
  </style>
  
  