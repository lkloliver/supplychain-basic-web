<template>
    <div class="dashboard-container">
      <!-- 顶栏 -->
      <header class="top-bar">
        <div class="logo-section">
          <img src="/logo.svg" alt="Logo" class="logo" />
          <h1 class="system-title">供应链管理系统</h1>
        </div>
        
        <div class="user-section">
          <div class="user-info" @click="toggleUserMenu">
            <span class="username">{{ user.username || '用户' }}</span>
            <img src="/avatar-placeholder.png" alt="Avatar" class="avatar" />
          </div>
          
          <div v-if="showUserMenu" class="user-menu">
            <div class="menu-item">
              <UserIcon class="menu-icon" />
              <span>个人信息</span>
            </div>
            <div class="menu-item">
              <SettingsIcon class="menu-icon" />
              <span>设置</span>
            </div>
            <div class="menu-item" @click="logout">
              <LogOutIcon class="menu-icon" />
              <span>退出登录</span>
            </div>
          </div>
        </div>
      </header>
      
      <!-- 导航栏 -->
      <nav class="nav-bar">
        <div 
          v-for="(item, index) in navItems" 
          :key="index"
          :class="['nav-item', { active: activeNavItem === item.key }]"
          @click="activeNavItem = item.key"
        >
          <component :is="item.icon" class="nav-icon" />
          <span>{{ item.label }}</span>
        </div>
      </nav>
      
      <div class="content-container">
        <!-- 侧边栏 -->
        <aside class="sidebar">
    <div class="module-nav">
      <h3 class="nav-title">功能模块</h3>
      
      <div class="nav-group">
        <div 
          class="nav-group-header" 
          @click="toggleNavGroup('contract')"
        >
          <FileTextIcon class="nav-icon" />
          <span>合同模块</span>
          <ChevronDownIcon 
            class="nav-arrow" 
            :class="{ 'rotate-180': expandedGroups.contract }" 
          />
        </div>
        <div 
          class="nav-group-content" 
          v-show="expandedGroups.contract"
        >
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'purchase-contract' }"
            @click="navigateToModule('purchase-contract')"
          >
            <span>采购合同</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'sales-contract' }"
            @click="navigateToModule('sales-contract')"
          >
            <span>销售合同</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'transport-contract' }"
            @click="navigateToModule('transport-contract')"
          >
            <span>运输合同</span>
          </div>
        </div>
      </div>
      
      <div class="nav-group">
        <div 
          class="nav-group-header" 
          @click="toggleNavGroup('goods')"
        >
          <PackageIcon class="nav-icon" />
          <span>货物模块</span>
          <ChevronDownIcon 
            class="nav-arrow" 
            :class="{ 'rotate-180': expandedGroups.goods }" 
          />
        </div>
        <div 
          class="nav-group-content" 
          v-show="expandedGroups.goods"
        >
          <template v-if="user.version === 'base'">
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'inbound-weight' }"
              @click="navigateToModule('inbound-weight')"
            >
              <span>入库过磅单</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'inbound-quality' }"
              @click="navigateToModule('inbound-quality')"
            >
              <span>入库质检单</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'inbound-image' }"
              @click="navigateToModule('inbound-image')"
            >
              <span>货物入库影像</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'outbound-weight' }"
              @click="navigateToModule('outbound-weight')"
            >
              <span>出库过磅单</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'outbound-quality' }"
              @click="navigateToModule('outbound-quality')"
            >
              <span>出库质检单</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'outbound-image' }"
              @click="navigateToModule('outbound-image')"
            >
              <span>货物出库影像</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'purchase-waybill' }"
              @click="navigateToModule('purchase-waybill')"
            >
              <span>采购运单</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'sales-waybill' }"
              @click="navigateToModule('sales-waybill')"
            >
              <span>销售运单</span>
            </div>
          </template>
          <template v-else>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'factory-weight' }"
              @click="navigateToModule('factory-weight')"
            >
              <span>到货厂家过磅单</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'factory-quality' }"
              @click="navigateToModule('factory-quality')"
            >
              <span>到货厂家质检单</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'factory-image' }"
              @click="navigateToModule('factory-image')"
            >
              <span>货物进厂影像</span>
            </div>
            <div 
              class="nav-item" 
              :class="{ active: activeModule === 'waybill' }"
              @click="navigateToModule('waybill')"
            >
              <span>运单</span>
            </div>
          </template>
        </div>
      </div>
      
      <div class="nav-group">
        <div 
          class="nav-group-header" 
          @click="toggleNavGroup('settlement')"
        >
          <CalculatorIcon class="nav-icon" />
          <span>结算模块</span>
          <ChevronDownIcon 
            class="nav-arrow" 
            :class="{ 'rotate-180': expandedGroups.settlement }" 
          />
        </div>
        <div 
          class="nav-group-content" 
          v-show="expandedGroups.settlement"
        >
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'purchase-settlement' }"
            @click="navigateToModule('purchase-settlement')"
          >
            <span>采购结算</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'sales-settlement' }"
            @click="navigateToModule('sales-settlement')"
          >
            <span>销售结算</span>
          </div>
        </div>
      </div>
      
      <div class="nav-group">
        <div 
          class="nav-group-header" 
          @click="toggleNavGroup('finance')"
        >
          <DollarSignIcon class="nav-icon" />
          <span>资金模块</span>
          <ChevronDownIcon 
            class="nav-arrow" 
            :class="{ 'rotate-180': expandedGroups.finance }" 
          />
        </div>
        <div 
          class="nav-group-content" 
          v-show="expandedGroups.finance"
        >
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'purchase-payment' }"
            @click="navigateToModule('purchase-payment')"
          >
            <span>采购付款记录</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'sales-receipt' }"
            @click="navigateToModule('sales-receipt')"
          >
            <span>销售收款记录</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'transport-payment' }"
            @click="navigateToModule('transport-payment')"
          >
            <span>运输付款记录</span>
          </div>
        </div>
      </div>
      
      <div class="nav-group">
        <div 
          class="nav-group-header" 
          @click="toggleNavGroup('invoice')"
        >
          <FileTextIcon class="nav-icon" />
          <span>发票模块</span>
          <ChevronDownIcon 
            class="nav-arrow" 
            :class="{ 'rotate-180': expandedGroups.invoice }" 
          />
        </div>
        <div 
          class="nav-group-content" 
          v-show="expandedGroups.invoice"
        >
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'purchase-invoice-in' }"
            @click="navigateToModule('purchase-invoice-in')"
          >
            <span>采购收票</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'purchase-invoice-out' }"
            @click="navigateToModule('purchase-invoice-out')"
          >
            <span>采购反向开票</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'sales-invoice' }"
            @click="navigateToModule('sales-invoice')"
          >
            <span>销售开票</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'transport-invoice' }"
            @click="navigateToModule('transport-invoice')"
          >
            <span>运输收票</span>
          </div>
        </div>
      </div>
      
      <div class="nav-group">
        <div 
          class="nav-group-header" 
          @click="toggleNavGroup('ledger')"
        >
          <BookIcon class="nav-icon" />
          <span>台账模块</span>
          <ChevronDownIcon 
            class="nav-arrow" 
            :class="{ 'rotate-180': expandedGroups.ledger }" 
          />
        </div>
        <div 
          class="nav-group-content" 
          v-show="expandedGroups.ledger"
        >
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'purchase-ledger' }"
            @click="navigateToModule('purchase-ledger')"
          >
            <span>采购台账</span>
          </div>
          <div 
            class="nav-item" 
            :class="{ active: activeModule === 'sales-ledger' }"
            @click="navigateToModule('sales-ledger')"
          >
            <span>销售台账</span>
          </div>
        </div>
      </div>
    </div>
  </aside>
        
        <!-- 仪表盘 -->
        <main class="dashboard">
          <div v-if="user.version === 'base'">
            <BaseVersionDashboard :active-module="activeModule" />
          </div>
          <div v-else>
            <NonBaseVersionDashboard :active-module="activeModule" />
          </div>
        </main>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, reactive, computed, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { useUserStore } from '@/stores/user';
  import { 
    HomeIcon, 
    ClipboardListIcon, 
    ClockIcon, 
    CheckSquareIcon,
    UserIcon,
    SettingsIcon,
    LogOutIcon,
    FileTextIcon,
    PackageIcon,
    CalculatorIcon,  // 确保这个图标被导入
    DollarSignIcon,
    BookIcon,
    ChevronDownIcon
  } from 'lucide-vue-next';
  
  import BaseVersionDashboard from './BaseVersionDashboard.vue';
  import NonBaseVersionDashboard from './NonBaseVersionDashboard.vue';
  
  const router = useRouter();
  const userStore = useUserStore();
  
  // 用户信息
  const user = computed(() => userStore.user);
  
  // 用户菜单
  const showUserMenu = ref(false);
  const toggleUserMenu = () => {
    showUserMenu.value = !showUserMenu.value;
  };
  
  // 导航项
  const navItems = [
    { key: 'home', label: '首页', icon: HomeIcon },
    { key: 'todo', label: '待办', icon: ClipboardListIcon },
    { key: 'processing', label: '办理中', icon: ClockIcon },
    { key: 'completed', label: '已办', icon: CheckSquareIcon }
  ];
  
  // 当前激活的导航项
  const activeNavItem = ref('home');
  
  // 当前激活的模块
  const activeModule = ref('');
  
  // 展开的导航组
  const expandedGroups = reactive({
    contract: true,
    goods: false,
    settlement: false,
    finance: false,
    invoice: false,
    ledger: false
  });
  
  // 切换导航组展开状态
  const toggleNavGroup = (group: string) => {
    expandedGroups[group as keyof typeof expandedGroups] = !expandedGroups[group as keyof typeof expandedGroups];
  };
  
  // 导航到模块
  const navigateToModule = (module: string) => {
    activeModule.value = module;
    // 这里可以根据需要添加路由导航或其他逻辑
    console.log('导航到模块:', module);
  };
  
  // 退出登录
  const logout = () => {
    userStore.clearUser();
    router.push('/login');
  };
  
  // 组件挂载时检查用户是否已登录
  onMounted(() => {
    if (!userStore.isLoggedIn) {
      router.push('/login');
    }
  });
  </script>
  
  <style scoped>
  .dashboard-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    background-color: #f5f7fa;
  }
  
  /* 顶栏样式 */
  .top-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 64px;
    padding: 0 1.5rem;
    background-color: #0a2463;
    color: white;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .logo-section {
    display: flex;
    align-items: center;
  }
  
  .logo {
    width: 32px;
    height: 32px;
    margin-right: 0.75rem;
  }
  
  .system-title {
    font-size: 1.25rem;
    font-weight: 600;
    margin: 0;
  }
  
  .user-section {
    position: relative;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  
  .username {
    margin-right: 0.75rem;
  }
  
  .avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .user-menu {
    position: absolute;
    top: 100%;
    right: 0;
    width: 180px;
    background-color: white;
    border-radius: 4px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    margin-top: 0.5rem;
    z-index: 100;
  }
  
  .menu-item {
    display: flex;
    align-items: center;
    padding: 0.75rem 1rem;
    color: #424242;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .menu-item:hover {
    background-color: #f5f5f5;
  }
  
  .menu-icon {
    width: 18px;
    height: 18px;
    margin-right: 0.75rem;
  }
  
  /* 导航栏样式 */
  .nav-bar {
    display: flex;
    background-color: #1e88e5;
    color: white;
    padding: 0 1.5rem;
  }
  
  .nav-item {
    display: flex;
    align-items: center;
    padding: 0.75rem 1.25rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .nav-item:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
  
  .nav-item.active {
    background-color: rgba(255, 255, 255, 0.2);
    font-weight: 500;
  }
  
  .nav-icon {
    width: 18px;
    height: 18px;
    margin-right: 0.5rem;
  }
  
  /* 内容区域样式 */
  .content-container {
    display: flex;
    flex: 1;
    overflow: hidden;
  }
  
  /* 侧边栏样式 */
  .sidebar {
    width: 280px;
    background-color: white;
    border-right: 1px solid #e0e0e0;
    overflow-y: auto;
  }
  
  .module-nav {
    display: flex;
    flex-direction: column;
  }
  
  .nav-title {
    font-size: 1.125rem;
    font-weight: 600;
    margin: 1.5rem 1.5rem 1rem;
    color: #0a2463;
  }
  
  .nav-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 0.5rem;
  }
  
  .nav-group-header {
    display: flex;
    align-items: center;
    padding: 0.75rem 1.5rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .nav-group-header:hover {
    background-color: #f5f7fa;
  }
  
  .nav-icon {
    width: 18px;
    height: 18px;
    margin-right: 0.75rem;
    color: #1e88e5;
  }
  
  .nav-arrow {
    width: 16px;
    height: 16px;
    margin-left: auto;
    color: #757575;
    transition: transform 0.3s;
  }
  
  .rotate-180 {
    transform: rotate(180deg);
  }
  
  .nav-group-content {
    display: flex;
    flex-direction: column;
    padding-left: 1rem;
  }
  
  .nav-item {
    display: flex;
    align-items: center;
    padding: 0.625rem 1.5rem 0.625rem 2.25rem;
    font-size: 0.875rem;
    color: #424242;
    cursor: pointer;
    transition: background-color 0.3s, color 0.3s;
  }
  
  .nav-item:hover {
    background-color: #f5f7fa;
    color: #1e88e5;
  }
  
  .nav-item.active {
    background-color: #e3f2fd;
    color: #1e88e5;
    font-weight: 500;
  }
  
  /* 仪表盘样式 */
  .dashboard {
    flex: 1;
    padding: 1.5rem;
    overflow-y: auto;
  }
  </style>
  
  