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
          <div class="menu-item" @click="goToUserCenter">
            <UserIcon class="menu-icon" />
            <span>个人中心</span>
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
      <router-link 
        v-for="(item, index) in navItems" 
        :key="index"
        :to="item.route"
        custom
        v-slot="{ navigate, isActive }"
      >
        <div 
          :class="['nav-item', { active: isActive }]"
          @click="navigate"
        >
          <component :is="item.icon" class="nav-icon" />
          <span>{{ item.label }}</span>
        </div>
      </router-link>
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
              <router-link 
                :to="{ name: 'PurchaseContract' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>采购合同</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'SalesContract' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>销售合同</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'TransportContract' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>运输合同</span>
                </div>
              </router-link>
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
                <router-link 
                  :to="{ name: 'InboundWeight' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>入库过磅单</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'InboundQuality' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>入库质检单</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'InboundImage' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>入库图片</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'OutboundWeight' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>出库过磅单</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'OutboundQuality' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>出库质检单</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'OutboundImage' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>出库图片</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'PurchaseWaybill' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>采购运单</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'SalesWaybill' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>销售运单</span>
                  </div>
                </router-link>
              </template>
              <template v-else>
                <router-link 
                  :to="{ name: 'FactoryWeight' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>到货厂家过磅单</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'FactoryQuality' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>到货厂家质检单</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'FactoryImage' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>到货厂家图片</span>
                  </div>
                </router-link>
                <router-link 
                  :to="{ name: 'Waybill' }"
                  custom
                  v-slot="{ navigate, isActive }"
                >
                  <div 
                    class="nav-item" 
                    :class="{ active: isActive }"
                    @click="navigate"
                  >
                    <span>运单</span>
                  </div>
                </router-link>
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
              <router-link 
                :to="{ name: 'PurchaseSettlement' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>采购结算</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'SalesSettlement' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>销售结算</span>
                </div>
              </router-link>
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
              <router-link 
                :to="{ name: 'PurchasePayment' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>采购付款</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'SalesReceipt' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>销售收款</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'TransportPayment' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>运输付款</span>
                </div>
              </router-link>
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
              <router-link 
                :to="{ name: 'PurchaseInvoiceIn' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>采购进项发票</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'PurchaseInvoiceOut' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>采购销项发票</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'SalesInvoice' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>销售发票</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'TransportInvoice' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>运输发票</span>
                </div>
              </router-link>
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
              <router-link 
                :to="{ name: 'PurchaseLedger' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>采购台账</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'SalesLedger' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>销售台账</span>
                </div>
              </router-link>
            </div>
          </div>
          
          <div class="nav-group" v-if="isAdmin">
            <div 
              class="nav-group-header" 
              @click="toggleNavGroup('access')"
            >
              <ShieldIcon class="nav-icon" />
              <span>访问控制</span>
              <ChevronDownIcon 
                class="nav-arrow" 
                :class="{ 'rotate-180': expandedGroups.access }" 
              />
            </div>
            <div 
              class="nav-group-content" 
              v-show="expandedGroups.access"
            >
              <router-link 
                :to="{ name: 'UserManagement' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>用户管理</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'DepartmentManagement' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>部门管理</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'RoleManagement' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>角色管理</span>
                </div>
              </router-link>
              <router-link 
                :to="{ name: 'PermissionManagement' }"
                custom
                v-slot="{ navigate, isActive }"
              >
                <div 
                  class="nav-item" 
                  :class="{ active: isActive }"
                  @click="navigate"
                >
                  <span>权限管理</span>
                </div>
              </router-link>
            </div>
          </div>
        </div>
      </aside>
      
      <!-- 主内容区域 - 路由视图 -->
      <main class="dashboard">
        <router-view :key="$route.fullPath"></router-view>
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
  CalculatorIcon,
  DollarSignIcon,
  BookIcon,
  ChevronDownIcon,
  ImageIcon,
  ClipboardCheckIcon,
  ScaleIcon,
  TruckIcon,
  ShieldIcon,  // 添加访问控制图标
  UsersIcon,    // 添加用户图标
  BriefcaseIcon // 添加部门图标
} from 'lucide-vue-next';

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
  { key: 'home', label: '首页', icon: HomeIcon, route: { name: 'DashboardHome' } },
  { key: 'todo', label: '待办', icon: ClipboardListIcon, route: { path: '/dashboard/todo' } },
  { key: 'processing', label: '办理中', icon: ClockIcon, route: { path: '/dashboard/processing' } },
  { key: 'completed', label: '已办', icon: CheckSquareIcon, route: { path: '/dashboard/completed' } }
];

