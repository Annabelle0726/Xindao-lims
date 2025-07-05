// 首页相关接口
import request from '@/utils/request'

//首页-->日历任务图
export function calendarWorkByWeek() {
  return request({
    url: '/report/calendarWorkByWeek',
    method: 'get'
  })
}
//首页-->首页工时统计
export function currentUserWorkHourCount(query) {
  return request({
    url: '/report/currentUserWorkHourCount',
    method: 'get',
    params: query
  })
}
//首页-->首页工时统计
export function msgRoll(query) {
  return request({
    url: '/informationNotification/msgRoll',
    method: 'get',
    params: query
  })
}
//首页-->修改待办事项状态
export function triggerModificationStatusToRead(query) {
  return request({
    url: '/informationNotification/triggerModificationStatusToRead',
    method: 'post',
    data: query
  })
}
