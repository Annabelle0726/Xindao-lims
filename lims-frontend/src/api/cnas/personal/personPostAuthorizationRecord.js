import request from "@/utils/request";

// 任职授权记录查询表格
export function PersonPostAuthorizationRecordPage(query) {
  return request({
    url: "/personPostAuthorizationRecord/PersonPostAuthorizationRecordPage",
    method: "get",
    params: query
  });
}
// 任职授权记录删除
export function deletePersonPostAuthorizationRecord(query) {
  return request({
    url: "/personPostAuthorizationRecord/deletePersonPostAuthorizationRecord",
    method: "delete",
    params: query
  });
}
// 任职授权导出
export function exportPersonPostAuthorizationRecord(query) {
  return request({
    url: "/personPostAuthorizationRecord/exportPersonPostAuthorizationRecord",
    method: "get",
    params: query,
    responseType: "blob"
  });
}
// 任职授权导出
export function addOrUpdatePersonPostAuthorizationRecord(query) {
  return request({
    url: "/personPostAuthorizationRecord/addOrUpdatePersonPostAuthorizationRecord",
    method: "post",
    data: query,
  });
}
