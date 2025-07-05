// 成品下单页面相关接口
import request from '@/utils/request'

// 查询成品下单列表
export function selectInsOrderParameter(query) {
  return request({
    url: '/insOrder/selectInsOrderParameter',
    method: 'get',
    params: query
  })
}
// 修改成品委托编号
export function updateOrderEntrustCode(query) {
  return request({
    url: '/insOrder/updateOrderEntrustCode',
    method: 'post',
    data: query
  })
}
// 成品检验单全部信息导出
export function rawAllInsOrderExport(query) {
  return request({
    url: '/insOrder/rawAllInsOrderExport',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}
// 将待检验的的撤销进行更改
export function updateInspected(query) {
  return request({
    url: '/insOrder/updateInspected',
    method: 'post',
    data: query
  })
}
// 撤销审核通过
export function checkUpdate(query) {
  return request({
    url: '/insOrder/checkUpdate',
    method: 'post',
    data: query
  })
}
// 查询订单总共预计时间
export function selectOrderManDay(query) {
  return request({
    url: '/insOrder/selectOrderManDay',
    method: 'get',
    params: query
  })
}
// 任务交接
export function upPlanUser2(query) {
  return request({
    url: '/insOrderPlan/upPlanUser2',
    method: 'get',
    params: query
  })
}
// 检验下发
export function upInsOrder(query) {
  return request({
    url: '/insOrder/upInsOrder',
    method: 'post',
    params: query
  })
}
// 将待审核更新成撤销状态
export function updateStatus(query) {
  return request({
    url: '/insOrder/updateStatus',
    method: 'post',
    data: query
  })
}
// 将待审核更新成撤销状态
export function delInsOrder(query) {
  return request({
    url: '/insOrder/delInsOrder',
    method: 'delete',
    params: query
  })
}
// 将待审核更新成撤销状态
export function selectNoProducts(query) {
  return request({
    url: '/insOrder/selectNoProducts',
    method: 'get',
    params: query
  })
}
// 查询成品标签打印信息
export function labelOrderPrinting(query) {
  return request({
    url: '/insOrder/labelOrderPrinting',
    method: 'post',
    data: query
  })
}
// 根据订单id查询样品
export function getSampleByOrderId(query) {
  return request({
    url: '/insOrder/getSampleByOrderId',
    method: 'get',
    params: query
  })
}
// 根据样品id查询检验项树
export function getProductTreeBySampleId(query) {
  return request({
    url: '/insOrder/getProductTreeBySampleId',
    method: 'get',
    params: query
  })
}
// 添加遗漏的检验项
export function addOmitOrderProduct(query) {
  return request({
    url: '/insOrder/addOmitOrderProduct',
    method: 'post',
    data: query
  })
}
// 修改样品型号
export function updateSampleModel(query) {
  return request({
    url: '/insOrder/updateSampleModel',
    method: 'post',
    data: query
  })
}
