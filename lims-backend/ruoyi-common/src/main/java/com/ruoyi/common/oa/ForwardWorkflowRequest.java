
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
 *         &lt;element name="requestid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="forwardoperator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="clientip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "requestid",
    "forwardoperator",
    "remark",
    "userId",
    "clientip"
})
@XmlRootElement(name = "forwardWorkflowRequest")
public class ForwardWorkflowRequest {

    protected int requestid;
    protected String forwardoperator;
    protected String remark;
    protected int userId;
    protected String clientip;

    /**
     * 获取requestid属性的值。
     *
     */
    public int getRequestid() {
        return requestid;
    }

    /**
     * 设置requestid属性的值。
     *
     */
    public void setRequestid(int value) {
        this.requestid = value;
    }

    /**
     * 获取forwardoperator属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getForwardoperator() {
        return forwardoperator;
    }

    /**
     * 设置forwardoperator属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setForwardoperator(String value) {
        this.forwardoperator = value;
    }

    /**
     * 获取remark属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置remark属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRemark(String value) {
        this.remark = value;
    }

    /**
     * 获取userId属性的值。
     *
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 设置userId属性的值。
     *
     */
    public void setUserId(int value) {
        this.userId = value;
    }

    /**
     * 获取clientip属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClientip() {
        return clientip;
    }

    /**
     * 设置clientip属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClientip(String value) {
        this.clientip = value;
    }

}
