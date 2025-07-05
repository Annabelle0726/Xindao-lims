// 质量监控计划页面相关接口
import request from '@/utils/request'

// 监控年度计划列表
export function pageQualityMonitor(query) {
  return request({
    url: '/qualityMonitor/pageQualityMonitor',
    method: 'get',
    params: query
  })
}
// 监控计划审核
export function examineQualityMonitor(query) {
  return request({
    url: '/qualityMonitor/examineQualityMonitor',
    method: 'post',
    data: query
  })
}
// 监控计划批准
export function ratifyQualityMonitor(query) {
  return request({
    url: '/qualityMonitor/ratifyQualityMonitor',
    method: 'post',
    data: query
  })
}
// 批准完成报告
export function ratifyFinishReport(query) {
  return request({
    url: '/qualityMonitor/ratifyFinishReport',
    method: 'post',
    data: query
  })
}
// 导出监控计划
export function exportQualityMonitorDetail(query) {
  return request({
    url: '/qualityMonitor/exportQualityMonitorDetail',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 删除监控计划
export function delQualitySupervise(query) {
  return request({
    url: '/qualityMonitor/delQualitySupervise',
    method: 'delete',
    params: query
  })
}
// 监控计划详情列表
export function pageQualityMonitorDetail(query) {
  return request({
    url: '/qualityMonitor/pageQualityMonitorDetail',
    method: 'get',
    params: query
  })
}
// 质量监控实施计划导出
export function exportQualityMonitorRatify(query) {
  return request({
    url: '/qualityMonitor/exportQualityMonitorRatify',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 质量监控评价导出
export function exportQualityMonitorEvaluate(query) {
  return request({
    url: '/qualityMonitor/exportQualityMonitorEvaluate',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 删除监控计划详情
export function delQualityMonitorDetail(query) {
  return request({
    url: '/qualityMonitor/delQualityMonitorDetail',
    method: 'delete',
    params: query
  })
}
// 新增监控计划详情
export function addQualityMonitorDetail(query) {
  return request({
    url: '/qualityMonitor/addQualityMonitorDetail',
    method: 'post',
    data: query
  })
}
// 修改监控计划详情
export function updateQualityMonitorDetail(query) {
  return request({
    url: '/qualityMonitor/updateQualityMonitorDetail',
    method: 'post',
    data: query
  })
}
// 查询监控计划详情实施信息
export function getQualityMonitorRatify(query) {
  return request({
    url: '/qualityMonitor/getQualityMonitorRatify',
    method: 'get',
    params: query
  })
}
// 新增监控计划详情实施信息
export function addQualityMonitorRatify(query) {
  return request({
    url: '/qualityMonitor/addQualityMonitorRatify',
    method: 'post',
    data: query
  })
}
// 提交监控计划详情批准意见
export function addQualityMonitorRatifyOpinion(query) {
  return request({
    url: '/qualityMonitor/addQualityMonitorRatifyOpinion',
    method: 'post',
    data: query
  })
}
// 查询监控评价
export function getQualityMonitorEvaluate(query) {
  return request({
    url: '/qualityMonitor/getQualityMonitorEvaluate',
    method: 'get',
    params: query
  })
}
// 新增监控评价批准
export function addMonitorEvaluateOpinion(query) {
  return request({
    url: '/qualityMonitor/addMonitorEvaluateOpinion',
    method: 'post',
    data: query
  })
}
// 新增监控评价
export function addQualityMonitorEvaluate(query) {
  return request({
    url: '/qualityMonitor/addQualityMonitorEvaluate',
    method: 'post',
    data: query
  })
}

// 查询监控评价附件列表
export function getEvaluateFileList(query) {
  return request({
    url: "/qualityMonitor/getEvaluateFileList",
    method: "get",
    params: query,
  });
}

// 删除监控评价附件列表
export function delVerifyEvaluateFileList(query) {
  return request({
    url: '/qualityMonitor/delVerifyEvaluateFileList',
    method: 'delete',
    params: query
  })
}
