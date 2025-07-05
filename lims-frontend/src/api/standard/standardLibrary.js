// 标准库相关接口

import request from "@/utils/request";

// 标准库树排序
export function updateTreeSort(data) {
  return request({
    url: "/standardTree/updateTreeSort",
    method: "post",
    data: data,
  });
}

// 标准库检验项排序保存接口
export function resetTreeDragBatch(data) {
  return request({
    url: "/standardTree/resetTreeDragBatch",
    method: "post",
    data: data,
  });
}

// 删除标准树的层级
export function delStandardTree(query) {
  return request({
    url: "/standardTree/delStandardTree",
    method: "delete",
    params: query,
  });
}

// 获取标准树
export function selectStandardTreeList(query) {
  return request({
    url: "/standardTree/selectStandardTreeList",
    method: "get",
    params: query,
  });
}

// 获取实验室名称
export function obtainItemParameterList(query) {
  return request({
    url: "/laboratoryScope/obtainItemParameterList",
    method: "get",
    params: query,
  });
}

// 获取检验对象枚举
export function selectTestObjectByName(query) {
  return request({
    url: "/capacityScope/selectTestObjectByName",
    method: "get",
    params: query,
  });
}

// 添加标准树
export function addStandardTree(data) {
  return request({
    url: "/standardTree/addStandardTree",
    method: "post",
    data: data,
  });
}

// 修改标准树
export function updateStandardTree(data) {
  return request({
    url: "/standardTree/updateStandardTree",
    method: "post",
    data: data,
  });
}

// 根据标准树进行标准查询
export function selectsStandardMethodByFLSSM(query) {
  return request({
    url: "/standardTree/selectsStandardMethodByFLSSM",
    method: "get",
    params: query,
  });
}

// 修改标准库中的要求值
export function upStandardProductList(data) {
  return request({
    url: "/standardTree/upStandardProductList",
    method: "post",
    data: data,
  });
}


// 通过检验标准查询检验项目
export function selectStandardProductListByMethodId(query) {
  return request({
    url: "/standardTree/selectStandardProductListByMethodId",
    method: "get",
    params: query,
  });
}

// 修改标准库中的区间设置
export function updateSection(data) {
  return request({
    url: "/standardTree/updateSection",
    method: "post",
    data: data,
  });
}

// 获取产品架构
export function upStandardProducts(data) {
  return request({
    url: "/standardTree/upStandardProducts",
    method: "post",
    data: data,
  });
}

// 查询原始记录模板枚举
export function getStandardTemplate(query) {
  return request({
    url: "/StandardTemplate/getStandardTemplate",
    method: "get",
    params: query,
  });
}

// 获取标准方法枚举
export function selectStandardMethods(query) {
  return request({
    url: "/standardMethod/selectStandardMethods",
    method: "get",
    params: query,
  });
}

// 批量编辑查询检验项目
export function selectStandardProductByMethodId(data) {
  return request({
    url: "/standardTree/selectStandardProductByMethodId",
    method: "post",
    data: data,
  });
}

// 批量编辑查询所有检验项目和检验子项枚举
export function selectStandardProductEnumByMethodId(data) {
  return request({
    url: "/standardTree/selectStandardProductEnumByMethodId",
    method: "post",
    data: data,
  });
}



// 检验项复制排序
export function copyStandardProductSort(data) {
  return request({
    url: "/standardTree/copyStandardProductSort",
    method: "post",
    data: data,
  });
}

// 提交需要复制的检验项
export function copyStandardProductOne(data) {
  return request({
    url: "/standardTree/copyStandardProductOne",
    method: "post",
    data: data,
  });
}

// 复制检验项
export function copyStandardProductList(data) {
  return request({
    url: "/standardTree/copyStandardProductList",
    method: "post",
    data: data,
  });
}

// 提交需要复制的检验项
export function updateStandardProductListBatch(data) {
  return request({
    url: "/standardTree/updateStandardProductListBatch",
    method: "post",
    data: data,
  });
}

// 获取标准树
export function selectStandardTreeList2(query) {
  return request({
    url: "/standardTree/selectStandardTreeList2",
    method: "get",
    params: query,
  });
}

// 标准库绑定厂家
export function selectSupplierAsk(data) {
  return request({
    url: "/standardTree/selectSupplierAsk",
    method: "post",
    data: data,
  });
}

// 新增标准库绑定厂家
export function addProductSupplierAsk(data) {
  return request({
    url: "/standardTree/addProductSupplierAsk",
    method: "post",
    data: data,
  });
}

// 修改标准库绑定厂家
export function updateProductSupplierAsk(data) {
  return request({
    url: "/standardTree/updateProductSupplierAsk",
    method: "post",
    data: data,
  });
}

// 删除标准库绑定厂家
export function deleteProductSupplierAsk(query) {
  return request({
    url: "/standardTree/deleteProductSupplierAsk",
    method: "delete",
    params: query,
  });
}
