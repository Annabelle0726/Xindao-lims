// 不符合工作相关接口
import request from "@/utils/request";

//查询监督记录不符合控制信息列表
export function pageSuperviseDetailAccording(query) {
  return request({
    url: "/qualitySupervise/pageSuperviseDetailAccording",
    method: "get",
    params: query,
  });
}

//导出不符合项
export function superviseDetailAccordingExport(query) {
  return request({
    url: "/qualitySupervise/superviseDetailAccordingExport",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//查询监督记录不符合控制信息
export function getSuperviseDetailAccording(query) {
  return request({
    url: "/qualitySupervise/getSuperviseDetailAccording",
    method: "get",
    params: query,
  });
}

//不符合项的分布分页查询
export function pageInconsistentDistribution(query) {
  return request({
    url: "/inconsistentDistribution/pageInconsistentDistribution",
    method: "get",
    params: query,
  });
}

//不符合项的分布删除
export function delInconsistentDistribution(query) {
  return request({
    url: "/inconsistentDistribution/delInconsistentDistribution",
    method: "delete",
    params: query,
  });
}

//导出不符合项的分布
export function exportInconsistentDistribution(query) {
  return request({
    url: "/inconsistentDistribution/exportInconsistentDistribution",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//不符合项的分布详情
export function getInconsistentDistributionOne(query) {
  return request({
    url: "/inconsistentDistribution/getInconsistentDistributionOne",
    method: "get",
    params: query,
  });
}

// 不符合项的分布新增
export function addInconsistentDistribution(data) {
  return request({
    url: "/inconsistentDistribution/addInconsistentDistribution",
    method: "post",
    data: data,
  });
}

// 不符合项的分布修改
export function updateInconsistentDistribution(data) {
  return request({
    url: "/inconsistentDistribution/updateInconsistentDistribution",
    method: "post",
    data: data,
  });
}
