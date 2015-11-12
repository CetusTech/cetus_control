package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class PriorityMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (19/06/2015)
 */
@ManagedBean
@RequestScoped
public class PriorityMBean extends GeneralManagedBean {
  
  private static final long   serialVersionUID        = -3255236201554716426L;
  
  private UserPortalDTO       userPortalDTO           = null;
  private List< PriorityDTO > listRegister            = null;
  private PriorityDTO         addObject               = null;
  private PriorityDTO         selectedObject          = null;
  private boolean             showConfirmAdd          = false;
  private boolean             showConfirmMod          = false;
  private boolean             showDialogConfirmUpdate = false;
  private boolean             showConfirmDelete       = false;
  private boolean             showAlertSelectRow      = false;
  private boolean             showViewDetail          = false;
  

  /**
   * </p> Instancia un nuevo priority m bean. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (19/06/2015)
   */
  public PriorityMBean () {
    addObject = new PriorityDTO();
    selectedObject = new PriorityDTO();
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    userPortalDTO = getUserDTO();
    if ( userPortalDTO != null ) {
      listRegister( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  /**
   * </p> Load. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (18/06/2015)
   */
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
  
  /**
   * </p> Load update. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (18/06/2015)
   */
  public void loadUpdate ( ActionEvent event ) {
    PriorityDTO priorytyDTO = null;
    try {
      if ( selectedObject != null ) {
        priorytyDTO = ( PriorityDTO ) getObjectSession( "selectedObject" );
        if ( priorytyDTO != null ) {
          selectedObject.setId( priorytyDTO.getId() );
          selectedObject.setClientCetus( priorytyDTO.getClientCetus() );
          selectedObject.setCreationUser( priorytyDTO.getCreationUser() );
          selectedObject.setCreationDate( priorytyDTO.getCreationDate() );
        }
        addObjectSession( selectedObject, "selectedObject" );
        this.showDialogConfirmUpdate = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> List register. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (18/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void listRegister ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findPriorityByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listRegister = ( List< PriorityDTO > ) response.getObjectResponse();
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< PriorityDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#remove()
   */
  @Override
  public String remove () {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      this.showConfirmDelete = false;
      selectedObject = ( PriorityDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        ConstantWEB.WEB_LOG.info( "Se procede a eliminar la prioridad ::> " + selectedObject.toString() );
        responseDTO = generalDelegate.remove( selectedObject );
        ConstantWEB.WEB_LOG.info( "Respuesta despues de eliminar la prioridad ::> " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Prioridad eliminada exitosamente..." );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
        } else {
          ConstantWEB.WEB_LOG.error( "Error eliminado la prioridad..." );
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + ConstantWEB.MESSAGE_ERROR_DELETE_DETAIL );
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
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      this.showConfirmMod = false;
      selectedObject = ( PriorityDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        selectedObject.setModificationUser( getUserInSession() );
        selectedObject.setModificationDate( currentDate );
        
        ConstantWEB.WEB_LOG.info( "Se procede a actualizar la prioridad ::> " + selectedObject.toString() );
        responseDTO = generalDelegate.edit( selectedObject );
        ConstantWEB.WEB_LOG.info( "Respuesta despues de actualizar la prioridad ::> " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Prioridad actualizada exitosamente..." );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
        } else {
          ConstantWEB.WEB_LOG.error( "Error actualizando la prioridad..." );
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + ConstantWEB.MESSAGE_ERROR_UPDATE );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @Override
  public String add () {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      this.showConfirmAdd = false;
      addObject = ( PriorityDTO ) getObjectSession( "addObject" );
      if ( addObject != null ) {
        addObject.setClientCetus( userPortalDTO.getPerson().getClient().getClientCetus() );
        addObject.setCreationUser( getUserInSession() );
        addObject.setCreationDate( currentDate );
        
        ConstantWEB.WEB_LOG.info( "Se procede a crear la prioridad ::> " + addObject.toString() );
        responseDTO = generalDelegate.create( addObject );
        ConstantWEB.WEB_LOG.info( "Respuesta despues de crear la prioridad ::> " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Prioridad creada exitosamente..." );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
        } else {
          ConstantWEB.WEB_LOG.error( "Error creando el Prioridad..." );
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + ConstantWEB.MESSAGE_ERROR_CREATE );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
    }
    return null;
  }
  
  /**
   * </p> Validate selected record. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (18/06/2015)
   */
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
  
  public List< PriorityDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< PriorityDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public PriorityDTO getAddObject () {
    return addObject;
  }
  
  public void setAddObject ( PriorityDTO addObject ) {
    this.addObject = addObject;
  }
  
  public PriorityDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( PriorityDTO selectedObject ) {
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
