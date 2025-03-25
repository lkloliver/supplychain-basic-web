<template>
    <div class="access-module">
      <div class="module-header">
        <h2 class="module-title">用户管理</h2>
        <div class="module-actions">
          <button class="action-button primary" @click="showAddModal = true">
            <i class="icon-plus"></i>新增用户
          </button>
        </div>
      </div>
  
      <div class="module-content">
        <!-- 搜索过滤区域 -->
        <div class="filter-section">
          <div class="filter-row">
            <div class="filter-item">
              <span class="filter-label">用户名</span>
              <input type="text" v-model="searchParams.username" class="filter-input" placeholder="请输入用户名">
            </div>
            <div class="filter-item">
              <span class="filter-label">手机号</span>
              <input type="text" v-model="searchParams.phone" class="filter-input" placeholder="请输入手机号">
            </div>
            <div class="filter-item">
              <span class="filter-label">部门</span>
              <select v-model="searchParams.deptId" class="filter-select">
                <option value="">全部</option>
                <option v-for="dept in departments" :key="dept.id" :value="dept.id">{{ dept.name }}</option>
              </select>
            </div>
            <div class="filter-item">
              <span class="filter-label">状态</span>
              <select v-model="searchParams.status" class="filter-select">
                <option value="">全部</option>
                <option value="1">启用</option>
                <option value="0">禁用</option>
              </select>
            </div>
          </div>
          <div class="filter-actions">
            <button class="action-button primary" @click="searchUsers">
              <i class="icon-search"></i>搜索
            </button>
            <button class="action-button default" @click="resetSearch">
              <i class="icon-refresh"></i>重置
            </button>
          </div>
        </div>
  
        <!-- 数据表格 -->
        <div class="data-table-wrapper">
          <table class="data-table">
            <thead>
              <tr>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>邮箱</th>
                <th>所属部门</th>
                <th>角色</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users" :key="user.id">
                <td>{{ user.username }}</td>
                <td>{{ user.realName }}</td>
                <td>{{ user.phone }}</td>
                <td>{{ user.email }}</td>
                <td>{{ user.deptName }}</td>
                <td>
                  <div class="tag-group">
                    <span v-for="role in user.roles" :key="role.id" class="tag info">
                      {{ role.name }}
                    </span>
                  </div>
                </td>
                <td>
                  <span :class="['status-tag', user.status === 1 ? 'success' : 'danger']">
                    {{ user.status === 1 ? '启用' : '禁用' }}
                  </span>
                </td>
                <td>{{ user.createTime }}</td>
                <td>
                  <div class="action-group">
                    <button class="table-action edit" @click="editUser(user)" title="编辑">
                      <i class="icon-edit"></i>
                    </button>
                    <button class="table-action assign" @click="assignRole(user)" title="分配角色">
                      <i class="icon-user-check"></i>
                    </button>
                    <button class="table-action delete" @click="confirmDelete(user)" title="删除">
                      <i class="icon-trash"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
  
        <!-- 分页 -->
        <div class="pagination-wrapper">
          <div class="pagination-info">
            显示 {{ (currentPage - 1) * pageSize + 1 }} 到 
            {{ Math.min(currentPage * pageSize, totalUsers) }} 条，
            共 {{ totalUsers }} 条记录
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
  
      <!-- 新增/编辑用户模态框 -->
      <div v-if="showAddModal || showEditModal" class="modal-wrapper">
        <div class="modal-backdrop" @click="closeAllModals"></div>
        <div class="modal">
          <div class="modal-header">
            <h3 class="modal-title">{{ showEditModal ? '编辑用户' : '新增用户' }}</h3>
            <button class="modal-close" @click="closeAllModals">
              <i class="icon-x"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="form-grid">
              <div class="form-item">
                <label class="form-label">用户名<span class="required">*</span></label>
                <input 
                  type="text" 
                  v-model="currentUser.username" 
                  class="form-input" 
                  placeholder="请输入用户名"
                  :disabled="showEditModal"
                >
              </div>
              <div class="form-item">
                <label class="form-label">姓名<span class="required">*</span></label>
                <input 
                  type="text" 
                  v-model="currentUser.realName" 
                  class="form-input" 
                  placeholder="请输入姓名"
                >
              </div>
              <template v-if="!showEditModal">
                <div class="form-item">
                  <label class="form-label">密码<span class="required">*</span></label>
                  <input 
                    type="password" 
                    v-model="currentUser.password" 
                    class="form-input" 
                    placeholder="请输入密码"
                  >
                </div>
                <div class="form-item">
                  <label class="form-label">确认密码<span class="required">*</span></label>
                  <input 
                    type="password" 
                    v-model="confirmPassword" 
                    class="form-input" 
                    placeholder="请再次输入密码"
                  >
                </div>
              </template>
              <div class="form-item">
                <label class="form-label">手机号</label>
                <input 
                  type="text" 
                  v-model="currentUser.phone" 
                  class="form-input" 
                  placeholder="请输入手机号"
                >
              </div>
              <div class="form-item">
                <label class="form-label">邮箱</label>
                <input 
                  type="email" 
                  v-model="currentUser.email" 
                  class="form-input" 
                  placeholder="请输入邮箱"
                >
              </div>
              <div class="form-item">
                <label class="form-label">所属部门</label>
                <select v-model="currentUser.deptId" class="form-select">
                  <option value="">请选择部门</option>
                  <option v-for="dept in departments" :key="dept.id" :value="dept.id">
                    {{ dept.name }}
                  </option>
                </select>
              </div>
              <div class="form-item">
                <label class="form-label">状态</label>
                <div class="switch-wrapper">
                  <label class="switch">
                    <input 
                      type="checkbox" 
                      v-model="userStatus"
                    >
                    <span class="slider"></span>
                  </label>
                  <span class="switch-text">{{ userStatus ? '启用' : '禁用' }}</span>
                </div>
              </div>
            </div>
            <div class="form-item full-width">
              <label class="form-label">备注</label>
              <textarea 
                v-model="currentUser.remark" 
                class="form-textarea" 
                rows="3" 
                placeholder="请输入备注信息"
              ></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button class="action-button default" @click="closeAllModals">取消</button>
            <button class="action-button primary" @click="saveUser">保存</button>
          </div>
        </div>
      </div>
  
      <!-- 分配角色模态框 -->
      <div v-if="showRoleModal" class="modal-wrapper">
        <div class="modal-backdrop" @click="closeAllModals"></div>
        <div class="modal">
          <div class="modal-header">
            <h3 class="modal-title">分配角色 - {{ currentUser.realName }}</h3>
            <button class="modal-close" @click="closeAllModals">
              <i class="icon-x"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="alert info">
              <i class="icon-info"></i>
              <span>用户可以拥有多个角色，每个角色具有不同的权限。请根据用户的实际工作需要分配合适的角色。</span>
            </div>
            
            <div class="form-item">
              <label class="form-label">已分配角色</label>
              <div class="assigned-roles">
                <div v-if="!currentUser.roles || currentUser.roles.length === 0" class="empty-text">
                  未分配任何角色
                </div>
                <div v-else class="tag-group">
                  <span v-for="role in currentUser.roles" :key="role.id" class="tag info with-close">
                    {{ role.name }}
                    <i class="icon-x" @click="removeRole(role)"></i>
                  </span>
                </div>
              </div>
            </div>
            
            <div class="form-item">
              <label class="form-label">可选角色</label>
              <div class="available-roles">
                <div v-for="role in availableRoles" :key="role.id" class="role-item">
                  <label class="checkbox">
                    <input 
                      type="checkbox" 
                      :value="role.id" 
                      v-model="selectedRoles"
                    >
                    <span class="checkmark"></span>
                    <span class="checkbox-text">{{ role.name }} - {{ role.description }}</span>
                  </label>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="action-button default" @click="closeAllModals">取消</button>
            <button class="action-button primary" @click="saveUserRoles">保存</button>
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
            <p>确定要删除用户 <strong>{{ currentUser.realName }}</strong> ({{ currentUser.username }}) 吗？此操作不可恢复！</p>
          </div>
          <div class="modal-footer">
            <button class="action-button default" @click="closeAllModals">取消</button>
            <button class="action-button danger" @click="deleteUser">确认删除</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'UserManagement',
    data() {
      return {
        // 搜索参数
        searchParams: {
          username: '',
          phone: '',
          deptId: '',
          status: ''
        },
        
        // 分页
        currentPage: 1,
        pageSize: 10,
        totalUsers: 0,
        
        // 用户数据
        users: [],
        
        // 部门数据
        departments: [],
        
        // 角色数据
        allRoles: [],
        
        // 当前操作的用户
        currentUser: {
          id: null,
          username: '',
          realName: '',
          password: '',
          phone: '',
          email: '',
          deptId: '',
          deptName: '',
          status: 1,
          remark: '',
          roles: []
        },
        
        // 确认密码
        confirmPassword: '',
        
        // 角色分配
        selectedRoles: [],
        
        // 模态框显示状态
        showAddModal: false,
        showEditModal: false,
        showRoleModal: false,
        showDeleteModal: false,
        
        // 用户状态开关
        userStatus: true
      }
    },
    computed: {
      // 总页数
      totalPages() {
        return Math.ceil(this.totalUsers / this.pageSize);
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
      
      // 可用角色（排除已分配的）
      availableRoles() {
        if (!this.currentUser.roles) {
          return this.allRoles;
        }
        
        const assignedRoleIds = this.currentUser.roles.map(role => role.id);
        return this.allRoles.filter(role => !assignedRoleIds.includes(role.id));
      }
    },
    watch: {
      userStatus(val) {
        this.currentUser.status = val ? 1 : 0;
      }
    },
    created() {
      this.fetchUsers();
      this.fetchDepartments();
      this.fetchRoles();
    },
    methods: {
      // 获取用户列表
      fetchUsers() {
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
      
      // 获取角色列表
      fetchRoles() {
        // 模拟API调用
        setTimeout(() => {
          // 模拟数据
          this.allRoles = [
            { id: 1, name: '管理员', description: '系统管理员，拥有所有权限' },
            { id: 2, name: '开发人员', description: '开发人员，拥有开发相关权限' },
            { id: 3, name: '市场专员', description: '市场专员，拥有市场相关权限' },
            { id: 4, name: '财务人员', description: '财务人员，拥有财务相关权限' },
            { id: 5, name: '测试人员', description: '测试人员，拥有测试相关权限' },
            { id: 6, name: '普通用户', description: '普通用户，拥有基本权限' }
          ];
        }, 500);
      },
      
      // 搜索用户
      searchUsers() {
        // 实际应用中这里会调用API
        console.log('搜索参数:', this.searchParams);
        this.currentPage = 1;
        this.fetchUsers();
      },
      
      // 重置搜索
      resetSearch() {
        this.searchParams = {
          username: '',
          phone: '',
          deptId: '',
          status: ''
        };
        this.searchUsers();
      },
      
      // 切换页码
      changePage(page) {
        if (page < 1 || page > this.totalPages) {
          return;
        }
        this.currentPage = page;
        // 实际应用中这里会调用API获取对应页的数据
      },
      
      // 编辑用户
      editUser(user) {
        this.currentUser = { ...user };
        this.userStatus = user.status === 1;
        this.confirmPassword = '';
        this.showEditModal = true;
      },
      
      // 分配角色
      assignRole(user) {
        this.currentUser = { ...user };
        this.selectedRoles = [];
        this.showRoleModal = true;
      },
      
      // 确认删除
      confirmDelete(user) {
        this.currentUser = { ...user };
        this.showDeleteModal = true;
      },
      
      // 关闭所有模态框
      closeAllModals() {
        this.showAddModal = false;
        this.showEditModal = false;
        this.showRoleModal = false;
        this.showDeleteModal = false;
      },
      
      // 移除角色
      removeRole(role) {
        if (this.currentUser.roles) {
          this.currentUser.roles = this.currentUser.roles.filter(r => r.id !== role.id);
        }
      },
      
      // 保存用户
      saveUser() {
        // 表单验证
        if (!this.currentUser.username) {
          alert('用户名不能为空');
          return;
        }
        
        if (!this.currentUser.realName) {
          alert('姓名不能为空');
          return;
        }
        
        if (this.showAddModal) {
          if (!this.currentUser.password) {
            alert('密码不能为空');
            return;
          }
          
          if (this.currentUser.password !== this.confirmPassword) {
            alert('两次输入的密码不一致');
            return;
          }
        }
        
        // 模拟API调用
        setTimeout(() => {
          if (this.showEditModal) {
            // 更新现有用户
            const index = this.users.findIndex(user => user.id === this.currentUser.id);
            if (index !== -1) {
              // 更新部门名称
              if (this.currentUser.deptId) {
                const dept = this.departments.find(dept => dept.id === this.currentUser.deptId);
                if (dept) {
                  this.currentUser.deptName = dept.name;
                }
              }
              
              this.users[index] = { ...this.currentUser };
            }
          } else {
            // 创建新用户
            const newId = Math.max(...this.users.map(user => user.id)) + 1;
            
            // 设置部门名称
            let deptName = '';
            if (this.currentUser.deptId) {
              const dept = this.departments.find(dept => dept.id === this.currentUser.deptId);
              if (dept) {
                deptName = dept.name;
              }
            }
            
            const newUser = {
              ...this.currentUser,
              id: newId,
              deptName,
              createTime: new Date().toLocaleString(),
              roles: []
            };
            
            this.users.push(newUser);
            this.totalUsers = this.users.length;
          }
          
          // 关闭模态框
          this.closeAllModals();
          
          // 显示成功消息
          alert(this.showEditModal ? '用户更新成功' : '用户创建成功');
        }, 500);
      },
      
      // 保存用户角色
      saveUserRoles() {
        // 添加选中的角色
        if (this.selectedRoles.length > 0) {
          if (!this.currentUser.roles) {
            this.currentUser.roles = [];
          }
          
          this.selectedRoles.forEach(roleId => {
            const role = this.allRoles.find(r => r.id === roleId);
            if (role && !this.currentUser.roles.some(r => r.id === roleId)) {
              this.currentUser.roles.push({ id: role.id, name: role.name });
            }
          });
        }
        
        // 模拟API调用
        setTimeout(() => {
          // 更新用户的角色
          const index = this.users.findIndex(user => user.id === this.currentUser.id);
          if (index !== -1) {
            this.users[index].roles = [...this.currentUser.roles];
          }
          
          // 关闭模态框
          this.closeAllModals();
          
          // 显示成功消息
          alert('角色分配成功');
        }, 500);
      },
      
      // 删除用户
      deleteUser() {
        // 模拟API调用
        setTimeout(() => {
          // 从列表中移除用户
          this.users = this.users.filter(user => user.id !== this.currentUser.id);
          this.totalUsers = this.users.length;
          
          // 关闭模态框
          this.closeAllModals();
          
          // 显示成功消息
          alert('用户删除成功');
        }, 500);
      }
    }
  }
  </script>
  
  <style scoped>
  /* 通用模块样式 */
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
  
  .icon-user-check {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'%3E%3C/path%3E%3Ccircle cx='8.5' cy='7' r='4'%3E%3C/circle%3E%3Cpolyline points='17 11 19 13 23 9'%3E%3C/polyline%3E%3C/svg%3E");
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
  
  .tag-group {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
  }
  
  .tag {
    display: inline-flex;
    align-items: center;
    padding: 2px 8px;
    border-radius: 2px;
    font-size: 12px;
  }
  
  .tag.info {
    background-color: #e6f7ff;
    border: 1px solid #91d5ff;
    color: #1890ff;
  }
  
  .tag.with-close {
    padding-right: 4px;
  }
  
  .tag.with-close .icon-x {
    margin-left: 4px;
    cursor: pointer;
    opacity: 0.7;
  }
  
  .tag.with-close .icon-x:hover {
    opacity: 1;
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
  
  .table-action.assign {
    color: #fa8c16;
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
  
  /* 角色分配相关样式 */
  .assigned-roles {
    min-height: 60px;
    padding: 8px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    margin-bottom: 16px;
  }
  
  .empty-text {
    color: #999;
    font-style: italic;
  }
  
  .available-roles {
    max-height: 200px;
    overflow-y: auto;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    padding: 8px;
  }
  
  .role-item {
    margin-bottom: 8px;
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
  </style>
  