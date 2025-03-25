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
            :class="['login-type-btn', { active: loginType === 'password' }]" 
            @click="loginType = 'password'"
          >
            用户名密码登录
          </button>
          <button 
            :class="['login-type-btn', { active: loginType === 'sms' }]" 
            @click="loginType = 'sms'"
          >
            手机验证码登录
          </button>
        </div>
        
        <!-- 用户名密码登录 -->
        <form v-if="loginType === 'password'" @submit.prevent="handlePasswordLogin">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="loginForm.username" 
              placeholder="请输入用户名"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="loginForm.password" 
              placeholder="请输入密码"
              required
            />
          </div>
          
          <div class="form-group">
            <label>版本选择</label>
            <div class="version-selector">
              <label class="version-option">
                <input 
                  type="radio" 
                  v-model="loginForm.version" 
                  value="base" 
                  name="version"
                />
                <span>基地版</span>
              </label>
              <label class="version-option">
                <input 
                  type="radio" 
                  v-model="loginForm.version" 
                  value="non-base" 
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
        <form v-else @submit.prevent="handleSmsLogin">
          <div class="form-group">
            <label for="phone">手机号</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="loginForm.phone" 
              placeholder="请输入手机号"
              required
            />
          </div>
          
          <div class="form-group">
            <label for="smsCode">验证码</label>
            <div class="sms-code-container">
              <input 
                type="text" 
                id="smsCode" 
                v-model="loginForm.smsCode" 
                placeholder="请输入验证码"
                required
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
                  value="base" 
                  name="version"
                />
                <span>基地版</span>
              </label>
              <label class="version-option">
                <input 
                  type="radio" 
                  v-model="loginForm.version" 
                  value="non-base" 
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
                v-model="registerForm.smsCode" 
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

const router = useRouter();
const userStore = useUserStore();

// 标签页切换
const activeTab = ref('login');

// 登录方式切换
const loginType = ref('password');

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  phone: '',
  smsCode: '',
  version: 'base', // 默认选择基地版
  isAdmin: false   // 添加管理员标志
});

// 注册表单数据
const registerForm = reactive({
  phone: '',
  smsCode: '',
  username: '',
  password: '',
  confirmPassword: ''
});

// 短信验证码倒计时
const smsCountdown = ref(0);
const regSmsCountdown = ref(0);

// 发送登录短信验证码
const sendSmsCode = async () => {
  if (!loginForm.phone) {
    alert('请输入手机号');
    return;
  }
  
  try {
    // 这里调用发送短信验证码的API
    // await api.sendSmsCode(loginForm.phone);
    
    // 模拟API调用
    console.log('发送验证码到', loginForm.phone);
    
    // 开始倒计时
    smsCountdown.value = 60;
    const timer = setInterval(() => {
      smsCountdown.value--;
      if (smsCountdown.value <= 0) {
        clearInterval(timer);
      }
    }, 1000);
  } catch (error) {
    console.error('发送验证码失败', error);
    alert('发送验证码失败，请稍后重试');
  }
};

// 发送注册短信验证码
const sendRegSmsCode = async () => {
  if (!registerForm.phone) {
    alert('请输入手机号');
    return;
  }
  
  try {
    // 这里调用发送短信验证码的API
    // await api.sendSmsCode(registerForm.phone);
    
    // 模拟API调用
    console.log('发送验证码到', registerForm.phone);
    
    // 开始倒计时
    regSmsCountdown.value = 60;
    const timer = setInterval(() => {
      regSmsCountdown.value--;
      if (regSmsCountdown.value <= 0) {
        clearInterval(timer);
      }
    }, 1000);
  } catch (error) {
    console.error('发送验证码失败', error);
    alert('发送验证码失败，请稍后重试');
  }
};

// 用户名密码登录
const handlePasswordLogin = async () => {
  try {
    // 这里调用登录API
    // const response = await api.login({
    //   username: loginForm.username,
    //   password: loginForm.password,
    //   version: loginForm.version
    // });
    
    // 模拟API调用
    console.log('用户名密码登录', loginForm);
    
    // 存储用户信息和版本信息
    userStore.setUser({
      username: loginForm.username,
      version: loginForm.version,
      role: loginForm.isAdmin ? 'admin' : 'user'  // 设置角色
    });
    
    // 登录成功，跳转到首页
    router.push('/dashboard');
  } catch (error) {
    console.error('登录失败', error);
    alert('登录失败，请检查用户名和密码');
  }
};

// 手机验证码登录
const handleSmsLogin = async () => {
  try {
    // 这里调用登录API
    // const response = await api.smsLogin({
    //   phone: loginForm.phone,
    //   smsCode: loginForm.smsCode,
    //   version: loginForm.version
    // });
    
    // 模拟API调用
    console.log('手机验证码登录', loginForm);
    
    // 存储用户信息和版本信息
    userStore.setUser({
      phone: loginForm.phone,
      version: loginForm.version,
      role: loginForm.isAdmin ? 'admin' : 'user'  // 设置角色
    });
    
    // 登录成功，跳转到首页
    router.push('/dashboard');
  } catch (error) {
    console.error('登录失败', error);
    alert('登录失败，请检查手机号和验证码');
  }
};

// 注册
const handleRegister = async () => {
  // 验证密码是否一致
  if (registerForm.password !== registerForm.confirmPassword) {
    alert('两次输入的密码不一致');
    return;
  }
  
  try {
    // 这里调用注册API
    // const response = await api.register({
    //   phone: registerForm.phone,
    //   smsCode: registerForm.smsCode,
    //   username: registerForm.username,
    //   password: registerForm.password
    // });
    
    // 模拟API调用
    console.log('注册', registerForm);
    
    // 注册成功，切换到登录页
    activeTab.value = 'login';
    alert('注册成功，请登录');
  } catch (error) {
    console.error('注册失败', error);
    alert('注册失败，请稍后重试');
  }
};
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

