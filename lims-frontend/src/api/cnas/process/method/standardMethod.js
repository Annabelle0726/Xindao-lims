// 实验室的检测能力档案相关接口
import request from "@/utils/request";

//获取标准方法列表
export function selectStandardMethodList(query) {
  return request({
    url: "/standardMethod/selectStandardMethodList",
    method: "get",
    params: query,
  });
}

//删除标准方法
export function delStandardMethod(query) {
  return request({
    url: "/standardMethod/delStandardMethod",
    method: "delete",
    params: query,
  });
}

// 添加标准方法
export function addStandardMethod(data) {
  return request({
    url: "/standardMethod/addStandardMethod",
    method: "post",
    data: data,
  });
}

// 修改标准方法
export function upStandardMethod(data) {
  return request({
    url: "/standardMethod/upStandardMethod",
    method: "post",
    data: data,
  });
}

//获取产品架构
export function getStandardTree2(query) {
  return request({
    url: "/standardTree/getStandardTree2",
    method: "get",
    params: query,
  });
}

