export interface UserInfo {
  username?: string;
  phone?: string;
  version: string;
  role: 'admin' | 'user';
}

export interface LoginForm {
  username: string;
  password: string;
  phone: string;
  smsCode: string;
  version: string;
  isAdmin: boolean;
}

export interface RegisterForm {
  phone: string;
  smsCode: string;
  username: string;
  password: string;
  confirmPassword: string;
} 