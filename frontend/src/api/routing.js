import request from '../utils/request'

// 计算路由（统一接口）
function calculate(algorithm, data) {
  return request({
    url: `/routing/${algorithm}`,
    method: 'post',
    data
  })
}

// 计算最短路径 (Dijkstra)
function calculateDijkstra(data) {
  return request({
    url: '/routing/dijkstra',
    method: 'post',
    data
  })
}

// 计算最短路径 (OSPF)
function calculateOSPF(data) {
  return request({
    url: '/routing/ospf',
    method: 'post',
    data
  })
}

// 计算最短路径 (RIP)
function calculateRIP(data) {
  return request({
    url: '/routing/rip',
    method: 'post',
    data
  })
}

// 计算最短路径 (BGP)
function calculateBGP(data) {
  return request({
    url: '/routing/bgp',
    method: 'post',
    data
  })
}

// 获取路由表
function getRoutingTable(nodeId) {
  return request({
    url: `/routing/table/${nodeId}`,
    method: 'get'
  })
}

export const routingApi = {
  calculate,
  calculateDijkstra,
  calculateOSPF,
  calculateRIP,
  calculateBGP,
  getRoutingTable
}
