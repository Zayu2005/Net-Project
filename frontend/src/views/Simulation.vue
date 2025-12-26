<template>
  <div class="simulation-page">
    <!-- 工具栏 -->
    <div class="toolbar">
      <t-space direction="vertical" style="width: 100%;">
        <!-- 第一行：节点选择和算法 -->
        <t-space>
          <t-select v-model="formData.sourceNodeId" placeholder="源节点" style="width: 150px">
            <t-option v-for="node in nodes" :key="node.nodeId" :value="node.nodeId" :label="node.nodeName" />
          </t-select>
          <span>→</span>
          <t-select v-model="formData.destNodeId" placeholder="目标节点" style="width: 150px">
            <t-option v-for="node in nodes" :key="node.nodeId" :value="node.nodeId" :label="node.nodeName" />
          </t-select>
          <t-select v-model="formData.routingAlgorithm" style="width: 120px">
            <t-option value="DIJKSTRA" label="Dijkstra" />
            <t-option value="OSPF" label="OSPF" />
            <t-option value="RIP" label="RIP" />
            <t-option value="BGP" label="BGP" />
          </t-select>
          <t-select v-model="formData.protocol" style="width: 100px">
            <t-option value="TCP" label="TCP" />
            <t-option value="UDP" label="UDP" />
            <t-option value="HTTP" label="HTTP" />
          </t-select>
        </t-space>

        <!-- 第二行：发送内容和按钮 -->
        <t-space>
          <t-input
            v-model="formData.dataContent"
            placeholder="输入要发送的数据内容..."
            style="width: 400px"
            clearable
          >
            <template #prefix-icon>
              <t-icon name="edit" />
            </template>
          </t-input>
          <t-button theme="primary" @click="startTransmission" :disabled="isAnimating || isTransmitting">
            <t-icon name="play-circle" />开始传输
          </t-button>
          <t-button
            v-if="routeResult"
            theme="success"
            variant="outline"
            @click="startPacketAnimation"
            :disabled="isTransmitting"
          >
            <t-icon name="refresh" />重播动画
          </t-button>
        </t-space>
      </t-space>
    </div>

    <!-- 主要内容 -->
    <div class="main-grid">
      <!-- 拓扑图 -->
      <div class="card-container topology-card">
        <div class="card-title">
          网络拓扑
          <span v-if="isTransmitting" class="animation-status">
            <t-loading size="small" />
            <span class="status-text">
              传输中... 第 {{ currentHop }}/{{ (routeResult?.path?.length || 1) - 1 }} 跳
            </span>
          </span>
        </div>
        <div ref="topologyRef" class="topology-container"></div>
      </div>

      <!-- 路由表 -->
      <div class="card-container route-card">
        <RouteTable :route-data="routeResult" />
      </div>

      <!-- 封装可视化 -->
      <div class="card-container encapsulation-card">
        <EncapsulationVisualization
          :encapsulation-data="encapsulationData"
          :is-animating="isAnimating"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted } from 'vue'
import { MessagePlugin } from 'tdesign-vue-next'
import { nodeApi } from '@/api/node'
import { linkApi } from '@/api/link'
import { routingApi } from '@/api/routing'
import { transmissionApi } from '@/api/transmission'
import { encapsulationApi } from '@/api/encapsulation'
import * as echarts from 'echarts'
import RouteTable from '@/components/RouteTable.vue'
import EncapsulationVisualization from '@/components/EncapsulationVisualization.vue'

const nodes = ref([])
const topologyRef = ref(null)
const routeResult = ref(null)
const encapsulationData = ref(null)
const isAnimating = ref(false)
const isTransmitting = ref(false)
const currentHop = ref(0)
const currentDelay = ref(0)
let chartInstance = null
let animationTimer = null
let delayTimer = null

const formData = ref({
  sourceNodeId: '',
  destNodeId: '',
  routingAlgorithm: 'DIJKSTRA',
  protocol: 'TCP',
  dataContent: 'Hello World'
})

const loadNodes = async () => {
  try {
    const res = await nodeApi.getAllNodes()
    nodes.value = res.data || []
    if (nodes.value.length > 0) {
      formData.value.sourceNodeId = nodes.value[0].nodeId
      formData.value.destNodeId = nodes.value[1]?.nodeId || nodes.value[0].nodeId
    }
  } catch (error) {
    MessagePlugin.error('加载节点失败')
  }
}

