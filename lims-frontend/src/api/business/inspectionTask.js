// 检验任务相关接口
import request from "@/utils/request";

// 通过检验单查询检验数据（数据查看）
export function selectSampleAndProductByOrderId(query) {
  return request({
    url: "/insOrder/selectSampleAndProductByOrderId",
    method: "get",
    params: query,
  });
}

// 检验任务-修改检验值
export function updateProductOnPlan(data) {
  return request({
    url: "/insOrderPlan/updateProductOnPlan",
    method: "post",
    data: data,
  });
}

// 查看不合格复测数据
export function getRetestResult(query) {
  return request({
    url: "/insOrder/getRetestResult",
    method: "get",
    params: query,
  });
}

// 认领检验任务
export function claimInsOrderPlan(data) {
  return request({
    url: "/insOrderPlan/claimInsOrderPlan",
    method: "post",
    data: data,
  });
}

// 任务交接2
export function upPlanUser2(query) {
  return request({
    url: "/insOrderPlan/upPlanUser2",
    method: "get",
    params: query,
  });
}

// 任务交接
export function upPlanUser(data) {
  return request({
    url: "/insOrderPlan/upPlanUser",
    method: "post",
    params: data,
  });
}

// 获取用户列表
export function selectUserCondition(query) {
  return request({
    url: "/system/newUser/selectUserCondition",
    method: "get",
    params: query,
  });
}

// 检验任务-特殊检验项绑定查询
export function getBindingProductByProductId(query) {
  return request({
    url: "/insOrderPlan/getBindingProductByProductId",
    method: "get",
    params: query,
  });
}

// 检验任务-根据检验项id查询检验项树信息
export function getProductTreeByProductId(query) {
  return request({
    url: "/insOrderPlan/getProductTreeByProductId",
    method: "get",
    params: query,
  });
}

// 检验任务-特殊检验项绑定
export function bindingProductTreeByProductId(data) {
  return request({
    url: "/insOrderPlan/bindingProductTreeByProductId",
    method: "post",
    data: data,
  });
}

// 检验任务-删除特殊检验项绑定信息
export function delProductTreeByProductId(query) {
  return request({
    url: "/insOrderPlan/delProductTreeByProductId",
    method: "delete",
    params: query,
  });
}

// 查询检验任务
export function selectInsOrderPlanList(query) {
  return request({
    url: "/insOrderPlan/selectInsOrderPlanList",
    method: "get",
    params: query,
  });
}

// 附件上传
export function uploadFile(data) {
  return request({
    url: "/insOrderPlan/uploadFile",
    method: "post",
    data: data,
  });
}

// 查询检验任务
export function doInsOrder(query) {
  return request({
    url: "/insOrderPlan/doInsOrder",
    method: "get",
    params: query,
  });
}

// 查询是否有需要多次检验的电缆配置
export function getCableTag(query) {
  return request({
    url: "/insOrderPlan/getCableTag",
    method: "get",
    params: query,
  });
}

// 查看重复标识
export function getRepetitionTag(query) {
  return request({
    url: "/insOrderPlan/getRepetitionTag",
    method: "get",
    params: query,
  });
}

// 查询是否有需要多次检验的电缆配置
export function getRawMaterialTag(query) {
  return request({
    url: "/insOrderPlan/getRawMaterialTag",
    method: "get",
    params: query,
  });
}

// 数采-数据采集
export function dataCollection(data) {
  return request({
    url: "/deviceScope/dataCollection",
    method: "post",
    data: data,
  });
}

// 填写温度与湿度
export function write(data) {
  return request({
    url: "/insOrderPlan/write",
    method: "post",
    data: data,
  });
}

// 获取检验项目和模板内容
export function getInsProduct(query) {
  return request({
    url: "/insOrderPlan/getInsProduct",
    method: "get",
    params: query,
  });
}

// 切换光纤-光纤带记录模版查询检验内容
export function getReportModel(query) {
  return request({
    url: "/insOrderPlan/getReportModel",
    method: "get",
    params: query,
  });
}

// 判断该设备是否可以数采
export function determineWhetherToCollectData(query) {
  return request({
    url: "/deviceScope/determineWhetherToCollectData",
    method: "get",
    params: query,
  });
}

// 通过设备分类获取设备列表
export function search(query) {
  return request({
    url: "/deviceScope/search",
    method: "get",
    params: query,
  });
}

// 复核检验任务
export function verifyPlan(data) {
  return request({
    url: "/insOrderPlan/verifyPlan",
    method: "post",
    params: data,
  });
}

// 校验检验任务提交
export function checkSubmitPlan(data) {
  return request({
    url: "/insOrderPlan/checkSubmitPlan",
    method: "post",
    params: data,
  });
}

// 检验任务提交
export function submitPlan(data) {
  return request({
    url: "/insOrderPlan/submitPlan",
    method: "post",
    params: data,
  });
}

// 保存检验内容
export function saveInsContext(data) {
  return request({
    url: "/insOrderPlan/saveInsContext",
    method: "post",
    data: data,
  });
}

// 附件下载
export function downFile(query) {
  return request({
    url: "/insOrderPlan/downFile",
    method: "get",
    params: query,
  });
}

// 查看检验单下的附件列表
export function getFileList(query) {
  return request({
    url: "/insOrderPlan/getFileList",
    method: "get",
    params: query,
  });
}

// 附件删除
export function delfile(query) {
  return request({
    url: "/insOrderPlan/delfile",
    method: "delete",
    params: query,
  });
}

// 检验单详情-任务切换
export function inspectionOrderDetailsTaskSwitching(query) {
  return request({
    url: "/insOrderPlan/inspectionOrderDetailsTaskSwitching",
    method: "get",
    params: query,
  });
}
// 查询复测模版
export function getInsProductUnqualifiedRetest(query) {
  return request({
    url: "/insOrderPlan/getInsProductUnqualifiedRetest",
    method: "get",
    params: query,
  });
}
// 查询工时
export function getWorkingHoursByOrderId(query) {
  return request({
    url: "/insOrderPlan/getWorkingHoursByOrderId",
    method: "get",
    params: query,
  });
}
// 修改工时详情
export function updateWorkingHours(query) {
  return request({
    url: "/insOrderPlan/updateWorkingHours",
    method: "post",
    data: query,
  });
}
