<template>
  <div class="goods-table-container">
    <div v-if="!hasData" class="empty-table-message">
      <p>暂无货物信息</p>
    </div>
    <table v-else class="goods-table">
      <thead>
        <tr>
          <th v-for="column in effectiveColumns" :key="column.key">{{ column.label }}</th>
          <th v-if="showActions" class="actions-column">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in safeData" :key="index">
          <td v-for="column in effectiveColumns" :key="`${index}-${column.key}`">
            {{ getFormattedValue(item, column) }}
          </td>
          <td v-if="showActions" class="actions-column">
            <div class="item-actions">
              <button 
                v-if="showViewAction" 
                class="item-action-btn view" 
                @click="handleView(item)"
                title="查看"
              >
                <EyeIcon class="action-icon" />
              </button>
              <button 
                v-if="showEditAction" 
                class="item-action-btn edit" 
                @click="handleEdit(item)"
                title="编辑"
              >
                <EditIcon class="action-icon" />
              </button>
            </div>
          </td>
        </tr>
      </tbody>
      <tfoot v-if="showFooter && hasData">
        <tr>
          <td v-for="column in effectiveColumns" :key="`footer-${column.key}`" :class="{ 'total-column': column.isTotal }">
            <template v-if="column.isTotal">
              {{ calculateTotal(column.key) }}
            </template>
            <template v-else-if="column.footerLabel">
              {{ column.footerLabel }}
            </template>
            <template v-else-if="column.key === effectiveColumns[0].key">
              合计
            </template>
          </td>
          <td v-if="showActions"></td>
        </tr>
      </tfoot>
    </table>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { EyeIcon, EditIcon } from 'lucide-vue-next';

const props = defineProps({
  // 数据数组
  data: {
    type: [Array, Object],
    default: () => []
  },
  // 原始文档对象
  document: {
    type: Object,
    default: () => ({})
  },
  // 分组配置
  group: {
    type: Object,
    default: () => ({})
  },
  // 列配置
  columns: {
    type: Array,
    default: () => []
  },
  // 是否显示操作列
  showActions: {
    type: Boolean,
    default: false
  },
  // 是否显示查看按钮
  showViewAction: {
    type: Boolean,
    default: true
  },
  // 是否显示编辑按钮
  showEditAction: {
    type: Boolean,
    default: true
  },
  // 是否显示表格页脚
  showFooter: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['view-item', 'edit-item']);

// 安全地获取数据数组
const safeData = computed(() => {
  if (!props.data) return [];
  
  // 如果是数组，直接返回
  if (Array.isArray(props.data)) return props.data;
  
  // 如果是对象，尝试找到数组属性
  if (typeof props.data === 'object') {
    // 查找第一个数组类型的属性
    for (const key in props.data) {
      if (Array.isArray(props.data[key])) {
        return props.data[key];
      }
    }
    
    // 如果没有找到数组，将对象转为单项数组
    return [props.data];
  }
  
  return [];
});

// 检查是否有数据
const hasData = computed(() => {
  return safeData.value && safeData.value.length > 0;
});

// 如果没有提供列配置，使用默认列
const effectiveColumns = computed(() => {
  // 优先使用直接传入的列
  if (props.columns && props.columns.length > 0) {
    return props.columns;
  }
  
  // 其次使用group中的列配置
  if (props.group && props.group.columns && props.group.columns.length > 0) {
    return props.group.columns;
  }
  
  // 如果数据存在，自动生成列
  if (hasData.value) {
    const firstItem = safeData.value[0];
    return Object.keys(firstItem).map(key => ({
      key,
      label: key.charAt(0).toUpperCase() + key.slice(1).replace(/([A-Z])/g, ' $1')
    }));
  }
  
  return [];
});

// 获取格式化后的值
const getFormattedValue = (item, column) => {
  if (!item || !column) return '';
  
  const value = item[column.key];
  
  // 如果列有格式化函数，使用它
  if (column.formatter && typeof column.formatter === 'function') {
    return column.formatter(value, item);
  }
  
  // 如果是金额字段，格式化为货币
  if (column.key.toLowerCase().includes('amount') || 
      column.key.toLowerCase().includes('price') || 
      column.key.toLowerCase().includes('cost') ||
      column.key.toLowerCase().includes('total')) {
    return formatCurrency(value);
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
  
  // 如果是金额字段，格式化为货币
  if (key.toLowerCase().includes('amount') || 
      key.toLowerCase().includes('price') || 
      key.toLowerCase().includes('cost') ||
      key.toLowerCase().includes('total')) {
    return formatCurrency(total);
  }
  
  return total;
};

// 格式化货币
const formatCurrency = (value) => {
  if (value === undefined || value === null) return '';
  
  const numValue = parseFloat(value);
  if (isNaN(numValue)) return value;
  
  return new Intl.NumberFormat('zh-CN', { 
    style: 'currency', 
    currency: 'CNY',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(numValue);
};

// 处理查看项目
const handleView = (item) => {
  emit('view-item', item);
};

// 处理编辑项目
const handleEdit = (item) => {
  emit('edit-item', item);
};
</script>

<style scoped>
.goods-table-container {
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

.goods-table tfoot .total-column {
  font-weight: 600;
  color: #1e88e5;
}

.actions-column {
  width: 80px;
  text-align: center;
}

.item-actions {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.item-action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 4px;
  background-color: transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.item-action-btn:hover {
  background-color: #f0f0f0;
}

.action-icon {
  width: 16px;
  height: 16px;
}

.view .action-icon {
  color: #1e88e5;
}

.edit .action-icon {
  color: #ffa000;
}
</style>

