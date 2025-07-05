// 样品管理页面相关接口
import request from '@/utils/request'

// 查询仓库
export function selectWarehouse() {
  return request({
    url: '/warehouse/selectWarehouse',
    method: 'get'
  })
}
// 样品入库
export function inWarehouse(query) {
  return request({
    url: '/warehouse/inWarehouse',
    method: 'post',
    data: query
  })
}
// 样品出库
export function outWarehouse(query) {
  return request({
    url: '/warehouse/outWarehouse',
    method: 'post',
    data: query
  })
}
// 修改仓库
export function upWarehouse(query) {
  return request({
    url: '/warehouse/upWarehouse',
    method: 'post',
    data: query
  })
}
// 添加仓库
export function addWarehouse(query) {
  return request({
    url: '/warehouse/addWarehouse',
    method: 'post',
    data: query
  })
}
// 修改货架
export function upShelf(query) {
  return request({
    url: '/warehouse/upShelf',
    method: 'post',
    data: query
  })
}
// 添加货架
export function addShelf(query) {
  return request({
    url: '/warehouse/addShelf',
    method: 'post',
    data: query
  })
}
// 删除仓库
export function delWarehouse(query) {
  return request({
    url: '/warehouse/delWarehouse',
    method: 'delete',
    params: query
  })
}
// 删除货架
export function delShelf(query) {
  return request({
    url: '/warehouse/delShelf',
    method: 'delete',
    params: query
  })
}
// 查询货架下的存放信息
export function getWarehouse(query) {
  return request({
    url: '/warehouse/getWarehouse',
    method: 'get',
    params: query
  })
}
// 查询货架下的存放信息
export function searchSampleId(query) {
  return request({
    url: '/warehouse/searchSampleId',
    method: 'post',
    data: query
  })
}