const initTopology = async () => {
  if (!topologyRef.value) return

  try {
    // 获取拓扑数据
    const res = await linkApi.getTopology()
    const { nodes: topologyNodes, links } = res.data || { nodes: [], links: [] }

    // 初始化 ECharts
    chartInstance = echarts.init(topologyRef.value)

    // 准备节点数据 - 使用数据库中的固定坐标
    const graphNodes = topologyNodes.map(node => {
      // 根据节点类型设置图标和颜色
      let symbol = 'circle'
      let symbolSize = 70
      let color = '#1a73e8'
      let borderColor = '#0d47a1'
      let label = node.nodeName

      if (node.nodeType === 'ROUTER') {
        symbol = 'path://M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372zm5.6-532.7c53 0 89 33.8 93 83.4.3 4.2 3.8 7.4 8 7.4h56.7c2.6 0 4.7-2.1 4.7-4.7 0-86.7-68.4-147.4-162.7-147.4C407.4 290 344 364.2 344 486.8v52.3C344 660.8 407.4 734 517.3 734c94 0 162.7-58.8 162.7-141.4 0-2.6-2.1-4.7-4.7-4.7h-56.8c-4.2 0-7.6 3.2-8 7.3-4.2 46.1-40.1 77.8-93 77.8-65.3 0-102.1-47.9-102.1-133.6v-52.6c.1-87 37-135.5 102.2-135.5z'  // 路由器图标 path
        color = '#1a73e8'
        borderColor = '#0d47a1'
        symbolSize = 80
      } else if (node.nodeType === 'SWITCH') {
        symbol = 'rect'
        color = '#4caf50'
        borderColor = '#2e7d32'
        symbolSize = 70
      } else if (node.nodeType === 'HOST') {
        symbol = 'path://M928 160H96c-17.7 0-32 14.3-32 32v640c0 17.7 14.3 32 32 32h832c17.7 0 32-14.3 32-32V192c0-17.7-14.3-32-32-32zm-40 632H136V232h752v560z'  // 电脑图标 path
        color = '#ff9800'
        borderColor = '#f57c00'
        symbolSize = 70
      }

      return {
        id: node.nodeId,
        name: node.nodeName,
        nodeType: node.nodeType || 'ROUTER',
        x: node.positionX,  // 使用数据库中的x坐标
        y: node.positionY,  // 使用数据库中的y坐标
        symbolSize: symbolSize,
        symbol: symbol,
        itemStyle: {
          color: color,
          borderColor: borderColor,
          borderWidth: 4
        },
        label: {
          show: true,
          color: '#fff',
          fontSize: 16,
          fontWeight: '600',
          fontFamily: 'Arial, sans-serif'
        },
        emphasis: {
          disabled: false,
          scale: 1.15,
          itemStyle: {
            color: color,
            borderColor: borderColor,
            borderWidth: 5
          }
        }
      }
    })

    // 准备边数据
    const graphLinks = links.map(link => ({
      source: link.sourceNodeId,
      target: link.destNodeId,
      lineStyle: {
        color: '#666',
        width: 3,
        curveness: 0
      },
      label: {
        show: true,
        formatter: (params) => {
          const l = links.find(lk =>
            (lk.sourceNodeId === params.data.source && lk.destNodeId === params.data.target) ||
            (lk.sourceNodeId === params.data.target && lk.destNodeId === params.data.source)
          )
          return `${l?.bandwidth || 0}M | ${l?.delay || 0}ms`
        },
        fontSize: 13,
        fontWeight: '500',
        color: '#333',
        backgroundColor: '#fff',
        padding: [6, 10],
        borderRadius: 3,
        borderColor: '#d9d9d9',
        borderWidth: 1.5
      },
      emphasis: {
        lineStyle: {
          width: 5,
          color: '#333'
        }
      }
    }))

    // 配置图表
    const option = {
      backgroundColor: '#ffffff',
      title: {
        text: '网络拓扑结构',
        left: 'center',
        top: 15,
        textStyle: {
          fontSize: 18,
          fontWeight: '600',
          color: '#1a1a1a'
        }
      },
      tooltip: {
        trigger: 'item',
        backgroundColor: '#fff',
        borderColor: '#d9d9d9',
        borderWidth: 2,
        textStyle: {
          color: '#333',
          fontSize: 13
        },
        padding: [12, 16],
        formatter: (params) => {
          if (params.dataType === 'node') {
            const nodeTypeMap = {
              'ROUTER': '路由器',
              'SWITCH': '交换机',
              'HOST': '主机'
            }
            const nodeTypeIcon = {
              'ROUTER': '🔀',
              'SWITCH': '📡',
              'HOST': '💻'
            }
            const nodeType = params.data.nodeType || 'ROUTER'
            return `<div style="padding: 4px;">
              <div style="font-weight: 600; font-size: 15px; margin-bottom: 8px; color: #1a1a1a;">${nodeTypeIcon[nodeType]} ${params.data.name}</div>
              <div style="color: #666; font-size: 13px; line-height: 1.6;">
                <div>节点ID: <span style="color: #1a73e8; font-weight: 500;">${params.data.id}</span></div>
                <div>类型: <span style="color: #1a73e8; font-weight: 500;">${nodeTypeMap[nodeType]}</span></div>
                <div style="margin-top: 6px; padding-top: 6px; border-top: 1px solid #f0f0f0; color: #999; font-size: 12px;">点击设置源/目标节点</div>
              </div>
            </div>`
          } else if (params.dataType === 'edge') {
            const link = links.find(l =>
              (l.sourceNodeId === params.data.source && l.destNodeId === params.data.target) ||
              (l.sourceNodeId === params.data.target && l.destNodeId === params.data.source)
            )
            return `<div style="padding: 4px;">
              <div style="font-weight: 600; font-size: 14px; margin-bottom: 8px; color: #1a1a1a;">链路信息</div>
              <div style="color: #666; font-size: 13px; line-height: 1.8;">
                <div>带宽: <span style="color: #1a73e8; font-weight: 500;">${link?.bandwidth || '-'} Mbps</span></div>
                <div>延迟: <span style="color: #1a73e8; font-weight: 500;">${link?.delay || '-'} ms</span></div>
              </div>
            </div>`
          }
        }
      },
      grid: {
        left: 40,
        right: 40,
        top: 60,
        bottom: 40
      },
      series: [
        {
          type: 'graph',
          layout: 'none',  // 使用固定布局,不使用力导向算法
          data: graphNodes,
          links: graphLinks,
          roam: true,
          draggable: true,
          label: {
            show: true,
            position: 'inside',
            formatter: '{b}'
          },
          edgeLabel: {
            show: true,
            fontSize: 13
          },
          lineStyle: {
            color: '#666',
            width: 3,
            curveness: 0
          },
          emphasis: {
            focus: 'adjacency',
            lineStyle: {
              width: 5
            },
            scale: 1.15
          }
        }
      ],
      animation: false
    }

    chartInstance.setOption(option)

    // 添加点击事件：点击节点可设置为源/目标节点
    chartInstance.on('click', (params) => {
      if (params.dataType === 'node') {
        if (!formData.value.sourceNodeId) {
          formData.value.sourceNodeId = params.data.id
          MessagePlugin.success(`已设置源节点: ${params.data.name}`)
        } else if (!formData.value.destNodeId || formData.value.sourceNodeId === formData.value.destNodeId) {
          if (params.data.id !== formData.value.sourceNodeId) {
            formData.value.destNodeId = params.data.id
            MessagePlugin.success(`已设置目标节点: ${params.data.name}`)
          }
        } else {
          formData.value.sourceNodeId = params.data.id
          formData.value.destNodeId = ''
          MessagePlugin.info(`已重新设置源节点: ${params.data.name}`)
        }
      }
    })

    // 响应式调整
    window.addEventListener('resize', handleResize)
  } catch (error) {
    console.error('加载拓扑图失败:', error)
    MessagePlugin.error('加载拓扑图失败')
  }
}

