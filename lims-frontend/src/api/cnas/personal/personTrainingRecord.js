import request from "@/utils/request";

// 导出培训记录
export function exportTrainingRecord(query) {
  return request({
    url: "/personTrainingRecord/exportTrainingRecord",
    method: "get",
    params: query,
    responseType: 'blob'
  });
}
// 查询人员 培训记录
export function trainingSelectTrainingRecord(query) {
  return request({
    url: "/personTrainingRecord/trainingSelectTrainingRecord",
    method: "get",
    params: query,
  });
}
// 查询人员明细 培训记录
export function queryPersonnelDetails(query) {
  return request({
    url: "/personTrainingRecord/queryPersonnelDetails",
    method: "get",
    params: query,
  });
}
