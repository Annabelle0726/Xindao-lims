// 不确定度评定相关接口
import request from "@/utils/request";

//设备树形
export function treeDevice(query) {
  return request({
    url: "/deviceScope/treeDevice",
    method: "get",
    params: query,
  });
}

//查询设备详情列表
export function selectDeviceParameter(query) {
  return request({
    url: "/deviceScope/selectDeviceParameter",
    method: "get",
    params: query,
  });
}

//获取实验室名称
export function obtainItemParameterList(query) {
  return request({
    url: "/laboratoryScope/obtainItemParameterList",
    method: "get",
    params: query,
  });
}

//设备工具明细导出
export function exportEquipmentDetails(query) {
  return request({
    url: "/deviceScope/exportEquipmentDetails",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//设备里面选择检验项目(树形结构)
export function getInsProduction(query) {
  return request({
    url: "/capacityScope/getInsProduction",
    method: "get",
    params: query,
  });
}

// 修改设备详情参数
export function upDeviceParameter(data) {
  return request({
    url: "/deviceScope/upDeviceParameter",
    method: "post",
    data: data,
  });
}

// 添加设备详情参数
export function addDeviceParameter(data) {
  return request({
    url: "/deviceScope/addDeviceParameter",
    method: "post",
    data: data,
  });
}

// 维护数采配置
export function numberCollect(data) {
  return request({
    url: "/deviceScope/numberCollect",
    method: "post",
    data: data,
  });
}

//设备预约接口-列表
export function getDeviceParameter(query) {
  return request({
    url: "/deviceScope/selectDeviceParameter",
    method: "get",
    params: query,
  });
}

//删除设备详情参数
export function delDeviceParameter(query) {
  return request({
    url: "/deviceScope/delDeviceParameter",
    method: "delete",
    params: query,
  });
}

// 查询数采配置
export function queryDataAcquisitionConfiguration(query) {
  return request({
    url: "/deviceScope/queryDataAcquisitionConfiguration",
    method: "get",
    params: query,
  });
}
// 查询数采配置
export function queryProductConfiguration(query) {
  return request({
    url: "/deviceScope/queryProductConfiguration",
    method: "get",
    params: query,
  });
}
// 数采配置--查询检验项
export function getNoConfigProduct(query) {
  return request({
    url: "/deviceScope/getNoConfigProduct",
    method: "get",
    params: query,
  });
}

// 维护数采配置
export function saveDataAcquisitionConfiguration(data) {
  return request({
    url: "/deviceScope/saveDataAcquisitionConfiguration",
    method: "post",
    data: data,
  });
}
// 维护文件配置
export function saveDeviceFileConfiguration(data) {
  return request({
    url: "/deviceScope/saveDeviceFileConfiguration",
    method: "post",
    data: data,
  });
}

//删除数采配置
export function deleteDataAcquisitionConfiguration(query) {
  return request({
    url: "/deviceScope/deleteDataAcquisitionConfiguration",
    method: "delete",
    params: query,
  });
}

//设备校准计划列表查询
export function pageDeviceCalibrationPlan(query) {
  return request({
    url: "/deviceCalibrationPlan/pageDeviceCalibrationPlan",
    method: "get",
    params: query,
  });
}

// 提交批准通知
export function submiatRatifyDeviceCalibrationPlan(data) {
  return request({
    url: "/deviceCalibrationPlan/submiatRatifyDeviceCalibrationPlan",
    method: "post",
    data: data,
  });
}

//设备校准计划导出
export function exportDeviceCalibrationPlanDetail(query) {
  return request({
    url: "/deviceCalibrationPlan/exportDeviceCalibrationPlanDetail",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 设备校准 新增 更新
export function addOrUpdateDeviceMetricRecord(data) {
  return request({
    url: "/deviceMetricRecord/addOrUpdateDeviceMetricRecord",
    method: "post",
    data: data,
  });
}

//设备校准计划删除
export function delDeviceCalibrationPlan(query) {
  return request({
    url: "/deviceCalibrationPlan/delDeviceCalibrationPlan",
    method: "delete",
    params: query,
  });
}

//设备校准计划详情删除
export function delDeviceCalibrationPlanDetail(query) {
  return request({
    url: "/deviceCalibrationPlan/delDeviceCalibrationPlanDetail",
    method: "delete",
    params: query,
  });
}

//设备校准计划详情列表
export function pageDeviceCalibrationPlanDetail(query) {
  return request({
    url: "/deviceCalibrationPlan/pageDeviceCalibrationPlanDetail",
    method: "get",
    params: query,
  });
}

// 新增设备校准计划详情
export function addDeviceCalibrationPlanDetail(data) {
  return request({
    url: "/deviceCalibrationPlan/addDeviceCalibrationPlanDetail",
    method: "post",
    data: data,
  });
}

// 修改设备校准计划详情
export function updateDeviceCalibrationPlanDetail(data) {
  return request({
    url: "/deviceCalibrationPlan/updateDeviceCalibrationPlanDetail",
    method: "post",
    data: data,
  });
}

// 设备校准计划批准
export function ratifyDeviceCalibrationPlan(data) {
  return request({
    url: "/deviceCalibrationPlan/ratifyDeviceCalibrationPlan",
    method: "post",
    data: data,
  });
}

//查询设备校准计划详情
export function getDeviceCalibrationPlan(query) {
  return request({
    url: "/deviceCalibrationPlan/getDeviceCalibrationPlan",
    method: "get",
    params: query,
  });
}

// 新增设备校准计划
export function addDeviceCalibrationPlan(data) {
  return request({
    url: "/deviceCalibrationPlan/addDeviceCalibrationPlan",
    method: "post",
    data: data,
  });
}

// 修改设备校准计划
export function updateDeviceCalibrationPlan(data) {
  return request({
    url: "/deviceCalibrationPlan/updateDeviceCalibrationPlan",
    method: "post",
    data: data,
  });
}

//通过设备分类获取设备列表
export function deviceScopeSearch(query) {
  return request({
    url: "/deviceScope/search",
    method: "get",
    params: query,
  });
}

//查询设备核查计划详情
export function getDeviceExaminePlan(query) {
  return request({
    url: "/deviceExaminePlan/getDeviceExaminePlan",
    method: "get",
    params: query,
  });
}

// 添加设备核查计划
export function addDeviceExaminePlan(data) {
  return request({
    url: "/deviceExaminePlan/addDeviceExaminePlan",
    method: "post",
    data: data,
  });
}

// 设备核查计划批量编辑
export function updateDeviceExaminePlan(data) {
  return request({
    url: "/deviceExaminePlan/updateDeviceExaminePlan",
    method: "post",
    data: data,
  });
}

//设备核查计划删除
export function delDeviceExaminePlan(query) {
  return request({
    url: "/deviceExaminePlan/delDeviceExaminePlan",
    method: "delete",
    params: query,
  });
}

//设备核查计划列表
export function pageDeviceExaminePlan(query) {
  return request({
    url: "/deviceExaminePlan/pageDeviceExaminePlan",
    method: "get",
    params: query,
  });
}

// 设备核查计划提交批准通知
export function submitRatifyDeviceExaminePlan(data) {
  return request({
    url: "/deviceExaminePlan/submitRatifyDeviceExaminePlan",
    method: "post",
    data: data,
  });
}

//导出设备核查计划
export function exportDeviceExaminePlanDetail(query) {
  return request({
    url: "/deviceExaminePlan/exportDeviceExaminePlanDetail",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除设备核查计划详情
export function delDeviceExaminePlanDetail(query) {
  return request({
    url: "/deviceExaminePlan/delDeviceExaminePlanDetail",
    method: "delete",
    params: query,
  });
}

//设备核查计划详情列表
export function pageDeviceExaminePlanDetail(query) {
  return request({
    url: "/deviceExaminePlan/pageDeviceExaminePlanDetail",
    method: "get",
    params: query,
  });
}

// 新增设备核查计划详情
export function addDeviceExaminePlanDetail(data) {
  return request({
    url: "/deviceExaminePlan/addDeviceExaminePlanDetail",
    method: "post",
    data: data,
  });
}

// 修改设备核查计划详情
export function updateDeviceExaminePlanDetail(data) {
  return request({
    url: "/deviceExaminePlan/updateDeviceExaminePlanDetail",
    method: "post",
    data: data,
  });
}

// 设备核查计划批准
export function ratifyDeviceExaminePlan(data) {
  return request({
    url: "/deviceExaminePlan/ratifyDeviceExaminePlan",
    method: "post",
    data: data,
  });
}

//核查记录导出
export function exportReviewExamineRecordDetail(query) {
  return request({
    url: "/deviceExaminePlan/exportReviewExamineRecordDetail",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//核查对比导出
export function exportReviewExamineRecordContrast(query) {
  return request({
    url: "/deviceExaminePlan/exportReviewExamineRecordContrast",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//查询核查对比记录
export function getExamineRecordContrast(query) {
  return request({
    url: "/deviceExaminePlan/getExamineRecordContrast",
    method: "get",
    params: query,
  });
}

// 审核核查对比记录
export function reviewExamineRecordContrast(data) {
  return request({
    url: "/deviceExaminePlan/reviewExamineRecordContrast",
    method: "post",
    data: data,
  });
}

// 新增核查对比记录
export function addExamineRecordContrast(data) {
  return request({
    url: "/deviceExaminePlan/addExamineRecordContrast",
    method: "post",
    data: data,
  });
}

//查询核查记录
export function getExamineRecord(query) {
  return request({
    url: "/deviceExaminePlan/getExamineRecord",
    method: "get",
    params: query,
  });
}

// 新增核查记录
export function addExamineRecord(data) {
  return request({
    url: "/deviceExaminePlan/addExamineRecord",
    method: "post",
    data: data,
  });
}

// 复核核查记录
export function reviewExamineRecord(data) {
  return request({
    url: "/deviceExaminePlan/reviewExamineRecord",
    method: "post",
    data: data,
  });
}

//分页查询设备保养计划
export function selectDeviceMaintenancePlanByPage(query) {
  return request({
    url: "/deviceMaintenancePlan/selectDeviceMaintenancePlanByPage",
    method: "get",
    params: query,
  });
}

// 设备保养计划提交审核
export function submitReviewMaintenancePlanStatus(data) {
  return request({
    url: "/deviceMaintenancePlan/submitReviewMaintenancePlanStatus",
    method: "post",
    data: data,
  });
}

//查询设备保养计划详情
export function getMaintenancePlanDetail(query) {
  return request({
    url: "/deviceMaintenancePlan/getMaintenancePlanDetail",
    method: "get",
    params: query,
  });
}

// 新增设备保养计划
export function addMaintenancePlan(data) {
  return request({
    url: "/deviceMaintenancePlan/addMaintenancePlan",
    method: "post",
    data: data,
  });
}

// 修改设备保养计划
export function updateMaintenancePlan(data) {
  return request({
    url: "/deviceMaintenancePlan/updateMaintenancePlan",
    method: "post",
    data: data,
  });
}

//导出设备保养计划
export function exportDeviceMaintenancePlan(query) {
  return request({
    url: "/deviceMaintenancePlan/exportDeviceMaintenancePlan",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除设备保养计划
export function deleteMaintenancePlan(query) {
  return request({
    url: "/deviceMaintenancePlan/deleteMaintenancePlan",
    method: "delete",
    params: query,
  });
}

//分页查询量值溯源计划
export function selectDeviceTraceabilityManagementByPage(query) {
  return request({
    url: "/deviceTraceabilityManagement/selectDeviceTraceabilityManagementByPage",
    method: "get",
    params: query,
  });
}

// 量值溯源计划提交批准
export function submitReviewTraceabilityManagementStatus(data) {
  return request({
    url: "/deviceTraceabilityManagement/submitReviewTraceabilityManagementStatus",
    method: "post",
    data: data,
  });
}

//查询量值溯源计划详情
export function getTraceabilityManagementDetail(query) {
  return request({
    url: "/deviceTraceabilityManagement/getTraceabilityManagementDetail",
    method: "get",
    params: query,
  });
}

// 新增量值溯源计划
export function addTraceabilityManagement(data) {
  return request({
    url: "/deviceTraceabilityManagement/addTraceabilityManagement",
    method: "post",
    data: data,
  });
}

// 修改量值溯源计划
export function updateTraceabilityManagement(data) {
  return request({
    url: "/deviceTraceabilityManagement/updateTraceabilityManagement",
    method: "post",
    data: data,
  });
}

// 量值溯源计划审核状态修改
export function reviewTraceabilityManagementStatus(data) {
  return request({
    url: "/deviceTraceabilityManagement/reviewTraceabilityManagementStatus",
    method: "post",
    data: data,
  });
}

//导出量值溯源计划
export function exportDeviceTraceabilityManagement(query) {
  return request({
    url: "/deviceTraceabilityManagement/exportDeviceTraceabilityManagement",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除量值溯源计划
export function deleteTraceabilityManagement(query) {
  return request({
    url: "/deviceTraceabilityManagement/deleteTraceabilityManagement",
    method: "delete",
    params: query,
  });
}

//利用外部设备申请列表
export function pageDeviceExternalApply(query) {
  return request({
    url: "/deviceExternalApply/pageDeviceExternalApply",
    method: "get",
    params: query,
  });
}

//借用外部仪器-导出
export function exportDeviceExternalApply(query) {
  return request({
    url: "/deviceExternalApply/exportDeviceExternalApply",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除利用外部设备申请
export function delDeviceExternalApply(query) {
  return request({
    url: "/deviceExternalApply/delDeviceExternalApply",
    method: "delete",
    params: query,
  });
}

//查询利用外部设备申请
export function getDeviceExternalApply(query) {
  return request({
    url: "/deviceExternalApply/getDeviceExternalApply",
    method: "get",
    params: query,
  });
}

// 新增利用外部设备申请
export function addDeviceExternalApply(data) {
  return request({
    url: "/deviceExternalApply/addDeviceExternalApply",
    method: "post",
    data: data,
  });
}

//cnas设备使用记录分页查询
export function deviceRecordPage(query) {
  return request({
    url: "/deviceRecord/deviceRecordPage",
    method: "get",
    params: query,
  });
}

// 设备使用记录保存
export function saveDeviceRecord(data) {
  return request({
    url: "/deviceRecord/saveDeviceRecord",
    method: "post",
    data: data,
  });
}

// 设备使用记录编辑
export function updateDeviceRecord(data) {
  return request({
    url: "/deviceRecord/updateDeviceRecord",
    method: "post",
    data: data,
  });
}

//删除设备使用记录
export function deleteDeviceRecord(query) {
  return request({
    url: "/deviceRecord/deleteDeviceRecord",
    method: "delete",
    params: query,
  });
}
//使用记录 导出
export function exportUseRecord(query) {
  return request({
    url: "/deviceRecord/exportUseRecord",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//新增设备档案
export function addDocument(data) {
  return request({
    url: "/deviceDocuments/addDocument",
    method: "post",
    data: data,
  });
}

// 获取相关文档数据的api-更新
export function updateDocument(data) {
  return request({
    url: "/deviceDocuments/updateDocument",
    method: "post",
    data: data,
  });
}

//删除设备档案
export function deleteDocumentById(query) {
  return request({
    url: "/deviceDocuments/deleteDocumentById",
    method: "delete",
    params: query,
  });
}
//

//设备验收列表
export function pageDeviceAcceptance(query) {
  return request({
    url: "/deviceAcceptance/pageDeviceAcceptance",
    method: "get",
    params: query,
  });
}

//设备验收附件列表
export function getDeviceAcceptanceFileList(query) {
  return request({
    url: "/deviceAcceptance/getDeviceAcceptanceFileList",
    method: "get",
    params: query,
  });
}

//设备验收附件删除
export function delDeviceAcceptanceFileList(query) {
  return request({
    url: "/deviceAcceptance/delDeviceAcceptanceFileList",
    method: "delete",
    params: query,
  });
}

//设备验收（装备）导出
export function exportDeviceAcceptance(query) {
  return request({
    url: "/deviceAcceptance/exportDeviceAcceptance",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除设备验收
export function delDeviceAcceptance(query) {
  return request({
    url: "/deviceAcceptance/delDeviceAcceptance",
    method: "delete",
    params: query,
  });
}

//查询设备验收
export function getDeviceAcceptance(query) {
  return request({
    url: "/deviceAcceptance/getDeviceAcceptance",
    method: "get",
    params: query,
  });
}

// 新增设备验收
export function addDeviceAcceptance(data) {
  return request({
    url: "/deviceAcceptance/addDeviceAcceptance",
    method: "post",
    data: data,
  });
}

// 编辑设备验收
export function updateDeviceAcceptance(data) {
  return request({
    url: "/deviceAcceptance/updateDeviceAcceptance",
    method: "post",
    data: data,
  });
}

//设备校准 分页查询
export function deviceMetricRecordPage(query) {
  return request({
    url: "/deviceMetricRecord/deviceMetricRecordPage",
    method: "get",
    params: query,
  });
}

//设备校准 新增 更新
export function saveOrUpdateDeviceMetric(data) {
  return request({
    url: "/deviceMetrics/saveOrUpdateDeviceMetric",
    method: "post",
    data: data,
  });
}

//设备校准 查询校准条目
export function showDeviceMetricsCopy(query) {
  return request({
    url: "/deviceMetricRecord/showDeviceMetricsCopy",
    method: "get",
    params: query,
  });
}

//设备校准 删除
export function deleteDeviceMetricRecord(query) {
  return request({
    url: "/deviceMetricRecord/deleteDeviceMetricRecord",
    method: "delete",
    params: query,
  });
}

//设备校准 导出
export function deviceMetricRecordExport(query) {
  return request({
    url: "/deviceMetricRecord/deviceMetricRecordExport",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除文件
export function deleteCNASFile(query) {
  return request({
    url: "/personBasicInfo/deleteCNASFile",
    method: "delete",
    params: query,
  });
}

//查询设备档案列表
export function getAllDocuments(query) {
  return request({
    url: `/deviceDocuments/getAllDocuments`,
    method: "get",
    params: query,
  });
}

//设备档案导出
export function exportDeviceFile(query) {
  return request({
    url: "/deviceScope/exportDeviceFile",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//设备校准 设备维护表 查询
export function selectDeviceMetric(query) {
  return request({
    url: "/deviceMetrics/selectDeviceMetric",
    method: "get",
    params: query,
  });
}

//设备校准 删除
export function deleteDeviceMetrics(query) {
  return request({
    url: "/deviceMetrics/deleteDeviceMetrics",
    method: "delete",
    params: query,
  });
}

//设备保养单条导出
export function exportMaintenanceRecord(query) {
  return request({
    url: "/deviceMaintain/exportMaintenanceRecord",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//设备维护删除
export function deleteDeviceMaintenance(query) {
  return request({
    url: "/deviceMaintain/deleteDeviceMaintenance",
    method: "delete",
    params: query,
  });
}

// 新增设备维护保养
export function addDeviceMaintenance(data) {
  return request({
    url: "/deviceMaintain/addDeviceMaintenance",
    method: "post",
    data: data,
  });
}
//设备维护表 查询
export function getDeviceMaintenancePage(query) {
  return request({
    url: "/deviceMaintain/getDeviceMaintenancePage",
    method: "get",
    params: query,
  });
}

// 借用-保存
export function saveDeviceBorrow(data) {
  return request({
    url: "/deviceBorrow/saveDeviceBorrow",
    method: "post",
    data: data,
  });
}

//借用-查看
export function getDeviceBorrow(query) {
  return request({
    url: "/deviceBorrow/getDeviceBorrow",
    method: "get",
    params: query,
  });
}

//借用-导出
export function deviceBorrowExport(query) {
  return request({
    url: "/deviceBorrow/deviceBorrowExport",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//借用-删除
export function deleteDeviceBorrow(query) {
  return request({
    url: "/deviceBorrow/deleteDeviceBorrow",
    method: "delete",
    params: query,
  });
}

//借用-列表
export function deviceBorrowPage(query) {
  return request({
    url: "/deviceBorrow/deviceBorrowPage",
    method: "get",
    params: query,
  });
}

// 设备启用/停用 保存，提交，驳回，通过接口
export function saveDeviceState(data) {
  return request({
    url: "/deviceState/saveDeviceState",
    method: "post",
    data: data,
  });
}

//通过设备编号获取设备列表
export function selectDeviceByCode(query) {
  return request({
    url: "/deviceScope/selectDeviceByCode",
    method: "get",
    params: query,
  });
}

//设备停启用单条导出
export function exportDeviceStatus(query) {
  return request({
    url: "/deviceState/exportDeviceStatus",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//设备启用/停用 删除
export function deleteDeviceState(query) {
  return request({
    url: "/deviceState/deleteDeviceState",
    method: "delete",
    params: query,
  });
}

//设备状态导出
export function deviceStateExport(query) {
  return request({
    url: "/deviceState/deviceStateExport",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//设备启用/停用 分页
export function getDeviceStatePage(query) {
  return request({
    url: "/deviceState/getDeviceStatePage",
    method: "get",
    params: query,
  });
}

//设备报废申请列表
export function pageDeviceScrapped(query) {
  return request({
    url: "/deviceScrapped/pageDeviceScrapped",
    method: "get",
    params: query,
  });
}

// 导出
export function exportDeviceScrapped(query) {
  return request({
    url: "/deviceScrapped/exportDeviceScrapped",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除设备报废申请
export function delDeviceScrapped(query) {
  return request({
    url: "/deviceScrapped/delDeviceScrapped",
    method: "delete",
    params: query,
  });
}

//查询设备报废申请
export function getDeviceScrapped(query) {
  return request({
    url: "/deviceScrapped/getDeviceScrapped",
    method: "get",
    params: query,
  });
}

// 新增设备报废申请
export function addDeviceScrapped(data) {
  return request({
    url: "/deviceScrapped/addDeviceScrapped",
    method: "post",
    data: data,
  });
}

//设备故障维修列表
export function pageDeviceBreakdownMaintenance(query) {
  return request({
    url: "/deviceBreakdownMaintenance/pageDeviceBreakdownMaintenance",
    method: "get",
    params: query,
  });
}

// 设备故障与维修-导出
export function exportDeviceBreakdownMaintenance(query) {
  return request({
    url: "/deviceBreakdownMaintenance/exportDeviceBreakdownMaintenance",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除设备故障维修
export function delDeviceBreakdownMaintenance(query) {
  return request({
    url: "/deviceBreakdownMaintenance/delDeviceBreakdownMaintenance",
    method: "delete",
    params: query,
  });
}

//查询设备故障维修
export function getDeviceBreakdownMaintenance(query) {
  return request({
    url: "/deviceBreakdownMaintenance/getDeviceBreakdownMaintenance",
    method: "get",
    params: query,
  });
}

// 新增设备故障维修
export function addDeviceBreakdownMaintenance(data) {
  return request({
    url: "/deviceBreakdownMaintenance/addDeviceBreakdownMaintenance",
    method: "post",
    data: data,
  });
}

//分页查询设备点检记录
export function getDeviceInspectionRecordByPage(query) {
  return request({
    url: "/deviceInspectionRecord/getDeviceInspectionRecordByPage",
    method: "get",
    params: query,
  });
}

// 导出设备点检记录
export function exportDeviceInspectionRecord(query) {
  return request({
    url: "/deviceInspectionRecord/exportDeviceInspectionRecord",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除设备点检记录
export function deleteDeviceInspectionRecord(query) {
  return request({
    url: "/deviceInspectionRecord/deleteDeviceInspectionRecord",
    method: "delete",
    params: query,
  });
}

//设备点检记录详情
export function getDeviceInspectionRecord(query) {
  return request({
    url: "/deviceInspectionRecord/getDeviceInspectionRecord",
    method: "get",
    params: query,
  });
}

// 新增设备点检记录
export function addDeviceInspectionRecord(data) {
  return request({
    url: "/deviceInspectionRecord/addDeviceInspectionRecord",
    method: "post",
    data: data,
  });
}

// 修改设备点检记录
export function updateDeviceInspectionRecord(data) {
  return request({
    url: "/deviceInspectionRecord/updateDeviceInspectionRecord",
    method: "post",
    data: data,
  });
}

// 复核设备点检记录
export function reviewDeviceInspectionRecord(data) {
  return request({
    url: "/deviceInspectionRecord/reviewDeviceInspectionRecord",
    method: "post",
    data: data,
  });
}

//设备事故报告列表
export function pageDeviceAccidentReport(query) {
  return request({
    url: "/deviceAccidentReport/pageDeviceAccidentReport",
    method: "get",
    params: query,
  });
}

// 导出设备事故
export function exportDeviceAccidentReport(query) {
  return request({
    url: "/deviceAccidentReport/exportDeviceAccidentReport",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//删除设备事故报告
export function delDeviceAccidentReport(query) {
  return request({
    url: "/deviceAccidentReport/delDeviceAccidentReport",
    method: "delete",
    params: query,
  });
}

//查询设备事故报告
export function getDeviceAccidentReport(query) {
  return request({
    url: "/deviceAccidentReport/getDeviceAccidentReport",
    method: "get",
    params: query,
  });
}

// 新增设备事故报告
export function addDeviceAccidentReport(data) {
  return request({
    url: "/deviceAccidentReport/addDeviceAccidentReport",
    method: "post",
    data: data,
  });
}

//作业指导书 审批
export function approvalOfHomeworkInstructionManual(data) {
  return request({
    url: "/deviceInstruction/approvalOfHomeworkInstructionManual",
    method: "post",
    data: data,
  });
}

//作业指导书 删除
export function homeworkGuidebook(query) {
  return request({
    url: "/deviceInstruction/homeworkGuidebook",
    method: "delete",
    params: query,
  });
}

//作业指导书 编辑查询
export function homeworkGuidebookEditor(query) {
  return request({
    url: "/deviceInstruction/homeworkGuidebookEditor",
    method: "get",
    params: query,
  });
}

//作业指导书新增
export function newHomeworkGuidebookAdded(data) {
  return request({
    url: "/deviceInstruction/newHomeworkGuidebookAdded",
    method: "post",
    data: data,
  });
}

//作业指导书受控文件删除
export function deleteHomeworkGuidebook(query) {
  return request({
    url: "/deviceInstruction/deleteHomeworkGuidebook",
    method: "delete",
    params: query,
  });
}

//作业指导书 查询
export function pageByPageQueryOfHomeworkInstructions(query) {
  return request({
    url: "/deviceInstruction/pageByPageQueryOfHomeworkInstructions",
    method: "get",
    params: query,
  });
}
//作业指导书 查询
export function getDeviceById(query) {
  return request({
    url: "/deviceScope/getDeviceById",
    method: "get",
    params: query,
  });
}
