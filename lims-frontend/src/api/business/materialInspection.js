// 原材料报检页面相关接口
import request from '@/utils/request'

// 查询IFS订单-待报检
export function getIfsOrder(query) {
  return request({
    url: 'insOrder/getIfsOrder',
    method: 'get',
  })
}
// 查询IFS订单-待报检
export function getWarehouseSubmit(query) {
  return request({
    url: '/rawMaterialOrder/getWarehouseSubmit',
    method: 'get',
    params: query
  })
}
// 查询原材料报检-已完成
export function getIfsByFinish(query) {
  return request({
    url: '/rawMaterialOrder/getIfsByFinish',
    method: 'get',
    params: query
  })
}
// 查询原材料报检信息-全部
export function getIfsByAll(query) {
  return request({
    url: '/rawMaterialOrder/getIfsByAll',
    method: 'get',
    params: query
  })
}
// 原材料根据id取消报检
export function revokeInspectionReport(query) {
  return request({
    url: '/rawMaterialOrder/revokeInspectionReport',
    method: 'post',
    data: query
  })
}
// 放行数据
export function concessionRelease(query) {
  return request({
    url: '/rawMaterialOrder/concessionRelease',
    method: 'post',
    data: query
  })
}
// 提前入库
export function advancedGodown(query) {
  return request({
    url: '/rawMaterialOrder/advancedGodown',
    method: 'post',
    data: query
  })
}
// 批量报检
export function inspectionReport(query) {
  return request({
    url: '/rawMaterialOrder/inspectionReport',
    method: 'post',
    data: query
  })
}
// 原材料根据手动报检
export function addIfsInventoryQuantity(query) {
  return request({
    url: '/rawMaterialOrder/addIfsInventoryQuantity',
    method: 'post',
    data: query
  })
}
// 原材料根据id报检
export function inspectionReportOne(query) {
  return request({
    url: '/rawMaterialOrder/inspectionReportOne',
    method: 'post',
    data: query
  })
}
// 原材料报检删除
export function delIfsInventory(query) {
  return request({
    url: '/rawMaterialOrder/delIfsInventory',
    method: 'delete',
    params: query
  })
}
// 原材料报检全部导出
export function rawAllExport(query) {
  return request({
    url: '/rawMaterialOrder/rawAllExport',
    method: 'get',
    data: query,
    responseType: "blob"
  })
}
