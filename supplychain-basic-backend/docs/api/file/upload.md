# 文件上传接口

## 接口说明
用于上传文件并返回文件的访问URL。支持多种文件类型，包括图片、文档等。

## 请求信息
- 请求路径：`/api/file/upload`
- 请求方式：POST
- Content-Type：multipart/form-data

## 请求参数
| 参数名 | 类型   | 必填 | 说明     | 示例值      |
|--------|--------|------|----------|-------------|
| file   | file   | 是   | 文件     | test.jpg    |
| type   | string | 否   | 文件类型 | image       |

## 请求示例
```bash
# 使用curl上传文件
curl -X POST "http://localhost:8080/api/file/upload" \
     -H "Authorization: Bearer your_token_here" \
     -F "file=@/path/to/your/file.jpg" \
     -F "type=image"
```

## 响应信息
- 响应格式：JSON

### 成功响应
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "url": "http://localhost:8080/files/2024/03/21/example.jpg",
        "filename": "example.jpg",
        "size": 1024,
        "type": "image/jpeg"
    }
}
```

### 失败响应
```json
{
    "code": 400,
    "message": "不支持的文件类型",
    "data": null
}
```

## 响应参数说明
| 参数名   | 类型   | 说明         | 示例值  |
|----------|--------|--------------|---------|
| code     | int    | 状态码       | 200     |
| message  | string | 响应信息     | success |
| data     | object | 文件信息     | -       |
| url      | string | 文件访问URL  | http://localhost:8080/files/example.jpg |
| filename | string | 文件名       | example.jpg |
| size     | long   | 文件大小(字节)| 1024    |
| type     | string | 文件MIME类型 | image/jpeg |

## 状态码说明
| 状态码 | 说明     | 处理建议                     |
|--------|----------|------------------------------|
| 200    | 成功     | 正常处理                     |
| 400    | 请求错误 | 检查文件类型或大小是否符合要求 |
| 401    | 未授权   | 检查token是否正确或过期      |
| 500    | 服务错误 | 联系管理员或稍后重试         |

## 错误信息说明
| 错误信息 | 说明     | 处理建议                     |
|----------|----------|------------------------------|
| 不支持的文件类型 | 上传的文件类型不被支持 | 检查文件类型是否符合要求 |
| 文件大小超过限制 | 文件大小超过系统限制 | 压缩文件或分片上传 |
| 上传失败 | 文件上传过程中出现错误 | 重试或联系管理员 |

## 支持的文件类型
- 图片：jpg、jpeg、png、gif
- 文档：pdf、doc、docx、xls、xlsx
- 其他：txt、zip、rar

## 注意事项
1. 文件大小限制：单个文件不超过10MB
2. 需要在请求头中携带有效的token
3. 上传成功后返回的URL可以直接访问文件
4. 建议在上传大文件时添加进度提示
5. 文件名会自动进行编码和重命名，以避免重复和特殊字符问题 