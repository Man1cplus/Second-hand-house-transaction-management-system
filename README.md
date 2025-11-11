# Second-hand-house-transaction-management-system

## 认证系统说明

本项目已升级为使用JWT（JSON Web Token）认证，密码使用BCrypt哈希加密存储。

### 主要改动

1. **JWT认证**：替代了原有的Session认证机制
2. **密码加密**：使用BCrypt算法对密码进行哈希加密存储
3. **无状态认证**：JWT token包含用户信息，无需服务器端存储Session

### 使用方式

#### 1. 用户登录

**请求示例：**
```http
POST /users/login
Content-Type: application/json

{
  "username": "your_username",
  "password": "your_password"
}
```

**响应示例：**
```json
{
  "success": true,
  "errorMsg": null,
  "data": {
    "user": {
      "userid": 1,
      "username": "your_username",
      "email": "user@example.com",
      ...
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

#### 2. 使用Token访问受保护的接口

在后续的API请求中，需要在请求头中携带JWT token：

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**示例：**
```http
GET /users/favorites?page=1&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 3. 用户注册

注册时密码会自动使用BCrypt加密存储：

```http
POST /users/register
Content-Type: application/json

{
  "username": "new_user",
  "password": "password123",
  "email": "newuser@example.com",
  "phonenumber": "13800138000"
}
```

### 配置说明

JWT配置在 `application.yaml` 中：

```yaml
jwt:
  secret: SecondHandHouseSecretKeyForJWTTokenGeneration2024  # JWT密钥，生产环境请修改
  expiration: 86400000  # Token过期时间（毫秒），默认24小时
```

**重要提示：**
- 生产环境请修改 `jwt.secret` 为更安全的随机字符串
- Token过期时间可根据需求调整

### 受保护的接口

以下接口需要JWT认证（需要在请求头中携带token）：
- `/users/profile/username` - 修改用户信息
- `/users/favorites/**` - 用户收藏相关
- `/contracts/purchase` - 购买房源
- `/viewingappointments/apply` - 申请预约看房
- 其他需要登录的接口

### 不需要认证的接口

- `/users/login` - 用户登录
- `/users/register` - 用户注册

### 密码安全

- 所有密码使用BCrypt算法加密存储
- 密码验证使用BCrypt的 `matches` 方法，不会明文比较
- 即使数据库泄露，攻击者也无法直接获取原始密码

### 注意事项

1. **Token过期**：Token默认24小时过期，过期后需要重新登录
2. **Token安全**：请妥善保管token，不要泄露给他人
3. **HTTPS**：生产环境建议使用HTTPS传输，保护token安全
4. **现有用户**：如果数据库中有旧用户（明文密码），需要重置密码或迁移密码
