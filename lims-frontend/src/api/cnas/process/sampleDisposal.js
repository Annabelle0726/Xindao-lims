// 检测或校准物品的处置
import request from "@/utils/request";

// 修改
export function doProcessDeal(data) {
  return request({
    url: "/processDeal/doProcessDeal",
    method: "post",
    data: data,
  });
}

// 新增
export function addProcessDeal(data) {
  return request({
    url: "/processDeal/addProcessDeal",
    method: "post",
    data: data,
  });
}

// 提交历史  传参id
export function submitProcessTotaldeal(data) {
  return request({
    url: "/processTotaldeal/submitProcessTotaldeal",
    method: "post",
    data: data,
  });
}

//查询详情  分页查询里面totaldealId  传参历史的id
export function pageProcessDeal(query) {
  return request({
    url: "/processDeal/pageProcessDeal",
    method: "get",
    params: query,
  });
}

// 审核  传参id和通过不通过state(中文)
export function checkProcessTotaldeal(data) {
  return request({
    url: "/processTotaldeal/checkProcessTotaldeal",
    method: "post",
    data: data,
  });
}

// 批准  传参id和通过不通过state(中文)
export function ratifyProcessTotaldeal(data) {
  return request({
    url: "/processTotaldeal/ratifyProcessTotaldeal",
    method: "post",
    data: data,
  });
}

//删除
export function delProcessDeal(query) {
  return request({
    url: "/processDeal/delProcessDeal",
    method: "delete",
    params: query,
  });
}

//查看历史
export function pageProcessTotaldeal(query) {
  return request({
    url: "/processTotaldeal/pageProcessTotaldeal",
    method: "get",
    params: query,
  });
}
