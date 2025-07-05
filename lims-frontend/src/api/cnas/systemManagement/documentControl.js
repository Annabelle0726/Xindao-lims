// 体系文件控制相关接口
import request from "@/utils/request";

// 文件清单-附件上传
export function uploadFileManageDocumentList(data) {
  return request({
    url: "/manageDocumentList/uploadFileManageDocumentList",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

//文件清单-列表
export function pageManageDocumentList(query) {
  return request({
    url: "/manageDocumentList/pageManageDocumentList",
    method: "get",
    params: query,
  });
}

//文件清单-删除
export function delManageDocumentList(query) {
  return request({
    url: "/manageDocumentList/delManageDocumentList",
    method: "delete",
    params: query,
  });
}

// 文件清单-编辑
export function doManageDocumentList(data) {
  return request({
    url: "/manageDocumentList/doManageDocumentList",
    method: "post",
    data: data,
  });
}

// 文件受控-新增
export function addManageDocumentControlled(data) {
  return request({
    url: "/manageDocumentControlled/addManageDocumentControlled",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

// 文件受控-修改
export function doManageDocumentControlled(data) {
  return request({
    url: "/manageDocumentControlled/doManageDocumentControlled",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

//文件受控-获取pdf文件流
export function checkManageDocumentControlledPdf(query) {
  return request({
    url: "/manageDocumentControlled/checkManageDocumentControlledPdf",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 文件受控-审核
export function checkManageDocumentControlled(data) {
  return request({
    url: "/manageDocumentControlled/checkManageDocumentControlled",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

//文件受控-删除
export function delManageDocumentControlled(query) {
  return request({
    url: "/manageDocumentControlled/delManageDocumentControlled",
    method: "delete",
    params: query,
  });
}

//文件受控-列表
export function pageManageDocumentControlled(query) {
  return request({
    url: "/manageDocumentControlled/pageManageDocumentControlled",
    method: "get",
    params: query,
  });
}

//文件作废-列表
export function pageManageDocumentCancel(query) {
  return request({
    url: "/manageDocumentCancel/pageManageDocumentCancel",
    method: "get",
    params: query,
  });
}

// 文件发放回收-导出
export function exportManageDocumentIssueRecycle(query) {
  return request({
    url: "/manageDocumentIssueRecycle/exportManageDocumentIssueRecycle",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 文件发放回收-新增
export function addManageDocumentIssueRecycle(data) {
  return request({
    url: "/manageDocumentIssueRecycle/addManageDocumentIssueRecycle",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

// 文件发放回收-编辑
export function doManageDocumentIssueRecycle(data) {
  return request({
    url: "/manageDocumentIssueRecycle/doManageDocumentIssueRecycle",
    method: "post",
    data: data,
  });
}

// 文件发放回收-审核
export function checkManageDocumentIssueRecycle(data) {
  return request({
    url: "/manageDocumentIssueRecycle/checkManageDocumentIssueRecycle",
    method: "post",
    data: data,
  });
}

// 文件发放回收-列表
export function pageManageDocumentIssueRecycle(query) {
  return request({
    url: "/manageDocumentIssueRecycle/pageManageDocumentIssueRecycle",
    method: "get",
    params: query,
  });
}

//文件发放回收-列表
export function delManageDocumentIssueRecycle(query) {
  return request({
    url: "/manageDocumentIssueRecycle/delManageDocumentIssueRecycle",
    method: "delete",
    params: query,
  });
}

// 文件变更-导出
export function exportManageDocumentAlter(query) {
  return request({
    url: "/manageDocumentAlter/exportManageDocumentAlter",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 文件变更-新增
export function addManageDocumentAlter(data) {
  return request({
    url: "/manageDocumentAlter/addManageDocumentAlter",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

// 文件变更-编辑
export function doManageDocumentAlter(data) {
  return request({
    url: "/manageDocumentAlter/doManageDocumentAlter",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

// 文件变更-转换流
export function checkManageDocumentAlterPdf(query) {
  return request({
    url: "/manageDocumentAlter/checkManageDocumentAlterPdf",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 文件变更-审核
export function checkManageDocumentAlter(data) {
  return request({
    url: "/manageDocumentAlter/checkManageDocumentAlter",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

//文件变更-删除
export function delManageDocumentAlter(query) {
  return request({
    url: "/manageDocumentAlter/delManageDocumentAlter",
    method: "delete",
    params: query,
  });
}

// 文件变更-列表
export function pageManageDocumentAlter(query) {
  return request({
    url: "/manageDocumentAlter/pageManageDocumentAlter",
    method: "get",
    params: query,
  });
}

// 文件作废-新增
export function addManageDocumentCancel(data) {
  return request({
    url: "/manageDocumentCancel/addManageDocumentCancel",
    method: "post",
    data: data,
  });
}

// 文件作废-编辑
export function doManageDocumentCancel(data) {
  return request({
    url: "/manageDocumentCancel/doManageDocumentCancel",
    method: "post",
    data: data,
  });
}

// 文件作废-审核
export function checkManageDocumentCancel(data) {
  return request({
    url: "/manageDocumentCancel/checkManageDocumentCancel",
    method: "post",
    data: data,
  });
}

//文件作废-导出
export function exportManageDocumentCancel(query) {
  return request({
    url: "/manageDocumentCancel/exportManageDocumentCancel",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//文件作废-删除
export function delManageDocumentCancel(query) {
  return request({
    url: "/manageDocumentCancel/delManageDocumentCancel",
    method: "delete",
    params: query,
  });
}
