// 投诉汇总表相关接口
import request from "@/utils/request";

// 投诉处理新增
export function addProcessComplain(data) {
  return request({
    url: "/processComplain/addProcessComplain",
    method: "post",
    data: data,
  });
}

//投诉处理详情
export function getProcessComplain(query) {
  return request({
    url: "/processComplain/getProcessComplain",
    method: "get",
    params: query,
  });
}

// 投诉处理修改
export function doProcessComplain(data) {
  return request({
    url: "/processComplain/doProcessComplain",
    method: "post",
    data: data,
  });
}

//投诉处理导出
export function exportProcessComplain(query) {
  return request({
    url: "/processComplain/exportProcessComplain",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//投诉处理分页
export function pageProcessComplain(query) {
  return request({
    url: "/processComplain/pageProcessComplain",
    method: "get",
    params: query,
  });
}

//投诉处理删除
export function delProcessComplain(query) {
  return request({
    url: "/processComplain/delProcessComplain",
    method: "delete",
    params: query,
  });
}
