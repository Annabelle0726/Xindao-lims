
package com.ruoyi.common.oa;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OA_WorkflowRequestInfo complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OA_WorkflowRequestInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WorkflowId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MainId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreaterId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WorkflowMainTable" type="{http://tempuri.org/}OA_WorkflowTable" minOccurs="0"/>
 *         &lt;element name="WorkflowDetailTables" type="{http://tempuri.org/}ArrayOfOA_WorkflowTable" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OA_WorkflowRequestInfo", propOrder = {
		"workflowId",
		"requestId",
		"mainId",
		"createrId",
		"requestLevel",
		"requestName",
		"remark",
		"isnextflow",
		"workflowMainTable",
		"workflowDetailTables"
})
@Data
public class OAWorkflowRequestInfo {

	@XmlElement(name = "WorkflowId")
	protected String workflowId;
	@XmlElement(name = "RequestId")
	protected String requestId;
	@XmlElement(name = "MainId")
	protected String mainId;
	@XmlElement(name = "CreaterId")
	protected String createrId;
	@XmlElement(name = "RequestLevel")
	protected String requestLevel;
	@XmlElement(name = "RequestName")
	protected String requestName;
	@XmlElement(name = "Remark")
	protected String remark;
	@XmlElement(name = "isnextflow")
	protected String isnextflow;
	@XmlElement(name = "WorkflowMainTable")
	protected OAWorkflowTable workflowMainTable;
	@XmlElement(name = "WorkflowDetailTables")
	protected ArrayOfOAWorkflowTable workflowDetailTables;

	/**
	 * 获取workflowId属性的值。
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * 设置workflowId属性的值。
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setWorkflowId(String value) {
		this.workflowId = value;
	}

	/**
	 * 获取requestId属性的值。
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * 设置requestId属性的值。
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setRequestId(String value) {
		this.requestId = value;
	}

	/**
	 * 获取mainId属性的值。
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getMainId() {
		return mainId;
	}

	/**
	 * 设置mainId属性的值。
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setMainId(String value) {
		this.mainId = value;
	}

	/**
	 * 获取createrId属性的值。
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getCreaterId() {
		return createrId;
	}

	/**
	 * 设置createrId属性的值。
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setCreaterId(String value) {
		this.createrId = value;
	}

	/**
	 * 获取requestLevel属性的值。
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getRequestLevel() {
		return requestLevel;
	}

	/**
	 * 设置requestLevel属性的值。
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setRequestLevel(String value) {
		this.requestLevel = value;
	}

	/**
	 * 获取requestName属性的值。
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getRequestName() {
		return requestName;
	}

	/**
	 * 设置requestName属性的值。
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setRequestName(String value) {
		this.requestName = value;
	}

	/**
	 * 获取remark属性的值。
	 *
	 * @return possible object is
	 * {@link String }
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置remark属性的值。
	 *
	 * @param value allowed object is
	 *              {@link String }
	 */
	public void setRemark(String value) {
		this.remark = value;
	}

	/**
	 * 获取workflowMainTable属性的值。
	 *
	 * @return possible object is
	 * {@link OAWorkflowTable }
	 */
	public OAWorkflowTable getWorkflowMainTable() {
		return workflowMainTable;
	}

	/**
	 * 设置workflowMainTable属性的值。
	 *
	 * @param value allowed object is
	 *              {@link OAWorkflowTable }
	 */
	public void setWorkflowMainTable(OAWorkflowTable value) {
		this.workflowMainTable = value;
	}

	/**
	 * 获取workflowDetailTables属性的值。
	 *
	 * @return possible object is
	 * {@link ArrayOfOAWorkflowTable }
	 */
	public ArrayOfOAWorkflowTable getWorkflowDetailTables() {
		return workflowDetailTables;
	}

	/**
	 * 设置workflowDetailTables属性的值。
	 *
	 * @param value allowed object is
	 *              {@link ArrayOfOAWorkflowTable }
	 */
	public void setWorkflowDetailTables(ArrayOfOAWorkflowTable value) {
		this.workflowDetailTables = value;
	}

}