const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

const highlightPath = () => {
  if (!chartInstance || !routeResult.value) return

  const pathNodes = routeResult.value.path || []
  const option = chartInstance.getOption()

  chartInstance.setOption({
    series: [
      {
        data: option.series[0].data.map(node => {
          const isInPath = pathNodes.includes(node.name)

          // 保留原始节点类型的颜色
          let baseColor = node.itemStyle?.color || '#1a73e8'
          let baseBorderColor = node.itemStyle?.borderColor || '#0d47a1'

          return {
            ...node,
            symbolSize: isInPath ? (node.symbolSize + 10) : node.symbolSize,
            itemStyle: {
              ...node.itemStyle,
              color: isInPath ? '#e53935' : baseColor,
              borderColor: isInPath ? '#b71c1c' : baseBorderColor,
              borderWidth: isInPath ? 5 : 4
            },
            label: {
              show: true,
              color: '#fff',
              fontSize: isInPath ? 18 : 16,
              fontWeight: '600',
              fontFamily: 'Arial, sans-serif'
            }
          }
        }),
        links: option.series[0].links.map(link => {
          const sourceNode = option.series[0].data.find(n => n.id === link.source)
          const targetNode = option.series[0].data.find(n => n.id === link.target)
          const sourceIndex = pathNodes.indexOf(sourceNode?.name)
          const targetIndex = pathNodes.indexOf(targetNode?.name)
          const isInPath = sourceIndex !== -1 && targetIndex !== -1 && Math.abs(sourceIndex - targetIndex) === 1

          return {
            ...link,
            lineStyle: {
              color: isInPath ? '#e53935' : '#666',
              width: isInPath ? 6 : 3,
              curveness: 0
            },
            label: {
              show: true,
              fontSize: isInPath ? 14 : 13,
              fontWeight: isInPath ? '600' : '500',
              color: isInPath ? '#b71c1c' : '#333',
              backgroundColor: '#fff',
              padding: [6, 10],
              borderRadius: 3,
              borderColor: isInPath ? '#e53935' : '#d9d9d9',
              borderWidth: isInPath ? 2 : 1.5
            }
          }
        })
      }
    ],
    animation: false
  })
}

