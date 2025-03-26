<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="入库质检单上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/goods/base/inbound-quality/upload"
      manageRouteName="/dashboard/goods/base/inbound-quality/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">计量号</label>
            <input 
              v-model="detailForm.measurementNo" 
              type="text" 
              placeholder="请输入计量号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">扣杂(%)</label>
            <input 
              v-model="detailForm.deductionRate" 
              type="number" 
              step="0.01"
              placeholder="请输入扣杂比例"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">结重(kg)</label>
            <input 
              v-model="detailForm.finalWeight" 
              type="number" 
              step="0.01"
              placeholder="请输入结重"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">放射性检测</label>
            <select v-model="detailForm.radiationTest" class="form-input">
              <option value="">请选择检测结果</option>
              <option value="pass">合格</option>
              <option value="fail">不合格</option>
            </select>
          </div>
        </div>

        <!-- 质检明细 -->
        <div class="form-array-group">
          <div class="array-header">
            <h4 class="array-title">质检明细</h4>
          </div>
          
          <!-- 品名列表 -->
          <div class="array-items">
            <div class="array-header">
              <h4 class="array-title">品名</h4>
              <button type="button" class="add-item-btn" @click="addProduct">
                <PlusIcon class="btn-icon" />
                添加品名
              </button>
            </div>
            <div v-for="(product, index) in detailForm.products" :key="index" class="array-item">
              <div class="array-item-header">
                <span class="array-item-title">品名 #{{ index + 1 }}</span>
                <button type="button" class="remove-item-btn" @click="removeProduct(index)">
                  <XIcon class="btn-icon" />
                </button>
              </div>
              <div class="array-item-content">
                <div class="form-grid">
                  <div class="form-group">
                    <label class="form-label">品名</label>
                    <input v-model="product.name" type="text" placeholder="请输入品名" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">占比(%)</label>
                    <input v-model="product.percentage" type="number" step="0.01" placeholder="请输入占比" class="form-input" />
                  </div>
                </div>
              </div>
            </div>
            <div v-if="detailForm.products.length === 0" class="empty-array-message">
              暂无品名，请点击上方按钮添加
            </div>
          </div>

          <!-- 处置物列表 -->
          <div class="array-items">
            <div class="array-header">
              <h4 class="array-title">处置物</h4>
              <button type="button" class="add-item-btn" @click="addDisposal">
                <PlusIcon class="btn-icon" />
                添加处置物
              </button>
            </div>
            <div v-for="(disposal, index) in detailForm.disposals" :key="index" class="array-item">
              <div class="array-item-header">
                <span class="array-item-title">处置物 #{{ index + 1 }}</span>
                <button type="button" class="remove-item-btn" @click="removeDisposal(index)">
                  <XIcon class="btn-icon" />
                </button>
              </div>
              <div class="array-item-content">
                <div class="form-grid">
                  <div class="form-group">
                    <label class="form-label">处置类别</label>
                    <select v-model="disposal.type" class="form-input">
                      <option value="">请选择处置类别</option>
                      <option value="recycle">回收</option>
                      <option value="dispose">处置</option>
                      <option value="return">退回</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label class="form-label">处置物名称</label>
                    <input v-model="disposal.name" type="text" placeholder="请输入处置物名称" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">数量(kg)</label>
                    <input v-model="disposal.quantity" type="number" step="0.01" placeholder="请输入数量" class="form-input" />
                  </div>
                </div>
              </div>
            </div>
            <div v-if="detailForm.disposals.length === 0" class="empty-array-message">
              暂无处置物，请点击上方按钮添加
            </div>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">备注</label>
          <textarea 
            v-model="detailForm.remarks" 
            placeholder="请输入备注信息"
            class="form-input"
            rows="3"
          ></textarea>
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
import { ref } from 'vue';
import { PlusIcon, XIcon, SparklesIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import '@/assets/styles/form.css';

interface DisposalItem {
  type: string;
  name: string;
  quantity: number;
}

interface ProductItem {
  name: string;
  percentage: number;
}

interface DetailForm {
  measurementNo: string;
  deductionRate: number;
  finalWeight: number;
  radiationTest: string;
  products: ProductItem[];
  disposals: DisposalItem[];
  remarks: string;
}

// 表单数据
const detailForm = ref<DetailForm>({
  measurementNo: '',
  deductionRate: 0,
  finalWeight: 0,
  radiationTest: '',
  products: [],
  disposals: [],
  remarks: ''
});

// 添加品名
const addProduct = () => {
  detailForm.value.products.push({
    name: '',
    percentage: 0
  });
}

// 删除品名
const removeProduct = (index: number) => {
  detailForm.value.products.splice(index, 1);
}

// 添加处置物
const addDisposal = () => {
  detailForm.value.disposals.push({
    type: '',
    name: '',
    quantity: 0
  });
}

// 删除处置物
const removeDisposal = (index: number) => {
  detailForm.value.disposals.splice(index, 1);
}

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'inbound_quality');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'inbound_quality'
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
    
    