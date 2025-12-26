<template>
  <div class="topology-page">
    <!-- 标签页切换 -->
    <t-tabs v-model="activeTab" theme="normal">
      <t-tab-panel value="nodes" label="节点管理">
        <div class="tab-content">
          <!-- 操作栏 -->
          <div class="toolbar">
            <t-button theme="primary" @click="handleAddNode">
              <t-icon name="add" />新增节点
            </t-button>
          </div>

          <!-- 节点表格 -->
          <t-table
            :data="nodeList"
            :columns="nodeColumns"
            row-key="nodeId"
            :bordered="true"
            :hover="true"
            :loading="nodeLoading"
            :pagination="{
              defaultCurrent: 1,
              defaultPageSize: 10,
              total: nodeList.length
            }"
          >
            <template #nodeType="{ row }">
              <t-tag
                v-if="row.nodeType === 'ROUTER'"
                theme="primary"
              >
                <t-icon name="router" />
                路由器
              </t-tag>
              <t-tag
                v-else-if="row.nodeType === 'SWITCH'"
                theme="success"
              >
                <t-icon name="layers" />
                交换机
              </t-tag>
              <t-tag
                v-else-if="row.nodeType === 'HOST'"
                theme="warning"
              >
                <t-icon name="laptop" />
                主机
              </t-tag>
              <t-tag v-else theme="default">未知</t-tag>
            </template>
            <template #status="{ row }">
              <t-tag :theme="row.status === 1 ? 'success' : 'default'">
                {{ row.status === 1 ? '活跃' : '离线' }}
              </t-tag>
            </template>
            <template #operation="{ row }">
              <t-space>
                <t-button theme="primary" variant="text" size="small" @click="handleEditNode(row)">
                  <t-icon name="edit" />编辑
                </t-button>
                <t-button theme="danger" variant="text" size="small" @click="handleDeleteNode(row)">
                  <t-icon name="delete" />删除
                </t-button>
              </t-space>
            </template>
          </t-table>
        </div>
      </t-tab-panel>

      <t-tab-panel value="links" label="链路管理">
        <div class="tab-content">
          <!-- 操作栏 -->
          <div class="toolbar">
            <t-button theme="primary" @click="handleAddLink">
              <t-icon name="add" />新增链路
            </t-button>
          </div>

          <!-- 链路表格 -->
          <t-table
            :data="linkList"
            :columns="linkColumns"
            row-key="linkId"
            :bordered="true"
            :hover="true"
            :loading="linkLoading"
            :pagination="{
              defaultCurrent: 1,
              defaultPageSize: 10,
              total: linkList.length
            }"
          >
            <template #status="{ row }">
              <t-tag :theme="row.status === 'UP' ? 'success' : 'danger'">
                {{ row.status === 'UP' ? '正常' : '断开' }}
              </t-tag>
            </template>
            <template #operation="{ row }">
              <t-space>
                <t-button theme="primary" variant="text" size="small" @click="handleEditLink(row)">
                  <t-icon name="edit" />编辑
                </t-button>
                <t-button theme="danger" variant="text" size="small" @click="handleDeleteLink(row)">
                  <t-icon name="delete" />删除
                </t-button>
              </t-space>
            </template>
          </t-table>
        </div>
      </t-tab-panel>
    </t-tabs>

    <!-- 节点编辑对话框 -->
    <t-dialog
      v-model:visible="nodeDialogVisible"
      :header="nodeDialogTitle"
      width="600px"
      @confirm="handleNodeConfirm"
    >
      <t-form :data="nodeFormData" label-align="left" :label-width="100">
        <t-form-item label="节点ID" name="nodeId">
          <t-input v-model="nodeFormData.nodeId" placeholder="请输入节点ID" :disabled="isEditMode" />
        </t-form-item>
        <t-form-item label="节点名称" name="nodeName">
          <t-input v-model="nodeFormData.nodeName" placeholder="请输入节点名称" />
        </t-form-item>
        <t-form-item label="IP地址" name="ipAddress">
          <t-input v-model="nodeFormData.ipAddress" placeholder="请输入IP地址" />
        </t-form-item>
        <t-form-item label="MAC地址" name="macAddress">
          <t-input v-model="nodeFormData.macAddress" placeholder="请输入MAC地址" />
        </t-form-item>
        <t-form-item label="X坐标" name="positionX">
          <t-input-number v-model="nodeFormData.positionX" :min="0" :max="1000" />
        </t-form-item>
        <t-form-item label="Y坐标" name="positionY">
          <t-input-number v-model="nodeFormData.positionY" :min="0" :max="1000" />
        </t-form-item>
        <t-form-item label="节点类型" name="nodeType">
          <t-select v-model="nodeFormData.nodeType" style="width: 100%">
            <t-option value="ROUTER" label="路由器">
              <div style="display: flex; align-items: center; gap: 8px;">
                <t-icon name="router" />
                <span>路由器</span>
              </div>
            </t-option>
            <t-option value="SWITCH" label="交换机">
              <div style="display: flex; align-items: center; gap: 8px;">
                <t-icon name="layers" />
                <span>交换机</span>
              </div>
            </t-option>
            <t-option value="HOST" label="主机">
              <div style="display: flex; align-items: center; gap: 8px;">
                <t-icon name="laptop" />
                <span>主机</span>
              </div>
            </t-option>
          </t-select>
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-select v-model="nodeFormData.status" style="width: 100%">
            <t-option :value="1" label="活跃" />
            <t-option :value="0" label="离线" />
          </t-select>
        </t-form-item>
      </t-form>
    </t-dialog>

    <!-- 链路编辑对话框 -->
    <t-dialog
      v-model:visible="linkDialogVisible"
      :header="linkDialogTitle"
      width="600px"
      @confirm="handleLinkConfirm"
    >
      <t-form :data="linkFormData" label-align="left" :label-width="100">
        <t-form-item label="链路ID" name="linkId">
          <t-input v-model="linkFormData.linkId" placeholder="请输入链路ID" :disabled="isEditMode" />
        </t-form-item>
        <t-form-item label="源节点" name="sourceNodeId">
          <t-select v-model="linkFormData.sourceNodeId" placeholder="请选择源节点" style="width: 100%">
            <t-option v-for="node in nodeList" :key="node.nodeId" :value="node.nodeId" :label="node.nodeName" />
          </t-select>
        </t-form-item>
        <t-form-item label="目标节点" name="destNodeId">
          <t-select v-model="linkFormData.destNodeId" placeholder="请选择目标节点" style="width: 100%">
            <t-option v-for="node in nodeList" :key="node.nodeId" :value="node.nodeId" :label="node.nodeName" />
          </t-select>
        </t-form-item>
        <t-form-item label="带宽" name="bandwidth">
          <t-input-number v-model="linkFormData.bandwidth" :min="1" :max="10000" suffix="Mbps" />
        </t-form-item>
        <t-form-item label="延迟" name="delay">
          <t-input-number v-model="linkFormData.delay" :min="0" :max="1000" suffix="ms" />
        </t-form-item>
        <t-form-item label="丢包率" name="lossRate">
          <t-input-number v-model="linkFormData.lossRate" :min="0" :max="100" :step="0.1" suffix="%" />
        </t-form-item>
        <t-form-item label="状态" name="status">
          <t-select v-model="linkFormData.status" style="width: 100%">
            <t-option value="UP" label="正常" />
            <t-option value="DOWN" label="断开" />
          </t-select>
        </t-form-item>
      </t-form>
    </t-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { MessagePlugin } from 'tdesign-vue-next'
