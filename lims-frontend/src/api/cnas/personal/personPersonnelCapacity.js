import request from "@/utils/request";

// 查询人员能力
export function personPersonnelCapacityPage(query) {
  return request({
    url: "/personPersonnelCapacity/personPersonnelCapacityPage",
    method: "get",
    params: query
  });
}
// 新增编辑人员能力
export function addOrUpdatePersonPersonnelCapacity(query) {
  return request({
    url: "/personPersonnelCapacity/addOrUpdatePersonPersonnelCapacity",
    method: "post",
    data: query
  });
}
// 新增编辑人员能力
export function submitConfirmPersonnelCapability(query) {
  return request({
    url: "/personPersonnelCapacity/submitConfirmPersonnelCapability",
    method: "post",
    data: query
  });
}
// 确认人员能力
export function confirmPersonnelCapability(query) {
  return request({
    url: "/personPersonnelCapacity/confirmPersonnelCapability",
    method: "post",
    data: query
  });
}
// 删除人员能力
export function deletePersonPersonnelCapacity(query) {
  return request({
    url: "/personPersonnelCapacity/deletePersonPersonnelCapacity",
    method: "delete",
    params: query
  });
}
// 导出人员能力
export function exportPersonnelCapacity(query) {
  return request({
    url: "/personPersonnelCapacity/exportPersonnelCapacity",
    method: "get",
    params: query,
    responseType: "blob"
  });
}
