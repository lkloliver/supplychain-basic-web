<template>
  <div class="contract-upload">
    <DocumentUploadTemplate
      title="运输合同上传"
      backRouteName="DashboardHome"
      uploadRouteName="/dashboard/contract/transport/upload"
      manageRouteName="/dashboard/contract/transport/manage"
      :onSubmit="handleFileUpload"
      @upload-success="handleUploadSuccess"
      @cancel-upload="handleCancelUpload"
      ref="templateRef"
    >
      <!-- 详情信息表单 -->
      <form @submit.prevent="handleDetailSubmit" class="form">
        <div class="form-grid">
          <div class="form-group">
            <label class="form-label">运输合同编号</label>
            <input 
              v-model="detailForm.contractNo" 
              type="text" 
              placeholder="请输入合同编号"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">签订日期</label>
            <input 
              v-model="detailForm.signDate" 
              type="date" 
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">合同起始时间</label>
            <input 
              v-model="detailForm.startTime" 
              type="date" 
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">合同截止时间</label>
            <input 
              v-model="detailForm.endTime" 
              type="date" 
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">运输方式</label>
            <select v-model="detailForm.transportMethod" class="form-input">
              <option value="">请选择运输方式</option>
              <option value="公路运输">公路运输</option>
              <option value="铁路运输">铁路运输</option>
              <option value="水路运输">水路运输</option>
              <option value="航空运输">航空运输</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">承运方信息</label>
            <input 
              v-model="detailForm.carrier" 
              type="text" 
              placeholder="请输入承运方信息"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">托运方信息</label>
            <input 
              v-model="detailForm.shipper" 
              type="text" 
              placeholder="请输入托运方信息"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label class="form-label">经办人</label>
            <input 
              v-model="detailForm.handler" 
              type="text" 
              placeholder="请输入经办人"
              class="form-input"
            />
          </div>
        </div>

        <!-- 运输明细 -->
        <div class="form-array-group">
          <div class="array-header">
            <h4 class="array-title">运输明细</h4>
            <button type="button" class="add-item-btn" @click="addTransportDetail">
              <PlusIcon class="btn-icon" />
              添加运输路线
            </button>
          </div>
          <div class="array-items">
            <div v-for="(item, index) in detailForm.transportDetails" :key="index" class="array-item">
              <div class="array-item-header">
                <span class="array-item-title">运输路线 #{{ index + 1 }}</span>
                <button type="button" class="remove-item-btn" @click="removeTransportDetail(index)">
                  <XIcon class="btn-icon" />
                </button>
              </div>
              <div class="array-item-content">
                <div class="form-grid">
                  <div class="form-group">
                    <label class="form-label">起运地</label>
                    <input v-model="item.startLocation" type="text" placeholder="请输入起运地" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">目的地</label>
                    <input v-model="item.endLocation" type="text" placeholder="请输入目的地" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">名称/品类</label>
                    <input v-model="item.category" type="text" placeholder="请输入名称/品类" class="form-input" />
                  </div>
                  <div class="form-group">
                    <label class="form-label">含税运输单价</label>
                    <input v-model="item.unitPrice" type="number" placeholder="请输入含税运输单价" class="form-input" />
                  </div>
                </div>
              </div>
            </div>
            <div v-if="detailForm.transportDetails.length === 0" class="empty-array-message">
              暂无运输路线信息，请点击上方按钮添加
            </div>
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="action-btn primary">提交详情信息</button>
        </div>
      </form>
    </DocumentUploadTemplate>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { PlusIcon, XIcon } from 'lucide-vue-next';
import DocumentUploadTemplate from '@/components/templates/DocumentUploadTemplate.vue';
import '@/assets/styles/form.css';
import { mockTransportContractRecognition } from '@/mocks/contract/transport';

const router = useRouter();
const templateRef = ref();
const fileId = ref('');

interface TransportDetail {
  startLocation: string;
  endLocation: string;
  category: string;
  unitPrice: number;
}

interface DetailForm {
  contractNo: string;
  signDate: string;
  startTime: string;
  endTime: string;
  transportMethod: string;
  carrier: string;
  shipper: string;
  handler: string;
  transportDetails: TransportDetail[];
}

// 表单数据
const detailForm = ref<DetailForm>({
  contractNo: '',
  signDate: '',
  startTime: '',
  endTime: '',
  transportMethod: '',
  carrier: '',
  shipper: '',
  handler: '',
  transportDetails: []
});

// 处理文件上传
const handleFileUpload = async (formData: {name: string, file: File}) => {
  const uploadData = new FormData();
  uploadData.append('name', formData.name);
  uploadData.append('file', formData.file);
  uploadData.append('fileType', 'transport_contract');

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000));
    const result = { 
      success: true, 
      fileId: 'mock_file_' + Date.now(),
      data: {
        name: formData.name,
        fileType: 'transport_contract'
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
const handleCancelUpload = () => {
  router.push('/dashboard/contract/transport/manage');
}

// 添加运输明细
const addTransportDetail = () => {
  detailForm.value.transportDetails.push({
    startLocation: '',
    endLocation: '',
    category: '',
    unitPrice: 0
  });
}

// 删除运输明细
const removeTransportDetail = (index: number) => {
  detailForm.value.transportDetails.splice(index, 1);
}

// 提交详情信息
const handleDetailSubmit = async () => {
  try {
    // TODO: 调用API保存详情信息
    console.log('提交详情信息:', detailForm.value);
    router.push('/dashboard/contract/transport/manage');
  } catch (error) {
    console.error('提交详情信息失败：', error);
  }
};
</script>

<style scoped>
/* 移除所有原有样式 */
</style>

