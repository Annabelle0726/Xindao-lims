import request from '@/utils/request'

// 查询资质明细列表
export function getCertificationDetail(query) {
  return request({
    url: '/certification/getCertificationDetail',
    method: 'get',
    params: query
  })
}
// 添加资质明细列表
export function addCertificationDetail(query) {
  return request({
    url: '/certification/addCertificationDetail',
    method: 'post',
    data: query
  })
}
// 删除资质明细列表
export function delCertificationDetail(query) {
  return request({
    url: '/certification/delCertificationDetail',
    method: 'delete',
    params: query
  })
}
