import request from "@/utils/request";

// 人员 - 奖惩记录 分页查询
export function rewardPunishmentPage(query) {
  return request({
    url: "/personRewardPunishmentRecord/rewardPunishmentPage",
    method: "get",
    params: query
  });
}
// 人员 - 奖惩记录 导出
export function rewardPunishmentExport(query) {
  return request({
    url: "/personRewardPunishmentRecord/rewardPunishmentExport",
    method: "get",
    params: query,
    responseType: 'blob'
  });
}
// 人员 - 奖惩记录 新增/更新
export function addOrUpdateRewardPunishment(query) {
  return request({
    url: "/personRewardPunishmentRecord/addOrUpdateRewardPunishment",
    method: "post",
    data: query,
  });
}
// 人员 - 奖惩记录 新增/更新
export function deleteRewardPunishment(query) {
  return request({
    url: "/personRewardPunishmentRecord/deleteRewardPunishment",
    method: "delete",
    params: query,
  });
}
