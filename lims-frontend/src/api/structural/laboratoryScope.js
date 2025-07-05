import request from '@/utils/request'

// 获取实验室名称
export function obtainItemParameterList() {
  return request({
    url: '/laboratoryScope/obtainItemParameterList',
    method: 'get'
  })
}
// 查询实验室管理列表
export function selectItemParameter(query) {
  return request({
    url: '/laboratoryScope/selectItemParameter',
    method: 'get',
    params: query
  })
}
// 添加实验室参数
export function addParameter(query) {
  return request({
    url: '/laboratoryScope/addParameter',
    method: 'post',
    data: query
  })
}
// 修改实验室参数
export function upParameter(query) {
  return request({
    url: '/laboratoryScope/upParameter',
    method: 'post',
    data: query
  })
}
// 删除实验室参数
export function delParameter(query) {
  return request({
    url: '/laboratoryScope/delParameter',
    method: 'delete',
    params: query
  })
}
// 查询印章列表
export function selectSeal(query) {
  return request({
    url: '/sealScope/selectSeal',
    method: 'get',
    params: query
  })
}
// 查询印章列表
export function addSeal(query) {
  return request({
    url: '/sealScope/addSeal',
    method: 'post',
    data: query
  })
}
// 查询资质明细列表
export function getCertificationDetail(query) {
  return request({
    url: '/certification/getCertificationDetail',
    method: 'get',
    params: query
  })
}
