import request from '@/utils/request'

export function getPageAcceptance(query) {
  return request({
    url: '/feStandardSubstanceAcceptance/getPageAcceptance',
    method: 'get',
    params: query
  })
}

export function updateAcceptanc(query) {
  return request({
    url: '/feStandardSubstanceAcceptance/updateAcceptanc',
    method: 'post',
    data: query
  })
}
// 新增验收
export function addAcceptance(query) {
  return request({
    url: '/feStandardSubstanceAcceptance/addAcceptance',
    method: 'post',
    data: query
  })
}
// 修改验收
export function updateAcceptance(query) {
  return request({
    url: '/feStandardSubstanceAcceptance/updateAcceptance',
    method: 'post',
    data: query
  })
}

/**
 * @desc 标准物质清单
 */
export function getStandardSubstanceAll(query) {
  return request({
    url: '/feStandardSubstance/getStandardSubstanceAll',
    method: 'get',
    params: query
  })
}
// 标准物质验收查看详情
export function getAcceptanceDetails(query) {
  return request({
    url: '/feStandardSubstanceAcceptance/getAcceptanceDetails',
    method: 'get',
    params: query
  })
}
// 标准物质验收导出
export function exportFeStandardSubstanceAcceptance(query) {
  return request({
    url: '/feStandardSubstanceAcceptance/exportFeStandardSubstanceAcceptance',
    method: 'get',
    params: query,
    responseType: "blob"
  })
}







