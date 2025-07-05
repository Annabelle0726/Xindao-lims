/**
 * @description 检验委托单
 */

// 实验室的检测能力档案相关接口
import request from "@/utils/request";

// 委托单检验分页查询
export function pageInspectionOrder(query) {
  return request({
    url: '/inspectionOrder/pageInspectionOrder',
    method: 'get',
    params: query
  })
}
// 委托单查询成品订单
export function getInsOrderOnInspection(query) {
  return request({
    url: '/inspectionOrder/getInsOrderOnInspection',
    method: 'get',
    params: query
  })
}
// 委托单检验新增
export function addInspectionOrder(query) {
  return request({
    url: '/inspectionOrder/addInspectionOrder',
    method: 'post',
    data: query
  })
}
// 委托单检验修改
export function updateInspectionOrder(query) {
  return request({
    url: '/inspectionOrder/updateInspectionOrder',
    method: 'post',
    data: query
  })
}
// 新增时根据成品订单查询委托单详情
export function getInspectionOrderByInsOderId(query) {
  return request({
    url: '/inspectionOrder/getInspectionOrderByInsOderId',
    method: 'get',
    params: query
  })
}
// 委托单检验查看详情
export function getInspectionOrderOne(query) {
  return request({
    url: '/inspectionOrder/getInspectionOrderOne',
    method: 'get',
    params: query
  })
}
// 委托单检验删除
export function delInspectionOrder(query) {
  return request({
    url: '/inspectionOrder/delInspectionOrder',
    method: 'delete',
    params: query
  })
}
// 委托单检验删除
export function exportInspectionOrder(query) {
  return request({
    url: '/inspectionOrder/exportInspectionOrder',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
