
package highscore.data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.ac.tuwien.big.we.highscore.data package. 
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

    private final static QName _HighScoreRequest_QNAME = new QName("http://big.tuwien.ac.at/we/highscore/data", "HighScoreRequest");
    private final static QName _HighScoreResponse_QNAME = new QName("http://big.tuwien.ac.at/we/highscore/data", "HighScoreResponse");
    private final static QName _UserKey_QNAME = new QName("http://big.tuwien.ac.at/we/highscore/data", "UserKey");
    private final static QName _UserData_QNAME = new QName("http://big.tuwien.ac.at/we/highscore/data", "UserData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.ac.tuwien.big.we.highscore.data
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UserDataType }
     * 
     */
    public UserDataType createUserDataType() {
        return new UserDataType();
    }

    /**
     * Create an instance of {@link HighScoreRequestType }
     * 
     */
    public HighScoreRequestType createHighScoreRequestType() {
        return new HighScoreRequestType();
    }

    /**
     * Create an instance of {@link UserType }
     * 
     */
    public UserType createUserType() {
        return new UserType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HighScoreRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://big.tuwien.ac.at/we/highscore/data", name = "HighScoreRequest")
    public JAXBElement<HighScoreRequestType> createHighScoreRequest(HighScoreRequestType value) {
        return new JAXBElement<HighScoreRequestType>(_HighScoreRequest_QNAME, HighScoreRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://big.tuwien.ac.at/we/highscore/data", name = "HighScoreResponse")
    public JAXBElement<String> createHighScoreResponse(String value) {
        return new JAXBElement<String>(_HighScoreResponse_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://big.tuwien.ac.at/we/highscore/data", name = "UserKey")
    public JAXBElement<String> createUserKey(String value) {
        return new JAXBElement<String>(_UserKey_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://big.tuwien.ac.at/we/highscore/data", name = "UserData")
    public JAXBElement<UserDataType> createUserData(UserDataType value) {
        return new JAXBElement<UserDataType>(_UserData_QNAME, UserDataType.class, null, value);
    }

}
