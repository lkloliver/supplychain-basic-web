<template>
    <div class="manage-container">
      <h1>采购运单管理</h1>
      
      <div class="search-section">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="运单号">
            <el-input v-model="searchForm.waybillNo" placeholder="请输入运单号"></el-input>
          </el-form-item>
          <el-form-item label="供应商">
            <el-select v-model="searchForm.supplier" placeholder="请选择供应商" clearable>
              <el-option v-for="item in supplierOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <div class="table-operations">
        <el-button type="success" @click="exportData">导出数据</el-button>
        <el-button type="danger" @click="batchDelete" :disabled="selectedItems.length === 0">批量删除</el-button>
      </div>
      
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="waybillNo" label="运单号" width="180"></el-table-column>
        <el-table-column prop="supplier" label="供应商" width="180"></el-table-column>
        <el-table-column prop="transportCompany" label="运输公司" width="180"></el-table-column>
        <el-table-column prop="vehicleNo" label="车牌号" width="120"></el-table-column>
        <el-table-column prop="driver" label="司机" width="120"></el-table-column>
        <el-table-column prop="contactPhone" label="联系电话" width="150"></el-table-column>
        <el-table-column prop="departureDate" label="发车日期" width="120"></el-table-column>
        <el-table-column prop="arrivalDate" label="到达日期" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button size="small" @click="viewDetails(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="editItem(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteItem(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </div>
      
      <!-- 详情对话框 -->
      <el-dialog title="运单详情" v-model="detailsDialogVisible" width="70%">
        <div class="details-container" v-if="currentItem">
          <div class="details-header">
            <h2>运单号: {{ currentItem.waybillNo }}</h2>
            <el-tag :type="getStatusType(currentItem.status)">{{ currentItem.status }}</el-tag>
          </div>
          
          <el-descriptions :column="2" border>
            <el-descriptions-item label="供应商">{{ currentItem.supplier }}</el-descriptions-item>
            <el-descriptions-item label="运输公司">{{ currentItem.transportCompany }}</el-descriptions-item>
            <el-descriptions-item label="车牌号">{{ currentItem.vehicleNo }}</el-descriptions-item>
            <el-descriptions-item label="司机">{{ currentItem.driver }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentItem.contactPhone }}</el-descriptions-item>
            <el-descriptions-item label="发车日期">{{ currentItem.departureDate }}</el-descriptions-item>
            <el-descriptions-item label="预计到达日期">{{ currentItem.arrivalDate }}</el-descriptions-item>
            <el-descriptions-item label="实际到达日期">{{ currentItem.actualArrivalDate || '未到达' }}</el-descriptions-item>
            <el-descriptions-item label="货物描述" :span="2">{{ currentItem.goodsDescription }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ currentItem.remarks || '无' }}</el-descriptions-item>
          </el-descriptions>
          
          <div class="details-section">
            <h3>货物明细</h3>
            <el-table :data="currentItem.goods || []" border style="width: 100%">
              <el-table-column prop="name" label="货物名称"></el-table-column>
              <el-table-column prop="quantity" label="数量"></el-table-column>
              <el-table-column prop="unit" label="单位"></el-table-column>
              <el-table-column prop="weight" label="重量(kg)"></el-table-column>
              <el-table-column prop="volume" label="体积(m³)"></el-table-column>
            </el-table>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="detailsDialogVisible = false">关闭</el-button>
            <el-button type="primary" @click="printDetails">打印</el-button>
          </span>
        </template>
      </el-dialog>
      
      <!-- 编辑对话框 -->
      <el-dialog :title="dialogTitle" v-model="editDialogVisible" width="70%">
        <el-form :model="editForm" :rules="rules" ref="editFormRef" label-width="120px">
          <el-form-item label="运单号" prop="waybillNo">
            <el-input v-model="editForm.waybillNo" :disabled="editMode"></el-input>
          </el-form-item>
          <el-form-item label="供应商" prop="supplier">
            <el-select v-model="editForm.supplier" placeholder="请选择供应商" style="width: 100%">
              <el-option v-for="item in supplierOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="运输公司" prop="transportCompany">
            <el-input v-model="editForm.transportCompany"></el-input>
          </el-form-item>
          <el-form-item label="车牌号" prop="vehicleNo">
            <el-input v-model="editForm.vehicleNo"></el-input>
          </el-form-item>
          <el-form-item label="司机" prop="driver">
            <el-input v-model="editForm.driver"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="editForm.contactPhone"></el-input>
          </el-form-item>
          <el-form-item label="发车日期" prop="departureDate">
            <el-date-picker v-model="editForm.departureDate" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
          </el-form-item>
          <el-form-item label="预计到达日期" prop="arrivalDate">
            <el-date-picker v-model="editForm.arrivalDate" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
          </el-form-item>
          <el-form-item label="实际到达日期" prop="actualArrivalDate">
            <el-date-picker v-model="editForm.actualArrivalDate" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="editForm.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="待发货" value="待发货"></el-option>
              <el-option label="运输中" value="运输中"></el-option>
              <el-option label="已到达" value="已到达"></el-option>
              <el-option label="已签收" value="已签收"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货物描述" prop="goodsDescription">
            <el-input type="textarea" v-model="editForm.goodsDescription" rows="3"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remarks">
            <el-input type="textarea" v-model="editForm.remarks" rows="3"></el-input>
          </el-form-item>
          
          <el-form-item label="货物明细">
            <el-button type="primary" @click="addGoodsItem" size="small">添加货物</el-button>
            <el-table :data="editForm.goods" border style="width: 100%; margin-top: 10px;">
              <el-table-column prop="name" label="货物名称">
                <template #default="scope">
                  <el-input v-model="scope.row.name" placeholder="货物名称"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="quantity" label="数量">
                <template #default="scope">
                  <el-input-number v-model="scope.row.quantity" :min="1" controls-position="right"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column prop="unit" label="单位">
                <template #default="scope">
                  <el-select v-model="scope.row.unit" placeholder="单位">
                    <el-option label="件" value="件"></el-option>
                    <el-option label="箱" value="箱"></el-option>
                    <el-option label="吨" value="吨"></el-option>
                    <el-option label="千克" value="千克"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column prop="weight" label="重量(kg)">
                <template #default="scope">
                  <el-input-number v-model="scope.row.weight" :min="0" :precision="2" controls-position="right"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column prop="volume" label="体积(m³)">
                <template #default="scope">
                  <el-input-number v-model="scope.row.volume" :min="0" :precision="3" controls-position="right"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="danger" @click="removeGoodsItem(scope.$index)" size="small">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="editDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitForm">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, computed, onMounted } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  // 模拟数据
  const supplierOptions = [
    { value: '供应商A', label: '供应商A' },
    { value: '供应商B', label: '供应商B' },
    { value: '供应商C', label: '供应商C' },
  ]
  
  // 搜索表单
  const searchForm = reactive({
    waybillNo: '',
    supplier: '',
    dateRange: []
  })
  
  // 表格数据
  const tableData = ref([
    {
      id: 1,
      waybillNo: 'WB20230001',
      supplier: '供应商A',
      transportCompany: '快速物流公司',
      vehicleNo: '京A12345',
      driver: '张三',
      contactPhone: '13800138000',
      departureDate: '2023-01-10',
      arrivalDate: '2023-01-12',
      actualArrivalDate: '2023-01-12',
      status: '已签收',
      goodsDescription: '农产品原料',
      remarks: '按时送达',
      goods: [
        { name: '小麦', quantity: 100, unit: '吨', weight: 100000, volume: 120 },
        { name: '玉米', quantity: 50, unit: '吨', weight: 50000, volume: 60 }
      ]
    },
    {
      id: 2,
      waybillNo: 'WB20230002',
      supplier: '供应商B',
      transportCompany: '安全运输有限公司',
      vehicleNo: '京B54321',
      driver: '李四',
      contactPhone: '13900139000',
      departureDate: '2023-01-15',
      arrivalDate: '2023-01-18',
      actualArrivalDate: null,
      status: '运输中',
      goodsDescription: '农产品原料',
      remarks: '',
      goods: [
        { name: '大豆', quantity: 80, unit: '吨', weight: 80000, volume: 90 }
      ]
    }
  ])
  
  // 分页
  const total = ref(100)
  const pageSize = ref(10)
  const currentPage = ref(1)
  
  // 选中项
  const selectedItems = ref([])
  
  // 对话框控制
  const detailsDialogVisible = ref(false)
  const editDialogVisible = ref(false)
  const currentItem = ref(null)
  const editMode = ref(false)
  const dialogTitle = computed(() => editMode.value ? '编辑运单' : '新增运单')
  
  // 编辑表单
  const editForm = reactive({
    waybillNo: '',
    supplier: '',
    transportCompany: '',
    vehicleNo: '',
    driver: '',
    contactPhone: '',
    departureDate: '',
    arrivalDate: '',
    actualArrivalDate: '',
    status: '',
    goodsDescription: '',
    remarks: '',
    goods: []
  })
  
  // 表单规则
  const rules = {
    waybillNo: [{ required: true, message: '请输入运单号', trigger: 'blur' }],
    supplier: [{ required: true, message: '请选择供应商', trigger: 'change' }],
    transportCompany: [{ required: true, message: '请输入运输公司', trigger: 'blur' }],
    vehicleNo: [{ required: true, message: '请输入车牌号', trigger: 'blur' }],
    driver: [{ required: true, message: '请输入司机姓名', trigger: 'blur' }],
    contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
    departureDate: [{ required: true, message: '请选择发车日期', trigger: 'change' }],
    arrivalDate: [{ required: true, message: '请选择预计到达日期', trigger: 'change' }],
    status: [{ required: true, message: '请选择状态', trigger: 'change' }]
  }
  
  const editFormRef = ref(null)
  
  // 获取状态对应的类型
  const getStatusType = (status) => {
    switch (status) {
      case '待发货':
        return 'info'
      case '运输中':
        return 'warning'
      case '已到达':
        return 'success'
      case '已签收':
        return 'success'
      default:
        return 'info'
    }
  }
  
  // 搜索
  const handleSearch = () => {
    ElMessage.success('搜索成功，这里应该调用后端API')
    // 实际项目中这里应该调用API获取数据
  }
  
  // 重置搜索
  const resetSearch = () => {
    Object.keys(searchForm).forEach(key => {
      searchForm[key] = ''
    })
    searchForm.dateRange = []
  }
  
  // 导出数据
  const exportData = () => {
    ElMessage.success('导出成功，这里应该调用后端API')
  }
  
  // 批量删除
  const batchDelete = () => {
    if (selectedItems.value.length === 0) {
      ElMessage.warning('请至少选择一条记录')
      return
    }
    
    ElMessageBox.confirm(`确定要删除选中的 ${selectedItems.value.length} 条记录吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      ElMessage.success(`成功删除 ${selectedItems.value.length} 条记录`)
      // 实际项目中这里应该调用API删除数据
    }).catch(() => {
      // 取消删除
    })
  }
  
  // 选择变更
  const handleSelectionChange = (selection) => {
    selectedItems.value = selection
  }
  
  // 查看详情
  const viewDetails = (row) => {
    currentItem.value = { ...row }
    detailsDialogVisible.value = true
  }
  
  // 编辑项目
  const editItem = (row) => {
    editMode.value = true
    Object.keys(editForm).forEach(key => {
      if (key === 'goods') {
        editForm.goods = JSON.parse(JSON.stringify(row.goods || []))
      } else {
        editForm[key] = row[key]
      }
    })
    editDialogVisible.value = true
  }
  
  // 删除项目
  const deleteItem = (row) => {
    ElMessageBox.confirm(`确定要删除运单号为 ${row.waybillNo} 的记录吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      ElMessage.success('删除成功')
      // 实际项目中这里应该调用API删除数据
    }).catch(() => {
      // 取消删除
    })
  }
  
  // 添加货物项
  const addGoodsItem = () => {
    editForm.goods.push({
      name: '',
      quantity: 1,
      unit: '件',
      weight: 0,
      volume: 0
    })
  }
  
  // 移除货物项
  const removeGoodsItem = (index) => {
    editForm.goods.splice(index, 1)
  }
  
  // 提交表单
  const submitForm = () => {
    editFormRef.value.validate((valid) => {
      if (valid) {
        ElMessage.success(editMode.value ? '更新成功' : '添加成功')
        editDialogVisible.value = false
        // 实际项目中这里应该调用API保存数据
      } else {
        return false
      }
    })
  }
  
  // 打印详情
  const printDetails = () => {
    ElMessage.success('准备打印')
    window.print()
  }
  
  // 分页处理
  const handleSizeChange = (size) => {
    pageSize.value = size
    // 重新加载数据
  }
  
  const handleCurrentChange = (page) => {
    currentPage.value = page
    // 重新加载数据
  }
  
  onMounted(() => {
    // 加载初始数据
    // 实际项目中这里应该调用API获取数据
  })
  </script>
  
  <style scoped>
  .manage-container {
    padding: 20px;
  }
  
  .search-section {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 4px;
  }
  
  .table-operations {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
  
  .details-container {
    padding: 10px;
  }
  
  .details-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .details-section {
    margin-top: 20px;
  }
  
  .details-section h3 {
    margin-bottom: 10px;
    font-size: 16px;
    font-weight: bold;
  }
  </style>
  
  