package co.com.cetus.cetuscontrol.web.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;

import co.com.cetus.cetuscontrol.dto.ClientDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.cetuscontrol.web.util.StatusUser;
import co.com.cetus.cetuscontrol.web.util.UtilWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.portal.ejb.service.FindRolsByApplicationResponseDTO;
import co.com.cetus.portal.ejb.service.FindRolsByLoginResponseDTO;
import co.com.cetus.portal.ejb.service.RolDTO;

@ManagedBean
@RequestScoped
public class PersonAdminMBean extends GeneralManagedBean {
  
  private static final long  serialVersionUID        = -3255236201554716426L;
  private List< PersonDTO >  listRegister            = null;
  private PersonDTO          addObject               = null;
  private PersonDTO          selectedObject          = null;
  private boolean            showConfirmAdd          = false;
  private boolean            showConfirmMod          = false;
  private boolean            showDialogConfirmUpdate = false;
  private boolean            showConfirmDelete       = false;
  private boolean            showAlertSelectRow      = false;
  private boolean            showViewDetail          = false;
  private boolean            showViewFielDateFinish  = false;
  private boolean            isGraduate              = false;
  private List< ClientDTO >  listClientDTO           = null;
  private ClientDTO          selectedClientDTO       = null;
  private boolean            clientSelected          = true;
  private boolean            personSelected          = true;
  private int                indexTab                = 0;
  private int                indexTabUpdate          = 0;
  private boolean            statusSession           = false;
  private List< RolDTO >     listRol;
  private List< SelectItem > listRolItem;
  private List< String >     selectedOptionsRol      = null;
  private UserPortalDTO      addObjectUserPortal     = null;
  private String             loginOld                = null;
  private UserPortalDTO      usuarioDTO              = null;
  private boolean            status;
  
  public PersonAdminMBean () {
    addObject = ( PersonDTO ) getObjectSession( "addObject" );
    if ( addObject == null && getObjectSession( "addObject" ) == null ) {
      addObject = new PersonDTO();
    }
    selectedObject = new PersonDTO();
    addObjectUserPortal = new UserPortalDTO();
  }
  
