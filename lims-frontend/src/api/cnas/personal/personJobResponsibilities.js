import request from "@/utils/request";

// 分页查询岗位职责
export function personJobResponsibilitiesSelect(query) {
  return request({
    url: "/personJobResponsibilities/personJobResponsibilitiesSelect",
    method: "get",
    params: query
  });
}
// 新增岗位职责
export function personJobResponsibilitiesSave(query) {
  return request({
    url: "/personJobResponsibilities/personJobResponsibilitiesSave",
    method: "post",
    data: query
  });
}
// 导出岗位职责
export function exportPersonJobResponsibilities(query) {
  return request({
    url: "/personJobResponsibilities/exportPersonJobResponsibilities",
    method: "post",
    params: query,
    responseType: "blob"
  });
}
// 删除岗位职责
export function personJobResponsibilitiesDelete(query) {
  return request({
    url: "/personJobResponsibilities/personJobResponsibilitiesDelete",
    method: "delete ",
    params: query
  });
}