import { nodeApi } from '@/api/node'
import { linkApi } from '@/api/link'

const activeTab = ref('nodes')
const nodeLoading = ref(false)
const linkLoading = ref(false)
const nodeList = ref([])
const linkList = ref([])

// 节点表格列定义
const nodeColumns = [
  { colKey: 'nodeId', title: '节点ID', width: 100 },
  { colKey: 'nodeName', title: '节点名称', width: 150 },
  { colKey: 'nodeType', title: '节点类型', width: 120, cell: 'nodeType' },
  { colKey: 'ipAddress', title: 'IP地址', width: 150 },
  { colKey: 'macAddress', title: 'MAC地址', width: 180 },
  { colKey: 'positionX', title: 'X坐标', width: 100 },
  { colKey: 'positionY', title: 'Y坐标', width: 100 },
  { colKey: 'status', title: '状态', width: 100, cell: 'status' },
  { colKey: 'operation', title: '操作', width: 150, cell: 'operation' }
]

// 链路表格列定义
const linkColumns = [
  { colKey: 'linkId', title: '链路ID', width: 100 },
  { colKey: 'sourceNodeId', title: '源节点', width: 120 },
  { colKey: 'destNodeId', title: '目标节点', width: 120 },
  { colKey: 'bandwidth', title: '带宽(Mbps)', width: 120 },
  { colKey: 'delay', title: '延迟(ms)', width: 100 },
  { colKey: 'lossRate', title: '丢包率(%)', width: 100 },
  { colKey: 'status', title: '状态', width: 100, cell: 'status' },
  { colKey: 'operation', title: '操作', width: 150, cell: 'operation' }
]

