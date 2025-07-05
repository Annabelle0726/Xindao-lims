// 工时管理相关接口

import request from "@/utils/request";

// 查询工时汇总
export function selectAuxiliaryAllByMonth(query) {
  return request({
    url: "/auxiliaryOriginalHours/selectAuxiliaryAllByMonth",
    method: "get",
    params: query,
  });
}

// 查询辅助工时
export function selectAuxiliaryWorkingHoursDay(query) {
  return request({
    url: "/auxiliaryWorkingHoursDay/selectAuxiliaryWorkingHoursDay",
    method: "get",
    params: query,
  });
}

// 查询产量工时
export function selectAuxiliaryOutputWorkingHours(query) {
  return request({
    url: "/auxiliaryOutputWorkingHours/selectAuxiliaryOutputWorkingHours",
    method: "get",
    params: query,
  });
}

// 删除辅助工时
export function deleteAuxiliaryWorkingHoursDay(query) {
  return request({
    url: "/auxiliaryWorkingHoursDay/deleteAuxiliaryWorkingHoursDay",
    method: "delete",
    params: query,
  });
}

// 新增辅助工时
export function insertAuxiliaryWorkingHoursDay(data) {
  return request({
    url: "/auxiliaryWorkingHoursDay/insertAuxiliaryWorkingHoursDay",
    method: "post",
    data: data,
  });
}

// 修改辅助工时
export function updateAuxiliaryWorkingHoursDay(data) {
  return request({
    url: "/auxiliaryWorkingHoursDay/updateAuxiliaryWorkingHoursDay",
    method: "post",
    data: data,
  });
}

// 辅助工时--批准
export function approve(data) {
  return request({
    url: "/auxiliaryWorkingHoursDay/approve",
    method: "post",
    data: data,
  });
}

// 统计产量工时汇总和辅助工时汇总
export function collectWorkingHours(query) {
  return request({
    url: "/auxiliaryOutputWorkingHours/collectWorkingHours",
    method: "get",
    params: query,
  });
}

// 根据编号当前用户信息查询所在班次
export function selectshiftByUser(query) {
  return request({
    url: "/auxiliaryWorkingHoursDay/selectshiftByUser",
    method: "get",
    params: query,
  });
}

// 根据编号查询辅助工时配置信息
export function selectAuxiliaryWorkingHoursByNumber(query) {
  return request({
    url: "/auxiliaryWorkingHoursDay/selectAuxiliaryWorkingHoursByNumber",
    method: "get",
    params: query,
  });
}

// 辅助工时导出
export function exportAssistantHours(data) {
  return request({
    url: "/auxiliaryWorkingHoursDay/exportAssistantHours",
    method: "post",
    data: data,
  });
}

// 产量工时导出
export function exportOutputHours(data) {
  return request({
    url: "/auxiliaryOutputWorkingHours/exportOutputHours",
    method: "post",
    data: data,
  });
}

// 查询辅助工时
export function selectAuxiliaryWorkingHours(query) {
  return request({
    url: "/auxiliaryWorkingHours/selectAuxiliaryWorkingHours",
    method: "get",
    params: query,
  });
}

// 获取实验室名称
export function obtainItemParameterList(query) {
  return request({
    url: "/laboratoryScope/obtainItemParameterList",
    method: "get",
    params: query,
  });
}

// 新增辅助工时
export function insertAuxiliaryWorkingHours(data) {
  return request({
    url: "/auxiliaryWorkingHours/insertAuxiliaryWorkingHours",
    method: "post",
    data: data,
  });
}

// 修改辅助工时
export function upAuxiliaryWorkingHours(data) {
  return request({
    url: "/auxiliaryWorkingHours/upAuxiliaryWorkingHours",
    method: "post",
    data: data,
  });
}

// 删除辅助工时
export function deleteAuxiliaryWorkingHours(query) {
  return request({
    url: "/auxiliaryWorkingHours/deleteAuxiliaryWorkingHours",
    method: "delete",
    params: query,
  });
}
