<template>
  <div class="contract-upload">
    <!--文件上传-->
    <DocumentUploadTemplate
      title="采购合同上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/contract/purchase/upload"
      manageRouteName="/dashboard/contract/purchase/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">采购合同编号</label>
            <input 
              v-model="detailForm.contractNo" 
              type="text" 
              placeholder="请输入合同编号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">签订时间</label>
            <input 
              v-model="detailForm.signDate" 
              type="date" 
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">买方信息</label>
            <input 
              v-model="detailForm.buyer" 
              type="text" 
              placeholder="请输入买方信息"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">卖方信息</label>
            <input 
              v-model="detailForm.seller" 
              type="text" 
              placeholder="请输入卖方信息"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">付款账户</label>
            <input 
              v-model="detailForm.paymentAccount" 
              type="text" 
              placeholder="请输入付款账户"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">收款账户</label>
            <input 
              v-model="detailForm.receivingAccount" 
              type="text" 
              placeholder="请输入收款账户"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">税率</label>
            <input 
              v-model="detailForm.taxRate" 
              type="number" 
              placeholder="请输入税率(%)"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">发票类型</label>
            <select v-model="detailForm.invoiceType" class="form-input">
              <option value="">请选择发票类型</option>
              <option value="special">增值税专用发票</option>
              <option value="normal">增值税普通发票</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">付款时间</label>
            <input 
              v-model="detailForm.paymentDate" 
              type="date" 
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">付款方式</label>
            <select v-model="detailForm.paymentMethod" class="form-input">
              <option value="">请选择付款方式</option>
              <option value="cash">现金</option>
              <option value="transfer">银行转账</option>
              <option value="check">支票</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">合同金额</label>
            <input 
              v-model="detailForm.totalAmount" 
              type="number" 
              placeholder="请输入合同金额"
              class="form-input"
            />
          </div>
        </div>

        <!-- 货物信息 -->
        <div class="form-array-group">
          <div class="array-header">
            <h4 class="array-title">货物明细</h4>
            <button type="button" class="add-item-btn" @click="addGoodsItem">
              <PlusIcon class="btn-icon" />
              添加货物
            </button>
          </div>
          <div class="array-items">
            <div v-for="(item, index) in detailForm.goods" :key="index" class="array-item">
              <div class="array-item-header">
                <span class="array-item-title">货物 #{{ index + 1 }}</span>
                <button type="button" class="remove-item-btn" @click="removeGoodsItem(index)">
                  <XIcon class="btn-icon" />
                </button>
              </div>
              <div class="array-item-content">
                <div class="form-grid">
                  <div class="form-group">
                    <label class="form-label">货物名称</label>
                    <input v-model="item.name" type="text" placeholder="请输入货物名称" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">数量</label>
                    <input v-model="item.quantity" type="number" placeholder="请输入数量" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">价格</label>
                    <input v-model="item.price" type="number" placeholder="请输入价格" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">规格型号</label>
                    <input v-model="item.specification" type="text" placeholder="请输入规格型号" class="form-input" />
                  </div>
                </div>
              </div>
            </div>
            <div v-if="detailForm.goods.length === 0" class="empty-array-message">
              暂无货物信息，请点击上方按钮添加
            </div>
          </div>
        </div>

        <!-- 备注 -->
        <div class="form-group">
          <label class="form-label">备注</label>
          <textarea 
            v-model="detailForm.remarks" 
            rows="3" 
            placeholder="请输入合同备注信息"
            class="form-input"
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
import { useRouter } from 'vue-router';
import { PlusIcon, XIcon, SparklesIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import '@/assets/styles/form.css';

const router = useRouter();
const fileId = ref('');
const templateRef = ref();

interface GoodsItem {
  name: string;
  quantity: number;
  price: number;
  specification: string;
}

interface DetailForm {
  contractNo: string;
  signDate: string;
  buyer: string;
  seller: string;
  paymentAccount: string;
  receivingAccount: string;
  taxRate: number;
  invoiceType: string;
  paymentDate: string;
  paymentMethod: string;
  totalAmount: number;
  goods: GoodsItem[];
  remarks: string;
}

// 表单数据
const detailForm = ref<DetailForm>({
  contractNo: '',
  signDate: '',
  buyer: '',
  seller: '',
  paymentAccount: '',
  receivingAccount: '',
  taxRate: 0,
  invoiceType: '',
  paymentDate: '',
  paymentMethod: '',
  totalAmount: 0,
  goods: [],
  remarks: ''
});

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'purchase_contract');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'purchase_contract'
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
    fileId.value = result.fileId;
  }
}

// 处理取消上传
const handleCancelUpload = async () => {
  if (fileId.value) {
    try {
      // TODO: 调用删除接口
      console.log('删除文件:', fileId.value);
      fileId.value = '';
    } catch (error) {
      console.error('删除文件失败：', error);
    }
  }
}

// 监听返回事件
const handlePrevStep = () => {
  // 返回时保留 fileId
  console.log('返回基本信息，当前 fileId:', fileId.value);
}

// 添加货物明细
const addGoodsItem = () => {
  detailForm.value.goods.push({
    name: '',
    quantity: 0,
    price: 0,
    specification: ''
  });
}

// 删除货物明细
const removeGoodsItem = (index: number) => {
  detailForm.value.goods.splice(index, 1);
}

// 提交详情信息
const handleDetailSubmit = async () => {
  try {
    // TODO: 调用API保存详情信息
  } catch (error) {
    console.error('提交详情信息失败：', error);
  }
};

// AI自动识别
const handleAIAutoFill = async () => {
  try {
    // TODO: 调用AI识别接口
    console.log('开始AI自动识别...');
  } catch (error) {
    console.error('AI识别失败：', error);
  }
};
</script>

<style scoped>
.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-group {
  margin-bottom: 16px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #555;
  margin-bottom: 8px;
}

.form-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
}

.form-input:focus {
  outline: none;
  border-color: #1e88e5;
}

.form-array-group {
  margin-top: 24px;
  border-top: 1px solid #eee;
  padding-top: 16px;
}

.array-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.array-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.add-item-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s;
}

.add-item-btn:hover {
  background-color: #e0e0e0;
}

.array-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.array-item {
  border: 1px solid #eee;
  border-radius: 6px;
  overflow: hidden;
}

.array-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #eee;
}

.array-item-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.remove-item-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background-color: transparent;
  border: none;
  border-radius: 4px;
  color: #999;
  cursor: pointer;
  transition: all 0.2s;
}

.remove-item-btn:hover {
  background-color: #f1f1f1;
  color: #d32f2f;
}

.array-item-content {
  padding: 16px;
}

.empty-array-message {
  padding: 16px;
  text-align: center;
  color: #999;
  font-size: 14px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.primary {
  background-color: #1e88e5;
  color: white;
}

.primary:hover {
  background-color: #1976d2;
}

.secondary {
  background-color: #f5f5f5;
  color: #333;
}

.secondary:hover {
  background-color: #e0e0e0;
}

.btn-icon {
  width: 16px;
  height: 16px;
}
</style>
  
  