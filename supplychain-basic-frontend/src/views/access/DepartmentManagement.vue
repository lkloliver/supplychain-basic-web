<template>
    <div class="access-management">
      <AccessManageTemplate
        title="部门管理"
        backRouteName="DashboardHome"
        :documents="departments"
        titleField="name"
        titleColumnName="部门名称"
        uploadTimeField="createTime"
        :documentIcon="BuildingIcon"
        searchPlaceholder="搜索部门名称..."
        emptyText="暂无部门数据"
        :showFilterActions="true"
        :showPagination="true"
        :currentPage="currentPage"
        :totalPages="totalPages"
        :columns="columns"
        @search="handleSearch"
        @filter="handleFilter"
        @sort="handleSort"
        @export="handleExport"
        @view="handleView"
        @edit="handleEdit"
        @delete="confirmDelete"
        @page-change="handlePageChange"
      >
      </AccessManageTemplate>
    </div>
    </template>
    
    <script>
    import { BuildingIcon } from 'lucide-vue-next';
    import AccessManageTemplate from '@/components/templates/AccessManageTemplate.vue';
    
    export default {
    name: 'DepartmentManagement',
    components: {
      AccessManageTemplate
    },
    data() {
      return {
        // 部门数据
        departments: [],
        
        // 分页
        currentPage: 1,
        pageSize: 10,
        totalDepartments: 0,
        
        // 搜索和筛选
        searchQuery: '',
        filters: {
          parentId: '',
          status: ''
        },
        
        // 加载状态
        loading: false,
        
        // 图标
        BuildingIcon,
        
        // 列表列定义
        columns: [
          { key: 'parentName', label: '上级部门' },
          { key: 'manager', label: '负责人' },
          { key: 'phone', label: '联系电话' },
          { 
            key: 'status', 
            label: '状态',
            formatter: (value) => value === 1 ? '启用' : '禁用'
          },
          { key: 'orderNum', label: '显示顺序' }
        ]
      }
    },
    computed: {
      totalPages() {
        return Math.ceil(this.totalDepartments / this.pageSize);
      },
      
      // 可作为上级部门的部门列表（排除自身及子部门）
      parentDepartments() {
        return this.departments.filter(dept => dept.level === 1);
      }
    },
    created() {
      this.fetchDepartments();
    },
    methods: {
      // 获取部门列表
      fetchDepartments() {
        this.loading = true;
        
        // 模拟API调用
        setTimeout(() => {
          // 模拟数据
          this.departments = [
            {
              id: 1,
              name: '总公司',
              parentId: '',
              parentName: '',
              manager: '张三',
              phone: '13800138000',
              email: 'zhangsan@example.com',
              orderNum: 1,
              status: 1,
              createTime: '2023-01-01 00:00:00',
              remark: '总公司',
              level: 1,
              dataScope: 'all'
            },
            {
              id: 2,
              name: '研发部',
              parentId: 1,
              parentName: '总公司',
              manager: '李四',
              phone: '13800138001',
              email: 'lisi@example.com',
              orderNum: 1,
              status: 1,
              createTime: '2023-01-02 00:00:00',
              remark: '研发部门',
              level: 2,
              dataScope: 'custom'
            },
            {
              id: 3,
              name: '市场部',
              parentId: 1,
              parentName: '总公司',
              manager: '王五',
              phone: '13800138002',
              email: 'wangwu@example.com',
              orderNum: 2,
              status: 1,
              createTime: '2023-01-03 00:00:00',
              remark: '市场部门',
              level: 2,
              dataScope: 'department'
            },
            {
              id: 4,
              name: '财务部',
              parentId: 1,
              parentName: '总公司',
              manager: '赵六',
              phone: '13800138003',
              email: 'zhaoliu@example.com',
              orderNum: 3,
              status: 1,
              createTime: '2023-01-04 00:00:00',
              remark: '财务部门',
              level: 2,
              dataScope: 'all'
            },
            {
              id: 5,
              name: '前端组',
              parentId: 2,
              parentName: '研发部',
              manager: '小明',
              phone: '13800138004',
              email: 'xiaoming@example.com',
              orderNum: 1,
              status: 1,
              createTime: '2023-01-05 00:00:00',
              remark: '前端开发组',
              level: 3,
              dataScope: 'all'
            },
            {
              id: 6,
              name: '后端组',
              parentId: 2,
              parentName: '研发部',
              manager: '小红',
              phone: '13800138005',
              email: 'xiaohong@example.com',
              orderNum: 2,
              status: 1,
              createTime: '2023-01-06 00:00:00',
              remark: '后端开发组',
              level: 3,
              dataScope: 'all'
            },
            {
              id: 7,
              name: '测试组',
              parentId: 2,
              parentName: '研发部',
              manager: '小刚',
              phone: '13800138006',
              email: 'xiaogang@example.com',
              orderNum: 3,
              status: 0,
              createTime: '2023-01-07 00:00:00',
              remark: '测试组',
              level: 3,
              dataScope: 'all'
            }
          ];
          
          this.totalDepartments = this.departments.length;
          this.loading = false;
        }, 500);
      },
      
      // 处理搜索
      handleSearch(query) {
        this.searchQuery = query;
        this.currentPage = 1;
        
        // 实际应用中这里会调用API
        console.log('搜索部门:', query);
        
        // 模拟搜索结果
        this.loading = true;
        setTimeout(() => {
          // 简单的客户端搜索模拟
          if (query) {
            this.departments = this.departments.filter(dept => 
              dept.name.includes(query) || 
              (dept.manager && dept.manager.includes(query))
            );
          } else {
            this.fetchDepartments();
          }
          this.loading = false;
        }, 500);
      },
      
      // 处理筛选
      handleFilter() {
        // 简化筛选逻辑，直接应用默认筛选
        this.applyFilter();
      },
      
      // 应用筛选
      applyFilter() {
        this.currentPage = 1;
        
        // 实际应用中这里会调用API
        console.log('筛选条件:', this.filters);
        
        // 模拟筛选结果
        this.loading = true;
        setTimeout(() => {
          // 简单的客户端筛选模拟
          this.fetchDepartments();
          let filteredDepts = [...this.departments];
          
          if (this.filters.parentId) {
            if (this.filters.parentId === '0') {
              filteredDepts = filteredDepts.filter(dept => !dept.parentId);
            } else {
              filteredDepts = filteredDepts.filter(dept => 
                dept.parentId === parseInt(this.filters.parentId)
              );
            }
          }
          
          if (this.filters.status !== '') {
            filteredDepts = filteredDepts.filter(dept => 
              dept.status === parseInt(this.filters.status)
            );
          }
          
          this.departments = filteredDepts;
          this.totalDepartments = filteredDepts.length;
          this.loading = false;
        }, 500);
      },
      
      // 处理排序
      handleSort() {
        // 实际应用中这里会调用API
        console.log('排序部门');
        
        // 模拟排序
        this.departments.sort((a, b) => a.orderNum - b.orderNum);
      },
      
      // 处理导出
      handleExport() {
        console.log('导出部门数据');
        // 实际应用中这里会调用导出API
        alert('部门数据导出功能将在后续版本中提供');
      },
      
      // 查看部门
      handleView(dept) {
        console.log('查看部门:', dept);
        // 实际应用中这里会跳转到部门详情页
        this.$router.push({ name: 'department-detail', params: { id: dept.id } });
      },
      
      // 编辑部门
      handleEdit(dept) {
        console.log('编辑部门:', dept);
        // 实际应用中这里会跳转到部门编辑页
        this.$router.push({ name: 'department-edit', params: { id: dept.id } });
      },
      
      // 确认删除部门
      confirmDelete(dept) {
        // 检查是否有子部门
        const hasChildDepartments = this.departments.some(d => d.parentId === dept.id);
        
        let message = `确定要删除部门 ${dept.name} 吗？此操作不可恢复！`;
        if (hasChildDepartments) {
          message += '\n警告：该部门下有子部门，删除将导致子部门一并删除！';
        }
        
        if (confirm(message)) {
          this.deleteDepartment(dept);
        }
      },
      
      // 删除部门
      deleteDepartment(dept) {
        this.loading = true;
        
        // 模拟API调用
        setTimeout(() => {
          // 递归删除部门及其子部门
          const deleteDeptIds = [dept.id];
          
          const findChildDepts = (parentId) => {
            const childDepts = this.departments.filter(d => d.parentId === parentId);
            childDepts.forEach(child => {
              deleteDeptIds.push(child.id);
              findChildDepts(child.id);
            });
          };
          
          findChildDepts(dept.id);
          
          // 过滤掉要删除的部门
          this.departments = this.departments.filter(d => !deleteDeptIds.includes(d.id));
          this.totalDepartments = this.departments.length;
          
          this.loading = false;
          
          // 显示成功消息
          alert('部门删除成功');
        }, 800);
      },
      
      // 处理页码变化
      handlePageChange(page) {
        this.currentPage = page;
        
        // 实际应用中这里会调用API获取对应页的数据
        console.log('切换到页码:', page);
        
        // 模拟分页
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
        }, 500);
      }
    }
    }
    </script>
    
    <style scoped>
    .access-management {
      padding: 0;
    }
    </style>
    
    