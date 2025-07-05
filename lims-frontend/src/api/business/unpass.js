// 不合格管理相关接口
import request from "@/utils/request";

//查询进货验证信息
export function getFactoryVerify(query) {
  return request({
    url: "/insOrderPlan/getFactoryVerify",
    method: "get",
    params: query,
  });
}

// 保存进货原始记录
export function addFactoryVerify(data) {
  return request({
    url: "/insOrderPlan/addFactoryVerify",
    method: "post",
    data: data,
  });
}

//查询不合格的检验项
export function getInsProductUnqualified(query) {
  return request({
    url: "/insOrderPlan/getInsProductUnqualified",
    method: "get",
    params: query,
  });
}

// 提交需要复测的检验项
export function addUnqualifiedRetest(data) {
  return request({
    url: "/insOrderPlan/addUnqualifiedRetest",
    method: "post",
    data: data,
  });
}

// 查询检验下单内容详情
export function getInsOrder(query) {
  return request({
    url: "/insOrder/getInsOrder",
    method: "get",
    params: query,
  });
}

// 查看OA详情
export function getUnqualifiedHandler(query) {
  return request({
    url: "/unqualifiedHandler/getUnqualifiedHandler",
    method: "get",
    params: query,
  });
}

// 下载OA附件
export function downFile(query) {
  return request({
    url: "/unqualifiedHandler/downFile",
    method: "get",
    params: query,
  });
}

// 提交不合格处理
export function addUnqualifiedHandler(data) {
  return request({
    url: "/unqualifiedHandler/addUnqualifiedHandler",
    method: "post",
    data: data,
  });
}
// 查询不合格样品数据
export function pageInsUnPass(data) {
  return request({
    url: "/unPass/pageInsUnPass",
    method: "post",
    data: data,
  });
}
