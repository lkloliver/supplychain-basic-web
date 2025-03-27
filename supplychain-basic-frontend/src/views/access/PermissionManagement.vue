<template>
    <div class="access-management">
      <AccessManageTemplate
        title="权限管理"
        backRouteName="DashboardHome"
        :documents="currentPermissions"
        titleField="name"
        titleColumnName="权限名称"
        :documentIcon="KeyIcon"
        searchPlaceholder="搜索权限名称或标识..."
        emptyText="暂无权限数据"
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
        <template #header-right>
          <div class="permission-type-switch">
            <button 
              class="btn" 
              :class="permissionType === 'operation' ? 'btn-primary' : 'btn-outline-secondary'"
              @click="switchPermissionType('operation')"
            >
              操作权限
            </button>
            <button 
              class="btn" 
              :class="permissionType === 'data' ? 'btn-primary' : 'btn-outline-secondary'"
              @click="switchPermissionType('data')"
            >
              数据权限
            </button>
          </div>
        </template>
      </AccessManageTemplate>
    </div>
    </template>
    
    <script>
    import { KeyIcon } from 'lucide-vue-next';
    import AccessManageTemplate from '@/components/templates/AccessManageTemplate.vue';
    
    export default {
    name: 'PermissionManagement',
    components: {
      AccessManageTemplate
    },
    data() {
      return {
        // 权限类型（操作权限/数据权限）
        permissionType: 'operation',
        
        // 权限数据
        operationPermissions: [],
        dataPermissions: [],
        
        // 分页
        currentPage: 1,
        pageSize: 10,
        
        // 搜索和筛选
        searchQuery: '',
        filters: {
          module: '',
          type: '',
          scope: ''
        },
        
        // 加载状态
        loading: false,
        
        // 图标
        KeyIcon,
        
        // 操作权限列
        operationColumns: [
          { key: 'code', label: '权限标识' },
          { key: 'type', label: '权限类型' },
          { key: 'module', label: '所属模块' },
          { key: 'description', label: '描述' }
        ],
        
        // 数据权限列
        dataColumns: [
          { key: 'code', label: '权限标识' },
          { key: 'scope', label: '数据范围' },
          { key: 'module', label: '所属模块' },
          { key: 'description', label: '描述' }
        ]
      }
    },
    computed: {
      // 当前显示的权限列表
      currentPermissions() {
        return this.permissionType === 'operation' ? this.operationPermissions : this.dataPermissions;
      },
      
      // 当前使用的列
      columns() {
        return this.permissionType === 'operation' ? this.operationColumns : this.dataColumns;
      },
      
      // 总页数
      totalPages() {
        const total = this.permissionType === 'operation' ? 
          this.operationPermissions.length : this.dataPermissions.length;
        return Math.ceil(total / this.pageSize);
      }
    },
    created() {
      this.fetchOperationPermissions();
      this.fetchDataPermissions();
    },
    methods: {
      // 获取操作权限列表
      fetchOperationPermissions() {
        this.loading = true;
        
        // 模拟API调用
        setTimeout(() => {
          // 模拟数据
          this.operationPermissions = [
            {
              id: 1,
              name: '用户查询',
              code: 'system:user:query',
              type: '按钮',
              module: '系统管理',
              description: '查询用户列表'
            },
            {
              id: 2,
              name: '用户新增',
              code: 'system:user:add',
              type: '按钮',
              module: '系统管理',
              description: '新增用户'
            },
            {
              id: 3,
              name: '用户编辑',
              code: 'system:user:edit',
              type: '按钮',
              module: '系统管理',
              description: '编辑用户'
            },
            {
              id: 4,
              name: '用户删除',
              code: 'system:user:delete',
              type: '按钮',
              module: '系统管理',
              description: '删除用户'
            },
            {
              id: 5,
              name: '角色查询',
              code: 'system:role:query',
              type: '按钮',
              module: '系统管理',
              description: '查询角色列表'
            },
            {
              id: 6,
              name: '角色新增',
              code: 'system:role:add',
              type: '按钮',
              module: '系统管理',
              description: '新增角色'
            },
            {
              id: 7,
              name: '角色编辑',
              code: 'system:role:edit',
              type: '按钮',
              module: '系统管理',
              description: '编辑角色'
            },
            {
              id: 8,
              name: '角色删除',
              code: 'system:role:delete',
              type: '按钮',
              module: '系统管理',
              description: '删除角色'
            },
            {
              id: 9,
              name: '采购合同查询',
              code: 'contract:purchase:query',
              type: '按钮',
              module: '合同管理',
              description: '查询采购合同列表'
            },
            {
              id: 10,
              name: '采购合同新增',
              code: 'contract:purchase:add',
              type: '按钮',
              module: '合同管理',
              description: '新增采购合同'
            }
          ];
          
          this.loading = false;
        }, 500);
      },
      
      // 获取数据权限列表
      fetchDataPermissions() {
        this.loading = true;
        
        // 模拟API调用
        setTimeout(() => {
          // 模拟数据
          this.dataPermissions = [
            {
              id: 1,
              name: '全部用户数据',
              code: 'data:user:all',
              scope: '全部数据',
              module: '系统管理',
              description: '可查看所有用户数据'
            },
            {
              id: 2,
              name: '部门用户数据',
              code: 'data:user:dept',
              scope: '部门数据',
              module: '系统管理',
              description: '只能查看本部门用户数据'
            },
            {
              id: 3,
              name: '个人用户数据',
              code: 'data:user:self',
              scope: '个人数据',
              module: '系统管理',
              description: '只能查看个人用户数据'
            },
            {
              id: 4,
              name: '全部合同数据',
              code: 'data:contract:all',
              scope: '全部数据',
              module: '合同管理',
              description: '可查看所有合同数据'
            },
            {
              id: 5,
              name: '部门合同数据',
              code: 'data:contract:dept',
              scope: '部门数据',
              module: '合同管理',
              description: '只能查看本部门合同数据'
            },
            {
              id: 6,
              name: '个人合同数据',
              code: 'data:contract:self',
              scope: '个人数据',
              module: '合同管理',
              description: '只能查看个人创建的合同数据'
            },
            {
              id: 7,
              name: '全部货物数据',
              code: 'data:goods:all',
              scope: '全部数据',
              module: '货物管理',
              description: '可查看所有货物数据'
            },
            {
              id: 8,
              name: '部门货物数据',
              code: 'data:goods:dept',
              scope: '部门数据',
              module: '货物管理',
              description: '只能查看本部门货物数据'
            },
            {
              id: 9,
              name: '个人货物数据',
              code: 'data:goods:self',
              scope: '个人数据',
              module: '货物管理',
              description: '只能查看个人创建的货物数据'
            }
          ];
          
          this.loading = false;
        }, 500);
      },
      
      // 切换权限类型
      switchPermissionType(type) {
        this.permissionType = type;
        this.currentPage = 1;
        this.searchQuery = '';
        this.filters = {
          module: '',
          type: '',
          scope: ''
        };
      },
      
      // 处理搜索
      handleSearch(query) {
        this.searchQuery = query;
        this.currentPage = 1;
        
        // 实际应用中这里会调用API
        console.log('搜索权限:', query);
        
        // 模拟搜索结果
        this.loading = true;
        setTimeout(() => {
          // 简单的客户端搜索模拟
          if (query) {
            if (this.permissionType === 'operation') {
              this.operationPermissions = this.operationPermissions.filter(perm => 
                perm.name.includes(query) || 
                perm.code.includes(query)
              );
            } else {
              this.dataPermissions = this.dataPermissions.filter(perm => 
                perm.name.includes(query) || 
                perm.code.includes(query)
              );
            }
          } else {
            if (this.permissionType === 'operation') {
              this.fetchOperationPermissions();
            } else {
              this.fetchDataPermissions();
            }
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
          if (this.permissionType === 'operation') {
            this.fetchOperationPermissions();
            let filteredPerms = [...this.operationPermissions];
            
            if (this.filters.module) {
              filteredPerms = filteredPerms.filter(perm => 
                perm.module === this.filters.module
              );
            }
            
            if (this.filters.type) {
              filteredPerms = filteredPerms.filter(perm => 
                perm.type === this.filters.type
              );
            }
            
            this.operationPermissions = filteredPerms;
          } else {
            this.fetchDataPermissions();
            let filteredPerms = [...this.dataPermissions];
            
            if (this.filters.module) {
              filteredPerms = filteredPerms.filter(perm => 
                perm.module === this.filters.module
              );
            }
            
            if (this.filters.scope) {
              filteredPerms = filteredPerms.filter(perm => 
                perm.scope === this.filters.scope
              );
            }
            
            this.dataPermissions = filteredPerms;
          }
          
          this.loading = false;
        }, 500);
      },
      
      // 处理排序
      handleSort() {
        // 实际应用中这里会调用API
        console.log('排序权限');
        
        // 模拟排序
        if (this.permissionType === 'operation') {
          this.operationPermissions.sort((a, b) => a.name.localeCompare(b.name));
        } else {
          this.dataPermissions.sort((a, b) => a.name.localeCompare(b.name));
        }
      },
      
      // 处理导出
      handleExport() {
        console.log('导出权限数据');
        // 实际应用中这里会调用导出API
        alert('权限数据导出功能将在后续版本中提供');
      },
      
      // 查看权限
      handleView(perm) {
        console.log('查看权限:', perm);
        // 实际应用中这里会跳转到权限详情页
        this.$router.push({ 
          name: this.permissionType === 'operation' ? 'operation-permission-detail' : 'data-permission-detail', 
          params: { id: perm.id } 
        });
      },
      
      // 编辑权限
      handleEdit(perm) {
        console.log('编辑权限:', perm);
        // 实际应用中这里会跳转到权限编辑页
        this.$router.push({ 
          name: this.permissionType === 'operation' ? 'operation-permission-edit' : 'data-permission-edit', 
          params: { id: perm.id } 
        });
      },
      
      // 确认删除权限
      confirmDelete(perm) {
        if (confirm(`确定要删除权限 ${perm.name} 吗？此操作不可恢复！\n警告：删除权限可能会影响现有角色的权限配置！`)) {
          this.deletePermission(perm);
        }
      },
      
      // 删除权限
      deletePermission(perm) {
        this.loading = true;
        
        // 模拟API调用
        setTimeout(() => {
          // 从列表中移除权限
          if (this.permissionType === 'operation') {
            this.operationPermissions = this.operationPermissions.filter(
              p => p.id !== perm.id
            );
          } else {
            this.dataPermissions = this.dataPermissions.filter(
              p => p.id !== perm.id
            );
          }
          
          this.loading = false;
          
          // 显示成功消息
          alert('权限删除成功');
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
    
    .permission-type-switch {
      display: flex;
      gap: 10px;
    }
    
    .permission-type-switch .btn {
      font-size: 14px;
      padding: 6px 12px;
    }
    
    .btn-primary {
      background-color: #1e88e5;
      color: white;
      border: 1px solid #1e88e5;
    }
    
    .btn-outline-secondary {
      background-color: transparent;
      color: #666;
      border: 1px solid #ddd;
    }
    </style>
    
    