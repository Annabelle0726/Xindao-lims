// 统计图表
import request from "@/utils/request";

//合格率统计
export function getRawPassRateByBarChart(query) {
  return request({
    url: "/dataAnalysis/getRawPassRateByBarChart",
    method: "get",
    params: query,
  });
}
//原材料统计
export function getRawPassRateByCake(query) {
  return request({
    url: "/dataAnalysis/getRawPassRateByCake",
    method: "get",
    params: query,
  });
}
//本月检验类型数量
export function getOrderTypeCookie() {
  return request({
    url: "/dataAnalysis/getOrderTypeCookie",
    method: "get",
  });
}
//本月检验类型数量
export function getRawUpMonth() {
  return request({
    url: "/dataAnalysis/getRawUpMonth",
    method: "get",
  });
}
//查询原材料项检分析
export function getRawProductAnalysis(query) {
  return request({
    url: "/dataAnalysis/getRawProductAnalysis",
    method: "post",
    data: query,
  });
}
//查询原材料项检分析合格率
export function getRawProductAnalysisRawPass(query) {
  return request({
    url: "/dataAnalysis/getRawProductAnalysisRawPass",
    method: "post",
    data: query,
  });
}
//查询原材料项检分析列表
export function getRawProductAnalysisAllList(query) {
  return request({
    url: "/dataAnalysis/getRawProductAnalysisAllList",
    method: "post",
    data: query,
  });
}
//查询检验项
export function getRawItemNames(query) {
  return request({
    url: "/dataAnalysis/getRawItemNames",
    method: "get",
    params: query,
  });
}
//查询原材料项检和厂家数据对比
export function getRawSupplierCompare(query) {
  return request({
    url: "/dataAnalysis/getRawSupplierCompare",
    method: "post",
    data: query,
  });
}
//查询预警列表
export function selectDeviationWarningPage(query) {
  return request({
    url: "/insProductDeviationWarning/selectDeviationWarningPage",
    method: "get",
    params: query,
  });
}
//查询预警列表
export function selectDeviationWarning(query) {
  return request({
    url: "/insProductDeviationWarning/selectDeviationWarning",
    method: "get",
    params: query,
  });
}
