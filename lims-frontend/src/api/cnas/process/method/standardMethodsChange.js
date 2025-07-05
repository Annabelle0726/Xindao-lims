// 实验室的检测能力档案相关接口
import request from "@/utils/request";

//标准方法更新验证列表
export function pagesMethodVerify(query) {
  return request({
    url: "/processMethodVerify/pagesMethodVerify",
    method: "get",
    params: query,
  });
}

//获取用户列表
export function selectUserCondition(query) {
  return request({
    url: "/system/newUser/selectUserCondition",
    method: "get",
    params: query,
  });
}

//查询标准方法验证详情
export function getMethodVerifyOne(query) {
  return request({
    url: "/processMethodVerify/getMethodVerifyOne",
    method: "get",
    params: query,
  });
}

// 通过设备分类获取设备列表
export function deviceScopeSearch(query) {
  return request({
    url: '/deviceScope/search',
    method: 'get',
    params: query
  })
}

// 新增标准方法验证
export function addMethodVerify(data) {
  return request({
    url: "/processMethodVerify/addMethodVerify",
    method: "post",
    data: data,
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

// 导出标准方法更新验证
export function exportMethodVerify(query) {
  return request({
    url: '/processMethodVerify/exportMethodVerify',
    method: "get",
    responseType: "blob",
    params: query,
  })
}

// 验证确认
export function methodVerifyAffirm(query) {
  return request({
    url: '/processMethodVerify/methodVerifyAffirm',
    method: 'get',
    params: query
  })
}

// 删除标准方法更新验证
export function delMethodVerify(query) {
  return request({
    url: '/processMethodVerify/delMethodVerify',
    method: 'delete',
    params: query
  })
}

// 原始记录列表
export function getVerifyMethodFileList(query) {
  return request({
    url: '/processMethodVerify/getVerifyMethodFileList',
    method: 'get',
    params: query
  })
}

// 删除验证原始记录列表
export function delVerifyMethodFileList(query) {
  return request({
    url: '/processMethodVerify/delVerifyMethodFileList',
    method: 'delete',
    params: query
  })
}
