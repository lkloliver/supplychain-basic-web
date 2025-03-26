<template>
    <div class="access-management">
      <AccessManageTemplate
        title="角色管理"
        backRouteName="DashboardHome"
        :documents="roles"
        titleField="name"
        titleColumnName="角色名称"
        uploadTimeField="createdAt"
        :documentIcon="ShieldIcon"
        searchPlaceholder="搜索角色名称..."
        emptyText="暂无角色数据"
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
    import { ShieldIcon } from 'lucide-vue-next';
    import AccessManageTemplate from '@/components/templates/AccessManageTemplate.vue';
    
    export default {
    name: 'RoleManagement',
    components: {
      AccessManageTemplate
    },
    data() {
      return {
        // 角色数据
        roles: [],
        
        // 分页
        currentPage: 1,
        pageSize: 10,
        totalRoles: 0,
        
        // 搜索和筛选
        searchQuery: '',
        filters: {
          status: ''
        },
        
        // 加载状态
        loading: false,
        
        // 图标
        ShieldIcon,
        
        // 列表列定义
        columns: [
          { key: 'description', label: '描述' },
          { 
            key: 'status', 
            label: '状态',
            formatter: (value) => value === 'active' ? '启用' : '禁用'
          },
          { 
            key: 'dataScope', 
            label: '数据权限范围',
            formatter: (value) => {
              const scopeMap = {
                'all': '全部数据权限',
                'department': '本部门数据权限',
                'departmentAndSub': '本部门及以下数据权限',
                'self': '仅本人数据权限',
                'custom': '自定义数据权限'
              };
              return scopeMap[value] || '未设置';
            }
          },
          { 
            key: 'createdAt', 
            label: '创建时间',
            formatter: (value) => {
              const date = new Date(value);
              return date.toLocaleDateString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
              });
            }
          }
        ]
      }
    },
    computed: {
      totalPages() {
        return Math.ceil(this.totalRoles / this.pageSize);
      }
    },
    created() {
      this.fetchRoles();
    },
    methods: {
      // 获取角色列表
      fetchRoles() {
        this.loading = true;
        
        // 模拟API调用
        setTimeout(() => {
          // 模拟数据
          this.roles = [
            {
              id: 1,
              name: '超级管理员',
              description: '系统最高权限，可以访问所有功能',
              createdAt: '2023-01-01T00:00:00Z',
              status: 'active',
              dataScope: 'all'
            },
            {
              id: 2,
              name: '采购管理员',
              description: '负责采购相关的管理工作',
              createdAt: '2023-01-02T00:00:00Z',
              status: 'active',
              dataScope: 'department'
            },
            {
              id: 3,
              name: '销售管理员',
              description: '负责销售相关的管理工作',
              createdAt: '2023-01-03T00:00:00Z',
              status: 'active',
              dataScope: 'department'
            },
            {
              id: 4,
              name: '财务管理员',
              description: '负责财务相关的管理工作',
              createdAt: '2023-01-04T00:00:00Z',
              status: 'active',
              dataScope: 'departmentAndSub'
            },
            {
              id: 5,
              name: '仓库管理员',
              description: '负责仓库相关的管理工作',
              createdAt: '2023-01-05T00:00:00Z',
              status: 'inactive',
              dataScope: 'self'
            }
          ];
          
          this.totalRoles = this.roles.length;
          this.loading = false;
        }, 500);
      },
      
      // 处理搜索
      handleSearch(query) {
        this.searchQuery = query;
        this.currentPage = 1;
        
        // 实际应用中这里会调用API
        console.log('搜索角色:', query);
        
        // 模拟搜索结果
        this.loading = true;
        setTimeout(() => {
          // 简单的客户端搜索模拟
          if (query) {
            this.roles = this.roles.filter(role => 
              role.name.includes(query) || 
              (role.description && role.description.includes(query))
            );
          } else {
            this.fetchRoles();
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
          this.fetchRoles();
          let filteredRoles = [...this.roles];
          
          if (this.filters.status) {
            filteredRoles = filteredRoles.filter(role => 
              role.status === this.filters.status
            );
          }
          
          this.roles = filteredRoles;
          this.totalRoles = filteredRoles.length;
          this.loading = false;
        }, 500);
      },
      
      // 处理排序
      handleSort() {
        // 实际应用中这里会调用API
        console.log('排序角色');
        
        // 模拟排序
        this.roles.sort((a, b) => a.name.localeCompare(b.name));
      },
      
      // 处理导出
      handleExport() {
        console.log('导出角色数据');
        // 实际应用中这里会调用导出API
        alert('角色数据导出功能将在后续版本中提供');
      },
      
      // 查看角色
      handleView(role) {
        console.log('查看角色:', role);
        // 实际应用中这里会跳转到角色详情页
        this.$router.push({ name: 'role-detail', params: { id: role.id } });
      },
      
      // 编辑角色
      handleEdit(role) {
        console.log('编辑角色:', role);
        // 实际应用中这里会跳转到角色编辑页
        this.$router.push({ name: 'role-edit', params: { id: role.id } });
      },
      
      // 确认删除角色
      confirmDelete(role) {
        if (confirm(`确定要删除角色 ${role.name} 吗？此操作不可恢复！\n警告：删除角色将影响所有拥有该角色的用户的权限！`)) {
          this.deleteRole(role);
        }
      },
      
      // 删除角色
      deleteRole(role) {
        this.loading = true;
        
        // 模拟API调用
        setTimeout(() => {
          // 从列表中移除角色
          this.roles = this.roles.filter(item => item.id !== role.id);
          this.totalRoles = this.roles.length;
          
          this.loading = false;
          
          // 显示成功消息
          alert('角色删除成功');
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
    
    