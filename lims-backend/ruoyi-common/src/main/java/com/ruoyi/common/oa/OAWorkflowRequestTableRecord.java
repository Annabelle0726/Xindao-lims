
package com.ruoyi.common.oa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>OA_WorkflowRequestTableRecord complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OA_WorkflowRequestTableRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WorkflowRequestTableFields" type="{http://tempuri.org/}ArrayOfOA_WorkflowRequestTableField" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OA_WorkflowRequestTableRecord", propOrder = {
    "workflowRequestTableFields"
})
public class OAWorkflowRequestTableRecord {

    @XmlElement(name = "WorkflowRequestTableFields")
    protected ArrayOfOAWorkflowRequestTableField workflowRequestTableFields;

    /**
     * 获取workflowRequestTableFields属性的值。
     *
     * @return
     *     possible object is
     *     {@link ArrayOfOAWorkflowRequestTableField }
     *
     */
    public ArrayOfOAWorkflowRequestTableField getWorkflowRequestTableFields() {
        return workflowRequestTableFields;
    }

    /**
     * 设置workflowRequestTableFields属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ArrayOfOAWorkflowRequestTableField }
     *
     */
    public void setWorkflowRequestTableFields(ArrayOfOAWorkflowRequestTableField value) {
        this.workflowRequestTableFields = value;
    }

}
