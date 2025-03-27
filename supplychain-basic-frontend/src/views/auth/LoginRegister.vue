<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="logo-container">
        <img src="/logo.svg" alt="Logo" class="logo" />
        <h1 class="title">供应链管理系统</h1>
      </div>
      
      <div class="tabs">
        <button 
          :class="['tab-btn', { active: activeTab === 'login' }]" 
          @click="activeTab = 'login'"
        >
          登录
        </button>
        <button 
          :class="['tab-btn', { active: activeTab === 'register' }]" 
          @click="activeTab = 'register'"
        >
          注册
        </button>
      </div>
      
      <!-- 登录表单 -->
      <div v-if="activeTab === 'login'" class="form-container">
        <div class="login-type-selector">
          <button 
            :class="['login-type-btn', { active: loginType === 'username' }]" 
            @click="switchLoginType('username')"
          >
            用户名密码登录
          </button>
          <button 
            :class="['login-type-btn', { active: loginType === 'phone' }]" 
            @click="switchLoginType('phone')"
          >
            手机验证码登录
          </button>
        </div>
        
        <!-- 用户名密码登录 -->
        <form v-if="loginType === 'username'" @submit.prevent="handleLogin" ref="loginFormRef" class="login-form">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="loginForm.username" 
              placeholder="请输入用户名"
              :rules="loginRules.username"
            />
          </div>
          
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="loginForm.password" 
              placeholder="请输入密码"
              :rules="loginRules.password"
            />
          </div>
          
          <div class="form-group">
            <label>版本选择</label>
            <div class="version-selector">
              <label class="version-option">
                <input 
                  type="radio" 
                  v-model="loginForm.version" 
                  value="BASE" 
                  name="version"
                />
                <span>基地版</span>
              </label>
              <label class="version-option">
                <input 
                  type="radio" 
                  v-model="loginForm.version" 
                  value="NON_BASE" 
                  name="version"
                />
                <span>非基地版</span>
              </label>
            </div>
          </div>
          
          <div class="form-group">
            <label>是否管理员</label>
            <div class="admin-selector">
              <label class="admin-option">
                <input 
                  type="checkbox" 
                  v-model="loginForm.isAdmin" 
                  name="isAdmin"
                />
                <span>管理员登录</span>
              </label>
            </div>
          </div>
          
          <button type="submit" class="submit-btn">登录</button>
        </form>
        
        <!-- 手机验证码登录 -->
        <form v-else @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label for="phone">手机号</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="loginForm.phone" 
              placeholder="请输入手机号"
              :rules="loginRules.phone"
            />
          </div>
          
          <div class="form-group">
            <label for="code">验证码</label>
            <div class="sms-code-container">
              <input 
                type="text" 
                id="code" 
                v-model="loginForm.code" 
                placeholder="请输入验证码"
                :rules="loginRules.code"
              />
              <button 
                type="button" 
                class="sms-btn" 
                :disabled="smsCountdown > 0"
                @click="sendSmsCode"
              >
                {{ smsCountdown > 0 ? `${smsCountdown}s后重新获取` : '获取验证码' }}
              </button>
            </div>
          </div>
          
          <div class="form-group">
            <label>版本选择</label>
            <div class="version-selector">
              <label class="version-option">
                <input 
                  type="radio" 
                  v-model="loginForm.version" 
                  value="BASE" 
                  name="version"
                />
                <span>基地版</span>
              </label>
              <label class="version-option">
                <input 
                  type="radio" 
                  v-model="loginForm.version" 
                  value="NON_BASE" 
                  name="version"
                />
                <span>非基地版</span>
              </label>
            </div>
          </div>
          
          <div class="form-group">
            <label>是否管理员</label>
            <div class="admin-selector">
              <label class="admin-option">
                <input 
                  type="checkbox" 
                  v-model="loginForm.isAdmin" 
                  name="isAdmin"
                />
                <span>管理员登录</span>
              </label>
            </div>
          </div>
          
          <button type="submit" class="submit-btn">登录</button>
        </form>
      </div>
      
      <!-- 注册表单 -->
      <div v-else class="form-container">
        <form @submit.prevent="handleRegister">
          <div class="form-group">
            <label for="reg-phone">手机号</label>
            <input 
              type="tel" 
              id="reg-phone" 
              v-model="registerForm.phone" 
              placeholder="请输入手机号"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="reg-smsCode">验证码</label>
            <div class="sms-code-container">
              <input 
                type="text" 
                id="reg-smsCode" 
                v-model="registerForm.code" 
                placeholder="请输入验证码"
                required
              />
              <button 
                type="button" 
                class="sms-btn" 
                :disabled="regSmsCountdown > 0"
                @click="sendRegSmsCode"
              >
                {{ regSmsCountdown > 0 ? `${regSmsCountdown}s后重新获取` : '获取验证码' }}
              </button>
            </div>
          </div>
          
          <div class="form-group">
            <label for="reg-username">用户名</label>
            <input 
              type="text" 
              id="reg-username" 
              v-model="registerForm.username" 
              placeholder="请输入用户名"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="reg-password">密码</label>
            <input 
              type="password" 
              id="reg-password" 
              v-model="registerForm.password" 
              placeholder="请输入密码"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="reg-confirm-password">确认密码</label>
            <input 
              type="password" 
              id="reg-confirm-password" 
              v-model="registerForm.confirmPassword" 
              placeholder="请再次输入密码"
              required
            />
          </div>
          
          <button type="submit" class="submit-btn">注册</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import type { FormInstance } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();

