// 原材料下单页面相关接口
import request from '@/utils/request'

// 查询原材料下单-待下单
export function getPurchaseOrder(query) {
  return request({
    url: '/rawMaterialOrder/getPurchaseOrder',
    method: 'get',
    params: query
  })
}
// 查询IFS订单-待检验
export function getIfsByStateOne(query) {
  return request({
    url: '/rawMaterialOrder/getIfsByStateOne',
    method: 'get',
    params: query
  })
}
// 查询原材料下单-已检验
export function getIfsByOver(query) {
  return request({
    url: '/rawMaterialOrder/getIfsByOver',
    method: 'get',
    params: query
  })
}
// 查询原材料下单-全部
export function getIfsByAll(query) {
  return request({
    url: '/rawMaterialOrder/getIfsByAll',
    method: 'get',
    params: query
  })
}
// 查询季度检验
export function getIfsByQuarter(query) {
  return request({
    url: '/rawMaterialOrder/getIfsByQuarter',
    method: 'get',
    params: query
  })
}
// 获取当前登录的客户信息
export function getUserNow(query) {
  return request({
    url: '/system/newUser/getUserNow',
    method: 'get',
  })
}
// 获取标准树下标准方法枚举
export function selectStandardMethodEnum(query) {
  return request({
    url: '/standardTree/selectStandardMethodEnum',
    method: 'get',
  })
}
// 查询检验下单内容详情
export function getInsOrder(query) {
  return request({
    url: '/insOrder/getInsOrder',
    method: 'get',
    params: query
  })
}
// 原材料下单直接免检免检接口
export function notificationRawOrder(query) {
  return request({
    url: '/rawMaterialOrder/notificationRawOrder',
    method: 'get',
    params: query
  })
}
// 查询
export function selectStandardTreeListByPartNo(query) {
  return request({
    url: '/rawMaterialOrder/selectStandardTreeListByPartNo',
    method: 'get',
    params: query
  })
}
// 删除原材料模板
export function delRawMaterOrderTemplate(query) {
  return request({
    url: '/rawMaterialOrder/delRawMaterOrderTemplate',
    method: 'delete',
    params: query
  })
}
// 根据id查询模板内容
export function selectRawMaterOrderTemplateById(query) {
  return request({
    url: '/rawMaterialOrder/selectRawMaterOrderTemplateById',
    method: 'get',
    params: query
  })
}
// 原材料模板列表查询
export function selectRawMaterOrderTemplate(query) {
  return request({
    url: '/rawMaterialOrder/selectRawMaterOrderTemplate',
    method: 'get',
    params: query
  })
}
// 原材料新增模板
export function addRawMaterOrderTemplate(query) {
  return request({
    url: '/rawMaterialOrder/addRawMaterOrderTemplate',
    method: 'post',
    data: query
  })
}
// 免检
export function addExemptionOrder(query) {
  return request({
    url: '/rawMaterialOrder/addExemptionOrder',
    method: 'post',
    data: query
  })
}
// 通过标准树查询对应的检验项目
export function selectStandardProductList(query) {
  return request({
    url: '/standardTree/selectStandardProductList',
    method: 'post',
    data: query
  })
}
// 根据标准树进行标准查询
export function selectsStandardMethodByFLSSM(query) {
  return request({
    url: '/standardTree/selectsStandardMethodByFLSSM',
    method: 'get',
    params: query
  })
}
// 添加检验下单数据
export function addInsOrder(query) {
  return request({
    url: '/insOrder/addInsOrder',
    method: 'post',
    data: query
  })
}
// 铜单丝下单
export function addRawCopperOrder(query) {
  return request({
    url: '/rawMaterialOrder/addRawCopperOrder',
    method: 'post',
    data: query
  })
}
// 铜单丝下单进厂检验免检接口
export function addRawCopperOrderExemptionOrder(query) {
  return request({
    url: '/rawMaterialOrder/addRawCopperOrderExemptionOrder',
    method: 'post',
    data: query
  })
}
// 获取标准树
export function selectStandardTreeList2() {
  return request({
    url: '/standardTree/selectStandardTreeList2',
    method: 'get'
  })
}
// 查询检验单模板
export function selectInsOrderTemplate(query) {
  return request({
    url: '/insOrder/selectInsOrderTemplate',
    method: 'get',
    params: query
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
// 原材料下单季度撤销
export function repealQuarterRawOrder(query) {
  return request({
    url: '/rawMaterialOrder/repealQuarterRawOrder',
    method: 'post',
    data: query
  })
}
// 原材料下单直接免检免检接口
export function rawOrderRelease(query) {
  return request({
    url: '/rawMaterialOrder/rawOrderRelease',
    method: 'post',
    data: query
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
// 原材料下单出厂撤销
export function repealEnterRawOrder(query) {
  return request({
    url: '/rawMaterialOrder/repealEnterRawOrder',
    method: 'post',
    data: query
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
// 原材料下单出厂撤销
export function updateEntrustCode(query) {
  return request({
    url: '/insOrder/updateEntrustCode',
    method: 'post',
    data: query
  })
}
// 原材料下单出厂撤销
export function printLabel(query) {
  return request({
    url: '/rawMaterialOrder/printLabel',
    method: 'post',
    data: query
  })
}
// 通过检验单查询检验数据（数据查看）
export function selectSampleAndProductByOrderId(query) {
  return request({
    url: '/insOrder/selectSampleAndProductByOrderId',
    method: 'get',
    params: query
  })
}
// 查看不合格复测数据
export function getRetestResult(query) {
  return request({
    url: '/insOrder/getRetestResult',
    method: 'get',
    params: query
  })
}
// 查看检验单下的附件列表
export function getFileList(query) {
  return request({
    url: '/insOrderPlan/getFileList',
    method: 'get',
    params: query
  })
}
// 检验单下的附件列表-附件下载
export function downFile(query) {
  return request({
    url: '/insOrderPlan/downFile',
    method: 'get',
    params: query
  })
}
// 检验单下的附件列表-删除
export function delFile(query) {
  return request({
    url: '/insOrderPlan/delfile',
    method: 'delete',
    params: query
  })
}
// 检验单下的附件列表-上传
export function uploadFile(query) {
  return request({
    url: '/insOrderPlan/uploadFile',
    method: 'delete',
    params: query
  })
}
// 保存不合格复测数据
export function saveUnqualifiedContext(query) {
  return request({
    url: '/insOrderPlan/saveUnqualifiedContext',
    method: 'post',
    data: query
  })
}
