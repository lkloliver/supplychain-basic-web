<template>
  <a-modal
    :visible="visible"
    :title="formData?.id ? '编辑用户' : '新增用户'"
    @ok="handleSubmit"
    @cancel="handleCancel"
    :confirmLoading="loading"
  >
    <a-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 16 }"
    >
      <a-form-item label="用户名" name="username">
        <a-input v-model:value="form.username" placeholder="请输入用户名" />
      </a-form-item>

      <a-form-item 
        label="密码" 
        name="password"
        :rules="[
          { required: !formData?.id, message: '请输入密码' },
          { min: 6, message: '密码长度不能小于6位' }
        ]"
      >
        <a-input-password 
          v-model:value="form.password" 
          placeholder="请输入密码"
          :disabled="!!formData?.id"
        />
      </a-form-item>

      <a-form-item label="姓名" name="realName">
        <a-input v-model:value="form.realName" placeholder="请输入姓名" />
      </a-form-item>

      <a-form-item label="手机号" name="phone">
        <a-input v-model:value="form.phone" placeholder="请输入手机号" />
      </a-form-item>

      <a-form-item label="邮箱" name="email">
        <a-input v-model:value="form.email" placeholder="请输入邮箱" />
      </a-form-item>

      <a-form-item label="所属部门" name="departmentId">
        <a-tree-select
          v-model:value="form.departmentId"
          :tree-data="departmentTree"
          placeholder="请选择所属部门"
          allow-clear
          tree-default-expand-all
        />
      </a-form-item>

      <a-form-item label="角色" name="roleIds">
        <a-select
          v-model:value="form.roleIds"
          mode="multiple"
          placeholder="请选择角色"
          :options="roleOptions"
        />
      </a-form-item>

      <a-form-item label="状态" name="status">
        <a-radio-group v-model:value="form.status">
          <a-radio value="active">启用</a-radio>
          <a-radio value="inactive">禁用</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { message } from 'ant-design-vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  formData: {
    type: Object,
    default: () => null
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref(null)
const loading = ref(false)

// 表单数据
const form = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  departmentId: undefined,
  roleIds: [],
  status: 'active'
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名' },
    { min: 3, message: '用户名长度不能小于3位' }
  ],
  realName: [
    { required: true, message: '请输入姓名' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  departmentId: [
    { required: true, message: '请选择所属部门' }
  ],
  roleIds: [
    { required: true, message: '请选择角色' }
  ]
}

// 部门树数据
const departmentTree = ref([])
// 角色选项
const roleOptions = ref([])

// 监听表单数据变化
watch(
  () => props.formData,
  (val) => {
    if (val) {
      Object.keys(form).forEach(key => {
        form[key] = val[key]
      })
    } else {
      Object.keys(form).forEach(key => {
        form[key] = ''
      })
      form.status = 'active'
      form.roleIds = []
      form.departmentId = undefined
    }
  },
  { immediate: true }
)

// 获取部门树数据
const fetchDepartmentTree = async () => {
  try {
    // 这里调用获取部门树的API
    // const res = await getDepartmentTree()
    // departmentTree.value = res.data
  } catch (error) {
    console.error('获取部门树失败:', error)
  }
}

// 获取角色列表
const fetchRoleOptions = async () => {
  try {
    // 这里调用获取角色列表的API
    // const res = await getRoleList()
    // roleOptions.value = res.data.map(item => ({
    //   label: item.name,
    //   value: item.id
    // }))
  } catch (error) {
    console.error('获取角色列表失败:', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    
    // 这里调用保存用户的API
    // const api = form.id ? updateUser : createUser
    // await api(form)
    
    message.success(form.id ? '编辑成功' : '新增成功')
    emit('success')
    handleCancel()
  } catch (error) {
    console.error('保存失败:', error)
  } finally {
    loading.value = false
  }
}

// 关闭弹窗
const handleCancel = () => {
  formRef.value?.resetFields()
  emit('update:visible', false)
}

// 初始化数据
fetchDepartmentTree()
fetchRoleOptions()
</script>

<style lang="less" scoped>
.ant-form-item {
  margin-bottom: 16px;
}
</style> 