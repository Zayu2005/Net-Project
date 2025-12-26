<template>
  <t-layout class="layout">
    <!-- 侧边导航 -->
    <t-aside :width="240">
      <div class="sidebar">
        <div class="logo">
          <span class="logo-text">网络模拟系统</span>
        </div>
        <t-menu
          :value="activeMenu"
          :theme="'light'"
          @change="handleMenuChange"
        >
          <t-menu-item value="home">
            <template #icon>
              <t-icon name="home" />
            </template>
            首页
          </t-menu-item>
          <t-menu-item value="simulation">
            <template #icon>
              <t-icon name="play-circle" />
            </template>
            网络模拟
          </t-menu-item>
          <t-menu-item value="topology">
            <template #icon>
              <t-icon name="network" />
            </template>
            拓扑管理
          </t-menu-item>
          <t-menu-item value="history">
            <template #icon>
              <t-icon name="history" />
            </template>
            历史记录
          </t-menu-item>
          <t-menu-item value="statistics">
            <template #icon>
              <t-icon name="chart-bar" />
            </template>
            统计分析
          </t-menu-item>
        </t-menu>
      </div>
    </t-aside>

    <!-- 主内容区 -->
    <t-layout>
      <!-- 顶部栏 -->
      <t-header class="header">
        <div class="header-left">
          <h1 class="header-title">{{ pageTitle }}</h1>
        </div>
        <div class="header-right">
          <t-space>
            <t-tag theme="primary" variant="light">v1.0.0</t-tag>
            <t-button theme="default" variant="text">
              <t-icon name="setting" />
            </t-button>
          </t-space>
        </div>
      </t-header>

      <!-- 内容区域 -->
      <t-content class="content">
        <router-view />
      </t-content>
    </t-layout>
  </t-layout>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const activeMenu = ref('home')

const pageTitles = {
  home: '首页',
  simulation: '网络模拟',
  topology: '拓扑管理',
  history: '历史记录',
  statistics: '统计分析'
}

const pageTitle = computed(() => {
  return pageTitles[activeMenu.value] || '网络模拟系统'
})

watch(() => route.name, (newName) => {
  if (newName) {
    activeMenu.value = newName
  }
}, { immediate: true })

const handleMenuChange = (value) => {
  activeMenu.value = value
  router.push({ name: value })
}
</script>

<style scoped>
.layout {
  height: 100vh;
  width: 100%;
}

.sidebar {
  height: 100vh;
  background: #fff;
  border-right: 1px solid #ddd;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 2px solid #000;
  background: #000;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
}

.header {
  height: 64px;
  background: #fff;
  border-bottom: 1px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #000;
  margin: 0;
}

.content {
  background: #f5f5f5;
  padding: 24px;
  overflow-y: auto;
  height: calc(100vh - 64px);
}

/* TDesign Menu 样式覆盖 */
:deep(.t-menu) {
  border: none;
}

:deep(.t-menu-item) {
  border-left: 3px solid transparent;
  margin: 4px 0;
}

:deep(.t-menu-item.t-is-active) {
  background: #e8f2ff !important;
  border-left-color: #0052d9;
  font-weight: 600;
}

:deep(.t-menu-item:hover) {
  background: #f5f5f5;
}
</style>
