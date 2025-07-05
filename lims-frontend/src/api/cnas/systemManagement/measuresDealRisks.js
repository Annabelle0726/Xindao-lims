// 应对风险的措施相关接口
import request from "@/utils/request";

//危险因素辨识与风险评价结果一览 分页
export function getPageResults(query) {
  return request({
    url: "/manageRiskAssessmentResults/getPageResults",
    method: "get",
    params: query,
  });
}

// 危险因素辨识与风险评价结果一览 审批
export function dangerousRiskApproval(data) {
  return request({
    url: "/manageRiskAssessmentResults/dangerousRiskApproval",
    method: "post",
    data: data,
  });
}

// 危险因素辨识与风险评价结果一览 批准
export function hazardIdentificationAndRiskApproval(data) {
  return request({
    url: "/manageRiskAssessmentResults/hazardIdentificationAndRiskApproval",
    method: "post",
    data: data,
  });
}

//危险因素辨识与风险评价结果一览 删除
export function removeRiskFactors(query) {
  return request({
    url: "/manageRiskAssessmentResults/removeRiskFactors",
    method: "delete",
    params: query,
  });
}

// 危险因素辨识与风险评价结果一览 新增
export function addNewRiskFactors(data) {
  return request({
    url: "/manageRiskAssessmentResults/addNewRiskFactors",
    method: "post",
    data: data,
  });
}

//危险因素辨识与风险评价结果一览 导出
export function exportHazardFactorIdentification(query) {
  return request({
    url: "/manageRiskAssessmentResults/exportHazardFactorIdentification",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//重大风险因素分析及控制计划清单 分页
export function getPageList(query) {
  return request({
    url: "/manageControlPlanList/getPageList",
    method: "get",
    params: query,
  });
}

// 重大风险因素分析及控制计划清单 审核
export function riskAnalysisApprovalOfControlPlanChecklist(data) {
  return request({
    url: "/manageControlPlanList/riskAnalysisApprovalOfControlPlanChecklist",
    method: "post",
    data: data,
  });
}

// 重大风险因素分析及控制计划清单 批准
export function approvalOfControlPlanChecklist(data) {
  return request({
    url: "/manageControlPlanList/approvalOfControlPlanChecklist",
    method: "post",
    data: data,
  });
}

//重大风险因素分析及控制计划清单 删除
export function deleteSignificantRiskFactorAnalysis(query) {
  return request({
    url: "/manageControlPlanList/deleteSignificantRiskFactorAnalysis",
    method: "delete",
    params: query,
  });
}

// 重大风险因素分析及控制计划清单 新增
export function analysisOfMajorRiskFactorsAdded(data) {
  return request({
    url: "/manageControlPlanList/analysisOfMajorRiskFactorsAdded",
    method: "post",
    data: data,
  });
}

//重大风险因素分析及控制计划清单 导出
export function exportSignificantRiskFactors(query) {
  return request({
    url: "/manageControlPlanList/exportSignificantRiskFactors",
    method: "get",
    responseType: "blob",
    params: query,
  });
}
