<template>
  <div class="dashboard-home">
    <h1 class="dashboard-title">欢迎使用供应链管理系统</h1>
    
    <!-- 合同模块 -->
    <div class="module">
      <h3 class="module-title">合同模块</h3>
      <div class="module-grid">
        <router-link :to="{ name: 'PurchaseContract' }" class="module-card">
          <FileTextIcon class="card-icon" />
          <span class="card-title">采购合同</span>
        </router-link>
        <router-link :to="{ name: 'SalesContract' }" class="module-card">
          <FileTextIcon class="card-icon" />
          <span class="card-title">销售合同</span>
        </router-link>
        <router-link :to="{ name: 'TransportContract' }" class="module-card">
          <TruckIcon class="card-icon" />
          <span class="card-title">运输合同</span>
        </router-link>
      </div>
    </div>
    
    <!-- 货物模块 -->
    <div class="module">
      <h3 class="module-title">货物模块</h3>
      <div class="module-grid">
        <template v-if="version === 'BASE'">
          <router-link :to="{ name: 'InboundWeight' }" class="module-card">
            <ScaleIcon class="card-icon" />
            <span class="card-title">入库过磅单</span>
          </router-link>
          <router-link :to="{ name: 'InboundQuality' }" class="module-card">
            <ClipboardCheckIcon class="card-icon" />
            <span class="card-title">入库质检单</span>
          </router-link>
          <router-link :to="{ name: 'InboundImage' }" class="module-card">
            <ImageIcon class="card-icon" />
            <span class="card-title">入库图片</span>
          </router-link>
          <router-link :to="{ name: 'OutboundWeight' }" class="module-card">
            <ScaleIcon class="card-icon" />
            <span class="card-title">出库过磅单</span>
          </router-link>
          <router-link :to="{ name: 'OutboundQuality' }" class="module-card">
            <ClipboardCheckIcon class="card-icon" />
            <span class="card-title">出库质检单</span>
          </router-link>
          <router-link :to="{ name: 'OutboundImage' }" class="module-card">
            <ImageIcon class="card-icon" />
            <span class="card-title">出库图片</span>
          </router-link>
          <router-link :to="{ name: 'PurchaseWaybill' }" class="module-card">
            <FileTextIcon class="card-icon" />
            <span class="card-title">采购运单</span>
          </router-link>
          <router-link :to="{ name: 'SalesWaybill' }" class="module-card">
            <FileTextIcon class="card-icon" />
            <span class="card-title">销售运单</span>
          </router-link>
        </template>
        <template v-else>
          <router-link :to="{ name: 'FactoryWeight' }" class="module-card">
            <ScaleIcon class="card-icon" />
            <span class="card-title">到货厂家过磅单</span>
          </router-link>
          <router-link :to="{ name: 'FactoryQuality' }" class="module-card">
            <ClipboardCheckIcon class="card-icon" />
            <span class="card-title">到货厂家质检单</span>
          </router-link>
          <router-link :to="{ name: 'FactoryImage' }" class="module-card">
            <ImageIcon class="card-icon" />
            <span class="card-title">到货厂家图片</span>
          </router-link>
          <router-link :to="{ name: 'Waybill' }" class="module-card">
            <FileTextIcon class="card-icon" />
            <span class="card-title">运单</span>
          </router-link>
        </template>
      </div>
    </div>
    
    <!-- 结算模块 -->
    <div class="module">
      <h3 class="module-title">结算模块</h3>
      <div class="module-grid">
        <router-link :to="{ name: 'PurchaseSettlement' }" class="module-card">
          <CalculatorIcon class="card-icon" />
          <span class="card-title">采购结算</span>
        </router-link>
        <router-link :to="{ name: 'SalesSettlement' }" class="module-card">
          <CalculatorIcon class="card-icon" />
          <span class="card-title">销售结算</span>
        </router-link>
      </div>
    </div>
    
    <!-- 资金模块 -->
    <div class="module">
      <h3 class="module-title">资金模块</h3>
      <div class="module-grid">
        <router-link :to="{ name: 'PurchasePayment' }" class="module-card">
          <DollarSignIcon class="card-icon" />
          <span class="card-title">采购付款</span>
        </router-link>
        <router-link :to="{ name: 'SalesReceipt' }" class="module-card">
          <DollarSignIcon class="card-icon" />
          <span class="card-title">销售收款</span>
        </router-link>
        <router-link :to="{ name: 'TransportPayment' }" class="module-card">
          <DollarSignIcon class="card-icon" />
          <span class="card-title">运输付款</span>
        </router-link>
      </div>
    </div>
    
    <!-- 发票模块 -->
    <div class="module">
      <h3 class="module-title">发票模块</h3>
      <div class="module-grid">
        <router-link :to="{ name: 'PurchaseInvoiceIn' }" class="module-card">
          <FileTextIcon class="card-icon" />
          <span class="card-title">采购进项发票</span>
        </router-link>
        <router-link :to="{ name: 'PurchaseInvoiceOut' }" class="module-card">
          <FileTextIcon class="card-icon" />
          <span class="card-title">采购销项发票</span>
        </router-link>
        <router-link :to="{ name: 'SalesInvoice' }" class="module-card">
          <FileTextIcon class="card-icon" />
          <span class="card-title">销售发票</span>
        </router-link>
        <router-link :to="{ name: 'TransportInvoice' }" class="module-card">
          <FileTextIcon class="card-icon" />
          <span class="card-title">运输发票</span>
        </router-link>
      </div>
    </div>
    
    <!-- 台账模块 -->
    <div class="module">
      <h3 class="module-title">台账模块</h3>
      <div class="module-grid">
        <router-link :to="{ name: 'PurchaseLedger' }" class="module-card">
          <BookIcon class="card-icon" />
          <span class="card-title">采购台账</span>
        </router-link>
        <router-link :to="{ name: 'SalesLedger' }" class="module-card">
          <BookIcon class="card-icon" />
          <span class="card-title">销售台账</span>
        </router-link>
      </div>
    </div>
    <!-- 区块链存证模块 -->
  <div class="module">
      <h3 class="module-title">区块链存证模块</h3>
      <div class="module-grid">
        <router-link :to="{ name: 'BlockchainRecord' }" class="module-card">
          <FileTextIcon class="card-icon blockchain-verify-icon" />
          <span class="card-title">区块链存证记录</span>
        </router-link>
        <router-link :to="{ name: 'BlockchainVerify' }" class="module-card">
          <FileTextIcon class="card-icon blockchain-verify-icon" />
          <span class="card-title">区块链验证</span>
        </router-link>
      </div>
    </div>
  </div>

  
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { useUserStore } from '@/stores/user';
import { 
  FileTextIcon, 
  TruckIcon, 
  ScaleIcon, 
  CalculatorIcon,
  DollarSignIcon,
  BookIcon,
  ClipboardCheckIcon,
  ImageIcon
} from 'lucide-vue-next';

const userStore = useUserStore();
const version = computed(() => userStore.version);
</script>

<style scoped>
.dashboard-home {
  padding: 1rem;
}

.dashboard-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  color: #0a2463;
}

.module {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

.module-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-top: 0;
  margin-bottom: 1.25rem;
  color: #0a2463;
}

.module-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 1rem;
}

.module-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.3s;
  text-decoration: none;
}

.module-card:hover {
  background-color: #e3f2fd;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-icon {
  width: 32px;
  height: 32px;
  color: #1e88e5;
  margin-bottom: 0.75rem;
}

.card-title {
  font-size: 0.875rem;
  font-weight: 500;
  text-align: center;
  color: #424242;
}

/* 区块链存证模块图标样式 */
.blockchain-icon {
  background-color: #e3f2fd;
  color: #1e88e5;
}

.blockchain-verify-icon {
  background-color: #e8f5e9;
  color: #43a047;
}
</style>