// 启动传输动画
const startPacketAnimation = async () => {
  if (!chartInstance || !routeResult.value) return

  const pathNodes = routeResult.value.path || []
  const pathDetails = routeResult.value.pathDetails || []

  if (pathNodes.length < 2) return

  isTransmitting.value = true
  currentHop.value = 0

  // 获取ECharts图表的option
  const option = chartInstance.getOption()
  const graphData = option.series[0].data

  // 逐跳播放动画
  for (let i = 0; i < pathNodes.length - 1; i++) {
    currentHop.value = i + 1

    const fromNodeName = pathNodes[i]
    const toNodeName = pathNodes[i + 1]

    // 获取节点坐标
    const fromNode = graphData.find(n => n.name === fromNodeName)
    const toNode = graphData.find(n => n.name === toNodeName)

    if (!fromNode || !toNode) continue

    // 获取当前跳的延迟
    const hopDetail = pathDetails[i]
    const delay = hopDetail?.delay || 10

    // 播放当前跳的动画
    await playHopAnimation(fromNode, toNode, delay, i)
  }

  isTransmitting.value = false
  currentHop.value = 0
  MessagePlugin.success('数据包传输完成!')
}

// 播放单跳动画
const playHopAnimation = (fromNode, toNode, delay, hopIndex) => {
  return new Promise((resolve) => {
    const option = chartInstance.getOption()
    const pathNodes = routeResult.value.path || []

    // 高亮当前跳的链路和节点
    highlightCurrentHop(hopIndex)

    // 获取节点的实际渲染坐标
    // 由于force布局，需要从实际渲染的数据中获取坐标
    let startPos, endPos

    try {
      // 方法1: 尝试从节点的value属性获取（如果有定义位置）
      if (fromNode.value && toNode.value) {
        startPos = chartInstance.convertToPixel('series[0]', fromNode.value)
        endPos = chartInstance.convertToPixel('series[0]', toNode.value)
      } else {
        // 方法2: 使用节点的固定位置或计算后的位置
        // 对于force布局，我们使用节点在画布上的位置
        const containerRect = topologyRef.value.getBoundingClientRect()
        const chartWidth = containerRect.width
        const chartHeight = containerRect.height

        // 获取所有节点的索引位置
        const allNodes = option.series[0].data
        const fromIndex = allNodes.findIndex(n => n.name === fromNode.name)
        const toIndex = allNodes.findIndex(n => n.name === toNode.name)

        // 使用force布局后的坐标（如果存在）
        if (fromNode.x !== undefined && fromNode.y !== undefined) {
          startPos = [fromNode.x, fromNode.y]
        } else {
          // 后备方案：使用图表中心点的相对位置
          const angle1 = (fromIndex / allNodes.length) * 2 * Math.PI
          startPos = [
            chartWidth / 2 + Math.cos(angle1) * 200,
            chartHeight / 2 + Math.sin(angle1) * 200
          ]
        }

        if (toNode.x !== undefined && toNode.y !== undefined) {
          endPos = [toNode.x, toNode.y]
        } else {
          const angle2 = (toIndex / allNodes.length) * 2 * Math.PI
          endPos = [
            chartWidth / 2 + Math.cos(angle2) * 200,
            chartHeight / 2 + Math.sin(angle2) * 200
          ]
        }
      }
    } catch (e) {
      console.error('获取节点坐标失败:', e)
      resolve()
      return
    }

    console.log('起点坐标:', startPos, '终点坐标:', endPos)

    // 添加数据包图形
    const packetGraphic = {
      id: 'packet',
      type: 'circle',
      shape: {
        cx: startPos[0],
        cy: startPos[1],
        r: 12
      },
      style: {
        fill: '#ff9800',
        stroke: '#f57c00',
        lineWidth: 3,
        shadowBlur: 10,
        shadowColor: 'rgba(255, 152, 0, 0.5)'
      },
      z: 100
    }

    // 添加延迟文本
    const delayText = {
      id: 'delayText',
      type: 'text',
      style: {
        text: `${delay}ms`,
        x: (startPos[0] + endPos[0]) / 2,
        y: (startPos[1] + endPos[1]) / 2 - 20,
        fill: '#ff5722',
        fontSize: 16,
        fontWeight: 'bold',
        textAlign: 'center',
        textBackgroundColor: '#fff',
        textBorderColor: '#ff5722',
        textBorderWidth: 2,
        textPadding: [6, 10],
        textBorderRadius: 4
      },
      z: 100
    }

    chartInstance.setOption({
      graphic: [packetGraphic, delayText]
    })

    // 动画参数
    const duration = Math.max(delay * 100, 500) // 将延迟转换为动画时长
    const frames = 60
    const frameDelay = duration / frames
    let frame = 0

    // 延迟倒计时
    currentDelay.value = delay
    const countdownInterval = setInterval(() => {
      currentDelay.value--
      if (currentDelay.value <= 0) {
        clearInterval(countdownInterval)
      }
    }, duration / delay)

    // 执行动画
    animationTimer = setInterval(() => {
      frame++
      const progress = frame / frames

      // 使用easeInOutQuad缓动函数
      const easeProgress = progress < 0.5
        ? 2 * progress * progress
        : 1 - Math.pow(-2 * progress + 2, 2) / 2

      const currentX = startPos[0] + (endPos[0] - startPos[0]) * easeProgress
      const currentY = startPos[1] + (endPos[1] - startPos[1]) * easeProgress

      chartInstance.setOption({
        graphic: [{
          id: 'packet',
          shape: {
            cx: currentX,
            cy: currentY
          }
        }]
      })

      if (frame >= frames) {
        clearInterval(animationTimer)
        clearInterval(countdownInterval)

        // 移除图形
        chartInstance.setOption({
          graphic: []
        })

        resolve()
      }
    }, frameDelay)
  })
}

