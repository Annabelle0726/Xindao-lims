// 费用统计页面相关接口
import request from '@/utils/request'

// 查询订单列表
export function selectRatesPage(query) {
  return request({
    url: '/insOrderRates/selectRatesPage',
    method: 'get',
    params: query
  })
}
// 查询订单费用详情
export function selectRatesDetail(query) {
  return request({
    url: '/insOrderRates/selectRatesDetail',
    method: 'get',
    params: query
  })
}