// 标签页切换
const activeTab = ref('login');

// 登录方式切换
const loginType = ref<'username' | 'phone'>('username');

// 定义登录表单类型
interface LoginForm {
  username: string;
  password: string;
  phone: string;
  code: string;
  version: 'BASE' | 'NON_BASE';
  isAdmin: boolean;
}

// 定义注册表单类型
interface RegisterForm {
  username: string;
  password: string;
  confirmPassword: string;
  phone: string;
  code: string;
}

// 登录表单数据
const loginForm = reactive<LoginForm>({
  username: '',
  password: '',
  phone: '',
  code: '',
  version: 'BASE',
  isAdmin: false
});

// 注册表单数据
const registerForm = reactive<RegisterForm>({
  username: '',
  password: '',
  confirmPassword: '',
  phone: '',
  code: ''
});

// 短信验证码倒计时
const smsCountdown = ref(0);
const regSmsCountdown = ref(0);

// 登录表单校验规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]{4,16}$/, message: '用户名必须是4-16位字母、数字或下划线', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]{6,20}$/, message: '密码必须是6-20位字母、数字或下划线', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { pattern: /^\d{6}$/, message: '验证码必须是6位数字', trigger: 'blur' }
  ]
};

// 登录表单引用
const loginFormRef = ref<FormInstance>();

// 登录方法
const handleLogin = async (event) => {
  try {
    // 验证表单
    if (loginType.value === 'username') {
      if (!loginForm.username) {
        ElMessage.error('请输入用户名')
        return
      }
      if (!loginForm.password) {
        ElMessage.error('请输入密码')
        return
      }
      if (!/^[a-zA-Z0-9_]{4,16}$/.test(loginForm.username)) {
        ElMessage.error('用户名必须是4-16位字母、数字或下划线')
        return
      }
      if (!/^[a-zA-Z0-9_]{6,20}$/.test(loginForm.password)) {
        ElMessage.error('密码必须是6-20位字母、数字或下划线')
        return
      }
    } else {
      if (!loginForm.phone) {
        ElMessage.error('请输入手机号')
        return
      }
      if (!loginForm.code) {
        ElMessage.error('请输入验证码')
        return
      }
      if (!/^1[3-9]\d{9}$/.test(loginForm.phone)) {
        ElMessage.error('手机号格式不正确')
        return
      }
      if (!/^\d{6}$/.test(loginForm.code)) {
        ElMessage.error('验证码必须是6位数字')
        return
      }
    }

    if (!loginForm.version) {
      ElMessage.error('请选择版本')
      return
    }

    // 构造登录请求参数
    const loginData = {
      isAdmin: loginForm.isAdmin,
      version: loginForm.version
    }

    // 根据登录方式添加对应的参数
    if (loginType.value === 'username') {
      Object.assign(loginData, {
        username: loginForm.username,
        password: loginForm.password
      })
    } else {
      Object.assign(loginData, {
        phone: loginForm.phone,
        code: loginForm.code
      })
    }

    // 发送登录请求
    const res = await request.post('/auth/login', loginData)
    
    if (res.code === 200) {
      // 保存token和用户信息
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data.user))
      localStorage.setItem('isAdmin', String(loginForm.isAdmin))
      localStorage.setItem('version', loginForm.version)
      
      // 更新状态管理
      userStore.initState()
      
      // 显示登录成功提示
      ElMessage.success('登录成功')
      
      // 跳转到仪表盘
      router.push('/dashboard')
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.response?.data?.message || '登录失败，请稍后重试')
  }
}

// 切换登录方式
const switchLoginType = (type) => {
  loginType.value = type
  // 清空表单
  loginForm.username = ''
  loginForm.password = ''
  loginForm.phone = ''
  loginForm.code = ''
}

// 发送验证码
const sendSmsCode = async () => {
  try {
    // 验证手机号格式
    if (!loginForm.phone) {
      ElMessage.error('请输入手机号')
      return
    }
    if (!/^1[3-9]\d{9}$/.test(loginForm.phone)) {
      ElMessage.error('请输入正确的手机号')
      return
    }

    // 发送验证码请求
    const res = await request.get('/sms/code', {
      params: { phone: loginForm.phone }
    })

    if (res.code === 200) {
      ElMessage.success('验证码已发送')
      // TODO: 添加倒计时逻辑
    } else {
      ElMessage.error(res.message || '验证码发送失败')
    }
  } catch (error) {
    console.error('验证码发送失败:', error)
    ElMessage.error(error.response?.data?.message || '验证码发送失败，请稍后重试')
  }
}

