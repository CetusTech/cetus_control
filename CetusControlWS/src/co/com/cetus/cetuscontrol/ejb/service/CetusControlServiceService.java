
package co.com.cetus.cetuscontrol.ejb.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CetusControlServiceService", targetNamespace = "http://service.ejb.cetuscontrol.cetus.com.co/", wsdlLocation = "http://cetusprep-cetustech.rhcloud.com/CetusControlEJB/CetusControlService?wsdl")
public class CetusControlServiceService
    extends Service
{

    private final static URL CETUSCONTROLSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException CETUSCONTROLSERVICESERVICE_EXCEPTION;
    private final static QName CETUSCONTROLSERVICESERVICE_QNAME = new QName("http://service.ejb.cetuscontrol.cetus.com.co/", "CetusControlServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://cetusprep-cetustech.rhcloud.com/CetusControlEJB/CetusControlService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CETUSCONTROLSERVICESERVICE_WSDL_LOCATION = url;
        CETUSCONTROLSERVICESERVICE_EXCEPTION = e;
    }

    public CetusControlServiceService() {
        super(__getWsdlLocation(), CETUSCONTROLSERVICESERVICE_QNAME);
    }

    public CetusControlServiceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), CETUSCONTROLSERVICESERVICE_QNAME, features);
    }

    public CetusControlServiceService(URL wsdlLocation) {
        super(wsdlLocation, CETUSCONTROLSERVICESERVICE_QNAME);
    }

    public CetusControlServiceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CETUSCONTROLSERVICESERVICE_QNAME, features);
    }

    public CetusControlServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CetusControlServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CetusControlService
     */
    @WebEndpoint(name = "CetusControlServicePort")
    public CetusControlService getCetusControlServicePort() {
        return super.getPort(new QName("http://service.ejb.cetuscontrol.cetus.com.co/", "CetusControlServicePort"), CetusControlService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CetusControlService
     */
    @WebEndpoint(name = "CetusControlServicePort")
    public CetusControlService getCetusControlServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.ejb.cetuscontrol.cetus.com.co/", "CetusControlServicePort"), CetusControlService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CETUSCONTROLSERVICESERVICE_EXCEPTION!= null) {
            throw CETUSCONTROLSERVICESERVICE_EXCEPTION;
        }
        return CETUSCONTROLSERVICESERVICE_WSDL_LOCATION;
    }

}
