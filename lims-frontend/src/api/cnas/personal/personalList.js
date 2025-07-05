import request from "@/utils/request";

// 删除人员明细所在组织架构
export function delUserDepardLimsId(query) {
  return request({
    url: "/system/newUser/delUserDepardLimsId",
    method: "delete",
    params: query,
  });
}
// 修改人员明细所在组织架构
export function upUserDepardLimsId(query) {
  return request({
    url: "/system/newUser/upUserDepardLimsId",
    method: "post",
    data: query,
  });
}
// 导出
export function exportPersonBasicInfo(query) {
  return request({
    url: "/personBasicInfo/exportPersonBasicInfo",
    method: "get",
    data: query,
    responseType: "blob"
  });
}
// 导出
export function exportPersonBasicInfoById(query) {
  return request({
    url: "/personBasicInfo/exportPersonBasicInfoById",
    method: "get",
    params: query,
    responseType: "blob"
  });
}
// 获取人员列表
export function basicInformationOfPersonnelSelectPage(query) {
  return request({
    url: "/personBasicInfo/basicInformationOfPersonnelSelectPage",
    method: "get",
    params: query
  });
}
// 删除部门
export function delDepartmentLims(query) {
  return request({
    url: "/personBasicInfo/delDepartmentLims",
    method: "delete",
    params: query
  });
}
// 添加部门
export function addDepartmentLims(query) {
  return request({
    url: "/personBasicInfo/addDepartmentLims",
    method: "post",
    data: query
  });
}
// 查询CNAS人员侧边栏
export function selectCNSAPersonTree() {
  return request({
    url: "/personBasicInfo/selectCNSAPersonTree",
    method: "get"
  });
}
// 人员培训基本信息附件列表
export function getBasicInfoFileList(query) {
  return request({
    url: "/personBasicInfo/getBasicInfoFileList",
    method: "get",
    params: query
  });
}
// 删除附件
export function delBasicInfoFileList(query) {
  return request({
    url: "/personBasicInfo/delBasicInfoFileList",
    method: "delete",
    params: query
  });
}
// 人员培训基本信息工作经历新增
export function addBasicInfoWork(query) {
  return request({
    url: "/personBasicInfo/addBasicInfoWork",
    method: "post",
    data: query
  });
}
// 人员培训基本信息工作经历修改
export function updateBasicInfoWorkList(query) {
  return request({
    url: "/personBasicInfo/updateBasicInfoWorkList",
    method: "post",
    data: query
  });
}
// 人员培训基本信息工作经历删除
export function delBasicInfoWorkList(query) {
  return request({
    url: "/personBasicInfo/delBasicInfoWorkList",
    method: "delete",
    params: query
  });
}
// 人员培训基本信息工作经历查询
export function getBasicInfoWorkList(query) {
  return request({
    url: "/personBasicInfo/getBasicInfoWorkList",
    method: "get",
    params: query
  });
}
// 获取附件
export function getAnnex(query) {
  return request({
    url: "/personBasicInfo/getAnnex",
    method: "get",
    params: query
  });
}
// 更新附件
export function updateAnnex(query) {
  return request({
    url: "/personBasicInfo/updateAnnex",
    method: "post",
    data: query
  });
}
// 更新附件
export function getAnnexByUserId(query) {
  return request({
    url: "/personBasicInfo/getAnnexByUserId",
    method: "get",
    params: query
  });
}
// 添加附件
export function addAnnex(query) {
  return request({
    url: "/personBasicInfo/addAnnex",
    method: "post",
    data: query
  });
}
// 删除附件
export function deleteAnnex(query) {
  return request({
    url: "/personBasicInfo/deleteAnnex",
    method: "delete",
    params: query
  });
}
// 人员基本信息查询
export function getCNASPersonnelInfo(query) {
  return request({
    url: "/personBasicInfo/getCNASPersonnelInfo",
    method: "get",
    params: query
  });
}
// 人员基本信息保存
export function saveCNASPersonnelInfo(query) {
  return request({
    url: "/personBasicInfo/saveCNASPersonnelInfo",
    method: "post",
    data: query
  });
}
// 删除文件
export function deleteCNASFile(query) {
  return request({
    url: "/personBasicInfo/deleteCNASFile",
    method: "delete",
    params: query
  });
}
