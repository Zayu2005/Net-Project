<template>
  <div class="home-page">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <h1 class="main-title">网络数据传输与封装模拟系统</h1>
      <p class="subtitle">NETWORK SIMULATION SYSTEM</p>
      <div class="feature-tags">
        <t-tag theme="primary" size="large">Dijkstra</t-tag>
        <t-tag theme="success" size="large">OSPF</t-tag>
        <t-tag theme="warning" size="large">RIP</t-tag>
        <t-tag theme="danger" size="large">BGP</t-tag>
        <t-tag theme="default" size="large">OSI 7层</t-tag>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <t-icon name="server" size="32px" />
        </div>
        <div class="stat-value">{{ stats.totalNodes }}</div>
        <div class="stat-label">网络节点</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <t-icon name="link" size="32px" />
        </div>
        <div class="stat-value">{{ stats.totalLinks }}</div>
        <div class="stat-label">网络链路</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <t-icon name="swap" size="32px" />
        </div>
        <div class="stat-value">{{ stats.totalTransmissions }}</div>
        <div class="stat-label">传输记录</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <t-icon name="check-circle" size="32px" />
        </div>
        <div class="stat-value">{{ stats.successRate }}%</div>
        <div class="stat-label">成功率</div>
      </div>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions">
      <h2 class="section-title">快速操作</h2>
      <t-space direction="horizontal" :size="16">
        <t-button theme="primary" size="large" @click="goToSimulation">
          <t-icon name="play-circle" />
          开始模拟
        </t-button>
        <t-button theme="default" size="large" @click="goToTopology">
          <t-icon name="network" />
          拓扑管理
        </t-button>
        <t-button theme="default" size="large" @click="goToHistory">
          <t-icon name="history" />
          历史记录
        </t-button>
        <t-button theme="default" size="large" @click="goToStatistics">
          <t-icon name="chart-bar" />
          统计分析
        </t-button>
      </t-space>
    </div>

    <!-- 系统信息 -->
    <div class="system-info">
      <t-card :bordered="true">
        <template #header>
          <div class="card-title">系统信息</div>
        </template>
        <t-space direction="vertical" :size="12">
          <div class="info-item">
            <span class="info-label">路由算法:</span>
            <span class="info-value">Dijkstra, OSPF, RIP, BGP</span>
          </div>
          <div class="info-item">
            <span class="info-label">协议栈:</span>
            <span class="info-value">OSI 7层模型完整实现</span>
          </div>
          <div class="info-item">
            <span class="info-label">版本:</span>
            <span class="info-value">v1.0.0</span>
          </div>
          <div class="info-item">
            <span class="info-label">技术栈:</span>
            <span class="info-value">Spring Boot 3 + Vue 3 + TDesign</span>
          </div>
        </t-space>
      </t-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { nodeApi } from '@/api/node'
import { linkApi } from '@/api/link'

const router = useRouter()

const stats = ref({
  totalNodes: 0,
  totalLinks: 0,
  totalTransmissions: 0,
  successRate: 0
})

const loadStats = async () => {
  try {
    const [nodesRes, linksRes] = await Promise.all([
      nodeApi.getAllNodes(),
      linkApi.getAllLinks()
    ])

    stats.value.totalNodes = nodesRes.data?.length || 0
    stats.value.totalLinks = linksRes.data?.length || 0
    stats.value.totalTransmissions = 0
    stats.value.successRate = 100
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const goToSimulation = () => router.push('/simulation')
const goToTopology = () => router.push('/topology')
const goToHistory = () => router.push('/history')
const goToStatistics = () => router.push('/statistics')

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.home-page {
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  text-align: center;
  margin-bottom: 48px;
  padding: 60px 20px;
  background: #fff;
  border: 2px solid #000;
}

.main-title {
  font-size: 36px;
  font-weight: 700;
  color: #000;
  margin-bottom: 12px;
  letter-spacing: 2px;
}

.subtitle {
  font-size: 16px;
  color: #666;
  margin-bottom: 24px;
  letter-spacing: 4px;
}

.feature-tags {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  background: #fff;
  border: 2px solid #000;
  padding: 24px;
  text-align: center;
  transition: all 0.2s;
}

.stat-card:hover {
  border-color: #0052d9;
  box-shadow: 0 4px 12px rgba(0, 82, 217, 0.15);
}

.stat-icon {
  color: #0052d9;
  margin-bottom: 12px;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #000;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.quick-actions {
  background: #fff;
  border: 1px solid #ddd;
  padding: 32px;
  margin-bottom: 24px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #000;
}

.system-info {
  background: #fff;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  width: 120px;
  font-weight: 600;
  color: #333;
}

.info-value {
  color: #666;
  font-family: 'Courier New', monospace;
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
