package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.event.SelectEvent;

import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class SearchTaskMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (14/05/2016)
 */
@ManagedBean
@RequestScoped
public class SearchTaskMBean extends GeneralManagedBean {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -3255236201554716426L;
                                             
  private List< TaskDTO >   listTask         = null;
  private UserPortalDTO     userPortalDTO    = null;
  private TaskDTO           selectedObject   = null;
                                             
  public SearchTaskMBean () {
  
  }
  
  @SuppressWarnings ( "unchecked" )
  @Override
  @PostConstruct
  public void initElement () {
    ResponseDTO responseDTO = null;
    try {
      userPortalDTO = getUserDTO();
      if ( userPortalDTO != null ) {
        listTask = ( List< TaskDTO > ) getObjectSession( "listTask" );
        if ( listTask == null || listTask.size() == 0 ) {
          responseDTO = generalDelegate.findTaskByFilter( userPortalDTO.getPerson().getClient().getId(),
                                                          ( String ) getObjectSession( ConstantWEB.DESC_FILTER_SEARCH ),
                                                          ( String ) getObjectSession( ConstantWEB.DESC_INPUT_SEARCH ) );
          
          if( UtilCommon.validateResponseSuccess( responseDTO ) ){
            listTask = ( List< TaskDTO > ) responseDTO.getObjectResponse();
          }else{
            listTask = new ArrayList<>();
          }
          addObjectSession( listTask, "listTask" );
        }
      } else {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
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
  
  /**
   * </p> On row select task. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (14/05/2016)
   */
  public void onRowSelectTask ( SelectEvent event ) {
    try {
      selectedObject = ( ( TaskDTO ) event.getObject() );
      if ( selectedObject != null ) {
      
      } else {
      }
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  public List< TaskDTO > getListTask () {
    return listTask;
  }
  
  public void setListTask ( List< TaskDTO > listTask ) {
    this.listTask = listTask;
  }
  
  public TaskDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( TaskDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
}
