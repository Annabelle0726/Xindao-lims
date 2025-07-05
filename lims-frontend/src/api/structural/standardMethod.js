import request from '@/utils/request'

// 获取检验项目参数
export function selectStandardMethods() {
  return request({
    url: '/standardMethod/selectStandardMethods',
    method: 'get'
  })
}
