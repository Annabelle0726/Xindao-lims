// 管理评审相关接口
import request from "@/utils/request";

// 管理计划-上传附件
export function addReviewProgramFile(data) {
  return request({
    url: "/manageReviewProgramFile/addReviewProgramFile",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

//管理计划-附件列表
export function selectReviewProgramFile(query) {
  return request({
    url: "/manageReviewProgramFile/selectReviewProgramFile",
    method: "get",
    params: query,
  });
}

//管理计划-查询管理评审计划
export function getPageReviewProgram(query) {
  return request({
    url: "/manageReviewProgram/getPageReviewProgram",
    method: "get",
    params: query,
  });
}

//管理计划-删除管理评审计划
export function deleteReviewProgram(query) {
  return request({
    url: "/manageReviewProgram/deleteReviewProgram",
    method: "delete",
    params: query,
  });
}

//管理计划
export function exportReviewProgram(query) {
  return request({
    url: "/manageReviewProgram/exportReviewProgram",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 管理计划-编辑管理评审计划
export function modifyReviewProgram(data) {
  return request({
    url: "/manageReviewProgram/modifyReviewProgram",
    method: "post",
    data: data,
  });
}

//会议记录-查询管理评审会议记录
export function getPageMeeting(query) {
  return request({
    url: "/manageMeeting/getPageMeeting",
    method: "get",
    params: query,
  });
}

//会议记录-删除管理评审会议记录
export function deleteMeeting(query) {
  return request({
    url: "/manageMeeting/deleteMeeting",
    method: "delete",
    params: query,
  });
}

//会议记录-下载管理评审会议记录
export function exportMeeting(query) {
  return request({
    url: "/manageMeeting/exportMeeting",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 管理计划-新增管理评审会议记录
export function addMeeting(data) {
  return request({
    url: "/manageMeeting/addMeeting",
    method: "post",
    data: data,
  });
}

// 管理计划-修改管理评审会议记录
export function modifyMeeting(data) {
  return request({
    url: "/manageMeeting/modifyMeeting",
    method: "post",
    data: data,
  });
}

//评审会议报告-查询管理评审会议报告
export function getPageReviewReport(query) {
  return request({
    url: "/manageReviewReport/getPageReviewReport",
    method: "get",
    params: query,
  });
}

//评审会议报告-删除管理评审会议报告
export function deleteReviewReport(query) {
  return request({
    url: "/manageReviewReport/deleteReviewReport",
    method: "delete",
    params: query,
  });
}

//评审会议报告-下载
export function exportReviewReport(query) {
  return request({
    url: "/manageReviewReport/exportReviewReport",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 评审会议报告-编辑管理评审会议报告
export function modifyReviewReport(data) {
  return request({
    url: "/manageReviewReport/modifyReviewReport",
    method: "post",
    data: data,
  });
}

// 评审会议报告-新增管理评审会议报告
export function addReviewReport(data) {
  return request({
    url: "/manageReviewReport/addReviewReport",
    method: "post",
    data: data,
  });
}

// 评审会议报告-新增管理评审计划
export function addReviewProgram(data) {
  return request({
    url: "/manageReviewProgram/addReviewProgram",
    method: "post",
    data: data,
  });
}
