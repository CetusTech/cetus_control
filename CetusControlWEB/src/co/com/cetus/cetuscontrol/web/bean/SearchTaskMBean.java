package co.com.cetus.cetuscontrol.web.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.event.SelectEvent;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
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
  private boolean           showViewDetail   = false;
  private boolean           showMenu         = false;
  private String            destination      = ConstantWEB.PATH_FILE_TASK;
  private String            separador        = System.getProperty( "file.separator" );
  private String            idUsuario        = null;
  private int               indexTab         = 0;
                                             
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
        idUsuario = String.valueOf( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
        listTask = ( List< TaskDTO > ) getObjectSession( "listTask" );
        if ( listTask == null || listTask.size() == 0 ) {
          responseDTO = generalDelegate.findTaskByFilter( userPortalDTO.getPerson().getClient().getId(),
                                                          ( String ) getObjectSession( ConstantWEB.DESC_FILTER_SEARCH ),
                                                          ( String ) getObjectSession( ConstantWEB.DESC_INPUT_SEARCH ) );
                                                          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            listTask = ( List< TaskDTO > ) responseDTO.getObjectResponse();
          } else {
            listTask = new ArrayList< >();
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
        if ( ( selectedObject.getStatus().getId() == ConstantWEB.STATUS_COMPLETED_VAL )
             || selectedObject.getStatus().getId() == ConstantWEB.STATUS_CANCELED_VAL ) {
          showMenu = true;
        }
        showViewDetail = true;
        addObjectSession( selectedObject, "selectedObject" );
      } else {
        cleanObjectSession( "selectedObject" );
      }
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  /**
   * </p> Close dialog view. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (24/05/2016)
   */
  public void closeDialogView () {
    selectedObject = new TaskDTO();
    selectedObject.setArea( new AreaDTO() );
    selectedObject.setPriority( new PriorityDTO() );
    selectedObject.setStatus( new StatusDTO() );
    selectedObject.setTaskType( new TaskTypeDTO() );
    removerFileUploads();
  }
  
  /**
   * </p> Remover file uploads. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  private void removerFileUploads () {
    cleanObjectSession( "listFilesTask" );
    if ( destination != null && idUsuario != null ) {
      
      File archivos = new File( destination + separador + idUsuario );
      if ( archivos != null ) {
        File[] lista = archivos.listFiles();
        if ( archivos.listFiles() != null && archivos.listFiles().length > 0 ) {
          for ( File file: lista ) {
            if ( !file.isDirectory() ) {
              file.delete();
            }
          }
        }
      }
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
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public boolean isShowMenu () {
    return showMenu;
  }
  
  public void setShowMenu ( boolean showMenu ) {
    this.showMenu = showMenu;
  }
  
  public int getIndexTab () {
    return indexTab;
  }
  
  public void setIndexTab ( int indexTab ) {
    this.indexTab = indexTab;
  }
  
}
