# 人员库管理系统 API 文档

## 1. 认证接口

### 1.1 登录接口
- **URL**: `/api/auth/login`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "username": "admin",
    "password": "123456"
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "username": "admin",
    "name": "超级管理员",
    "roles": [
      {
        "id": 1,
        "name": "ADMIN",
        "description": "管理员"
      }
    ],
    "token": "mock-token"
  }
  ```

## 2. 人员信息接口

### 2.1 获取人员列表（管理员）
- **URL**: `/api/admin/personnel`
- **方法**: `GET`
- **参数**:
  - `name`: 姓名（模糊搜索）
  - `gender`: 性别
  - `categoryId`: 专业类目ID
  - `department`: 部门（模糊搜索）
  - `position`: 职位（模糊搜索）
  - `phone`: 电话（模糊搜索）
  - `email`: 邮箱（模糊搜索）
  - `page`: 页码（默认1）
  - `size`: 每页数量（默认10）
- **响应**:
  ```json
  {
    "content": [
      {
        "id": 1,
        "name": "张三",
        "gender": "男",
        "age": 25,
        "category": {
          "id": 1,
          "name": "计算机科学"
        },
        "department": "技术部",
        "position": "工程师",
        "phone": "13800138000",
        "email": "zhangsan@example.com",
        "address": "北京市海淀区"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
  ```

### 2.2 保存人员信息（录入员）
- **URL**: `/api/entry/personnel`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "name": "李四",
    "gender": "女",
    "age": 23,
    "categoryId": 2,
    "department": "市场部",
    "position": "专员",
    "phone": "13800138001",
    "email": "lisi@example.com",
    "address": "上海市浦东新区"
  }
  ```
- **响应**:
  ```json
  {
    "id": 2,
    "name": "李四",
    "gender": "女",
    "age": 23,
    "category": {
      "id": 2,
      "name": "电子工程"
    },
    "department": "市场部",
    "position": "专员",
    "phone": "13800138001",
    "email": "lisi@example.com",
    "address": "上海市浦东新区"
  }
  ```

### 2.3 删除人员信息（管理员）
- **URL**: `/api/admin/personnel/{id}`
- **方法**: `DELETE`
- **响应**: 200 OK

### 2.4 导入Excel（录入员）
- **URL**: `/api/entry/personnel/import`
- **方法**: `POST`
- **参数**: `file` (MultipartFile)
- **响应**: 200 OK

### 2.5 导出Excel（管理员）
- **URL**: `/api/admin/personnel/export`
- **方法**: `GET`
- **参数**: 同获取人员列表
- **响应**: Excel文件

### 2.6 随机抽取（管理员）
- **URL**: `/api/admin/personnel/select`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "selectionType": "pure_random", // pure_random, specified_range, specified_personnel
    "count": 3,
    "filter": {...}, // 筛选条件，用于specified_range
    "personnelIds": [1, 2, 3] // 指定的人员ID，用于specified_personnel
  }
  ```
- **响应**:
  ```json
  {
    "id": 1,
    "user": {
      "id": 1,
      "name": "超级管理员"
    },
    "selectionType": "pure_random",
    "criteria": "",
    "selectedCount": 3,
    "results": [
      {
        "personnel": {
          "id": 1,
          "name": "张三",
          "gender": "男",
          "age": 25,
          "department": "技术部",
          "position": "工程师",
          "phone": "13800138000"
        }
      }
    ]
  }
  ```

## 3. 专业类目接口

### 3.1 获取专业类目列表
- **URL**: `/api/admin/categories`
- **方法**: `GET`
- **响应**:
  ```json
  [
    {
      "id": 1,
      "name": "计算机科学",
      "description": "计算机相关专业"
    }
  ]
  ```

### 3.2 添加专业类目
- **URL**: `/api/admin/categories`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "name": "新类目",
    "description": "新类目描述"
  }
  ```
- **响应**:
  ```json
  {
    "id": 3,
    "name": "新类目",
    "description": "新类目描述"
  }
  ```

### 3.3 更新专业类目
- **URL**: `/api/admin/categories/{id}`
- **方法**: `PUT`
- **请求体**:
  ```json
  {
    "name": "更新的类目",
    "description": "更新的描述"
  }
  ```
- **响应**:
  ```json
  {
    "id": 3,
    "name": "更新的类目",
    "description": "更新的描述"
  }
  ```

### 3.4 删除专业类目
- **URL**: `/api/admin/categories/{id}`
- **方法**: `DELETE`
- **响应**: 200 OK

## 4. 用户管理接口

### 4.1 获取用户列表
- **URL**: `/api/admin/users`
- **方法**: `GET`
- **响应**:
  ```json
  [
    {
      "id": 1,
      "username": "admin",
      "name": "超级管理员",
      "email": "admin@example.com",
      "phone": "13800138000",
      "roles": [
        {
          "id": 1,
          "name": "ADMIN"
        }
      ]
    }
  ]
  ```

### 4.2 添加用户
- **URL**: `/api/admin/users`
- **方法**: `POST`
- **请求体**:
  ```json
  {
    "username": "newuser",
    "password": "123456",
    "name": "新用户",
    "email": "newuser@example.com",
    "phone": "13800138002",
    "roles": [
      {
        "name": "ENTRY"
      }
    ]
  }
  ```
- **响应**:
  ```json
  {
    "id": 3,
    "username": "newuser",
    "name": "新用户",
    "email": "newuser@example.com",
    "phone": "13800138002",
    "roles": [
      {
        "id": 2,
        "name": "ENTRY"
      }
    ]
  }
  ```

### 4.3 删除用户
- **URL**: `/api/admin/users/{id}`
- **方法**: `DELETE`
- **响应**: 200 OK

## 5. 抽取记录接口

### 5.1 获取抽取记录列表
- **URL**: `/api/admin/selection-records`
- **方法**: `GET`
- **响应**:
  ```json
  [
    {
      "id": 1,
      "user": {
        "id": 1,
        "name": "超级管理员"
      },
      "selectionType": "pure_random",
      "criteria": "",
      "selectedCount": 3,
      "createdAt": "2024-01-01T12:00:00"
    }
  ]
  ```
