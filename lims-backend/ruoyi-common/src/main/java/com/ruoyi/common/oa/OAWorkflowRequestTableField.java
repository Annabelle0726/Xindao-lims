
package com.ruoyi.common.oa;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;


/**
 * <p>OA_WorkflowRequestTableField complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="OA_WorkflowRequestTableField">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FieldName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FieldValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="View" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Edit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FieldType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SysName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueType1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueType2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueType3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ValueTableName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OA_WorkflowRequestTableField", propOrder = {
    "fieldName",
    "fieldValue",
    "view",
    "edit",
    "fieldType",
    "sysName",
    "valueType1",
    "valueType2",
    "valueType3",
    "valueKey",
    "valueTableName"
})
@Data
public class OAWorkflowRequestTableField {

    @XmlElement(name = "FieldName")
    protected String fieldName;
    @XmlElement(name = "FieldValue")
    protected String fieldValue;
    @XmlElement(name = "View")
    protected String view;
    @XmlElement(name = "Edit")
    protected String edit;
    @XmlElement(name = "FieldType")
    protected int fieldType;
    @XmlElement(name = "SysName")
    protected String sysName;
    @XmlElement(name = "ValueType1")
    protected String valueType1;
    @XmlElement(name = "ValueType2")
    protected String valueType2;
    @XmlElement(name = "ValueType3")
    protected String valueType3;
    @XmlElement(name = "ValueKey")
    protected String valueKey;
    @XmlElement(name = "ValueTableName")
    protected String valueTableName;

    /**
     * 获取fieldName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 设置fieldName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    /**
     * 获取fieldValue属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * 设置fieldValue属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFieldValue(String value) {
        this.fieldValue = value;
    }

    /**
     * 获取view属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getView() {
        return view;
    }

    /**
     * 设置view属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setView(String value) {
        this.view = value;
    }

    /**
     * 获取edit属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEdit() {
        return edit;
    }

    /**
     * 设置edit属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEdit(String value) {
        this.edit = value;
    }

    /**
     * 获取fieldType属性的值。
     *
     */
    public int getFieldType() {
        return fieldType;
    }

    /**
     * 设置fieldType属性的值。
     *
     */
    public void setFieldType(int value) {
        this.fieldType = value;
    }

    /**
     * 获取sysName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * 设置sysName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSysName(String value) {
        this.sysName = value;
    }

    /**
     * 获取valueType1属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValueType1() {
        return valueType1;
    }

    /**
     * 设置valueType1属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValueType1(String value) {
        this.valueType1 = value;
    }

    /**
     * 获取valueType2属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValueType2() {
        return valueType2;
    }

    /**
     * 设置valueType2属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValueType2(String value) {
        this.valueType2 = value;
    }

    /**
     * 获取valueType3属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValueType3() {
        return valueType3;
    }

    /**
     * 设置valueType3属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValueType3(String value) {
        this.valueType3 = value;
    }

    /**
     * 获取valueKey属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValueKey() {
        return valueKey;
    }

    /**
     * 设置valueKey属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValueKey(String value) {
        this.valueKey = value;
    }

    /**
     * 获取valueTableName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValueTableName() {
        return valueTableName;
    }

    /**
     * 设置valueTableName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValueTableName(String value) {
        this.valueTableName = value;
    }

	@Override
	public String toString() {
		return "OAWorkflowRequestTableField{" +
				"fieldName='" + fieldName + '\'' +
				", fieldValue='" + fieldValue + '\'' +
				", view='" + view + '\'' +
				", edit='" + edit + '\'' +
				", fieldType=" + fieldType +
				", sysName='" + sysName + '\'' +
				", valueType1='" + valueType1 + '\'' +
				", valueType2='" + valueType2 + '\'' +
				", valueType3='" + valueType3 + '\'' +
				", valueKey='" + valueKey + '\'' +
				", valueTableName='" + valueTableName + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OAWorkflowRequestTableField that = (OAWorkflowRequestTableField) o;
		return fieldType == that.fieldType && Objects.equals(fieldName, that.fieldName) && Objects.equals(fieldValue, that.fieldValue) && Objects.equals(view, that.view) && Objects.equals(edit, that.edit) && Objects.equals(sysName, that.sysName) && Objects.equals(valueType1, that.valueType1) && Objects.equals(valueType2, that.valueType2) && Objects.equals(valueType3, that.valueType3) && Objects.equals(valueKey, that.valueKey) && Objects.equals(valueTableName, that.valueTableName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldName, fieldValue, view, edit, fieldType, sysName, valueType1, valueType2, valueType3, valueKey, valueTableName);
	}

}
