package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.event.SelectEvent;

import co.com.cetus.cetuscontrol.dto.GroupTDTO;
import co.com.cetus.cetuscontrol.dto.NotificationGeneralDTO;
import co.com.cetus.cetuscontrol.dto.NotificationSettingDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

@ManagedBean
@RequestScoped
public class NotificationMBean extends GeneralManagedBean {
  
  private static final long              serialVersionUID        = 1L;
                                                                 
  /** The selected object. */
  private int                            idGroup                 = 0;
                                                                 
  private List< SelectItem >             listGroup               = null;
                                                                 
  private List< NotificationSettingDTO > listRegister            = null;
                                                                 
  private NotificationSettingDTO         selectedObject          = null;
                                                                 
  private boolean                        showViewDetail          = false;
  private boolean                        showAlertSelectRow      = false;
  private boolean                        showConfirmMod          = false;
  private boolean                        showConfirmClone        = false;
  private boolean                        activate                = false;
  private String                         selectEmail             = null;
  private List< PersonDTO >              listEmail               = null;
  private boolean                        showDialogConfirmUpdate = false;
  private List< GroupTDTO >              listGroupClone          = null;
  private boolean                        showAlertSelectRow2     = false;
  private List< GroupTDTO >              selectedGroupClone      = null;
  private boolean                        showDialogConfirmClone  = false;
                                                                 
