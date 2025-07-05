// 成品抽样详情页面相关接口
import request from '@/utils/request'

// 原材料根据id报检
export function getIfsStock(query) {
  return request({
    url: '/finishProductSpotCheck/getIfsStock',
    method: 'post',
    data: query
  })
}
// 季度抽检查看详情
export function getQuarter(query) {
  return request({
    url: '/finishProductSpotCheck/getQuarter',
    method: 'get',
    params: query
  })
}
// 提交季度抽样
export function addQuarter(query) {
  return request({
    url: '/finishProductSpotCheck/addQuarter',
    method: 'post',
    data: query
  })
}
// 编辑季度检验
export function updateQuarterOnOrder(query) {
  return request({
    url: '/finishProductSpotCheck/updateQuarterOnOrder',
    method: 'post',
    data: query
  })
}
