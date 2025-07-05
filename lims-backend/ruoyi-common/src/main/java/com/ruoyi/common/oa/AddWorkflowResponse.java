
package com.ruoyi.common.oa;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="AddWorkflowResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "addWorkflowResult",
    "errorCode",
    "errorMsg"
})
@XmlRootElement(name = "AddWorkflowResponse")
public class AddWorkflowResponse {

    @XmlElement(name = "AddWorkflowResult")
    protected String addWorkflowResult;
    protected String errorCode;
    protected String errorMsg;

    /**
     * 获取addWorkflowResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAddWorkflowResult() {
        return addWorkflowResult;
    }

    /**
     * 设置addWorkflowResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAddWorkflowResult(String value) {
        this.addWorkflowResult = value;
    }

    /**
     * 获取errorCode属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置errorCode属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * 获取errorMsg属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置errorMsg属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorMsg(String value) {
        this.errorMsg = value;
    }

}
