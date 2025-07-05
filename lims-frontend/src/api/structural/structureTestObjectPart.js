import request from '@/utils/request'

// 检验对象根据产品id查询零件
export function selectByTestObjectId(query) {
  return request({
    url: '/structureTestObjectPart/selectByTestObjectId',
    method: 'get',
    params: query
  })
}
// 检验对象新增产品零件
export function addTestObjectPart(query) {
  return request({
    url: '/structureTestObjectPart/addTestObjectPart',
    method: 'post',
    data: query
  })
}
// 检验对象修改产品零件
export function updateTestObjectPart(query) {
  return request({
    url: '/structureTestObjectPart/updateTestObjectPart',
    method: 'post',
    data: query
  })
}
// 检验对象删除产品零件
export function deleteTestObjectPart(query) {
  return request({
    url: '/structureTestObjectPart/deleteTestObjectPart',
    method: 'delete',
    params: query
  })
}
// 根据产品id查询零件
export function selectByProductId(query) {
  return request({
    url: '/productPart/selectByProductId',
    method: 'get',
    params: query
  })
}
// 新增产品零件
export function addProductPart(query) {
  return request({
    url: '/productPart/addProductPart',
    method: 'post',
    data: query
  })
}
// 修改产品零件
export function updateProductPart(query) {
  return request({
    url: '/productPart/updateProductPart',
    method: 'post',
    data: query
  })
}
// 删除产品零件
export function deleteProductPart(query) {
  return request({
    url: '/productPart/deleteProductPart',
    method: 'delete',
    params: query
  })
}
