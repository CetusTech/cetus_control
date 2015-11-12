package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

import co.com.cetus.cetuscontrol.dto.NotificationDTO;
import co.com.cetus.cetuscontrol.dto.NotificationTableDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class StatusMBean.
 * Clase para el manejo de eventos de la gestión de estados cetus controller
 * @author eChia - Cetus Technology
 * @version CetusControlWEB (04-mar-2015)
 */
@ManagedBean
@RequestScoped
public class NotificationMBean extends GeneralManagedBean {
  
  /**
   * 
   */
  private static final long       serialVersionUID        = 1L;
  
  /** The list register. */
  private List< NotificationDTO > listRegisterNo          = null;
  
  private List< NotificationTableDTO > listRegisterNoTable = null;
  
  /** The add object. */
  private NotificationDTO         addObject               = null;
  
  /** The selected object. */
  private NotificationDTO         selectedObject          = null;
  
  private int                    notificationTableCbox;
  
  /** The show confirm add. */
  private boolean                 showConfirmAdd          = false;
  
  /** The show confirm mod. */
  private boolean                 showConfirmMod          = false;
  
  /** The show dialog confirm update. */
  private boolean                 showDialogConfirmUpdate = false;
  
  /** The show confirm delete. */
  private boolean                 showConfirmDelete       = false;
  
  /** The show alert select row. */
  private boolean                 showAlertSelectRow      = false;
  
  /** The show view detail. */
  private boolean                 showViewDetail          = false;
  
  private DualListModel< String > emailsPickList          = null;
  
  private List< String >          emailsPickListSource    = null;
  
  private List< String >          emailsPickListTarget    = null;
  
  private boolean disableNotifications = false;
  
