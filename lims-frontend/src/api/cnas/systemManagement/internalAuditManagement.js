// 内审管理相关接口
import request from "@/utils/request";

//年度计划-分页
export function pageInternalPlan(query) {
  return request({
    url: "/internalPlan/pageInternalPlan",
    method: "get",
    params: query,
  });
}

//年度计划-删除
export function delInternalPlan(query) {
  return request({
    url: "/internalPlan/delInternalPlan",
    method: "delete",
    params: query,
  });
}

//年度计划-导出
export function exportInternalPlan(query) {
  return request({
    url: "/internalPlan/exportInternalPlan",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//年度计划详情
export function getInternalPlanOne(query) {
  return request({
    url: "/internalPlan/getInternalPlanOne",
    method: "get",
    params: query,
  });
}

// 年度计划-新增
export function addInternalPlan(data) {
  return request({
    url: "/internalPlan/addInternalPlan",
    method: "post",
    data: data,
  });
}

// 年度计划-修改
export function updateInternalPlan(data) {
  return request({
    url: "/internalPlan/updateInternalPlan",
    method: "post",
    data: data,
  });
}

// 年度计划-审核
export function examineInternalPlan(data) {
  return request({
    url: "/internalPlan/examineInternalPlan",
    method: "post",
    data: data,
  });
}

// 年度计划-批准
export function ratifyInternalPlan(data) {
  return request({
    url: "/internalPlan/ratifyInternalPlan",
    method: "post",
    data: data,
  });
}

//内审实施计划分页查询
export function pageInternalImplement(query) {
  return request({
    url: "/internalImplement/pageInternalImplement",
    method: "get",
    params: query,
  });
}

//内审实施计划删除
export function delInternalImplement(query) {
  return request({
    url: "/internalImplement/delInternalImplement",
    method: "delete",
    params: query,
  });
}

//导出内审实施计划
export function exportInternalImplement(query) {
  return request({
    url: "/internalImplement/exportInternalImplement",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//内审实施计划详情
export function getInternalImplementOne(query) {
  return request({
    url: "/internalImplement/getInternalImplementOne",
    method: "get",
    params: query,
  });
}

// 内审实施计划新增
export function addInternalImplement(data) {
  return request({
    url: "/internalImplement/addInternalImplement",
    method: "post",
    data: data,
  });
}

// 内审实施计划修改
export function updateInternalImplement(data) {
  return request({
    url: "/internalImplement/updateInternalImplement",
    method: "post",
    data: data,
  });
}

// 内审实施计划修改
export function ratifyInternalImplement(data) {
  return request({
    url: "/internalImplement/ratifyInternalImplement",
    method: "post",
    data: data,
  });
}

//会议签到查询
export function pageInternalMeeting(query) {
  return request({
    url: "/internalMeeting/pageInternalMeeting",
    method: "get",
    params: query,
  });
}

// 会议签到删除
export function delInternalMeeting(query) {
  return request({
    url: "/internalMeeting/delInternalMeeting",
    method: "delete",
    params: query,
  });
}

//导出内审会议
export function exportInternalMeeting(query) {
  return request({
    url: "/internalMeeting/exportInternalMeeting",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//会议签到查询详情
export function getInternalMeetingOne(query) {
  return request({
    url: "/internalMeeting/getInternalMeetingOne",
    method: "get",
    params: query,
  });
}

// 新增会议签到
export function addInternalMeeting(data) {
  return request({
    url: "/internalMeeting/addInternalMeeting",
    method: "post",
    data: data,
  });
}

// 会议签到修改
export function updateInternalMeeting(data) {
  return request({
    url: "/internalMeeting/updateInternalMeeting",
    method: "post",
    data: data,
  });
}

//内审检查分页查询
export function pageInternalCheck(query) {
  return request({
    url: "/internalCheck/pageInternalCheck",
    method: "get",
    params: query,
  });
}

//内审检查删除
export function delInternalCheck(query) {
  return request({
    url: "/internalCheck/delInternalCheck",
    method: "delete",
    params: query,
  });
}

//导出内审检查
export function exportInternalCheck(query) {
  return request({
    url: "/internalCheck/exportInternalCheck",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//内审检查详情
export function getInternalCheckOne(query) {
  return request({
    url: "/internalCheck/getInternalCheckOne",
    method: "get",
    params: query,
  });
}

// 内审检查新增
export function addInternalCheck(data) {
  return request({
    url: "/internalCheck/addInternalCheck",
    method: "post",
    data: data,
  });
}

// 内审检查修改
export function updateInternalCheck(data) {
  return request({
    url: "/internalCheck/updateInternalCheck",
    method: "post",
    data: data,
  });
}

// 内审检查批准
export function ratifyInternalCheck(data) {
  return request({
    url: "/internalCheck/ratifyInternalCheck",
    method: "post",
    data: data,
  });
}

//查询内审管理纠正措施列表
export function pageInternalCorrect(query) {
  return request({
    url: "/internalCorrect/pageInternalCorrect",
    method: "get",
    params: query,
  });
}

//导出内审管理纠正措施附件
export function exportInternalCorrect(query) {
  return request({
    url: "/internalCorrect/exportInternalCorrect",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//查询内审管理纠正处理详情
export function getInternalCorrect(query) {
  return request({
    url: "/internalCorrect/getInternalCorrect",
    method: "get",
    params: query,
  });
}

// 提交内审管理纠正措施列表
export function addInternalCorrect(data) {
  return request({
    url: "/internalCorrect/addInternalCorrect",
    method: "post",
    data: data,
  });
}

//内审报告删除
export function delInternalReport(query) {
  return request({
    url: "/internalReport/delInternalReport",
    method: "delete",
    params: query,
  });
}

//内审报告分页查询
export function pageInternalReport(query) {
  return request({
    url: "/internalReport/pageInternalReport",
    method: "get",
    params: query,
  });
}

//导出内审报告
export function exportInternalReport(query) {
  return request({
    url: "/internalReport/exportInternalReport",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//内审报告详情
export function getInternalReportOne(query) {
  return request({
    url: "/internalReport/getInternalReportOne",
    method: "get",
    params: query,
  });
}

// 内审报告新增
export function addInternalReport(data) {
  return request({
    url: "/internalReport/addInternalReport",
    method: "post",
    data: data,
  });
}

// 内审报告修改
export function updateInternalReport(data) {
  return request({
    url: "/internalReport/updateInternalReport",
    method: "post",
    data: data,
  });
}

// 内审报告审核
export function examineInternalReport(data) {
  return request({
    url: "/internalReport/examineInternalReport",
    method: "post",
    data: data,
  });
}

// 内审报告负责人填写
export function qualityInternalReport(data) {
  return request({
    url: "/internalReport/qualityInternalReport",
    method: "post",
    data: data,
  });
}

//查询内审管理纠正措施附件
export function getInternalCorrectFileList(query) {
  return request({
    url: "/internalCorrect/getInternalCorrectFileList",
    method: "get",
    params: query,
  });
}

//删除内审管理纠正措施附件
export function delInternalCorrectFile(query) {
  return request({
    url: "/internalCorrect/delInternalCorrectFile",
    method: "delete",
    params: query,
  });
}
