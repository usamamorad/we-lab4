
package highscore;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.ac.tuwien.big.we.highscore package. 
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

    private final static QName _Failure_QNAME = new QName("http://big.tuwien.ac.at/we/highscore", "Failure");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.ac.tuwien.big.we.highscore
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FailureType }
     * 
     */
    public FailureType createFailureType() {
        return new FailureType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FailureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://big.tuwien.ac.at/we/highscore", name = "Failure")
    public JAXBElement<FailureType> createFailure(FailureType value) {
        return new JAXBElement<FailureType>(_Failure_QNAME, FailureType.class, null, value);
    }

}
