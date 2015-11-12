package co.com.cetus.cetuscontrol.ejb.delegate;

import java.net.MalformedURLException;
import java.net.URL;

import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.portal.ejb.service.CreateUserRequestDTO;
import co.com.cetus.portal.ejb.service.CreateUserResponseDTO;
import co.com.cetus.portal.ejb.service.DeleteUserRequestDTO;
import co.com.cetus.portal.ejb.service.FindParameterRequestDTO;
import co.com.cetus.portal.ejb.service.FindParameterResponseDTO;
import co.com.cetus.portal.ejb.service.FindRolsByApplicationRequestDTO;
import co.com.cetus.portal.ejb.service.FindRolsByApplicationResponseDTO;
import co.com.cetus.portal.ejb.service.FindRolsByLoginRequestDTO;
import co.com.cetus.portal.ejb.service.FindRolsByLoginResponseDTO;
import co.com.cetus.portal.ejb.service.ResponseWSDTO;
import co.com.cetus.portal.ejb.service.UpdateUserRequestDTO;
import co.com.cetus.portal.ejb.service.ValidPermServiceRequestDTO;
import co.com.cetus.servicie.facade.CustomerServiceFacade;

/**
 * The Class CetusVortalDelegate.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
public class CetusVortalDelegate {
  
  /** The customer service. */
  private CustomerServiceFacade customerService;
  
  /**
   * </p> Instancia un nuevo cetus vortal delegate. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (18/02/2015)
   */
  public CetusVortalDelegate () {
    init();
  }
  
  /**
   * </p> Inits the. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (18/02/2015)
   */
  private void init () {
    try {
      customerService = new CustomerServiceFacade( new URL( ConstantEJB.WSDL_CETUS_CUSTOMER_SERVICE ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "####### CustomerService Instanciado Correctamente" );
    } catch ( MalformedURLException e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Metodo para consultar los parametros en cetus vortal de un componente EJB </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param findParameterRequestDTO the find parameter request dto
   * @return el find parameter response dto
   * @since CetusControlEJB (18/02/2015)
   */
  public FindParameterResponseDTO findParameter ( FindParameterRequestDTO findParameterRequestDTO ) {
    FindParameterResponseDTO findParameterResponseDTO = null;
    try {
      findParameterResponseDTO = customerService.findParameter( findParameterRequestDTO );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return findParameterResponseDTO;
  }
  
  /**
   * </p> Metodo para validar permiso para consumir un servicio de Cetus Control </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param validPermServiceRequestDTO the valid perm service request dto
   * @return el co.com.cetus.portal.ejb.service. response wsdto
   * @since CetusControlEJB (18/02/2015)
   */
  public ResponseWSDTO validPermissionService ( ValidPermServiceRequestDTO validPermServiceRequestDTO ) {
    ResponseWSDTO responseWSDTO = null;
    try {
      responseWSDTO = customerService.validPermissionService( validPermServiceRequestDTO );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return responseWSDTO;
  }
  
  public FindRolsByApplicationResponseDTO
      findRolByApplication ( FindRolsByApplicationRequestDTO findRolsByApplicationRequestDTO ) {
    FindRolsByApplicationResponseDTO responseWSDTO = null;
    try {
      if ( customerService != null ) {
        responseWSDTO = customerService.findRolByApplication( findRolsByApplicationRequestDTO );
      } else {
        responseWSDTO = null;
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseWSDTO = null;
    }
    return responseWSDTO;
  }
  
  public CreateUserResponseDTO createUserCetus ( CreateUserRequestDTO request ) {
    CreateUserResponseDTO responseWSDTO = null;
    try {
      responseWSDTO = customerService.createUser( request );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return responseWSDTO;
  }
  
  public ResponseWSDTO deleteUserCetus ( DeleteUserRequestDTO request ) {
    ResponseWSDTO responseWSDTO = null;
    try {
      responseWSDTO = customerService.deleteUser( request );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return responseWSDTO;
  }
  
  public FindRolsByLoginResponseDTO findRolByLogin ( FindRolsByLoginRequestDTO request ) {
    FindRolsByLoginResponseDTO responseWSDTO = null;
    try {
      if ( customerService != null ) {
        responseWSDTO = customerService.findRolByLogin( request );
      } else {
        responseWSDTO = null;
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      responseWSDTO = null;
    }
    return responseWSDTO;
  }
  
  public ResponseWSDTO updateUserCetus ( UpdateUserRequestDTO request ) {
    ResponseWSDTO responseWSDTO = null;
    try {
      responseWSDTO = customerService.updateUser( request );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return responseWSDTO;
  }
}