// 展开的导航组
const expandedGroups = reactive({
  contract: true,
  goods: false,
  settlement: false,
  finance: false,
  invoice: false,
  ledger: false,
  access: false  // 添加访问控制组
});

// 添加isAdmin计算属性
const isAdmin = computed(() => userStore.isAdmin);

// 切换导航组展开状态
const toggleNavGroup = (group: string) => {
  expandedGroups[group as keyof typeof expandedGroups] = !expandedGroups[group as keyof typeof expandedGroups];
};

// 退出登录
const logout = () => {
  userStore.clearUser();
  router.push('/login');
};

// 跳转到个人中心
const goToUserCenter = () => {
  router.push('/dashboard/user-center');
  showUserMenu.value = false;
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
  height: 100vh;
  overflow: hidden;
}

/* 顶栏样式 */
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #ffffff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.logo-section {
  display: flex;
  align-items: center;
}

.logo {
  height: 40px;
  width: auto;
  margin-right: 10px;
}

.system-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.user-section {
  position: relative;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f5f5f5;
}

.username {
  margin-right: 10px;
  font-size: 14px;
  color: #333;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.user-menu {
  position: absolute;
  top: 100%;
  right: 0;
  width: 180px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
  margin-top: 5px;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.menu-item:hover {
  background-color: #f5f5f5;
}

.menu-icon {
  width: 16px;
  height: 16px;
  margin-right: 10px;
  color: #666;
}

/* 导航栏样式 */
.nav-bar {
  display: flex;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e0e0e0;
  padding: 0 20px;
  height: 50px;
  overflow-x: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 0 15px;
  height: 100%;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
  color: #666;
}

.nav-item:hover {
  color: #1890ff;
}

.nav-item.active {
  color: #1890ff;
  border-bottom: 2px solid #1890ff;
}

.nav-icon {
  width: 18px;
  height: 18px;
  margin-right: 8px;
}

/* 内容容器样式 */
.content-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 侧边栏样式 */
.sidebar {
  width: 240px;
  background-color: #f5f7fa;
  border-right: 1px solid #e0e0e0;
  overflow-y: auto;
  flex-shrink: 0;
}

.module-nav {
  padding: 15px 0;
}

.nav-title {
  padding: 0 20px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #999;
  font-weight: 600;
}

.nav-group {
  margin-bottom: 5px;
}

.nav-group-header {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.2s;
  color: #333;
}

.nav-group-header:hover {
  background-color: #e6f7ff;
}

.nav-group-content {
  padding: 5px 0;
}

.nav-group-content .nav-item {
  padding: 8px 20px 8px 48px;
  height: auto;
  color: #666;
}

.nav-group-content .nav-item:hover {
  background-color: #e6f7ff;
  color: #1890ff;
}

.nav-group-content .nav-item.active {
  background-color: #e6f7ff;
  color: #1890ff;
  border-right: 3px solid #1890ff;
  border-bottom: none;
}

.nav-arrow {
  width: 16px;
  height: 16px;
  margin-left: auto;
  transition: transform 0.2s;
}

.rotate-180 {
  transform: rotate(180deg);
}

/* 主内容区域样式 */
.dashboard {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f0f2f5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    width: 200px;
  }
  
  .system-title {
    display: none;
  }
}

@media (max-width: 576px) {
  .sidebar {
    position: fixed;
    left: -240px;
    top: 110px;
    bottom: 0;
    z-index: 100;
    transition: left 0.3s;
  }
  
  .sidebar.open {
    left: 0;
  }
  
  .nav-bar {
    padding: 0 10px;
  }
  
  .nav-item {
    padding: 0 10px;
  }
}
</style>

