import request from '@/utils/request'

// 查询客户列表
export function selectCustomPageList(query) {
  return request({
    url: '/system/custom/selectCustomPageList',
    method: 'get',
    params: query
  })
}
// 新增客户
export function addCustom(query) {
  return request({
    url: '/system/custom/addCustom',
    method: 'post',
    data: query
  })
}
// 修改客户
export function upCustom(query) {
  return request({
    url: '/system/custom/upCustom',
    method: 'post',
    data: query
  })
}
// 删除客户
export function delCustomById(query) {
  return request({
    url: '/system/custom/delCustomById',
    method: 'delete',
    params: query
  })
}
