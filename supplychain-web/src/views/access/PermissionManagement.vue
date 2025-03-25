<template>
  <div class="permission-management">
    <div class="card">
      <div class="card-header">
        <h5 class="card-title">权限管理</h5>
        <p class="card-subtitle mb-2 text-muted">管理系统中的原子操作权限和数据权限</p>
      </div>
      <div class="card-body">
        <!-- 权限类型切换 -->
        <ul class="nav nav-tabs" id="permissionTabs" role="tablist">
          <li class="nav-item">
            <a class="nav-link active" id="operation-tab" data-toggle="tab" href="#operation" role="tab" aria-controls="operation" aria-selected="true">
              操作权限
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" id="data-tab" data-toggle="tab" href="#data" role="tab" aria-controls="data" aria-selected="false">
              数据权限
            </a>
          </li>
        </ul>
        
        <div class="tab-content mt-3" id="permissionTabsContent">
          <!-- 操作权限 -->
          <div class="tab-pane fade show active" id="operation" role="tabpanel" aria-labelledby="operation-tab">
            <div class="alert alert-info mb-3">
              <i class="fas fa-info-circle"></i> 
              操作权限是系统中最基本的权限单元，用于控制用户可以执行的具体操作，如查看、新增、编辑、删除等。
            </div>
            <div class="d-flex justify-content-between mb-3">
              <div>
                <button type="button" class="btn btn-primary" @click="openAddOperationPermModal()">
                  <i class="fas fa-plus"></i> 新增操作权限
                </button>
              </div>
              <div class="input-group" style="width: 300px;">
                <input type="text" class="form-control" placeholder="搜索权限" v-model="operationSearchQuery">
                <div class="input-group-append">
                  <button class="btn btn-outline-secondary" type="button" @click="searchOperationPermissions">
                    <i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            
            <div class="table-responsive">
              <table class="table table-bordered table-striped">
                <thead>
                  <tr>
                    <th style="width: 5%">序号</th>
                    <th style="width: 15%">权限名称</th>
                    <th style="width: 15%">权限标识</th>
                    <th style="width: 15%">权限类型</th>
                    <th style="width: 15%">所属模块</th>
                    <th style="width: 25%">描述</th>
                    <th style="width: 10%">操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(perm, index) in filteredOperationPermissions" :key="perm.id">
                    <td>{{ index + 1 }}</td>
                    <td>{{ perm.name }}</td>
                    <td>{{ perm.code }}</td>
                    <td>{{ perm.type }}</td>
                    <td>{{ perm.module }}</td>
                    <td>{{ perm.description }}</td>
                    <td>
                      <div class="btn-group">
                        <button type="button" class="btn btn-info btn-xs" @click="openEditOperationPermModal(perm)">
                          <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs" @click="confirmDeleteOperationPerm(perm)">
                          <i class="fas fa-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          
          <!-- 数据权限 -->
          <div class="tab-pane fade" id="data" role="tabpanel" aria-labelledby="data-tab">
            <div class="alert alert-info mb-3">
              <i class="fas fa-info-circle"></i> 
              数据权限用于控制用户可以访问的数据范围，如全部数据、部门数据、个人数据等。
            </div>
            <div class="d-flex justify-content-between mb-3">
              <div>
                <button type="button" class="btn btn-primary" @click="openAddDataPermModal()">
                  <i class="fas fa-plus"></i> 新增数据权限
                </button>
              </div>
              <div class="input-group" style="width: 300px;">
                <input type="text" class="form-control" placeholder="搜索权限" v-model="dataSearchQuery">
                <div class="input-group-append">
                  <button class="btn btn-outline-secondary" type="button" @click="searchDataPermissions">
                    <i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
            </div>
            
            <div class="table-responsive">
              <table class="table table-bordered table-striped">
                <thead>
                  <tr>
                    <th style="width: 5%">序号</th>
                    <th style="width: 15%">权限名称</th>
                    <th style="width: 15%">权限标识</th>
                    <th style="width: 15%">数据范围</th>
                    <th style="width: 15%">所属模块</th>
                    <th style="width: 25%">描述</th>
                    <th style="width: 10%">操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(perm, index) in filteredDataPermissions" :key="perm.id">
                    <td>{{ index + 1 }}</td>
                    <td>{{ perm.name }}</td>
                    <td>{{ perm.code }}</td>
                    <td>{{ perm.scope }}</td>
                    <td>{{ perm.module }}</td>
                    <td>{{ perm.description }}</td>
                    <td>
                      <div class="btn-group">
                        <button type="button" class="btn btn-info btn-xs" @click="openEditDataPermModal(perm)">
                          <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs" @click="confirmDeleteDataPerm(perm)">
                          <i class="fas fa-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  
    <!-- 操作权限模态框 -->
    <div class="modal fade" id="operationPermModal" tabindex="-1" role="dialog" aria-labelledby="operationPermModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="operationPermModalLabel">
              {{ isEdit ? '编辑操作权限' : '新增操作权限' }}
            </h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label for="operationPermName">权限名称<span class="text-danger">*</span></label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="operationPermName" 
                  v-model="currentOperationPerm.name" 
                  placeholder="请输入权限名称"
                >
              </div>
              <div class="form-group">
                <label for="operationPermCode">权限标识<span class="text-danger">*</span></label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="operationPermCode" 
                  v-model="currentOperationPerm.code" 
                  placeholder="请输入权限标识，如：system:user:add"
                >
              </div>
              <div class="form-group">
                <label for="operationPermType">权限类型<span class="text-danger">*</span></label>
                <select class="form-control" id="operationPermType" v-model="currentOperationPerm.type">
                  <option value="菜单">菜单</option>
                  <option value="按钮">按钮</option>
                  <option value="接口">接口</option>
                  <option value="页面">页面</option>
                  <option value="功能">功能</option>
                </select>
              </div>
              <div class="form-group">
                <label for="operationPermModule">所属模块<span class="text-danger">*</span></label>
                <select class="form-control" id="operationPermModule" v-model="currentOperationPerm.module">
                  <option value="系统管理">系统管理</option>
                  <option value="合同管理">合同管理</option>
                  <option value="货物管理">货物管理</option>
                  <option value="结算管理">结算管理</option>
                  <option value="财务管理">财务管理</option>
                  <option value="发票管理">发票管理</option>
                  <option value="台账管理">台账管理</option>
                </select>
              </div>
              <div class="form-group">
                <label for="operationPermDescription">描述</label>
                <textarea 
                  class="form-control" 
                  id="operationPermDescription" 
                  v-model="currentOperationPerm.description" 
                  rows="3" 
                  placeholder="请输入权限描述"
                ></textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" @click="saveOperationPerm">保存</button>
          </div>
        </div>
      </div>
    </div>
  
    <!-- 数据权限模态框 -->
    <div class="modal fade" id="dataPermModal" tabindex="-1" role="dialog" aria-labelledby="dataPermModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="dataPermModalLabel">
              {{ isEdit ? '编辑数据权限' : '新增数据权限' }}
            </h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label for="dataPermName">权限名称<span class="text-danger">*</span></label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="dataPermName" 
                  v-model="currentDataPerm.name" 
                  placeholder="请输入权限名称"
                >
              </div>
              <div class="form-group">
                <label for="dataPermCode">权限标识<span class="text-danger">*</span></label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="dataPermCode" 
                  v-model="currentDataPerm.code" 
                  placeholder="请输入权限标识，如：data:contract:all"
                >
              </div>
              <div class="form-group">
                <label for="dataPermScope">数据范围<span class="text-danger">*</span></label>
                <select class="form-control" id="dataPermScope" v-model="currentDataPerm.scope">
                  <option value="全部数据">全部数据</option>
                  <option value="部门数据">部门数据</option>
                  <option value="部门及以下数据">部门及以下数据</option>
                  <option value="个人数据">个人数据</option>
                  <option value="自定义数据">自定义数据</option>
                  <option value="指定部门数据">指定部门数据</option>
                </select>
              </div>
              <div class="form-group" v-if="currentDataPerm.scope === '指定部门数据'">
                <label>指定部门</label>
                <select class="form-control" v-model="currentDataPerm.specifiedDeptId">
                  <option value="">请选择部门</option>
                  <option v-for="dept in departments" :key="dept.id" :value="dept.id">
                    {{ dept.name }}
                  </option>
                </select>
              </div>
              <div class="form-group">
                <label for="dataPermModule">所属模块<span class="text-danger">*</span></label>
                <select class="form-control" id="dataPermModule" v-model="currentDataPerm.module">
                  <option value="合同管理">合同管理</option>
                  <option value="货物管理">货物管理</option>
                  <option value="结算管理">结算管理</option>
                  <option value="财务管理">财务管理</option>
                  <option value="发票管理">发票管理</option>
                  <option value="台账管理">台账管理</option>
                </select>
              </div>
              <div class="form-group">
                <label for="dataPermDescription">描述</label>
                <textarea 
                  class="form-control" 
                  id="dataPermDescription" 
                  v-model="currentDataPerm.description" 
                  rows="3" 
                  placeholder="请输入权限描述"
                ></textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-primary" @click="saveDataPerm">保存</button>
          </div>
        </div>
      </div>
    </div>
  
    <!-- 删除确认模态框 -->
    <div class="modal fade" id="deletePermModal" tabindex="-1" role="dialog" aria-labelledby="deletePermModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="deletePermModalLabel">确认删除</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>确定要删除权限 <strong>{{ currentPermToDelete.name }}</strong> 吗？此操作不可恢复！</p>
            <p class="text-danger">
              <i class="fas fa-exclamation-triangle"></i> 
              警告：删除权限可能会影响现有角色的权限配置！
            </p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
            <button type="button" class="btn btn-danger" @click="deletePermission">确认删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script>
  export default {
    name: 'PermissionManagement',
    data() {
      return {
        // 搜索查询
        operationSearchQuery: '',
        dataSearchQuery: '',
        
        // 操作权限数据
        operationPermissions: [],
        
        // 数据权限数据
        dataPermissions: [],
        
        // 当前操作的权限
        currentOperationPerm: {
          id: null,
          name: '',
          code: '',
          type: '按钮',
          module: '系统管理',
          description: ''
        },
        
        currentDataPerm: {
          id: null,
          name: '',
          code: '',
          scope: '全部数据',
          module: '合同管理',
          description: '',
          specifiedDeptId: ''
        },
        
        // 当前要删除的权限
        currentPermToDelete: {
          id: null,
          name: '',
          type: 'operation' // 'operation' 或 'data'
        },
        
        // 编辑状态标记
        isEdit: false,
  
        departments: [
          { id: 1, name: '总公司' },
          { id: 2, name: '研发部' },
          { id: 3, name: '市场部' },
          { id: 4, name: '财务部' },
          { id: 5, name: '前端组' },
          { id: 6, name: '后端组' },
          { id: 7, name: '测试组' }
        ],
      }
    },
    computed: {
      // 过滤后的操作权限
      filteredOperationPermissions() {
        if (!this.operationSearchQuery) {
          return this.operationPermissions;
        }
        
        const query = this.operationSearchQuery.toLowerCase();
        return this.operationPermissions.filter(perm => 
          perm.name.toLowerCase().includes(query) ||
          perm.code.toLowerCase().includes(query) ||
          perm.type.toLowerCase().includes(query) ||
          perm.module.toLowerCase().includes(query) ||
          (perm.description && perm.description.toLowerCase().includes(query))
        );
      },
      
      // 过滤后的数据权限
      filteredDataPermissions() {
        if (!this.dataSearchQuery) {
          return this.dataPermissions;
        }
        
        const query = this.dataSearchQuery.toLowerCase();
        return this.dataPermissions.filter(perm => 
          perm.name.toLowerCase().includes(query) ||
          perm.code.toLowerCase().includes(query) ||
          perm.scope.toLowerCase().includes(query) ||
          perm.module.toLowerCase().includes(query) ||
          (perm.description && perm.description.toLowerCase().includes(query))
        );
      }
    },
    created() {
      this.fetchOperationPermissions();
      this.fetchDataPermissions();
    },
    methods: {
      // 获取操作权限列表
      fetchOperationPermissions() {
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
        }, 500);
      },
      
      // 获取数据权限列表
      fetchDataPermissions() {
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
        }, 500);
      },
      
      // 搜索操作权限
      searchOperationPermissions() {
        // 实际应用中这里会调用API
        console.log('搜索操作权限:', this.operationSearchQuery);
      },
      
      // 搜索数据权限
      searchDataPermissions() {
        // 实际应用中这里会调用API
        console.log('搜索数据权限:', this.dataSearchQuery);
      },
      
      // 打开新增操作权限模态框
      openAddOperationPermModal() {
        this.isEdit = false;
        this.currentOperationPerm = {
          id: null,
          name: '',
          code: '',
          type: '按钮',
          module: '系统管理',
          description: ''
        };
        
        // 使用jQuery打开模态框
        $('#operationPermModal').modal('show');
      },
      
      // 打开编辑操作权限模态框
      openEditOperationPermModal(perm) {
        this.isEdit = true;
        this.currentOperationPerm = { ...perm };
        
        // 使用jQuery打开模态框
        $('#operationPermModal').modal('show');
      },
      
      // 保存操作权限
      saveOperationPerm() {
        // 表单验证
        if (!this.currentOperationPerm.name) {
          alert('权限名称不能为空');
          return;
        }
        
        if (!this.currentOperationPerm.code) {
          alert('权限标识不能为空');
          return;
        }
        
        // 模拟API调用
        setTimeout(() => {
          if (this.isEdit) {
            // 更新现有权限
            const index = this.operationPermissions.findIndex(perm => perm.id === this.currentOperationPerm.id);
            if (index !== -1) {
              this.operationPermissions[index] = { ...this.currentOperationPerm };
            }
          } else {
            // 创建新权限
            const newId = Math.max(...this.operationPermissions.map(perm => perm.id)) + 1;
            
            const newPerm = {
              ...this.currentOperationPerm,
              id: newId
            };
            
            this.operationPermissions.push(newPerm);
          }
          
          // 关闭模态框
          $('#operationPermModal').modal('hide');
          
          // 显示成功消息
          alert(this.isEdit ? '操作权限更新成功' : '操作权限创建成功');
        }, 500);
      },
      
      // 打开新增数据权限模态框
      openAddDataPermModal() {
        this.isEdit = false;
        this.currentDataPerm = {
          id: null,
          name: '',
          code: '',
          scope: '全部数据',
          module: '合同管理',
          description: '',
          specifiedDeptId: ''
        };
        
        // 使用jQuery打开模态框
        $('#dataPermModal').modal('show');
      },
      
      // 打开编辑数据权限模态框
      openEditDataPermModal(perm) {
        this.isEdit = true;
        this.currentDataPerm = { ...perm };
        
        // 使用jQuery打开模态框
        $('#dataPermModal').modal('show');
      },
      
      // 保存数据权限
      saveDataPerm() {
        // 表单验证
        if (!this.currentDataPerm.name) {
          alert('权限名称不能为空');
          return;
        }
        
        if (!this.currentDataPerm.code) {
          alert('权限标识不能为空');
          return;
        }
        
        // 验证指定部门
        if (this.currentDataPerm.scope === '指定部门数据' && !this.currentDataPerm.specifiedDeptId) {
          alert('请选择指定部门');
          return;
        }
        
        // 模拟API调用
        setTimeout(() => {
          if (this.isEdit) {
            // 更新现有权限
            const index = this.dataPermissions.findIndex(perm => perm.id === this.currentDataPerm.id);
            if (index !== -1) {
              this.dataPermissions[index] = { ...this.currentDataPerm };
            }
          } else {
            // 创建新权限
            const newId = Math.max(...this.dataPermissions.map(perm => perm.id)) + 1;
            
            const newPerm = {
              ...this.currentDataPerm,
              id: newId
            };
            
            this.dataPermissions.push(newPerm);
          }
          
          // 关闭模态框
          $('#dataPermModal').modal('hide');
          
          // 显示成功消息
          alert(this.isEdit ? '数据权限更新成功' : '数据权限创建成功');
        }, 500);
      },
      
      // 确认删除操作权限
      confirmDeleteOperationPerm(perm) {
        this.currentPermToDelete = {
          id: perm.id,
          name: perm.name,
          type: 'operation'
        };
        
        // 打开确认删除模态框
        $('#deletePermModal').modal('show');
      },
      
      // 确认删除数据权限
      confirmDeleteDataPerm(perm) {
        this.currentPermToDelete = {
          id: perm.id,
          name: perm.name,
          type: 'data'
        };
        
        // 打开确认删除模态框
        $('#deletePermModal').modal('show');
      },
      
      // 删除权限
      deletePermission() {
        // 模拟API调用
        setTimeout(() => {
          if (this.currentPermToDelete.type === 'operation') {
            // 删除操作权限
            this.operationPermissions = this.operationPermissions.filter(
              perm => perm.id !== this.currentPermToDelete.id
            );
          } else {
            // 删除数据权限
            this.dataPermissions = this.dataPermissions.filter(
              perm => perm.id !== this.currentPermToDelete.id
            );
          }
          
          // 关闭模态框
          $('#deletePermModal').modal('hide');
          
          // 显示成功消息
          alert('权限删除成功');
        }, 500);
      }
    }
  }
  </script>
  
  <style scoped>
  .permission-management {
    padding: 15px;
  }
  </style>
  
  