
package com.ruoyi.common.oa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OA_WorkflowTable complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OA_WorkflowTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TableName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WorkflowRequestTableRecords" type="{http://tempuri.org/}ArrayOfOA_WorkflowRequestTableRecord" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OA_WorkflowTable", propOrder = {
    "tableName",
    "workflowRequestTableRecords"
})
public class OAWorkflowTable {

    @XmlElement(name = "TableName")
    protected String tableName;
    @XmlElement(name = "WorkflowRequestTableRecords")
    protected ArrayOfOAWorkflowRequestTableRecord workflowRequestTableRecords;

    /**
     * 获取tableName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置tableName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTableName(String value) {
        this.tableName = value;
    }

    /**
     * 获取workflowRequestTableRecords属性的值。
     *
     * @return
     *     possible object is
     *     {@link ArrayOfOAWorkflowRequestTableRecord }
     *
     */
    public ArrayOfOAWorkflowRequestTableRecord getWorkflowRequestTableRecords() {
        return workflowRequestTableRecords;
    }

    /**
     * 设置workflowRequestTableRecords属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfOAWorkflowRequestTableRecord }
     *
     */
    public void setWorkflowRequestTableRecords(ArrayOfOAWorkflowRequestTableRecord value) {
        this.workflowRequestTableRecords = value;
    }

}
