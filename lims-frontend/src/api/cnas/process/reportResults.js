// 报告结果相关接口
import request from "@/utils/request";

//8报告结果-导出
export function exportProcessReport(data) {
  return request({
    url: "/processReport/exportProcessReport",
    method: "get",
    params: data,
  });
}

// 8报告结果-新增
export function addProcessReport(data) {
  return request({
    url: "/processReport/addProcessReport",
    method: "post",
    data: data,
  });
}

// 8报告结果-修改
export function doProcessReport(data) {
  return request({
    url: "/processReport/doProcessReport",
    method: "post",
    data: data,
  });
}

//8报告结果-删除
export function delProcessReport(query) {
  return request({
    url: "/processReport/delProcessReport",
    method: "delete",
    params: query,
  });
}

//8报告结果-列表
export function pageProcessReport(query) {
  return request({
    url: "/processReport/pageProcessReport",
    method: "get",
    params: query,
  });
}
