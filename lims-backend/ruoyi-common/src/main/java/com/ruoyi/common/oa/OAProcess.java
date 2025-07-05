package com.ruoyi.common.oa;

import java.util.List;
import java.util.Map;

public class OAProcess {

    public static String oaReturnMsg(int code, String msg) {
        return "<miap><miap-header><errorcode>" + code + "</errorcode><errormsg>" + msg + "</errormsg></miap-header" + "><miap-Body></miap-Body></miap>";
    }

    /**
     * @param mainFields：主表字段数据
     * @param detailFields:子表字段数据
     * @param requestName:创建的流程的名称
     * @param workflowId:OA流程ID
     * @param createrId:OA账号
     * @return OAResult
     * @throws Exception
     */
    public static OAResult start(Map<String, String> mainFields, List<Map<String, String>> detailFields, String requestName, String workflowId, String createrId) throws Exception {
        OAService oaService = new OAService();
        //创建流程主体
        OAWorkflowRequestInfo wri = new OAWorkflowRequestInfo();
        wri.setWorkflowId(workflowId);
        wri.setCreaterId(createrId);
        wri.setRequestLevel("0");
        wri.setRequestName(requestName);

        OAWorkflowTable wmi = oaService.createOAMainWorkflowTable(mainFields);
        // 将主表信息填充到流程信息里
        wri.setWorkflowMainTable(wmi);

        ArrayOfOAWorkflowTable owdts = oaService.createOADetailWorkflowTable(detailFields);
        // 将明细表信息填充到流程信息里
        wri.setWorkflowDetailTables(owdts);

        // 发起OA流程
        OAResult oaResult = oaService.sendWorkFlow(wri);
        return oaResult;
    }


    public static OAResult start(Map<String, String> mainFields, String requestName, String workflowId, String createrId) throws Exception {
        OAService oaService = new OAService();
        //创建流程主体
        OAWorkflowRequestInfo wri = new OAWorkflowRequestInfo();
        wri.setWorkflowId(workflowId);
        wri.setCreaterId(createrId);
        wri.setRequestLevel("0");
        wri.setRequestName(requestName);
        //todo: oa提交 1: 直接发送流程, 0:存入草稿箱
//        wri.setIsnextflow("1");
        wri.setIsnextflow("0");
        //创建主表
        OAWorkflowTable wmi = oaService.createOAMainWorkflowTable(mainFields);
        // 将主表信息填充到流程信息里
        wri.setWorkflowMainTable(wmi);
        // 发起OA流程
        OAResult oaResult = oaService.sendWorkFlow(wri);
        return oaResult;
    }
}
