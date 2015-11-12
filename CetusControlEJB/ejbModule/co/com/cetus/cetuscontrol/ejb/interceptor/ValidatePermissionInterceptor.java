package co.com.cetus.cetuscontrol.ejb.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import co.com.cetus.cetuscontrol.ejb.delegate.CetusVortalDelegate;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.common.dto.ResponseWSDTO;
import co.com.cetus.common.dto.UserWSDTO;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.portal.ejb.service.ValidPermServiceRequestDTO;

public class ValidatePermissionInterceptor {
  
  /**
   * </p> Metodo encargado de interceptar la solicitud y validar el permiso sobre el servicio a consumir. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param ctx the ctx
   * @return el object
   * @throws Exception the exception
   * @since CetusControlEJB (18/02/2015)
   */
  @AroundInvoke
  public Object interceptService ( InvocationContext ctx ) throws Exception {
    co.com.cetus.portal.ejb.service.ResponseWSDTO response = null;
    ResponseWSDTO lResponseWSDTO = null;
    Object[] parameter = null;
    ValidPermServiceRequestDTO validPermDTO = null;
    UserWSDTO userWSDTO = null;
    CetusVortalDelegate delegate = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Interceptado servicio ::> " + ctx.getMethod().getName() );
      
      parameter = ctx.getParameters();
      userWSDTO = ( UserWSDTO ) parameter[0];
      
      // Validar permisos del usuario para ejecutar el servicio
      validPermDTO = new ValidPermServiceRequestDTO();
      validPermDTO.setApplication( ConstantEJB.ID_APPLICATION_CETUS_CONTROL );
      validPermDTO.setService( ctx.getMethod().getName() );
      validPermDTO.setUser( userWSDTO.getUser() );
      validPermDTO.setPassword( userWSDTO.getPassword() );
      
      delegate = new CetusVortalDelegate();
      response = delegate.validPermissionService( validPermDTO );
      
      if ( !validateResponseSuccess( response ) ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Usuario no autorizado para ejecutar el servicio" );
        lResponseWSDTO = UtilCommon.createMessageFAILURE_WS();
        lResponseWSDTO.setMessage( ConstantEJB.NO_PERMISSION_SERVICE );
      } else {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Usuario autorizado para ejecutar el servicio" );
        lResponseWSDTO = ( ResponseWSDTO ) ctx.proceed();
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      lResponseWSDTO = UtilCommon.createMessageFAILURE_WS();
      lResponseWSDTO.setMessage( lResponseWSDTO.getMessage() + "-" + e.getMessage() );
    }
    return lResponseWSDTO;
  }
  
  /**
   * </p> Validate response success. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param pResponse the p response
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (18/02/2015)
   */
  private boolean validateResponseSuccess ( co.com.cetus.portal.ejb.service.ResponseWSDTO pResponse ) {
    if ( pResponse != null && pResponse.getCode() != null && !pResponse.getCode().isEmpty()
         && pResponse.getCode().equals( ConstantCommon.WSResponse.CODE_ONE )
         && pResponse.getType() != null && !pResponse.getType().isEmpty() && pResponse.getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) ) {
      return true;
    }
    return false;
  }
  
}
