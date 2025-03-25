<template>
  <div class="goods-table-input">
    <div class="table-actions">
      <button type="button" class="add-row-btn" @click="addRow">
        <PlusIcon class="btn-icon" />
        添加货物
      </button>
      
      <button 
        v-if="showImportBtn" 
        type="button" 
        class="import-btn" 
        @click="importFromExcel"
      >
        <FileSpreadsheetIcon class="btn-icon" />
        从Excel导入
      </button>
    </div>
    
    <div class="goods-table-wrapper">
      <table class="goods-table">
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
                @input="handleNumberInput(row, col.key)"
              />
              
              <!-- 下拉选择 -->
              <select 
                v-else-if="col.type === 'select'"
                v-model="row[col.key]" 
                class="cell-select"
              >
                <option value="">请选择</option>
                <option 
                  v-for="(opt, optIndex) in col.options" 
                  :key="optIndex" 
                  :value="opt.value"
                >
                  {{ opt.label }}
                </option>
              </select>
              
              <!-- 日期选择 -->
              <input 
                v-else-if="col.type === 'date'"
                type="date" 
                v-model="row[col.key]" 
                class="cell-input"
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
              暂无货物数据，请点击"添加货物"按钮添加
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
import { ref, computed, watch } from 'vue';
import { PlusIcon, TrashIcon, FileSpreadsheetIcon } from 'lucide-vue-next';

const props = defineProps({
  value: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    required: true
  },
  rowTemplate: {
    type: Object,
    default: () => ({})
  },
  showFooter: {
    type: Boolean,
    default: true
  },
  showImportBtn: {
    type: Boolean,
    default: false
  },
  autoCalculateAmount: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['input']);

// 本地数据，用于双向绑定
const localValue = ref(Array.isArray(props.value) ? [...props.value] : []);

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
  const newRow = { ...props.rowTemplate };
  // 确保每个列都有初始值
  props.columns.forEach(col => {
    if (newRow[col.key] === undefined) {
      if (col.type === 'number') {
        newRow[col.key] = 0;
      } else {
        newRow[col.key] = '';
      }
    }
  });
  localValue.value.push(newRow);
};

// 删除行
const deleteRow = (index) => {
  localValue.value.splice(index, 1);
};

// 处理数字输入，自动计算金额
const handleNumberInput = (row, key) => {
  if (props.autoCalculateAmount && (key === 'quantity' || key === 'unitPrice')) {
    const quantity = parseFloat(row.quantity) || 0;
    const unitPrice = parseFloat(row.unitPrice) || 0;
    row.amount = (quantity * unitPrice).toFixed(2);
  }
};

// 计算列总和
const calculateColumnSum = (key) => {
  const sum = localValue.value.reduce((acc, row) => {
    const value = parseFloat(row[key]);
    return acc + (isNaN(value) ? 0 : value);
  }, 0);
  
  // 格式化数字，保留两位小数
  return sum.toFixed(2);
};

// 获取列样式
const getColumnStyle = (col) => {
  const style = {};
  if (col.width) {
    style.width = typeof col.width === 'number' ? `${col.width}px` : col.width;
  }
  return style;
};

// 从Excel导入（模拟功能）
const importFromExcel = () => {
  alert('此功能需要集成Excel解析库，如SheetJS。这里仅作为示例。');
  
  // 模拟导入数据
  const mockData = [
    { name: '货物A', specification: '规格1', unit: '个', quantity: 10, unitPrice: 100, amount: 1000 },
    { name: '货物B', specification: '规格2', unit: '箱', quantity: 5, unitPrice: 200, amount: 1000 }
  ];
  
  localValue.value = [...mockData];
};
</script>

<style scoped>
.goods-table-input {
  margin-bottom: 20px;
}

.table-actions {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.add-row-btn,
.import-btn {
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

.add-row-btn:hover,
.import-btn:hover {
  background-color: #e0e0e0;
}

.goods-table-wrapper {
  overflow-x: auto;
  border: 1px solid #eee;
  border-radius: 4px;
}

.goods-table {
  width: 100%;
  border-collapse: collapse;
}

.goods-table th,
.goods-table td {
  padding: 8px;
  border: 1px solid #eee;
  text-align: left;
}

.goods-table th {
  background-color: #f9f9f9;
  font-weight: 500;
  font-size: 13px;
}

.action-column {
  width: 60px;
  text-align: center;
}

.cell-input,
.cell-select {
  width: 100%;
  padding: 6px 8px;
  border: 1px solid #ddd;
  border-radius: 3px;
  font-size: 13px;
}

.cell-input:focus,
.cell-select:focus {
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

