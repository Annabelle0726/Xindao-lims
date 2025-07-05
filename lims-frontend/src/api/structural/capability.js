import request from '@/utils/request'

// 获取检验项目参数
export function selectItemParameterList(query) {
  return request({
    url: '/capacityScope/selectItemParameterList',
    method: 'get',
    params: query
  })
}
// 获取检测对象
export function selectTestObjectList(query) {
  return request({
    url: '/capacityScope/selectTestObjectList',
    method: 'get',
    params: query
  })
}
// 新增检验项目参数
export function addItemParameter(query) {
  return request({
    url: '/capacityScope/addItemParameter',
    method: 'post',
    data: query
  })
}
// 删除检验项目参数
export function delItemParameter(query) {
  return request({
    url: '/capacityScope/delItemParameter',
    method: 'delete',
    params: query
  })
}
// 修改检验项目参数
export function upItemParameter(query) {
  return request({
    url: '/capacityScope/upItemParameter',
    method: 'post',
    data: query
  })
}
// 获取检验对象树
export function getItemTree() {
  return request({
    url: '/capacityScope/getItemTree',
    method: 'get'
  })
}
// 新增检测对象
export function addTestObject(query) {
  return request({
    url: '/capacityScope/addTestObject',
    method: 'post',
    data: query
  })
}
// 删除检测对象
export function delTestObject(query) {
  return request({
    url: '/capacityScope/delTestObject',
    method: 'delete',
    params: query
  })
}
// 修改检测对象
export function upTestObject(query) {
  return request({
    url: '/capacityScope/upTestObject',
    method: 'post',
    data: query
  })
}
// 查询检验对象的产品维护列表
export function selectProductListByObjectId(query) {
  return request({
    url: '/capacityScope/selectProductListByObjectId',
    method: 'get',
    params: query
  })
}
// 新增产品
export function addProduct(query) {
  return request({
    url: '/capacityScope/addProduct',
    method: 'post',
    data: query
  })
}
// 删除产品
export function delProduct(query) {
  return request({
    url: '/capacityScope/delProduct',
    method: 'delete',
    params: query
  })
}
// 修改产品
export function upProduct(query) {
  return request({
    url: '/capacityScope/upProduct',
    method: 'post',
    data: query
  })
}
// 获取实验室名称
export function obtainItemParameterList() {
  return request({
    url: '/laboratoryScope/obtainItemParameterList',
    method: 'get',
  })
}
// 查询原始记录模板枚举
export function getStandardTemplate() {
  return request({
    url: '/StandardTemplate/getStandardTemplate',
    method: 'get',
  })
}
// 根据产品id查询厂家密度绑定
export function selectSupplierDensityByProductId(query) {
  return request({
    url: '/productSupplierDensity/selectSupplierDensityByProductId',
    method: 'get',
    params: query
  })
}
// 新增产品厂家密度绑定
export function addProductSupplierDensity(query) {
  return request({
    url: '/productSupplierDensity/addProductSupplierDensity',
    method: 'post',
    data: query
  })
}
// 修改产品厂家密度绑定
export function updateProductSupplierDensity(query) {
  return request({
    url: '/productSupplierDensity/updateProductSupplierDensity',
    method: 'post',
    data: query
  })
}
// 删除产品厂家密度绑定
export function deleteProductSupplierDensity(query) {
  return request({
    url: '/productSupplierDensity/deleteProductSupplierDensity',
    method: 'delete',
    params: query
  })
}
