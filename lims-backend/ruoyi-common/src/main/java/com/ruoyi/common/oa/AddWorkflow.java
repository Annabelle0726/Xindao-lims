
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
 *         &lt;element name="appKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wri" type="{http://tempuri.org/}OA_WorkflowRequestInfo" minOccurs="0"/>
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
    "appKey",
    "wri",
    "errorCode",
    "errorMsg"
})
@XmlRootElement(name = "AddWorkflow")
public class AddWorkflow {

    protected String appKey;
    protected OAWorkflowRequestInfo wri;
    protected String errorCode;
    protected String errorMsg;

    /**
     * 获取appKey属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * 设置appKey属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAppKey(String value) {
        this.appKey = value;
    }

    /**
     * 获取wri属性的值。
     *
     * @return
     *     possible object is
     *     {@link OAWorkflowRequestInfo }
     *
     */
    public OAWorkflowRequestInfo getWri() {
        return wri;
    }

    /**
     * 设置wri属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link OAWorkflowRequestInfo }
     *
     */
    public void setWri(OAWorkflowRequestInfo value) {
        this.wri = value;
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
