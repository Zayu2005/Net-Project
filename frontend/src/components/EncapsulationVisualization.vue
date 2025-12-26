<template>
  <div class="encapsulation-container" :class="{ 'fullscreen': isFullscreen }">
    <div class="encap-header">
      <h3>OSI七层封装与解封装完整流程</h3>
      <t-space>
        <t-select
          v-if="layers && layers.length > 0"
          v-model="animationSpeed"
          size="small"
          style="width: 120px"
          :disabled="isPlaying"
        >
          <t-option value="very-slow" label="很慢 3s" />
          <t-option value="slow" label="慢速 2s" />
          <t-option value="normal" label="正常 1s" />
          <t-option value="fast" label="快速 0.5s" />
        </t-select>
        <t-button
          v-if="layers && layers.length > 0"
          size="small"
          :theme="isPlaying ? 'warning' : 'success'"
          @click="toggleAnimation"
        >
          <t-icon :name="isPlaying ? 'pause-circle' : 'play-circle'" />
          {{ isPlaying ? '暂停' : '开始演示' }}
        </t-button>
        <t-button
          v-if="layers && layers.length > 0 && (senderStep > 0 || receiverStep > 0)"
          size="small"
          theme="default"
          variant="outline"
          @click="resetAnimation"
          :disabled="isPlaying"
        >
          <t-icon name="refresh" />
          重置
        </t-button>
        <t-button
          v-if="layers && layers.length > 0"
          size="small"
          theme="primary"
          variant="outline"
          @click="toggleFullscreen"
        >
          <t-icon :name="isFullscreen ? 'fullscreen-exit' : 'fullscreen'" />
          {{ isFullscreen ? '退出全屏' : '全屏显示' }}
        </t-button>
      </t-space>
    </div>

    <div v-if="layers && layers.length > 0" class="dual-node-container">
      <!-- 动画进度说明 -->
      <div v-if="isPlaying || senderStep > 0 || receiverStep > 0" class="animation-status">
        <div class="status-item">
          <span class="status-label">发送端封装:</span>
          <span class="status-value">{{ senderStep }}/{{ layers.length }}</span>
        </div>
        <div class="status-item" v-if="isTransmitting">
          <span class="status-label transmitting">
            <t-icon name="swap" />
            数据传输中...
          </span>
        </div>
        <div class="status-item">
          <span class="status-label">接收端解封装:</span>
          <span class="status-value">{{ receiverStep }}/{{ layers.length }}</span>
        </div>
      </div>

      <!-- 双节点可视化 -->
      <div class="nodes-wrapper">
        <!-- 发送端 -->
        <div class="node-section sender-section">
          <div class="node-header">
            <div class="node-icon">
              <t-icon name="laptop" size="24px" />
            </div>
            <div class="node-info">
              <h4>发送端 (源节点)</h4>
              <p>数据封装过程</p>
            </div>
          </div>

          <div class="layers-stack">
            <div
              v-for="(layer, index) in sortedLayersForSender"
              :key="'sender-' + layer.layerNumber"
              class="layer-box"
              :class="{
                'active': index < senderStep,
                'processing': index === senderStep - 1 && isPlaying && currentPhase === 'encapsulation',
                'completed': index < senderStep && !(index === senderStep - 1 && isPlaying && currentPhase === 'encapsulation')
              }"
              :style="{ animationDelay: (index * 0.1) + 's' }"
            >
              <div class="layer-content">
                <div class="layer-number">L{{ layer.layerNumber }}</div>
                <div class="layer-details">
                  <div class="layer-name">{{ layer.layerName }}</div>
                  <div class="layer-size">{{ layer.payloadSize }}B</div>
                </div>
                <div v-if="index === senderStep - 1 && isPlaying && currentPhase === 'encapsulation'" class="layer-spinner">
                  <t-loading size="small" />
                </div>
              </div>

              <!-- 封装动画效果 -->
              <div v-if="index === senderStep - 1 && isPlaying && currentPhase === 'encapsulation'" class="wrapping-effect">
                <div class="wrap-line" v-for="i in 3" :key="i" :style="{ animationDelay: (i * 0.2) + 's' }"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 传输通道 -->
        <div class="transmission-channel">
          <div class="channel-line">
            <div v-if="isTransmitting" class="data-packet" :class="{ 'moving': isTransmitting }">
              <div class="packet-icon">
                <t-icon name="file" />
              </div>
              <div class="packet-info">数据包</div>
            </div>
          </div>
          <div class="channel-label">网络传输</div>
        </div>

        <!-- 接收端 -->
        <div class="node-section receiver-section">
          <div class="node-header">
            <div class="node-icon">
              <t-icon name="server" size="24px" />
            </div>
            <div class="node-info">
              <h4>接收端 (目标节点)</h4>
              <p>数据解封装过程</p>
            </div>
          </div>

          <div class="layers-stack">
            <div
              v-for="(layer, index) in sortedLayersForReceiver"
              :key="'receiver-' + layer.layerNumber"
              class="layer-box"
              :class="{
                'active': index < receiverStep,
                'processing': index === receiverStep - 1 && isPlaying && currentPhase === 'decapsulation',
                'completed': index < receiverStep && !(index === receiverStep - 1 && isPlaying && currentPhase === 'decapsulation')
              }"
              :style="{ animationDelay: (index * 0.1) + 's' }"
            >
              <div class="layer-content">
                <div class="layer-number">L{{ layer.layerNumber }}</div>
                <div class="layer-details">
                  <div class="layer-name">{{ layer.layerName }}</div>
                  <div class="layer-size">{{ layer.payloadSize }}B</div>
                </div>
                <div v-if="index === receiverStep - 1 && isPlaying && currentPhase === 'decapsulation'" class="layer-spinner">
                  <t-loading size="small" />
                </div>
              </div>

              <!-- 解封装动画效果 -->
              <div v-if="index === receiverStep - 1 && isPlaying && currentPhase === 'decapsulation'" class="unwrapping-effect">
                <div class="unwrap-line" v-for="i in 3" :key="i" :style="{ animationDelay: (i * 0.2) + 's' }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 完成提示 -->
      <div v-if="receiverStep === layers.length && !isPlaying" class="completion-message">
        <t-icon name="check-circle" size="32px" />
        <span>数据传输完成！总大小: <strong>{{ totalSize }} 字节</strong></span>
      </div>
    </div>

    <div v-else class="empty-state">
      <t-icon name="layers" size="48px" />
      <p>开始传输后查看OSI封装与解封装过程</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onUnmounted, onMounted } from 'vue'

