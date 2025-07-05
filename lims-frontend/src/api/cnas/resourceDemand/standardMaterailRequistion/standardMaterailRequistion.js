import request from '@/utils/request'

// 分页查询
export function getPageSubstanceRecord(query) {
  return request({
    url: '/feStandardSubstanceRecord/getPageSubstanceRecord',
    method: 'get',
    params: query
  })
}


