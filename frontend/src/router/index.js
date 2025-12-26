import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layout/MainLayout.vue'
import Home from '@/views/Home.vue'
import Simulation from '@/views/Simulation.vue'
import TopologyManage from '@/views/TopologyManage.vue'
import History from '@/views/History.vue'
import Statistics from '@/views/Statistics.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'home',
        component: Home
      },
      {
        path: 'simulation',
        name: 'simulation',
        component: Simulation
      },
      {
        path: 'topology',
        name: 'topology',
        component: TopologyManage
      },
      {
        path: 'history',
        name: 'history',
        component: History
      },
      {
        path: 'statistics',
        name: 'statistics',
        component: Statistics
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
