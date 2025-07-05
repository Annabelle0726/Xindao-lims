import request from '@/utils/request'

export function selectPersonnelOverview(query) {
  return request({
    url: '/user/selectPersonnelOverview',
    method: 'get',
    params: query
  })
}
