<template>
  <div class="contract-upload">
    <!--文件上传-->
    <DocumentUploadTemplate
      title="销售合同上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/contract/sales/upload"
      manageRouteName="/dashboard/contract/sales/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
    />

    <!-- 详情信息 -->
    <div v-if="fileId" class="detail-form">
      <div class="form-card">
        <h3 class="form-card-title">销售合同详情</h3>
        <form @submit.prevent="handleDetailSubmit" class="form">
          <div class="form-grid">
            <!-- 基础信息 -->
            <div class="form-group">
              <label class="form-label">销售合同编号</label>
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

            <!-- 付款信息 -->
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

            <!-- 合同信息 -->
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

          <!-- 货物明细 -->
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

          <div class="form-actions">
            <button type="submit" class="action-btn primary">提交详情信息</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { PlusIcon, XIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import '@/assets/styles/form.css';

const router = useRouter();
const fileId = ref('');

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
  uploadData.append('fileType', 'sales_contract');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'sales_contract'
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
</script>

<style scoped>
/* 移除所有原有样式 */
</style>