// 高亮当前跳
const highlightCurrentHop = (hopIndex) => {
  if (!chartInstance || !routeResult.value) return

  const pathNodes = routeResult.value.path || []
  const option = chartInstance.getOption()

  const currentFromNode = pathNodes[hopIndex]
  const currentToNode = pathNodes[hopIndex + 1]

  chartInstance.setOption({
    series: [{
      data: option.series[0].data.map(node => {
        const isCurrentNode = node.name === currentFromNode || node.name === currentToNode
        const isInPath = pathNodes.includes(node.name)

        // 保留原始节点类型的颜色
        let baseColor = node.itemStyle?.color || '#1a73e8'
        let baseBorderColor = node.itemStyle?.borderColor || '#0d47a1'
        let baseSymbolSize = node.symbolSize || 70

        return {
          ...node,
          symbolSize: isCurrentNode ? (baseSymbolSize + 20) : (isInPath ? (baseSymbolSize + 10) : baseSymbolSize),
          itemStyle: {
            ...node.itemStyle,
            color: isCurrentNode ? '#ff9800' : (isInPath ? '#e53935' : baseColor),
            borderColor: isCurrentNode ? '#f57c00' : (isInPath ? '#b71c1c' : baseBorderColor),
            borderWidth: isCurrentNode ? 6 : (isInPath ? 5 : 4),
            shadowBlur: isCurrentNode ? 20 : 0,
            shadowColor: isCurrentNode ? 'rgba(255, 152, 0, 0.6)' : 'transparent'
          },
          label: {
            show: true,
            color: '#fff',
            fontSize: isCurrentNode ? 20 : (isInPath ? 18 : 16),
            fontWeight: '600'
          }
        }
      }),
      links: option.series[0].links.map(link => {
        const sourceNode = option.series[0].data.find(n => n.id === link.source)
        const targetNode = option.series[0].data.find(n => n.id === link.target)

        const isCurrentLink =
          (sourceNode?.name === currentFromNode && targetNode?.name === currentToNode) ||
          (sourceNode?.name === currentToNode && targetNode?.name === currentFromNode)

        const sourceIndex = pathNodes.indexOf(sourceNode?.name)
        const targetIndex = pathNodes.indexOf(targetNode?.name)
        const isInPath = sourceIndex !== -1 && targetIndex !== -1 && Math.abs(sourceIndex - targetIndex) === 1

        return {
          ...link,
          lineStyle: {
            color: isCurrentLink ? '#ff9800' : (isInPath ? '#e53935' : '#666'),
            width: isCurrentLink ? 8 : (isInPath ? 6 : 3),
            shadowBlur: isCurrentLink ? 15 : 0,
            shadowColor: isCurrentLink ? 'rgba(255, 152, 0, 0.6)' : 'transparent'
          }
        }
      })
    }]
  })
}