const props = defineProps({
  encapsulationData: {
    type: Object,
    default: null
  },
  isAnimating: {
    type: Boolean,
    default: false
  }
})

const isPlaying = ref(false)
const senderStep = ref(0)
const receiverStep = ref(0)
const isTransmitting = ref(false)
const currentPhase = ref('encapsulation') // 'encapsulation', 'transmission', 'decapsulation'
const animationSpeed = ref('slow')
const isFullscreen = ref(false)
let animationTimer = null

const layers = computed(() => props.encapsulationData?.layers || [])
const totalSize = computed(() => props.encapsulationData?.totalSize || 0)

// 动画速度配置（毫秒）
const speedConfig = {
  'very-slow': 3000,
  'slow': 2000,
  'normal': 1000,
  'fast': 500
}

const animationDuration = computed(() => speedConfig[animationSpeed.value])

// 发送端层排序（从应用层7到物理层1）
const sortedLayersForSender = computed(() => {
  if (!layers.value.length) return []
  return [...layers.value].sort((a, b) => b.layerNumber - a.layerNumber)
})

// 接收端层排序（从物理层1到应用层7）
const sortedLayersForReceiver = computed(() => {
  if (!layers.value.length) return []
  return [...layers.value].sort((a, b) => a.layerNumber - b.layerNumber)
})

const toggleAnimation = () => {
  if (isPlaying.value) {
    pauseAnimation()
  } else {
    startAnimation()
  }
}

const startAnimation = () => {
  if (isPlaying.value) return

  isPlaying.value = true

  // 如果是重新开始
  if (senderStep.value === 0) {
    currentPhase.value = 'encapsulation'
    playEncapsulation()
  }
  // 如果在封装阶段
  else if (currentPhase.value === 'encapsulation' && senderStep.value < layers.value.length) {
    playEncapsulation()
  }
  // 如果在传输阶段
  else if (currentPhase.value === 'transmission') {
    playTransmission()
  }
  // 如果在解封装阶段
  else if (currentPhase.value === 'decapsulation' && receiverStep.value < layers.value.length) {
    playDecapsulation()
  }
}

const playEncapsulation = () => {
  if (!isPlaying.value || senderStep.value >= layers.value.length) {
    // 封装完成，开始传输
    currentPhase.value = 'transmission'
    playTransmission()
    return
  }

  animationTimer = setTimeout(() => {
    senderStep.value++

    if (senderStep.value < layers.value.length) {
      playEncapsulation()
    } else {
      // 封装完成，开始传输
      currentPhase.value = 'transmission'
      playTransmission()
    }
  }, animationDuration.value)
}

