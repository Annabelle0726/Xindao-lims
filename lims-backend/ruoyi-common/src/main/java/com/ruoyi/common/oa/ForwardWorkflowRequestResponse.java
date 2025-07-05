
package com.ruoyi.common.oa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="forwardWorkflowRequestResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "forwardWorkflowRequestResult"
})
@XmlRootElement(name = "forwardWorkflowRequestResponse")
public class ForwardWorkflowRequestResponse {

    protected String forwardWorkflowRequestResult;

    /**
     * 获取forwardWorkflowRequestResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getForwardWorkflowRequestResult() {
        return forwardWorkflowRequestResult;
    }

    /**
     * 设置forwardWorkflowRequestResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setForwardWorkflowRequestResult(String value) {
        this.forwardWorkflowRequestResult = value;
    }

}
