// 报告编制页面相关接口
import request from '@/utils/request'

// 报告还原
export function upReportUrl(query) {
  return request({
    url: '/insReport/upReportUrl',
    method: 'post',
    data: query
  })
}
// 查询检验报告数据
export function pageInsReport(query) {
  return request({
    url: '/insReport/pageInsReport',
    method: 'get',
    params: query
  })
}
// 报告批量下载
export function downAll(query) {
  return request({
    url: '/insReport/downAll',
    method: 'get',
    params: query
  })
}
// 报告退回到检验任务
export function sendBackTask(query) {
  return request({
    url: '/insReport/sendBackTask',
    method: 'post',
    data: query
  })
}
// 报告审核
export function examineReport(query) {
  return request({
    url: '/insReport/examineReport',
    method: 'post',
    data: query
  })
}
// 报告提交
export function writeReport(query) {
  return request({
    url: '/insReport/writeReport',
    method: 'post',
    data: query
  })
}
// 报告批准
export function ratifyReport(query) {
  return request({
    url: '/insReport/ratifyReport',
    method: 'post',
    data: query
  })
}
