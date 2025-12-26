# 网络数据传输与封装模拟系统 - 构建指南

## 项目概述

这是一个完整的全栈网络模拟系统，用于学习和演示网络通信、路由算法、协议栈封装等核心网络概念。

**技术栈:**
- **后端:** Spring Boot 3.2+ | MyBatis-Plus | MySQL 8.0+ | Redis 7+
- **前端:** Vue 3.4+ | Vite 5+ | Element Plus | ECharts
- **路由算法:** Dijkstra | OSPF | RIP | BGP
- **协议栈:** 完整OSI 7层实现

---

## 项目结构

```
Net-Code/
├── backend/                          # 后端Spring Boot 3项目
│   ├── src/main/java/com/network/simulation/
│   │   ├── NetworkSimulationApplication.java    # 启动类
│   │   ├── entity/                   # 实体类 ✅ 已创建3个示例
│   │   ├── mapper/                   # Mapper接口 ✅ 已创建3个示例
│   │   ├── service/                  # 业务层 ⚠️ 需完善
│   │   ├── controller/               # 控制层 ⚠️ 需完善
│   │   ├── algorithm/                # 路由算法 ✅ Dijkstra已完成
│   │   ├── protocol/                 # 协议栈 ⚠️ 需完善
│   │   ├── common/                   # 通用工具 ✅ 已完成
│   │   └── config/                   # 配置类 ⚠️ 需完善
│   ├── src/main/resources/
│   │   ├── db/
│   │   │   ├── schema.sql           # ✅ 建表脚本已完成
│   │   │   └── data.sql             # ✅ 初始数据已完成
│   │   ├── application.yml          # ✅ 配置文件已完成
│   │   └── logback-spring.xml       # ✅ 日志配置已完成
│   └── pom.xml                      # ✅ Maven配置已完成
│
├── frontend/                         # 前端Vue 3项目
│   ├── src/
│   │   ├── api/                     # ✅ API接口已完成
│   │   ├── views/                   # ✅ 基础页面已完成
│   │   ├── router/                  # ✅ 路由配置已完成
│   │   ├── utils/                   # ✅ 工具类已完成
│   │   ├── components/              # ⚠️ 需完善可视化组件
│   │   ├── store/                   # ⚠️ 需完善状态管理
│   │   ├── App.vue                  # ✅ 主组件已完成
│   │   └── main.js                  # ✅ 入口文件已完成
│   ├── package.json                 # ✅ 依赖配置已完成
│   ├── vite.config.js               # ✅ Vite配置已完成
│   └── index.html                   # ✅ HTML模板已完成
│
├── docs/                            # 项目文档
│   ├── api_documentation.md         # API接口文档
│   ├── database_design.md           # 数据库设计文档
│   ├── backend_springboot3_structure.md
│   └── frontend_vue3_structure.md
│
└── network_simulation.html          # 独立演示页面
```

---

## 快速开始

### 环境要求

**必需:**
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.9+ (可选)

**推荐:**
- Redis 7+ (缓存)
- Git

---

## 第一步：数据库初始化

### 1.1 创建数据库

```bash
# 登录MySQL (密码: 1234)
mysql -u root -p1234

# 执行建表脚本
mysql -u root -p1234 < backend/src/main/resources/db/schema.sql

# 插入初始数据
mysql -u root -p1234 < backend/src/main/resources/db/data.sql
```

### 1.2 验证数据库

```sql
USE network_simulation;

-- 查看所有表
SHOW TABLES;

-- 查看初始节点数据 (应该有6个节点: A, B, C, D, E, F)
SELECT * FROM network_node;

-- 查看初始链路数据 (应该有8条链路)
SELECT * FROM network_link;
```

**预期结果:**
- 7个表：network_node, network_link, routing_table, packet_transmission, packet_hop, protocol_layer, system_config
- 6个初始节点 (A-F)
- 8条初始链路

---

## 第二步：启动后端

### 2.1 方式一：使用Maven

```bash
# 进入后端目录
cd backend

# 清理并编译
mvn clean package -DskipTests

# 运行
java -jar target/simulation-backend-1.0.0.jar
```

### 2.2 方式二：使用IDE (推荐)

1. 用IntelliJ IDEA或Eclipse打开 `backend` 目录
2. 等待Maven依赖下载完成
3. 找到 `NetworkSimulationApplication.java`
4. 右键运行 `main` 方法

### 2.3 验证后端启动

访问以下地址：

- **应用首页:** http://localhost:8080
- **API文档:** http://localhost:8080/doc.html (Knife4j Swagger UI)

**预期日志输出:**
```
========================================
网络模拟系统启动成功!
API文档地址: http://localhost:8080/doc.html
========================================
```

---

## 第三步：启动前端

### 3.1 安装依赖

```bash
# 进入前端目录
cd frontend

# 安装依赖 (首次运行)
npm install
```

### 3.2 启动开发服务器

```bash
npm run dev
```

### 3.3 访问前端

浏览器访问: http://localhost:3000

**预期效果:**
- 看到精美的首页，包含4个功能模块入口
- 可以点击进入各个模块（目前是占位页面）

---

## 数据库配置说明

### 当前配置 (backend/src/main/resources/application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/network_simulation
    username: root
    password: 1234  # 已按您的要求配置
