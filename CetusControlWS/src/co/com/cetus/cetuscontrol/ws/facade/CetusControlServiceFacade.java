package co.com.cetus.cetuscontrol.ws.facade;

import java.net.URL;

import javax.xml.namespace.QName;

import co.com.cetus.cetuscontrol.ejb.service.CetusControlService;
import co.com.cetus.cetuscontrol.ejb.service.CetusControlServiceService;
import co.com.cetus.cetuscontrol.ejb.service.ReloadParameterRequestDTO;
import co.com.cetus.cetuscontrol.ejb.service.ResponseWSDTO;

/**
 * The Class CetusControlServiceFacade.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWS (23/03/2016)
 */
public class CetusControlServiceFacade {
  
  /** The service. */
  private CetusControlServiceService service                     = null;
                                                                 
  /** The port. */
  private CetusControlService        port                        = null;
                                                                 
  /** The Constant CETUS_CONTROL_SERVICE_QNAME. */
  private final static QName         CETUS_CONTROL_SERVICE_QNAME = new QName( "http://service.ejb.cetuscontrol.cetus.com.co/",
                                                                              "CetusControlServiceService" );
                                                                              
  /**
   * </p> Instancia un nuevo cetus control service facade. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param urlWS the url ws
   * @since CetusControlWS (23/03/2016)
   */
  public CetusControlServiceFacade ( URL urlWS ) {
    try {
      if ( urlWS != null ) {
        service = new CetusControlServiceService( urlWS, CETUS_CONTROL_SERVICE_QNAME );
      } else {
        //WSDL por defecto
        service = new CetusControlServiceService();
      }
      port = service.getCetusControlServicePort();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * </p> Reload parameter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param reloadParameterRequestDTO the reload parameter request dto
   * @return el response wsdto
   * @since CetusControlWS (23/03/2016)
   */
  public ResponseWSDTO reloadParameter ( ReloadParameterRequestDTO reloadParameterRequestDTO ) {
    return port.reloadParameter( reloadParameterRequestDTO );
    
  }
  
}
