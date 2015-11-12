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
import co.com.cetus.common.util.UtilCommon;

@WebServlet ( name = "HomeServlet", urlPatterns = { "/Home" } )
public class HomeServlet extends HttpServlet {
  
  private static final long      serialVersionUID = 1L;
  
  private static GeneralDelegate generalDelegate  = GeneralDelegate.getInstance();
  
  /* (non-Javadoc)
   * @see javax.servlet.GenericServlet#init()
   */
  @Override
  public void init () throws ServletException {
    super.init();
    ConstantWEB.WEB_LOG.debug( "########## INICIANDO SERVLET CetusControlWEB" );
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
    String app = null, user = null, url = null, acronym = null;
    ResponseDTO responseDTO = null;
    UserPortalDTO userDTO = null;
    ParameterGeneralDTO parameterGeneralDTO = null;
    try {
      ConstantWEB.WEB_LOG.debug( "########## VALIDAR PETICION DE INGRESO A CETUS CONTROL" );
      if ( request != null ) {
        //Aplicacion de donde llega la peticion
        app = request.getParameter( "app" );
        //Usuario que se encuentra loqueado en el sistema
        user = request.getParameter( "user" );
        //Url de la vista a mostrar
        url = request.getParameter( "url" );
        //Acronimo de la funcionalidad
        acronym = request.getParameter( "acronym" );
        
        HttpSession theSession = request.getSession( false );
        
        if ( theSession != null ) {
          synchronized ( theSession ) {
            theSession.invalidate();
          }
        }
        
        if ( !UtilCommon.stringNullOrEmpty( app ) && !UtilCommon.stringNullOrEmpty( user ) && !UtilCommon.stringNullOrEmpty( url ) ) {
          
          //Buscar si el usuario que solicita una vista de Cetus Control Tiene privilegios
          responseDTO = generalDelegate.searchUserLogged( user );
          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            
            userDTO = ( UserPortalDTO ) responseDTO.getObjectResponse();
            if ( userDTO != null ) {
              
              // Subiendo los parametros generales del cliente cetus
              responseDTO = generalDelegate.findParameterGeneralByClientCetus( userDTO.getPerson().getClient().getClientCetus().getId() );
              
              if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                parameterGeneralDTO = ( ParameterGeneralDTO ) responseDTO.getObjectResponse();
              } else {
                ConstantWEB.WEB_LOG.info( "El Cliente Cetus no tiene configurado los parametros generales" );
              }
              
              HttpSession session = request.getSession( true );
              session.setAttribute( ConstantWEB.DESC_USER_DTO, userDTO );
              session.setAttribute( ConstantWEB.DESC_PARAMETER_GENERAL_DTO, parameterGeneralDTO );
              session.setAttribute( ConstantWEB.DESC_ACRONYM, acronym );
              session.setAttribute( ConstantWEB.DESC_IP_REQUEST, request.getRemoteAddr() );
              session.setAttribute( ConstantWEB.DESC_HOST_REQUEST, request.getRemoteHost() );
              session.setAttribute( ConstantWEB.DESC_APP, app );//Subir a session el id de la aplicacion 
              response.sendRedirect( request.getContextPath() + url );
            } else {
              response.sendRedirect( request.getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
            }
          } else {
            response.sendRedirect( request.getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
          }
        } else {
          response.sendRedirect( request.getContextPath() + ConstantWEB.URL_PAGE_ERROR );
        }
      }
    } catch ( Exception e ) {
      try {
        response.sendRedirect( request.getContextPath() + ConstantWEB.URL_PAGE_ERROR );
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      } catch ( IOException e1 ) {
        ConstantWEB.WEB_LOG.error( e1.getMessage(), e1 );
      }
    }
  }
}
