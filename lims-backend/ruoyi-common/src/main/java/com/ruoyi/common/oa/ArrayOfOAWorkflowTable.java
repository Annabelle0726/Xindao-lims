
package com.ruoyi.common.oa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>ArrayOfOA_WorkflowTable complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="ArrayOfOA_WorkflowTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OA_WorkflowTable" type="{http://tempuri.org/}OA_WorkflowTable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOA_WorkflowTable", propOrder = {
    "oaWorkflowTable"
})
public class ArrayOfOAWorkflowTable {

    @XmlElement(name = "OA_WorkflowTable", nillable = true)
    protected List<OAWorkflowTable> oaWorkflowTable;

    /**
     * Gets the value of the oaWorkflowTable property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oaWorkflowTable property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOAWorkflowTable().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OAWorkflowTable }
     *
     *
     */
    public List<OAWorkflowTable> getOAWorkflowTable() {
        if (oaWorkflowTable == null) {
            oaWorkflowTable = new ArrayList<OAWorkflowTable>();
        }
        return this.oaWorkflowTable;
    }

}
