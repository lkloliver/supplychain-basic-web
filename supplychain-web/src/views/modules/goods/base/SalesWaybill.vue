<template>
    <div class="sales-waybill-container">
      <h1>销售运单</h1>
      
      <el-tabs v-model="activeTab" class="module-tabs">
        <el-tab-pane label="运单概览" name="overview">
          <div class="overview-container">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-card class="overview-card">
                  <template #header>
                    <div class="card-header">
                      <span>待发货运单</span>
                      <el-tag type="info">{{ statistics.pending }}</el-tag>
                    </div>
                  </template>
                  <div class="card-content">
                    <el-progress type="dashboard" :percentage="statistics.pendingPercentage" :color="colors.pending"></el-progress>
                    <div class="progress-label">占比 {{ statistics.pendingPercentage }}%</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="overview-card">
                  <template #header>
                    <div class="card-header">
                      <span>运输中运单</span>
                      <el-tag type="warning">{{ statistics.inTransit }}</el-tag>
                    </div>
                  </template>
                  <div class="card-content">
                    <el-progress type="dashboard" :percentage="statistics.inTransitPercentage" :color="colors.inTransit"></el-progress>
                    <div class="progress-label">占比 {{ statistics.inTransitPercentage }}%</div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="overview-card">
                  <template #header>
                    <div class="card-header">
                      <span>已完成运单</span>
                      <el-tag type="success">{{ statistics.completed }}</el-tag>
                    </div>
                  </template>
                  <div class="card-content">
                    <el-progress type="dashboard" :percentage="statistics.completedPercentage" :color="colors.completed"></el-progress>
                    <div class="progress-label">占比 {{ statistics.completedPercentage }}%</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            
            <div class="chart-container">
              <el-card class="chart-card">
                <template #header>
                  <div class="card-header">
                    <span>近30天运单统计</span>
                  </div>
                </template>
                <div class="chart" ref="lineChartRef"></div>
              </el-card>
            </div>
            
            <div class="recent-waybills">
              <h2>最近运单</h2>
              <el-table :data="recentWaybills" style="width: 100%">
                <el-table-column prop="waybillNo" label="运单号" width="180"></el-table-column>
                <el-table-column prop="customer" label="客户" width="180"></el-table-column>
                <el-table-column prop="destination" label="目的地" width="180"></el-table-column>
                <el-table-column prop="departureDate" label="发车日期" width="120"></el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="scope">
                    <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="scope">
                    <el-button size="small" @click="viewWaybillDetails(scope.row)">查看详情</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="运单列表" name="list">
          <div class="list-container">
            <div class="search-section">
              <el-form :inline="true" :model="searchForm" class="search-form">
                <el-form-item label="运单号">
                  <el-input v-model="searchForm.waybillNo" placeholder="请输入运单号"></el-input>
                </el-form-item>
                <el-form-item label="客户">
                  <el-select v-model="searchForm.customer" placeholder="请选择客户" clearable>
                    <el-option v-for="item in customerOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="状态">
                  <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                    <el-option label="待发货" value="待发货"></el-option>
                    <el-option label="运输中" value="运输中"></el-option>
                    <el-option label="已到达" value="已到达"></el-option>
                    <el-option label="已签收" value="已签收"></el-option>
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
              <el-button type="primary" @click="navigateToUpload">新增运单</el-button>
              <el-button type="success" @click="exportData">导出数据</el-button>
            </div>
            
            <el-table :data="waybillList" border style="width: 100%">
              <el-table-column prop="waybillNo" label="运单号" width="180"></el-table-column>
              <el-table-column prop="customer" label="客户" width="180"></el-table-column>
              <el-table-column prop="destination" label="目的地" width="180"></el-table-column>
              <el-table-column prop="transportCompany" label="运输公司" width="180"></el-table-column>
              <el-table-column prop="vehicleNo" label="车牌号" width="120"></el-table-column>
              <el-table-column prop="departureDate" label="发车日期" width="120"></el-table-column>
              <el-table-column prop="estimatedArrivalDate" label="预计到达日期" width="120"></el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" @click="viewWaybillDetails(scope.row)">查看</el-button>
                  <el-button size="small" type="primary" @click="editWaybill(scope.row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteWaybill(scope.row)">删除</el-button>
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
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="数据分析" name="analysis">
          <div class="analysis-container">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card class="chart-card">
                  <template #header>
                    <div class="card-header">
                      <span>运单状态分布</span>
                    </div>
                  </template>
                  <div class="chart" ref="pieChartRef"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="chart-card">
                  <template #header>
                    <div class="card-header">
                      <span>客户运单数量排名</span>
                    </div>
                  </template>
                  <div class="chart" ref="barChartRef"></div>
                </el-card>
              </el-col>
            </el-row>
            
            <el-card class="chart-card">
              <template #header>
                <div class="card-header">
                  <span>月度运单统计</span>
                </div>
              </template>
              <div class="chart" ref="monthlyChartRef"></div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
      
      <!-- 运单详情对话框 -->
      <el-dialog title="运单详情" v-model="detailsDialogVisible" width="70%">
        <div class="details-container" v-if="currentWaybill">
          <div class="details-header">
            <h2>运单号: {{ currentWaybill.waybillNo }}</h2>
            <el-tag :type="getStatusType(currentWaybill.status)">{{ currentWaybill.status }}</el-tag>
          </div>
          
          <el-descriptions :column="2" border>
            <el-descriptions-item label="客户">{{ currentWaybill.customer }}</el-descriptions-item>
            <el-descriptions-item label="目的地">{{ currentWaybill.destination }}</el-descriptions-item>
            <el-descriptions-item label="运输公司">{{ currentWaybill.transportCompany }}</el-descriptions-item>
            <el-descriptions-item label="车牌号">{{ currentWaybill.vehicleNo }}</el-descriptions-item>
            <el-descriptions-item label="司机">{{ currentWaybill.driver }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentWaybill.contactPhone }}</el-descriptions-item>
            <el-descriptions-item label="发车日期">{{ currentWaybill.departureDate }}</el-descriptions-item>
            <el-descriptions-item label="预计到达日期">{{ currentWaybill.estimatedArrivalDate }}</el-descriptions-item>
            <el-descriptions-item label="实际到达日期">{{ currentWaybill.actualArrivalDate || '未到达' }}</el-descriptions-item>
            <el-descriptions-item label="货物描述" :span="2">{{ currentWaybill.goodsDescription }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ currentWaybill.remarks || '无' }}</el-descriptions-item>
          </el-descriptions>
          
          <div class="details-section">
            <h3>货物明细</h3>
            <el-table :data="currentWaybill.goods || []" border style="width: 100%">
              <el-table-column prop="name" label="货物名称"></el-table-column>
              <el-table-column prop="quantity" label="数量"></el-table-column>
              <el-table-column prop="unit" label="单位"></el-table-column>
              <el-table-column prop="weight" label="重量(kg)"></el-table-column>
              <el-table-column prop="volume" label="体积(m³)"></el-table-column>
            </el-table>
          </div>
          
          <div class="details-section">
            <h3>运输轨迹</h3>
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in currentWaybill.trackingInfo"
                :key="index"
                :timestamp="activity.time"
                :type="activity.type"
                :color="activity.color"
              >
                {{ activity.content }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="detailsDialogVisible = false">关闭</el-button>
            <el-button type="primary" @click="printDetails">打印</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted, nextTick } from 'vue'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import * as echarts from 'echarts'
  
  const router = useRouter()
  const activeTab = ref('overview')
  
  // 图表引用
  const lineChartRef = ref(null)
  const pieChartRef = ref(null)
  const barChartRef = ref(null)
  const monthlyChartRef = ref(null)
  
  // 统计数据
  const statistics = reactive({
    pending: 15,
    inTransit: 28,
    completed: 57,
    total: 100,
    pendingPercentage: 15,
    inTransitPercentage: 28,
    completedPercentage: 57
  })
  
  // 颜色配置
  const colors = {
    pending: '#909399',
    inTransit: '#E6A23C',
    completed: '#67C23A'
  }
  
  // 客户选项
  const customerOptions = [
    { value: '客户A', label: '客户A' },
    { value: '客户B', label: '客户B' },
    { value: '客户C', label: '客户C' },
  ]
  
  // 搜索表单
  const searchForm = reactive({
    waybillNo: '',
    customer: '',
    status: '',
    dateRange: []
  })
  
  // 最近运单数据
  const recentWaybills = ref([
    {
      waybillNo: 'SW20230001',
      customer: '客户A',
      destination: '北京市',
      departureDate: '2023-01-10',
      status: '已签收',
      transportCompany: '快速物流公司',
      vehicleNo: '京A12345',
      driver: '张三',
      contactPhone: '13800138000',
      estimatedArrivalDate: '2023-01-12',
      actualArrivalDate: '2023-01-12',
      goodsDescription: '加工食品',
      remarks: '按时送达',
      goods: [
        { name: '面粉', quantity: 100, unit: '袋', weight: 5000, volume: 10 },
        { name: '食用油', quantity: 50, unit: '箱', weight: 1000, volume: 5 }
      ],
      trackingInfo: [
        { time: '2023-01-10 08:00', content: '货物已装车', type: 'primary', color: '#409EFF' },
        { time: '2023-01-10 09:30', content: '运输车辆已出发', type: 'primary', color: '#409EFF' },
        { time: '2023-01-11 12:00', content: '运输中', type: 'warning', color: '#E6A23C' },
        { time: '2023-01-12 10:00', content: '已到达目的地', type: 'success', color: '#67C23A' },
        { time: '2023-01-12 14:00', content: '客户已签收', type: 'success', color: '#67C23A' }
      ]
    },
    {
      waybillNo: 'SW20230002',
      customer: '客户B',
      destination: '上海市',
      departureDate: '2023-01-15',
      status: '运输中',
      transportCompany: '安全运输有限公司',
      vehicleNo: '沪B54321',
      driver: '李四',
      contactPhone: '13900139000',
      estimatedArrivalDate: '2023-01-18',
      actualArrivalDate: null,
      goodsDescription: '加工食品',
      remarks: '',
      goods: [
        { name: '饼干', quantity: 200, unit: '箱', weight: 2000, volume: 8 }
      ],
      trackingInfo: [
        { time: '2023-01-15 08:00', content: '货物已装车', type: 'primary', color: '#409EFF' },
        { time: '2023-01-15 09:30', content: '运输车辆已出发', type: 'primary', color: '#409EFF' },
        { time: '2023-01-16 12:00', content: '运输中', type: 'warning', color: '#E6A23C' }
      ]
    }
  ])
  
  // 运单列表数据
  const waybillList = ref([...recentWaybills.value])
  
  // 分页
  const total = ref(100)
  const pageSize = ref(10)
  const currentPage = ref(1)
  
  // 详情对话框
  const detailsDialogVisible = ref(false)
  const currentWaybill = ref(null)
  
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
  
  // 查看运单详情
  const viewWaybillDetails = (row) => {
    currentWaybill.value = { ...row }
    detailsDialogVisible.value = true
  }
  
  // 编辑运单
  const editWaybill = (row) => {
    ElMessage.info('编辑运单功能待实现')
    // 实际项目中可以跳转到编辑页面或打开编辑对话框
  }
  
  // 删除运单
  const deleteWaybill = (row) => {
    ElMessage.success(`删除运单 ${row.waybillNo} 成功`)
    // 实际项目中应该调用API删除数据
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
  
  // 跳转到上传页面
  const navigateToUpload = () => {
    router.push('/dashboard/goods/base/sales-waybill/upload')
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
  
  // 初始化图表
  const initCharts = () => {
    // 近30天运单统计折线图
    const lineChart = echarts.init(lineChartRef.value)
    const lineOption = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['新增运单', '完成运单']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: Array.from({ length: 30 }, (_, i) => {
          const date = new Date()
          date.setDate(date.getDate() - 29 + i)
          return `${date.getMonth() + 1}/${date.getDate()}`
        })
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '新增运单',
          type: 'line',
          data: Array.from({ length: 30 }, () => Math.floor(Math.random() * 10))
        },
        {
          name: '完成运单',
          type: 'line',
          data: Array.from({ length: 30 }, () => Math.floor(Math.random() * 8))
        }
      ]
    }
    lineChart.setOption(lineOption)
    
    // 运单状态分布饼图
    const pieChart = echarts.init(pieChartRef.value)
    const pieOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 10,
        data: ['待发货', '运输中', '已到达', '已签收']
      },
      series: [
        {
          name: '运单状态',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            { value: 15, name: '待发货' },
            { value: 28, name: '运输中' },
            { value: 12, name: '已到达' },
            { value: 45, name: '已签收' }
          ]
        }
      ]
    }
    pieChart.setOption(pieOption)
    
    // 客户运单数量排名柱状图
    const barChart = echarts.init(barChartRef.value)
    const barOption = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01]
      },
      yAxis: {
        type: 'category',
        data: ['客户F', '客户E', '客户D', '客户C', '客户B', '客户A']
      },
      series: [
        {
          name: '运单数量',
          type: 'bar',
          data: [4, 8, 15, 22, 33, 48]
        }
      ]
    }
    barChart.setOption(barOption)
    
    // 月度运单统计
    const monthlyChart = echarts.init(monthlyChartRef.value)
    const monthlyOption = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['新增运单', '完成运单']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '新增运单',
          type: 'bar',
          data: [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330]
        },
        {
          name: '完成运单',
          type: 'bar',
          data: [110, 122, 91, 124, 80, 220, 200, 172, 181, 224, 280, 320]
        }
      ]
    }
    monthlyChart.setOption(monthlyOption)
    
    // 窗口大小变化时重新调整图表大小
    window.addEventListener('resize', () => {
      lineChart.resize()
      pieChart.resize()
      barChart.resize()
      monthlyChart.resize()
    })
  }
  
  onMounted(() => {
    // 等待DOM渲染完成后初始化图表
    nextTick(() => {
      initCharts()
    })
  })
  </script>
  
  <style scoped>
  .sales-waybill-container {
    padding: 20px;
  }
  
  .module-tabs {
    margin-top: 20px;
  }
  
  .overview-container {
    margin-top: 20px;
  }
  
  .overview-card {
    height: 100%;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .card-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px 0;
  }
  
  .progress-label {
    margin-top: 10px;
    font-size: 14px;
    color: #606266;
  }
  
  .chart-container {
    margin-top: 20px;
  }
  
  .chart-card {
    margin-bottom: 20px;
  }
  
  .chart {
    height: 300px;
  }
  
  .recent-waybills {
    margin-top: 20px;
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
  
  .analysis-container {
    margin-top: 20px;
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
  
  