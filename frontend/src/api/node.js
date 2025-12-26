import request from '../utils/request'

// 获取所有节点
function getAllNodes() {
  return request({
    url: '/nodes',
    method: 'get'
  })
}

// 获取节点详情
function getNodeById(nodeId) {
  return request({
    url: `/nodes/${nodeId}`,
    method: 'get'
  })
}

// 创建节点
function createNode(data) {
  return request({
    url: '/nodes',
    method: 'post',
    data
  })
}

// 更新节点
function updateNode(nodeId, data) {
  return request({
    url: `/nodes/${nodeId}`,
    method: 'put',
    data
  })
}

// 删除节点
function deleteNode(nodeId) {
  return request({
    url: `/nodes/${nodeId}`,
    method: 'delete'
  })
}

export const nodeApi = {
  getAllNodes,
  getNodeById,
  createNode,
  updateNode,
  deleteNode
}
