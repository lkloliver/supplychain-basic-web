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
            <p>{{ isVerified ? '您的企业已完成存证主体认证，可以进行区块链存证操作。' : '请完成企业认证，以便在区块链上进行可信存证。' }}</p>
          </div>
          <button v-if="!isVerified" class="verify-btn" @click="startVerification">
            立即认证
          </button>
          <button v-else class="view-btn" @click="viewCertificate">
            查看证书
          </button>
        </div>
      </div>
      
      <div class="verification-steps" v-if="!isVerified">
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
      
      <div class="auth-info" v-if="isVerified">
        <h3 class="info-title">认证信息</h3>
        
        <div class="info-card">
          <div class="info-item">
            <span class="label">企业名称：</span>
            <span class="value">{{ companyInfo.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">统一社会信用代码：</span>
            <span class="value">{{ companyInfo.creditCode }}</span>
          </div>
          <div class="info-item">
            <span class="label">法定代表人：</span>
            <span class="value">{{ companyInfo.legalPerson }}</span>
          </div>
          <div class="info-item">
            <span class="label">认证时间：</span>
            <span class="value">{{ companyInfo.verificationTime }}</span>
          </div>
          <div class="info-item">
            <span class="label">认证有效期：</span>
            <span class="value">{{ companyInfo.validUntil }}</span>
          </div>
          <div class="info-item">
            <span class="label">区块链存证地址：</span>
            <div class="value-container">
              <span class="value">{{ companyInfo.blockchainAddress }}</span>
              <button class="action-btn" @click="copyAddress">复制</button>
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
  
  // 认证状态
  const isVerified = ref(true);
  
  // 认证步骤
  const verificationSteps = [
    {
      title: '提交企业信息',
      description: '填写企业基本信息，包括企业名称、统一社会信用代码、法定代表人等。'
    },
    {
      title: '上传证明材料',
      description: '上传营业执照、法定代表人身份证等证明材料。'
    },
    {
      title: '等待审核',
      description: '系统将在1-3个工作日内完成审核，审核结果将通过系统消息通知。'
    },
    {
      title: '完成认证',
      description: '审核通过后，系统将自动为企业生成区块链存证证书。'
    }
  ];
  
  // 企业信息
  const companyInfo = ref({
    name: '示例科技有限公司',
    creditCode: '91110105MA00B7GT2R',
    legalPerson: '张三',
    verificationTime: '2023-05-20 10:30:45',
    validUntil: '2024-05-20',
    blockchainAddress: '0x8f5b2b7c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a'
  });
  
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
  
  // 开始认证
  const startVerification = () => {
    alert('认证功能待实现');
  };
  
  // 查看证书
  const viewCertificate = () => {
    alert('查看证书功能待实现');
  };
  
  // 复制地址
  const copyAddress = () => {
    navigator.clipboard.writeText(companyInfo.value.blockchainAddress)
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
  
  .verify-btn, .view-btn {
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
  
  .view-btn {
    background-color: #52c41a;
    color: white;
  }
  
  .view-btn:hover {
    background-color: #73d13d;
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
  
  