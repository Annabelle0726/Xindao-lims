import request from '@/utils/request'

// 获取实验室名称
export function getPageStandardSubstance(query) {
  return request({
    url: '/feStandardSubstance/getPageStandardSubstance',
    method: 'get',
    params: query
  })
}

//标准物质清单新增编辑
export function addStandardSubstance(query) {
  return request({
    url: '/feStandardSubstance/addStandardSubstance',
    method: 'post',
    data: query
  })
}

//删除标准物质清单
export function removeStandardSubstance(query) {
  return request({
    url: '/feStandardSubstance/removeStandardSubstance',
    method: 'delete',
    params: query
  })
}

// 文件变更-导出
export function exportOfStandardSubstanceList(query) {
  return request({
    url: "/feStandardSubstance/exportOfStandardSubstanceList",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//标准物质清单借用
export function borrowSubstance(query) {
  return request({
    url: '/feStandardSubstanceRecord/borrowSubstance',
    method: 'post',
    data: query
  })
}

//标准物质清单归还
export function returnSubstance(query) {
  return request({
    url: '/feStandardSubstanceRecord/returnSubstance',
    method: 'post',
    data: query
  })
}



