import request from '../utils/request'

// 获取所有链路
function getAllLinks() {
  return request({
    url: '/links',
    method: 'get'
  })
}

// 获取链路详情
function getLinkById(linkId) {
  return request({
    url: `/links/${linkId}`,
    method: 'get'
  })
}

// 创建链路
function createLink(data) {
  return request({
    url: '/links',
    method: 'post',
    data
  })
}

// 更新链路
function updateLink(linkId, data) {
  return request({
    url: `/links/${linkId}`,
    method: 'put',
    data
  })
}

// 删除链路
function deleteLink(linkId) {
  return request({
    url: `/links/${linkId}`,
    method: 'delete'
  })
}

// 获取完整拓扑
function getTopology() {
  return request({
    url: '/topology',
    method: 'get'
  })
}

export const linkApi = {
  getAllLinks,
  getLinkById,
  createLink,
  updateLink,
  deleteLink,
  getTopology
}
