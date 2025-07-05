import request from "@/utils/request";

// 查询人员培训
export function personTraining(query) {
  return request({
    url: "/personTraining/personTrainingSelect",
    method: "get",
    params: query
  });
}
// 查询培训计划年度计划明细表
export function queryTheAnnualPlanDetailsTable(query) {
  return request({
    url: "/personTraining/queryTheAnnualPlanDetailsTable",
    method: "get",
    params: query
  });
}
// 删除人员培训信息
export function personTrainingDelete(query) {
  return request({
    url: "/personTraining/personTrainingDelete",
    method: "delete",
    params: query
  });
}
// 审核 年度人员培训
export function reviewAnnualPersonnelTraining(query) {
  return request({
    url: "/personTraining/reviewAnnualPersonnelTraining",
    method: "post",
    data: query
  });
}
// 批准 年度人员培训
export function approveAnnualPersonnelTraining(query) {
  return request({
    url: "/personTraining/approveAnnualPersonnelTraining",
    method: "post",
    data: query
  });
}
// 导出人员培训
export function exportPersonTraining(query) {
  return request({
    url: "/personTraining/exportPersonTraining",
    method: "get",
    params: query,
    responseType: "blob"
  });
}
// 导出人员培训与考核记录
export function exportPersonTrainingRecord(query) {
  return request({
    url: "/personTraining/exportPersonTrainingRecord",
    method: "get",
    params: query,
    responseType: "blob"
  });
}
// 批量删除 年度计划明细表
export function deleteAnnualPlanDetailTable(query) {
  return request({
    url: "/personTraining/deleteAnnualPlanDetailTable",
    method: "delete",
    params: query
  });
}
// 批量删除 年度计划明细表
export function addOrUpdatePersonTrainingDetailed(query) {
  return request({
    url: "/personTraining/addOrUpdatePersonTrainingDetailed",
    method: "post",
    data: query
  });
}
// 培训与考核记录 查询
export function trainingAndAssessmentRecordsPage(query) {
  return request({
    url: "/personTraining/trainingAndAssessmentRecordsPage",
    method: "get",
    params: query
  });
}
// 培训与考核 失焦更新
export function outOfFocusPreservation(query) {
  return request({
    url: "/personTraining/outOfFocusPreservation",
    method: "post",
    data: query
  });
}
// 培训与考核 新增人员
export function newPersonnelAddedToTrainingRecords(query) {
  return request({
    url: "/personTraining/newPersonnelAddedToTrainingRecords",
    method: "post",
    data: query
  });
}
// 培训与考核 删除人员
export function deleteTrainingAndAssessmentRecords(query) {
  return request({
    url: "/personTraining/deleteTrainingAndAssessmentRecords",
    method: "delete",
    params: query
  });
}
// 培训与考核记录 提交
export function trainingAndAssessmentRecordsAdded(query) {
  return request({
    url: "/personTraining/trainingAndAssessmentRecordsAdded",
    method: "post",
    data: query
  });
}
// 培训与考核记录 提交评价
export function trainingAndAssessmentRecordsEvaluate(query) {
  return request({
    url: "/personTraining/trainingAndAssessmentRecordsEvaluate",
    method: "post",
    data: query
  });
}

// 人员培训详情附件列表
export function getTrainingDetailedFileList(query) {
  return request({
    url: "/personTraining/getTrainingDetailedFileList",
    method: "get",
    params: query
  });
}
// 人员培训详情附件删除
export function delTrainingDetailedFileList(query) {
  return request({
    url: "/personTraining/delTrainingDetailedFileList",
    method: "delete",
    params: query
  });
}
