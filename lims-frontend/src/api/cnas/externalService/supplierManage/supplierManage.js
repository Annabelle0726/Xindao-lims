import request from '@/utils/request'

// 分页查询
export function selectQualifiedSupplierManagementPage(query) {
  return request({
    url: '/supplierManagement/selectQualifiedSupplierManagementPage',
    method: 'get',
    params: query
  })
}

// 编辑供应商
export function updateSupplierManagement(query) {
  return request({
    url: '/supplierManagement/updateSupplierManagement',
    method: 'post',
    data: query
  })
}

// 新增供应商
export function addSupplierManagement(query) {
  return request({
    url: '/supplierManagement/addSupplierManagement',
    method: 'post',
    data: query
  })
}

// 导出供应商
export function exportSupplierManagement(query) {
  return request({
    url: "/supplierManagement/exportSupplierManagement",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 删除供应商
export function delSupplierManagement(query) {
  return request({
    url: '/supplierManagement/delSupplierManagement',
    method: 'delete',
    params: query
  })
}





