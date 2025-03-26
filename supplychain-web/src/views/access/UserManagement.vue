<template>
  <div class="access-management">
    <AccessManageTemplate
      title="用户管理"
      backRouteName="DashboardHome"
      :documents="users"
      titleField="username"
      titleColumnName="用户名"
      uploadTimeField="createTime"
      :documentIcon="UserIcon"
      searchPlaceholder="搜索用户名、姓名或手机号..."
      emptyText="暂无用户数据"
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
  import { UserIcon } from 'lucide-vue-next';
  import AccessManageTemplate from '@/components/templates/AccessManageTemplate.vue';
  
  export default {
  name: 'UserManagement',
  components: {
    AccessManageTemplate
  },
  data() {
    return {
      // 用户数据
      users: [],
      
      // 当前操作的用户
      currentUser: {},
      
      // 部门数据
      departments: [],
      
      // 分页
      currentPage: 1,
      pageSize: 10,
      totalUsers: 0,
      
      // 搜索和筛选
      searchQuery: '',
      filters: {
        deptId: '',
        status: ''
      },
      
      // 加载状态
      loading: false,
      
      // 图标
      UserIcon,
      
      // 列表列定义
      columns: [
        { key: 'realName', label: '姓名' },
        { key: 'phone', label: '手机号' },
        { key: 'deptName', label: '所属部门' },
        { 
          key: 'status', 
          label: '状态',
          formatter: (value) => value === 1 ? '启用' : '禁用'
        },
        { key: 'email', label: '邮箱' }
      ]
    }
  },
  computed: {
    totalPages() {
      return Math.ceil(this.totalUsers / this.pageSize);
    }
  },
  created() {
    this.fetchUsers();
    this.fetchDepartments();
  },
  methods: {
    // 获取用户列表
    fetchUsers() {
      this.loading = true;
      
      // 模拟API调用
      setTimeout(() => {
        // 模拟数据
        this.users = [
          {
            id: 1,
            username: 'admin',
            realName: '系统管理员',
            phone: '13800138000',
            email: 'admin@example.com',
            deptId: 1,
            deptName: '总公司',
            status: 1,
            createTime: '2023-01-01 00:00:00',
            remark: '超级管理员',
            roles: [
              { id: 1, name: '管理员' }
            ]
          },
          {
            id: 2,
            username: 'zhangsan',
            realName: '张三',
            phone: '13800138001',
            email: 'zhangsan@example.com',
            deptId: 2,
            deptName: '研发部',
            status: 1,
            createTime: '2023-01-02 00:00:00',
            remark: '开发人员',
            roles: [
              { id: 2, name: '开发人员' }
            ]
          },
          {
            id: 3,
            username: 'lisi',
            realName: '李四',
            phone: '13800138002',
            email: 'lisi@example.com',
            deptId: 3,
            deptName: '市场部',
            status: 1,
            createTime: '2023-01-03 00:00:00',
            remark: '市场专员',
            roles: [
              { id: 3, name: '市场专员' }
            ]
          },
          {
            id: 4,
            username: 'wangwu',
            realName: '王五',
            phone: '13800138003',
            email: 'wangwu@example.com',
            deptId: 4,
            deptName: '财务部',
            status: 1,
            createTime: '2023-01-04 00:00:00',
            remark: '财务人员',
            roles: [
              { id: 4, name: '财务人员' }
            ]
          },
          {
            id: 5,
            username: 'zhaoliu',
            realName: '赵六',
            phone: '13800138004',
            email: 'zhaoliu@example.com',
            deptId: 5,
            deptName: '前端组',
            status: 0,
            createTime: '2023-01-05 00:00:00',
            remark: '前端开发',
            roles: [
              { id: 2, name: '开发人员' },
              { id: 5, name: '测试人员' }
            ]
          }
        ];
        
        this.totalUsers = this.users.length;
        this.loading = false;
      }, 500);
    },
    
    // 获取部门列表
    fetchDepartments() {
      // 模拟API调用
      setTimeout(() => {
        // 模拟数据
        this.departments = [
          { id: 1, name: '总公司' },
          { id: 2, name: '研发部' },
          { id: 3, name: '市场部' },
          { id: 4, name: '财务部' },
          { id: 5, name: '前端组' },
          { id: 6, name: '后端组' },
          { id: 7, name: '测试组' }
        ];
      }, 500);
    },
    
    // 处理搜索
    handleSearch(query) {
      this.searchQuery = query;
      this.currentPage = 1;
      
      // 实际应用中这里会调用API
      console.log('搜索用户:', query);
      
      // 模拟搜索结果
      this.loading = true;
      setTimeout(() => {
        // 简单的客户端搜索模拟
        if (query) {
          this.users = this.users.filter(user => 
            user.username.includes(query) || 
            user.realName.includes(query) || 
            user.phone.includes(query)
          );
        } else {
          this.fetchUsers();
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
        this.fetchUsers();
        let filteredUsers = [...this.users];
        
        if (this.filters.deptId) {
          filteredUsers = filteredUsers.filter(user => 
            user.deptId === parseInt(this.filters.deptId)
          );
        }
        
        if (this.filters.status !== '') {
          filteredUsers = filteredUsers.filter(user => 
            user.status === parseInt(this.filters.status)
          );
        }
        
        this.users = filteredUsers;
        this.totalUsers = filteredUsers.length;
        this.loading = false;
      }, 500);
    },
    
    // 处理排序
    handleSort() {
      // 实际应用中这里会调用API
      console.log('排序用户');
      
      // 模拟排序
      this.users.sort((a, b) => a.username.localeCompare(b.username));
    },
    
    // 处理导出
    handleExport() {
      console.log('导出用户数据');
      // 实际应用中这里会调用导出API
      alert('用户数据导出功能将在后续版本中提供');
    },
    
    // 查看用户
    handleView(user) {
      console.log('查看用户:', user);
      // 实际应用中这里会跳转到用户详情页或打开详情模态框
      this.$router.push({ name: 'user-detail', params: { id: user.id } });
    },
    
    // 编辑用户
    handleEdit(user) {
      console.log('编辑用户:', user);
      // 实际应用中这里会跳转到用户编辑页
      this.$router.push({ name: 'user-edit', params: { id: user.id } });
    },
    
    // 确认删除用户
    confirmDelete(user) {
      if (confirm(`确定要删除用户 ${user.realName} (${user.username}) 吗？此操作不可恢复！`)) {
        this.deleteUser(user);
      }
    },
    
    // 删除用户
    deleteUser(user) {
      this.loading = true;
      
      // 模拟API调用
      setTimeout(() => {
        // 从列表中移除用户
        this.users = this.users.filter(item => item.id !== user.id);
        this.totalUsers = this.users.length;
        
        this.loading = false;
        
        // 显示成功消息
        alert('用户删除成功');
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
  
  