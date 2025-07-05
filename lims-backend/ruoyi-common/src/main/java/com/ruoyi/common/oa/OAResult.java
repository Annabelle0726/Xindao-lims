package com.ruoyi.common.oa;

import lombok.Data;

/**
 * @author ZTT
 */
@Data
public class OAResult {
	private static final String SUCCESS_CODE = "0";

	/**
	 * 错误代码
	 */
	private String errorCode;
	/**
	 * 错误信息
	 */
	private String errorMsg;
	/**
	 * 流程编号
	 */
	private String addWorkflowResult;


	public boolean success() {
		return SUCCESS_CODE.equals(errorCode);
	}
}
