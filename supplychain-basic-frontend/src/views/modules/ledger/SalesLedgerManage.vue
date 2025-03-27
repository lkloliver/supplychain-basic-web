<template>
  <div class="ledger-manage">
    <!-- 顶部操作栏 -->
    <div class="top-actions">
      <div class="group-selector">
        <span class="label">分组方式：</span>
        <select v-model="groupBy" class="select-input">
          <option value="measurementNo">按计量号组合</option>
          <option value="settlement">按结算单组合</option>
          <option value="invoice">按发票组合</option>
          <option value="transferRecord">按转账记录组合</option>
        </select>
      </div>
      <div class="search-box">
        <div class="search-input-wrapper">
          <i class="search-icon">🔍</i>
          <input 
            type="text" 
            v-model="searchKey"
            placeholder="搜索台账信息"
            class="search-input"
            @keyup.enter="handleSearch"
            @input="handleSearchInput"
          />
          <i 
            v-if="searchKey" 
            class="clear-icon"
            @click="clearSearch"
          >
            ✕
          </i>
        </div>
      </div>
    </div>

    <!-- 台账列表 -->
    <div class="ledger-list">
      <div v-for="group in groupedLedgers" :key="group.key" class="ledger-group">
        <!-- 分组标题 -->
        <div class="group-header">
          <h3 class="group-title">{{ getGroupTitle(group.key) }}</h3>
          <div class="group-summary">
            <span>共 {{ group.items.length }} 条记录</span>
            <span>总金额：{{ calculateGroupTotal(group.items) }}</span>
          </div>
        </div>

        <!-- 分组内容 -->
        <table class="data-table">
          <thead>
            <tr>
              <th>销售合同</th>
              <th>过磅单</th>
              <th>质检单</th>
              <th>运单</th>
              <th>销售结算单</th>
              <th>发票</th>
              <th>转账记录</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="ledger in group.items" :key="ledger.id">
              <td>{{ ledger.contract?.contractNo }}</td>
              <td>{{ ledger.weight?.measurementNo }}</td>
              <td>{{ ledger.quality?.measurementNo }}</td>
              <td>{{ ledger.waybill?.name }}</td>
              <td>{{ ledger.settlement?.name }}</td>
              <td>{{ ledger.invoice?.name }}</td>
              <td>{{ ledger.transferRecord?.name }}</td>
              <td>
                <button class="action-btn primary" @click="viewDetail(ledger)">
                  查看
                </button>
                <button class="action-btn secondary" @click="editLedger(ledger)">
                  编辑
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button 
        class="page-btn"
        :disabled="currentPage === 1"
        @click="currentPage--"
      >
        上一页
      </button>
      <span class="page-info">
        第 {{ currentPage }} 页 / 共 {{ totalPages }} 页
      </span>
      <button 
        class="page-btn"
        :disabled="currentPage === totalPages"
        @click="currentPage++"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import '@/assets/styles/ledger.css';

// 分组方式
const groupBy = ref('measurementNo');

// 搜索关键词
const searchKey = ref('');

// 当前页码
const currentPage = ref(1);
const pageSize = 10;

// 模拟台账数据
const ledgers = ref([
  {
    id: 1,
    contract: {
      contractNo: 'SC-2024-001',
      contractName: '2024年第一季度销售合同'
    },
    weight: {
      measurementNo: 'WB-2024-001',
      name: '过磅单-2024-001'
    },
    quality: {
      measurementNo: 'QZ-2024-001',
      name: '质检单-2024-001'
    },
    waybill: {
      name: '运单-2024-001',
      transportMethod: '公路运输'
    },
    settlement: {
      name: '销售结算单-2024-001',
      amount: '100000.00'
    },
    invoice: {
      name: '销售发票-2024-001',
      amount: '100000.00'
    },
    transferRecord: {
      name: '转账记录-2024-001',
      amount: '100000.00'
    }
  }
]);

// 过滤后的数据
const filteredLedgers = computed(() => {
  if (!searchKey.value) return ledgers.value;
  const key = searchKey.value.toLowerCase();
  return ledgers.value.filter(ledger => 
    ledger.contract?.contractNo.toLowerCase().includes(key) ||
    ledger.weight?.measurementNo.toLowerCase().includes(key) ||
    ledger.quality?.measurementNo.toLowerCase().includes(key) ||
    ledger.waybill?.name.toLowerCase().includes(key) ||
    ledger.settlement?.name.toLowerCase().includes(key) ||
    ledger.invoice?.name.toLowerCase().includes(key) ||
    ledger.transferRecord?.name.toLowerCase().includes(key)
  );
});

// 分组后的数据
const groupedLedgers = computed(() => {
  const groups: { [key: string]: any[] } = {};
  
  filteredLedgers.value.forEach(ledger => {
    let key = '';
    switch (groupBy.value) {
      case 'measurementNo':
        key = ledger.weight?.measurementNo || ledger.quality?.measurementNo || '未分组';
        break;
      case 'settlement':
        key = ledger.settlement?.name || '未分组';
        break;
      case 'invoice':
        key = ledger.invoice?.name || '未分组';
        break;
      case 'transferRecord':
        key = ledger.transferRecord?.name || '未分组';
        break;
    }
    
    if (!groups[key]) {
      groups[key] = [];
    }
    groups[key].push(ledger);
  });
  
  return Object.entries(groups).map(([key, items]) => ({
    key,
    items
  }));
});

// 获取分组标题
const getGroupTitle = (key: string) => {
  switch (groupBy.value) {
    case 'measurementNo':
      return `计量号：${key}`;
    case 'settlement':
      return `结算单：${key}`;
    case 'invoice':
      return `发票：${key}`;
    case 'transferRecord':
      return `转账记录：${key}`;
    default:
      return key;
  }
};

// 计算分组总金额
const calculateGroupTotal = (items: any[]) => {
  return items.reduce((sum, item) => {
    return sum + (parseFloat(item.settlement?.amount || 0));
  }, 0).toFixed(2);
};

// 总页数
const totalPages = computed(() => {
  return Math.ceil(filteredLedgers.value.length / pageSize);
});

// 搜索方法
const handleSearch = () => {
  currentPage.value = 1;
};

// 搜索输入处理
const handleSearchInput = () => {
  currentPage.value = 1;
};

// 清除搜索
const clearSearch = () => {
  searchKey.value = '';
  currentPage.value = 1;
};

// 查看详情
const viewDetail = (ledger: any) => {
  console.log('查看台账详情：', ledger);
  // TODO: 实现查看详情功能
};

// 编辑台账
const editLedger = (ledger: any) => {
  console.log('编辑台账：', ledger);
  // TODO: 实现编辑功能
};
</script>
  
  