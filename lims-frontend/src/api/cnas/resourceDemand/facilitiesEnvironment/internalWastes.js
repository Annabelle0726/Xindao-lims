import request from '@/utils/request'

// 安全内务三废处理分页查询
export function pageInternalWastes(query) {
  return request({
    url: '/internalWastes/pageInternalWastes',
    method: 'get',
    params: query
  })
}

// 安全内务三废处理查看详情
export function getInternalWastesOne(query) {
  return request({
    url: '/internalWastes/getInternalWastesOne',
    method: 'get',
    params: query
  })
}

// 安全内务三废处理新增
export function addInternalWastes(query) {
  return request({
    url: '/internalWastes/addInternalWastes',
    method: 'post',
    data: query
  })
}

// 安全内务三废处理分页修改
export function updateInternalWastes(query) {
  return request({
    url: '/internalWastes/updateInternalWastes',
    method: 'post',
    data: query
  })
}

// 导出三废处理
export function exportInternalWastes(query) {
  return request({
    url: "/internalWastes/exportInternalWastes",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//安全内务三废处理删除
export function removeStandardSubstance(query) {
  return request({
    url: '/internalWastes/delInternalWastes',
    method: 'delete',
    params: query
  })
}
