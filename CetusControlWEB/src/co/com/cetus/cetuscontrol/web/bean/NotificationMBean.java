package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import co.com.cetus.cetuscontrol.dto.GroupTDTO;
import co.com.cetus.cetuscontrol.dto.NotificationGeneralDTO;
import co.com.cetus.cetuscontrol.dto.NotificationSettingDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

@ManagedBean
@RequestScoped
public class NotificationMBean extends GeneralManagedBean {
  
  private static final long              serialVersionUID   = 1L;
                                                            
  /** The selected object. */
  private int                            idGroup            = 0;
                                                            
  private List< SelectItem >             listGroup          = null;
                                                            
  private List< NotificationSettingDTO > listRegister       = null;
                                                            
  private NotificationSettingDTO         selectedObject     = null;
                                                            
  private boolean                        showViewDetail     = false;
  private boolean                        showAlertSelectRow = false;
  private boolean                        showConfirmMod     = false;
  private boolean                        activate     = false;
                                                            
  public NotificationMBean () {
    
    selectedObject = new NotificationSettingDTO();
    selectedObject.setNotificationGeneral( new NotificationGeneralDTO() );
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
    List< GroupTDTO > list = null;
    try {
      response = generalDelegate.findGroupByPerson( getUserDTO().getPerson().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        list = ( List< GroupTDTO > ) response.getObjectResponse();
        if ( list != null && !list.isEmpty() ) {
          this.listGroup = new ArrayList< SelectItem >();
          for ( GroupTDTO objDTO: list ) {
            this.listGroup.add( new SelectItem( objDTO.getId(), objDTO.getName() ) );
          }
        }
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listGroup = new ArrayList< SelectItem >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void changeGroup () {
    try {
      if ( idGroup > 0 ) {
        addObjectSession( idGroup, "idGroup" );
        listNotifications( idGroup );
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
    try {
      
      response = generalDelegate.findNotificationGenClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio
        list = ( List< NotificationGeneralDTO > ) response.getObjectResponse();
        if ( list != null && !list.isEmpty() ) {
          
          response = generalDelegate.findNotificationByGroup( idGroup );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            listNS = ( List< NotificationSettingDTO > ) response.getObjectResponse();
          }else{
            listNS = new ArrayList<NotificationSettingDTO>();
          }
          
          this.listRegister = new ArrayList< NotificationSettingDTO >();
          forNgeneral: for ( NotificationGeneralDTO notificationGeneralDTO: list ) {
            int i = 0;
            while ( listNS.size() > 0 && i < listNS.size() ) {
              NotificationSettingDTO nSettingDTO = listNS.get( i );
              if( notificationGeneralDTO.getId() == nSettingDTO.getNotificationGeneral().getId() ){
                this.listRegister.add( nSettingDTO );
                listNS.remove( i );
                continue forNgeneral;
              }
              i++;
            }
                       
            notificationSettingDTO = new NotificationSettingDTO();
            notificationSettingDTO.setNotificationGeneral( new NotificationGeneralDTO() );
            notificationSettingDTO.getNotificationGeneral().setId( notificationGeneralDTO.getId() );
            notificationSettingDTO.getNotificationGeneral().setName( notificationGeneralDTO.getName() );
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
        this.showViewDetail = true;
        this.showConfirmMod = true;
      } else {
        this.showAlertSelectRow = true;
        this.showViewDetail = false;
        this.showConfirmMod = false;
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

  
  
}