  public NotificationMBean () {
    addObject = new NotificationDTO();
    selectedObject = new NotificationDTO();
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    if ( getUserDTO() != null ) {
      listRegister();
      listRegisterNoTable();
      /**Inicializacion de listas de correos:**/
      //add
      this.emailsPickListSource = this.loadListEmails();
      this.emailsPickListTarget = new ArrayList< String >();
      this.emailsPickList = new DualListModel< String >( emailsPickListSource, emailsPickListTarget );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listRegister () {
    ResponseDTO response = null;
    try {      
      response = generalDelegate.findNotifications( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listRegisterNo = ( List< NotificationDTO > ) response.getObjectResponse();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegisterNo = new ArrayList< NotificationDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listRegisterNoTable () {
    ResponseDTO response = null;
    try {      
      response = generalDelegate.findNotificationsTableNoSystem ( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listRegisterNoTable = ( List< NotificationTableDTO > ) response.getObjectResponse();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegisterNo = new ArrayList< NotificationDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  public String load ( ActionEvent event ) {
    try {
      if ( addObject != null ) {
        
          if(  validateMessages() ){
        
            List< String > emails = this.emailsPickList.getTarget(); //Registros a almacenar
            String emailsAdd = "";
            for ( String email: emails ) {
              emailsAdd += email + ";";
            }
            addObject.setMails( emailsAdd.substring( 0, emailsAdd.length() - 1 ) );
            NotificationTableDTO ntDTO = new NotificationTableDTO();ntDTO.setId( notificationTableCbox );
            addObject.setNotificationTable( ntDTO );
            addObjectSession( addObject, "addObject" );
            this.showConfirmAdd = true;
            
          }else{
            addMessageError( "notificationAddMsg", ConstantWEB.MESSAGE_ERROR_CHEK_NOTACTIONS, null ); 
          }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
    return null;
  }
  
  boolean validateMessages(){
    boolean flag = true;
    if( addObject.isCreateAction() && addObject.getCreateActionMessage().isEmpty() )
      flag = false;
    if( !addObject.isCreateAction() && !addObject.getCreateActionMessage().isEmpty() )
      flag = false;    
    if( addObject.isUpdateAction() && addObject.getUpdateActionMessage().isEmpty() )
      flag = false;
    if( !addObject.isUpdateAction() && !addObject.getUpdateActionMessage().isEmpty() )
      flag = false;    
    if( addObject.isDeleteAction() && addObject.getDeleteActionMessage().isEmpty() )
      flag = false;
    if( !addObject.isDeleteAction() && !addObject.getDeleteActionMessage().isEmpty() )
      flag = false;
    if( !addObject.isCreateAction() && !addObject.isUpdateAction() && !addObject.isDeleteAction() &&
        addObject.getCreateActionMessage().isEmpty() && addObject.getUpdateActionMessage().isEmpty() && addObject.getDeleteActionMessage().isEmpty())
      flag = false;
    
    return flag;
  }
  
  @SuppressWarnings ( "unchecked" )
  private List< String > loadListEmails () {
    ResponseDTO response = null;
    List< String > listEmails = null;
    try {
      response = generalDelegate.findEmailsFromPerson( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        listEmails = ( List< String > ) response.getObjectResponse();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegisterNo = new ArrayList< NotificationDTO >();
        }
      }
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return listEmails;
    
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#remove()
   */
  @Override
  public String remove () {
    ResponseDTO response = null;
    try {
      this.showConfirmDelete = false;
      selectedObject = ( NotificationDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        if( !selectedObject.isNoTableSystem() ){
            ConstantWEB.WEB_LOG.debug( "########## REGISTRO A ELIMINAR  " + selectedObject.getDescription() );
            response = generalDelegate.remove( selectedObject );
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              ConstantWEB.WEB_LOG.debug( "########## REGISTRO ELIMINADO  " + selectedObject.getDescription() );
              this.initElement();
              addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
            } else {
              ConstantWEB.WEB_LOG.debug( "########## ERROR ELIMINADO EL REGISTRO  " + selectedObject.getDescription() );
              addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_DELETE_DETAIL );
            }
        }else{
          ConstantWEB.WEB_LOG.debug( "########## NO SE PUEDE ELIMINAR NOTIFICACION  " + selectedObject.getDescription() );
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_DELETE_NOTACTIONS );
        }        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#update()
   */
  @Override
  public String update () {
    boolean lSuccessfull = false;
    RequestContext context = null;
    ResponseDTO response = null;
    try {
      this.showConfirmMod = false;
      selectedObject = ( NotificationDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        context = RequestContext.getCurrentInstance();
        selectedObject.setCreateUser( selectedObject.getCreateUser() );
        selectedObject.setCreateDate( selectedObject.getCreateDate() );
        selectedObject.setModificationUser( getUserInSession() );
        selectedObject.setModificationDate( currentDate );
        NotificationTableDTO ntDTO = new NotificationTableDTO();
        ntDTO.setId( selectedObject.getIdNotTable() );
        selectedObject.setNotificationTable( ntDTO );
        
        ConstantWEB.WEB_LOG.debug( "######### ACTUALIZAR EL SIGUIENTE REGISTRO " + selectedObject.getDescription() );
        response = generalDelegate.edit( selectedObject );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          ConstantWEB.WEB_LOG.debug( "######### REGISTRO ACTUALIZADO CORRECTAMENTE " + selectedObject.getDescription() );
          this.initElement();
          lSuccessfull = true;
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
        } else {
          //Obetener codigo de error de oracle
          String code = "0";
          if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
            code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
            addMessageError( null, ConstantWEB.MESSAGE_ERROR, "Codigo de Error: " + code );
          } else {
            addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_UPDATE );
          }
          ConstantWEB.WEB_LOG.debug( "######### ERROR ACTUALIZANDO EL REGISTRO" + selectedObject.getDescription() );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    context.addCallbackParam( "lSuccessfull", lSuccessfull );
    return null;
  }
  
  public void loadUpdate ( ActionEvent event ) {
    NotificationDTO notificationDTO = null;
    try {
      if ( selectedObject != null ) {
        notificationDTO = ( NotificationDTO ) getObjectSession( "selectedObject" );
        if ( notificationDTO != null ) {
          selectedObject.setId( notificationDTO.getId() );
          selectedObject.setClientCetus( notificationDTO.getClientCetus() );
          selectedObject.setCreateDate( notificationDTO.getCreateDate() );
          selectedObject.setCreateUser( notificationDTO.getCreateUser() );
          selectedObject.setIdNotTable( notificationDTO.getIdNotTable() );
          selectedObject.setModificationUser( getUserInSession() );
          
          if( notificationDTO.isNoTableSystem() ){
            selectedObject.setNoTableSystem( notificationDTO.isNoTableSystem() );
            selectedObject.setCreateActionMessage( notificationDTO.getCreateActionMessage() );
            selectedObject.setCreateAction( notificationDTO.isCreateAction() );
            selectedObject.setUpdateActionMessage( notificationDTO.getUpdateActionMessage() );
            selectedObject.setUpdateAction( notificationDTO.isUpdateAction() );
            selectedObject.setDeleteActionMessage( notificationDTO.getDeleteActionMessage() );
            selectedObject.setDeleteAction( notificationDTO.isDeleteAction() );
            selectedObject.setOwnerNotify( notificationDTO.isOwnerNotify() );
          }
          
          List< String > emails = this.emailsPickList.getTarget(); //Emails actualizar
          if( emails != null && emails.size()>0 ){
            String emailsAdd = "";
            for ( String email: emails ) {
              emailsAdd += email + ";";
            }
            selectedObject.setMails( emailsAdd.substring( 0, emailsAdd.length() - 1 ) );
          }
        }
        cleanObjectSession( "selectedObject" );
        addObjectSession( selectedObject, "selectedObject" );
        this.showDialogConfirmUpdate = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e ); 
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @Override
  public String add () {
    boolean lSuccessfull = false;
    RequestContext context = null;
    ResponseDTO response = null;
    try {
      this.showConfirmAdd = false;
      addObject = ( NotificationDTO ) getObjectSession( "addObject" );
      if ( addObject != null ) {
        if ( getUserDTO() != null ) {
          addObject.setClientCetus( getUserDTO().getPerson().getClient().getClientCetus() );
        }
        context = RequestContext.getCurrentInstance();
        
        /**Getionar Info de Notificacion:**/
        addObject.setCreateUser( getUserInSession() );
        addObject.setCreateDate( new Date() );
        
        response = generalDelegate.create( addObject );
        
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          ConstantWEB.WEB_LOG.debug( "..::Notificacion creada exitosamente::.." + addObject.getDescription() );
          this.initElement();
          lSuccessfull = true;
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
        } else {
          //Obetener codigo de error de oracle
          String code = "0";
          if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
            code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
    }
    context.addCallbackParam( "lSuccessfull", lSuccessfull );
    return null;
  }
  
  public void validateSelectedRecord ( ActionEvent event ) {
    try {
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        /**Inicializacion de listas de correos:**/
        List< String > listEmailsAdd = this.loadListEmails();
        List< String > listEmailsAct = new ArrayList< String >();
        String[] emailsTemp = selectedObject.getMails() != null ? selectedObject.getMails().split( ";" ) : null;
        if( emailsTemp != null && emailsTemp.length > 0 ){
          for ( String e: emailsTemp ) {
            listEmailsAct.add( e );
          }        
          listEmailsAdd.removeAll( listEmailsAct );
        }
        this.emailsPickListSource = listEmailsAdd!=null?listEmailsAdd:new ArrayList< String >();
        this.emailsPickListTarget = listEmailsAct;
        this.emailsPickList = new DualListModel< String >( emailsPickListSource, emailsPickListTarget );    
        
        if( selectedObject.isNoTableSystem() ){
          this.disableNotifications = true;
        }
        
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
    }
  }
  
  public List< NotificationDTO > getListRegisterNo () {
    return listRegisterNo;
  }
  
  public void setListRegisterNo ( List< NotificationDTO > listRegisterNo ) {
    this.listRegisterNo = listRegisterNo;
  }

  public List< NotificationTableDTO > getListRegisterNoTable () {
    return listRegisterNoTable;
  }

  public void setListRegisterNoTable ( List< NotificationTableDTO > listRegisterNoTable ) {
    this.listRegisterNoTable = listRegisterNoTable;
  }

  public NotificationDTO getAddObject () {
    return addObject;
  }
  
  public void setAddObject ( NotificationDTO addObject ) {
    this.addObject = addObject;
  }
  
  public NotificationDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( NotificationDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
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
  
  public DualListModel< String > getEmailsPickList () {
    return emailsPickList;
  }
  
  public void setEmailsPickList ( DualListModel< String > emailsPickList ) {
    this.emailsPickList = emailsPickList;
  }
  
  public List< String > getEmailsPickListSource () {
    return emailsPickListSource;
  }
  
  public void setEmailsPickListSource ( List< String > emailsPickListSource ) {
    this.emailsPickListSource = emailsPickListSource;
  }
  
  public List< String > getEmailsPickListTarget () {
    return emailsPickListTarget;
  }
  
  public void setEmailsPickListTarget ( List< String > emailsPickListTarget ) {
    this.emailsPickListTarget = emailsPickListTarget;
  }

  public long getNotificationTableCbox () {
    return notificationTableCbox;
  }

  public void setNotificationTableCbox ( int notificationTableCbox ) {
    this.notificationTableCbox = notificationTableCbox;
  }

  public boolean isDisableNotifications () {
    return disableNotifications;
  }

  public void setDisableNotifications ( boolean disableNotifications ) {
    this.disableNotifications = disableNotifications;
  }

}
