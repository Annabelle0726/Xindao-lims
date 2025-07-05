// 质量监督计划页面相关接口
import request from '@/utils/request'

// 质量监督年计划列表
export function pageQualitySupervise(query) {
  return request({
    url: '/qualitySupervise/pageQualitySupervise',
    method: 'get',
    params: query
  })
}
// 质量监督年计划列表
export function ratifyQualitySupervise(query) {
  return request({
    url: '/qualitySupervise/ratifyQualitySupervise',
    method: 'post',
    data: query
  })
}
// 质量监督年计划列表
export function exportQualitySupervise(query) {
  return request({
    url: '/qualitySupervise/exportQualitySupervise',
    method: 'get',
    data: query,
    responseType: "blob"
  })
}
// 删除监督计划
export function delQualitySupervise(query) {
  return request({
    url: '/qualitySupervise/delQualitySupervise',
    method: 'delete',
    params: query,
  })
}
// 质量监督年计划列表
export function pageQualitySuperviseDetail(query) {
  return request({
    url: '/qualitySupervise/pageQualitySuperviseDetail',
    method: 'get',
    params: query,
  })
}
// 导出质量监督计划记录单
export function exportSuperviseDetailRecord(query) {
  return request({
    url: '/qualitySupervise/exportSuperviseDetailRecord',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 导出不符合项
export function superviseDetailAccordingExport(query) {
  return request({
    url: '/qualitySupervise/superviseDetailAccordingExport',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 导出监督纠正措施
export function exportSuperviseDetaillCorrect(query) {
  return request({
    url: '/qualitySupervise/exportSuperviseDetaillCorrect',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 删除监督计划详情
export function delQualitySuperviseDetail(query) {
  return request({
    url: '/qualitySupervise/delQualitySuperviseDetail',
    method: 'delete',
    params: query,
  })
}
// 新增监督计划详情
export function addQualitySuperviseDetail(query) {
  return request({
    url: '/qualitySupervise/addQualitySuperviseDetail',
    method: 'post',
    data: query,
  })
}
// 修改监督计划详情
export function updateQualitySuperviseDetail(query) {
  return request({
    url: '/qualitySupervise/updateQualitySuperviseDetail',
    method: 'post',
    data: query,
  })
}
// 查询监督计划记录流程详情
export function getSuperviseDetailRecord(query) {
  return request({
    url: '/qualitySupervise/getSuperviseDetailRecord',
    method: 'get',
    params: query,
  })
}
// 新增监督记录信息
export function addSuperviseDetailRecord(query) {
  return request({
    url: '/qualitySupervise/addSuperviseDetailRecord',
    method: 'post',
    data: query,
  })
}
// 提交监督记录批准
export function addSuperviseRecordOpinion(query) {
  return request({
    url: '/qualitySupervise/addSuperviseRecordOpinion',
    method: 'post',
    data: query,
  })
}
// 获取当前监督记录的监督员列表
export function getRecordUser(query) {
  return request({
    url: '/qualitySupervise/getRecordUser',
    method: 'get',
    params: query,
  })
}
// 查询监督记录不符合控制信息
export function getSuperviseDetailAccording(query) {
  return request({
    url: '/qualitySupervise/getSuperviseDetailAccording',
    method: 'get',
    params: query,
  })
}
// (装备流程)新增监督记录不符合控制信息
export function addEquipSuperviseDetailAccording(query) {
  return request({
    url: '/qualitySupervise/addEquipSuperviseDetailAccording',
    method: 'post',
    data: query,
  })
}
// (装备流程)批准监督记录不符合控制信息
export function approverEquipSuperviseDetailAccording(query) {
  return request({
    url: '/qualitySupervise/approverEquipSuperviseDetailAccording',
    method: 'post',
    data: query,
  })
}
// 查询监督纠正处理
export function getSuperviseDetailCorrect(query) {
  return request({
    url: '/qualitySupervise/getSuperviseDetailCorrect',
    method: 'get',
    params: query,
  })
}
// (装备流程)新增监督纠正处理
export function addEquipSuperviseDetailCorrect(query) {
  return request({
    url: '/qualitySupervise/addEquipSuperviseDetailCorrect',
    method: 'post',
    data: query,
  })
}
// (装备流程)批准监督纠正处理
export function approveEquipSuperviseDetailCorrect(query) {
  return request({
    url: '/qualitySupervise/approveEquipSuperviseDetailCorrect',
    method: 'post',
    data: query,
  })
}
// (装备流程)批准监督纠正处理
export function getThisYearTrainingDetailed(query) {
  return request({
    url: '/personTraining/getThisYearTrainingDetailed',
    method: 'get'
  })
}
