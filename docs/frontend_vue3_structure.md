# Vue3 前端项目结构

## 项目技术栈

- **框架**: Vue 3.4+
- **构建工具**: Vite 5+
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **UI组件库**: Element Plus
- **网络请求**: Axios
- **可视化**: ECharts 5 / D3.js
- **CSS预处理器**: SCSS
- **代码规范**: ESLint + Prettier

---

## 项目目录结构

```
network-simulation-frontend/
├── public/                          # 静态资源
│   ├── favicon.ico
│   └── logo.png
├── src/
│   ├── api/                         # API接口
│   │   ├── index.js                 # API统一导出
│   │   ├── node.js                  # 节点相关API
│   │   ├── link.js                  # 链路相关API
│   │   ├── routing.js               # 路由算法API
│   │   ├── transmission.js          # 传输相关API
│   │   └── encapsulation.js         # 封装相关API
│   ├── assets/                      # 资源文件
│   │   ├── images/                  # 图片
│   │   ├── icons/                   # 图标
│   │   └── styles/                  # 全局样式
│   │       ├── index.scss           # 样式入口
│   │       ├── variables.scss       # 变量
│   │       └── mixins.scss          # 混合
│   ├── components/                  # 通用组件
│   │   ├── NetworkTopology/         # 网络拓扑组件
│   │   │   ├── index.vue
│   │   │   ├── TopologyCanvas.vue   # Canvas拓扑图
│   │   │   └── TopologyControls.vue # 拓扑控制面板
│   │   ├── ProtocolStack/           # 协议栈组件
│   │   │   ├── index.vue
│   │   │   ├── LayerCard.vue        # 单层卡片
│   │   │   └── EncapsulationFlow.vue # 封装流程动画
│   │   ├── RouteVisualizer/         # 路由可视化组件
│   │   │   ├── index.vue
│   │   │   ├── PathAnimation.vue    # 路径动画
│   │   │   └── HopDetails.vue       # 跳转详情
│   │   ├── TransmissionLog/         # 传输日志组件
│   │   │   └── index.vue
│   │   ├── StatisticsPanel/         # 统计面板组件
│   │   │   └── index.vue
│   │   └── Common/                  # 公共组件
│   │       ├── Loading.vue
│   │       ├── Empty.vue
│   │       └── NodeIcon.vue
│   ├── composables/                 # 组合式函数
│   │   ├── useWebSocket.js          # WebSocket hook
│   │   ├── useAnimation.js          # 动画hook
│   │   ├── useRouting.js            # 路由算法hook
│   │   └── useEncapsulation.js      # 封装处理hook
│   ├── layout/                      # 布局组件
│   │   ├── MainLayout.vue           # 主布局
│   │   ├── Header.vue               # 头部
│   │   └── Sidebar.vue              # 侧边栏
│   ├── router/                      # 路由配置
│   │   └── index.js
│   ├── store/                       # Pinia状态管理
│   │   ├── index.js                 # Store入口
│   │   ├── modules/
│   │   │   ├── topology.js          # 拓扑状态
│   │   │   ├── transmission.js      # 传输状态
│   │   │   ├── routing.js           # 路由状态
│   │   │   └── encapsulation.js     # 封装状态
│   ├── utils/                       # 工具函数
│   │   ├── request.js               # Axios封装
│   │   ├── algorithms/              # 算法实现
│   │   │   ├── dijkstra.js
│   │   │   ├── ospf.js
│   │   │   └── rip.js
│   │   ├── encapsulation.js         # 封装工具
│   │   ├── validation.js            # 验证工具
│   │   └── constants.js             # 常量定义
│   ├── views/                       # 页面视图
│   │   ├── Home/                    # 首页
│   │   │   └── index.vue
│   │   ├── Simulation/              # 模拟主页面
│   │   │   └── index.vue
│   │   ├── TopologyManage/          # 拓扑管理
│   │   │   └── index.vue
│   │   ├── History/                 # 历史记录
│   │   │   └── index.vue
│   │   ├── Statistics/              # 统计分析
│   │   │   └── index.vue
│   │   └── Settings/                # 系统设置
│   │       └── index.vue
│   ├── App.vue                      # 根组件
│   └── main.js                      # 入口文件
├── .env.development                 # 开发环境配置
├── .env.production                  # 生产环境配置
├── .eslintrc.js                     # ESLint配置
├── .prettierrc.js                   # Prettier配置
├── index.html                       # HTML模板
├── package.json                     # 依赖配置
├── vite.config.js                   # Vite配置
└── README.md                        # 项目说明
```

---

## 核心文件说明

### 1. package.json
```json
{
  "name": "network-simulation-frontend",
  "version": "1.0.0",
  "description": "网络数据传输与封装模拟系统前端",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext .vue,.js,.jsx,.cjs,.mjs --fix --ignore-path .gitignore"
  },
  "dependencies": {
    "vue": "^3.4.0",
    "vue-router": "^4.2.5",
    "pinia": "^2.1.7",
    "axios": "^1.6.0",
    "element-plus": "^2.5.0",
    "@element-plus/icons-vue": "^2.3.0",
    "echarts": "^5.4.3",
    "d3": "^7.8.5",
    "lodash-es": "^4.17.21",
    "dayjs": "^1.11.10"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "vite": "^5.0.0",
    "sass": "^1.69.0",
    "eslint": "^8.55.0",
    "eslint-plugin-vue": "^9.19.0",
    "prettier": "^3.1.0"
  }
}
```

### 2. vite.config.js
```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false
  }
})
```

