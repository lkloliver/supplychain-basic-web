<template>
    <div class="authentication-center">
      <h2 class="section-title">存证主体认证</h2>
      
      <div class="auth-status">
        <div class="status-card" :class="{ 'verified': isVerified }">
          <div class="status-icon">
            <CheckCircleIcon v-if="isVerified" class="icon verified" />
            <AlertCircleIcon v-else class="icon unverified" />
          </div>
          <div class="status-info">
            <h3>{{ isVerified ? '已完成认证' : '未完成认证' }}</h3>
            <p>{{ isVerified ? '您的账户已完成存证主体认证，可以进行区块链存证操作。' : '请完成认证，以便在区块链上进行可信存证。' }}</p>
          </div>
          <button v-if="!isVerified" class="verify-btn" @click="startVerification">
            立即认证
          </button>
          <button v-else class="reverify-btn" @click="startVerification">
            重新认证
          </button>
        </div>
      </div>
      
      <div v-if="isVerified" class="auth-info">
        <h3 class="info-title">认证信息</h3>
        
        <div class="info-card">
          <div class="info-item">
            <span class="label">认证类型：</span>
            <span class="value">{{ getAuthTypeName(authInfo.type) }}</span>
          </div>
          <div class="info-item">
            <span class="label">认证方式：</span>
            <span class="value">{{ getAuthMethodName(authInfo.method) }}</span>
          </div>
          <div class="info-item">
            <span class="label">认证时间：</span>
            <span class="value">{{ authInfo.verificationTime }}</span>
          </div>
          <div class="info-item">
            <span class="label">认证有效期：</span>
            <span class="value">{{ authInfo.validUntil }}</span>
          </div>
          <div class="info-item">
            <span class="label">区块链存证地址：</span>
            <div class="value-container">
              <span class="value">{{ authInfo.blockchainAddress }}</span>
              <button class="action-btn" @click="copyAddress">复制</button>
            </div>
          </div>
        </div>
      </div>
      
      <div v-if="showAuthForm" class="auth-form-container">
        <AuthenticationForm
          @submit="handleAuthSubmit"
          @cancel="showAuthForm = false"
        />
      </div>
      
      <div v-if="!showAuthForm" class="verification-steps">
        <h3 class="steps-title">认证流程</h3>
        
        <div class="step-list">
          <div class="step-item" v-for="(step, index) in verificationSteps" :key="index">
            <div class="step-number">{{ index + 1 }}</div>
            <div class="step-content">
              <h4>{{ step.title }}</h4>
              <p>{{ step.description }}</p>
            </div>
          </div>
        </div>
      </div>
      
      <div class="auth-benefits">
        <h3 class="benefits-title">
          <LightbulbIcon class="benefits-icon" />
          认证优势
        </h3>
        
        <div class="benefits-list">
          <div class="benefit-item" v-for="(benefit, index) in benefits" :key="index">
            <component :is="benefit.icon" class="benefit-icon" />
            <div class="benefit-content">
              <h4>{{ benefit.title }}</h4>
              <p>{{ benefit.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue';
  import { 
    CheckCircleIcon, 
    AlertCircleIcon, 
    LightbulbIcon,
    ShieldIcon,
    TrendingUpIcon,
    FileTextIcon,
    AwardIcon
  } from 'lucide-vue-next';
  import AuthenticationForm from '@/components/forms/AuthenticationForm.vue';
  
  // 认证状态
  const isVerified = ref(true);
  
  // 认证信息
  const authInfo = ref({
    type: 'natural',
    method: 'biometric',
    verificationTime: '2023-06-15 14:30:22',
    validUntil: '2024-06-15',
    blockchainAddress: '0x7a58c0be72be218b41c608b7fe7c5bb630736c71'
  });
  
  // 显示认证表单
  const showAuthForm = ref(false);
  
  // 认证步骤
  const verificationSteps = [
    {
      title: '选择认证类型',
      description: '根据您的身份选择相应的认证类型：自然人、个体工商户或企业法人。'
    },
    {
      title: '填写认证信息',
      description: '根据选择的认证类型，填写相应的认证信息，包括基本信息、证件信息等。'
    },
    {
      title: '选择认证方式',
      description: '根据认证类型选择相应的认证方式，包括生物特征识别、国家统一身份认证平台或证件证照对比。'
    },
    {
      title: '提交认证',
      description: '确认信息无误后提交认证申请，系统将在1-3个工作日内完成审核。'
    },
    {
      title: '完成认证',
      description: '审核通过后，系统将自动为您生成区块链存证证书。'
    }
  ];
  
  // 认证优势
  const benefits = [
    {
      title: '数据可信',
      description: '通过区块链技术确保数据不可篡改，提高数据可信度。',
      icon: ShieldIcon
    },
    {
      title: '效率提升',
      description: '简化业务流程，减少中间环节，提高交易效率。',
      icon: TrendingUpIcon
    },
    {
      title: '法律效力',
      description: '区块链存证具有法律效力，可用于解决商业纠纷。',
      icon: FileTextIcon
    },
    {
      title: '品牌信誉',
      description: '提升企业在供应链中的信誉度，增强合作伙伴信任。',
      icon: AwardIcon
    }
  ];
  
  // 获取认证类型名称
  const getAuthTypeName = (type: string) => {
    const types: Record<string, string> = {
      natural: '自然人',
      individual: '个体工商户',
      enterprise: '企业法人'
    };
    return types[type] || type;
  };
  
  // 获取认证方式名称
  const getAuthMethodName = (method: string) => {
    const methods: Record<string, string> = {
      biometric: '生物特征识别',
      national: '国家统一身份认证平台',
      document: '证件证照对比'
    };
    return methods[method] || method;
  };
  
  // 开始认证
  const startVerification = () => {
    showAuthForm.value = true;
  };
  
  // 处理认证提交
  const handleAuthSubmit = async (data: any) => {
    try {
      // TODO: 调用认证API
      console.log('认证数据：', data);
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // 更新认证状态和信息
      isVerified.value = true;
      authInfo.value = {
        ...authInfo.value,
        type: data.authType,
        method: data.authMethod,
        verificationTime: new Date().toLocaleString(),
        validUntil: new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toLocaleDateString()
      };
      
      showAuthForm.value = false;
      alert('认证申请已提交，请等待审核。');
    } catch (error) {
      console.error('认证提交失败：', error);
      alert('认证提交失败，请重试。');
    }
  };
  
  // 复制地址
  const copyAddress = () => {
    navigator.clipboard.writeText(authInfo.value.blockchainAddress)
      .then(() => {
        alert('地址已复制到剪贴板');
      })
      .catch(err => {
        console.error('复制失败:', err);
      });
  };
  </script>
  
  <style scoped>
  .authentication-center {
    padding: 10px 0;
  }
  
  .section-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 20px;
    color: #333;
  }
  
  .auth-status {
    margin-bottom: 24px;
  }
  
  .status-card {
    display: flex;
    align-items: center;
    background-color: #fff7e6;
    border: 1px solid #ffd591;
    border-radius: 8px;
    padding: 20px;
  }
  
  .status-card.verified {
    background-color: #f6ffed;
    border-color: #b7eb8f;
  }
  
  .status-icon {
    margin-right: 16px;
  }
  
  .icon {
    width: 40px;
    height: 40px;
  }
  
  .icon.verified {
    color: #52c41a;
  }
  
  .icon.unverified {
    color: #fa8c16;
  }
  
  .status-info {
    flex: 1;
  }
  
  .status-info h3 {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 4px;
    color: #333;
  }
  
  .status-info p {
    color: #666;
    margin: 0;
  }
  
  .verify-btn,
  .reverify-btn {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.3s;
  }
  
  .verify-btn {
    background-color: #fa8c16;
    color: white;
  }
  
  .verify-btn:hover {
    background-color: #ffa940;
  }
  
  .reverify-btn {
    background-color: #52c41a;
    color: white;
  }
  
  .reverify-btn:hover {
    background-color: #73d13d;
  }
  
  .auth-info {
    margin-bottom: 24px;
  }
  
  .info-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 16px;
    color: #333;
  }
  
  .info-card {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 16px;
  }
  
  .info-item {
    display: flex;
    margin-bottom: 12px;
  }
  
  .info-item:last-child {
    margin-bottom: 0;
  }
  
  .label {
    width: 150px;
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
  
  .auth-form-container {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 24px;
  }
  
  .verification-steps {
    margin-bottom: 24px;
  }
  
  .steps-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 16px;
    color: #333;
  }
  
  .step-list {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 16px;
  }
  
  .step-item {
    display: flex;
    margin-bottom: 16px;
  }
  
  .step-item:last-child {
    margin-bottom: 0;
  }
  
  .step-number {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    background-color: #1890ff;
    color: white;
    border-radius: 50%;
    font-weight: 600;
    margin-right: 12px;
    flex-shrink: 0;
  }
  
  .step-content h4 {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 4px;
    color: #333;
  }
  
  .step-content p {
    color: #666;
    margin: 0;
    font-size: 14px;
  }
  
  .auth-benefits {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 20px;
  }
  
  .benefits-title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 16px;
    color: #333;
  }
  
  .benefits-icon {
    width: 20px;
    height: 20px;
    margin-right: 8px;
    color: #1890ff;
  }
  
  .benefits-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 16px;
  }
  
  .benefit-item {
    display: flex;
    align-items: flex-start;
  }
  
  .benefit-icon {
    width: 18px;
    height: 18px;
    margin-right: 8px;
    margin-top: 2px;
    color: #1890ff;
  }
  
  .benefit-content h4 {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 4px;
    color: #333;
  }
  
  .benefit-content p {
    color: #666;
    margin: 0;
    font-size: 14px;
    line-height: 1.5;
  }
  </style>
  
  