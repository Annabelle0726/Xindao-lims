// 体系文件记录相关接口
import request from "@/utils/request";

// 文件审批记录-新增
export function addManageRecordCheck(data) {
  return request({
    url: "/manageRecordCheck/addManageRecordCheck",
    method: "post",
    data: data,
  });
}

// 文件审批记录-修改
export function doManageRecordCheck(data) {
  return request({
    url: "/manageRecordCheck/doManageRecordCheck",
    method: "post",
    data: data,
  });
}

//文件审批记录-导出
export function exportOutManageRecordCheck(query) {
  return request({
    url: "/manageRecordCheck/exportOutManageRecordCheck",
    method: "get",
    params: query,
  });
}

// 文件审批记录-审核
export function checkManageRecordCheck(data) {
  return request({
    url: "/manageRecordCheck/checkManageRecordCheck",
    method: "post",
    data: data,
  });
}

// 文件审批记录-批准
export function ratifyManageRecordCheck(data) {
  return request({
    url: "/manageRecordCheck/ratifyManageRecordCheck",
    method: "post",
    data: data,
  });
}

//文件审批记录-删除
export function delManageRecordCheck(query) {
  return request({
    url: "/manageRecordCheck/delManageRecordCheck",
    method: "delete",
    params: query,
  });
}

//文件审批记录-列表
export function pageManageRecordCheck(query) {
  return request({
    url: "/manageRecordCheck/pageManageRecordCheck",
    method: "get",
    params: query,
  });
}

// 外来文件确认记录-新增
export function addManageRecordVerify(data) {
  return request({
    url: "/manageRecordVerify/addManageRecordVerify",
    method: "post",
    data: data,
  });
}

//外来文件确认记录-填写--列表
export function pageManageRecordVerify(query) {
  return request({
    url: "/manageRecordVerify/pageManageRecordVerify",
    method: "get",
    params: query,
  });
}

// 外来文件确认记录-历史--提交
export function submitManageRecordTotal(data) {
  return request({
    url: "/manageRecordTotal/submitManageRecordTotal",
    method: "post",
    data: data,
  });
}

// 外来文件确认记录-历史--审核
export function ratifyManageRecordTotal(data) {
  return request({
    url: "/manageRecordTotal/ratifyManageRecordTotal",
    method: "post",
    data: data,
  });
}

//外来文件确认记录-填写--删除
export function delManageRecordVerify(query) {
  return request({
    url: "/manageRecordVerify/delManageRecordVerify",
    method: "delete",
    params: query,
  });
}

//外来文件确认记录-历史--列表
export function pageManageRecordTotal(query) {
  return request({
    url: "/manageRecordTotal/pageManageRecordTotal",
    method: "get",
    params: query,
  });
}

// 外来文件确认记录-填写--修改
export function doManageRecordVerify(data) {
  return request({
    url: "/manageRecordVerify/doManageRecordVerify",
    method: "post",
    data: data,
  });
}

//文件发放回收记录-导出
export function exportOutManageRecordIssueRecycle(query) {
  return request({
    url: "/manageRecordIssueRecycle/exportOutManageRecordIssueRecycle",
    method: "get",
    params: query,
  });
}

// 文件发放回收记录-新增
export function addManageRecordIssueRecycle(data) {
  return request({
    url: "/manageRecordIssueRecycle/addManageRecordIssueRecycle",
    method: "post",
    data: data,
  });
}

// 文件发放回收记录-编辑
export function doManageRecordIssueRecycle(data) {
  return request({
    url: "/manageRecordIssueRecycle/doManageRecordIssueRecycle",
    method: "post",
    data: data,
  });
}

//查询CNAS人员侧边栏
export function selectCNSAPersonTree(query) {
  return request({
    url: "/personBasicInfo/selectCNSAPersonTree",
    method: "get",
    params: query,
  });
}

//文件发放回收记录-列表
export function pageManageRecordIssueRecycle(query) {
  return request({
    url: "/manageRecordIssueRecycle/pageManageRecordIssueRecycle",
    method: "get",
    params: query,
  });
}

//文件发放回收记录-删除
export function delManageRecordIssueRecycle(query) {
  return request({
    url: "/manageRecordIssueRecycle/delManageRecordIssueRecycle",
    method: "delete",
    params: query,
  });
}

