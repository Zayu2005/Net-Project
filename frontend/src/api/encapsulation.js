import request from '../utils/request'

// 执行封装
function encapsulate(data) {
  return request({
    url: '/encapsulation',
    method: 'post',
    data
  })
}

// 执行解封装
function decapsulate(transmissionId) {
  return request({
    url: `/encapsulation/decapsulate/${transmissionId}`,
    method: 'post'
  })
}

// 获取协议层详情
function getLayerDetails(transmissionId) {
  return request({
    url: `/encapsulation/${transmissionId}`,
    method: 'get'
  })
}

export const encapsulationApi = {
  encapsulate,
  decapsulate,
  getLayerDetails
}