// 清理动画
const cleanupAnimation = () => {
  if (animationTimer) {
    clearInterval(animationTimer)
    animationTimer = null
  }
  if (delayTimer) {
    clearInterval(delayTimer)
    delayTimer = null
  }
  if (chartInstance) {
    chartInstance.setOption({
      graphic: []
    })
  }
  isTransmitting.value = false
  currentHop.value = 0
  currentDelay.value = 0
}

const startTransmission = async () => {
  if (!formData.value.sourceNodeId || !formData.value.destNodeId) {
    MessagePlugin.warning('请选择节点')
    return
  }

  try {
    isAnimating.value = true

    // 1. 计算路由
    const alg = formData.value.routingAlgorithm.toLowerCase()
    const routeRes = await routingApi.calculate(alg, {
      sourceNodeId: formData.value.sourceNodeId,
      destNodeId: formData.value.destNodeId
    })
    routeResult.value = routeRes.data

    // 2. 开始传输
    const transmissionRes = await transmissionApi.startTransmission(formData.value)

    // 3. 获取封装数据
    const encapRes = await encapsulationApi.encapsulate({
      transmissionId: transmissionRes.data.transmissionId,
      protocol: formData.value.protocol,
      dataContent: formData.value.dataContent
    })
    encapsulationData.value = encapRes.data

    isAnimating.value = false
    MessagePlugin.success('传输成功!')

    // 4. 启动动画可视化
    await startPacketAnimation()

  } catch (error) {
    isAnimating.value = false
    console.error('传输失败:', error)
    MessagePlugin.error('传输失败')
  }
}

// 监听路由结果变化，高亮路径
watch(routeResult, () => {
  highlightPath()
})

onMounted(() => {
  loadNodes()
  initTopology()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  cleanupAnimation()
  if (chartInstance) {
    chartInstance.dispose()
  }
})
</script>

<style scoped>
.simulation-page {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 64px);
}

.toolbar {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 18px;
  margin-bottom: 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.toolbar .t-space {
  width: 100%;
}

.main-grid {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  grid-template-rows: auto auto;
  gap: 20px;
  height: calc(100vh - 240px);
}

.topology-card {
  grid-column: 1;
  grid-row: 1 / 3;
}

.route-card {
  grid-column: 2;
  grid-row: 1;
}

.encapsulation-card {
  grid-column: 2;
  grid-row: 2;
}

@media (max-width: 1400px) {
  .main-grid {
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto;
    height: auto;
  }

  .topology-card {
    grid-column: 1;
    grid-row: 1;
  }

  .route-card {
    grid-column: 1;
    grid-row: 2;
  }

  .encapsulation-card {
    grid-column: 1;
    grid-row: 3;
  }
}

.card-container {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: 17px;
  font-weight: 600;
  color: #1a1a1a;
  padding: 20px;
  padding-bottom: 12px;
  margin-bottom: 0;
  border-bottom: 3px solid #1a73e8;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.animation-status {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #ff9800;
  font-weight: 500;
  padding: 6px 12px;
  background: rgba(255, 152, 0, 0.1);
  border-radius: 4px;
  border: 1px solid rgba(255, 152, 0, 0.3);
}

.status-text {
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
}

.topology-container {
  height: 100%;
  min-height: 600px;
  background: #ffffff;
  overflow: hidden;
}

</style>
