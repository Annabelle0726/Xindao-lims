// 原始记录模板相关接口

import request from "@/utils/request";

// 查询客户列表
export function selectStandardTemplatePageList(query) {
  return request({
    url: "/StandardTemplate/selectStandardTemplatePageList",
    method: "get",
    params: query,
  });
}

// 模版复制
export function copyStandardTemplate(data) {
  return request({
    url: "/StandardTemplate/copyStandardTemplate",
    method: "post",
    data: data,
  });
}

// 添加检验模板
export function addStandardTemplate(data) {
  return request({
    url: "/StandardTemplate/addStandardTemplate",
    method: "post",
    data: data,
  });
}

// 修改检验模板
export function upStandardTemplate(data) {
  return request({
    url: "/StandardTemplate/upStandardTemplate",
    method: "post",
    data: data,
  });
}

// 删除检验模板
export function delStandardTemplate(query) {
  return request({
    url: "/StandardTemplate/delStandardTemplate",
    method: "delete",
    params: query,
  });
}

// 查询模板
export function getEditTemplatePreparation(query) {
  return request({
    url: "/StandardTemplate/getEditTemplatePreparation",
    method: "get",
    params: query,
  });
}
