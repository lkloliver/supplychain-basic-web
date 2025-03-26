<template>
  <div class="transport-details-input">
    <div class="table-actions">
      <button type="button" class="add-row-btn" @click="addRow">
        <PlusIcon class="btn-icon" />
        添加运输路线
      </button>
    </div>
    
    <div class="table-wrapper">
      <table class="transport-table">
        <thead>
          <tr>
            <th v-for="(col, index) in columns" :key="index" :style="getColumnStyle(col)">
              {{ col.label }}
            </th>
            <th class="action-column">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, rowIndex) in localValue" :key="rowIndex">
            <td v-for="(col, colIndex) in columns" :key="colIndex">
              <!-- 文本输入 -->
              <input 
                v-if="col.type === 'text' || !col.type"
                type="text" 
                v-model="row[col.key]" 
                class="cell-input"
                :placeholder="col.placeholder || ''"
              />
              
              <!-- 数字输入 -->
              <input 
                v-else-if="col.type === 'number'"
                type="number" 
                v-model.number="row[col.key]" 
                class="cell-input"
                :placeholder="col.placeholder || ''"
                :step="col.step || 'any'"
              />
            </td>
            <td class="action-cell">
              <button type="button" class="delete-row-btn" @click="deleteRow(rowIndex)">
                <TrashIcon class="btn-icon" />
              </button>
            </td>
          </tr>
          
          <tr v-if="localValue.length === 0">
            <td :colspan="columns.length + 1" class="empty-message">
              暂无运输路线数据，请点击"添加运输路线"按钮添加
            </td>
          </tr>
        </tbody>
        
        <tfoot v-if="showFooter && localValue.length > 0">
          <tr>
            <td 
              v-for="(col, colIndex) in columns" 
              :key="`footer-${colIndex}`"
              :class="{ 'total-cell': col.sum }"
            >
              <template v-if="col.sum">
                {{ calculateColumnSum(col.key) }}
              </template>
              <template v-else-if="colIndex === 0">
                合计
              </template>
            </td>
            <td></td>
          </tr>
        </tfoot>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { PlusIcon, TrashIcon } from 'lucide-vue-next';

interface Column {
  key: string;
  label: string;
  type?: string;
  width?: string | number;
  placeholder?: string;
  step?: number;
  sum?: boolean;
}

interface TransportDetail {
  startLocation: string;
  endLocation: string;
  category: string;
  unitPrice: number;
}

const props = defineProps<{
  value: TransportDetail[];
  columns: Column[];
  showFooter?: boolean;
}>();

const emit = defineEmits(['input']);

// 本地数据，用于双向绑定
const localValue = ref<TransportDetail[]>(Array.isArray(props.value) ? [...props.value] : []);

// 监听外部value变化
watch(() => props.value, (newVal) => {
  if (newVal !== localValue.value) {
    localValue.value = Array.isArray(newVal) ? [...newVal] : [];
  }
}, { deep: true });

// 监听本地数据变化，向外发送更新
watch(localValue, (newVal) => {
  emit('input', newVal);
}, { deep: true });

// 添加行
const addRow = () => {
  const newRow: TransportDetail = {
    startLocation: '',
    endLocation: '',
    category: '',
    unitPrice: 0
  };
  localValue.value.push(newRow);
};

// 删除行
const deleteRow = (index: number) => {
  localValue.value.splice(index, 1);
};

// 计算列总和
const calculateColumnSum = (key: string) => {
  const sum = localValue.value.reduce((acc, row) => {
    const value = parseFloat(String(row[key as keyof TransportDetail]));
    return acc + (isNaN(value) ? 0 : value);
  }, 0);
  
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(sum);
};

// 获取列样式
const getColumnStyle = (col: Column) => {
  const style: { width?: string } = {};
  if (col.width) {
    style.width = typeof col.width === 'number' ? `${col.width}px` : col.width;
  }
  return style;
};
</script>

<style scoped>
.transport-details-input {
  margin-bottom: 20px;
}

.table-actions {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.add-row-btn {
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

.add-row-btn:hover {
  background-color: #e0e0e0;
}

.table-wrapper {
  overflow-x: auto;
  border: 1px solid #eee;
  border-radius: 4px;
}

.transport-table {
  width: 100%;
  border-collapse: collapse;
}

.transport-table th,
.transport-table td {
  padding: 8px;
  border: 1px solid #eee;
  text-align: left;
}

.transport-table th {
  background-color: #f9f9f9;
  font-weight: 500;
  font-size: 13px;
}

.action-column {
  width: 60px;
  text-align: center;
}

.cell-input {
  width: 100%;
  padding: 6px 8px;
  border: 1px solid #ddd;
  border-radius: 3px;
  font-size: 13px;
}

.cell-input:focus {
  outline: none;
  border-color: #1e88e5;
}

.action-cell {
  text-align: center;
}

.delete-row-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  background-color: transparent;
  border: none;
  border-radius: 4px;
  color: #999;
  cursor: pointer;
  transition: all 0.2s;
}

.delete-row-btn:hover {
  background-color: #f1f1f1;
  color: #d32f2f;
}

.btn-icon {
  width: 16px;
  height: 16px;
}

.empty-message {
  text-align: center;
  padding: 16px;
  color: #999;
  font-style: italic;
}

.total-cell {
  font-weight: 600;
  background-color: #f5f5f5;
}
</style> 