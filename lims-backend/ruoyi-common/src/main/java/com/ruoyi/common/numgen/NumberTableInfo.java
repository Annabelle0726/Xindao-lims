package com.ruoyi.common.numgen;

import lombok.Data;

/**
 * @Author: zhangxy
 * @Date: 2020-09-11 16:48
 */
@Data
public class NumberTableInfo {

	private String tableName;
	private String numberFieldName;

	private boolean logicDelete;
}
