package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.com.cetus.cetuscontrol.dto.GroupTDTO;
import co.com.cetus.cetuscontrol.dto.NotificationSettingDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

@ManagedBean
@RequestScoped
public class NotificationMBean extends GeneralManagedBean {
  
  private static final long              serialVersionUID = 1L;
                                                          
  /** The selected object. */
  private int                            idGroup          = 0;
                                                          
  private List< SelectItem >             listGroup        = null;
                                                          
  private List< NotificationSettingDTO > listRegister     = null;
                                                          
  public NotificationMBean () {
    listRegister = new ArrayList< >();
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    if ( getUserDTO() != null ) {
      loadGroup();
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
    List< GroupTDTO > list = null;
    try {
      if ( idGroup > 0 ) {
        addObjectSession( idGroup, "idGroup" );
        
        listNotifications( idGroup );
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listNotifications ( int idGroup ) {
    ResponseDTO response = null;
    try {
      //      response = generalDelegate.findGroupByPerson( getUserDTO().getPerson().getId() );
      //      if ( UtilCommon.validateResponseSuccess( response ) ) {
      //        //Respuesta del servicio 
      //        this.listRegister = ( List< GroupTDTO > ) response.getObjectResponse();
      //      } else {
      //        //si no encontro registro para listar 
      //        if ( UtilCommon.validateResponseNoResult( response ) ) {
      //          this.listRegister = new ArrayList< GroupTDTO >();
      //        }
      //      }
      ConstantWEB.WEB_LOG.info( "idGroup::" + idGroup );
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
  
}