// 注册
const handleRegister = async () => {
  // 验证密码是否一致
  if (registerForm.password !== registerForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致');
    return;
  }
  
  try {
    const response = await request.post('/register', {
      phone: registerForm.phone,
      code: registerForm.code,
      username: registerForm.username,
      password: registerForm.password,
      confirmPassword: registerForm.confirmPassword
    });
    
    if (response.code === 200) {
      ElMessage.success('注册成功，请登录');
      // 注册成功，切换到登录页
      activeTab.value = 'login';
      // 清空注册表单
      registerForm.phone = '';
      registerForm.code = '';
      registerForm.username = '';
      registerForm.password = '';
      registerForm.confirmPassword = '';
    } else {
      ElMessage.error(response.message || '注册失败，请稍后重试');
    }
  } catch (error: any) {
    console.error('注册失败', error);
    ElMessage.error(error.response?.data?.message || '注册失败，请稍后重试');
  }
};

// 发送注册短信验证码
const sendRegSmsCode = async () => {
  if (!registerForm.phone) {
    ElMessage.warning('请输入手机号');
    return;
  }
  
  if (!loginRules.phone[1].pattern.test(registerForm.phone)) {
    ElMessage.warning('请输入正确的手机号格式');
    return;
  }
  
  try {
    const response = await request.get('/sms/code', {
      params: { phone: registerForm.phone }
    });
    ElMessage.success('验证码已发送');
    // 开始倒计时
    regSmsCountdown.value = 60;
    const timer = setInterval(() => {
      regSmsCountdown.value--;
      if (regSmsCountdown.value <= 0) {
        clearInterval(timer);
      }
    }, 1000);
  } catch (error: any) {
    console.error('验证码发送失败:', error);
    ElMessage.error(error.response?.data?.message || '验证码发送失败，请稍后重试');
  }
};

defineExpose({
  activeTab,
  loginType,
  loginForm,
  loginFormRef,
  handleLogin,
  switchLoginType,
  sendSmsCode,
  registerForm,
  regSmsCountdown,
  sendRegSmsCode
});
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a2463, #1e88e5);
}

.auth-card {
  width: 100%;
  max-width: 480px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  padding: 2rem;
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 2rem;
}

.logo {
  width: 48px;
  height: 48px;
  margin-right: 1rem;
}

.title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #0a2463;
  margin: 0;
}

.tabs {
  display: flex;
  margin-bottom: 1.5rem;
  border-bottom: 1px solid #e0e0e0;
}

.tab-btn {
  flex: 1;
  padding: 0.75rem 0;
  background: none;
  border: none;
  font-size: 1rem;
  font-weight: 500;
  color: #757575;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-btn.active {
  color: #1e88e5;
  border-bottom: 2px solid #1e88e5;
}

.form-container {
  margin-top: 1rem;
}

.login-type-selector {
  display: flex;
  margin-bottom: 1.5rem;
}

.login-type-btn {
  flex: 1;
  padding: 0.5rem 0;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
  font-size: 0.875rem;
  color: #757575;
  cursor: pointer;
  transition: all 0.3s;
}

.login-type-btn:first-child {
  border-radius: 4px 0 0 4px;
}

.login-type-btn:last-child {
  border-radius: 0 4px 4px 0;
}

.login-type-btn.active {
  background: #e3f2fd;
  color: #1e88e5;
  border-color: #1e88e5;
}

.form-group {
  margin-bottom: 1.25rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  color: #424242;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input:focus {
  border-color: #1e88e5;
  outline: none;
}

.sms-code-container {
  display: flex;
  gap: 0.5rem;
}

.sms-code-container input {
  flex: 1;
}

.sms-btn {
  padding: 0 1rem;
  background: #1e88e5;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 0.875rem;
  cursor: pointer;
  white-space: nowrap;
  transition: background-color 0.3s;
}

.sms-btn:hover {
  background: #1976d2;
}

.sms-btn:disabled {
  background: #90caf9;
  cursor: not-allowed;
}

.version-selector {
  display: flex;
  gap: 1.5rem;
}

.version-option {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.version-option input {
  margin-right: 0.5rem;
  width: auto;
}

.submit-btn {
  width: 100%;
  padding: 0.75rem;
  background: #1e88e5;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background: #1976d2;
}

.admin-selector {
  display: flex;
  margin-top: 0.5rem;
}

.admin-option {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.admin-option input {
  margin-right: 0.5rem;
  width: auto;
}
</style>

