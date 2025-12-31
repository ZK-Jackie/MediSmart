# 🏥 MediSmart - 智能医疗知识图谱系统

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-green.svg)](https://vuejs.org/)
[![Python](https://img.shields.io/badge/Python-3.11-blue.svg)](https://www.python.org/)
[![Neo4j](https://img.shields.io/badge/Neo4j-5.26.5-008CC1.svg)](https://neo4j.com/)
[![License](https://img.shields.io/badge/license-MIT-orange.svg)](LICENSE)

**一个基于知识图谱和大语言模型的智能医疗问答系统**

[功能特性](#-功能特性) • [快速开始](#-快速开始) • [项目结构](#-项目结构)

</div>

---

## 📖 项目简介

MediSmart 是一个集成了**知识图谱**、**大语言模型**和**智能问答**的医疗健康管理系统。系统通过Neo4j图数据库存储医疗知识，结合LangChain和OpenAI GPT模型，为用户提供专业、准确的医疗健康咨询服务。

### 核心优势

- 🧠 **知识图谱驱动** - 基于Neo4j的医疗知识图谱，涵盖疾病、症状、药物、检查等多维度医疗实体
- 💬 **智能对话系统** - 集成大语言模型，支持流式问答和上下文理解
- 🔐 **安全可靠** - 完善的用户认证、权限管理和数据安全机制
- 🎨 **现代化UI** - Vue3前端框架，响应式设计，用户体验优秀
- 📊 **可视化展示** - 医疗知识图谱的交互式可视化
- 🧪 **完善测试** - 108+单元测试，85%+代码覆盖率

---

## 🎯 功能特性

### 1️⃣ 智能问答系统
- ✅ AI医疗助手，支持自然语言问答
- ✅ 流式(SSE)响应，实时获取回答
- ✅ 多轮对话，上下文记忆
- ✅ 会话管理（创建、删除、标题修改）

### 2️⃣ 知识图谱管理
- ✅ 医疗实体管理（疾病、症状、药物等）
- ✅ 关系图谱可视化
- ✅ 模糊/精准搜索
- ✅ 批量导入/导出
- ✅ 节点CRUD操作

### 3️⃣ 用户系统
- ✅ 用户注册/登录/登出
- ✅ JWT令牌认证
- ✅ 验证码验证
- ✅ 密码加密存储
- ✅ 角色权限管理

### 4️⃣ 数据管理
- ✅ 实时数据同步
- ✅ 缓存机制（Redis）
- ✅ 数据备份与恢复

---

## 🛠️ 技术栈

### 后端技术
- **核心框架**: Spring Boot 2.7.6
- **安全框架**: Spring Security + JWT
- **持久层**: MyBatis Plus 3.5.3
- **数据库**: 
  - MySQL 8.x (业务数据)
  - Neo4j 5.26.5 (知识图谱)
  - Redis (缓存)
- **构建工具**: Maven 3.x
- **Java版本**: JDK 1.8

### 前端技术
- **框架**: Vue.js 2.6
- **状态管理**: Vuex
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **UI框架**: Element
- **图谱可视化**: ECharts
- **构建工具**: Webpack

### AI服务
- **框架**: FastAPI
- **AI框架**: LangChain 0.3.x   LangGraph 0.3.x
- **聊天记录存储**: LangChain-Redis
- **图数据库集成**: LangChain-Neo4j
- **模型**: OpenAI GPT / 兼容API
- **Python版本**: 3.11

### DevOps
- **容器化**: Docker + Docker Compose
- **Web服务器**: Nginx
- **监控**: Spring Boot Actuator

---

## 🚀 快速开始

### 环境要求

- Java JDK 1.8+
- Node.js >=14, <=21
- Python 3.11
- Maven 3.6+
- MySQL 8.0+
- Neo4j 5.x
- Redis stack server 6.x, 7.x, Redis 8.x
- Docker & Docker Compose (可选)

### 使用Docker Compose部署（推荐）

1. **克隆项目**
```bash
git clone https://github.com/your-org/medismart.git
cd medismart
```

2. **配置环境变量**
```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量
nano .env
```

3. **自行下载必备的文件**
- neo4j 插件
- neo4j 数据库备份文件

4. **启动所有服务**
```bash
docker-compose up -d
```

5. **访问应用**
- 前端应用: http://localhost:8080
- 后端API: http://localhost:8081
- Neo4j浏览器: http://localhost:7474


### 本地开发部署

#### 1. 数据库准备

**MySQL数据库**
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE medismart DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构
mysql -u root -p medismart < sql/sql.sql
```

**Neo4j图数据库**
```bash
# 启动Neo4j
neo4j start

# 访问 http://localhost:7474 初始化
# 默认用户名/密码: neo4j/password

# 导入数据
neo4j-admin database load --overwrite-destination=true --from-path=neo4j/dumps neo4j
```

**Redis**
```bash
# 启动Redis
redis-server
```

#### 2. 后端服务

```bash
# 修改配置文件
nano src/main/resources/application-dev.yml

# 编译打包
mvn clean package -DskipTests

# 启动服务
java -jar target/medismart-0.0.1-SNAPSHOT.jar

# 或使用IDE直接运行 MediSmartServerApplication
```

#### 3. AI服务

```bash
cd medismart-ai

# 创建虚拟环境
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# 安装依赖
pip install -e .

# 配置环境变量
cp .env.example .env
nano .env

# 启动服务
python -m app --host
```

#### 4. 前端应用

```bash
cd medismart-ui

# 安装依赖
npm install

# 开发模式
npm run serve

# 生产构建
npm run build
```

---

## 📁 项目结构

```
MediSmart/
├── src/                          # 后端源码
│   ├── main/
│   │   ├── java/org/superdata/medismart/
│   │   │   ├── annotation/       # 自定义注解
│   │   │   ├── common/          # 公共类（常量、异常、响应）
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器层
│   │   │   ├── entity/          # 实体类
│   │   │   │   ├── node/        # 图数据库节点实体
│   │   │   │   └── request/     # 请求DTO
│   │   │   ├── mapper/          # 数据访问层
│   │   │   │   ├── neo4j/       # Neo4j仓储
│   │   │   │   └── sql/         # MySQL映射器
│   │   │   ├── security/        # 安全框架
│   │   │   ├── service/         # 业务逻辑层
│   │   │   │   └── impl/        # 业务实现
│   │   │   └── utils/           # 工具类
│   │   └── resources/
│   │       ├── mapper/          # MyBatis XML
│   │       └── application.yml  # 配置文件
│   └── test/                    # 测试代码（108+测试）
│       ├── java/
│       │   └── org/superdata/medismart/
│       │       ├── base/        # 测试基类
│       │       ├── controller/  # 控制器测试
│       │       ├── service/     # 服务测试
│       │       └── utils/       # 工具测试
│       └── resources/
│           └── application-test.yml
├── medismart-ui/                # 前端应用
│   ├── public/                  # 静态资源
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── assets/             # 资源文件
│   │   ├── components/         # 组件
│   │   ├── layout/             # 布局
│   │   ├── plugins/            # 插件
│   │   ├── router/             # 路由
│   │   ├── store/              # 状态管理
│   │   ├── utils/              # 工具
│   │   ├── views/              # 视图
│   │   ├── App.vue             # 根组件
│   │   └── main.js             # 入口文件
│   └── room-client/            # 远程诊疗客户端
├── medismart-ai/                # AI服务
│   ├── app/
│   │   ├── api/                # API路由
│   │   │   └── v1/             # v1版本API
│   │   ├── core/               # 核心配置
│   │   ├── services/           # 服务层
│   │   │   └── ai/             # AI服务
│   │   └── tools/              # 工具函数
│   ├── pyproject.toml          # Python项目配置
│   └── __main__.py             # 入口文件
├── neo4j/                       # Neo4j数据
│   ├── data/                   # 数据目录
│   ├── dumps/                  # 数据库备份
│   └── plugins/                # 插件
├── neo4j-medismart/            # Neo4j安装包
├── nginx/                       # Nginx配置
│   └── conf/
│       └── medismart.conf      # 站点配置
├── sql/                         # SQL脚本
│   └── sql.sql                 # 数据库初始化
├── docker-compose.yaml          # Docker编排文件
├── pom.xml                      # Maven配置
├── README.md                    # 项目文档
└── TEST_SUMMARY.md             # 测试总结
```

---

## 🔧 配置说明

### 后端配置 (application.yml)

```yaml
server:
  port: 8081

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/medismart?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    
  redis:
    host: localhost
    port: 6379
    database: 0
    
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: neo4j

# JWT配置
jwt:
  secret: your-secret-key
  expiration: 216000000  # 60小时

# AI服务配置
chat:
  stream:
    url: http://localhost:8000/chat/stream
  history:
    url: http://localhost:8000/chat/history
```

### AI服务配置

参照 `medismart-ai/.env.example` 文件，创建 `.env` 文件并修改对应参数


### 前端配置 (vue.config.js)

```javascript
module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
```

---

## ❓ 常见问题

### Q: 如何重置Neo4j数据库？
```bash
# 停止Neo4j
neo4j stop

# 删除数据
rm -rf data/databases/neo4j
rm -rf data/transactions/neo4j

# 重新导入
neo4j-admin database load --overwrite-destination=true --from-path=neo4j/dumps neo4j
```

### Q: 如何修改端口？
修改对应配置文件中的端口号：
- 后端: `application.yml` -> `server.port`
- 前端: `vue.config.js` -> `devServer.port`
- AI服务: 启动命令 `--port`参数

### Q: 如何更换大模型API？
修改 `medismart-ai/.env` 文件中的：
```env
OPENAI_API_KEY=your-key
OPENAI_BASE_URL=https://your-api-endpoint
```

---

## 📜 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

---