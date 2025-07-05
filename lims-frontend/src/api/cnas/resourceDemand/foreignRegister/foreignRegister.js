import request from '@/utils/request'

// 外来人员分页查询
export function pageForeignRegister(query) {
  return request({
    url: '/foreignRegister/pageForeignRegister',
    method: 'get',
    params: query
  })
}

// 外来人员查看详情
export function getForeignRegisterOne(query) {
  return request({
    url: '/foreignRegister/getForeignRegisterOne',
    method: 'get',
    params: query
  })
}

// 获取用户列表
export function selectUserCondition(query) {
  return request({
    url: "/system/newUser/selectUserCondition",
    method: "get",
    params: query,
  });
}

// 外来人员新增
export function addForeignRegister(query) {
  return request({
    url: '/foreignRegister/addForeignRegister',
    method: 'post',
    data: query
  })
}

// 外来人员修改
export function updateForeignRegister(query) {
  return request({
    url: '/foreignRegister/updateForeignRegister',
    method: 'post',
    data: query
  })
}

//外来人员删除
export function delForeignRegister(query) {
  return request({
    url: '/foreignRegister/delForeignRegister',
    method: 'delete',
    params: query
  })
}

// 导出外来人员
export function exportForeignRegister(query) {
  return request({
    url: "/foreignRegister/exportForeignRegister",
    method: "get",
    responseType: "blob",
    params: query,
  });
}
