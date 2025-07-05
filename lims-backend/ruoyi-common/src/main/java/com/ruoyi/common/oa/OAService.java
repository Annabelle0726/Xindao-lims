package com.ruoyi.common.oa;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZTT
 */
public class OAService {
	/**
	 * 创建OA流程主表
	 *
	 * @param fields
	 * @return
	 */
	public OAWorkflowTable createOAMainWorkflowTable(Map<String, String> fields) {
		//------------------主表列赋值---------------------------
		List<OAWorkflowRequestTableField> oaWorkflowRequestTableField = new ArrayList<OAWorkflowRequestTableField>();

		for (Map.Entry<String, String> field : fields.entrySet()) {
			oaWorkflowRequestTableField.add(createOAWorkflowRequestTableField(field.getKey(), field.getValue().toString()));
		}

//		for (OAWorkflowRequestTableField workflowRequestTableField : oaWorkflowRequestTableField) {
//
//			System.out.println(workflowRequestTableField.fieldName +","+ workflowRequestTableField.fieldValue +"--"+ workflowRequestTableField.fieldType);
//		}
		//创建主表列
		ArrayOfOAWorkflowRequestTableField wrti = new ArrayOfOAWorkflowRequestTableField();
		wrti.oaWorkflowRequestTableField = oaWorkflowRequestTableField;
		// 将主表列填充到主表行里
		OAWorkflowRequestTableRecord oaWorkflowRequestTableRecord = new OAWorkflowRequestTableRecord();
		oaWorkflowRequestTableRecord.setWorkflowRequestTableFields(wrti);

		//创建主表行
		ArrayOfOAWorkflowRequestTableRecord wrtri = new ArrayOfOAWorkflowRequestTableRecord();
		wrtri.getOAWorkflowRequestTableRecord().add(oaWorkflowRequestTableRecord);

		//创建主表
		OAWorkflowTable wmi = new OAWorkflowTable();
		//将主表行填充到主表里
		wmi.setWorkflowRequestTableRecords(wrtri);

		return wmi;
	}

	/**
	 * 创建OA明细表流程
	 *
	 * @param fields
	 * @return
	 */
	public ArrayOfOAWorkflowTable createOADetailWorkflowTable(List<Map<String, String>> fields) {
		//------------------明细表赋值---------------------------
		//创建1张明细表
		ArrayOfOAWorkflowTable owdts = new ArrayOfOAWorkflowTable();
		//将明细列填充到明细行中
		//明细属性
		ArrayOfOAWorkflowRequestTableRecord owrtr = new ArrayOfOAWorkflowRequestTableRecord();
		for (int i = 0; i < fields.size(); i++) {
			List<OAWorkflowRequestTableField> owrtf_ = new ArrayList<OAWorkflowRequestTableField>();

			for (Map.Entry<String, String> field : fields.get(i).entrySet()) {
				owrtf_.add(createOAWorkflowRequestTableField(field.getKey(), field.getValue()));
			}
			ArrayOfOAWorkflowRequestTableField owrti = new ArrayOfOAWorkflowRequestTableField();
			owrti.oaWorkflowRequestTableField = owrtf_;
			OAWorkflowRequestTableRecord owrtis = new OAWorkflowRequestTableRecord();
			owrtis.setWorkflowRequestTableFields(owrti);
			owrtr.getOAWorkflowRequestTableRecord().add(owrtis);
		}


		// 将明细行填充到明细表里
		OAWorkflowTable owrtrs = new OAWorkflowTable();
		owrtrs.setWorkflowRequestTableRecords(owrtr);
		owdts.getOAWorkflowTable().add(owrtrs);

		return owdts;
	}

	/**
	 * 创建OA流程表格属性
	 *
	 * @param fieldName
	 * @param fieldValue
	 * @param view
	 * @param edit
	 * @return
	 */
	public OAWorkflowRequestTableField createOAWorkflowRequestTableField(String fieldName, String fieldValue, String view, String edit) {
		// 创建属性
		OAWorkflowRequestTableField oaWorkflowRequestTableField = new OAWorkflowRequestTableField();

		// 对属性进行设置
		oaWorkflowRequestTableField.setFieldName(fieldName);
		oaWorkflowRequestTableField.setFieldValue(fieldValue);
		oaWorkflowRequestTableField.setView(view);
		oaWorkflowRequestTableField.setEdit(edit);

		return oaWorkflowRequestTableField;
	}

	/**
	 * 创建OA流程表格属性
	 *
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public OAWorkflowRequestTableField createOAWorkflowRequestTableField(String fieldName, String fieldValue) {
		// 创建属性
		OAWorkflowRequestTableField oaWorkflowRequestTableField = new OAWorkflowRequestTableField();

		// 对属性进行设置
		oaWorkflowRequestTableField.setFieldName(fieldName);
		oaWorkflowRequestTableField.setFieldValue(fieldValue);
		oaWorkflowRequestTableField.setView("true");
		oaWorkflowRequestTableField.setEdit("true");
		if (fieldName.equals("sqr") || fieldName.equals("fkr")) {
			oaWorkflowRequestTableField.setFieldType(1);
		}
		return oaWorkflowRequestTableField;
	}

	/**
	 * 发送OA流程
	 *
	 * @param wri
	 * @return
	 */
	public OAResult sendWorkFlow(OAWorkflowRequestInfo wri) {
		Holder<String> errorCode = new Holder<String>();
		Holder<String> errorMsg = new Holder<String>();
		Holder<String> addWorkflowResult = new Holder<String>();

		OAWorkFlowRequest owfr = new OAWorkFlowRequest();
		// 调用OA流程新增方法，发起OA流程
		owfr.getOAWorkFlowRequestSoap().addWorkflow("5DEDD10D2E434A139A05953BDB66CC68", wri, errorCode, errorMsg, addWorkflowResult);

		// 流程发送结果
		OAResult oaResult = new OAResult();
		oaResult.setErrorCode(errorCode.value);
		oaResult.setErrorMsg(errorMsg.value);
		oaResult.setAddWorkflowResult(addWorkflowResult.value);

		return oaResult;
	}
}