const playTransmission = () => {
  isTransmitting.value = true

  animationTimer = setTimeout(() => {
    isTransmitting.value = false
    // 传输完成，开始解封装
    currentPhase.value = 'decapsulation'
    playDecapsulation()
  }, animationDuration.value * 1.5) // 传输时间稍长
}

const playDecapsulation = () => {
  if (!isPlaying.value || receiverStep.value >= layers.value.length) {
    isPlaying.value = false
    return
  }

  animationTimer = setTimeout(() => {
    receiverStep.value++

    if (receiverStep.value < layers.value.length) {
      playDecapsulation()
    } else {
      isPlaying.value = false
    }
  }, animationDuration.value)
}

const pauseAnimation = () => {
  isPlaying.value = false
  if (animationTimer) {
    clearTimeout(animationTimer)
    animationTimer = null
  }
}

const resetAnimation = () => {
  pauseAnimation()
  senderStep.value = 0
  receiverStep.value = 0
  isTransmitting.value = false
  currentPhase.value = 'encapsulation'
}

const toggleFullscreen = () => {
  const container = document.querySelector('.encapsulation-container')

  if (!isFullscreen.value) {
    if (container.requestFullscreen) {
      container.requestFullscreen()
    } else if (container.webkitRequestFullscreen) {
      container.webkitRequestFullscreen()
    } else if (container.msRequestFullscreen) {
      container.msRequestFullscreen()
    }
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    } else if (document.webkitExitFullscreen) {
      document.webkitExitFullscreen()
    } else if (document.msExitFullscreen) {
      document.msExitFullscreen()
    }
  }
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  isFullscreen.value = !!(
    document.fullscreenElement ||
    document.webkitFullscreenElement ||
    document.msFullscreenElement
  )
}

onMounted(() => {
  document.addEventListener('fullscreenchange', handleFullscreenChange)
  document.addEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.addEventListener('msfullscreenchange', handleFullscreenChange)
})

// 监听数据变化，自动播放动画
watch(() => props.encapsulationData, (newData) => {
  if (newData && newData.layers && newData.layers.length > 0) {
    resetAnimation()
    // 延迟500ms后自动播放
    setTimeout(() => {
      startAnimation()
    }, 500)
  }
}, { immediate: true })

// 组件卸载时清理
onUnmounted(() => {
  pauseAnimation()
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
  document.removeEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.removeEventListener('msfullscreenchange', handleFullscreenChange)
})
</script>

<style scoped>
.encapsulation-container {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 20px;
  height: 100%;
  overflow-y: auto;
  position: relative;
}

.encapsulation-container.fullscreen {
  background: #f5f7fa;
  padding: 40px;
  display: flex;
  flex-direction: column;
}

.encap-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #1a73e8;
}

.encap-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
}

.fullscreen .encap-header h3 {
  font-size: 24px;
}

/* 动画状态 */
.animation-status {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border: 2px solid #1a73e8;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-label {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.status-label.transmitting {
  color: #ff9800;
  display: flex;
  align-items: center;
  gap: 6px;
  animation: pulse 1s ease-in-out infinite;
}

.status-value {
  font-size: 16px;
  font-weight: 700;
  color: #1a73e8;
}

/* 双节点布局 */
.dual-node-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.nodes-wrapper {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: 20px;
  align-items: start;
}

.fullscreen .nodes-wrapper {
  gap: 40px;
  padding: 20px 0;
}

/* 节点部分 */
.node-section {
  background: #fff;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  min-height: 500px;
  display: flex;
  flex-direction: column;
}

.fullscreen .node-section {
  min-height: 600px;
  padding: 30px;
}

.sender-section {
  border-color: #1a73e8;
  background: linear-gradient(135deg, #ffffff 0%, #e3f2fd 100%);
}

.receiver-section {
  border-color: #4caf50;
  background: linear-gradient(135deg, #ffffff 0%, #e8f5e9 100%);
}

.node-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid currentColor;
}

.sender-section .node-header {
  color: #1a73e8;
}

.receiver-section .node-header {
  color: #4caf50;
}

.node-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: currentColor;
  color: #fff;
  border-radius: 8px;
}

.fullscreen .node-icon {
  width: 64px;
  height: 64px;
}

.node-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.fullscreen .node-info h4 {
  font-size: 20px;
}

.node-info p {
  margin: 0;
  font-size: 12px;
  color: #666;
}

.fullscreen .node-info p {
  font-size: 14px;
}

