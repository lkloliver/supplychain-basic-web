# 用户注册接口

## 接口说明
用户通过手机号、验证码、用户名和密码进行注册。

## 请求信息
- 请求路径：`/api/register`
- 请求方式：POST
- Content-Type：application/json

## 请求参数
| 参数名 | 类型   | 必填 | 说明     | 示例值      | 格式要求 |
|--------|--------|------|----------|-------------|----------|
| phone  | string | 是   | 手机号码 | 13800138000 | 11位手机号 |
| code   | string | 是   | 验证码   | 123456     | 6位数字   |
| username | string | 是 | 用户名   | test_user   | 4-16位字母、数字或下划线 |
| password | string | 是 | 密码     | password123 | 6-20位字母、数字或下划线 |
| confirmPassword | string | 是 | 确认密码 | password123 | 必须与password一致 |

## 请求示例
```json
{
    "phone": "13800138000",
    "code": "123456",
    "username": "test_user",
    "password": "password123",
    "confirmPassword": "password123"
}
```

## 响应信息
- 响应格式：JSON

### 成功响应
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "id": 1,
        "phone": "13800138000",
        "username": "test_user"
    }
}
```

### 失败响应
```json
{
    "code": 400,
    "message": "验证码错误或已过期",
    "data": null
}
```

## 响应参数说明
| 参数名  | 类型   | 说明     | 示例值  |
|---------|--------|----------|---------|
| code    | int    | 状态码   | 200     |
| message | string | 响应信息 | success |
| data    | object | 用户信息 | -       |

## 状态码说明
| 状态码 | 说明     | 处理建议                     |
|--------|----------|------------------------------|
| 200    | 成功     | 正常处理                     |
| 400    | 请求错误 | 检查请求参数是否正确         |
| 500    | 服务错误 | 联系管理员或稍后重试         |

## 错误信息说明
| 错误信息 | 说明     | 处理建议                     |
|----------|----------|------------------------------|
| 验证码错误或已过期 | 验证码不正确或超过有效期 | 重新获取验证码 |
| 两次输入的密码不一致 | 密码和确认密码不匹配 | 检查密码输入 |
| 用户名已存在 | 该用户名已被注册 | 更换用户名 |
| 该手机号已注册 | 该手机号已被注册 | 使用其他手机号或直接登录 |

## 调用示例
```bash
curl -X POST "http://localhost:8080/api/register" \
     -H "Content-Type: application/json" \
     -d '{
           "phone": "13800138000",
           "code": "123456",
           "username": "test_user",
           "password": "password123",
           "confirmPassword": "password123"
         }'
```

## 注意事项
1. 注册前需要先获取手机验证码
2. 验证码有效期为5分钟
3. 密码会被加密存储
4. 用户名和手机号不能重复
5. 密码必须符合格式要求 