// 节点对话框
const nodeDialogVisible = ref(false)
const nodeDialogTitle = ref('新增节点')
const isEditMode = ref(false)
const nodeFormData = ref({
  nodeId: '',
  nodeName: '',
  ipAddress: '',
  macAddress: '',
  positionX: 0,
  positionY: 0,
  nodeType: 'ROUTER',
  status: 1
})

// 链路对话框
const linkDialogVisible = ref(false)
const linkDialogTitle = ref('新增链路')
const linkFormData = ref({
  linkId: '',
  sourceNodeId: '',
  destNodeId: '',
  bandwidth: 100,
  delay: 10,
  lossRate: 0,
  status: 'UP'
})

// 加载节点列表
const loadNodes = async () => {
  nodeLoading.value = true
  try {
    const res = await nodeApi.getAllNodes()
    nodeList.value = res.data || []
  } catch (error) {
    MessagePlugin.error('加载节点列表失败')
  } finally {
    nodeLoading.value = false
  }
}

// 加载链路列表
const loadLinks = async () => {
  linkLoading.value = true
  try {
    const res = await linkApi.getAllLinks()
    linkList.value = res.data || []
  } catch (error) {
    MessagePlugin.error('加载链路列表失败')
  } finally {
    linkLoading.value = false
  }
}

// 新增节点
const handleAddNode = () => {
  isEditMode.value = false
  nodeDialogTitle.value = '新增节点'
  nodeFormData.value = {
    nodeId: '',
    nodeName: '',
    ipAddress: '',
    macAddress: '',
    positionX: 0,
    positionY: 0,
    nodeType: 'ROUTER',
    status: 1
  }
  nodeDialogVisible.value = true
}

// 编辑节点
const handleEditNode = (row) => {
  isEditMode.value = true
  nodeDialogTitle.value = '编辑节点'
  nodeFormData.value = { ...row }
  nodeDialogVisible.value = true
}

// 删除节点
const handleDeleteNode = async (row) => {
  try {
    await nodeApi.deleteNode(row.nodeId)
    MessagePlugin.success('删除成功')
    loadNodes()
  } catch (error) {
    MessagePlugin.error('删除失败')
  }
}

// 节点对话框确认
const handleNodeConfirm = async () => {
  try {
    if (isEditMode.value) {
      await nodeApi.updateNode(nodeFormData.value.nodeId, nodeFormData.value)
      MessagePlugin.success('更新成功')
    } else {
      await nodeApi.createNode(nodeFormData.value)
      MessagePlugin.success('创建成功')
    }
    nodeDialogVisible.value = false
    loadNodes()
  } catch (error) {
    MessagePlugin.error('操作失败')
  }
}

// 新增链路
const handleAddLink = () => {
  isEditMode.value = false
  linkDialogTitle.value = '新增链路'
  linkFormData.value = {
    linkId: '',
    sourceNodeId: '',
    destNodeId: '',
    bandwidth: 100,
    delay: 10,
    lossRate: 0,
    status: 'UP'
  }
  linkDialogVisible.value = true
}

// 编辑链路
const handleEditLink = (row) => {
  isEditMode.value = true
  linkDialogTitle.value = '编辑链路'
  linkFormData.value = { ...row }
  linkDialogVisible.value = true
}

// 删除链路
const handleDeleteLink = async (row) => {
  try {
    await linkApi.deleteLink(row.linkId)
    MessagePlugin.success('删除成功')
    loadLinks()
  } catch (error) {
    MessagePlugin.error('删除失败')
  }
}

// 链路对话框确认
const handleLinkConfirm = async () => {
  try {
    if (isEditMode.value) {
      await linkApi.updateLink(linkFormData.value.linkId, linkFormData.value)
      MessagePlugin.success('更新成功')
    } else {
      await linkApi.createLink(linkFormData.value)
      MessagePlugin.success('创建成功')
    }
    linkDialogVisible.value = false
    loadLinks()
  } catch (error) {
    MessagePlugin.error('操作失败')
  }
}

onMounted(() => {
  loadNodes()
  loadLinks()
})
</script>

<style scoped>
.topology-page {
  padding: 20px;
}

.tab-content {
  padding: 20px 0;
}

.toolbar {
  background: #fff;
  border: 1px solid #ddd;
  padding: 16px;
  margin-bottom: 20px;
}
</style>
