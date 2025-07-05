// 抽样信息详情页面相关接口
import request from '@/utils/request'

// 抽样信息-季度查询
export function getQuarterPage(query) {
  return request({
    url: '/finishProductSpotCheck/getQuarterPage',
    method: 'get',
    params: query
  })
}
// 抽样信息-年度查询
export function getSpotCheckYearPage(query) {
  return request({
    url: '/finishProductSpotCheck/getSpotCheckYearPage',
    method: 'get',
    params: query
  })
}
// 删除季度抽检
export function deleteQuarter(query) {
  return request({
    url: '/finishProductSpotCheck/deleteQuarter',
    method: 'delete',
    params: query
  })
}
// 生成季度报告
export function finalReportQuarter(query) {
  return request({
    url: '/finishProductSpotCheck/finalReportQuarter',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 生成年度报告
export function finalReportSpotCheckYear(query) {
  return request({
    url: '/finishProductSpotCheck/finalReportSpotCheckYear',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 删除年度抽检
export function deleteSpotCheckYear(query) {
  return request({
    url: '/finishProductSpotCheck/deleteSpotCheckYear',
    method: 'delete',
    params: query,
  })
}
// 年度抽检查看详情
export function getSpotCheckYear(query) {
  return request({
    url: '/finishProductSpotCheck/getSpotCheckYear',
    method: 'get',
    params: query,
  })
}
// 提交年度抽样
export function addSpotCheckYear(query) {
  return request({
    url: '/finishProductSpotCheck/addSpotCheckYear',
    method: 'post',
    data: query,
  })
}
// 编辑年度抽样
export function updateSpotCheckYear(query) {
  return request({
    url: '/finishProductSpotCheck/updateSpotCheckYear',
    method: 'post',
    data: query,
  })
}
// 季度抽检查看详情
export function getQuarter(query) {
  return request({
    url: '/finishProductSpotCheck/getQuarter',
    method: 'get',
    params: query,
  })
}
// 提交季度抽样
export function addQuarter(query) {
  return request({
    url: '/finishProductSpotCheck/addQuarter',
    method: 'post',
    data : query,
  })
}
// 编辑季度检验
export function updateQuarterOnOrder(query) {
  return request({
    url: '/finishProductSpotCheck/updateQuarterOnOrder',
    method: 'post',
    data : query,
  })
}
