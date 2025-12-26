<template>
  <div class="statistics-page">
    <!-- 性能指标卡片 -->
    <div class="metrics-grid">
      <div class="metric-card">
        <div class="metric-header">
          <t-icon name="server" size="24px" />
          <span class="metric-title">总传输次数</span>
        </div>
        <div class="metric-value">{{ metrics.totalTransmissions }}</div>
        <div class="metric-footer">
          <span class="metric-label">今日: {{ metrics.todayTransmissions }}</span>
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-header">
          <t-icon name="check-circle" size="24px" />
          <span class="metric-title">成功率</span>
        </div>
        <div class="metric-value">{{ metrics.successRate }}%</div>
        <div class="metric-footer">
          <span class="metric-label">成功: {{ metrics.successCount }} / 失败: {{ metrics.failCount }}</span>
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-header">
          <t-icon name="time" size="24px" />
          <span class="metric-title">平均延迟</span>
        </div>
        <div class="metric-value">{{ metrics.avgDelay }} ms</div>
        <div class="metric-footer">
          <span class="metric-label">最小: {{ metrics.minDelay }} ms / 最大: {{ metrics.maxDelay }} ms</span>
        </div>
      </div>

      <div class="metric-card">
        <div class="metric-header">
          <t-icon name="swap" size="24px" />
          <span class="metric-title">平均跳数</span>
        </div>
        <div class="metric-value">{{ metrics.avgHopCount }}</div>
        <div class="metric-footer">
          <span class="metric-label">最小: {{ metrics.minHopCount }} / 最大: {{ metrics.maxHopCount }}</span>
        </div>
      </div>
    </div>

    <!-- 图表容器 -->
    <div class="charts-grid">
      <!-- 算法使用统计 -->
      <div class="chart-card">
        <div class="card-header">
          <h3 class="card-title">算法使用统计</h3>
        </div>
        <div ref="algorithmChartRef" class="chart-container"></div>
      </div>

      <!-- 传输趋势 -->
      <div class="chart-card">
        <div class="card-header">
          <h3 class="card-title">传输趋势</h3>
        </div>
        <div ref="trendChartRef" class="chart-container"></div>
      </div>

      <!-- 算法性能对比 -->
      <div class="chart-card full-width">
        <div class="card-header">
          <h3 class="card-title">算法性能对比</h3>
        </div>
        <div ref="performanceChartRef" class="chart-container"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { MessagePlugin } from 'tdesign-vue-next'
import * as echarts from 'echarts'
import { transmissionApi } from '@/api/transmission'

const metrics = ref({
  totalTransmissions: 0,
  todayTransmissions: 0,
  successRate: 0,
  successCount: 0,
  failCount: 0,
  avgDelay: 0,
  minDelay: 0,
  maxDelay: 0,
  avgHopCount: 0,
  minHopCount: 0,
  maxHopCount: 0
})

const algorithmChartRef = ref(null)
const trendChartRef = ref(null)
const performanceChartRef = ref(null)

let algorithmChart = null
let trendChart = null
let performanceChart = null

// 加载统计数据
const loadStatistics = async () => {
  try {
    const res = await transmissionApi.getHistory()
    const data = res.data || []

    // 计算指标
    metrics.value.totalTransmissions = data.length
    metrics.value.successCount = data.filter(item => item.status === 'SUCCESS').length
    metrics.value.failCount = data.length - metrics.value.successCount
    metrics.value.successRate = data.length > 0
      ? ((metrics.value.successCount / data.length) * 100).toFixed(2)
      : 0

    // 延迟统计
    const delays = data.map(item => item.totalDelay || 0).filter(d => d > 0)
    if (delays.length > 0) {
      metrics.value.avgDelay = (delays.reduce((a, b) => a + b, 0) / delays.length).toFixed(2)
      metrics.value.minDelay = Math.min(...delays)
      metrics.value.maxDelay = Math.max(...delays)
    }

    // 跳数统计
    const hopCounts = data.map(item => item.hopCount || 0).filter(h => h > 0)
    if (hopCounts.length > 0) {
      metrics.value.avgHopCount = (hopCounts.reduce((a, b) => a + b, 0) / hopCounts.length).toFixed(2)
      metrics.value.minHopCount = Math.min(...hopCounts)
      metrics.value.maxHopCount = Math.max(...hopCounts)
    }

    // 初始化图表
    initAlgorithmChart(data)
    initTrendChart(data)
    initPerformanceChart(data)
  } catch (error) {
    MessagePlugin.error('加载统计数据失败')
  }
}

// 算法使用统计柱状图
const initAlgorithmChart = (data) => {
  if (!algorithmChartRef.value) return

  const algorithmCount = {
    DIJKSTRA: 0,
    OSPF: 0,
    RIP: 0,
    BGP: 0
  }

  data.forEach(item => {
    if (algorithmCount[item.routingAlgorithm] !== undefined) {
      algorithmCount[item.routingAlgorithm]++
    }
  })

  algorithmChart = echarts.init(algorithmChartRef.value)
  const option = {
    color: ['#0052d9'],
    grid: {
      left: '10%',
      right: '10%',
      bottom: '15%',
      top: '10%'
    },
    xAxis: {
      type: 'category',
      data: ['Dijkstra', 'OSPF', 'RIP', 'BGP'],
      axisLine: { lineStyle: { color: '#000' } },
      axisLabel: { color: '#333' }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: '#000' } },
      axisLabel: { color: '#333' },
      splitLine: { lineStyle: { type: 'dashed' } }
    },
    series: [{
      type: 'bar',
      data: [
        algorithmCount.DIJKSTRA,
        algorithmCount.OSPF,
        algorithmCount.RIP,
        algorithmCount.BGP
      ],
      itemStyle: {
        borderRadius: 0
      },
      label: {
        show: true,
        position: 'top',
        color: '#333',
        fontWeight: 'bold'
      }
    }]
  }
  algorithmChart.setOption(option)
}

