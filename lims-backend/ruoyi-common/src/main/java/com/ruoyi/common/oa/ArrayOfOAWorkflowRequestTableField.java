
package com.ruoyi.common.oa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>ArrayOfOA_WorkflowRequestTableField complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="ArrayOfOA_WorkflowRequestTableField">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OA_WorkflowRequestTableField" type="{http://tempuri.org/}OA_WorkflowRequestTableField" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOA_WorkflowRequestTableField", propOrder = {
    "oaWorkflowRequestTableField"
})
public class ArrayOfOAWorkflowRequestTableField {

    @XmlElement(name = "OA_WorkflowRequestTableField", nillable = true)
    protected List<OAWorkflowRequestTableField> oaWorkflowRequestTableField;

    /**
     * Gets the value of the oaWorkflowRequestTableField property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oaWorkflowRequestTableField property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOAWorkflowRequestTableField().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OAWorkflowRequestTableField }
     *
     *
     */
    public List<OAWorkflowRequestTableField> getOAWorkflowRequestTableField() {
        if (oaWorkflowRequestTableField == null) {
            oaWorkflowRequestTableField = new ArrayList<OAWorkflowRequestTableField>();
        }
        return this.oaWorkflowRequestTableField;
    }

}
