package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import co.com.cetus.cetuscontrol.dto.ClientDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.portal.ejb.service.FindRolsByApplicationRequestDTO;
import co.com.cetus.portal.ejb.service.FindRolsByApplicationResponseDTO;
import co.com.cetus.portal.ejb.service.RolDTO;

public class UserPortalAdminMBean extends GeneralManagedBean {
  
  /** The Constant serialVersionUID. */
  private static final long     serialVersionUID        = -3255236201554716426L;
                                                        
  /** The list register. */
  private List< UserPortalDTO > listRegister            = null;
                                                        
  /** The add object. */
  private UserPortalDTO         addObject               = null;
                                                        
  /** The selected object. */
  private UserPortalDTO         selectedObject          = null;
                                                        
  /** The show confirm add. */
  private boolean               showConfirmAdd          = false;
                                                        
  /** The show confirm mod. */
  private boolean               showConfirmMod          = false;
                                                        
  /** The show dialog confirm update. */
  private boolean               showDialogConfirmUpdate = false;
                                                        
  /** The show confirm delete. */
  private boolean               showConfirmDelete       = false;
                                                        
  /** The show alert select row. */
  private boolean               showAlertSelectRow      = false;
                                                        
  /** The show view detail. */
  private boolean               showViewDetail          = false;
                                                        
  /** The person dto. */
  private PersonDTO             personDTO               = null;
                                                        
  /** The identity. */
  private String                identity                = null;
                                                        
  private String                typeIdentity            = null;
                                                        
  /** The exists person. */
  private boolean               existsPerson            = false;
                                                        
  /** The message. */
  private String                message                 = null;
                                                        
  private boolean               skip;
                                
  private List< ClientDTO >     listClientDTO           = null;
                                                        
  private ClientDTO             selectedClientDTO       = null;
                                                        
  private List< PersonDTO >     listPersonDTO           = null;
                                                        
  private List< RolDTO >        listRol;
                                
  private List< SelectItem >    listRolItem;
                                
  private List< String >        selectedOptionsRol      = null;
                                                        
  public UserPortalAdminMBean () {
    addObject = new UserPortalDTO();
    addObject.setPerson( new PersonDTO() );
    selectedObject = new UserPortalDTO();
    selectedObject.setPerson( new PersonDTO() );
    
    this.listRolByApp();
  }
  
