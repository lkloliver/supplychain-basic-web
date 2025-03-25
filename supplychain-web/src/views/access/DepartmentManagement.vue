<template>
    <div class="access-module">
      <div class="module-header">
        <h2 class="module-title">部门管理</h2>
        <div class="module-actions">
          <button class="action-button primary" @click="showAddModal = true">
            <i class="icon-plus"></i>新增部门
          </button>
        </div>
      </div>
  
      <div class="module-content">
        <!-- 搜索过滤区域 -->
        <div class="filter-section">
          <div class="filter-row">
            <div class="filter-item">
              <span class="filter-label">部门名称</span>
              <input type="text" v-model="searchQuery" class="filter-input" placeholder="请输入部门名称">
            </div>
            <div class="filter-item">
              <span class="filter-label">状态</span>
              <select v-model="statusFilter" class="filter-select">
                <option value="">全部</option>
                <option value="1">启用</option>
                <option value="0">禁用</option>
              </select>
            </div>
            <div class="filter-actions">
              <button class="action-button primary" @click="searchDepartments">
                <i class="icon-search"></i>搜索
              </button>
              <button class="action-button default" @click="resetSearch">
                <i class="icon-refresh"></i>重置
              </button>
            </div>
          </div>
        </div>
  
        <!-- 数据表格 -->
        <div class="data-table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th width="5%">序号</th>
                <th width="20%">部门名称</th>
                <th width="15%">上级部门</th>
                <th width="10%">负责人</th>
                <th width="15%">联系电话</th>
                <th width="15%">创建时间</th>
                <th width="10%">状态</th>
                <th width="10%">操作</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="filteredDepartments.length > 0">
                <tr v-for="(dept, index) in filteredDepartments" :key="dept.id">
                  <td>{{ index + 1 }}</td>
                  <td>
                    <span v-if="dept.level > 1" :style="{ paddingLeft: (dept.level - 1) * 20 + 'px' }">
                      <i class="icon-corner-down-right"></i>
                    </span>
                    {{ dept.name }}
                  </td>
                  <td>{{ dept.parentName || '-' }}</td>
                  <td>{{ dept.manager || '-' }}</td>
                  <td>{{ dept.phone || '-' }}</td>
                  <td>{{ dept.createTime }}</td>
                  <td>
                    <span :class="['status-tag', dept.status === 1 ? 'success' : 'danger']">
                      {{ dept.status === 1 ? '启用' : '禁用' }}
                    </span>
                  </td>
                  <td>
                    <div class="action-group">
                      <button class="table-action edit" @click="editDepartment(dept)" title="编辑">
                        <i class="icon-edit"></i>
                      </button>
                      <button class="table-action data-perm" @click="openDataPermModal(dept)" title="数据权限">
                        <i class="icon-database"></i>
                      </button>
                      <button class="table-action delete" @click="confirmDelete(dept)" title="删除">
                        <i class="icon-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </template>
              <tr v-else>
                <td colspan="8" class="empty-data">暂无数据</td>
              </tr>
            </tbody>
          </table>
        </div>
  
        <!-- 分页 -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            显示 {{ (currentPage - 1) * pageSize + 1 }} 到 
            {{ Math.min(currentPage * pageSize, totalDepartments) }} 条，
            共 {{ totalDepartments }} 条记录
          </div>
          <div class="pagination">
            <button 
              class="pagination-button" 
              :disabled="currentPage === 1"
              @click="changePage(currentPage - 1)"
            >
              <i class="icon-chevron-left"></i>
            </button>
            <span v-for="page in displayedPages" :key="page" 
                  class="page-number" 
                  :class="{ active: currentPage === page }"
                  @click="changePage(page)">
              {{ page }}
            </span>
            <button 
              class="pagination-button" 
              :disabled="currentPage === totalPages"
              @click="changePage(currentPage + 1)"
            >
              <i class="icon-chevron-right"></i>
            </button>
          </div>
        </div>
      </div>
  
      <!-- 新增/编辑部门模态框 -->
      <div v-if="showAddModal || showEditModal" class="modal-wrapper">
        <div class="modal-backdrop" @click="closeAllModals"></div>
        <div class="modal">
          <div class="modal-header">
            <h3 class="modal-title">{{ showEditModal ? '编辑部门' : '新增部门' }}</h3>
            <button class="modal-close" @click="closeAllModals">
              <i class="icon-x"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="form-grid">
              <div class="form-item">
                <label class="form-label">部门名称<span class="required">*</span></label>
                <input 
                  type="text" 
                  v-model="currentDepartment.name" 
                  class="form-input" 
                  placeholder="请输入部门名称"
                >
              </div>
              <div class="form-item">
                <label class="form-label">上级部门</label>
                <select v-model="currentDepartment.parentId" class="form-select">
                  <option value="">无上级部门</option>
                  <option v-for="dept in departmentOptions" :key="dept.id" :value="dept.id">
                    {{ dept.name }}
                  </option>
                </select>
              </div>
              <div class="form-item">
                <label class="form-label">负责人</label>
                <input 
                  type="text" 
                  v-model="currentDepartment.manager" 
                  class="form-input" 
                  placeholder="请输入负责人姓名"
                >
              </div>
              <div class="form-item">
                <label class="form-label">联系电话</label>
                <input 
                  type="text" 
                  v-model="currentDepartment.phone" 
                  class="form-input" 
                  placeholder="请输入联系电话"
                >
              </div>
              <div class="form-item">
                <label class="form-label">邮箱</label>
                <input 
                  type="email" 
                  v-model="currentDepartment.email" 
                  class="form-input" 
                  placeholder="请输入邮箱"
                >
              </div>
              <div class="form-item">
                <label class="form-label">显示顺序</label>
                <input 
                  type="number" 
                  v-model="currentDepartment.orderNum" 
                  class="form-input" 
                  placeholder="请输入显示顺序"
                >
              </div>
              <div class="form-item">
                <label class="form-label">状态</label>
                <div class="switch-wrapper">
                  <label class="switch">
                    <input 
                      type="checkbox" 
                      v-model="deptStatus"
                    >
                    <span class="slider"></span>
                  </label>
                  <span class="switch-text">{{ deptStatus ? '启用' : '禁用' }}</span>
                </div>
              </div>
            </div>
            <div class="form-item full-width">
              <label class="form-label">备注</label>
              <textarea 
                v-model="currentDepartment.remark" 
                class="form-textarea" 
                rows="3" 
                placeholder="请输入备注信息"
              ></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button class="action-button default" @click="closeAllModals">取消</button>
            <button class="action-button primary" @click="saveDepartment">保存</button>
          </div>
        </div>
      </div>
  
      <!-- 数据权限模态框 -->
      <div v-if="showDataPermModal" class="modal-wrapper">
        <div class="modal-backdrop" @click="closeAllModals"></div>
        <div class="modal">
          <div class="modal-header">
            <h3 class="modal-title">部门数据权限 - {{ currentDepartment.name }}</h3>
            <button class="modal-close" @click="closeAllModals">
              <i class="icon-x"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="alert info">
              <i class="icon-info"></i>
              <span>部门数据权限决定了该部门用户可以访问的数据范围。</span>
            </div>
            
            <div class="form-item">
              <label class="form-label">数据权限范围</label>
              <select v-model="currentDepartment.dataScope" class="form-select">
                <option value="all">全部数据权限</option>
                <option value="department">本部门数据权限</option>
                <option value="departmentAndSub">本部门及以下数据权限</option>
                <option value="self">仅本人数据权限</option>
                <option value="custom">自定义数据权限</option>
              </select>
            </div>
            
            <div v-if="currentDepartment.dataScope === 'custom'" class="custom-permissions">
              <h4 class="section-title">模块数据权限设置</h4>
              <div class="module-permissions">
                <div v-for="module in modulePermissions" :key="module.id" class="module-item">
                  <div class="module-header">
                    <label class="checkbox">
                      <input 
                        type="checkbox" 
                        v-model="module.checked"
                        @change="toggleModulePermission(module)"
                      >
                      <span class="checkmark"></span>
                      <span class="checkbox-text">{{ module.name }}</span>
                    </label>
                  </div>
                  <div class="module-content">
                    <div class="sub-modules">
                      <div v-for="subModule in module.subModules" :key="subModule.id" class="sub-module">
                        <label class="checkbox">
                          <input 
                            type="checkbox" 
                            v-model="subModule.checked"
                            @change="updateModuleCheckStatus(module)"
                          >
                          <span class="checkmark"></span>
                          <span class="checkbox-text">{{ subModule.name }}</span>
                        </label>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="action-button default" @click="closeAllModals">取消</button>
            <button class="action-button primary" @click="saveDataPermissions">保存</button>
          </div>
        </div>
      </div>
  
      <!-- 删除确认模态框 -->
      <div v-if="showDeleteModal" class="modal-wrapper">
        <div class="modal-backdrop" @click="closeAllModals"></div>
        <div class="modal small">
          <div class="modal-header">
            <h3 class="modal-title">确认删除</h3>
            <button class="modal-close" @click="closeAllModals">
              <i class="icon-x"></i>
            </button>
          </div>
          <div class="modal-body">
            <p>确定要删除部门 <strong>{{ currentDepartment.name }}</strong> 吗？此操作不可恢复！</p>
            <p v-if="hasChildDepartments" class="warning-text">
              <i class="icon-alert-triangle"></i>
              警告：该部门下有子部门，删除将导致子部门一并删除！
            </p>
          </div>
          <div class="modal-footer">
            <button class="action-button default" @click="closeAllModals">取消</button>
            <button class="action-button danger" @click="deleteDepartment">确认删除</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'DepartmentManagement',
    data() {
      return {
        // 搜索和筛选
        searchQuery: '',
        statusFilter: '',
        
        // 分页
        currentPage: 1,
        pageSize: 10,
        totalDepartments: 0,
        
        // 部门数据
        departments: [],
        
        // 当前操作的部门
        currentDepartment: {
          id: null,
          name: '',
          parentId: '',
          parentName: '',
          manager: '',
          phone: '',
          email: '',
          orderNum: 0,
          status: 1,
          remark: '',
          level: 1,
          dataScope: 'all' // 数据权限字段，默认为全部
        },
        
        // 部门状态开关
        deptStatus: true,
        
        // 子部门检查
        hasChildDepartments: false,
        
        // 模态框显示状态
        showAddModal: false,
        showEditModal: false,
        showDataPermModal: false,
        showDeleteModal: false,
        
        // 模块权限数据
        modulePermissions: [
          {
            id: 1,
            name: '合同管理',
            checked: false,
            subModules: [
              { id: 101, name: '采购合同', checked: false },
              { id: 102, name: '销售合同', checked: false },
              { id: 103, name: '运输合同', checked: false }
            ]
          },
          {
            id: 2,
            name: '货物管理',
            checked: false,
            subModules: [
              { id: 201, name: '入库重量', checked: false },
              { id: 202, name: '出库重量', checked: false },
              { id: 203, name: '入库质检', checked: false },
              { id: 204, name: '出库质检', checked: false }
            ]
          },
          {
            id: 3,
            name: '结算管理',
            checked: false,
            subModules: [
              { id: 301, name: '采购结算', checked: false },
              { id: 302, name: '销售结算', checked: false }
            ]
          }
        ]
      }
    },
    computed: {
      // 过滤后的部门列表
      filteredDepartments() {
        let result = [...this.departments];
        
        if (this.searchQuery) {
          result = result.filter(dept => 
            dept.name.toLowerCase().includes(this.searchQuery.toLowerCase())
          );
        }
        
        if (this.statusFilter !== '') {
          result = result.filter(dept => 
            dept.status === parseInt(this.statusFilter)
          );
        }
        
        return result;
      },
      
      // 总页数
      totalPages() {
        return Math.ceil(this.totalDepartments / this.pageSize);
      },
      
      // 显示的页码
      displayedPages() {
        const pages = [];
        const maxPages = 5;
        
        let startPage = Math.max(1, this.currentPage - Math.floor(maxPages / 2));
        let endPage = startPage + maxPages - 1;
        
        if (endPage > this.totalPages) {
          endPage = this.totalPages;
          startPage = Math.max(1, endPage - maxPages + 1);
        }
        
        for (let i = startPage; i <= endPage; i++) {
          pages.push(i);
        }
        
        return pages;
      },
      
      // 部门选项（用于上级部门选择）
      departmentOptions() {
        // 过滤掉当前部门及其子部门，防止循环引用
        if (!this.showEditModal) {
          return this.departments;
        }
        
        const currentId = this.currentDepartment.id;
        return this.departments.filter(dept => {
          // 排除自己
          if (dept.id === currentId) {
            return false;
          }
          
          // 排除子部门
          let parent = dept;
          while (parent.parentId) {
            if (parent.parentId === currentId) {
              return false;
            }
            parent = this.departments.find(d => d.id === parent.parentId) || {};
          }
          
          return true;
        });
      }
    },
    watch: {
      deptStatus(val) {
        this.currentDepartment.status = val ? 1 : 0;
      }
    },
    created() {
      this.fetchDepartments();
    },
    methods: {
      // 获取部门列表
      fetchDepartments() {
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
        }, 500);
      },
      
      // 搜索部门
      searchDepartments() {
        this.currentPage = 1;
        // 实际应用中这里会调用API
      },
      
      // 重置搜索
      resetSearch() {
        this.searchQuery = '';
        this.statusFilter = '';
        this.currentPage = 1;
        // 实际应用中这里会重新调用API
      },
      
      // 切换页码
      changePage(page) {
        if (page < 1 || page > this.totalPages) {
          return;
        }
        this.currentPage = page;
        // 实际应用中这里会调用API获取对应页的数据
      },
      
      // 编辑部门
      editDepartment(department) {
        this.currentDepartment = { ...department };
        this.deptStatus = department.status === 1;
        this.showEditModal = true;
      },
      
      // 打开数据权限模态框
      openDataPermModal(dept) {
        this.currentDepartment = { ...dept };
        this.resetModulePermissions();
        
        // 模拟加载部门的数据权限
        this.loadDepartmentDataPermissions(dept.id);
        
        this.showDataPermModal = true;
      },
      
      // 确认删除
      confirmDelete(department) {
        this.currentDepartment = { ...department };
        
        // 检查是否有子部门
        this.hasChildDepartments = this.departments.some(dept => dept.parentId === department.id);
        
        this.showDeleteModal = true;
      },
      
      // 关闭所有模态框
      closeAllModals() {
        this.showAddModal = false;
        this.showEditModal = false;
        this.showDataPermModal = false;
        this.showDeleteModal = false;
      },
      
      // 重置模块权限
      resetModulePermissions() {
        this.modulePermissions.forEach(module => {
          module.checked = false;
          module.subModules.forEach(subModule => {
            subModule.checked = false;
          });
        });
      },
      
      // 加载部门数据权限
      loadDepartmentDataPermissions(deptId) {
        // 模拟从API加载部门数据权限
        // 这里根据部门ID设置不同的权限
        if (deptId === 2) { // 研发部
          // 设置数据权限范围
          this.currentDepartment.dataScope = 'custom';
          
          // 设置模块权限
          const devModules = ['合同管理'];
          this.modulePermissions.forEach(module => {
            if (devModules.includes(module.name)) {
              module.checked = true;
              module.subModules.forEach(subModule => {
                subModule.checked = true;
              });
            }
          });
        } else if (deptId === 3) { // 市场部
          // 设置数据权限范围
          this.currentDepartment.dataScope = 'department';
        }
      },
      
      // 切换模块权限
      toggleModulePermission(module) {
        // 当模块checkbox改变时，更新所有子模块
        module.subModules.forEach(subModule => {
          subModule.checked = module.checked;
        });
      },
      
      // 更新模块选中状态
      updateModuleCheckStatus(module) {
        // 当子模块checkbox改变时，更新模块checkbox状态
        const allChecked = module.subModules.every(subModule => subModule.checked);
        const someChecked = module.subModules.some(subModule => subModule.checked);
        
        module.checked = allChecked;
      },
      
      // 保存部门
      saveDepartment() {
        // 表单验证
        if (!this.currentDepartment.name) {
          alert('部门名称不能为空');
          return;
        }
        
        // 模拟API调用
        setTimeout(() => {
          if (this.showEditModal) {
            // 更新现有部门
            const index = this.departments.findIndex(dept => dept.id === this.currentDepartment.id);
            if (index !== -1) {
              // 更新父部门名称
              if (this.currentDepartment.parentId) {
                const parentDept = this.departments.find(dept => dept.id === this.currentDepartment.parentId);
                if (parentDept) {
                  this.currentDepartment.parentName = parentDept.name;
                  this.currentDepartment.level = parentDept.level + 1;
                }
              } else {
                this.currentDepartment.parentName = '';
                this.currentDepartment.level = 1;
              }
              
              this.departments[index] = { ...this.currentDepartment };
            }
          } else {
            // 创建新部门
            const newId = Math.max(...this.departments.map(dept => dept.id)) + 1;
            
            // 设置父部门名称和层级
            let parentName = '';
            let level = 1;
            if (this.currentDepartment.parentId) {
              const parentDept = this.departments.find(dept => dept.id === this.currentDepartment.parentId);
              if (parentDept) {
                parentName = parentDept.name;
                level = parentDept.level + 1;
              }
            }
            
            const newDepartment = {
              ...this.currentDepartment,
              id: newId,
              parentName,
              level,
              createTime: new Date().toLocaleString()
            };
            
            this.departments.push(newDepartment);
            this.totalDepartments = this.departments.length;
          }
          
          // 关闭模态框
          this.closeAllModals();
          
          // 显示成功消息
          alert(this.showEditModal ? '部门更新成功' : '部门创建成功');
        }, 500);
      },
      
      // 保存数据权限
      saveDataPermissions() {
        // 收集选中的模块权限
        const selectedModulePermissions = [];
        this.modulePermissions.forEach(module => {
          module.subModules.forEach(subModule => {
            if (subModule.checked) {
              selectedModulePermissions.push(subModule.id);
            }
          });
        });
        
        // 准备要保存的数据权限数据
        const dataPermissionData = {
          departmentId: this.currentDepartment.id,
          dataScope: this.currentDepartment.dataScope,
          modulePermissions: selectedModulePermissions
        };
        
        // 实际应用中，这里会调用API保存数据权限
        console.log('保存数据权限:', dataPermissionData);
        
        // 关闭模态框
        this.closeAllModals();
        
        // 显示成功消息
        alert('部门数据权限设置成功');
      },
      
      // 删除部门
      deleteDepartment() {
        // 模拟API调用
        setTimeout(() => {
          // 递归删除部门及其子部门
          const deleteDeptIds = [this.currentDepartment.id];
          
          const findChildDepts = (parentId) => {
            const childDepts = this.departments.filter(dept => dept.parentId === parentId);
            childDepts.forEach(child => {
              deleteDeptIds.push(child.id);
              findChildDepts(child.id);
            });
          };
          
          findChildDepts(this.currentDepartment.id);
          
          // 过滤掉要删除的部门
          this.departments = this.departments.filter(dept => !deleteDeptIds.includes(dept.id));
          this.totalDepartments = this.departments.length;
          
          // 关闭模态框
          this.closeAllModals();
          
          // 显示成功消息
          alert('部门删除成功');
        }, 500);
      }
    }
  }
  </script>
  
  <style scoped>
  /* 使用与UserManagement.vue相同的基础样式 */
  .access-module {
    padding: 20px;
  }
  
  .module-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .module-title {
    font-size: 22px;
    font-weight: 600;
    color: #333;
    margin: 0;
  }
  
  .module-actions {
    display: flex;
    gap: 10px;
  }
  
  .module-content {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
    padding: 20px;
  }
  
  /* 按钮样式 */
  .action-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 5px;
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .action-button.primary {
    background-color: #1890ff;
    color: white;
  }
  
  .action-button.primary:hover {
    background-color: #40a9ff;
  }
  
  .action-button.default {
    background-color: #f0f0f0;
    color: #333;
  }
  
  .action-button.default:hover {
    background-color: #e0e0e0;
  }
  
  .action-button.danger {
    background-color: #ff4d4f;
    color: white;
  }
  
  .action-button.danger:hover {
    background-color: #ff7875;
  }
  
  /* 图标样式 */
  [class^="icon-"] {
    display: inline-block;
    width: 16px;
    height: 16px;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
  }
  
  .icon-plus {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cline x1='12' y1='5' x2='12' y2='19'%3E%3C/line%3E%3Cline x1='5' y1='12' x2='19' y2='12'%3E%3C/line%3E%3C/svg%3E");
  }
  
  .icon-search {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Ccircle cx='11' cy='11' r='8'%3E%3C/circle%3E%3Cline x1='21' y1='21' x2='16.65' y2='16.65'%3E%3C/line%3E%3C/svg%3E");
  }
  
  .icon-refresh {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M23 4v6h-6'%3E%3C/path%3E%3Cpath d='M1 20v-6h6'%3E%3C/path%3E%3Cpath d='M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15'%3E%3C/path%3E%3C/svg%3E");
  }
  
  .icon-edit {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7'%3E%3C/path%3E%3Cpath d='M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z'%3E%3C/path%3E%3C/svg%3E");
  }
  
  .icon-database {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cellipse cx='12' cy='5' rx='9' ry='3'%3E%3C/ellipse%3E%3Cpath d='M21 12c0 1.66-4 3-9 3s-9-1.34-9-3'%3E%3C/path%3E%3Cpath d='M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5'%3E%3C/path%3E%3C/svg%3E");
  }
  
  .icon-trash {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='3 6 5 6 21 6'%3E%3C/polyline%3E%3Cpath d='M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2'%3E%3C/path%3E%3C/svg%3E");
  }
  
  .icon-x {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cline x1='18' y1='6' x2='6' y2='18'%3E%3C/line%3E%3Cline x1='6' y1='6' x2='18' y2='18'%3E%3C/line%3E%3C/svg%3E");
  }
  
  .icon-chevron-left {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='15 18 9 12 15 6'%3E%3C/polyline%3E%3C/svg%3E");
  }
  
  .icon-chevron-right {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='9 18 15 12 9 6'%3E%3C/polyline%3E%3C/svg%3E");
  }
  
  .icon-info {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Ccircle cx='12' cy='12' r='10'%3E%3C/circle%3E%3Cline x1='12' y1='16' x2='12' y2='12'%3E%3C/line%3E%3Cline x1='12' y1='8' x2='12.01' y2='8'%3E%3C/line%3E%3C/svg%3E");
  }
  
  .icon-corner-down-right {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='15 10 20 15 15 20'%3E%3C/polyline%3E%3Cpath d='M4 4v7a4 4 0 0 0 4 4h12'%3E%3C/path%3E%3C/svg%3E");
  }
  
  .icon-alert-triangle {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z'%3E%3C/path%3E%3Cline x1='12' y1='9' x2='12' y2='13'%3E%3C/line%3E%3Cline x1='12' y1='17' x2='12.01' y2='17'%3E%3C/line%3E%3C/svg%3E");
  }
  
  /* 过滤区域样式 */
  .filter-section {
    margin-bottom: 20px;
  }
  
  .filter-row {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    margin-bottom: 15px;
  }
  
  .filter-item {
    flex: 1;
    min-width: 200px;
  }
  
  .filter-label {
    display: block;
    font-size: 14px;
    color: #666;
    margin-bottom: 5px;
  }
  
  .filter-input,
  .filter-select {
    width: 100%;
    padding: 8px 12px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    font-size: 14px;
    transition: all 0.3s;
  }
  
  .filter-input:focus,
  .filter-select:focus {
    border-color: #40a9ff;
    box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
    outline: none;
  }
  
  .filter-actions {
    display: flex;
    gap: 10px;
  }
  
  /* 表格样式 */
  .data-table-wrapper {
    overflow-x: auto;
    margin-bottom: 20px;
  }
  
  .data-table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .data-table th,
  .data-table td {
    padding: 12px 16px;
    text-align: left;
    border-bottom: 1px solid #f0f0f0;
  }
  
  .data-table th {
    background-color: #fafafa;
    font-weight: 500;
    color: #333;
  }
  
  .data-table tbody tr:hover {
    background-color: #fafafa;
  }
  
  .empty-data {
    text-align: center;
    padding: 20px;
    color: #999;
  }
  
  .status-tag {
    display: inline-block;
    padding: 2px 8px;
    border-radius: 2px;
    font-size: 12px;
  }
  
  .status-tag.success {
    background-color: #f6ffed;
    border: 1px solid #b7eb8f;
    color: #52c41a;
  }
  
  .status-tag.danger {
    background-color: #fff2f0;
    border: 1px solid #ffccc7;
    color: #ff4d4f;
  }
  
  .action-group {
    display: flex;
    gap: 8px;
  }
  
  .table-action {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 24px;
    height: 24px;
    border: none;
    border-radius: 4px;
    background-color: transparent;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .table-action:hover {
    background-color: #f0f0f0;
  }
  
  .table-action.edit {
    color: #1890ff;
  }
  
  .table-action.data-perm {
    color: #722ed1;
  }
  
  .table-action.delete {
    color: #ff4d4f;
  }
  
  /* 分页样式 */
  .pagination-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
  }
  
  .pagination-info {
    font-size: 14px;
    color: #666;
  }
  
  .pagination {
    display: flex;
    align-items: center;
  }
  
  .pagination-button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    background-color: #fff;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .pagination-button:hover:not(:disabled) {
    border-color: #1890ff;
    color: #1890ff;
  }
  
  .pagination-button:disabled {
    color: #d9d9d9;
    cursor: not-allowed;
  }
  
  .page-number {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 32px;
    height: 32px;
    margin: 0 4px;
    padding: 0 6px;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .page-number:hover {
    background-color: #f0f0f0;
  }
  
  .page-number.active {
    background-color: #1890ff;
    color: #fff;
  }
  
  /* 模态框样式 */
  .modal-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
  }
  
  .modal-backdrop {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.45);
  }
  
  .modal {
    position: relative;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    width: 100%;
    max-width: 600px;
    max-height: 90vh;
    overflow-y: auto;
    z-index: 1001;
  }
  
  .modal.small {
    max-width: 400px;
  }
  
  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    border-bottom: 1px solid #f0f0f0;
  }
  
  .modal-title {
    font-size: 16px;
    font-weight: 500;
    color: #333;
    margin: 0;
  }
  
  .modal-close {
    background: none;
    border: none;
    cursor: pointer;
    color: #999;
  }
  
  .modal-body {
    padding: 24px;
  }
  
  .modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    padding: 10px 24px;
    border-top: 1px solid #f0f0f0;
  }
  
  /* 表单样式 */
  .form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .form-item {
    margin-bottom: 16px;
  }
  
  .form-item.full-width {
    grid-column: span 2;
  }
  
  .form-label {
    display: block;
    font-size: 14px;
    color: #333;
    margin-bottom: 8px;
  }
  
  .required {
    color: #ff4d4f;
    margin-left: 2px;
  }
  
  .form-input,
  .form-select,
  .form-textarea {
    width: 100%;
    padding: 8px 12px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    font-size: 14px;
    transition: all 0.3s;
  }
  
  .form-input:focus,
  .form-select:focus,
  .form-textarea:focus {
    border-color: #40a9ff;
    box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
    outline: none;
  }
  
  .form-textarea {
    resize: vertical;
    min-height: 80px;
  }
  
  /* 开关样式 */
  .switch-wrapper {
    display: flex;
    align-items: center;
  }
  
  .switch {
    position: relative;
    display: inline-block;
    width: 44px;
    height: 22px;
    margin-right: 8px;
  }
  
  .switch input {
    opacity: 0;
    width: 0;
    height: 0;
  }
  
  .slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    transition: .4s;
    border-radius: 22px;
  }
  
  .slider:before {
    position: absolute;
    content: "";
    height: 18px;
    width: 18px;
    left: 2px;
    bottom: 2px;
    background-color: white;
    transition: .4s;
    border-radius: 50%;
  }
  
  input:checked + .slider {
    background-color: #1890ff;
  }
  
  input:focus + .slider {
    box-shadow: 0 0 1px #1890ff;
  }
  
  input:checked + .slider:before {
    transform: translateX(22px);
  }
  
  .switch-text {
    font-size: 14px;
    color: #333;
  }
  
  /* 提示框样式 */
  .alert {
    display: flex;
    align-items: flex-start;
    padding: 12px 16px;
    border-radius: 4px;
    margin-bottom: 16px;
  }
  
  .alert.info {
    background-color: #e6f7ff;
    border: 1px solid #91d5ff;
  }
  
  .alert .icon-info {
    margin-right: 8px;
    flex-shrink: 0;
    color: #1890ff;
  }
  
  /* 数据权限相关样式 */
  .custom-permissions {
    margin-top: 16px;
  }
  
  .section-title {
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 12px;
    color: #333;
  }
  
  .module-permissions {
    border: 1px solid #d9d9d9;
    border-radius: 4px;
  }
  
  .module-item {
    border-bottom: 1px solid #f0f0f0;
  }
  
  .module-item:last-child {
    border-bottom: none;
  }
  
  .module-header {
    padding: 12px 16px;
    background-color: #fafafa;
  }
  
  .module-content {
    padding: 12px 16px;
    padding-left: 40px;
  }
  
  .sub-modules {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    gap: 8px;
  }
  
  /* 复选框样式 */
  .checkbox {
    display: block;
    position: relative;
    padding-left: 28px;
    margin-bottom: 8px;
    cursor: pointer;
    font-size: 14px;
    user-select: none;
  }
  
  .checkbox input {
    position: absolute;
    opacity: 0;
    cursor: pointer;
    height: 0;
    width: 0;
  }
  
  .checkmark {
    position: absolute;
    top: 0;
    left: 0;
    height: 18px;
    width: 18px;
    background-color: #fff;
    border: 1px solid #d9d9d9;
    border-radius: 2px;
  }
  
  .checkbox:hover input ~ .checkmark {
    border-color: #40a9ff;
  }
  
  .checkbox input:checked ~ .checkmark {
    background-color: #1890ff;
    border-color: #1890ff;
  }
  
  .checkmark:after {
    content: "";
    position: absolute;
    display: none;
  }
  
  .checkbox input:checked ~ .checkmark:after {
    display: block;
  }
  
  .checkbox .checkmark:after {
    left: 5px;
    top: 2px;
    width: 5px;
    height: 10px;
    border: solid white;
    border-width: 0 2px 2px 0;
    transform: rotate(45deg);
  }
  
  .checkbox-text {
    color: #333;
  }
  
  .warning-text {
    color: #ff4d4f;
    display: flex;
    align-items: center;
    gap: 8px;
  }
  </style>
  