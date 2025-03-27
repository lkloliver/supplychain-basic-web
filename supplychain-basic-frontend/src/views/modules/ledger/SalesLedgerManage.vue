<template>
  <div class="ledger-manage">
    <!-- 顶部操作栏 -->
    <div class="top-actions">
      <div class="group-selector">
        <span class="label">分组方式：</span>
        <select v-model="groupBy" class="select-input">
          <option value="noteNo">按计量号组合</option>
          <option value="settlementId">按结算单组合</option>
          <option value="invoiceId">按发票组合</option>
          <option value="paymentId">按付款记录组合</option>
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
    <div class="ledger-list" v-loading="loading">
      <template v-if="groupedLedgers.length > 0">
        <div v-for="group in groupedLedgers" :key="group.key" class="ledger-group">
          <!-- 分组标题 -->
          <div class="group-header">
            <h3 class="group-title">{{ getGroupTitle(group.key) }}</h3>
            <div class="group-summary">
              <span>共 {{ group.items.length }} 条记录</span>
              <span>总金额：¥{{ calculateGroupTotal(group.items) }}</span>
            </div>
          </div>

          <!-- 分组内容 -->
          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr>
                  <th>销售合同</th>
                  <th>过磅单</th>
                  <th>质检单</th>
                  <th>运单</th>
                  <th>结算单</th>
                  <th>发票</th>
                  <th>收款记录</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="ledger in group.items" :key="ledger.id">
                  <td>{{ ledger.contractId }}</td>
                  <td>{{ ledger.weightNoteNo }}</td>
                  <td>{{ ledger.qualityNoteNo }}</td>
                  <td>{{ ledger.transportNoteId }}</td>
                  <td>{{ ledger.settlementId }}</td>
                  <td>{{ ledger.invoiceId }}</td>
                  <td>{{ ledger.paymentId }}</td>
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
      </template>
      <div v-else class="empty-state">
        <i class="empty-icon">📊</i>
        <p>暂无台账数据</p>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="groupedLedgers.length > 0">
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

    <!-- 弹窗组件 -->
    <transition name="fade">
      <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
        <div class="modal-content" @click.stop>
          <div class="modal-header">
            <h3>台账详情</h3>
            <button class="close-btn" @click="closeDetailModal">×</button>
          </div>
          <div class="modal-body">
            <div class="detail-section">
              <h4>基本信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">租户类型：</span>
                  <span class="value">{{ getTenantTypeText(selectedLedger?.tenantType) }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">销售方：</span>
                  <span class="value">{{ selectedLedger?.sellerName }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">购买方：</span>
                  <span class="value">{{ selectedLedger?.buyerName }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">纳税人类型：</span>
                  <span class="value">{{ selectedLedger?.buyerTaxType }}</span>
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h4>销售信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">销售时间：</span>
                  <span class="value">{{ formatDateTime(selectedLedger?.saleTime) }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">销售地点：</span>
                  <span class="value">{{ selectedLedger?.saleLocation }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">产品名称：</span>
                  <span class="value">{{ selectedLedger?.productName }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">销售数量：</span>
                  <span class="value">{{ selectedLedger?.actualQuantity }}</span>
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h4>价格信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">销售价格：</span>
                  <span class="value">¥{{ selectedLedger?.priceWithoutTax }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">税率：</span>
                  <span class="value">{{ selectedLedger?.taxRate }}%</span>
                </div>
                <div class="detail-item">
                  <span class="label">税额：</span>
                  <span class="value">¥{{ selectedLedger?.taxAmount }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">应收金额：</span>
                  <span class="value">¥{{ selectedLedger?.receivableAmount }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">实收金额：</span>
                  <span class="value">¥{{ selectedLedger?.receivedAmount }}</span>
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h4>关联信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">合同ID：</span>
                  <span class="value">{{ selectedLedger?.contractId }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">质检单ID：</span>
                  <span class="value">{{ selectedLedger?.qualityNoteId }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">质检单号：</span>
                  <span class="value">{{ selectedLedger?.qualityNoteNo }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">运单ID：</span>
                  <span class="value">{{ selectedLedger?.transportNoteId }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">磅单ID：</span>
                  <span class="value">{{ selectedLedger?.weightNoteId }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">磅单号：</span>
                  <span class="value">{{ selectedLedger?.weightNoteNo }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">结算单ID：</span>
                  <span class="value">{{ selectedLedger?.settlementId }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">发票ID：</span>
                  <span class="value">{{ selectedLedger?.invoiceId }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">开票时间：</span>
                  <span class="value">{{ formatDateTime(selectedLedger?.invoiceTime) }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">付款记录ID：</span>
                  <span class="value">{{ selectedLedger?.paymentId }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-if="showEditModal" class="modal-overlay" @click="closeEditModal">
        <div class="modal-content" @click.stop>
          <div class="modal-header">
            <h3>编辑台账</h3>
            <button class="close-btn" @click="closeEditModal">×</button>
          </div>
          <div class="modal-body">
            <div class="detail-section">
              <h4>基本信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">租户类型：</span>
                  <span class="value">{{ getTenantTypeText(editingLedger?.tenantType) }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">销售方：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.sellerName"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">购买方：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.buyerName"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">纳税人类型：</span>
                  <select v-model="editingLedger.buyerTaxType" class="input-field">
                    <option value="一般纳税人">一般纳税人</option>
                    <option value="小规模纳税人">小规模纳税人</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h4>销售信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">销售时间：</span>
                  <input 
                    type="datetime-local" 
                    v-model="editingLedger.saleTime"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">销售地点：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.saleLocation"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">产品名称：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.productName"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">销售数量：</span>
                  <input 
                    type="number" 
                    v-model="editingLedger.actualQuantity"
                    class="edit-input"
                  />
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h4>价格信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">销售价格：</span>
                  <input 
                    type="number" 
                    v-model="editingLedger.priceWithoutTax"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">税率：</span>
                  <select v-model="editingLedger.taxRate" class="input-field">
                    <option value="13">13%</option>
                    <option value="9">9%</option>
                    <option value="6">6%</option>
                    <option value="3">3%</option>
                  </select>
                </div>
                <div class="detail-item">
                  <span class="label">税额：</span>
                  <input 
                    type="number" 
                    v-model="editingLedger.taxAmount"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">应收金额：</span>
                  <input 
                    type="number" 
                    v-model="editingLedger.receivableAmount"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">实收金额：</span>
                  <input 
                    type="number" 
                    v-model="editingLedger.receivedAmount"
                    class="edit-input"
                  />
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h4>关联信息</h4>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="label">合同ID：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.contractId"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">质检单ID：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.qualityNoteId"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">质检单号：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.qualityNoteNo"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">运单ID：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.transportNoteId"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">磅单ID：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.weightNoteId"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">磅单号：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.weightNoteNo"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">结算单ID：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.settlementId"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">发票ID：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.invoiceId"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">开票时间：</span>
                  <input 
                    type="datetime-local" 
                    v-model="editingLedger.invoiceTime"
                    class="edit-input"
                  />
                </div>
                <div class="detail-item">
                  <span class="label">付款记录ID：</span>
                  <input 
                    type="text" 
                    v-model="editingLedger.paymentId"
                    class="edit-input"
                  />
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn secondary" @click="closeEditModal">取消</button>
            <button class="btn primary" @click="handleSubmit">提交</button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import '@/assets/styles/ledger.css';

const router = useRouter();

// 分组方式
const groupBy = ref('noteNo');

// 搜索关键词
const searchKey = ref('');

// 当前页码
const currentPage = ref(1);
const pageSize = 10;

// 添加加载状态
const loading = ref(false);

// 模拟台账数据
// 实际使用时需要调用 salesLedgerApi.getAllLedgers(tenantType) 获取所有销售台账数据
const ledgers = ref([
  {
    id: 1,
    tenantType: 'nonbase',
    sellerName: '销售方A',
    buyerName: '购买方B',
    buyerTaxType: '一般纳税人',
    saleTime: '2024-03-20T10:00:00',
    saleLocation: '广州',
    productName: '产品A',
    actualQuantity: 100,
    priceWithoutTax: 1000,
    taxRate: 13,
    taxAmount: 130,
    receivableAmount: 1130,
    receivedAmount: 1130,
    contractId: 'CT-001',
    qualityNoteId: 'QN-001',
    qualityNoteNo: 'QZ-2024-001',
    transportNoteId: 'TN-001',
    weightNoteId: 'WN-001',
    weightNoteNo: 'WB-2024-001',
    settlementId: 'ST-001',
    invoiceId: 'IN-001',
    invoiceTime: '2024-03-21T10:00:00',
    paymentId: 'PAY-001'
  },
  {
    id: 2,
    tenantType: 'nonbase',
    sellerName: '销售方C',
    buyerName: '购买方D',
    buyerTaxType: '小规模纳税人',
    saleTime: '2024-03-19T14:30:00',
    saleLocation: '深圳',
    productName: '产品B',
    actualQuantity: 200,
    priceWithoutTax: 800,
    taxRate: 3,
    taxAmount: 24,
    receivableAmount: 824,
    receivedAmount: 824,
    contractId: 'CT-002',
    qualityNoteId: 'QN-002',
    qualityNoteNo: 'QZ-2024-002',
    transportNoteId: 'TN-002',
    weightNoteId: 'WN-002',
    weightNoteNo: 'WB-2024-002',
    settlementId: 'ST-002',
    invoiceId: 'IN-002',
    invoiceTime: '2024-03-20T09:00:00',
    paymentId: 'PAY-002'
  },
  {
    id: 3,
    tenantType: 'nonbase',
    sellerName: '销售方E',
    buyerName: '购买方F',
    buyerTaxType: '一般纳税人',
    saleTime: '2024-03-18T09:15:00',
    saleLocation: '东莞',
    productName: '产品C',
    actualQuantity: 150,
    priceWithoutTax: 1200,
    taxRate: 13,
    taxAmount: 156,
    receivableAmount: 1356,
    receivedAmount: 1000,
    contractId: 'CT-003',
    qualityNoteId: 'QN-003',
    qualityNoteNo: 'QZ-2024-003',
    transportNoteId: 'TN-003',
    weightNoteId: 'WN-003',
    weightNoteNo: 'WB-2024-003',
    settlementId: 'ST-001',
    invoiceId: 'IN-003',
    invoiceTime: '2024-03-19T15:30:00',
    paymentId: 'PAY-003'
  },
  {
    id: 4,
    tenantType: 'nonbase',
    sellerName: '销售方G',
    buyerName: '购买方H',
    buyerTaxType: '一般纳税人',
    saleTime: '2024-03-17T11:20:00',
    saleLocation: '佛山',
    productName: '产品D',
    actualQuantity: 300,
    priceWithoutTax: 900,
    taxRate: 13,
    taxAmount: 117,
    receivableAmount: 1017,
    receivedAmount: 0,
    contractId: 'CT-004',
    qualityNoteId: 'QN-004',
    qualityNoteNo: 'QZ-2024-004',
    transportNoteId: 'TN-004',
    weightNoteId: 'WN-004',
    weightNoteNo: 'WB-2024-004',
    settlementId: 'ST-004',
    invoiceId: 'IN-004',
    invoiceTime: '2024-03-18T16:45:00',
    paymentId: 'PAY-004'
  },
  {
    id: 5,
    tenantType: 'nonbase',
    sellerName: '销售方I',
    buyerName: '购买方J',
    buyerTaxType: '小规模纳税人',
    saleTime: '2024-03-16T16:00:00',
    saleLocation: '惠州',
    productName: '产品E',
    actualQuantity: 250,
    priceWithoutTax: 750,
    taxRate: 3,
    taxAmount: 22.5,
    receivableAmount: 772.5,
    receivedAmount: 772.5,
    contractId: 'CT-005',
    qualityNoteId: 'QN-005',
    qualityNoteNo: 'QZ-2024-005',
    transportNoteId: 'TN-005',
    weightNoteId: 'WN-005',
    weightNoteNo: 'WB-2024-005',
    settlementId: 'ST-005',
    invoiceId: 'IN-005',
    invoiceTime: '2024-03-17T10:15:00',
    paymentId: 'PAY-005'
  }
]);

// 获取租户类型文本
const getTenantTypeText = (type: string) => {
  switch (type) {
    case 'base':
      return '基地版';
    case 'nonbase':
      return '非基地版';
    default:
      return '通用数据';
  }
};

// 格式化日期时间
const formatDateTime = (datetime: string) => {
  if (!datetime) return '';
  return new Date(datetime).toLocaleString();
};

// 过滤后的数据
// 实际使用时需要调用 salesLedgerApi.getLedgersByField('searchKey', searchKey, tenantType) 进行模糊查询销售台账
const filteredLedgers = computed(() => {
  if (!searchKey.value) return ledgers.value;
  const key = searchKey.value.toLowerCase();
  return ledgers.value.filter(ledger => 
    ledger.buyerName.toLowerCase().includes(key) ||
    ledger.sellerName.toLowerCase().includes(key) ||
    ledger.productName.toLowerCase().includes(key) ||
    ledger.weightNoteNo?.toLowerCase().includes(key) ||
    ledger.qualityNoteNo?.toLowerCase().includes(key) ||
    ledger.settlementId?.toLowerCase().includes(key) ||
    ledger.invoiceId?.toLowerCase().includes(key) ||
    ledger.paymentId?.toLowerCase().includes(key)
  );
});

// 分组后的数据
const groupedLedgers = computed(() => {
  const groups: { [key: string]: any[] } = {};
  
  filteredLedgers.value.forEach(ledger => {
    let key = '';
    switch (groupBy.value) {
      case 'noteNo':
        key = ledger.weightNoteNo || ledger.qualityNoteNo || '未分组';
        break;
      case 'settlementId':
        key = ledger.settlementId || '未分组';
        break;
      case 'invoiceId':
        key = ledger.invoiceId || '未分组';
        break;
      case 'paymentId':
        key = ledger.paymentId || '未分组';
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
    case 'noteNo':
      return `计量号：${key}`;
    case 'settlementId':
      return `结算单：${key}`;
    case 'invoiceId':
      return `发票：${key}`;
    case 'paymentId':
      return `付款记录：${key}`;
    default:
      return key;
  }
};

// 计算分组总金额
const calculateGroupTotal = (items: any[]) => {
  return items.reduce((sum, item) => {
    return sum + (parseFloat(item.receivableAmount || 0));
  }, 0).toFixed(2);
};

// 总页数
const totalPages = computed(() => {
  return Math.ceil(filteredLedgers.value.length / pageSize);
});

// 搜索方法
// 实际使用时需要调用 salesLedgerApi.getLedgersByField('searchKey', searchKey, tenantType) 进行模糊搜索
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

// 弹窗控制
const showDetailModal = ref(false);
const selectedLedger = ref<any>(null);

// 查看详情
// 实际使用时需要调用 salesLedgerApi.getLedgerDetail(id, tenantType) 获取台账详情
const viewDetail = (ledger: any) => {
  console.log('查看台账详情：', ledger);
  selectedLedger.value = ledger;
  showDetailModal.value = true;
};

// 关闭详情弹窗
const closeDetailModal = () => {
  showDetailModal.value = false;
  selectedLedger.value = null;
};

// 编辑弹窗控制
const showEditModal = ref(false);
const editingLedger = ref<any>(null);

// 编辑台账
const editLedger = (ledger: any) => {
  editingLedger.value = { ...ledger };
  showEditModal.value = true;
};

// 关闭编辑弹窗
const closeEditModal = () => {
  showEditModal.value = false;
  editingLedger.value = null;
};

// 监听编辑数据变化，自动计算税额和应收金额
watch(
  () => ({
    priceWithoutTax: editingLedger.value?.priceWithoutTax,
    taxRate: editingLedger.value?.taxRate
  }),
  (newValue) => {
    if (editingLedger.value) {
      const { priceWithoutTax, taxRate } = newValue;
      const taxAmount = (priceWithoutTax * taxRate) / 100;
      editingLedger.value.taxAmount = taxAmount;
      editingLedger.value.payableAmount = priceWithoutTax + taxAmount;
    }
  },
  { deep: true }
);

// 提交编辑
// 实际使用时需要调用 salesLedgerApi.updateLedger(editingLedger, tenantType) 更新台账数据
const handleSubmit = () => {
  if (!editingLedger.value) return;
  
  // 更新台账数据
  const index = ledgers.value.findIndex(l => l.id === editingLedger.value.id);
  if (index !== -1) {
    ledgers.value[index] = { ...editingLedger.value };
  }
  
  closeEditModal();
};

// 模拟加载数据
// 实际使用时需要调用 salesLedgerApi.getAllLedgers(tenantType) 获取所有销售台账数据
const loadData = async () => {
  loading.value = true;
  try {
    // TODO: 调用API获取数据
    await new Promise(resolve => setTimeout(resolve, 1000));
  } finally {
    loading.value = false;
  }
};

// 在组件挂载时加载数据
onMounted(() => {
  loadData();
});
</script>

<style scoped>
.ledger-manage {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.top-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.group-selector {
  display: flex;
  align-items: center;
  gap: 10px;
}

.label {
  color: #666;
}

.select-input {
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  min-width: 150px;
}

.search-box {
  width: 300px;
}

.ledger-list {
  position: relative;
  min-height: 200px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state p {
  color: #999;
  font-size: 14px;
  margin: 0;
}

.table-wrapper {
  overflow-x: auto;
  margin: 0 -20px;
  padding: 0 20px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin: 15px 0;
  background-color: white;
}

.data-table th,
.data-table td {
  padding: 12px 8px;
  text-align: left;
  border-bottom: 1px solid #ebeef5;
}

.data-table th {
  background-color: #f5f7fa;
  font-weight: 500;
  color: #606266;
}

.data-table tr:hover {
  background-color: #f5f7fa;
}

.action-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  margin-right: 8px;
  background: #f0f7ff;
  color: #1890ff;
  border: 1px solid #1890ff;
  display: inline-block;
}

.action-btn:last-child {
  margin-right: 0;
}

.action-btn.primary {
  background: #1890ff;
  color: white;
}

.action-btn.primary:hover {
  background: #40a9ff;
  border-color: #40a9ff;
}

.action-btn.secondary {
  background: #f0f7ff;
  color: #1890ff;
  border: 1px solid #1890ff;
}

.action-btn.secondary:hover {
  background: #e6f7ff;
  border-color: #40a9ff;
  color: #40a9ff;
}

.search-input-wrapper {
  position: relative;
  width: 100%;
}

.search-input {
  width: 100%;
  padding: 8px 12px 8px 36px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s;
}

.search-input:focus {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
  outline: none;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

.clear-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  cursor: pointer;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.3s;
}

.clear-icon:hover {
  background: #f5f5f5;
  color: #666;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  border-radius: 8px;
  width: 800px;
  max-height: 80vh;
  overflow-y: auto;
  position: relative;
  margin: 20px;
}

.modal-header {
  padding: 16px 24px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  background: #fff;
  z-index: 1;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #999;
  cursor: pointer;
  padding: 0;
  line-height: 1;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s;
}

.close-btn:hover {
  background: #f5f5f5;
  color: #666;
}

.modal-body {
  padding: 24px;
}

.detail-section {
  margin-bottom: 24px;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.detail-section h4 {
  margin: 0 0 16px;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 8px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
}

.detail-item .label {
  width: 100px;
  color: #666;
  font-size: 14px;
}

.detail-item .value {
  flex: 1;
  color: #333;
  font-size: 14px;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-top: 24px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-btn {
  padding: 8px 16px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background: #fff;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  color: #1890ff;
  border-color: #1890ff;
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.page-info {
  color: #666;
  font-size: 14px;
}

.group-header {
  background: #fafafa;
  padding: 16px 20px;
  border-radius: 8px 8px 0 0;
  border-bottom: 1px solid #f0f0f0;
}

.group-title {
  font-size: 16px;
  color: #333;
  margin: 0;
  font-weight: 500;
}

.group-summary {
  display: flex;
  gap: 24px;
  color: #666;
  font-size: 14px;
}

.group-summary span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.edit-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  transition: all 0.3s;
}

.edit-input:focus {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
  outline: none;
}

.modal-footer {
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  background: #fafafa;
  border-radius: 0 0 8px 8px;
}

.btn {
  padding: 8px 24px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  min-width: 80px;
  transition: all 0.3s;
}

.btn.primary {
  background: #1890ff;
  color: #fff;
  border: none;
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
}

.btn.primary:hover {
  background: #40a9ff;
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
}

.btn.secondary {
  background: #fff;
  color: #666;
  border: 1px solid #d9d9d9;
}

.btn.secondary:hover {
  color: #1890ff;
  border-color: #1890ff;
  background: #f0f7ff;
}

.input-field {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  transition: all 0.3s;
}

.input-field:focus {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
  outline: none;
}
</style>
  
  