  private void listRolByApp () {
    FindRolsByApplicationRequestDTO findRolsByApplicationRequestDTO = null;
    ResponseDTO response = null;
    int idApp;
    try {
      findRolsByApplicationRequestDTO = new FindRolsByApplicationRequestDTO();
      if ( getObjectSession( ConstantWEB.DESC_APP ) != null ) {
        idApp = Integer.parseInt( ( String ) getObjectSession( ConstantWEB.DESC_APP ) );
        System.out.println( "ID DE LA APLICACION --- " + idApp );
        findRolsByApplicationRequestDTO.setIdApplication( idApp );
        findRolsByApplicationRequestDTO.setUser( ConstantWEB.USER_PROPERTIE );
        findRolsByApplicationRequestDTO.setPassword( ConstantWEB.PASSWORD_PROPERTIE );
        response = generalDelegate.findRolByApplication( findRolsByApplicationRequestDTO.getIdApplication() );
        
        if ( response != null && UtilCommon.validateResponseSuccess( response ) ) {
          //Existen roles asociados a la aplicacion
          
          FindRolsByApplicationResponseDTO f = ( FindRolsByApplicationResponseDTO ) response.getObjectResponse();
          listRol = f.getRol();
          if ( listRol != null ) {
            listRolItem = new ArrayList< SelectItem >();
            for ( RolDTO item: listRol ) {
              listRolItem.add( new SelectItem( item.getId(), item.getDescripcion() ) );
            }
          }
          
        } else {
          addMessageError( null,
                           ConstantWEB.MESSAGE_ERROR, response != null && response.getMessage() != null ? response.getMessage()
                                                                                                        : ConstantWEB.MESSAGE_ERROR_CREATE );
        }
      }
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  //  
  //  /* (non-Javadoc)
  //   * @see co.com.triangulo.agarthi.web.bean.GeneralManagedBean#initElement()
  //   */
  //  @Override
  //  @PostConstruct
  //  public void initElement () {
  //    listRegister();
  //    listTypeIdentity();
  //    listClient();
  //  }
  //  
  //  
  //  public void load ( ActionEvent event ) {
  //    try {
  //      if ( addObject != null && getObjectSession( "personDTO" ) != null ) {
  //        addObjectSession( addObject, "addObject" );
  //        addObjectSession( selectedOptionsRol, "selectedOptionsRol" );
  //        this.showConfirmAdd = true;
  //      } else {
  //        addMessageError( "btnAdd", ConstantWEB.WARNING, "Debe Seleccionar una Persona." );
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //    }
  //  }
  //  
  //  public void clearObject () {
  //    cleanObjectSession( "selectedObject" );
  //    cleanObjectSession( "personDTO" );
  //    cleanObjectSession( "selectedClientDTO" );
  //    
  //  }
  //  
  //  /**
  //   * </p> Load update. </p>
  //   *
  //   * @param event the event
  //   * @author Andres Herrera - Triangulo Soluciones S.A.S
  //   * @since AgarthiWeb (28/01/2013)
  //   */
  //  public void loadUpdate ( ActionEvent event ) {
  //    UserPortalDTO lDto = null;
  //    try {
  //      if ( selectedObject != null && ( getObjectSession( "personDTO" ) != null || selectedObject.getPersona() != null ) ) {
  //        lDto = ( UserPortalDTO ) getObjectSession( "selectedObject" );
  //        if ( lDto != null ) {
  //          selectedObject.setId( lDto.getId() );
  //          selectedObject.setLoginCetus( lDto.getLoginCetus() );
  //          selectedObject.setFechaCreacion( lDto.getFechaCreacion() );
  //          selectedObject.setUsuarioCreacion( lDto.getUsuarioCreacion() );
  //          addObjectSession( selectedOptionsRol, "selectedOptionsRol" );
  //          if ( getObjectSession( "personDTO" ) == null ) {
  //            addObjectSession( lDto.getPersona(), "personDTO" );
  //          }
  //        }
  //        addObjectSession( selectedObject, "selectedObject" );
  //        this.showDialogConfirmUpdate = true;
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //    }
  //  }
  //  
  //  /**
  //   * </p> List register. </p>
  //   *
  //   * @author Andres Herrera - Triangulo Soluciones S.A.S
  //   * @since AgarthiWeb (28/01/2013)
  //   */
  //  @SuppressWarnings ( "unchecked" )
  //  private void listRegister () {
  //    UserPortalDTO lUserPortalDTOClient = null;
  //    ResponseDTO response = null;
  //    try {
  //      lUserPortalDTOClient = new UserPortalDTO();
  //      
  //      response = delegate.findAllOrder( lUserPortalDTOClient.getClass().getSimpleName(), ConstantWEB.VAR_LOGIN_CETUS,
  //                                        ConstantCommon.TIPO_ASC );
  //      
  //      if ( UtilWEB.validateResponseSuccess( response ) && response.getDataResponseXML() != null ) {
  //        //Respuesta del servicio 
  //        this.listRegister = ( List< UserPortalDTO > ) UtilCommon.fromXML( response.getDataResponseXML() );
  //      } else {
  //        //si no encontro registro para listar 
  //        if ( UtilWEB.validateResponseNoResult( response ) ) {
  //          this.listRegister = new ArrayList< UserPortalDTO >();
  //        }
  //      }
  //    } catch ( Exception e ) {
  //      addMessageError( null, e.getMessage(), "" );
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //    }
  //    
  //  }
  //  
  //  @SuppressWarnings ( "unchecked" )
  //  private void listClient () {
  //    ResponseDTO response = null;
  //    try {
  //      response = delegate.findAllOrder( ClientDTO.class.getSimpleName(), ConstantWEB.VAR_RAZON_SOCIAL,
  //                                        ConstantCommon.TIPO_ASC );
  //      if ( UtilWEB.validateResponseSuccessXMLOutput( response ) ) {
  //        this.listClientDTO = ( List< ClientDTO > ) UtilCommon.fromXML( response.getDataResponseXML() );
  //      } else {
  //        if ( UtilWEB.validateResponseNoResult( response ) ) {
  //          this.listClientDTO = new ArrayList< ClientDTO >();
  //        }
  //      }
  //    } catch ( Exception e ) {
  //      addMessageError( null, ConstantWEB.ERROR, e.getMessage() );
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //    }
  //  }
  //  
  //  /* (non-Javadoc)
  //   * @see co.com.triangulo.agarthi.web.bean.GeneralManagedBean#delete()
  //   */
  //  @Override
  //  public String delete () {
  //    ResponseDTO response = null;
  //    co.com.triangulo.cetus.portal.ejb.service.ResponseWSDTO responseCetus = null;
  //    try {
  //      this.showConfirmDelete = false;
  //      selectedObject = ( UserPortalDTO ) getObjectSession( "selectedObject" );
  //      if ( selectedObject != null ) {
  //        
  //        responseCetus = deleteUserCetus( selectedObject );
  //        
  //        if ( validateResponseCetus( responseCetus ) ) {
  //          response = delegate.remove( UtilCommon.toXML( selectedObject ), selectedObject.getClass().getSimpleName() );
  //          
  //          if ( UtilWEB.validateResponseSuccess( response ) ) {
  //            this.initElement();
  //            addMessageInfo( null, ConstantWEB.INFORMATION, response.getMessage() );
  //          } else {
  //            addMessageError( null, ConstantWEB.ERROR, response.getMessage() );
  //          }
  //          
  //        } else {
  //          addMessageError( null, ConstantWEB.ERROR,
  //                           ( responseCetus != null && responseCetus.getMessage() != null ) ? responseCetus.getMessage()
  //                                                                                          : ConstantWEB.ERROR_CREATE_USER_CETUS );
  //        }
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //      addMessageError( null, ConstantWEB.MESSAGE_ERROR_DELETE, null );
  //    } finally {
  //      cleanObjectSession( "existsPerson" );
  //      cleanObjectSession( "personDTO" );
  //    }
  //    return null;
  //  }
  //  
  //  /* (non-Javadoc)
  //   * @see co.com.triangulo.agarthi.web.bean.GeneralManagedBean#update()
  //   */
  //  @Override
  //  public String update () {
  //    boolean lSuccessfull = false;
  //    RequestContext context = null;
  //    ResponseDTO response = null;
  //    co.com.triangulo.cetus.portal.ejb.service.ResponseWSDTO responseCetus = null;
  //    try {
  //      this.showConfirmMod = false;
  //      selectedObject = ( UserPortalDTO ) getObjectSession( "selectedObject" );
  //      
  //      if ( selectedObject != null ) {
  //        context = RequestContext.getCurrentInstance();
  //        selectedObject.setUsuarioCreacion( selectedObject.getUsuarioCreacion() );
  //        selectedObject.setFechaCreacion( selectedObject.getFechaCreacion() );
  //        selectedObject.setUsuarioModificacion( getUsuarioCreacion() );
  //        selectedObject.setFechaModificacion( new Date() );
  //        selectedObject.setPersona( ( PersonDTO ) getObjectSession( "personDTO" ) );
  //        
  //        responseCetus = updateUserCetus( selectedObject );
  //        
  //        if ( validateResponseCetus( responseCetus ) ) {
  //          response = delegate.edit( UtilCommon.toXML( selectedObject ), selectedObject.getClass().getSimpleName() );
  //          
  //          if ( UtilWEB.validateResponseSuccess( response ) ) {
  //            this.initElement();
  //            lSuccessfull = true;
  //            context.addCallbackParam( "lSuccessfull", lSuccessfull );
  //            addMessageInfo( null, ConstantWEB.INFORMATION, response.getMessage() );
  //          } else {
  //            addMessageError( null, ConstantWEB.ERROR, response.getMessage() );
  //          }
  //        } else {
  //          addMessageError( null, ConstantWEB.ERROR,
  //                           ( responseCetus != null && responseCetus.getMessage() != null ) ? responseCetus.getMessage()
  //                                                                                          : ConstantWEB.ERROR_CREATE_USER_CETUS );
  //        }
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //      addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, null );
  //    } finally {
  //      cleanObjectSession( "selectedObject" );
  //      cleanObjectSession( "personDTO" );
  //      cleanObjectSession( "selectedClientDTO" );
  //    }
  //    
  //    return null;
  //  }
  //  
  //  /* (non-Javadoc)
  //   * @see co.com.triangulo.agarthi.web.bean.GeneralManagedBean#add()
  //   */
  //  @Override
  //  public String add () {
  //    boolean lSuccessfull = false;
  //    RequestContext context = null;
  //    ResponseDTO response = null;
  //    co.com.triangulo.cetus.portal.ejb.service.CreateUserResponseDTO responseCetus = null;
  //    try {
  //      this.showConfirmAdd = false;
  //      addObject = ( UserPortalDTO ) getObjectSession( "addObject" );
  //      if ( addObject != null && getObjectSession( "personDTO" ) != null ) {
  //        addObject.setPersona( ( PersonDTO ) getObjectSession( "personDTO" ) );
  //        addObject.setUsuarioCreacion( getUsuarioCreacion() );
  //        addObject.setFechaCreacion( new Date() );
  //        addObject.setStatus( ConstantWEB.ACTIVE );
  //        
  //        //Crear usuario en CETUS
  //        context = RequestContext.getCurrentInstance();
  //        responseCetus = createUserCetus( addObject );
  //        if ( responseCetus != null && responseCetus != null && responseCetus.getResponse() != null && responseCetus.getResponse().getCode() != null
  //             && !responseCetus.getResponse().getCode().isEmpty()
  //             && responseCetus.getResponse().getCode() != null
  //             && !responseCetus.getResponse().getCode().isEmpty()
  //             && responseCetus.getResponse().getCode().equals( ConstantCommon.WSResponse.CODE_ONE )
  //             && responseCetus.getResponse().getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) && responseCetus.getUsuarioDTO() != null
  //             && responseCetus.getUsuarioDTO().getId() > 0 ) {
  //          
  //          addObject.setId( responseCetus.getUsuarioDTO().getId() );
  //          response = delegate.create( UtilCommon.toXML( addObject ), addObject.getClass().getSimpleName() );
  //          if ( UtilWEB.validateResponseSuccess( response ) ) {
  //            this.initElement();
  //            lSuccessfull = true;
  //            addMessageInfo( null, ConstantWEB.INFORMATION, response.getMessage() );
  //          } else {
  //            addMessageError( null, ConstantWEB.ERROR, response.getMessage() );
  //          }
  //        } else {
  //          addMessageError( null,
  //                           ConstantWEB.ERROR,
  //                           ( responseCetus != null && responseCetus.getResponse() != null && responseCetus.getResponse().getMessage() != null )
  //                                                                                                                                               ? responseCetus.getResponse()
  //                                                                                                                                                              .getMessage()
  //                                                                                                                                               : ConstantWEB.ERROR_CREATE_USER_CETUS );
  //        }
  //        context.addCallbackParam( "lSuccessfull", lSuccessfull );
  //        
  //      }
  //      
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
  //    } finally {
  //    }
  //    
  //    return null;
  //  }
  //  
  //  public co.com.triangulo.cetus.portal.ejb.service.ResponseWSDTO deleteUserCetus ( UserPortalDTO pUserPortalDTO ) {
  //    UsuarioDTO usuarioDTO = null;
  //    co.com.triangulo.cetus.portal.ejb.service.ResponseWSDTO response = null;
  //    DeleteUserRequestDTO request = null;
  //    try {
  //      if ( pUserPortalDTO != null ) {
  //        usuarioDTO = new UsuarioDTO();
  //        usuarioDTO.setId( pUserPortalDTO.getId() );
  //        
  //        request = new DeleteUserRequestDTO();
  //        request.setPassword( ConstantWEB.PASSWORD_WS_CETUS );
  //        request.setUser( ConstantWEB.USER_WS_CETUS );
  //        request.setUsuarioDTO( usuarioDTO );
  //        response = delegate.deleteUserCetus( request );
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //      return null;
  //    }
  //    return response;
  //  }
  //  
  //  public boolean validateResponseCetus ( ResponseWSDTO response ) {
  //    if ( response != null && response != null && response.getCode() != null && !response.getCode().isEmpty() && response.getCode() != null
  //         && !response.getCode().isEmpty()
  //         && response.getCode().equals( ConstantCommon.WSResponse.CODE_ONE )
  //         && response.getType().equals( ConstantCommon.WSResponse.TYPE_SUCCESS ) ) {
  //      return true;
  //    }
  //    return false;
  //  }
  //  
  //  @SuppressWarnings ( "unchecked" )
  //  public co.com.triangulo.cetus.portal.ejb.service.ResponseWSDTO updateUserCetus ( UserPortalDTO pUserPortalDTO ) {
  //    UsuarioDTO usuarioDTO = null;
  //    co.com.triangulo.cetus.portal.ejb.service.ResponseWSDTO response = null;
  //    UpdateUserRequestDTO request;
  //    List< String > roles = null;
  //    try {
  //      if ( getObjectSession( "selectedOptionsRol" ) != null ) {
  //        roles = ( List< String > ) getObjectSession( "selectedOptionsRol" );
  //      }
  //      if ( pUserPortalDTO != null ) {
  //        usuarioDTO = new UsuarioDTO();
  //        usuarioDTO.setEmail( pUserPortalDTO.getPersona().getEmail() );
  //        usuarioDTO.setAddress( pUserPortalDTO.getPersona().getDireccion() );
  //        usuarioDTO.setFechaCreacion( UtilWEB.converDateToXMLGregorianCalendar( pUserPortalDTO.getFechaCreacion() ) );
  //        usuarioDTO.setUsuarioCreacion( pUserPortalDTO.getUsuarioCreacion() );
  //        usuarioDTO.setIdentificacion( pUserPortalDTO.getPersona().getNumeroIdentificacion() );
  //        usuarioDTO.setLogin( pUserPortalDTO.getLoginCetus() );
  //        usuarioDTO.setPhone( pUserPortalDTO.getPersona().getTelefonoFijo() );
  //        usuarioDTO.setId( pUserPortalDTO.getId() );
  //        request = new UpdateUserRequestDTO();
  //        request.setPassword( ConstantWEB.PASSWORD_WS_CETUS );
  //        request.setUser( ConstantWEB.USER_WS_CETUS );
  //        request.setUsuarioDTO( usuarioDTO );
  //        request.setRol( UtilCommon.toXML( roles ) );
  //        if ( pUserPortalDTO.getStatus().equals( ConstantWEB.ACTIVE ) ) {
  //          usuarioDTO.setStatus( ConstantWEB.USER_ACTIVO );
  //        } else {
  //          usuarioDTO.setStatus( ConstantWEB.USER_INACTIVO );
  //        }
  //        response = delegate.updateUserCetus( request );
  //        
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //      return null;
  //    }
  //    return response;
  //  }
  //  
  //  @SuppressWarnings ( "unchecked" )
  //  public co.com.triangulo.cetus.portal.ejb.service.CreateUserResponseDTO createUserCetus ( UserPortalDTO pUserPortalDTO ) {
  //    UsuarioDTO usuarioDTO = null;
  //    co.com.triangulo.cetus.portal.ejb.service.CreateUserResponseDTO response = null;
  //    CreateUserRequestDTO request;
  //    List< String > roles = null;
  //    try {
  //      if ( getObjectSession( "selectedOptionsRol" ) != null ) {
  //        roles = ( List< String > ) getObjectSession( "selectedOptionsRol" );
  //      }
  //      if ( pUserPortalDTO != null ) {
  //        usuarioDTO = new UsuarioDTO();
  //        usuarioDTO.setEmail( pUserPortalDTO.getPersona().getEmail() );
  //        usuarioDTO.setAddress( pUserPortalDTO.getPersona().getDireccion() );
  //        usuarioDTO.setFechaCreacion( UtilWEB.converDateToXMLGregorianCalendar( pUserPortalDTO.getFechaCreacion() ) );
  //        usuarioDTO.setUsuarioCreacion( pUserPortalDTO.getUsuarioCreacion() );
  //        usuarioDTO.setIdentificacion( pUserPortalDTO.getPersona().getNumeroIdentificacion() );
  //        usuarioDTO.setLogin( pUserPortalDTO.getLoginCetus() );
  //        usuarioDTO.setPhone( pUserPortalDTO.getPersona().getTelefonoFijo() );
  //        
  //        request = new CreateUserRequestDTO();
  //        request.setPassword( ConstantWEB.PASSWORD_WS_CETUS );
  //        request.setUser( ConstantWEB.USER_WS_CETUS );
  //        request.setUsuarioDTO( usuarioDTO );
  //        request.setRol( UtilCommon.toXML( roles ) );
  //        response = delegate.createUserCetus( request );
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //      return null;
  //    }
  //    return response;
  //  }
  //  
  //  /**
  //   * </p> Validate selected record. </p>
  //   *
  //   * @param event the event
  //   * @author Andres Herrera - Triangulo Soluciones S.A.S
  //   * @since AgarthiWeb (28/01/2013)
  //   */
  //  public void validateSelectedRecord ( ActionEvent event ) {
  //    try {
  //      cleanObjectSession( "personDTO" );
  //      cleanObjectSession( "selectedObject" );
  //      cleanObjectSession( "selectedClientDTO" );
  //      
  //      if ( selectedObject != null ) {
  //        addObjectSession( selectedObject, "selectedObject" );
  //        if ( selectedObject.getPersona() != null && selectedObject.getPersona().getCliente() != null ) {
  //          addObjectSession( selectedObject.getPersona().getCliente(), "selectedClientDTO" );
  //        }
  //        this.showAlertSelectRow = false;
  //        this.showViewDetail = true;
  //        this.showConfirmMod = true;
  //        this.showConfirmDelete = true;
  //      } else {
  //        this.showAlertSelectRow = true;
  //        this.showViewDetail = false;
  //        this.showConfirmMod = false;
  //        this.showConfirmDelete = false;
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //    }
  //  }
  //  
  //  @SuppressWarnings ( "unchecked" )
  //  public String onFlowProcess ( FlowEvent event ) {
  //    ResponseDTO response = null;
  //    try {
  //      if ( event.getOldStep().equals( "client" ) ) {
  //        //Validar que haya seleccionado un cliente
  //        if ( selectedClientDTO == null ) {
  //          addMessageWarning( ":formAdd:wizUser", ConstantWEB.WARNING, ConstantWEB.MSG_WARNING_CLIENT_NOT_EMPTY );
  //          return "client";
  //        } else {
  //          addObjectSession( selectedClientDTO, "selectedClientDTO" );
  //          response = delegate.findAllPersonByIdClient( selectedClientDTO.getId() );
  //          if ( UtilWEB.validateResponseSuccessXMLOutput( response ) ) {
  //            this.listPersonDTO = ( List< PersonDTO > ) UtilCommon.fromXML( response.getDataResponseXML() );
  //          } else {
  //            this.listPersonDTO = new ArrayList< PersonDTO >();
  //          }
  //          addObjectSession( listPersonDTO, "listPersonDTO" );
  //        }
  //      }
  //      
  //      if ( event.getOldStep().equals( "person" ) && event.getNewStep().equals( "user" ) ) {
  //        //Validar que haya seleccionado una persona para el cliente seleccionado 
  //        if ( personDTO == null ) {
  //          addMessageWarning( ":formAdd:wizUser", ConstantWEB.WARNING, ConstantWEB.MSG_WARING_PERSON_NOT_EMPTY );
  //          return "person";
  //        } else {
  //          addObjectSession( personDTO, "personDTO" );
  //        }
  //      }
  //      
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //    }
  //    return event.getNewStep();
  //  }
  //  
  //  @SuppressWarnings ( "unchecked" )
  //  public String onFlowProcessUpdate ( FlowEvent event ) {
  //    ResponseDTO response = null;
  //    UserPortalDTO UserPortalDTOTemp = null;
  //    List< RolDTO > listaRolDTO;
  //    FindRolsByLoginResponseDTO responseLogin = null;
  //    FindRolsByLoginRequestDTO requestLogin = null;
  //    try {
  //      
  //      if ( selectedObject != null ) {
  //        
  //        //Preciono el siguiente de la pestañana de Cliente
  //        if ( event.getOldStep() != null && event.getOldStep().equals( "clientUpdate" ) ) {
  //          //Validar que haya seleccionado un cliente
  //          if ( selectedClientDTO == null ) {
  //            addMessageWarning( ":formUpdate:wizUserUpdate", ConstantWEB.WARNING, "Debe seleccionar un cliente" );
  //            return "clientUpdate";
  //          } else {
  //            //Cliente Seleccionado 
  //            addObjectSession( selectedClientDTO, "selectedClientDTO" );
  //            
  //            //Buscar las personas asociadas a el cliente seleccionado
  //            response = delegate.findAllPersonByIdClient( selectedClientDTO.getId() );
  //            if ( UtilWEB.validateResponseSuccessXMLOutput( response ) ) {
  //              this.listPersonDTO = ( List< PersonDTO > ) UtilCommon.fromXML( response.getDataResponseXML() );
  //            } else {
  //              this.listPersonDTO = new ArrayList< PersonDTO >();
  //            }
  //            addObjectSession( listPersonDTO, "listPersonDTO" );
  //            
  //            //Obtener la informacion del usuario antes de actualizar 
  //            UserPortalDTOTemp = ( UserPortalDTO ) getObjectSession( "selectedObject" );
  //            
  //            //Si el usuario no ha cambiado de cliente se deja por defecto la persona que tiene actualmente
  //            if ( UserPortalDTOTemp != null && UserPortalDTOTemp.getPersona().getCliente().getId() == selectedClientDTO.getId() ) {
  //              addObjectSession( UserPortalDTOTemp.getPersona(), "personDTO" );
  //            }
  //          }
  //        }
  //        //Preciono el siguiente de la pestañana de Persona para pasar a la de USuario
  //        if ( event.getOldStep().equals( "personUpdate" ) && event.getNewStep().equals( "userUpdate" ) ) {
  //          //Validar que haya seleccionado una persona para el cliente seleccionado 
  //          if ( personDTO == null ) {
  //            addMessageWarning( ":formUpdate:wizUserUpdate", ConstantWEB.WARNING, "Debe seleccionar una persona" );
  //            return "personUpdate";
  //          } else {
  //            addObjectSession( personDTO, "personDTO" );
  //            
  //            //Consultar Roles para el usuario seleccionado 
  //            selectedObject = ( UserPortalDTO ) getObjectSession( "selectedObject" );
  //            if ( selectedObject != null ) {
  //              requestLogin = new FindRolsByLoginRequestDTO();
  //              requestLogin.setLogin( selectedObject.getLoginCetus() );
  //              requestLogin.setPassword( ConstantWEB.PASSWORD_WS_CETUS );
  //              requestLogin.setUser( ConstantWEB.USER_WS_CETUS );
  //              responseLogin = delegate.findRolByLogin( requestLogin );
  //              if ( responseLogin != null && validateResponseCetus( responseLogin.getResponseWSDTO() ) && responseLogin.getRol() != null
  //                   && !responseLogin.getRol().isEmpty() ) {
  //                listaRolDTO = responseLogin.getRol();
  //                selectedOptionsRol = new ArrayList< String >();
  //                if ( listaRolDTO != null && !listaRolDTO.isEmpty() ) {
  //                  for ( RolDTO roldto: listaRolDTO ) {
  //                    for ( SelectItem rol: listRolItem ) {
  //                      if ( roldto.getId() == ( Integer ) rol.getValue() ) {
  //                        //Seleccionar Rol para un login
  //                        selectedOptionsRol.add( String.valueOf( roldto.getId() ) );
  //                      }
  //                    }
  //                  }
  //                }
  //              }
  //            }
  //          }
  //        }
  //        // Si preciona el boton atras en la pestaña de Usuario 
  //        if ( event.getOldStep().equals( "userUpdate" ) && event.getNewStep().equals( "personUpdate" ) ) {
  //          UserPortalDTOTemp = ( UserPortalDTO ) getObjectSession( "selectedObject" );
  //          if ( UserPortalDTOTemp != null ) {
  //            personDTO = UserPortalDTOTemp.getPersona();
  //            if ( personDTO != null ) {
  //              selectedClientDTO = personDTO.getCliente();
  //              if ( selectedClientDTO != null ) {
  //                //Cliente Seleccionado 
  //                addObjectSession( selectedClientDTO, "selectedClientDTO" );
  //                
  //                //Buscar las personas asociadas a el cliente seleccionado
  //                response = delegate.findAllPersonByIdClient( selectedClientDTO.getId() );
  //                if ( UtilWEB.validateResponseSuccessXMLOutput( response ) ) {
  //                  this.listPersonDTO = ( List< PersonDTO > ) UtilCommon.fromXML( response.getDataResponseXML() );
  //                } else {
  //                  this.listPersonDTO = new ArrayList< PersonDTO >();
  //                }
  //                addObjectSession( listPersonDTO, "listPersonDTO" );
  //              }
  //              addObjectSession( personDTO, "personDTO" );
  //            }
  //          }
  //        }
  //        
  //        if ( event.getOldStep().equals( "personUpdate" ) && event.getNewStep().equals( "clientUpdate" ) ) {
  //          UserPortalDTOTemp = ( UserPortalDTO ) getObjectSession( "selectedObject" );
  //          if ( UserPortalDTOTemp != null ) {
  //            personDTO = UserPortalDTOTemp.getPersona();
  //            if ( personDTO != null ) {
  //              selectedClientDTO = personDTO.getCliente();
  //              if ( selectedClientDTO != null ) {
  //                //Cliente Seleccionado 
  //                addObjectSession( selectedClientDTO, "selectedClientDTO" );
  //              }
  //            }
  //          }
  //        }
  //        
  //      }
  //    } catch ( Exception e ) {
  //      ConstantWEB.AGARTHI_WAR.error( e.getMessage(), e );
  //    }
  //    return event.getNewStep();
  //  }
  //  
  /**
   * </p> Obtiene el list register. </p>
   *
   * @return the list register
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public List< UserPortalDTO > getListRegister () {
    return listRegister;
  }
  
  /**
   * </p> Asigna el list register. </p>
   *
   * @param listRegister the new list register
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setListRegister ( List< UserPortalDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  /**
   * </p> Obtiene el adds the object. </p>
   *
   * @return the adds the object
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public UserPortalDTO getAddObject () {
    return addObject;
  }
  
  /**
   * </p> Asigna el adds the object. </p>
   *
   * @param addObject the new adds the object
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setAddObject ( UserPortalDTO addObject ) {
    this.addObject = addObject;
  }
  
  /**
   * </p> Obtiene el selected object. </p>
   *
   * @return the selected object
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public UserPortalDTO getSelectedObject () {
    return selectedObject;
  }
  
  /**
   * </p> Asigna el selected object. </p>
   *
   * @param selectedObject the new selected object
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setSelectedObject ( UserPortalDTO selectedObject ) {
    this.showAlertSelectRow = true;
    this.selectedObject = selectedObject;
  }
  
  /**
   * </p> Verifica si es show confirm add. </p>
   *
   * @return true, if is show confirm add
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  /**
   * </p> Asigna el show confirm add. </p>
   *
   * @param showConfirmAdd the new show confirm add
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  /**
   * </p> Asigna el show confirm mod. </p>
   *
   * @param showConfirmMod the new show confirm mod
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  /**
   * </p> Verifica si es show confirm mod. </p>
   *
   * @return true, if is show confirm mod
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  /**
   * </p> Verifica si es show alert select row. </p>
   *
   * @return true, if is show alert select row
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  /**
   * </p> Asigna el show alert select row. </p>
   *
   * @param showAlertSelectRow the new show alert select row
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  /**
   * </p> Verifica si es show view detail. </p>
   *
   * @return true, if is show view detail
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  /**
   * </p> Asigna el show view detail. </p>
   *
   * @param showViewDetail the new show view detail
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  /**
   * </p> Asigna el show dialog confirm update. </p>
   *
   * @param showDialogConfirmUpdate the new show dialog confirm update
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  /**
   * </p> Verifica si es show dialog confirm update. </p>
   *
   * @return true, if is show dialog confirm update
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  /**
   * </p> Verifica si es show confirm delete. </p>
   *
   * @return true, if is show confirm delete
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  /**
   * </p> Asigna el show confirm delete. </p>
   *
   * @param showConfirmDelete the new show confirm delete
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
  }
  
  /**
   * </p> Obtiene el person dto. </p>
   *
   * @return the person dto
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public PersonDTO getPersonDTO () {
    personDTO = ( PersonDTO ) getObjectSession( "personDTO" );
    return personDTO;
  }
  
  /**
   * </p> Asigna el person dto. </p>
   *
   * @param personDTO the new person dto
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setPersonDTO ( PersonDTO personDTO ) {
    this.personDTO = personDTO;
  }
  
  /**
   * </p> Obtiene el identity. </p>
   *
   * @return the identity
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public String getIdentity () {
    return identity;
  }
  
  /**
   * </p> Asigna el identity. </p>
   *
   * @param identity the new identity
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setIdentity ( String identity ) {
    this.identity = identity;
  }
  
  /**
   * </p> Verifica si es exists person. </p>
   *
   * @return true, if is exists person
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public boolean isExistsPerson () {
    existsPerson = ( Boolean ) ( getObjectSession( "existsPerson" ) != null ? getObjectSession( "existsPerson" ) : false );
    return existsPerson;
  }
  
  /**
   * </p> Asigna el exists person. </p>
   *
   * @param existsPerson the new exists person
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setExistsPerson ( boolean existsPerson ) {
    this.existsPerson = existsPerson;
  }
  
  /**
   * </p> Obtiene el message. </p>
   *
   * @return the message
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public String getMessage () {
    return message;
  }
  
  /**
   * </p> Asigna el message. </p>
   *
   * @param message the new message
   * @author Andres Herrera - Triangulo Soluciones S.A.S
   * @since AgarthiWeb (28/01/2013)
   */
  public void setMessage ( String message ) {
    this.message = message;
  }
  
  public String getTypeIdentity () {
    return typeIdentity;
  }
  
  public void setTypeIdentity ( String typeIdentity ) {
    this.typeIdentity = typeIdentity;
  }
  
  //  @SuppressWarnings ( "unchecked" )
  //  public List< TipoIdentificacionDTO > getListTypeIdentityDTO () {
  //    listTypeIdentityDTO = ( List< TipoIdentificacionDTO > ) getObjectSession( "listTypeIdentityDTO" );
  //    return listTypeIdentityDTO;
  //  }
  //  
  //  public void setListTypeIdentityDTO ( List< TipoIdentificacionDTO > listTypeIdentityDTO ) {
  //    this.listTypeIdentityDTO = listTypeIdentityDTO;
  //  }
  
  public boolean isSkip () {
    return skip;
  }
  
  public void setSkip ( boolean skip ) {
    this.skip = skip;
  }
  
  public List< ClientDTO > getListClientDTO () {
    return listClientDTO;
  }
  
  public void setListClientDTO ( List< ClientDTO > listClientDTO ) {
    this.listClientDTO = listClientDTO;
  }
  
  public ClientDTO getSelectedClientDTO () {
    selectedClientDTO = ( ClientDTO ) getObjectSession( "selectedClientDTO" );
    return selectedClientDTO;
  }
  
  public void setSelectedClientDTO ( ClientDTO selectedClientDTO ) {
    this.selectedClientDTO = selectedClientDTO;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< PersonDTO > getListPersonDTO () {
    listPersonDTO = ( List< PersonDTO > ) getObjectSession( "listPersonDTO" );
    return listPersonDTO;
  }
  
  public void setListPersonDTO ( List< PersonDTO > listPersonDTO ) {
    this.listPersonDTO = listPersonDTO;
  }
  
  public List< SelectItem > getListRolItem () {
    return listRolItem;
  }
  
  public void setListRolItem ( List< SelectItem > listRolItem ) {
    this.listRolItem = listRolItem;
  }
  
  public List< String > getSelectedOptionsRol () {
    return selectedOptionsRol;
  }
  
  public void setSelectedOptionsRol ( List< String > selectedOptionsRol ) {
    this.selectedOptionsRol = selectedOptionsRol;
  }
  
  @Override
  public void initElement () {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String remove () {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String update () {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String add () {
    // TODO Auto-generated method stub
    return null;
  }
  
}
