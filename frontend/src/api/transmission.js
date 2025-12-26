import request from '../utils/request'

// 开始传输
function startTransmission(data) {
  return request({
    url: '/transmission/start',
    method: 'post',
    data
  })
}

// 获取传输状态
function getTransmissionStatus(transmissionId) {
  return request({
    url: `/transmission/${transmissionId}`,
    method: 'get'
  })
}

// 获取传输历史
function getHistory(params) {
  return request({
    url: '/transmission/history',
    method: 'get',
    params
  })
}

// 获取跳转详情
function getTransmissionHops(transmissionId) {
  return request({
    url: `/transmission/${transmissionId}/hops`,
    method: 'get'
  })
}

export const transmissionApi = {
  startTransmission,
  getTransmissionStatus,
  getHistory,
  getTransmissionHops
}
