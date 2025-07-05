// 实验室的检测能力档案相关接口
import request from "@/utils/request";

// 标准查新列表
export function pageMethodSearchNew(query) {
  return request({
    url: "/processMethodSearchNew/pageMethodSearchNew",
    method: "get",
    params: query,
  });
}

// 查询存档
export function pageSearchNewArchived(query) {
  return request({
    url: "/processMethodSearchNew/pageSearchNewArchived",
    method: "get",
    params: query,
  });
}

// 新增标准查新审批流程
export function addSearchNewArchived(data) {
  return request({
    url: "/processMethodSearchNew/addSearchNewArchived",
    method: "post",
    data: data,
  });
}

// 新增标准查新
export function addMethodSearchNew(data) {
  return request({
    url: "/processMethodSearchNew/addMethodSearchNew",
    method: "post",
    data: data,
  });
}

// 修改标准查新
export function updateMethodSearchNew(data) {
  return request({
    url: "/processMethodSearchNew/updateMethodSearchNew",
    method: "post",
    data: data,
  });
}

// 存档批准
export function ratifySearchNewArchivedr(data) {
  return request({
    url: "/processMethodSearchNew/ratifySearchNewArchivedr",
    method: "post",
    data: data,
  });
}

// 查询存档备份列表
export function pageSearchNewBackups(query) {
  return request({
    url: "/processMethodSearchNew/pageSearchNewBackups",
    method: "get",
    params: query,
  });
}

// 导出标准查新
export function exportMethodSearchNew(query) {
  return request({
    url: "/processMethodSearchNew/exportMethodSearchNew",
    method: "get",
    responseType: "blob",
    params: query,
  });
}
