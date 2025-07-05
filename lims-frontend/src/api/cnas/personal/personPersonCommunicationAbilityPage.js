import request from "@/utils/request";

// 查询人员沟通记录
export function personPersonCommunicationAbilityPage(query) {
  return request({
    url: "/personCommunicationAbility/personPersonCommunicationAbilityPage",
    method: "get",
    params: query
  });
}
// 删除人员沟通记录
export function deletePersonCommunicationAbility(query) {
  return request({
    url: "/personCommunicationAbility/deletePersonCommunicationAbility",
    method: "delete",
    params: query
  });
}
// 导出人员沟通记录
export function exportPersonCommunicationAbility(query) {
  return request({
    url: "/personCommunicationAbility/exportPersonCommunicationAbility",
    method: "get",
    params: query,
    responseType: "blob"
  });
}
// 新增-编辑人员沟通记录
export function addOrUpdatePersonCommunicationAbility(query) {
  return request({
    url: "/personCommunicationAbility/addOrUpdatePersonCommunicationAbility",
    method: "post",
    data: query
  });
}
