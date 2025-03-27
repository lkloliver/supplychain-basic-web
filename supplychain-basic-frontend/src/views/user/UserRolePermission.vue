<template>
    <div class="user-role-permission">
      <h2 class="section-title">用户角色与权限</h2>
      
      <div class="role-info">
        <div class="info-card">
          <div class="card-header">
            <UsersIcon class="card-icon" />
            <h3>当前角色</h3>
          </div>
          
          <div class="card-content">
            <div class="role-badge" :class="roleClass">
              {{ userRole }}
            </div>
            
            <div class="role-description">
              {{ roleDescription }}
            </div>
          </div>
        </div>
      </div>
      
      <div class="permission-list">
        <h3 class="list-title">权限列表</h3>
        
        <div class="permission-group" v-for="(group, index) in permissionGroups" :key="index">
          <div class="group-header">
            <component :is="group.icon" class="group-icon" />
            <span>{{ group.name }}</span>
          </div>
          
          <div class="permissions">
            <div class="permission-item" v-for="(permission, pIndex) in group.permissions" :key="pIndex">
              <CheckCircleIcon v-if="permission.granted" class="permission-icon granted" />
              <XCircleIcon v-else class="permission-icon denied" />
              <span>{{ permission.name }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="role-upgrade">
        <h3 class="upgrade-title">角色升级</h3>
        <p class="upgrade-description">
          如需升级角色获取更多权限，请联系系统管理员或提交升级申请。
        </p>
        <button class="upgrade-btn" @click="applyForUpgrade">
          <ArrowUpCircleIcon class="btn-icon" />
          申请角色升级
        </button>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { computed, ref } from 'vue';
  import { 
    UsersIcon, 
    CheckCircleIcon, 
    XCircleIcon, 
    ArrowUpCircleIcon,
    FileTextIcon,
    PackageIcon,
    CalculatorIcon,
    DollarSignIcon
  } from 'lucide-vue-next';
  
  // 模拟数据
  const userRole = ref('供应商');
  
  // 根据角色计算样式类
  const roleClass = computed(() => {
    const roleMap: Record<string, string> = {
      '管理员': 'admin',
      '操作员': 'operator',
      '供应商': 'supplier',
      '客户': 'customer'
    };
    
    return roleMap[userRole.value] || 'default';
  });
  
  // 角色描述
  const roleDescription = computed(() => {
    const descMap: Record<string, string> = {
      '管理员': '拥有系统的全部管理权限，可以管理用户、角色和所有业务数据。',
      '操作员': '可以执行日常业务操作，但无法修改系统配置和用户权限。',
      '供应商': '可以管理与自身相关的采购合同、货物和结算信息。',
      '客户': '可以查看与自身相关的销售合同、货物和结算信息。'
    };
    
    return descMap[userRole.value] || '未知角色';
  });
  
  // 权限组
  const permissionGroups = ref([
    {
      name: '合同模块',
      icon: FileTextIcon,
      permissions: [
        { name: '查看采购合同', granted: true },
        { name: '创建采购合同', granted: true },
        { name: '修改采购合同', granted: true },
        { name: '查看销售合同', granted: false },
        { name: '创建销售合同', granted: false },
        { name: '查看运输合同', granted: true }
      ]
    },
    {
      name: '货物模块',
      icon: PackageIcon,
      permissions: [
        { name: '查看入库记录', granted: true },
        { name: '创建入库记录', granted: true },
        { name: '查看出库记录', granted: false },
        { name: '创建出库记录', granted: false }
      ]
    },
    {
      name: '结算模块',
      icon: CalculatorIcon,
      permissions: [
        { name: '查看采购结算', granted: true },
        { name: '创建采购结算', granted: true },
        { name: '查看销售结算', granted: false },
        { name: '创建销售结算', granted: false }
      ]
    },
    {
      name: '资金模块',
      icon: DollarSignIcon,
      permissions: [
        { name: '查看采购付款', granted: true },
        { name: '创建采购付款', granted: false },
        { name: '查看销售收款', granted: false },
        { name: '创建销售收款', granted: false }
      ]
    }
  ]);
  
  // 申请角色升级
  const applyForUpgrade = () => {
    alert('角色升级申请功能待实现');
  };
  </script>
  
  <style scoped>
  .user-role-permission {
    padding: 10px 0;
  }
  
  .section-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 20px;
    color: #333;
  }
  
  .role-info {
    margin-bottom: 24px;
  }
  
  .info-card {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }
  
  .card-header {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
  }
  
  .card-icon {
    width: 20px;
    height: 20px;
    margin-right: 8px;
    color: #1890ff;
  }
  
  .card-header h3 {
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }
  
  .role-badge {
    display: inline-block;
    padding: 4px 12px;
    border-radius: 16px;
    font-size: 14px;
    font-weight: 500;
    margin-bottom: 12px;
  }
  
  .role-badge.admin {
    background-color: #f6ffed;
    color: #52c41a;
    border: 1px solid #b7eb8f;
  }
  
  .role-badge.operator {
    background-color: #e6f7ff;
    color: #1890ff;
    border: 1px solid #91d5ff;
  }
  
  .role-badge.supplier {
    background-color: #fff7e6;
    color: #fa8c16;
    border: 1px solid #ffd591;
  }
  
  .role-badge.customer {
    background-color: #f9f0ff;
    color: #722ed1;
    border: 1px solid #d3adf7;
  }
  
  .role-badge.default {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #d9d9d9;
  }
  
  .role-description {
    color: #666;
    line-height: 1.5;
  }
  
  .permission-list {
    margin-bottom: 24px;
  }
  
  .list-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 16px;
    color: #333;
  }
  
  .permission-group {
    background-color: #f9f9f9;
    border-radius: 8px;
    margin-bottom: 16px;
    overflow: hidden;
  }
  
  .group-header {
    display: flex;
    align-items: center;
    padding: 12px 16px;
    background-color: #f0f0f0;
    font-weight: 600;
    color: #333;
  }
  
  .group-icon {
    width: 18px;
    height: 18px;
    margin-right: 8px;
    color: #1890ff;
  }
  
  .permissions {
    padding: 12px 16px;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 12px;
  }
  
  .permission-item {
    display: flex;
    align-items: center;
  }
  
  .permission-icon {
    width: 16px;
    height: 16px;
    margin-right: 8px;
  }
  
  .permission-icon.granted {
    color: #52c41a;
  }
  
  .permission-icon.denied {
    color: #f5222d;
  }
  
  .role-upgrade {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 20px;
  }
  
  .upgrade-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 12px;
    color: #333;
  }
  
  .upgrade-description {
    color: #666;
    margin-bottom: 16px;
    line-height: 1.5;
  }
  
  .upgrade-btn {
    display: flex;
    align-items: center;
    padding: 8px 16px;
    background-color: #1890ff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.3s;
  }
  
  .upgrade-btn:hover {
    background-color: #40a9ff;
  }
  
  .btn-icon {
    width: 16px;
    height: 16px;
    margin-right: 8px;
  }
  </style>
  
  