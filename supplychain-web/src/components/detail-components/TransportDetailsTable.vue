<template>
  <div class="transport-details-table">
    <div v-if="!hasData" class="empty-table-message">
      <p>暂无运输明细</p>
    </div>
    <table v-else class="goods-table">
      <thead>
        <tr>
          <th v-for="column in effectiveColumns" :key="column.key">{{ column.label }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in safeData" :key="index">
          <td v-for="column in effectiveColumns" :key="`${index}-${column.key}`">
            {{ getFormattedValue(item, column) }}
          </td>
        </tr>
      </tbody>
      <tfoot v-if="showFooter && hasData">
        <tr>
          <td v-for="column in effectiveColumns" :key="`footer-${column.key}`">
            <template v-if="column.key === effectiveColumns[0].key">
              合计
            </template>
            <template v-else-if="column.key === 'unitPrice'">
              {{ calculateTotal('unitPrice') }}
            </template>
          </td>
        </tr>
      </tfoot>
    </table>

    <div v-if="showActions" class="table-actions">
      <el-button type="primary" @click="handleAdd">添加运输路线</el-button>
    </div>

    <!-- 添加/编辑运输明细的对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="currentItem"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="起运地" prop="startLocation">
          <el-input v-model="currentItem.startLocation" placeholder="请输入起运地" />
        </el-form-item>
        <el-form-item label="目的地" prop="endLocation">
          <el-input v-model="currentItem.endLocation" placeholder="请输入目的地" />
        </el-form-item>
        <el-form-item label="名称/品类" prop="category">
          <el-input v-model="currentItem.category" placeholder="请输入名称/品类" />
        </el-form-item>
        <el-form-item label="含税运输单价" prop="unitPrice">
          <el-input-number 
            v-model="currentItem.unitPrice"
            :min="0"
            :precision="2"
            :step="100"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue';
import type { FormInstance } from 'element-plus';

interface Column {
  key: string;
  label: string;
  width?: string;
  formatter?: (value: any) => string;
}

interface TransportDetail {
  startLocation: string;
  endLocation: string;
  category: string;
  unitPrice: number;
}

const props = defineProps<{
  data: TransportDetail[];
  columns: Column[];
  showFooter?: boolean;
  showActions?: boolean;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: TransportDetail[]): void;
}>();

const dialogVisible = ref(false);
const currentItem = ref<TransportDetail>({
  startLocation: '',
  endLocation: '',
  category: '',
  unitPrice: 0
});
const formRef = ref<FormInstance>();

const rules = {
  startLocation: [{ required: true, message: '请输入起运地', trigger: 'blur' }],
  endLocation: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  category: [{ required: true, message: '请输入名称/品类', trigger: 'blur' }],
  unitPrice: [{ required: true, message: '请输入含税运输单价', trigger: 'blur' }]
};

const dialogTitle = computed(() => '添加运输路线');

const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(value);
};

const handleAdd = () => {
  currentItem.value = {
    startLocation: '',
    endLocation: '',
    category: '',
    unitPrice: 0
  };
  dialogVisible.value = true;
};

const handleSave = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      const newValue = [...props.data, { ...currentItem.value }];
      emit('update:modelValue', newValue);
      dialogVisible.value = false;
    }
  });
};

// 安全地获取数据数组
const safeData = computed(() => {
  if (!props.data) return [];
  if (Array.isArray(props.data)) return props.data;
  if (typeof props.data === 'object') {
    for (const key in props.data) {
      if (Array.isArray(props.data[key])) {
        return props.data[key];
      }
    }
    return [props.data];
  }
  return [];
});

// 检查是否有数据
const hasData = computed(() => {
  return safeData.value && safeData.value.length > 0;
});

// 获取有效的列配置
const effectiveColumns = computed(() => {
  // 优先使用直接传入的列配置
  if (props.columns && props.columns.length > 0) {
    return props.columns;
  }
  // 如果没有列配置，则使用默认的
  if (hasData.value) {
    const firstItem = safeData.value[0];
    return Object.keys(firstItem).map(key => ({
      key,
      label: key.charAt(0).toUpperCase() + key.slice(1)
    }));
  }
  return [];
});

// 获取格式化后的值
const getFormattedValue = (item, column) => {
  if (!item || !column) return '';
  const value = item[column.key];
  if (column.formatter && typeof column.formatter === 'function') {
    return column.formatter(value, item);
  }
  return value !== undefined && value !== null ? value : '';
};

// 计算列总计
const calculateTotal = (key) => {
  if (!hasData.value) return '';
  const total = safeData.value.reduce((sum, item) => {
    const value = parseFloat(item[key]);
    return isNaN(value) ? sum : sum + value;
  }, 0);
  
  if (key === 'unitPrice') {
    return new Intl.NumberFormat('zh-CN', { 
      style: 'currency', 
      currency: 'CNY',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(total);
  }
  return total;
};

// 添加 watch 来监控数据变化
watch(() => props.data, (newVal) => {
  console.log('TransportDetailsTable data changed:', newVal);
}, { immediate: true });

watch(() => props.columns, (newVal) => {
  console.log('TransportDetailsTable columns changed:', newVal);
}, { immediate: true });
</script>

<style scoped>
.transport-details-table {
  width: 100%;
  overflow-x: auto;
  margin-bottom: 16px;
}

.empty-table-message {
  padding: 20px;
  text-align: center;
  color: #999;
  background-color: #f9f9f9;
  border-radius: 4px;
  border: 1px dashed #ddd;
}

.goods-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.goods-table th,
.goods-table td {
  padding: 10px 12px;
  text-align: left;
  border: 1px solid #eee;
}

.goods-table th {
  background-color: #f5f5f5;
  font-weight: 500;
  color: #333;
}

.goods-table tbody tr:hover {
  background-color: #f9f9f9;
}

.goods-table tfoot {
  background-color: #f5f5f5;
  font-weight: 500;
}

.table-actions {
  margin-top: 16px;
}

:deep(.el-table) {
  margin-top: 8px;
}
</style> 