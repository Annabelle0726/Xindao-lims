// 不合格处理相关接口
import request from "@/utils/request";

//查询不合格处理
export function page(query) {
  return request({
    url: "/unqualifiedHandler/page",
    method: "get",
    params: query,
  });
}
//删除不合格处理
export function deleteUnqualifiedHandler(query) {
  return request({
    url: "/unqualifiedHandler/deleteUnqualifiedHandler",
    method: "delete",
    params: query,
  });
}
//提交OA流程
export function pushOA(query) {
  return request({
    url: "/unqualifiedHandler/pushOA",
    method: "post",
    data: query,
  });
}
