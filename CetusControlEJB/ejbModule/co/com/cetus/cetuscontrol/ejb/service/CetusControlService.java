package co.com.cetus.cetuscontrol.ejb.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import co.com.cetus.cetuscontrol.ejb.interceptor.ValidatePermissionInterceptor;
import co.com.cetus.cetuscontrol.ejb.process.CetusControlWSProcess;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.common.dto.ReloadParameterRequestDTO;
import co.com.cetus.common.dto.ResponseWSDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class CetusControlService.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
@Stateless
@WebService
public class CetusControlService {
  
  @EJB
  CetusControlWSProcess cetusControlWSProcess;
  
  public CetusControlService () {
  }
  
  /**
   * </p> Servicio para recargar los parametros de la aplicacion Cetus Control. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param reloadParameterDTO the reload parameter dto
   * @return el response wsdto
   * @since CetusControlEJB (18/02/2015)
   */
  @Interceptors ( ValidatePermissionInterceptor.class )
  @WebMethod
  public @WebResult ( name = "responseWSDTO" ) ResponseWSDTO
      reloadParameter ( @WebParam ( name = "reloadParameterRequestDTO" ) ReloadParameterRequestDTO reloadParameterDTO ) {
    ResponseWSDTO responseWSDTO = null;
    try {
      responseWSDTO = cetusControlWSProcess.reloadParameter( reloadParameterDTO );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseWSDTO = UtilCommon.createMessageFAILURE_WS();
    }
    return responseWSDTO;
  }
  
}
