// 班次相关接口

import request from "@/utils/request";

// 绩效管理-班次-分页查询
export function page(query) {
  return request({
    url: "/performanceShift/page",
    method: "get",
    params: query,
  });
}

// 绩效管理-班次-年份分页查询
export function pageYear(query) {
  return request({
    url: "/performanceShift/pageYear",
    method: "get",
    params: query,
  });
}

// 绩效管理-班次-排班
export function add(data) {
  return request({
    url: "/performanceShift/add",
    method: "post",
    data: data,
  });
}

// 绩效管理-班次-时间配置-查询时间配置信息
export function list(query) {
  return request({
    url: "/shiftTime/list",
    method: "get",
    params: query,
  });
}

// 绩效管理-班次-时间配置-新增
export function shiftAdd(data) {
  return request({
    url: "/shiftTime/add",
    method: "post",
    data: data,
  });
}

// 绩效管理-班次-时间配置-修改
export function shiftUpdate(data) {
  return request({
    url: "/shiftTime/update",
    method: "post",
    data: data,
  });
}

// 绩效管理-班次-时间配置-删除
export function shiftRemove(query) {
  return request({
    url: "/shiftTime/remove",
    method: "delete",
    params: query,
  });
}

// 绩效管理-班次-导出
export function exportFile(query) {
  return request({
    url: "/performanceShift/export",
    method: "get",
    params: query,
  });
}

// 绩效管理-班次-导出
export function obtainItemParameterList(query) {
  return request({
    url: "/laboratoryScope/obtainItemParameterList",
    method: "get",
    params: query,
  });
}

// 绩效管理-班次-班次状态修改
export function update(data) {
  return request({
    url: "/performanceShift/update",
    method: "post",
    data: data,
  });
}

// 获取用户列表
export function selectUserCondition(query) {
  return request({
    url: "/system/newUser/selectUserCondition",
    method: "get",
    params: query,
  });
}
