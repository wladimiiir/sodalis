
package sk.magiksoft.sodalis.updater;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sk.magiksoft.sodalis.updater package. 
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

    private final static QName _CreateUpdate_QNAME = new QName("http://updater.sodalis.magiksoft.sk/", "createUpdate");
    private final static QName _CreateUpdateResponse_QNAME = new QName("http://updater.sodalis.magiksoft.sk/", "createUpdateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sk.magiksoft.sodalis.updater
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateUpdate }
     * 
     */
    public CreateUpdate createCreateUpdate() {
        return new CreateUpdate();
    }

    /**
     * Create an instance of {@link CreateUpdate.Properties }
     * 
     */
    public CreateUpdate.Properties createCreateUpdateProperties() {
        return new CreateUpdate.Properties();
    }

    /**
     * Create an instance of {@link CreateUpdateResponse }
     * 
     */
    public CreateUpdateResponse createCreateUpdateResponse() {
        return new CreateUpdateResponse();
    }

    /**
     * Create an instance of {@link CreateUpdate.Properties.Entry }
     * 
     */
    public CreateUpdate.Properties.Entry createCreateUpdatePropertiesEntry() {
        return new CreateUpdate.Properties.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUpdate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://updater.sodalis.magiksoft.sk/", name = "createUpdate")
    public JAXBElement<CreateUpdate> createCreateUpdate(CreateUpdate value) {
        return new JAXBElement<CreateUpdate>(_CreateUpdate_QNAME, CreateUpdate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://updater.sodalis.magiksoft.sk/", name = "createUpdateResponse")
    public JAXBElement<CreateUpdateResponse> createCreateUpdateResponse(CreateUpdateResponse value) {
        return new JAXBElement<CreateUpdateResponse>(_CreateUpdateResponse_QNAME, CreateUpdateResponse.class, null, value);
    }

}
