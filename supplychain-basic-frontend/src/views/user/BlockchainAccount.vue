<template>
    <div class="blockchain-account">
      <h2 class="section-title">区块链账户</h2>
      
      <div class="account-info">
        <div class="info-card">
          <div class="card-header">
            <KeyIcon class="card-icon" />
            <h3>账户信息</h3>
          </div>
          
          <div class="card-content">
            <div class="info-item">
              <span class="label">账户地址：</span>
              <div class="value-container">
                <span class="value">{{ accountAddress || '未绑定区块链账户' }}</span>
                <button v-if="!accountAddress" class="action-btn" @click="bindAccount">绑定账户</button>
                <button v-else class="action-btn" @click="copyAddress">复制</button>
              </div>
            </div>
            
            <div class="info-item">
              <span class="label">账户状态：</span>
              <span class="value status" :class="accountStatus ? 'active' : 'inactive'">
                {{ accountStatus ? '已激活' : '未激活' }}
              </span>
            </div>
            
            <div class="info-item">
              <span class="label">绑定时间：</span>
              <span class="value">{{ bindTime || '—' }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="account-actions">
        <button class="primary-btn" :disabled="!accountAddress" @click="regenerateAccount">
          <RefreshCwIcon class="btn-icon" />
          重新生成账户
        </button>
        <button class="secondary-btn" :disabled="!accountAddress" @click="exportPrivateKey">
          <DownloadIcon class="btn-icon" />
          导出私钥
        </button>
      </div>
      
      <div class="account-guide">
        <h3 class="guide-title">
          <InfoIcon class="guide-icon" />
          区块链账户说明
        </h3>
        <div class="guide-content">
          <p>区块链账户是您在区块链网络上的身份标识，用于进行数据存证、交易确认等操作。</p>
          <p>请妥善保管您的账户私钥，不要泄露给他人，以免造成资产损失。</p>
          <p>如需帮助，请联系系统管理员。</p>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue';
  import { KeyIcon, RefreshCwIcon, DownloadIcon, InfoIcon } from 'lucide-vue-next';
  
  // 模拟数据
  const accountAddress = ref('0x7a58c0be72be218b41c608b7fe7c5bb630736c71');
  const accountStatus = ref(true);
  const bindTime = ref('2023-06-15 14:30:22');
  
  // 绑定账户
  const bindAccount = () => {
    // 实现绑定账户的逻辑
    alert('绑定账户功能待实现');
  };
  
  // 复制地址
  const copyAddress = () => {
    navigator.clipboard.writeText(accountAddress.value)
      .then(() => {
        alert('地址已复制到剪贴板');
      })
      .catch(err => {
        console.error('复制失败:', err);
      });
  };
  
  // 重新生成账户
  const regenerateAccount = () => {
    // 实现重新生成账户的逻辑
    alert('重新生成账户功能待实现');
  };
  
  // 导出私钥
  const exportPrivateKey = () => {
    // 实现导出私钥的逻辑
    alert('导出私钥功能待实现');
  };
  </script>
  
  <style scoped>
  .blockchain-account {
    padding: 10px 0;
  }
  
  .section-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 20px;
    color: #333;
  }
  
  .account-info {
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
  
  .info-item {
    display: flex;
    margin-bottom: 12px;
  }
  
  .label {
    width: 100px;
    color: #666;
    flex-shrink: 0;
  }
  
  .value-container {
    display: flex;
    align-items: center;
    flex: 1;
  }
  
  .value {
    color: #333;
    word-break: break-all;
  }
  
  .status {
    padding: 2px 8px;
    border-radius: 10px;
    font-size: 12px;
  }
  
  .status.active {
    background-color: #e6f7ff;
    color: #1890ff;
  }
  
  .status.inactive {
    background-color: #fff1f0;
    color: #f5222d;
  }
  
  .action-btn {
    margin-left: 10px;
    padding: 2px 8px;
    background-color: #f0f0f0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    color: #666;
  }
  
  .action-btn:hover {
    background-color: #e0e0e0;
  }
  
  .account-actions {
    display: flex;
    gap: 12px;
    margin-bottom: 24px;
  }
  
  .primary-btn, .secondary-btn {
    display: flex;
    align-items: center;
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.3s;
  }
  
  .primary-btn {
    background-color: #1890ff;
    color: white;
  }
  
  .primary-btn:hover {
    background-color: #40a9ff;
  }
  
  .secondary-btn {
    background-color: #f0f0f0;
    color: #666;
  }
  
  .secondary-btn:hover {
    background-color: #e0e0e0;
  }
  
  .primary-btn:disabled, .secondary-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  
  .btn-icon {
    width: 16px;
    height: 16px;
    margin-right: 8px;
  }
  
  .account-guide {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 16px;
  }
  
  .guide-title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 12px;
    color: #333;
  }
  
  .guide-icon {
    width: 18px;
    height: 18px;
    margin-right: 8px;
    color: #1890ff;
  }
  
  .guide-content p {
    margin-bottom: 8px;
    color: #666;
    line-height: 1.5;
  }
  </style>
  
  