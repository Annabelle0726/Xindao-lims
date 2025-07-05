// 成品下单详情页面相关接口
import request from '@/utils/request'

// 成品下单界面查询季度信息
export function getQuarterOnOrder() {
  return request({
    url: '/finishProductSpotCheck/getQuarterOnOrder',
    method: 'get'
  })
}
// 修改成品下单内容
export function updateInsOrder(query) {
  return request({
    url: '/insOrder/updateInsOrder',
    method: 'post',
    data: query
  })
}
// 添加检验下单数据
export function addInsOrder(query) {
  return request({
    url: '/insOrder/addInsOrder',
    method: 'post',
    data: query
  })
}
// 审核检验单机进行状态修改
export function upInsOrderOfState(query) {
  return request({
    url: '/insOrder/upInsOrderOfState',
    method: 'post',
    data: query
  })
}
// 查询订单总共预计时间
export function selectOrderManDay(query) {
  return request({
    url: '/insOrder/selectOrderManDay',
    method: 'get',
    params: query
  })
}
// 检验下发
export function upInsOrder(query) {
  return request({
    url: '/insOrder/upInsOrder',
    method: 'post',
    params: query
  })
}
// 删除检验单模板
export function delInsOrderTemplate(query) {
  return request({
    url: '/insOrder/delInsOrderTemplate',
    method: 'delete',
    params: query
  })
}
// 添加检验单模板
export function addInsOrderTemplate(query) {
  return request({
    url: '/insOrder/addInsOrderTemplate',
    method: 'post',
    data: query
  })
}
// 通过检验单模板id获取检验单模板内容
export function selectInsOrderTemplateById(query) {
  return request({
    url: '/insOrder/selectInsOrderTemplateById',
    method: 'get',
    params: query
  })
}
