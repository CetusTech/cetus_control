package co.com.cetus.cetuscontrol.ejb.process;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.ejb.validator.CetusControlEJBValidator;
import co.com.cetus.common.dto.ParameterDTO;
import co.com.cetus.common.dto.ReloadParameterRequestDTO;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.dto.ResponseWSDTO;
import co.com.cetus.common.exception.ValidatorException;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class CetusControlWSProcess.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
@Singleton
@Lock ( LockType.READ )
public class CetusControlWSProcess {
  
  @EJB
  CetusControlParameterProcess cetusControlProcess;
  
  public CetusControlWSProcess () {
  }
  
  /**
   * </p> Metodo para recargar los parametros del componente . </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param reloadParameterDTO the reload parameter dto
   * @return el response wsdto
   * @since CetusControlEJB (18/02/2015)
   */
  @SuppressWarnings ( "unchecked" )
  public ResponseWSDTO reloadParameter ( ReloadParameterRequestDTO reloadParameterDTO ) {
    ResponseWSDTO responseWSDTO = null;
    ResponseDTO responseDTO = null;
    List< ParameterDTO > listParameter = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "********* Inicia recarga el componente [" + reloadParameterDTO.getComponent() + "] *********" );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Component =" + reloadParameterDTO.getComponent() + ", ListParameter ="
                                              + reloadParameterDTO.getListParameter() );
      
      CetusControlEJBValidator.componentIsNullOrEmpty( reloadParameterDTO.getComponent() );
      CetusControlEJBValidator.listParameterIsNullOrEmpty( reloadParameterDTO.getListParameter() );
      
      listParameter = ( List< ParameterDTO > ) UtilCommon.fromXML( reloadParameterDTO.getListParameter() );
      
      if ( ConstantEJB.CETUS_CONTROL_EJB_COMPONENT.equals( reloadParameterDTO.getComponent() ) ) {
        if ( listParameter == null ) {
          listParameter = new ArrayList< ParameterDTO >();
        }
        responseDTO = cetusControlProcess.reloadParameter( listParameter );
        
        if ( responseDTO != null ) {
          responseWSDTO = UtilCommon.convetResponseToResponseWS( responseDTO );
        } else {
          responseWSDTO = UtilCommon.createMessageFAILURE_WS();
        }
      } else {
        responseWSDTO = UtilCommon.createMessageWRONG_PARAMETERS_WS();
        responseWSDTO.setMessage( ConstantEJB.NO_EXISTS_COMPONENT );
      }
    } catch ( ValidatorException ve ) {
      responseWSDTO = UtilCommon.createMessageWRONG_PARAMETERS_WS();
      responseWSDTO.setMessage( ve.getMessage() );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseWSDTO = UtilCommon.createMessageFAILURE_WS();
    }
    ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "********* Finaliza recarga el componente [" + reloadParameterDTO.getComponent() + "] *********" );
    return responseWSDTO;
  }
  
}