//文件修订-导出列表
export function exportOutManageRecordAudit(query) {
  return request({
    url: "/manageRecordAudit/exportOutManageRecordAudit",
    method: "get",
    params: query,
  });
}

// 文件修订-新增
export function addManageRecordAudit(data) {
  return request({
    url: "/manageRecordAudit/addManageRecordAudit",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

// 文件修订-编辑
export function doManageRecordAudit(data) {
  return request({
    url: "/manageRecordAudit/doManageRecordAudit",
    method: "post",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    data: data,
  });
}

// 文件修订-批准
export function ratifyManageRecordAudit(data) {
  return request({
    url: "/manageRecordAudit/ratifyManageRecordAudit",
    method: "post",
    data: data,
  });
}

//文件修订-列表
export function pageManageRecordAudit(query) {
  return request({
    url: "/manageRecordAudit/pageManageRecordAudit",
    method: "get",
    params: query,
  });
}

//文件修订-删除
export function delManageRecordAudit(query) {
  return request({
    url: "/manageRecordAudit/delManageRecordAudit",
    method: "delete",
    params: query,
  });
}

//文件定期审查记录-导出
export function exportOutManageRecordIntervals(query) {
  return request({
    url: "/manageRecordIntervals/exportOutManageRecordIntervals",
    method: "get",
    params: query,
  });
}

// 文件定期审查记录-新增
export function addManageRecordIntervals(data) {
  return request({
    url: "/manageRecordIntervals/addManageRecordIntervals",
    method: "post",
    data: data,
  });
}

// 文件定期审查记录-编辑
export function doManageRecordIntervals(data) {
  return request({
    url: "/manageRecordIntervals/doManageRecordIntervals",
    method: "post",
    data: data,
  });
}

// 文件定期审查记录-历史记录-批准
export function ratifyManageRecordIntervalsTotal(data) {
  return request({
    url: "/manageRecordIntervalsTotal/ratifyManageRecordIntervalsTotal",
    method: "post",
    data: data,
  });
}

// 文件定期审查记录-历史记录-提交
export function submitManageRecordIntervalsTotal(data) {
  return request({
    url: "/manageRecordIntervalsTotal/submitManageRecordIntervalsTotal",
    method: "post",
    data: data,
  });
}

//文件定期审查记录-列表
export function pageManageRecordIntervals(query) {
  return request({
    url: "/manageRecordIntervals/pageManageRecordIntervals",
    method: "get",
    params: query,
  });
}

//文件定期审查记录-历史记录-列表
export function pageManageRecordIntervalsTotal(query) {
  return request({
    url: "/manageRecordIntervalsTotal/pageManageRecordIntervalsTotal",
    method: "get",
    params: query,
  });
}

//文件定期审查记录-删除
export function delManageRecordIntervals(query) {
  return request({
    url: "/manageRecordIntervals/delManageRecordIntervals",
    method: "delete",
    params: query,
  });
}

//文件作废记录-导出
export function exportOutManageRecordCancel(query) {
  return request({
    url: "/manageRecordCancel/exportOutManageRecordCancel",
    method: "get",
    params: query,
  });
}

// 文件作废记录-新增
export function addManageRecordCancel(data) {
  return request({
    url: "/manageRecordCancel/addManageRecordCancel",
    method: "post",
    data: data,
  });
}

// 文件作废记录-编辑
export function doManageRecordCancel(data) {
  return request({
    url: "/manageRecordCancel/doManageRecordCancel",
    method: "post",
    data: data,
  });
}

// 文件作废记录-批准
export function ratifyManageRecordCancel(data) {
  return request({
    url: "/manageRecordCancel/ratifyManageRecordCancel",
    method: "post",
    data: data,
  });
}

//文件作废记录-删除
export function delManageRecordCancel(query) {
  return request({
    url: "/manageRecordCancel/delManageRecordCancel",
    method: "delete",
    params: query,
  });
}

//文件作废记录-列表
export function pageManageRecordCancel(query) {
  return request({
    url: "/manageRecordCancel/pageManageRecordCancel",
    method: "get",
    params: query,
  });
}
