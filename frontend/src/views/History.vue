<template>
  <div class="history-page">
    <!-- 筛选工具栏 -->
    <div class="filter-bar">
      <t-space>
        <t-date-range-picker
          v-model="dateRange"
          clearable
          placeholder="选择时间范围"
          style="width: 300px"
        />
        <t-select
          v-model="filterAlgorithm"
          placeholder="路由算法"
          clearable
          style="width: 150px"
        >
          <t-option value="DIJKSTRA" label="Dijkstra" />
          <t-option value="OSPF" label="OSPF" />
          <t-option value="RIP" label="RIP" />
          <t-option value="BGP" label="BGP" />
        </t-select>
        <t-select
          v-model="filterStatus"
          placeholder="状态"
          clearable
          style="width: 120px"
        >
          <t-option value="SUCCESS" label="成功" />
          <t-option value="FAILED" label="失败" />
        </t-select>
        <t-button theme="primary" @click="handleSearch">
          <t-icon name="search" />查询
        </t-button>
        <t-button theme="default" @click="handleReset">
          <t-icon name="refresh" />重置
        </t-button>
      </t-space>
    </div>

    <!-- 历史记录表格 -->
    <div class="table-container">
      <t-table
        :data="historyList"
        :columns="columns"
        row-key="transmissionId"
        :bordered="true"
        :hover="true"
        :loading="loading"
        :pagination="{
          current: pagination.current,
          pageSize: pagination.pageSize,
          total: pagination.total,
          showJumper: true,
          onChange: handlePageChange
        }"
      >
        <template #routingAlgorithm="{ row }">
          <t-tag theme="primary">{{ row.routingAlgorithm }}</t-tag>
        </template>
        <template #protocol="{ row }">
          <t-tag theme="success">{{ row.protocol }}</t-tag>
        </template>
        <template #status="{ row }">
          <t-tag :theme="row.status === 'SUCCESS' ? 'success' : 'danger'">
            {{ row.status === 'SUCCESS' ? '成功' : '失败' }}
          </t-tag>
        </template>
        <template #operation="{ row }">
          <t-button theme="primary" variant="text" size="small" @click="handleViewDetail(row)">
            <t-icon name="view-list" />详情
          </t-button>
        </template>
      </t-table>
    </div>

    <!-- 详情对话框 -->
    <t-dialog
      v-model:visible="detailDialogVisible"
      header="传输详情"
      width="800px"
      :footer="false"
    >
      <div v-if="currentRecord" class="detail-content">
        <!-- 基本信息 -->
        <div class="detail-section">
          <h3 class="section-title">基本信息</h3>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">传输ID:</span>
              <span class="value">{{ currentRecord.transmissionId }}</span>
            </div>
            <div class="info-item">
              <span class="label">源节点:</span>
              <span class="value">{{ currentRecord.sourceNodeId }}</span>
            </div>
            <div class="info-item">
              <span class="label">目标节点:</span>
              <span class="value">{{ currentRecord.destNodeId }}</span>
            </div>
            <div class="info-item">
              <span class="label">路由算法:</span>
              <t-tag theme="primary">{{ currentRecord.routingAlgorithm }}</t-tag>
            </div>
            <div class="info-item">
              <span class="label">协议类型:</span>
              <t-tag theme="success">{{ currentRecord.protocol }}</t-tag>
            </div>
            <div class="info-item">
              <span class="label">传输状态:</span>
              <t-tag :theme="currentRecord.status === 'SUCCESS' ? 'success' : 'danger'">
                {{ currentRecord.status === 'SUCCESS' ? '成功' : '失败' }}
              </t-tag>
            </div>
            <div class="info-item">
              <span class="label">传输时间:</span>
              <span class="value">{{ currentRecord.timestamp }}</span>
            </div>
          </div>
        </div>

        <!-- 传输路径 -->
        <div class="detail-section">
          <h3 class="section-title">传输路径</h3>
          <div class="path-display">
            <template v-for="(node, i) in currentRecord.path?.split(',')" :key="i">
              <span class="path-node">{{ node }}</span>
              <span v-if="i < currentRecord.path?.split(',').length - 1" class="path-arrow">→</span>
            </template>
          </div>
          <div class="path-stats">
            <div class="stat-item">
              <span class="stat-label">跳数:</span>
              <span class="stat-value">{{ currentRecord.hopCount || 0 }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">总延迟:</span>
              <span class="stat-value">{{ currentRecord.totalDelay || 0 }} ms</span>
            </div>
          </div>
        </div>

        <!-- 数据内容 -->
        <div class="detail-section">
          <h3 class="section-title">数据内容</h3>
          <div class="data-content">
            {{ currentRecord.dataContent || '无数据' }}
          </div>
        </div>

        <!-- OSI协议栈封装 -->
        <div class="detail-section" v-if="currentRecord.encapsulationData">
          <h3 class="section-title">OSI协议栈封装</h3>
          <div class="protocol-stack">
            <div class="layer-item" v-for="layer in protocolLayers" :key="layer.key">
              <div class="layer-header">
                <span class="layer-name">{{ layer.name }}</span>
                <t-tag size="small" theme="default">{{ layer.label }}</t-tag>
              </div>
              <div class="layer-content">
                {{ currentRecord.encapsulationData?.[layer.key] || '无数据' }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </t-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { MessagePlugin } from 'tdesign-vue-next'
import { transmissionApi } from '@/api/transmission'

const loading = ref(false)
const historyList = ref([])
const dateRange = ref([])
const filterAlgorithm = ref('')
const filterStatus = ref('')

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

// 表格列定义
const columns = [
  { colKey: 'transmissionId', title: '传输ID', width: 120, ellipsis: true },
  { colKey: 'sourceNodeId', title: '源节点', width: 100 },
  { colKey: 'destNodeId', title: '目标节点', width: 100 },
  { colKey: 'routingAlgorithm', title: '路由算法', width: 120, cell: 'routingAlgorithm' },
  { colKey: 'protocol', title: '协议', width: 100, cell: 'protocol' },
  { colKey: 'status', title: '状态', width: 100, cell: 'status' },
  { colKey: 'timestamp', title: '传输时间', width: 180 },
  { colKey: 'operation', title: '操作', width: 100, cell: 'operation', fixed: 'right' }
]

// 详情对话框
const detailDialogVisible = ref(false)
const currentRecord = ref(null)

// OSI协议栈层次
const protocolLayers = [
  { key: 'applicationLayer', name: '应用层', label: 'Layer 7' },
  { key: 'presentationLayer', name: '表示层', label: 'Layer 6' },
  { key: 'sessionLayer', name: '会话层', label: 'Layer 5' },
  { key: 'transportLayer', name: '传输层', label: 'Layer 4' },
  { key: 'networkLayer', name: '网络层', label: 'Layer 3' },
  { key: 'dataLinkLayer', name: '数据链路层', label: 'Layer 2' },
  { key: 'physicalLayer', name: '物理层', label: 'Layer 1' }
]

// 加载历史记录
const loadHistory = async () => {
  loading.value = true
  try {
    const res = await transmissionApi.getHistory()
    let list = res.data || []

    // 应用筛选
    if (filterAlgorithm.value) {
      list = list.filter(item => item.routingAlgorithm === filterAlgorithm.value)
    }
    if (filterStatus.value) {
      list = list.filter(item => item.status === filterStatus.value)
    }

    historyList.value = list
    pagination.value.total = list.length
  } catch (error) {
    MessagePlugin.error('加载历史记录失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleSearch = () => {
  pagination.value.current = 1
  loadHistory()
}

// 重置
const handleReset = () => {
  dateRange.value = []
  filterAlgorithm.value = ''
  filterStatus.value = ''
  pagination.value.current = 1
  loadHistory()
}

// 分页变化
const handlePageChange = (pageInfo) => {
  pagination.value.current = pageInfo.current
  pagination.value.pageSize = pageInfo.pageSize
}

// 查看详情
const handleViewDetail = (row) => {
  currentRecord.value = row
  detailDialogVisible.value = true
}

onMounted(() => {
  loadHistory()
})
</script>

<style scoped>
.history-page {
  padding: 20px;
}

.filter-bar {
  background: #fff;
  border: 1px solid #ddd;
  padding: 16px;
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  border: 1px solid #ddd;
  padding: 20px;
}

.detail-content {
  max-height: 600px;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-section:last-child {
  border-bottom: none;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #000;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.label {
  font-weight: 600;
  color: #333;
  min-width: 80px;
}

.value {
  color: #666;
  font-family: 'Courier New', monospace;
}

.path-display {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.path-node {
  padding: 6px 12px;
  background: #0052d9;
  color: #fff;
  font-weight: 600;
}

.path-arrow {
  color: #666;
  font-size: 18px;
}

.path-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-label {
  font-weight: 600;
  color: #333;
}

.stat-value {
  color: #0052d9;
  font-size: 18px;
  font-weight: 600;
  font-family: 'Courier New', monospace;
}

.data-content {
  background: #f5f5f5;
  border: 1px solid #ddd;
  padding: 16px;
  font-family: 'Courier New', monospace;
  word-break: break-all;
}

.protocol-stack {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.layer-item {
  border: 2px solid #000;
  overflow: hidden;
}

.layer-header {
  background: #f5f5f5;
  padding: 12px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ddd;
}

.layer-name {
  font-weight: 600;
  color: #000;
}

.layer-content {
  padding: 12px 16px;
  background: #fff;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #666;
  word-break: break-all;
}
</style>
