<template>
    <div class="role-management">
      <div class="card">
        <div class="card-header">
          <h5 class="card-title">角色管理</h5>
          <p class="card-subtitle mb-2 text-muted">管理系统角色及其对应的权限</p>
          <div class="card-tools">
            <button type="button" class="btn btn-primary btn-sm" @click="openAddRoleModal">
              <i class="fas fa-plus"></i> 新增角色
            </button>
          </div>
        </div>
        <div class="card-body">
          <div class="filter-section">
            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <label>角色名称</label>
                  <input type="text" class="form-control" v-model="filters.name" placeholder="搜索角色名称">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label>状态</label>
                  <select class="form-control" v-model="filters.status">
                    <option value="">全部</option>
                    <option value="active">启用</option>
                    <option value="inactive">禁用</option>
                  </select>
                </div>
              </div>
              <div class="col-md-4 d-flex align-items-end">
                <button class="btn btn-primary mr-2" @click="searchRoles">搜索</button>
                <button class="btn btn-secondary" @click="resetFilters">重置</button>
              </div>
            </div>
          </div>
    
          <div class="table-responsive mt-4">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>角色名称</th>
                  <th>描述</th>
                  <th>创建时间</th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="role in roles" :key="role.id">
                  <td>{{ role.name }}</td>
                  <td>{{ role.description }}</td>
                  <td>{{ formatDate(role.createdAt) }}</td>
                  <td>
                    <span :class="role.status === 'active' ? 'badge badge-success' : 'badge badge-danger'">
                      {{ role.status === 'active' ? '启用' : '禁用' }}
                    </span>
                  </td>
                  <td>
                    <div class="btn-group">
                      <button type="button" class="btn btn-info btn-xs" @click="openEditRoleModal(role)">
                        <i class="fas fa-edit"></i>
                      </button>
                      <button type="button" class="btn btn-primary btn-xs" @click="openPermissionModal(role)">
                        <i class="fas fa-key"></i> 权限设置
                      </button>
                      <button type="button" class="btn btn-danger btn-xs" @click="confirmDeleteRole(role)">
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
    
          <div class="pagination-container mt-4">
            <nav>
              <ul class="pagination">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                  <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">上一页</a>
                </li>
                <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
                  <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                  <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">下一页</a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    
      <!-- 添加/编辑角色模态框 -->
      <div class="modal fade" id="roleModal" tabindex="-1" role="dialog" aria-labelledby="roleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="roleModalLabel">{{ isEditing ? '编辑角色' : '添加角色' }}</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="saveRole">
                <div class="form-group">
                  <label for="roleName">角色名称 <span class="text-danger">*</span></label>
                  <input type="text" class="form-control" id="roleName" v-model="currentRole.name" required>
                </div>
                <div class="form-group">
                  <label for="roleDescription">描述</label>
                  <textarea class="form-control" id="roleDescription" v-model="currentRole.description" rows="3"></textarea>
                </div>
                <div class="form-group">
                  <label for="roleStatus">状态</label>
                  <select class="form-control" id="roleStatus" v-model="currentRole.status">
                    <option value="active">启用</option>
                    <option value="inactive">禁用</option>
                  </select>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                  <button type="submit" class="btn btn-primary">保存</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    
      <!-- 角色权限模态框 -->
      <div class="modal fade" id="permissionModal" tabindex="-1" role="dialog" aria-labelledby="permissionModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="permissionModalLabel">角色权限设置 - {{ currentRole.name }}</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <div class="alert alert-info">
                <i class="fas fa-info-circle"></i>
                角色权限决定了拥有该角色的用户可以执行的操作和访问的数据范围。
              </div>
    
              <ul class="nav nav-tabs" id="permissionTab" role="tablist">
                <li class="nav-item">
                  <a class="nav-link active" id="menu-tab" data-toggle="tab" href="#menu" role="tab" aria-controls="menu" aria-selected="true">
                    菜单权限
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" id="operation-tab" data-toggle="tab" href="#operation" role="tab" aria-controls="operation" aria-selected="false">
                    操作权限
                  </a>
                </li>
                <li class="nav-item">
                  <a class="nav-link" id="data-tab" data-toggle="tab" href="#data" role="tab" aria-controls="data" aria-selected="false">
                    数据权限
                  </a>
                </li>
              </ul>
    
              <div class="tab-content mt-3" id="permissionTabContent">
                <!-- 菜单权限 -->
                <div class="tab-pane fade show active" id="menu" role="tabpanel" aria-labelledby="menu-tab">
                  <div class="permission-tree">
                    <div v-for="module in menuPermissions" :key="module.id" class="permission-module">
                      <div class="permission-module-header">
                        <div class="form-check">
                          <input class="form-check-input" type="checkbox"
                                 :id="'module-' + module.id"
                                 v-model="module.checked"
                                 @change="toggleModulePermission(module)">
                          <label class="form-check-label" :for="'module-' + module.id">
                            {{ module.name }}
                          </label>
                        </div>
                      </div>
                      <div class="permission-module-items ml-4">
                        <div v-for="item in module.items" :key="item.id" class="form-check">
                          <input class="form-check-input" type="checkbox"
                                 :id="'item-' + item.id"
                                 v-model="item.checked"
                                 @change="updateModuleCheckStatus(module)">
                          <label class="form-check-label" :for="'item-' + item.id">
                            {{ item.name }}
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
    
                <!-- 操作权限 -->
                <div class="tab-pane fade" id="operation" role="tabpanel" aria-labelledby="operation-tab">
                  <div class="permission-list">
                    <div v-for="(group, groupName) in operationPermissions" :key="groupName" class="permission-group mb-4">
                      <h6>{{ groupName }}</h6>
                      <div class="row">
                        <div v-for="permission in group" :key="permission.id" class="col-md-4 mb-2">
                          <div class="form-check">
                            <input class="form-check-input" type="checkbox"
                                   :id="'op-' + permission.id"
                                   v-model="permission.checked">
                            <label class="form-check-label" :for="'op-' + permission.id">
                              {{ permission.name }}
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
    
                <!-- 数据权限 -->
                <div class="tab-pane fade" id="data" role="tabpanel" aria-labelledby="data-tab">
                  <div class="data-permission">
                    <div class="form-group">
                      <label>数据权限范围</label>
                      <select class="form-control" v-model="currentRole.dataScope">
                        <option value="all">全部数据权限</option>
                        <option value="department">本部门数据权限</option>
                        <option value="departmentAndSub">本部门及以下数据权限</option>
                        <option value="self">仅本人数据权限</option>
                        <option value="custom">自定义数据权限</option>
                      </select>
                    </div>
    
                    <div v-if="currentRole.dataScope === 'custom'" class="custom-departments mt-3">
                      <h6>选择部门</h6>
                      <div class="department-tree">
                        <div v-for="dept in departments" :key="dept.id" class="department-item">
                          <div class="form-check">
                            <input class="form-check-input" type="checkbox"
                                   :id="'dept-' + dept.id"
                                   v-model="selectedDepartments"
                                   :value="dept.id">
                            <label class="form-check-label" :for="'dept-' + dept.id">
                              {{ dept.name }}
                            </label>
                          </div>
                          <div v-if="dept.children && dept.children.length" class="ml-4">
                            <div v-for="child in dept.children" :key="child.id" class="form-check">
                              <input class="form-check-input" type="checkbox"
                                     :id="'dept-' + child.id"
                                     v-model="selectedDepartments"
                                     :value="child.id">
                              <label class="form-check-label" :for="'dept-' + child.id">
                                {{ child.name }}
                              </label>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
              <button type="button" class="btn btn-primary" @click="savePermissions">保存权限</button>
            </div>
          </div>
        </div>
      </div>
    
      <!-- 删除确认模态框 -->
      <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="deleteModalLabel">确认删除</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              您确定要删除角色 <strong>{{ currentRole.name }}</strong> 吗？此操作不可逆。
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
              <button type="button" class="btn btn-danger" @click="deleteRole">确认删除</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    </template>
    
    <script>
    export default {
      name: 'RoleManagement',
      data() {
        return {
          roles: [],
          currentPage: 1,
          totalPages: 5,
          filters: {
            name: '',
            status: ''
          },
          currentRole: {
            id: null,
            name: '',
            description: '',
            status: 'active',
            dataScope: 'all'
          },
          isEditing: false,
          menuPermissions: [
            {
              id: 1,
              name: '合同管理',
              checked: false,
              items: [
                { id: 101, name: '采购合同', checked: false },
                { id: 102, name: '销售合同', checked: false },
                { id: 103, name: '运输合同', checked: false }
              ]
            },
            {
              id: 2,
              name: '货物管理',
              checked: false,
              items: [
                { id: 201, name: '入库重量', checked: false },
                { id: 202, name: '出库重量', checked: false },
                { id: 203, name: '入库质检', checked: false },
                { id: 204, name: '出库质检', checked: false },
                { id: 205, name: '入库图片', checked: false },
                { id: 206, name: '出库图片', checked: false },
                { id: 207, name: '采购运单', checked: false },
                { id: 208, name: '销售运单', checked: false }
              ]
            },
            {
              id: 3,
              name: '结算管理',
              checked: false,
              items: [
                { id: 301, name: '采购结算', checked: false },
                { id: 302, name: '销售结算', checked: false }
              ]
            },
            {
              id: 4,
              name: '财务管理',
              checked: false,
              items: [
                { id: 401, name: '采购付款', checked: false },
                { id: 402, name: '销售收款', checked: false },
                { id: 403, name: '运输付款', checked: false }
              ]
            },
            {
              id: 5,
              name: '发票管理',
              checked: false,
              items: [
                { id: 501, name: '采购进项发票', checked: false },
                { id: 502, name: '采购销项发票', checked: false },
                { id: 503, name: '销售发票', checked: false },
                { id: 504, name: '运输发票', checked: false }
              ]
            },
            {
              id: 6,
              name: '台账管理',
              checked: false,
              items: [
                { id: 601, name: '采购台账', checked: false },
                { id: 602, name: '销售台账', checked: false }
              ]
            },
            {
              id: 7,
              name: '访问控制',
              checked: false,
              items: [
                { id: 701, name: '用户管理', checked: false },
                { id: 702, name: '部门管理', checked: false },
                { id: 703, name: '角色管理', checked: false },
                { id: 704, name: '权限管理', checked: false }
              ]
            }
          ],
          operationPermissions: {
            '合同管理': [
              { id: 1001, name: '查看', checked: false },
              { id: 1002, name: '新增', checked: false },
              { id: 1003, name: '编辑', checked: false },
              { id: 1004, name: '删除', checked: false },
              { id: 1005, name: '导出', checked: false },
              { id: 1006, name: '审批', checked: false }
            ],
            '货物管理': [
              { id: 2001, name: '查看', checked: false },
              { id: 2002, name: '新增', checked: false },
              { id: 2003, name: '编辑', checked: false },
              { id: 2004, name: '删除', checked: false },
              { id: 2005, name: '导出', checked: false }
            ],
            '结算管理': [
              { id: 3001, name: '查看', checked: false },
              { id: 3002, name: '新增', checked: false },
              { id: 3003, name: '编辑', checked: false },
              { id: 3004, name: '删除', checked: false },
              { id: 3005, name: '导出', checked: false },
              { id: 3006, name: '审批', checked: false }
            ],
            '财务管理': [
              { id: 4001, name: '查看', checked: false },
              { id: 4002, name: '新增', checked: false },
              { id: 4003, name: '编辑', checked: false },
              { id: 4004, name: '删除', checked: false },
              { id: 4005, name: '导出', checked: false },
              { id: 4006, name: '审批', checked: false }
            ]
          },
          departments: [
            {
              id: 1,
              name: '总公司',
              children: [
                { id: 11, name: '财务部' },
                { id: 12, name: '人事部' },
                { id: 13, name: '技术部' }
              ]
            },
            {
              id: 2,
              name: '北京分公司',
              children: [
                { id: 21, name: '销售部' },
                { id: 22, name: '采购部' }
              ]
            },
            {
              id: 3,
              name: '上海分公司',
              children: [
                { id: 31, name: '销售部' },
                { id: 32, name: '采购部' }
              ]
            }
          ],
          selectedDepartments: []
        };
      },
      created() {
        this.loadRoles();
      },
      methods: {
        loadRoles() {
          // 模拟从API获取角色数据
          this.roles = [
            {
              id: 1,
              name: '超级管理员',
              description: '系统最高权限，可以访问所有功能',
              createdAt: '2023-01-01T00:00:00Z',
              status: 'active'
            },
            {
              id: 2,
              name: '采购管理员',
              description: '负责采购相关的管理工作',
              createdAt: '2023-01-02T00:00:00Z',
              status: 'active'
            },
            {
              id: 3,
              name: '销售管理员',
              description: '负责销售相关的管理工作',
              createdAt: '2023-01-03T00:00:00Z',
              status: 'active'
            },
            {
              id: 4,
              name: '财务管理员',
              description: '负责财务相关的管理工作',
              createdAt: '2023-01-04T00:00:00Z',
              status: 'active'
            },
            {
              id: 5,
              name: '仓库管理员',
              description: '负责仓库相关的管理工作',
              createdAt: '2023-01-05T00:00:00Z',
              status: 'inactive'
            }
          ];
        },
        formatDate(dateString) {
          const date = new Date(dateString);
          return date.toLocaleDateString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
          });
        },
        searchRoles() {
          // 实际应用中，这里会调用API进行搜索
          console.log('搜索条件:', this.filters);
          // 模拟搜索结果
          this.loadRoles();
        },
        resetFilters() {
          this.filters = {
            name: '',
            status: ''
          };
          this.searchRoles();
        },
        changePage(page) {
          if (page < 1 || page > this.totalPages) return;
          this.currentPage = page;
          this.loadRoles();
        },
        openAddRoleModal() {
          this.isEditing = false;
          this.currentRole = {
            id: null,
            name: '',
            description: '',
            status: 'active',
            dataScope: 'all'
          };
          $('#roleModal').modal('show');
        },
        openEditRoleModal(role) {
          this.isEditing = true;
          this.currentRole = { ...role };
          $('#roleModal').modal('show');
        },
        saveRole() {
          // 实际应用中，这里会调用API保存角色
          console.log('保存角色:', this.currentRole);
    
          if (this.isEditing) {
            // 更新现有角色
            const index = this.roles.findIndex(r => r.id === this.currentRole.id);
            if (index !== -1) {
              this.roles[index] = { ...this.currentRole };
            }
          } else {
            // 添加新角色
            const newRole = {
              ...this.currentRole,
              id: this.roles.length + 1,
              createdAt: new Date().toISOString()
            };
            this.roles.push(newRole);
          }
    
          // 关闭模态框
          $('#roleModal').modal('hide');
        },
        openPermissionModal(role) {
          this.currentRole = { ...role };
          this.resetPermissions();
          this.loadRolePermissions(role.id);
          $('#permissionModal').modal('show');
        },
        resetPermissions() {
          // 重置菜单权限
          this.menuPermissions.forEach(module => {
            module.checked = false;
            module.items.forEach(item => {
              item.checked = false;
            });
          });
    
          // 重置操作权限
          Object.keys(this.operationPermissions).forEach(key => {
            this.operationPermissions[key].forEach(permission => {
              permission.checked = false;
            });
          });
    
          // 重置数据权限
          this.currentRole.dataScope = 'all';
          this.selectedDepartments = [];
        },
        loadRolePermissions(roleId) {
          // 模拟从API加载角色权限
          // 这里根据角色ID设置不同的权限
          if (roleId === 1) { // 超级管理员
            // 设置所有菜单权限
            this.menuPermissions.forEach(module => {
              module.checked = true;
              module.items.forEach(item => {
                item.checked = true;
              });
            });
    
            // 设置所有操作权限
            Object.keys(this.operationPermissions).forEach(key => {
              this.operationPermissions[key].forEach(permission => {
                permission.checked = true;
              });
            });
    
            // 设置数据权限
            this.currentRole.dataScope = 'all';
          } else if (roleId === 2) { // 采购管理员
            // 设置采购相关的菜单权限
            const purchaseModules = ['合同管理', '货物管理', '结算管理', '财务管理', '发票管理', '台账管理'];
            this.menuPermissions.forEach(module => {
              if (purchaseModules.includes(module.name)) {
                if (module.name === '合同管理') {
                  module.items.forEach(item => {
                    if (item.name.includes('采购')) {
                      item.checked = true;
                    }
                  });
                  this.updateModuleCheckStatus(module);
                }
              }
            });
    
            // 设置采购相关的操作权限
            this.operationPermissions['合同管理'].forEach(permission => {
              if (['查看', '新增', '编辑'].includes(permission.name)) {
                permission.checked = true;
              }
            });
    
            // 设置数据权限
            this.currentRole.dataScope = 'department';
          }
          // 可以根据需要添加更多角色的权限设置
        },
        toggleModulePermission(module) {
          // 当模块checkbox改变时，更新所有子项
          module.items.forEach(item => {
            item.checked = module.checked;
          });
        },
        updateModuleCheckStatus(module) {
          // 当子项checkbox改变时，更新模块checkbox状态
          const allChecked = module.items.every(item => item.checked);
          const someChecked = module.items.some(item => item.checked);
    
          module.checked = allChecked;
    
          // 如果需要实现"半选"状态，可以在这里添加逻辑
          // 例如：module.indeterminate = !allChecked && someChecked;
        },
        savePermissions() {
          // 收集选中的菜单权限
          const selectedMenuPermissions = [];
          this.menuPermissions.forEach(module => {
            module.items.forEach(item => {
              if (item.checked) {
                selectedMenuPermissions.push(item.id);
              }
            });
          });
    
          // 收集选中的操作权限
          const selectedOperationPermissions = [];
          Object.keys(this.operationPermissions).forEach(key => {
            this.operationPermissions[key].forEach(permission => {
              if (permission.checked) {
                selectedOperationPermissions.push(permission.id);
              }
            });
          });
    
          // 准备要保存的权限数据
          const permissionData = {
            roleId: this.currentRole.id,
            menuPermissions: selectedMenuPermissions,
            operationPermissions: selectedOperationPermissions,
            dataScope: this.currentRole.dataScope,
            departments: this.currentRole.dataScope === 'custom' ? this.selectedDepartments : []
          };
    
          // 实际应用中，这里会调用API保存权限
          console.log('保存权限:', permissionData);
    
          // 关闭模态框
          $('#permissionModal').modal('hide');
        },
        confirmDeleteRole(role) {
          this.currentRole = { ...role };
          $('#deleteModal').modal('show');
        },
        deleteRole() {
          // 实际应用中，这里会调用API删除角色
          console.log('删除角色:', this.currentRole.id);
    
          // 从列表中移除角色
          this.roles = this.roles.filter(r => r.id !== this.currentRole.id);
    
          // 关闭模态框
          $('#deleteModal').modal('hide');
        }
      }
    };
    </script>
    
    <style scoped>
    .role-management {
      padding: 20px;
    }
    
    .filter-section {
      background-color: #f8f9fa;
      padding: 15px;
      border-radius: 5px;
      margin-bottom: 20px;
    }
    
    .card {
      box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
      margin-bottom: 20px;
    }
    
    .permission-module {
      margin-bottom: 15px;
    }
    
    .permission-module-header {
      font-weight: bold;
      margin-bottom: 10px;
    }
    
    .department-tree {
      max-height: 300px;
      overflow-y: auto;
    }
    
    .department-item {
      margin-bottom: 10px;
    }
    </style>
    
    