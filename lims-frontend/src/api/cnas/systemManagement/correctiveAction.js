// 纠正措施相关接口
import request from "@/utils/request";

//查询监督纠正措施列表
export function pageSuperviseDetailCorrect(query) {
  return request({
    url: "/qualitySupervise/pageSuperviseDetailCorrect",
    method: "get",
    params: query,
  });
}

//导出监督纠正措施
export function exportSuperviseDetaillCorrect(query) {
  return request({
    url: "/qualitySupervise/exportSuperviseDetaillCorrect",
    method: "get",
    responseType: "blob",
    params: query,
  });
}

//查询监督纠正措施附件
export function getSuperviseDetailCorrectFileList(query) {
  return request({
    url: "/qualitySupervise/getSuperviseDetailCorrectFileList",
    method: "get",
    params: query,
  });
}

//删除监督纠正措施附件
export function delSuperviseDetailCorrectFile(query) {
  return request({
    url: "/qualitySupervise/delSuperviseDetailCorrectFile",
    method: "delete",
    params: query,
  });
}

//查询监督纠正处理
export function getSuperviseDetailCorrect(query) {
  return request({
    url: "/qualitySupervise/getSuperviseDetailCorrect",
    method: "get",
    params: query,
  });
}

//查询今年人员培训信息
export function getThisYearTrainingDetailed(query) {
  return request({
    url: "/personTraining/getThisYearTrainingDetailed",
    method: "get",
    params: query,
  });
}
