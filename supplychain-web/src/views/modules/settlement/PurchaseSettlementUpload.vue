<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="采购结算上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/settlement/purchase/upload"
      manageRouteName="/dashboard/settlement/purchase/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <!-- 基本信息 -->
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">结算单编号</label>
            <input 
              v-model="detailForm.settlementNo" 
              type="text" 
              placeholder="请输入结算单编号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">结算总金额(元)</label>
            <input 
              v-model="detailForm.totalAmount" 
              type="number" 
              step="0.01"
              placeholder="请输入结算总金额"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">供货方</label>
            <input 
              v-model="detailForm.supplier" 
              type="text" 
              placeholder="请输入供货方"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">购货方</label>
            <input 
              v-model="detailForm.buyer" 
              type="text" 
              placeholder="请输入购货方"
              class="form-input"
            />
          </div>
        </div>

        <!-- 结算明细 -->
        <div class="form-array-group">
          <div class="array-header">
            <h4 class="array-title">结算明细</h4>
          </div>
          
          <div class="array-items">
            <div class="array-header">
              <h4 class="array-title">结算明细列表</h4>
              <button type="button" class="add-item-btn" @click="addDetail">
                <PlusIcon class="btn-icon" />
                添加明细
              </button>
            </div>
            <div v-for="(detail, index) in detailForm.details" :key="index" class="array-item">
              <div class="array-item-header">
                <span class="array-item-title">明细 #{{ index + 1 }}</span>
                <button type="button" class="remove-item-btn" @click="removeDetail(index)">
                  <XIcon class="btn-icon" />
                </button>
              </div>
              <div class="array-item-content">
                <div class="form-grid">
                  <div class="form-group">
                    <label class="form-label">日期</label>
                    <input 
                      v-model="detail.date" 
                      type="date" 
                      class="form-input"
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">车号</label>
                    <input 
                      v-model="detail.vehicleNo" 
                      type="text" 
                      placeholder="请输入车号"
                      class="form-input"
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">毛重(kg)</label>
                    <input 
                      v-model="detail.grossWeight" 
                      type="number" 
                      step="0.01"
                      placeholder="请输入毛重"
                      class="form-input"
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">皮重(kg)</label>
                    <input 
                      v-model="detail.tareWeight" 
                      type="number" 
                      step="0.01"
                      placeholder="请输入皮重"
                      class="form-input"
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">净重(kg)</label>
                    <input 
                      v-model="detail.netWeight" 
                      type="number" 
                      step="0.01"
                      placeholder="净重"
                      class="form-input"
                      readonly
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">结重(kg)</label>
                    <input 
                      v-model="detail.finalWeight" 
                      type="number" 
                      step="0.01"
                      placeholder="请输入结重"
                      class="form-input"
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">结算单价(元/kg)</label>
                    <input 
                      v-model="detail.unitPrice" 
                      type="number" 
                      step="0.01"
                      placeholder="结算单价"
                      class="form-input"
                      readonly
                    />
                  </div>
                  <div class="form-group">
                    <label class="form-label">结算金额(元)</label>
                    <input 
                      v-model="detail.amount" 
                      type="number" 
                      step="0.01"
                      placeholder="请输入结算金额"
                      class="form-input"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div v-if="detailForm.details.length === 0" class="empty-array-message">
              暂无结算明细，请点击上方按钮添加
            </div>
          </div>
        </div>

        <!-- 按钮组 -->
        <div class="form-actions">
          <button type="button" class="action-btn secondary" @click="handleAIAutoFill">
            <SparklesIcon class="btn-icon" />
            AI自动识别
          </button>
          <button type="submit" class="action-btn primary">
            提交详情信息
          </button>
        </div>
      </form>
    </DocumentUploadTemplate>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { SparklesIcon, PlusIcon, XIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import '@/assets/styles/form.css';

interface SettlementDetail {
  date: string;
  vehicleNo: string;
  grossWeight: number;
  tareWeight: number;
  netWeight: number;
  finalWeight: number;
  unitPrice: number;
  amount: number;
}

interface DetailForm {
  settlementNo: string;
  totalAmount: number;
  supplier: string;
  buyer: string;
  details: SettlementDetail[];
}

// 表单数据
const detailForm = ref<DetailForm>({
  settlementNo: '',
  totalAmount: 0,
  supplier: '',
  buyer: '',
  details: []
});

// 添加明细
const addDetail = () => {
  detailForm.value.details.push({
    date: '',
    vehicleNo: '',
    grossWeight: 0,
    tareWeight: 0,
    netWeight: 0,
    finalWeight: 0,
    unitPrice: 0,
    amount: 0
  });
};

// 删除明细
const removeDetail = (index: number) => {
  detailForm.value.details.splice(index, 1);
};

// 监听明细变化，自动计算净重和结算金额
watch(
  () => detailForm.value.details,
  (details) => {
    details.forEach(detail => {
      // 计算净重
      detail.netWeight = detail.grossWeight - detail.tareWeight;
      // 计算结算单价
      if (detail.finalWeight > 0) {
        detail.unitPrice = detail.amount / detail.finalWeight;
      }
    });
    // 计算总金额
    detailForm.value.totalAmount = details.reduce((sum, detail) => sum + detail.amount, 0);
  },
  { deep: true }
);

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'purchase_settlement');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'purchase_settlement'
      }
    };
    return result;
  } catch (error) {
    throw error;
  }
}

// 处理上传成功
const handleUploadSuccess = (result: any) => {
  if (result.success && result.fileId) {
    // 文件上传成功后，模板组件会自动切换到下一步
  }
}

// 处理取消上传
const handleCancelUpload = async () => {
  // TODO: 调用删除接口
  console.log('取消上传');
}

// AI自动识别
const handleAIAutoFill = async () => {
  try {
    // TODO: 调用AI识别接口
    console.log('开始AI自动识别...');
  } catch (error) {
    console.error('AI识别失败：', error);
  }
}

// 提交详情信息
const handleDetailSubmit = async () => {
  try {
    // TODO: 调用API保存详情信息
    console.log('提交详情信息：', detailForm.value);
  } catch (error) {
    console.error('提交详情信息失败：', error);
  }
};
</script>
  
  