### 3. .env.development
```env
VITE_APP_TITLE=网络模拟系统
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_WS_BASE_URL=ws://localhost:8080/ws
```

### 4. .env.production
```env
VITE_APP_TITLE=网络模拟系统
VITE_API_BASE_URL=https://your-domain.com/api/v1
VITE_WS_BASE_URL=wss://your-domain.com/ws
```

---

## 安装与运行

### 安装依赖
```bash
npm install
# 或
yarn install
# 或
pnpm install
```

### 开发模式
```bash
npm run dev
```
访问: http://localhost:3000

### 生产构建
```bash
npm run build
```

### 预览构建结果
```bash
npm run preview
```

---

## 关键功能模块说明

### 1. 网络拓扑可视化
- 使用Canvas或SVG绘制网络拓扑图
- 支持节点拖拽、缩放
- 实时显示路径高亮
- 节点和链路的动态增删改

### 2. 路由算法可视化
- Dijkstra算法动画演示
- OSPF链路状态更新可视化
- RIP路由表更新动画
- 路径计算过程展示

### 3. 协议栈封装动画
- 自顶向下逐层封装动画
- 每层头部信息实时显示
- 数据包大小变化展示
- 封装/解封装对比

### 4. 传输过程实时追踪
- WebSocket实时推送
- 逐跳传输动画
- TTL递减显示
- 延迟累加展示

### 5. 数据统计与分析
- ECharts图表展示
- 传输成功率统计
- 协议分布饼图
- 节点流量柱状图

---

## 组件开发规范

### 组件命名
- 使用PascalCase命名
- 组件文件名与组件名一致
- 多单词组件名避免与HTML元素冲突

### 组件结构
```vue
<template>
  <!-- 模板 -->
</template>

<script setup>
// 组合式API
import { ref, computed, onMounted } from 'vue'

// Props定义
const props = defineProps({
  // ...
})

// Emits定义
const emit = defineEmits(['update', 'change'])

// 响应式数据
const data = ref(null)

// 计算属性
const computedValue = computed(() => {
  // ...
})

// 生命周期
onMounted(() => {
  // ...
})
</script>

<style scoped lang="scss">
/* 样式 */
</style>
```

### 代码风格
- 使用ESLint + Prettier
- 2空格缩进
- 单引号
- 组件Props使用驼峰命名
- 事件使用kebab-case

---

## 状态管理示例

### store/modules/topology.js
```javascript
import { defineStore } from 'pinia'
import { getNodes, getLinks } from '@/api/node'

export const useTopologyStore = defineStore('topology', {
  state: () => ({
    nodes: [],
    links: [],
    selectedNode: null,
    selectedPath: []
  }),

  getters: {
    activeNodes: (state) => state.nodes.filter(n => n.status === 1),
    activeLinks: (state) => state.links.filter(l => l.status === 1)
  },

  actions: {
    async fetchTopology() {
      const [nodesRes, linksRes] = await Promise.all([
        getNodes(),
        getLinks()
      ])
      this.nodes = nodesRes.data
      this.links = linksRes.data
    },

    setSelectedPath(path) {
      this.selectedPath = path
    }
  }
})
```

---

## 路由配置示例

### router/index.js
```javascript
import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layout/MainLayout.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    redirect: '/simulation',
    children: [
      {
        path: 'simulation',
        name: 'Simulation',
        component: () => import('@/views/Simulation/index.vue'),
        meta: { title: '网络模拟' }
      },
      {
        path: 'topology',
        name: 'Topology',
        component: () => import('@/views/TopologyManage/index.vue'),
        meta: { title: '拓扑管理' }
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/History/index.vue'),
        meta: { title: '历史记录' }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/Statistics/index.vue'),
        meta: { title: '统计分析' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings/index.vue'),
        meta: { title: '系统设置' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
```

---

## API封装示例

### utils/request.js
```javascript
import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 可以在这里添加token
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default service
```

### api/transmission.js
```javascript
import request from '@/utils/request'

export function startTransmission(data) {
  return request({
    url: '/transmission/start',
    method: 'post',
    data
  })
}

export function getTransmissionStatus(transmissionId) {
  return request({
    url: `/transmission/${transmissionId}`,
    method: 'get'
  })
}

export function getTransmissionHops(transmissionId) {
  return request({
    url: `/transmission/${transmissionId}/hops`,
    method: 'get'
  })
}
```

---

## WebSocket使用示例

### composables/useWebSocket.js
```javascript
import { ref, onUnmounted } from 'vue'

export function useWebSocket(url) {
  const ws = ref(null)
  const messages = ref([])
  const isConnected = ref(false)

  const connect = () => {
    ws.value = new WebSocket(url)

    ws.value.onopen = () => {
      isConnected.value = true
      console.log('WebSocket connected')
    }

    ws.value.onmessage = (event) => {
      const data = JSON.parse(event.data)
      messages.value.push(data)
    }

    ws.value.onerror = (error) => {
      console.error('WebSocket error:', error)
    }

    ws.value.onclose = () => {
      isConnected.value = false
      console.log('WebSocket disconnected')
    }
  }

  const disconnect = () => {
    if (ws.value) {
      ws.value.close()
    }
  }

  const send = (data) => {
    if (ws.value && isConnected.value) {
      ws.value.send(JSON.stringify(data))
    }
  }

  onUnmounted(() => {
    disconnect()
  })

  return {
    connect,
    disconnect,
    send,
    messages,
    isConnected
  }
}
```

---

## 部署说明

### Nginx配置示例
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        root /var/www/network-simulation;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /ws {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```
