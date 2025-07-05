import request from '@/utils/request'

// 设施和环境条件要求-电源稳定性查询
export function getLaboratoryFacilityPowerStablePage(query) {
  return request({
    url: '/fePowerStable/getLaboratoryFacilityPowerStablePage',
    method: 'get',
    params: query
  })
}

// 通过设备分类获取设备列表
export function deviceScopeSearch(query) {
  return request({
    url: '/deviceScope/search',
    method: 'get',
    params: query
  })
}

// 设备树形
export function treeDevice(query) {
  return request({
    url: '/deviceScope/treeDevice',
    method: 'get',
    params: query
  })
}

//获取用户列表
export function selectUserCondition(query) {
  return request({
    url: "/system/newUser/selectUserCondition",
    method: "get",
    params: query,
  });
}

// 设施和环境条件要求-电源稳定性新增/修改
export function addLaboratoryFacilityPowerStable(query) {
  return request({
    url: '/fePowerStable/addLaboratoryFacilityPowerStable',
    method: 'post',
    data: query
  })
}

// 设施和环境条件-设施和环境条件要求-电源稳定性-测定量 删除
export function deleteFeMeasuredQuantity(query) {
  return request({
    url: '/fePowerStable/deleteFeMeasuredQuantity',
    method: 'delete',
    params: query
  })
}

// 设施和环境条件要求-电源稳定性删除
export function deleteLaboratoryFacilityPowerStable(query) {
  return request({
    url: '/fePowerStable/deleteLaboratoryFacilityPowerStable',
    method: 'delete',
    params: query
  })
}

// 电源稳定性-测定量 根据电源稳定性查询
export function getFeMeasuredQuantityService(query) {
  return request({
    url: "/fePowerStable/getFeMeasuredQuantityService",
    method: "get",
    params: query,
  });
}

// 电源稳定性-测定量 导出
export function exportFePowerStable(query) {
  return request({
    url: "/fePowerStable/exportFePowerStable",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 设施和环境条件要求-防雷检测查询
export function getLightningProtectionDetection(query) {
  return request({
    url: '/feLightningProtection/getLightningProtectionDetection',
    method: 'get',
    params: query
  })
}

// 设施和环境条件要求-防雷检测新增/修改
export function addLightningProtectionDetection(query) {
  return request({
    url: '/feLightningProtection/addLightningProtectionDetection',
    method: 'post',
    data: query,
    headers: {
      'Content-Type': 'multipart/form-data' // 必须设置为 multipart
    }
  })
}

// 设施和环境条件要求-防雷检测删除
export function deleteLightningProtectionDetection(query) {
  return request({
    url: '/feLightningProtection/deleteLightningProtectionDetection',
    method: 'delete',
    params: query
  })
}

// 设施和环境条件要求-防雷检测导出
export function exportOfLightningProtectionDetection(query) {
  return request({
    url: "/feLightningProtection/exportOfLightningProtectionDetection",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 设施和环境条件要求-照度记录表-检测区域 查询
export function getFeLightningProtection(query) {
  return request({
    url: '/feIllumination/getFeLightningProtection',
    method: 'get',
    params: query
  })
}

// 设施和环境条件要求-照度记录表-检测区域 新增/修改
export function addFeLightningProtection(query) {
  return request({
    url: '/feIllumination/addFeLightningProtection',
    method: 'post',
    data: query,
  })
}

// 设施和环境条件要求-照度记录表-检测区域 删除
export function deleteFeLightningProtection(query) {
  return request({
    url: '/feIllumination/deleteFeLightningProtection',
    method: 'delete',
    params: query
  })
}

// 照度记录表-检测区域 根据照度记录查询
export function getFeIlluminationDetectionArea(query) {
  return request({
    url: '/feIllumination/getFeIlluminationDetectionArea',
    method: 'get',
    params: query
  })
}

// 照度记录表-检测区域 删除
export function deleteFeIlluminationDetectionArea(query) {
  return request({
    url: '/feIllumination/deleteFeIlluminationDetectionArea',
    method: 'delete',
    params: query
  })
}

// 照度记录表-检测区域 导出
export function exportFeIllumination(query) {
  return request({
    url: "/feIllumination/exportFeIllumination",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 试验区域-查询"
export function getFeTempHumDate(query) {
  return request({
    url: '/feTempHumDate/getFeTempHumDate',
    method: 'get',
    params: query
  })
}

// 试验区域-新增/修改
export function addFeTempHumDate(query) {
  return request({
    url: '/feTempHumDate/addFeTempHumDate',
    method: 'post',
    data: query,
  })
}

// 设施和环境条件要求-温湿度记录查询
export function getFeTempHumRecordPage(query) {
  return request({
    url: '/feTempHumDate/getFeTempHumRecordPage',
    method: 'get',
    params: query
  })
}

// 设施和环境条件要求-温湿度记录新增/修改
export function addFeTempHumRecord(query) {
  return request({
    url: '/feTempHumDate/addFeTempHumRecord',
    method: 'post',
    data: query,
  })
}

// 设施和环境条件要求-温湿度记录删除
export function deleteFeTempHumRecord(query) {
  return request({
    url: '/feTempHumDate/deleteFeTempHumRecord',
    method: 'delete',
    params: query
  })
}

// 试验区域-删除
export function deleteFeTempHumDate(query) {
  return request({
    url: '/feTempHumDate/deleteFeTempHumDate',
    method: 'delete',
    params: query
  })
}

// // 设施和环境条件要求-温湿度记录 导出
export function exportTemperatureAndHumidityRecords(query) {
  return request({
    url: "/feTempHumDate/exportTemperatureAndHumidityRecords",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

// 温湿度确认
export function affirmFeTempHumDate(query) {
  return request({
    url: '/feTempHumDate/affirmFeTempHumDate',
    method: 'post',
    data: query,
  })
}