/* 层堆栈 */
.layers-stack {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.fullscreen .layers-stack {
  gap: 12px;
}

.layer-box {
  position: relative;
  background: #fff;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  padding: 12px;
  opacity: 0.3;
  transform: translateX(-20px);
  transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.fullscreen .layer-box {
  padding: 16px 20px;
}

.layer-box.active {
  opacity: 1;
  transform: translateX(0);
  border-color: #1a73e8;
  box-shadow: 0 2px 8px rgba(26, 115, 232, 0.2);
}

.receiver-section .layer-box.active {
  border-color: #4caf50;
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.2);
}

.layer-box.processing {
  border-color: #ff9800;
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  box-shadow: 0 4px 16px rgba(255, 152, 0, 0.4);
  animation: pulse 1.5s ease-in-out infinite;
}

.layer-box.completed {
  border-color: #4caf50;
  background: #f1f8e9;
}

@keyframes pulse {
  0%, 100% {
    transform: translateX(0) scale(1);
  }
  50% {
    transform: translateX(0) scale(1.02);
  }
}

.layer-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.layer-number {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1a73e8;
  color: #fff;
  font-weight: 700;
  font-size: 14px;
  border-radius: 8px;
  flex-shrink: 0;
}

.fullscreen .layer-number {
  width: 50px;
  height: 50px;
  font-size: 16px;
}

.layer-box.processing .layer-number {
  background: #ff9800;
}

.layer-box.completed .layer-number {
  background: #4caf50;
}

.layer-details {
  flex: 1;
}

.layer-name {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 2px;
}

.fullscreen .layer-name {
  font-size: 16px;
}

.layer-size {
  font-size: 12px;
  color: #666;
}

.fullscreen .layer-size {
  font-size: 14px;
}

.layer-spinner {
  margin-left: auto;
}

/* 封装/解封装效果 */
.wrapping-effect,
.unwrapping-effect {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  overflow: hidden;
  border-radius: 6px;
}

.wrap-line,
.unwrap-line {
  position: absolute;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent 0%, #ff9800 50%, transparent 100%);
  animation: wrapAnimation 1.5s ease-in-out infinite;
}

.wrap-line:nth-child(1) { top: 20%; }
.wrap-line:nth-child(2) { top: 50%; }
.wrap-line:nth-child(3) { top: 80%; }

.unwrap-line:nth-child(1) { top: 20%; }
.unwrap-line:nth-child(2) { top: 50%; }
.unwrap-line:nth-child(3) { top: 80%; }

@keyframes wrapAnimation {
  0% {
    transform: translateX(-100%);
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    transform: translateX(100%);
    opacity: 0;
  }
}

/* 传输通道 */
.transmission-channel {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 20px 0;
}

.channel-line {
  position: relative;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #1a73e8 0%, #4caf50 100%);
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(26, 115, 232, 0.3);
}

.fullscreen .channel-line {
  height: 6px;
  min-width: 150px;
}

.data-packet {
  position: absolute;
  top: 50%;
  left: 0;
  transform: translate(-50%, -50%);
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  box-shadow: 0 4px 16px rgba(255, 152, 0, 0.5);
  z-index: 10;
}

.fullscreen .data-packet {
  width: 80px;
  height: 80px;
}

.data-packet.moving {
  animation: packetMove 1.5s ease-in-out forwards;
}

@keyframes packetMove {
  0% {
    left: 0;
  }
  100% {
    left: 100%;
  }
}

.packet-icon {
  font-size: 24px;
}

.fullscreen .packet-icon {
  font-size: 32px;
}

.packet-info {
  font-size: 10px;
  font-weight: 600;
  margin-top: 2px;
}

.fullscreen .packet-info {
  font-size: 12px;
}

.channel-label {
  font-size: 13px;
  font-weight: 600;
  color: #666;
  padding: 6px 12px;
  background: #fff;
  border: 2px solid #e0e0e0;
  border-radius: 16px;
}

.fullscreen .channel-label {
  font-size: 16px;
  padding: 8px 16px;
}

/* 完成消息 */
.completion-message {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 20px;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  border: 2px solid #4caf50;
  border-radius: 12px;
  font-size: 16px;
  color: #2e7d32;
  animation: slideIn 0.5s ease;
}

.fullscreen .completion-message {
  font-size: 20px;
  padding: 30px;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.completion-message strong {
  font-weight: 700;
  color: #1b5e20;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: #999;
}

.empty-state p {
  margin-top: 16px;
  font-size: 14px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .nodes-wrapper {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .transmission-channel {
    transform: rotate(90deg);
    margin: 20px 0;
  }
}
</style>