// 传输趋势折线图
const initTrendChart = (data) => {
  if (!trendChartRef.value) return

  // 按日期统计
  const dateMap = new Map()
  data.forEach(item => {
    const date = item.timestamp?.split(' ')[0] || ''
    if (date) {
      dateMap.set(date, (dateMap.get(date) || 0) + 1)
    }
  })

  const dates = Array.from(dateMap.keys()).sort().slice(-7)
  const counts = dates.map(date => dateMap.get(date) || 0)

  trendChart = echarts.init(trendChartRef.value)
  const option = {
    color: ['#0052d9'],
    grid: {
      left: '10%',
      right: '10%',
      bottom: '15%',
      top: '10%'
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { lineStyle: { color: '#000' } },
      axisLabel: { color: '#333', rotate: 45 }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: '#000' } },
      axisLabel: { color: '#333' },
      splitLine: { lineStyle: { type: 'dashed' } }
    },
    series: [{
      type: 'line',
      data: counts,
      smooth: false,
      lineStyle: { width: 3 },
      itemStyle: {
        borderRadius: 0,
        borderWidth: 2,
        borderColor: '#fff'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(0, 82, 217, 0.3)' },
          { offset: 1, color: 'rgba(0, 82, 217, 0.05)' }
        ])
      }
    }]
  }
  trendChart.setOption(option)
}

// 算法性能对比雷达图
const initPerformanceChart = (data) => {
  if (!performanceChartRef.value) return

  const algorithms = ['DIJKSTRA', 'OSPF', 'RIP', 'BGP']
  const performanceData = algorithms.map(alg => {
    const algData = data.filter(item => item.routingAlgorithm === alg)
    if (algData.length === 0) return { value: [0, 0, 0, 0, 0], name: alg }

    const successRate = (algData.filter(item => item.status === 'SUCCESS').length / algData.length) * 100
    const avgDelay = algData.reduce((sum, item) => sum + (item.totalDelay || 0), 0) / algData.length
    const avgHops = algData.reduce((sum, item) => sum + (item.hopCount || 0), 0) / algData.length
    const usage = algData.length

    return {
      value: [
        successRate.toFixed(2),
        100 - avgDelay, // 反转延迟使得越小越好
        100 - avgHops * 10, // 反转跳数使得越小越好
        usage,
        algData.length
      ],
      name: alg
    }
  })

  performanceChart = echarts.init(performanceChartRef.value)
  const option = {
    color: ['#0052d9', '#00a870', '#ed7b2f', '#e34d59'],
    legend: {
      data: algorithms,
      bottom: 10
    },
    radar: {
      indicator: [
        { name: '成功率', max: 100 },
        { name: '低延迟', max: 100 },
        { name: '少跳数', max: 100 },
        { name: '使用频率', max: Math.max(...performanceData.map(d => d.value[3])) || 10 },
        { name: '稳定性', max: 100 }
      ],
      splitArea: {
        areaStyle: {
          color: ['rgba(0, 82, 217, 0.05)', 'rgba(0, 82, 217, 0.1)']
        }
      },
      axisLine: {
        lineStyle: { color: '#000' }
      },
      splitLine: {
        lineStyle: { color: '#ddd' }
      }
    },
    series: [{
      type: 'radar',
      data: performanceData.map((item, index) => ({
        ...item,
        name: algorithms[index],
        lineStyle: { width: 2 },
        areaStyle: { opacity: 0.2 }
      }))
    }]
  }
  performanceChart.setOption(option)
}

// 响应式调整
const handleResize = () => {
  algorithmChart?.resize()
  trendChart?.resize()
  performanceChart?.resize()
}

onMounted(() => {
  loadStatistics()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  algorithmChart?.dispose()
  trendChart?.dispose()
  performanceChart?.dispose()
})
</script>

<style scoped>
.statistics-page {
  padding: 20px;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.metric-card {
  background: #fff;
  border: 2px solid #000;
  padding: 20px;
  transition: all 0.2s;
}

.metric-card:hover {
  border-color: #0052d9;
  box-shadow: 0 4px 12px rgba(0, 82, 217, 0.15);
}

.metric-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  color: #0052d9;
}

.metric-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.metric-value {
  font-size: 32px;
  font-weight: 700;
  color: #000;
  margin-bottom: 12px;
  font-family: 'Courier New', monospace;
}

.metric-footer {
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.metric-label {
  font-size: 12px;
  color: #666;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.chart-card {
  background: #fff;
  border: 2px solid #000;
  padding: 20px;
}

.chart-card.full-width {
  grid-column: 1 / -1;
}

.card-header {
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #000;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.chart-container {
  height: 300px;
  width: 100%;
}

@media (max-width: 1200px) {
  .metrics-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .metrics-grid {
    grid-template-columns: 1fr;
  }
}
</style>