  @Override
  @PostConstruct
  public void initElement () {
    if ( getUserDTO() != null ) {
      this.listRegister();
      this.listClient();
      this.listRolByApp();
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  @Override
  public String remove () {
    ResponseDTO response = null;
    List< UserPortalDTO > listUserPortalDTO = null;
    boolean error = false;
    try {
      this.showConfirmDelete = false;
      selectedObject = ( PersonDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null && selectedObject.getMaster() != 1 ) {
        
        //Consultar usuarios portal por persona.
        response = generalDelegate.findUserByPerson( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          if ( response.getObjectResponse() != null ) {
            listUserPortalDTO = ( List< UserPortalDTO > ) response.getObjectResponse();
          }
        }
        
        if ( listUserPortalDTO != null ) {
          for ( UserPortalDTO userPortalDTO: listUserPortalDTO ) {
            response = this.generalDelegate.deleteUserCetus( userPortalDTO );
            if ( response != null && ( UtilCommon.validateResponseSuccess( response ) || UtilCommon.validateResponseSuccessWithWarning( response ) ) ) {
              ConstantWEB.WEB_LOG.debug( "######### ELIMINADO EL USUARIO EN CETUS VORTAL :: " + userPortalDTO.getLoginCetus() );
              response = generalDelegate.remove( userPortalDTO );
              if ( UtilCommon.validateResponseSuccess( response ) ) {
                ConstantWEB.WEB_LOG.debug( "######### ELIMINADO EL USUARIO :: " + userPortalDTO.getLoginCetus() );
              } else {
                ConstantWEB.WEB_LOG.debug( "######### NO SE PUDO ELIMINAR EL USUARIO  :: " + userPortalDTO.getLoginCetus() );
                error = true;
                break;
              }
            } else {
              ConstantWEB.WEB_LOG.debug( "######### NO SE PUDO ELIMINAR EL USUARIO EN CETUS VORTAL :: " + userPortalDTO.getLoginCetus() );
              error = true;
              break;
            }
          }
        }
        
        if ( !error ) {
          selectedObject.setUserPortals( null );
          response = generalDelegate.remove( selectedObject );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            ConstantWEB.WEB_LOG.debug( "########## PERSONA ELIMINADA  " + selectedObject.getIdentity() );
            this.initElement();
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
          } else {
            ConstantWEB.WEB_LOG.debug( "########## ERROR ELIMINADO EL REGISTRO  " + selectedObject.getIdentity() + " Detalle: "
                                       + response.getMessage() );
            addMessageError( null, ConstantWEB.MESSAGE_ERROR, response.getMessage() );
          }
        } else {
          ConstantWEB.WEB_LOG.debug( "########## ERROR ELIMINADO LOS USUARIOS ASOCIADOS PARA EL RESPONSABLE " + selectedObject.getIdentity()
                                     + " Detalle: "
                                     + response.getMessage() );
          addMessageError( null, ConstantWEB.MESSAGE_ERROR,
                           "No se pudo eliminar el usuario asociado al responsable. Detalle: " + response.getMessage() );
        }
        
      } else {
        ConstantWEB.WEB_LOG.debug( "########## NO ES POSIBLE ELIMINAR EL RESPONSABLE " + selectedObject.getIdentity() );
        addMessageError( null, ConstantWEB.MESSAGE_ERROR, "No es posible Eliminar el Responsable Master" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
  }
  
  @SuppressWarnings ( "unchecked" )
  @Override
  public String update () {
    boolean lSuccessfull = false;
    RequestContext context = null;
    ResponseDTO response = null;
    try {
      this.showConfirmMod = false;
      selectedObject = ( PersonDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        context = RequestContext.getCurrentInstance();
        selectedObject.setCreationUser( selectedObject.getCreationUser() );
        selectedObject.setCreationDate( selectedObject.getCreationDate() );
        selectedObject.setMaster( BigDecimal.ZERO.intValue() );
        selectedObject.setModificationUser( getUserInSession() );
        selectedObject.setModificationDate( new Date() );
        selectedClientDTO = ( ClientDTO ) getObjectSession( "selectedClientDTO" );
        selectedObject.setClient( selectedClientDTO );
        selectedOptionsRol = ( List< String > ) getObjectSession( "selectedOptionsRol" );
        addObjectUserPortal = ( UserPortalDTO ) getObjectSession( "addObjectUserPortal" );
        addObjectUserPortal.setPerson( selectedObject );
        addObjectUserPortal.setLoginOld( ( String ) getObjectSession( "loginOld" ) );
        response = generalDelegate.updateUserCetus( addObjectUserPortal, selectedOptionsRol );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          //Actualizado correctamente en cetus vortal
          if ( addObjectUserPortal != null && addObjectUserPortal.getId() > 0 ) {
            response = generalDelegate.edit( addObjectUserPortal );
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              //Actualizado correctamente en Cetus control el usuario
              response = generalDelegate.edit( selectedObject );
              if ( UtilCommon.validateResponseSuccess( response ) ) {
                this.initElement();
                lSuccessfull = true;
                usuarioDTO = addObjectUserPortal;
                status = usuarioDTO.getStatus() == 1 ? true : false;
                addObjectSession( status, "status" );
                ConstantWEB.WEB_LOG.debug( "######### REGISTRO ACTUALIZADO CORRECTAMENTE " + selectedObject.getIdentity() );
                addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
                cleanObjectSession( "selectedClientDTO" );
                cleanObjectSession( "indexTab" );
                cleanObjectSession( "indexTabUpdate" );
                cleanObjectSession( "clientSelected" );
                cleanObjectSession( "addObjectUserPortal" );
                cleanObjectSession( "selectedOptionsRol" );
              } else {
                //Obetener codigo de error de oracle
                String code = "0";
                if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
                  code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
                  if ( code != null && !code.isEmpty() ) {
                    code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
                    addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
                  }
                } else {
                  addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
                }
                ConstantWEB.WEB_LOG.debug( "######### ERROR ACTUALIZANDO EL REGISTRO" + selectedObject.getIdentity() );
              }
            } else {
              //Obetener codigo de error de oracle
              String code = "0";
              if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
                code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
                if ( code != null && !code.isEmpty() ) {
                  code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
                  addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
                }
              } else {
                addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
              }
              ConstantWEB.WEB_LOG.debug( "######### ERROR ACTUALIZANDO EL REGISTRO DE USUARIO PORTAL " + selectedObject.getIdentity() );
            }
          }
          
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, response.getMessage() );
          ConstantWEB.WEB_LOG.debug( "######### ERROR ACTUALIZANDO EL REGISTRO EN CETUS VORTAL" + selectedObject.getIdentity() );
        }
        
        context.addCallbackParam( "lSuccessfull", lSuccessfull );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    
    return null;
  }
  
  @SuppressWarnings ( "unchecked" )
  @Override
  public String add () {
    boolean lSuccessfull = false;
    RequestContext context = null;
    ResponseDTO response = null;
    PersonDTO personDTO = null;
    UserPortalDTO userPortalDTO = null;
    String codePerson = null;
    try {
      ConstantWEB.WEB_LOG.debug( "######### INICIAR CREAR PERSONA " );
      this.showConfirmAdd = false;
      addObject = ( PersonDTO ) getObjectSession( "addObject" );
      addObject.setId( 0 );
      addObjectUserPortal = ( UserPortalDTO ) getObjectSession( "addObjectUserPortal" );
      selectedClientDTO = ( ClientDTO ) getObjectSession( "selectedClientDTO" );
      selectedOptionsRol = ( List< String > ) getObjectSession( "selectedOptionsRol" );
      if ( addObject != null && addObjectUserPortal != null ) {
        ConstantWEB.WEB_LOG.debug( "######### OBJETO PERSONA OK " );
        context = RequestContext.getCurrentInstance();
        response = generalDelegate.generateCodePerson();
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          codePerson = ( String ) response.getObjectResponse();
          ConstantWEB.WEB_LOG.info( "########## CODIGO CLIENTE GENERADO :: " + codePerson );
          if ( codePerson != null ) {
            addObject.setCode( codePerson );
          }
          addObject.setCreationUser( getUserInSession() );
          addObject.setCreationDate( new Date() );
          addObject.setClient( new ClientDTO() );
          addObject.setMaster( BigDecimal.ZERO.intValue() );
          addObject.getClient().setId( selectedClientDTO.getId() );
          addObjectSession( addObject, "addObject" );
          //Crear persona en Cetus Control
          response = generalDelegate.create( addObject );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            personDTO = ( PersonDTO ) response.getObjectResponse();
            if ( personDTO != null && personDTO.getId() > 0 ) {
              ConstantWEB.WEB_LOG.debug( "######### PERSONA OK " );
              addObjectUserPortal.setCreationUser( getUserInSession() );
              addObjectUserPortal.setCreationDate( new Date() );
              addObjectUserPortal.setStatus( StatusUser.ACTIVE.getValueNum() );
              addObjectUserPortal.setPerson( personDTO );
              //Crear usuario Portal en Cetus Control
              response = generalDelegate.create( addObjectUserPortal );
              userPortalDTO = ( UserPortalDTO ) response.getObjectResponse();
              if ( UtilCommon.validateResponseSuccess( response ) ) {
                ConstantWEB.WEB_LOG.debug( "######### USUARIO PORTAL OK " );
                //crear usuario en cetus
                response = generalDelegate.createUserCetus( ( UserPortalDTO ) response.getObjectResponse(), selectedOptionsRol );
                if ( UtilCommon.validateResponseSuccess( response ) ) {
                  ConstantWEB.WEB_LOG.debug( "######### USUARIO CETUS OK " );
                  this.initElement();
                  lSuccessfull = true;
                  addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
                  ConstantWEB.WEB_LOG.debug( "######### REGISTRO CREADO EXITOSAMENTE " + selectedObject.getIdentity() );
                  cleanObjectSession( "addObject" );
                  cleanObjectSession( "selectedClientDTO" );
                  cleanObjectSession( "indexTab" );
                  cleanObjectSession( "indexTabUpdate" );
                  cleanObjectSession( "clientSelected" );
                  cleanObjectSession( "addObjectUserPortal" );
                  cleanObjectSession( "selectedOptionsRol" );
                } else {
                  String code = "0";
                  if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
                    code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
                    if ( code != null && !code.isEmpty() ) {
                      code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
                      addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
                    }
                  } else {
                    addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
                  }
                  response = generalDelegate.remove( userPortalDTO );
                  ConstantWEB.WEB_LOG.debug( "######### REMOVIDO USUARIO PORTAL OK " );
                  response = generalDelegate.remove( personDTO );
                  ConstantWEB.WEB_LOG.debug( "######### REMOVIDO PERSONA OK " );
                  ConstantWEB.WEB_LOG.debug( "########## SE HIZO ROOLBACK A LA PERSONA PORQUE FALLO LA CREACION DEL USUARIO EN CETUS" );
                }
              } else {
                
                //Obetener codigo de error de oracle
                String code = "0";
                if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
                  code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
                  if ( code != null && !code.isEmpty() ) {
                    code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
                    addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
                  }
                } else {
                  addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
                }
                response = generalDelegate.remove( personDTO );
                ConstantWEB.WEB_LOG.debug( "######### USUARIO PORTAL KO " + response.getMessage() );
                ConstantWEB.WEB_LOG.debug( "######### REMOVIDO PERSONA OK " );
                ConstantWEB.WEB_LOG.debug( "########## SE HIZO ROOLBACK A LA PERSONA PORQUE FALLO LA CREACION DEL USUARIO PORTAL" );
              }
            }
          } else {
            ConstantWEB.WEB_LOG.debug( "######### CREANDO PERSONA KO " + response.getMessage() );
            //Obetener codigo de error de oracle
            String code = "0";
            if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
              code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
              if ( code != null && !code.isEmpty() ) {
                code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
                addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
              }
            } else {
              addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
            }
          }
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR + " No Es posible generar el Código del Cliente." );
        }
        context.addCallbackParam( "lSuccessfull", lSuccessfull );
      }
    } catch ( Exception e ) {
      addObjectSession( addObject, "addObject" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listClient () {
    ResponseDTO response = null;
    try {
      ConstantWEB.WEB_LOG.debug( "######### ID DEL CLIENTE CETUS PARA EL USUARIO LOGUEADO "
                                 + getUserDTO().getPerson().getClient().getClientCetus().getId() );
      response = generalDelegate.findClientByClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listClientDTO = ( List< ClientDTO > ) response.getObjectResponse();
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listClientDTO = new ArrayList< ClientDTO >();
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
  }
  
  public void load ( ActionEvent event ) {
    try {
      selectedClientDTO = ( ClientDTO ) getObjectSession( "selectedClientDTO" );
      if ( selectedClientDTO != null ) {
        if ( addObject != null && addObjectUserPortal != null && selectedOptionsRol != null ) {
          addObjectSession( addObject, "addObject" );
          addObjectSession( addObjectUserPortal, "addObjectUserPortal" );
          addObjectSession( selectedOptionsRol, "selectedOptionsRol" );
        }
        this.showConfirmAdd = true;
      } else {
        addMessageWarning( "btnAdd", ConstantWEB.MESSAGE_WARNING, ConstantWEB.MSG_WARNING_CLIENT_NOT_EMPTY );
        this.showConfirmAdd = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
  }
  
  public void loadUpdate ( ActionEvent event ) {
    PersonDTO personTemp = null;
    UserPortalDTO temp = null;
    try {
      if ( selectedObject != null ) {
        selectedClientDTO = ( ClientDTO ) getObjectSession( "selectedClientDTO" );
        if ( selectedClientDTO != null ) {
          if ( selectedObject != null && addObjectUserPortal != null && selectedOptionsRol != null ) {
            temp = ( UserPortalDTO ) getObjectSession( "addObjectUserPortal" );
            loginOld = temp != null ? temp.getLoginCetus() : null;
            addObjectSession( loginOld, "loginOld" );
            addObjectUserPortal.setStatus( status ? BigDecimal.ONE.intValue() : BigDecimal.ZERO.intValue() );
            addObjectUserPortal.setCreationDate( temp != null ? temp.getCreationDate() : new Date() );
            addObjectUserPortal.setCreationUser( temp != null ? temp.getCreationUser() : getUserInSession() );
            personTemp = ( PersonDTO ) getObjectSession( "selectedObject" );
            selectedObject.setCreationDate( personTemp.getCreationDate() );
            selectedObject.setCreationUser( personTemp.getCreationUser() );
            cleanObjectSession( "selectedObject" );
            addObjectSession( selectedObject, "selectedObject" );
            addObjectSession( addObjectUserPortal, "addObjectUserPortal" );
            addObjectSession( selectedOptionsRol, "selectedOptionsRol" );
          }
        }
        //addObjectSession( selectedObject, "selectedObject" );
        this.showDialogConfirmUpdate = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listRegister () {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findPersonByClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listRegister = ( List< PersonDTO > ) response.getObjectResponse();
      } else {
        this.listRegister = new ArrayList< PersonDTO >();
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  public void validateSelectedRecord ( ActionEvent event ) {
    ResponseDTO response = null;
    try {
      if ( selectedObject != null ) {
        response = generalDelegate.findUserByPerson( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          //Tiene Usuario portal la persona seleecionada 
          usuarioDTO = ( ( List< UserPortalDTO > ) response.getObjectResponse() ).get( 0 );
          status = usuarioDTO.getStatus() == 1 ? true : false;
          addObjectSession( status, "status" );
        } else {
          usuarioDTO = null;
        }
        addObjectSession( usuarioDTO, "usuarioDTO" );
        addObjectSession( selectedObject, "selectedObject" );
        this.showAlertSelectRow = false;
        this.showViewDetail = true;
        this.showConfirmMod = true;
        this.showConfirmDelete = true;
      } else {
        this.showAlertSelectRow = true;
        this.showViewDetail = false;
        this.showConfirmMod = false;
        this.showConfirmDelete = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
  }
  
  public void nextTabDataPerson ( String pIdComponent ) {
    try {
      if ( selectedClientDTO != null ) {
        addObjectSession( selectedClientDTO, "selectedClientDTO" );
        indexTab = 1;
        clientSelected = false;
        addObjectSession( clientSelected, "clientSelected" );
      } else {
        clientSelected = true;
        addObjectSession( clientSelected, "clientSelected" );
        indexTab = 0;
        addMessageWarning( pIdComponent, ConstantWEB.MESSAGE_WARNING, "Debe Seleccionar un Cliente" );
      }
      addObjectSession( indexTab, "indexTab" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    
  }
  
  public void nextTabUser () {
    try {
      indexTab = 1;
      if ( addObject != null ) {
        cleanObjectSession( "addObjectUserPortal" );
        indexTab = 2;
        addObjectSession( addObject, "addObject" );
        personSelected = false;
        addObjectSession( personSelected, "personSelected" );
      }
      addObjectSession( indexTab, "indexTab" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    
  }
  
  public void nextTabDataPersonUpdate ( String pIdComponent ) {
    try {
      if ( selectedClientDTO != null ) {
        addObjectSession( selectedClientDTO, "selectedClientDTO" );
        indexTabUpdate = 1;
        clientSelected = false;
        addObjectSession( clientSelected, "clientSelected" );
        selectedObject = ( PersonDTO ) getObjectSession( "selectedObject" );
      } else {
        clientSelected = true;
        addObjectSession( clientSelected, "clientSelected" );
        indexTabUpdate = 0;
        addMessageWarning( pIdComponent, ConstantWEB.MESSAGE_WARNING, "Debe Seleccionar un Cliente" );
      }
      addObjectSession( indexTabUpdate, "indexTabUpdate" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public void nextTabUserUpdate () {
    ResponseDTO response = null;
    FindRolsByLoginResponseDTO responseCetus = null;
    List< RolDTO > list = null;
    List< UserPortalDTO > listUser = null;
    PersonDTO personTemp = null;
    try {
      indexTabUpdate = 1;
      if ( selectedObject != null ) {
        personTemp = ( PersonDTO ) getObjectSession( "selectedObject" );
        response = generalDelegate.findUserByPerson( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          listUser = ( List< UserPortalDTO > ) response.getObjectResponse();
          if ( listUser != null && !listUser.isEmpty() ) {
            addObjectUserPortal = listUser.get( 0 );
            if ( addObjectUserPortal != null ) {
              addObjectSession( addObjectUserPortal, "addObjectUserPortal" );
              response = generalDelegate.findRolByLogin( addObjectUserPortal.getLoginCetus() );
              if ( UtilCommon.validateResponseSuccess( response ) ) {
                responseCetus = ( FindRolsByLoginResponseDTO ) response.getObjectResponse();
                if ( responseCetus != null ) {
                  list = responseCetus.getRol();
                  selectedOptionsRol = new ArrayList< String >();
                  for ( RolDTO rolDTO: list ) {
                    selectedOptionsRol.add( String.valueOf( rolDTO.getId() ) );
                  }
                }
              }
              
            }
          }
        }
        indexTabUpdate = 2;
        selectedObject.setCreationDate( personTemp.getCreationDate() );
        selectedObject.setCreationUser( personTemp.getCreationUser() );
        addObjectSession( selectedOptionsRol, "selectedOptionsRol" );
        //addObjectSession( selectedObject, "selectedObject" );
        personSelected = false;
        addObjectSession( personSelected, "personSelected" );
      }
      addObjectSession( indexTabUpdate, "indexTabUpdate" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public void onRowSelect ( SelectEvent event ) {
    ResponseDTO response = null;
    try {
      //FacesMessage msg = new FacesMessage( "Car Selected", ( ( PersonDTO ) event.getObject() ).getLastNames() );
      selectedClientDTO = ( ( PersonDTO ) event.getObject() ).getClient();
      selectedObject = ( ( PersonDTO ) event.getObject() );
      if ( selectedObject != null ) {
        response = generalDelegate.findUserByPerson( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          //Tiene Usuario portal la persona seleecionada 
          usuarioDTO = ( ( List< UserPortalDTO > ) response.getObjectResponse() ).get( 0 );
          status = usuarioDTO.getStatus() == 1 ? true : false;
          addObjectSession( status, "status" );
        } else {
          usuarioDTO = null;
        }
        addObjectSession( usuarioDTO, "usuarioDTO" );
      }
      addObjectSession( selectedObject, "selectedObject" );
      addObjectSession( selectedClientDTO, "selectedClientDTO" );
      //FacesContext.getCurrentInstance().addMessage( null, msg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  public void onRowUnselect ( UnselectEvent event ) {
    cleanObjectSession( "selectedClientDTO" );
  }
  
  public void enabledFindDateFinish () {
    this.showViewFielDateFinish = isGraduate;
  }
  
  public void onTabChange ( TabChangeEvent event ) {
    if ( selectedClientDTO != null ) {
      clientSelected = true;
    }
    addObjectSession( clientSelected, "clientSelected" );
  }
  
  public List< PersonDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< PersonDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public PersonDTO getAddObject () {
    return addObject;
  }
  
  public void setAddObject ( PersonDTO addObject ) {
    this.addObject = addObject;
  }
  
  public PersonDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( PersonDTO selectedObject ) {
    this.showAlertSelectRow = true;
    this.selectedObject = selectedObject;
  }
  
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
  }
  
  public boolean isShowViewFielDateFinish () {
    return showViewFielDateFinish;
  }
  
  public void setShowViewFielDateFinish ( boolean showViewFielDateFinish ) {
    this.showViewFielDateFinish = showViewFielDateFinish;
  }
  
  public boolean isGraduate () {
    return isGraduate;
  }
  
  public void setGraduate ( boolean isGraduate ) {
    this.isGraduate = isGraduate;
  }
  
  public ClientDTO getSelectedClientDTO () {
    selectedClientDTO = ( ClientDTO ) getObjectSession( "selectedClientDTO" );
    return selectedClientDTO;
  }
  
  public void setSelectedClientDTO ( ClientDTO selectedClientDTO ) {
    this.selectedClientDTO = selectedClientDTO;
  }
  
  public List< ClientDTO > getListClientDTO () {
    return listClientDTO;
  }
  
  public void setListClientDTO ( List< ClientDTO > listClientDTO ) {
    this.listClientDTO = listClientDTO;
  }
  
  public boolean isClientSelected () {
    clientSelected = ( Boolean ) ( getObjectSession( "clientSelected" ) != null ? getObjectSession( "clientSelected" ) : true );
    return clientSelected;
  }
  
  public void setClientSelected ( boolean clientSelected ) {
    this.clientSelected = clientSelected;
  }
  
  public int getIndexTab () {
    indexTab = ( Integer ) ( getObjectSession( "indexTab" ) != null ? getObjectSession( "indexTab" ) : 0 );
    return indexTab;
  }
  
  public void setIndexTab ( int indexTab ) {
    this.indexTab = indexTab;
  }
  
  public boolean isStatusSession () {
    statusSession = ( Boolean ) ( getObjectSession( "statusSession" ) != null ? getObjectSession( "statusSession" ) : false );
    return statusSession;
  }
  
  public void setStatusSession ( boolean statusSession ) {
    this.statusSession = statusSession;
  }
  
  public void clearObject () {
    cleanObjectSession( "selectedObject" );
    cleanObjectSession( "personDTO" );
    cleanObjectSession( "selectedClientDTO" );
    cleanObjectSession( "addObject" );
    cleanObjectSession( "selectedOptionsRol" );
    cleanObjectSession( "addObjectUserPortal" );
    cleanObjectSession( "indexTab" );
    cleanObjectSession( "indexTabUpdate" );
    cleanObjectSession( "personSelected" );
    cleanObjectSession( "clientSelected" );
    cleanObjectSession( "addObject" );
    addObjectUserPortal = new UserPortalDTO();
    addObject = new PersonDTO();
  }
  
  private void listRolByApp () {
    ResponseDTO response = null;
    int idApp;
    try {
      if ( getObjectSession( ConstantWEB.DESC_APP ) != null ) {
        idApp = Integer.parseInt( ( String ) getObjectSession( ConstantWEB.DESC_APP ) );
        System.out.println( "ID DE LA APLICACION --- " + idApp );
        response = generalDelegate.findRolByApplication( idApp );
        if ( response != null && UtilCommon.validateResponseSuccess( response ) && response.getObjectResponse() != null ) {
          listRol = ( ( FindRolsByApplicationResponseDTO ) response.getObjectResponse() ).getRol();
          if ( listRol != null ) {
            listRolItem = new ArrayList< SelectItem >();
            for ( RolDTO item: listRol ) {
              listRolItem.add( new SelectItem( item.getId(), item.getDescripcion() ) );
            }
          }
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, response != null && response.getMessage() != null ? response.getMessage()
                                                                                                             : ConstantWEB.MESSAGE_ERROR_CREATE );
        }
      }
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public List< SelectItem > getListRolItem () {
    return listRolItem;
  }
  
  public void setListRolItem ( List< SelectItem > listRolItem ) {
    this.listRolItem = listRolItem;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< String > getSelectedOptionsRol () {
    selectedOptionsRol = ( List< String > ) ( getObjectSession( "selectedOptionsRol" ) != null ? getObjectSession( "selectedOptionsRol" ) : null );
    return selectedOptionsRol;
  }
  
  public void setSelectedOptionsRol ( List< String > selectedOptionsRol ) {
    this.selectedOptionsRol = selectedOptionsRol;
  }
  
  public UserPortalDTO getAddObjectUserPortal () {
    return addObjectUserPortal;
  }
  
  public void setAddObjectUserPortal ( UserPortalDTO addObjectUserPortal ) {
    this.addObjectUserPortal = addObjectUserPortal;
  }
  
  public boolean isPersonSelected () {
    personSelected = ( Boolean ) ( getObjectSession( "personSelected" ) != null ? getObjectSession( "personSelected" ) : true );
    return personSelected;
  }
  
  public void setPersonSelected ( boolean personSelected ) {
    this.personSelected = personSelected;
  }
  
  public int getIndexTabUpdate () {
    indexTabUpdate = ( Integer ) ( getObjectSession( "indexTabUpdate" ) != null ? getObjectSession( "indexTabUpdate" ) : 0 );
    return indexTabUpdate;
  }
  
  public void setIndexTabUpdate ( int indexTabUpdate ) {
    this.indexTabUpdate = indexTabUpdate;
  }
  
  public boolean isStatus () {
    status = ( Boolean ) ( getObjectSession( "status" ) != null ? getObjectSession( "status" ) : false );
    return status;
  }
  
  public void setStatus ( boolean status ) {
    this.status = status;
  }
  
  public String getLoginOld () {
    return loginOld;
  }
  
  public void setLoginOld ( String loginOld ) {
    this.loginOld = loginOld;
  }
  
  public UserPortalDTO getUsuarioDTO () {
    usuarioDTO = ( UserPortalDTO ) ( getObjectSession( "usuarioDTO" ) != null ? getObjectSession( "usuarioDTO" ) : null );
    return usuarioDTO;
  }
  
  public void setUsuarioDTO ( UserPortalDTO usuarioDTO ) {
    this.usuarioDTO = usuarioDTO;
  }
  
}
