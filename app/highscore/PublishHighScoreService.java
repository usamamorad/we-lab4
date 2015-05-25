
package highscore;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * 
 * 		    Using the high score service one may push high score result to the high score board.
 * 		
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "PublishHighScoreService", targetNamespace = "http://big.tuwien.ac.at/we/highscore", wsdlLocation = "http://playground.big.tuwien.ac.at:8080/highscoreservice/PublishHighScoreService?wsdl")
public class PublishHighScoreService
    extends Service
{

    private final static URL PUBLISHHIGHSCORESERVICE_WSDL_LOCATION;
    private final static WebServiceException PUBLISHHIGHSCORESERVICE_EXCEPTION;
    private final static QName PUBLISHHIGHSCORESERVICE_QNAME = new QName("http://big.tuwien.ac.at/we/highscore", "PublishHighScoreService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://playground.big.tuwien.ac.at:8080/highscoreservice/PublishHighScoreService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        PUBLISHHIGHSCORESERVICE_WSDL_LOCATION = url;
        PUBLISHHIGHSCORESERVICE_EXCEPTION = e;
    }

    public PublishHighScoreService() {
        super(__getWsdlLocation(), PUBLISHHIGHSCORESERVICE_QNAME);
    }

    public PublishHighScoreService(WebServiceFeature... features) {
        super(__getWsdlLocation(), PUBLISHHIGHSCORESERVICE_QNAME, features);
    }

    public PublishHighScoreService(URL wsdlLocation) {
        super(wsdlLocation, PUBLISHHIGHSCORESERVICE_QNAME);
    }

    public PublishHighScoreService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, PUBLISHHIGHSCORESERVICE_QNAME, features);
    }

    public PublishHighScoreService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PublishHighScoreService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns PublishHighScoreEndpoint
     */
    @WebEndpoint(name = "PublishHighScorePort")
    public PublishHighScoreEndpoint getPublishHighScorePort() {
        return super.getPort(new QName("http://big.tuwien.ac.at/we/highscore", "PublishHighScorePort"), PublishHighScoreEndpoint.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PublishHighScoreEndpoint
     */
    @WebEndpoint(name = "PublishHighScorePort")
    public PublishHighScoreEndpoint getPublishHighScorePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://big.tuwien.ac.at/we/highscore", "PublishHighScorePort"), PublishHighScoreEndpoint.class, features);
    }

    private static URL __getWsdlLocation() {
        if (PUBLISHHIGHSCORESERVICE_EXCEPTION!= null) {
            throw PUBLISHHIGHSCORESERVICE_EXCEPTION;
        }
        return PUBLISHHIGHSCORESERVICE_WSDL_LOCATION;
    }

}
