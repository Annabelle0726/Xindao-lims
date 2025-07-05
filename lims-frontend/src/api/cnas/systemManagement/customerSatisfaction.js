// 客户满意度相关接口
import request from "@/utils/request";

//客户满意度调查列表
export function pageClientSatisfaction(query) {
  return request({
    url: "/clientSatisfaction/pageClientSatisfaction",
    method: "get",
    params: query,
  });
}

//查询客户分析附件
export function pageAnalyseFile(query) {
  return request({
    url: "/clientSatisfaction/pageAnalyseFile",
    method: "get",
    params: query,
  });
}

//删除新增客户满意度调查
export function delClientSatisfaction(query) {
  return request({
    url: "/clientSatisfaction/delClientSatisfaction",
    method: "delete",
    params: query,
  });
}

//删除客户分析附件
export function delAnalyseFile(query) {
  return request({
    url: "/clientSatisfaction/delAnalyseFile",
    method: "delete",
    params: query,
  });
}

// 新增客户满意度调查
export function addClientSatisfaction(data) {
  return request({
    url: "/clientSatisfaction/addClientSatisfaction",
    method: "post",
    data: data,
  });
}

// 修改新增客户满意度调查
export function updateClientSatisfaction(data) {
  return request({
    url: "/clientSatisfaction/updateClientSatisfaction",
    method: "post",
    data: data,
  });
}