```

### 如需修改配置

编辑以下文件：
- `application.yml` (主配置)
- `application-dev.yml` (开发环境)
- `application-prod.yml` (生产环境)

---

## 项目状态总结

### ✅ 已完成

1. **数据库设计**
   - 7个表的完整SQL建表脚本
   - 初始数据（6节点 + 8链路 + 系统配置）

2. **后端核心框架**
   - Spring Boot 3.2 项目结构
   - Maven配置 (pom.xml)
   - 应用配置 (application.yml, logback)
   - 启动类 (NetworkSimulationApplication.java)
   - 统一返回结果 (Result, ResultCode)
   - 全局异常处理 (GlobalExceptionHandler)
   - 3个实体类示例 (NetworkNode, NetworkLink, PacketTransmission)
   - 3个Mapper接口示例
   - Dijkstra算法完整实现

3. **前端核心框架**
   - Vue 3 + Vite 5 项目结构
   - 依赖配置 (package.json)
   - 路由配置 (Vue Router 4)
   - Axios请求封装
   - 4个API模块 (node, link, routing, transmission)
   - 5个基础页面 (Home, Simulation, Topology, History, Statistics)
   - 全局样式

### ⚠️ 需要完善的部分

根据 `docs/` 目录下的完整文档，以下模块需要您继续实现：

#### 后端待完成

1. **Entity实体类** (剩余4个)
   - RoutingTable.java
   - PacketHop.java
   - ProtocolLayer.java
   - SystemConfig.java

2. **Mapper接口** (剩余4个)
   - RoutingTableMapper.java
   - PacketHopMapper.java
   - ProtocolLayerMapper.java
   - SystemConfigMapper.java

3. **Service业务层** (8个接口 + 8个实现类)
   - 参考 `docs/backend_springboot3_structure.md`

4. **Controller控制层** (8个控制器)
   - NodeController.java
   - LinkController.java
   - RoutingController.java
   - TransmissionController.java
   - EncapsulationController.java
   - TopologyController.java
   - StatisticsController.java
   - ConfigController.java
   - 参考 `docs/api_documentation.md` (40+个API接口)

5. **路由算法** (剩余3个)
   - OSPFAlgorithm.java
   - RIPAlgorithm.java
   - BGPAlgorithm.java

6. **协议栈处理**
   - ApplicationLayer.java
   - TransportLayer.java
   - NetworkLayer.java
   - DataLinkLayer.java
   - PhysicalLayer.java
   - ProtocolStackProcessor.java

7. **配置类**
   - MybatisPlusConfig.java
   - RedisConfig.java
   - WebSocketConfig.java
   - CorsConfig.java
   - Knife4jConfig.java

8. **WebSocket实时推送**
   - TransmissionWebSocketHandler.java
   - WebSocketMessageService.java

#### 前端待完成

1. **网络拓扑可视化组件**
   - NetworkTopology.vue (使用Canvas/SVG)
   - 节点拖拽、链路绘制

2. **协议栈展示组件**
   - ProtocolStack.vue
   - 逐层封装动画

3. **路由可视化组件**
   - RouteVisualizer.vue
   - Dijkstra算法动画演示

4. **传输日志组件**
   - TransmissionLog.vue
   - 实时WebSocket推送

5. **统计图表组件**
   - 使用ECharts实现各种统计图表

6. **状态管理**
   - Pinia Store模块 (topology, transmission, routing)

---

## 如何完成剩余开发

### 推荐步骤

1. **先完成后端API**
   - 按照 `docs/api_documentation.md` 实现所有Controller
   - 参考 `docs/backend_springboot3_structure.md` 的代码示例
   - 实现Service业务逻辑

2. **测试API**
   - 使用Knife4j Swagger UI (http://localhost:8080/doc.html) 测试

3. **完成前端页面**
   - 参考 `docs/frontend_vue3_structure.md`
   - 实现可视化组件

4. **集成测试**
   - 前后端联调
   - 完善错误处理

### 参考文档

所有详细的代码示例、API定义、数据库设计都在 `docs/` 目录下：

- `api_documentation.md` - 完整的40+个API接口定义
- `database_design.md` - 7个表的详细设计和SQL脚本
- `backend_springboot3_structure.md` - 后端代码示例（包含完整Dijkstra实现）
- `frontend_vue3_structure.md` - 前端代码示例和组件设计

---

## 常见问题

### Q1: 数据库连接失败

**检查:**
1. MySQL是否已启动
2. 数据库名、用户名、密码是否正确 (root/1234)
3. 端口是否为3306

### Q2: Maven依赖下载慢

**解决:**
配置Maven镜像源为阿里云：
```xml
<!-- settings.xml -->
<mirror>
  <id>aliyun</id>
  <mirrorOf>central</mirrorOf>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

### Q3: 前端npm安装慢

**解决:**
```bash
# 使用淘宝镜像
npm config set registry https://registry.npmmirror.com
npm install
```

### Q4: 端口冲突

- 后端默认端口: 8080 (可在application.yml修改)
- 前端默认端口: 3000 (可在vite.config.js修改)

---

## 技术亮点

### 路由算法
- **Dijkstra:** 最短路径算法 (已完成)
- **OSPF:** 开放式最短路径优先
- **RIP:** 路由信息协议
- **BGP:** 边界网关协议

### 协议栈模拟
- 完整的OSI 7层实现
- 支持TCP, UDP, ICMP, HTTP
- 逐层封装/解封装可视化

### 实时通信
- WebSocket实时推送传输状态
- TTL递减、延迟累加动画

---

## 下一步计划

1. **完成剩余Entity和Mapper**
2. **实现所有Controller和Service**
3. **实现OSPF、RIP、BGP算法**
4. **实现协议栈处理模块**
5. **完成前端可视化组件**
6. **实现WebSocket实时推送**
7. **集成测试和优化**

---

## 联系方式

如有问题，请参考 `docs/` 目录下的文档，或提交Issue到GitHub仓库。

## License

MIT License

---

**祝您构建顺利！ 🚀**
