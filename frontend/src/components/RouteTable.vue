<template>
  <div class="route-table-container">
    <div class="route-header">
      <h3>路由信息</h3>
      <t-tag v-if="routeData" :theme="getAlgorithmTheme(routeData.algorithm)" size="large">
        {{ routeData.algorithm }}
      </t-tag>
    </div>

    <div v-if="routeData" class="route-content">
      <!-- 路由摘要 -->
      <div class="route-summary">
        <div class="summary-item">
          <span class="label">源节点:</span>
          <span class="value">{{ routeData.sourceNodeId }}</span>
        </div>
        <div class="summary-item">
          <span class="label">目标节点:</span>
          <span class="value">{{ routeData.destNodeId }}</span>
        </div>
        <div class="summary-item">
          <span class="label">跳数:</span>
          <span class="value">{{ routeData.hopCount }}</span>
        </div>
        <div class="summary-item">
          <span class="label">总权重:</span>
          <span class="value">{{ routeData.totalWeight }}</span>
        </div>
        <div class="summary-item">
          <span class="label">总延迟:</span>
          <span class="value">{{ routeData.totalDelay }} ms</span>
        </div>
      </div>

      <!-- 路径可视化 -->
      <div class="path-visualization">
        <div class="path-title">路径:</div>
        <div class="path-nodes">
          <template v-for="(node, index) in routeData.path" :key="index">
            <div class="path-node">{{ node }}</div>
            <div v-if="index < routeData.path.length - 1" class="path-arrow">
              <t-icon name="arrow-right" />
            </div>
          </template>
        </div>
      </div>

      <!-- 详细路由表 -->
      <div class="route-table">
        <table>
          <thead>
            <tr>
              <th>跳序</th>
              <th>源节点</th>
              <th>目标节点</th>
              <th>权重</th>
              <th>延迟(ms)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(detail, index) in routeData.pathDetails" :key="index">
              <td>{{ index + 1 }}</td>
              <td>{{ detail.fromNode }}</td>
              <td>{{ detail.toNode }}</td>
              <td>{{ detail.weight }}</td>
              <td>{{ detail.delay }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div v-else class="empty-state">
      <t-icon name="route" size="48px" />
      <p>选择节点并计算路由</p>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

defineProps({
  routeData: {
    type: Object,
    default: null
  }
})

const getAlgorithmTheme = (algorithm) => {
  const themes = {
    'DIJKSTRA': 'primary',
    'OSPF': 'success',
    'RIP': 'warning',
    'BGP': 'danger'
  }
  return themes[algorithm] || 'default'
}
</script>

<style scoped>
.route-table-container {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.route-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #1a73e8;
}

.route-header h3 {
  margin: 0;
  font-size: 17px;
  font-weight: 600;
  color: #1a1a1a;
}

.route-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.route-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 12px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
}

.summary-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.summary-item .label {
  font-size: 12px;
  color: #666;
  font-weight: 500;
}

.summary-item .value {
  font-size: 16px;
  color: #1a1a1a;
  font-weight: 600;
}

.path-visualization {
  padding: 16px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.path-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.path-nodes {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.path-node {
  padding: 10px 16px;
  background: #1a73e8;
  color: #fff;
  font-weight: 600;
  border-radius: 4px;
  font-size: 14px;
}

.path-arrow {
  color: #1a73e8;
  font-size: 20px;
}

.route-table {
  overflow-x: auto;
}

.route-table table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.route-table th,
.route-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.route-table th {
  background: #f5f7fa;
  font-weight: 600;
  color: #333;
}

.route-table tbody tr:hover {
  background: #f5f7fa;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #999;
}

.empty-state p {
  margin-top: 16px;
  font-size: 14px;
}
</style>
