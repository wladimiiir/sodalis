
package sk.magiksoft.sodalis.updater.jaxws;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createUpdate", namespace = "http://updater.sodalis.magiksoft.sk/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createUpdate", namespace = "http://updater.sodalis.magiksoft.sk/")
public class CreateUpdate {

    @XmlElement(name = "properties", namespace = "")
    private HashMap<String, String> properties;

    /**
     * 
     * @return
     *     returns HashMap<String,String>
     */
    public HashMap<String, String> getProperties() {
        return this.properties;
    }

    /**
     * 
     * @param properties
     *     the value for the properties property
     */
    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

}
