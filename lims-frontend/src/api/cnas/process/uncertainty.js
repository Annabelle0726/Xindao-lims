// 不确定度评定相关接口
import request from "@/utils/request";

//导出
export function exportProcessEvaluate(query) {
  return request({
    url: "/processEvaluate/exportProcessEvaluate",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 填写 (主要上传id和备注note就好了)
export function doProcessEvaluate(data) {
  return request({
    url: "/processEvaluate/doProcessEvaluate",
    method: "post",
    data: data,
  });
}

//删除
export function delProcessEvaluate(query) {
  return request({
    url: "/processEvaluate/delProcessEvaluate",
    method: "delete",
    params: query,
  });
}

//分页
export function pageProcessEvaluate(query) {
  return request({
    url: "/processEvaluate/pageProcessEvaluate",
    method: "get",
    params: query,
  });
}
