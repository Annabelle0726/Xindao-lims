// 实验室的检测能力档案相关接口
import request from "@/utils/request";

// 标准方法更新验证列表
export function pagesMethodVerify(query) {
  return request({
    url: "/processMethodVerify/pagesMethodVerify",
    method: "get",
    params: query,
  });
}

// 删除标准方法更新验证
export function delMethodVerify(query) {
  return request({
    url: '/processMethodVerify/delMethodVerify',
    method: 'delete',
    params: query
  })
}

// 导出标准方法更新验证
export function exportMethodVerify(query) {
  return request({
    url: '/processMethodVerify/exportMethodVerify',
    method: "get",
    responseType: "blob",
    params: query,
  })
}

//查询标准方法验证详情
export function getMethodVerifyOne(query) {
  return request({
    url: "/processMethodVerify/getMethodVerifyOne",
    method: "get",
    params: query,
  });
}

// 修改标准方法验证
export function updateMethodVerify(data) {
  return request({
    url: "/processMethodVerify/updateMethodVerify",
    method: "post",
    data: data,
  });
}

// 新增标准方法验证
export function addMethodVerify(data) {
  return request({
    url: "/processMethodVerify/addMethodVerify",
    method: "post",
    data: data,
  });
}

// 验证确认
export function methodVerifyAffirm(query) {
  return request({
    url: '/processMethodVerify/methodVerifyAffirm',
    method: 'get',
    params: query
  })
}