  public NotificationMBean () {
    
    selectedObject = new NotificationSettingDTO();
    selectedObject.setNotificationGeneral( new NotificationGeneralDTO() );
    selectedObject.setGroup( new GroupTDTO() );
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @SuppressWarnings ( "unchecked" )
  @Override
  @PostConstruct
  public void initElement () {
    if ( getUserDTO() != null ) {
      loadGroup();
      listRegister = ( List< NotificationSettingDTO > ) getObjectSession( "listRegister" );
      if ( listRegister == null ) {
        listRegister = new ArrayList< NotificationSettingDTO >();
      }
      
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void loadGroup () {
    ResponseDTO response = null;
    List< GroupTDTO > list = new ArrayList< GroupTDTO >();
    try {
      response = generalDelegate.findGroupByPerson( getUserDTO().getPerson().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        list = ( List< GroupTDTO > ) response.getObjectResponse();
        if ( list != null && !list.isEmpty() ) {
          this.listGroup = new ArrayList< SelectItem >();
          this.listGroupClone = new ArrayList< GroupTDTO >();
          for ( GroupTDTO objDTO: list ) {
            this.listGroup.add( new SelectItem( objDTO.getId(), objDTO.getName() ) );
            this.listGroupClone.add( objDTO );
          }
        }
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listGroup = new ArrayList< SelectItem >();
        }
      }
      
      addObjectSession( list, "listGroupT" );
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void changeGroup () {
    try {
      addObjectSession( idGroup, "idGroup" );
      if ( idGroup > 0 ) {
        listNotifications( idGroup );
      }else{
        cleanObjectSession( "listRegister" );
        this.listRegister = new ArrayList< NotificationSettingDTO >();        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> List notifications. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idGroup the id group
   * @since CetusControlWEB (21/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void listNotifications ( int idGroup ) {
    ResponseDTO response = null;
    List< NotificationGeneralDTO > list = null;
    NotificationSettingDTO notificationSettingDTO = null;
    List< NotificationSettingDTO > listNS = null;
    List< GroupTDTO > listGroup = null;
    try {
      
      response = generalDelegate.findNotificationGenClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio
        list = ( List< NotificationGeneralDTO > ) response.getObjectResponse();
        if ( list != null && !list.isEmpty() ) {
          listGroup = ( List< GroupTDTO > ) getObjectSession( "listGroupT" );
          
          response = generalDelegate.findNotificationByGroup( idGroup );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            listNS = ( List< NotificationSettingDTO > ) response.getObjectResponse();
          } else {
            listNS = new ArrayList< NotificationSettingDTO >();
          }
          
          this.listRegister = new ArrayList< NotificationSettingDTO >();
          forNgeneral: for ( NotificationGeneralDTO notificationGeneralDTO: list ) {
            int i = 0;
            while ( listNS.size() > 0 && i < listNS.size() ) {
              NotificationSettingDTO nSettingDTO = listNS.get( i );
              if ( notificationGeneralDTO.getId() == nSettingDTO.getNotificationGeneral().getId() ) {
                this.listRegister.add( nSettingDTO );
                listNS.remove( i );
                continue forNgeneral;
              }
              i++;
            }
            
            notificationSettingDTO = new NotificationSettingDTO();
            notificationSettingDTO.setNotificationGeneral( notificationGeneralDTO );
            if ( notificationGeneralDTO.isMandatory() && notificationGeneralDTO.isDefault() ) {
              notificationSettingDTO.setEmails( ConstantWEB.RESPONSIBLE_TASK );
              
              if ( listGroup != null ) {
                for ( GroupTDTO groupTDTO: listGroup ) {
                  if ( groupTDTO.getId() == idGroup ) {
                    notificationSettingDTO.setGroup( groupTDTO );
                    break;
                  }
                }
              }
            }
            this.listRegister.add( notificationSettingDTO );
          }
        }
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< NotificationSettingDTO >();
        }
      }
      addObjectSession( listRegister, "listRegister" );
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Validate selected record. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (21/02/2016)
   */
  public void validateSelectedRecord ( ActionEvent event ) {
    try {
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        this.showAlertSelectRow = false;
        this.showConfirmMod = true;
        if ( selectedObject.getId() > 0
             || ( selectedObject.getNotificationGeneral().isMandatory() && selectedObject.getNotificationGeneral().isDefault() ) ) {
          this.activate = true;
        }
        
        cleanObject();
        loadEmailPerson( selectedObject.getEmails() );
      } else {
        this.showAlertSelectRow = true;
        this.showConfirmMod = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Complete text. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param query the query
   * @return el list
   * @since CetusControlWEB (24/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public List< String > completeText ( String query ) {
    List< String > results = new ArrayList< String >();
    ArrayList< PersonDTO > listPerson = null;
    ResponseDTO response = null;
    StringBuilder option = null;
    PersonDTO personDTO = null;
    ArrayList< PersonDTO > listPersonClone = null;
    try {
      listPerson = ( ArrayList< PersonDTO > ) getObjectSession( "listPerson" );
      if ( listPerson == null ) {
        response = generalDelegate.findPersonByClient( getUserDTO().getPerson().getClient().getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          listPerson = ( ArrayList< PersonDTO > ) response.getObjectResponse();
          addObjectSession( listPerson, "listPerson" );
        }
      }
      
      if ( listPerson != null && listPerson.size() > 0 ) {
        
        listEmail = ( List< PersonDTO > ) getObjectSession( "listEmail" );
        if ( listEmail == null ) {
          listEmail = new ArrayList< PersonDTO >();
        }
        int i = 0;
        listPersonClone = ( ArrayList< PersonDTO > ) listPerson.clone();
        whilePerson: while ( listPersonClone != null && listPersonClone.size() > 0 && listPersonClone.size() > i ) {
          personDTO = listPersonClone.get( i );
          for ( PersonDTO email: listEmail ) {
            if ( personDTO.getEmail().equals( email.getEmail() ) ) {
              listPersonClone.remove( i );
              continue whilePerson;
            }
          }
          i++;
          option = new StringBuilder();
          option.append( personDTO.getEmail() + "::(" );
          option.append( personDTO.getNames() + " " + personDTO.getLastNames() + ")" );
          if ( option.toString().toLowerCase().contains( query.toLowerCase() ) ) {
            results.add( option.toString() );
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return results;
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public void onItemSelect ( SelectEvent event ) {
    List< PersonDTO > listPerson = null;
    String email = null;
    try {
      listEmail = ( List< PersonDTO > ) getObjectSession( "listEmail" );
      if ( listEmail == null ) {
        listEmail = new ArrayList< PersonDTO >();
      }
      
      listPerson = ( List< PersonDTO > ) getObjectSession( "listPerson" );
      if ( listPerson != null ) {
        email = event.getObject().toString().split( "::" )[0];
        for ( PersonDTO personDTO: listPerson ) {
          if ( personDTO.getEmail().equals( email ) ) {
            listEmail.add( personDTO );
            break;
          }
        }
      }
      addObjectSession( listEmail, "listEmail" );
      selectEmail = null;
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  private void cleanObject () {
    try {
      cleanObjectSession( "listPerson" );
      cleanObjectSession( "listEmail" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void loadEmailPerson ( String emails ) {
    List< PersonDTO > listPerson = null;
    ResponseDTO response = null;
    String[] arrEmail = null;
    PersonDTO perDTO = null;
    try {
      ConstantWEB.WEB_LOG.info( "emails = " + emails );
      listPerson = ( List< PersonDTO > ) getObjectSession( "listPerson" );
      if ( listPerson == null ) {
        response = generalDelegate.findPersonByClient( getUserDTO().getPerson().getClient().getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          listPerson = ( List< PersonDTO > ) response.getObjectResponse();
          addObjectSession( listPerson, "listPerson" );
        }
      }
      listEmail = new ArrayList< PersonDTO >();
      if ( ConstantWEB.RESPONSIBLE_TASK != null && !ConstantWEB.RESPONSIBLE_TASK.isEmpty() ) {
        perDTO = new PersonDTO();
        perDTO.setEmail( ConstantWEB.RESPONSIBLE_TASK );
        perDTO.setNames( ConstantWEB.RESPONSIBLE_TASK );
        listEmail.add( perDTO );
      }
      
      if ( emails != null ) {
        arrEmail = emails.split( ";" );
        for ( String email: arrEmail ) {
          if ( !email.contains( "@" ) && !email.equals( ConstantWEB.RESPONSIBLE_TASK ) ) {
            perDTO = new PersonDTO();
            perDTO.setEmail( email );
            perDTO.setNames( email );
            listEmail.add( perDTO );
          }
          for ( PersonDTO person: listPerson ) {
            if ( email.equals( person.getEmail() ) ) {
              listEmail.add( person );
              break;
            }
          }
        }
      }
      
      addObjectSession( listEmail, "listEmail" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  public void loadUpdate ( ActionEvent event ) {
    String emails = null;
    try {
      selectedObject = ( NotificationSettingDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        
        if( selectedObject.getNotificationGeneral().isMandatory() && selectedObject.getNotificationGeneral().isDefault() ) {
          activate = true;
        }
        
        if ( ( selectedObject.getId() == 0 && activate ) || selectedObject.getId() > 0 ) {
          
          listEmail = ( List< PersonDTO > ) getObjectSession( "listEmail" );
          if ( listEmail != null ) {
            for ( PersonDTO personDTO: listEmail ) {
              if ( emails == null ) {
                emails = personDTO.getEmail();
              } else {
                emails += ";" + personDTO.getEmail();
              }
            }
          }
          selectedObject.setEmails( emails );
          selectedObject.setGroup( new GroupTDTO() );
          selectedObject.getGroup().setId( ( int ) getObjectSession( "idGroup" ) );
          addObjectSession( selectedObject, "selectedObject" );
          addObjectSession( activate, "activate" );
          this.showDialogConfirmUpdate = true;
        } else {
          addMessageError( "activate", ConstantWEB.ERROR_ACTIVATE_NOTIFICATION, null );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#remove()
   */
  @Override
  public String remove () {
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#update()
   */
  @Override
  public String update () {
    ResponseDTO responseDTO = null;
    try {
      selectedObject = ( NotificationSettingDTO ) getObjectSession( "selectedObject" );
      activate = ( boolean ) getObjectSession( "activate" );
      if ( selectedObject != null ) {
        ConstantWEB.WEB_LOG.info( "Notificacion a insertar o actualizar o eliminar selectedObject :: " + selectedObject.toString() );
        if ( selectedObject.getId() == 0 ) {
          
          if ( selectedObject.getNotificationGeneral().isMandatory() && selectedObject.getNotificationGeneral().isDefault()
               && ( selectedObject.getEmails().split( ";" ) ).length == 1 ) {
            ConstantWEB.WEB_LOG.warn( "La notificacion no sera creada, es mandatoria, por defecto y solo se notificara al responsable" );
          } else {
            ConstantWEB.WEB_LOG.info( "La notificacion sera creada..." );
            selectedObject.setCreateDate( getCurrentDateTime() );
            selectedObject.setCreateUser( getUserInSession() );
            responseDTO = generalDelegate.create( selectedObject );
            ConstantWEB.WEB_LOG.info( "Respuesta despues de crear la notificacion ::> " + responseDTO.toString() );
          }
          
          
        } else if ( selectedObject.getId() > 1 && activate ) {
          
          if ( selectedObject.getNotificationGeneral().isMandatory() && selectedObject.getNotificationGeneral().isDefault()
              && ( selectedObject.getEmails().split( ";" ) ).length == 1 ) {
            ConstantWEB.WEB_LOG.warn( "La notificacion sera eliminada, es mandatoria, por defecto y solo se notificara al responsable" );
            responseDTO = generalDelegate.remove( selectedObject );
            ConstantWEB.WEB_LOG.info( "Respuesta despues de eliminar la notificacion ::> " + responseDTO.toString() );
          } else {
            ConstantWEB.WEB_LOG.info( "La notificacion sera actualizada..." );
            selectedObject.setModificationUser( getUserInSession() );
            selectedObject.setModificationDate( getCurrentDateTime() );
            responseDTO = generalDelegate.edit( selectedObject );
            ConstantWEB.WEB_LOG.info( "Respuesta despues de actualizar la notificacion ::> " + responseDTO.toString() );
          }
          
        } else {
          ConstantWEB.WEB_LOG.info( "La notificacion sera eliminada..." );
          responseDTO = generalDelegate.remove( selectedObject );
          ConstantWEB.WEB_LOG.info( "Respuesta despues de eliminar la notificacion ::> " + responseDTO.toString() );
        }
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Registro ejecutado exitosamente..." );
          this.listNotifications( selectedObject.getGroup().getId() );
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
        } else {
          ConstantWEB.WEB_LOG.error( "Error creando o actualizando la notificacion..." );
          //Obetener codigo de error de oracle
          String code = "0";
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + ConstantWEB.MESSAGE_ERROR_UPDATE );
        }
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Delete email. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (27/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void deleteEmailList ( String email ) {
    PersonDTO personDTO = null;
    try {
      if ( email != null ) {
        listEmail = ( List< PersonDTO > ) getObjectSession( "listEmail" );
        if ( listEmail != null ) {
          for ( int i = 0; i < listEmail.size(); i++ ) {
            personDTO = listEmail.get( i );
            if ( email.equals( personDTO.getEmail() ) ) {
              listEmail.remove( i );
              addObjectSession( listEmail, "listEmail" );
              break;
            }
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void loadGroupClone ( int groupId ) {
    ArrayList< GroupTDTO > list = null;
    GroupTDTO groupTDTO = null;
    try {
      list = ( ArrayList< GroupTDTO > ) getObjectSession( "listGroupT" );
      if ( list == null ) {
        list = new ArrayList< GroupTDTO >();
      }
      
      for ( int i = 0; i < list.size(); i++ ) {
        groupTDTO = list.get( i );
        if ( groupId == groupTDTO.getId() ) {
          list.remove( i );
          break;
        }
      }
      listGroupClone = ( List< GroupTDTO > ) list.clone();
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void validateSelectedRecord2 ( ActionEvent event ) {
    try {
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        if ( selectedObject.getId() > 0
             || ( selectedObject.getNotificationGeneral().isMandatory() && selectedObject.getNotificationGeneral().isDefault() ) ) {
          this.showViewDetail = true;
          this.showConfirmClone = true;
          loadGroupClone( selectedObject.getGroup().getId() );
          cleanObject();
          loadEmailPerson( selectedObject.getEmails() );
        } else {
          this.showAlertSelectRow2 = true;
          this.showAlertSelectRow = false;
          this.showViewDetail = false;
          this.showConfirmClone = false;
        }
        
      } else {
        this.showAlertSelectRow = true;
        this.showViewDetail = false;
        this.showConfirmClone = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load clone. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (27/02/2016)
   */
  public void loadClone ( ActionEvent event ) {
    NotificationSettingDTO notificationSettingDTO = null;
    List< NotificationSettingDTO > listNotificationSetting = null;
    List< NotificationSettingDTO > listNotificationSettingDel = null;
    try {
      selectedObject = ( NotificationSettingDTO ) getObjectSession( "selectedObject" );
      ConstantWEB.WEB_LOG.info( "selectedObject::" + selectedObject );
      if ( selectedObject != null ) {
        ConstantWEB.WEB_LOG.info( "selectedGroupClone::" + selectedGroupClone.size() );
        if ( selectedGroupClone != null && selectedGroupClone.size() > 0 ) {
          this.showDialogConfirmClone = true;
          listNotificationSetting = new ArrayList< NotificationSettingDTO >();
          listNotificationSettingDel = new ArrayList< NotificationSettingDTO >();
          for ( GroupTDTO group: selectedGroupClone ) {
            notificationSettingDTO = new NotificationSettingDTO();
            notificationSettingDTO.setGroup( new GroupTDTO() );
            notificationSettingDTO.setNotificationGeneral( new NotificationGeneralDTO() );
            notificationSettingDTO.getGroup().setId( group.getId() );
            notificationSettingDTO.setEmails( selectedObject.getEmails() );
            notificationSettingDTO.getNotificationGeneral().setId( selectedObject.getNotificationGeneral().getId() );
            
            if ( selectedObject.getId() > 0 ) {
              listNotificationSetting.add( notificationSettingDTO );
            } else if ( selectedObject.getNotificationGeneral().isMandatory() && selectedObject.getNotificationGeneral().isDefault() ) {
              listNotificationSettingDel.add( notificationSettingDTO );
            }
            
          }
          addObjectSession( listNotificationSettingDel, "listNotificationSettingDel" );
          addObjectSession( listNotificationSetting, "listNotificationSetting" );
          addObjectSession( selectedGroupClone, "selectedGroupClone" );
        } else {
          addMessageError( "checkboxListGroup", ConstantWEB.ERROR_GROUP_CLONE, null );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Clone. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (27/02/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public String clone () {
    List< NotificationSettingDTO > list = null;
    ResponseDTO responseDTO = null;
    NotificationSettingDTO notification = null;
    int contSuscesfull = 0;
    List< NotificationSettingDTO > listDel = null;
    try {
      listDel = ( List< NotificationSettingDTO > ) getObjectSession( "listNotificationSettingDel" );
      if ( listDel != null && listDel.size() > 0 ) {
        for ( NotificationSettingDTO notificationSettingDTO: listDel ) {
          responseDTO = generalDelegate.findNotificationByGroupGen( notificationSettingDTO.getGroup().getId(),
                                                                    notificationSettingDTO.getNotificationGeneral().getId() );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            notification = ( NotificationSettingDTO ) responseDTO.getObjectResponse();
            
            ConstantWEB.WEB_LOG.info( "Antes de eliminar la notificacion :: " + notification.toString() );
            responseDTO = generalDelegate.remove( notification );
            ConstantWEB.WEB_LOG.info( "Despues de editar la notificacion :: " + responseDTO.toString() );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              contSuscesfull++;
            }
          } else {
            contSuscesfull++;
          }
        }
        
        if ( listDel.size() == contSuscesfull ) {
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_UPDATE );
        }
        
      } else {
        list = ( List< NotificationSettingDTO > ) getObjectSession( "listNotificationSetting" );
        if ( list != null ) {
          for ( NotificationSettingDTO notificationSettingDTO: list ) {
            responseDTO = generalDelegate.findNotificationByGroupGen( notificationSettingDTO.getGroup().getId(),
                                                                      notificationSettingDTO.getNotificationGeneral().getId() );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              notification = ( NotificationSettingDTO ) responseDTO.getObjectResponse();
              notification.setEmails( notificationSettingDTO.getEmails() );
              notification.setModificationDate( getCurrentDateTime() );
              notification.setModificationUser( getUserInSession() );
              
              ConstantWEB.WEB_LOG.info( "Antes de editar clonar la notificacion :: " + notification.toString() );
              responseDTO = generalDelegate.edit( notification );
              ConstantWEB.WEB_LOG.info( "Despues de editar clonar la notificacion :: " + responseDTO.toString() );
              if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                contSuscesfull++;
              }
              
            } else if ( UtilCommon.validateResponseNoResult( responseDTO ) ) {
              notificationSettingDTO.setCreateDate( getCurrentDateTime() );
              notificationSettingDTO.setCreateUser( getUserInSession() );
              ConstantWEB.WEB_LOG.info( "Antes de crear clonar la notificacion :: " + notificationSettingDTO.toString() );
              responseDTO = generalDelegate.create( notificationSettingDTO );
              ConstantWEB.WEB_LOG.info( "Despues de crear clonar la notificacion :: " + responseDTO.toString() );
              if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                contSuscesfull++;
              }
            }
          }
          
          if ( list.size() == contSuscesfull ) {
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
          } else {
            addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_UPDATE );
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @Override
  public String add () {
    return null;
  }
  
  public int getIdGroup () {
    return idGroup;
  }
  
  public void setIdGroup ( int idGroup ) {
    this.idGroup = idGroup;
  }
  
  public List< SelectItem > getListGroup () {
    return listGroup;
  }
  
  public void setListGroup ( List< SelectItem > listGroup ) {
    this.listGroup = listGroup;
  }
  
  public List< NotificationSettingDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< NotificationSettingDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public NotificationSettingDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( NotificationSettingDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  public boolean isActivate () {
    return activate;
  }
  
  public void setActivate ( boolean activate ) {
    this.activate = activate;
  }
  
  public boolean isShowConfirmClone () {
    return showConfirmClone;
  }
  
  public void setShowConfirmClone ( boolean showConfirmClone ) {
    this.showConfirmClone = showConfirmClone;
  }
  
  public String getSelectEmail () {
    return selectEmail;
  }
  
  public void setSelectEmail ( String selectEmail ) {
    this.selectEmail = selectEmail;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< PersonDTO > getListEmail () {
    listEmail = ( List< PersonDTO > ) getObjectSession( "listEmail" );
    if ( listEmail == null ) {
      listEmail = new ArrayList< PersonDTO >();
    }
    return listEmail;
  }
  
  public void setListEmail ( List< PersonDTO > listEmail ) {
    this.listEmail = listEmail;
  }
  
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  public List< GroupTDTO > getListGroupClone () {
    return listGroupClone;
  }
  
  public void setListGroupClone ( List< GroupTDTO > listGroupClone ) {
    this.listGroupClone = listGroupClone;
  }
  
  public boolean isShowAlertSelectRow2 () {
    return showAlertSelectRow2;
  }
  
  public void setShowAlertSelectRow2 ( boolean showAlertSelectRow2 ) {
    this.showAlertSelectRow2 = showAlertSelectRow2;
  }
  
  public List< GroupTDTO > getSelectedGroupClone () {
    return selectedGroupClone;
  }
  
  public void setSelectedGroupClone ( List< GroupTDTO > selectedGroupClone ) {
    this.selectedGroupClone = selectedGroupClone;
  }
  
  public boolean isShowDialogConfirmClone () {
    return showDialogConfirmClone;
  }
  
  public void setShowDialogConfirmClone ( boolean showDialogConfirmClone ) {
    this.showDialogConfirmClone = showDialogConfirmClone;
  }
  
}
