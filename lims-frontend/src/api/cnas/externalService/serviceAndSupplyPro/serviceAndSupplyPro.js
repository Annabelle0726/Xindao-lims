import request from '@/utils/request'

// 服务和供应品采购
export function procurementSuppliesList(query) {
  return request({
    url: '/procurementSuppliesList/procurementSuppliesList',
    method: 'get',
    params: query
  })
}

export function deleteProcurementSuppliesList(query) {
  return request({
    url: '/procurementSuppliesList/deleteProcurementSuppliesList',
    method: 'delete',
    params: query
  })
}

export function exportProcurementSuppliesList(query) {
  return request({
    url: "/procurementSuppliesList/exportProcurementSuppliesList",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

export function addProcurementSuppliesList(query) {
  return request({
    url: '/procurementSuppliesList/addProcurementSuppliesList',
    method: 'post',
    data: query,
  })
}

export function selectEnumByCategory(query) {
  return request({
    url: '/enum/selectEnumByCategory',
    method: 'post',
    data: query,
  })
}

export function updateProcurementSuppliesList(query) {
  return request({
    url: '/procurementSuppliesList/updateProcurementSuppliesList',
    method: 'post',
    data: query,
  })
}

// 服务和供应品采购
export function selectSupplierManagementAll(query) {
  return request({
    url: '/supplierManagement/selectSupplierManagementAll',
    method: 'get',
    params: query
  })
}

export function addProcurementSuppliesExpends(query) {
  return request({
    url: '/procurementSuppliesExpends/addProcurementSuppliesExpends',
    method: 'post',
    data: query,
  })
}

export function deleteProcurementSuppliesExpends(query) {
  return request({
    url: '/procurementSuppliesExpends/deleteProcurementSuppliesExpends',
    method: 'delete',
    params: query
  })
}

export function procurementSuppliesExpendlist(query) {
  return request({
    url: '/procurementSuppliesExpends/procurementSuppliesExpendlist/',
    method: 'get',
    params: query
  })
}

export function exportProcurementSuppliesStoreExcel(query) {
  return request({
    url: '/procurementSuppliesStore/exportExcel',
    method: "get",
    responseType: "blob",
    params: query,
  })
}

// 新增节点
export function addSuppliersDirectoryContents(query) {
  return request({
    url: '/suppliersDirectoryContents/addSuppliersDirectoryContents',
    method: 'post',
    data: query,
  })
}

// 编辑节点
export function updateSuppliersDirectoryContents(query) {
  return request({
    url: '/suppliersDirectoryContents/updateSuppliersDirectoryContents',
    method: 'post',
    data: query,
  })
}

// 删除节点
export function deleteSuppliersDirectoryContentsById(query) {
  return request({
    url: '/suppliersDirectoryContents/deleteSuppliersDirectoryContentsById',
    method: 'delete',
    params: query
  })
}

// 采购物资目录删除
export function deleteProcurementSuppliesContentById(query) {
  return request({
    url: '/procurementSuppliesContents/deleteProcurementSuppliesContentById',
    method: 'delete',
    params: query
  })
}

// 查询所有节点
export function getSuppliersDirectoryContentsNodeNames(query) {
  return request({
    url: '/suppliersDirectoryContents/getSuppliersDirectoryContentsNodeNames',
    method: 'get',
    params: query
  })
}

// 采购物资目录获取节点名称
export function getProcurementSuppliesContentsNodeNames(query) {
  return request({
    url: '/procurementSuppliesContents/getNodeNames',
    method: 'get',
    params: query
  })
}

//获取用户列表
export function selectUserCondition(query) {
  return request({
    url: "/system/newUser/selectUserCondition",
    method: "get",
    params: query,
  });
}

// 采购物资目录详情
export function selectProcurementSuppliesContentById(query) {
  return request({
    url: "/procurementSuppliesContents/selectProcurementSuppliesContentById",
    method: "get",
    params: query,
  });
}

// 采购物资目录列表
export function directoryListing(query) {
  return request({
    url: "/procurementSuppliesContents/directoryListing",
    method: "get",
    params: query,
  });
}

// 耗材入库修改
export function updateStore(query) {
  return request({
    url: '/procurementSuppliesStore/updateStore',
    method: 'post',
    data: query,
  })
}

// 耗材入库新增
export function addStore(query) {
  return request({
    url: '/procurementSuppliesStore/addStore',
    method: 'post',
    data: query,
  })
}

// 根据id查询耗材入库
export function selectStoreById(query) {
  return request({
    url: "/procurementSuppliesStore/selectStoreById",
    method: "get",
    params: query,
  });
}

// 耗材入库列表
export function storeList(query) {
  return request({
    url: "/procurementSuppliesStore/storeList",
    method: "get",
    params: query,
  });
}

// 耗材入库删除
export function deleteStore(query) {
  return request({
    url: '/procurementSuppliesStore/deleteStore',
    method: 'delete',
    params: query
  })
}

// 采购物资目录新增
export function addProcurementSuppliesContents(query) {
  return request({
    url: '/procurementSuppliesContents/addProcurementSuppliesContents',
    method: 'post',
    data: query,
  })
}


