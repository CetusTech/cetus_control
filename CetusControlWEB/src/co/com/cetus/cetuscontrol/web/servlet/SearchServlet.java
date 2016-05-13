package co.com.cetus.cetuscontrol.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.com.cetus.cetuscontrol.dto.ParameterGeneralDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.delegate.GeneralDelegate;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.encriptor.Encriptor;
import co.com.cetus.common.encriptor.EncriptorFactory;
import co.com.cetus.common.encriptor.EncriptorType;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class SearchServlet.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (12/05/2016)
 */
@WebServlet ( name = "SearchServlet", urlPatterns = { "/Search" } )
public class SearchServlet extends HttpServlet {
  
  private static final long      serialVersionUID = 1L;
                                                  
  private static GeneralDelegate generalDelegate  = GeneralDelegate.getInstance();
                                                  
  /* (non-Javadoc)
   * @see javax.servlet.GenericServlet#init()
   */
  @Override
  public void init () throws ServletException {
    super.init();
  }
  
  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    this.processRequest( request, response );
  }
  
  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void doPost ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    this.processRequest( request, response );
  }
  
  private void processRequest ( HttpServletRequest request, HttpServletResponse response ) {
    String optionSearch = null, user = null, filter = null, acronym = null, token = null, parameter = null, input = null;
    ResponseDTO responseDTO = null;
    UserPortalDTO userDTO = null;
    ParameterGeneralDTO parameterGeneralDTO = null;
    String[] params = null;
    try {
      ConstantWEB.WEB_LOG.debug( "VALIDANDO INGRESO AL PROCESO DE BUSQUEDA GENERAL" );
      if ( request != null ) {
        
        parameter = request.getParameter( "parameters" );
        token = request.getParameter( "token" );
        
        ConstantWEB.WEB_LOG.debug( "parameters = " + parameter );
        ConstantWEB.WEB_LOG.debug( "token = " + token );
        
        if ( !UtilCommon.stringNullOrEmpty( parameter ) && !UtilCommon.stringNullOrEmpty( token ) ) {
          
          Encriptor encriptor = EncriptorFactory.createEncriptor( EncriptorType.AES128 );
          token = encriptor.getValueDecrypted( token, null );
          parameter = encriptor.getValueDecrypted( parameter, token );
        } else {
          throw new Exception( "El token o los parametros son nulos" );
        }
        ConstantWEB.WEB_LOG.debug( "token recibido = " + token );
        ConstantWEB.WEB_LOG.debug( "Parametros recibidos = " + parameter );
        
        params = parameter.split( "\\" + ConstantCommon.PARAMETRES_SEPARATOR );
        if ( params != null && params.length == 5 ) {
          //Usuario que se encuentra logueado en el sistema
          user = params[0];
          // Opcion de busqueda
          optionSearch = params[1];
          // Filtro de busqueda
          filter = params[2];
          // Entrada para la busqueda
          input = params[3];
          //Acronimo de la funcionalidad
          acronym = params[4];
          
        } else {
          throw new Exception( "El numero de los parametros no son correctos" );
        }
        
        HttpSession theSession = request.getSession( false );
        
        if ( theSession != null ) {
          synchronized ( theSession ) {
            theSession.invalidate();
          }
        }
        
        String ipClient = UtilCommon.getIpRequester( request );
        ConstantWEB.WEB_LOG.info( "user=" + user + ", optionSearch=" + optionSearch + ", filter=" + filter + ", input=" + input + ", acronym="
                                  + acronym + ", ipClient=" + ipClient );
        if ( !UtilCommon.stringNullOrEmpty( user ) && !UtilCommon.stringNullOrEmpty( optionSearch ) && !UtilCommon.stringNullOrEmpty( filter )
             && !UtilCommon.stringNullOrEmpty( input ) && !UtilCommon.stringNullOrEmpty( acronym ) ) {
             
          //Buscar si el usuario que solicita una vista de Cetus Control Tiene privilegios
          responseDTO = generalDelegate.searchUserLogged( user );
          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            userDTO = ( UserPortalDTO ) responseDTO.getObjectResponse();
            if ( userDTO != null ) {
              
              // Subiendo los parametros generales del cliente cetus
              responseDTO = generalDelegate.findParameterGeneralByClientCetus( userDTO.getPerson().getClient().getClientCetus().getId() );
              //              
              if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                parameterGeneralDTO = ( ParameterGeneralDTO ) responseDTO.getObjectResponse();
              } else {
                ConstantWEB.WEB_LOG.info( "El Cliente Cetus no tiene configurado los parametros generales" );
              }
                            
                            HttpSession session = request.getSession( true );
              //              session.setAttribute( ConstantWEB.DESC_USER_DTO, userDTO );
              //              session.setAttribute( ConstantWEB.DESC_PARAMETER_GENERAL_DTO, parameterGeneralDTO );
              //              session.setAttribute( ConstantWEB.DESC_ACRONYM, acronym );
              //              session.setAttribute( ConstantWEB.DESC_IP_REQUEST, ipClient );
              //              //              session.setAttribute( ConstantWEB.DESC_HOST_REQUEST, request.getRemoteHost() );
              //              session.setAttribute( ConstantWEB.DESC_APP, app );//Subir a session el id de la aplicacion 
              //              response.sendRedirect( request.getContextPath() + url );
            } else {
              response.sendRedirect( request.getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
            }
          } else {
            response.sendRedirect( request.getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
          }
        }
      }
    } catch (
    
    Exception e )
    
    {
      try {
        response.sendRedirect( request.getContextPath() + ConstantWEB.URL_PAGE_ERROR );
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      } catch ( IOException e1 ) {
        ConstantWEB.WEB_LOG.error( e1.getMessage(), e1 );
      }
    }
  }
}
