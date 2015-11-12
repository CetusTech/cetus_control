package co.com.cetus.cetuscontrol.web.bean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import co.com.cetus.cetuscontrol.dto.StatusDTO;
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
public class StatusMBean extends GeneralManagedBean {
  
  /**
   * 
   */
  private static final long serialVersionUID        = 1L;
  
  /** The list register. */
  private List< StatusDTO > listRegister            = null;
  
  /** The add object. */
  private StatusDTO         addObject               = null;
  
  /** The selected object. */
  private StatusDTO         selectedObject          = null;
  
  /** The show confirm add. */
  private boolean           showConfirmAdd          = false;
  
  /** The show confirm mod. */
  private boolean           showConfirmMod          = false;
  
  /** The show dialog confirm update. */
  private boolean           showDialogConfirmUpdate = false;
  
  /** The show confirm delete. */
  private boolean           showConfirmDelete       = false;
  
  /** The show alert select row. */
  private boolean           showAlertSelectRow      = false;
  
  /** The show view detail. */
  private boolean           showViewDetail          = false;
  
  public StatusMBean () {
    addObject = new StatusDTO();
    selectedObject = new StatusDTO();
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    if ( getUserDTO() != null ) {
      listRegister();
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listRegister () {
    ResponseDTO response = null;
    try {
      
      response = generalDelegate.findStatus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listRegister = ( List< StatusDTO > ) response.getObjectResponse();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< StatusDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  public void load ( ActionEvent event ) {
    try {
      if ( addObject != null ) {
        addObjectSession( addObject, "addObject" );
        this.showConfirmAdd = true;
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
    ResponseDTO response = null;
    try {
      this.showConfirmDelete = false;
      selectedObject = ( StatusDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        if ( selectedObject.getId() == ConstantWEB.STATUS_ASSIGNED || selectedObject.getId() == ConstantWEB.STATUS_COMPLETED
             || selectedObject.getId() == ConstantWEB.STATUS_INPROGRESS || selectedObject.getId() == ConstantWEB.STATUS_CANCELED ) {
          addMessageWarning( null, ConstantWEB.MESSAGE_WARNING, MessageFormat.format( ConstantWEB.MESSAGE_ERROR_DELETE_MASTER, "Estado" ) );
        } else {
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
      selectedObject = ( StatusDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        context = RequestContext.getCurrentInstance();
        selectedObject.setCreationUser( getUserInSession() );
        selectedObject.setModificationDate( currentDate );
        selectedObject.setDescription( selectedObject.getDescription().toUpperCase() );
        selectedObject.setColor( selectedObject.getColor().toUpperCase() );
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
    StatusDTO statusDTO = null;
    try {
      if ( selectedObject != null ) {
        statusDTO = ( StatusDTO ) getObjectSession( "selectedObject" );
        if ( statusDTO != null ) {
          selectedObject.setId( statusDTO.getId() );
          selectedObject.setClientCetus( statusDTO.getClientCetus() );
          selectedObject.setCreationDate( statusDTO.getCreationDate() );
          selectedObject.setCreationUser( statusDTO.getCreationUser() );
          selectedObject.setModificationUser( getUserInSession() );
        }
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
      addObject = ( StatusDTO ) getObjectSession( "addObject" );
      if ( addObject != null ) {
        if ( getUserDTO() != null ) {
          addObject.setClientCetus( getUserDTO().getPerson().getClient().getClientCetus() );
        }
        context = RequestContext.getCurrentInstance();
        addObject.setCreationUser( getUserInSession() );
        addObject.setDescription( addObject.getDescription().toUpperCase() );
        addObject.setColor( addObject.getColor().toUpperCase() );
        addObject.setCreationDate( new Date() );
        response = generalDelegate.create( addObject );
        
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          ConstantWEB.WEB_LOG.debug( "..::Estado creado exitosamente::.." + addObject.getDescription() );
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
  
  public List< StatusDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< StatusDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public StatusDTO getAddObject () {
    return addObject;
  }
  
  public void setAddObject ( StatusDTO addObject ) {
    this.addObject = addObject;
  }
  
  public StatusDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( StatusDTO selectedObject ) {
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
  
}
