package co.com.cetus.cetuscontrol.ejb.process;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import co.com.cetus.cetuscontrol.ejb.delegate.CetusVortalDelegate;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.common.dto.ParameterDTO;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.portal.ejb.service.FindParameterRequestDTO;
import co.com.cetus.portal.ejb.service.FindParameterResponseDTO;

/**
 * The Class CetusControlParameterProcess.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
@Singleton
@Lock ( LockType.READ )
public class CetusControlParameterProcess {
  
  /** The cetus vortal delegate. */
  private CetusVortalDelegate cetusVortalDelegate;
  
  /**
   * </p> Instancia un nuevo cetus control parameter process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (18/02/2015)
   */
  public CetusControlParameterProcess () {
    cetusVortalDelegate = new CetusVortalDelegate();
  }
  
  /**
   * </p> Reload parameter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param listParameter the list parameter
   * @return el response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseDTO reloadParameter ( List< ParameterDTO > listParameter ) {
    ResponseDTO responseDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Inicia el re cargue de los parametros de Cetus Control..." );
      if ( ConstantEJB.loadParameter( listParameter ) ) {
        responseDTO = UtilCommon.createMessageSUCCESS();
      } else {
        responseDTO = UtilCommon.createMessageFAILURE();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseDTO = UtilCommon.createMessageFAILURE();
      responseDTO.setMessage( responseDTO.getMessage() + "-" + e.getMessage() );
      
    }
    ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Inicia el re cargue de los parametros de Cetus Control..." );
    return responseDTO;
  }
  
  /**
   * </p> Load parameter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el list
   * @since CetusControlEJB (18/02/2015)
   */
  public List< ParameterDTO > loadParameter () {
    List< co.com.cetus.portal.ejb.service.ParameterDTO > list = null;
    co.com.cetus.portal.ejb.service.ResponseWSDTO responseWSDTO = null;
    FindParameterResponseDTO findResponseDTO = null;
    FindParameterRequestDTO findParameterRequestDTO = new FindParameterRequestDTO();
    List< ParameterDTO > listParameter = new ArrayList< ParameterDTO >();
    ParameterDTO parameterDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( " CARGA DE PARAMETROS DESDE CETUS VORTAL " );
      findParameterRequestDTO.setUser( ConstantEJB.USER_WS_CETUS );
      findParameterRequestDTO.setPassword( ConstantEJB.PASSWORD_WS_CETUS );
      findParameterRequestDTO.setIdApplication( ConstantEJB.ID_APPLICATION_CETUS_CONTROL );
      findParameterRequestDTO.setNameComponent( ConstantEJB.CETUS_CONTROL_EJB_COMPONENT );
      
      findResponseDTO = cetusVortalDelegate.findParameter( findParameterRequestDTO );
      responseWSDTO = findResponseDTO.getResponseWSDTO();
      if ( responseWSDTO != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
        list = findResponseDTO.getParameters();
        for ( co.com.cetus.portal.ejb.service.ParameterDTO parameter: list ) {
          parameterDTO = new ParameterDTO( parameter.getName(), parameter.getValue() );
          listParameter.add( parameterDTO );
        }
      }
      //Consultar Parametros de CETUS VORTAL
      findParameterRequestDTO.setUser( ConstantEJB.USER_WS_CETUS );
      findParameterRequestDTO.setPassword( ConstantEJB.PASSWORD_WS_CETUS );
      findParameterRequestDTO.setIdApplication( ConstantEJB.ID_APPLICATION_CETUS_VORTAL );
      findParameterRequestDTO.setNameComponent( ConstantEJB.CETUS_VORTAL_EJB_COMPONENT );
      
      findResponseDTO = cetusVortalDelegate.findParameter( findParameterRequestDTO );
      responseWSDTO = findResponseDTO.getResponseWSDTO();
      if ( responseWSDTO != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
        list = findResponseDTO.getParameters();
        for ( co.com.cetus.portal.ejb.service.ParameterDTO parameter: list ) {
          parameterDTO = new ParameterDTO( parameter.getName(), parameter.getValue() );
          listParameter.add( parameterDTO );
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Finaliza el cargue de los parametros de Cetus Control..." );
    return listParameter;
  }
  
}
