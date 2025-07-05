
package com.ruoyi.common.oa;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.chinaztt.mes.common.oa package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _String_QNAME = new QName("http://tempuri.org/", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.chinaztt.mes.common.oa
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddWorkflowResponse }
     *
     */
    public AddWorkflowResponse createAddWorkflowResponse() {
        return new AddWorkflowResponse();
    }

    /**
     * Create an instance of {@link AddWorkflow }
     *
     */
    public AddWorkflow createAddWorkflow() {
        return new AddWorkflow();
    }

    /**
     * Create an instance of {@link OAWorkflowRequestInfo }
     *
     */
    public OAWorkflowRequestInfo createOAWorkflowRequestInfo() {
        return new OAWorkflowRequestInfo();
    }

    /**
     * Create an instance of {@link UpdateAndSubmitWorkflow }
     *
     */
    public UpdateAndSubmitWorkflow createUpdateAndSubmitWorkflow() {
        return new UpdateAndSubmitWorkflow();
    }

    /**
     * Create an instance of {@link UpdateAndSubmitWorkflowResponse }
     *
     */
    public UpdateAndSubmitWorkflowResponse createUpdateAndSubmitWorkflowResponse() {
        return new UpdateAndSubmitWorkflowResponse();
    }

    /**
     * Create an instance of {@link ForwardWorkflowRequestResponse }
     *
     */
    public ForwardWorkflowRequestResponse createForwardWorkflowRequestResponse() {
        return new ForwardWorkflowRequestResponse();
    }

    /**
     * Create an instance of {@link ForwardWorkflowRequest }
     *
     */
    public ForwardWorkflowRequest createForwardWorkflowRequest() {
        return new ForwardWorkflowRequest();
    }

    /**
     * Create an instance of {@link OAWorkflowTable }
     *
     */
    public OAWorkflowTable createOAWorkflowTable() {
        return new OAWorkflowTable();
    }

    /**
     * Create an instance of {@link OAWorkflowRequestTableRecord }
     *
     */
    public OAWorkflowRequestTableRecord createOAWorkflowRequestTableRecord() {
        return new OAWorkflowRequestTableRecord();
    }

    /**
     * Create an instance of {@link ArrayOfOAWorkflowTable }
     *
     */
    public ArrayOfOAWorkflowTable createArrayOfOAWorkflowTable() {
        return new ArrayOfOAWorkflowTable();
    }

    /**
     * Create an instance of {@link ArrayOfOAWorkflowRequestTableRecord }
     *
     */
    public ArrayOfOAWorkflowRequestTableRecord createArrayOfOAWorkflowRequestTableRecord() {
        return new ArrayOfOAWorkflowRequestTableRecord();
    }

    /**
     * Create an instance of {@link OAWorkflowRequestTableField }
     *
     */
    public OAWorkflowRequestTableField createOAWorkflowRequestTableField() {
        return new OAWorkflowRequestTableField();
    }

    /**
     * Create an instance of {@link ArrayOfOAWorkflowRequestTableField }
     *
     */
    public ArrayOfOAWorkflowRequestTableField createArrayOfOAWorkflowRequestTableField() {
        return new ArrayOfOAWorkflowRequestTableField();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
