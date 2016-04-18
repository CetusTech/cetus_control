package co.com.cetus.cetuscontrol.web.bean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DualListModel;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.AreaTypeTaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class AreaTaskTypeMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (14/04/2016)
 */
@ManagedBean
@RequestScoped
public class AreaTaskTypeMBean extends GeneralManagedBean {
  
  private static final long            serialVersionUID = -3255236201554716426L;
                                                        
  private UserPortalDTO                userPortalDTO    = null;
  private List< AreaDTO >              listRegister     = null;
  private DualListModel< TaskTypeDTO > pickListTaskType = null;
  private int                          idArea           = 0;
  private boolean                      showConfirmAdd   = false;
                                                        
  /**
   * </p> Instancia un nuevo area task type m bean. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (15/04/2016)
   */
  public AreaTaskTypeMBean () {
    pickListTaskType = new DualListModel< TaskTypeDTO >( new ArrayList< TaskTypeDTO >(), new ArrayList< TaskTypeDTO >() );
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
  @SuppressWarnings ( "unchecked" )
  @Override
  public String add () {
    ResponseDTO responseDTO = null;
    String code = "0";
    List< TaskTypeDTO > listTaskType = null;
    List< String > listExists = new ArrayList< String >();
    List< String > listExistsAux = new ArrayList< String >();
    List< String > listNew = new ArrayList< String >();
    List< AreaTypeTaskDTO > listAreaTypeTaskDTO = null;
    HashMap< String, String > mapDescription = new HashMap< String, String >();
    try {
      idArea = ( int ) getObjectSession( "idArea" );
      this.showConfirmAdd = false;
      listTaskType = ( List< TaskTypeDTO > ) getObjectSession( "listAreaTaskType" );
      if ( listTaskType != null && listTaskType.size() > 0 ) {
        for ( TaskTypeDTO taskTypeDTO: listTaskType ) {
          listExists.add( String.valueOf( taskTypeDTO.getId() ) );
        }
      }
      ConstantWEB.WEB_LOG.info( "Lista de tipos de tareas relacionadas actualmente al area  :: " + listExists.toString() );
      
      pickListTaskType = ( DualListModel< TaskTypeDTO > ) getObjectSession( "pickListTaskType" );
      if ( pickListTaskType != null ) {
        List< TaskTypeDTO > listTarget = pickListTaskType.getTarget();
        if ( listTarget != null && listTarget.size() > 0 ) {
          for ( TaskTypeDTO taskTypeDTO: listTarget ) {
            listNew.add( String.valueOf( taskTypeDTO.getId() ) );
            mapDescription.put( String.valueOf( taskTypeDTO.getId() ), taskTypeDTO.getDescription() );
          }
        }
      }
      ConstantWEB.WEB_LOG.info( "Lista de tipos de tareas nuevas relacionadas al area  :: " + listNew.toString() );
      
      listExistsAux.addAll( listExists );
      listExists.removeAll( listNew );
      
      ConstantWEB.WEB_LOG.info( "Lista de tipos de tareas a eliminar del area " + listExists.toString() );
      
      if ( listExists.size() > 0 ) {
        listAreaTypeTaskDTO = ( List< AreaTypeTaskDTO > ) getObjectSession( "listAreaTypeTaskDTO" );
        if ( listAreaTypeTaskDTO != null && listAreaTypeTaskDTO.size() > 0 ) {
          for ( String string: listExists ) {
            for ( AreaTypeTaskDTO areaTypeTaskDTO: listAreaTypeTaskDTO ) {
              if ( string.equals( String.valueOf( areaTypeTaskDTO.getTaskType().getId() ) ) ) {
                responseDTO = generalDelegate.remove( areaTypeTaskDTO );
                if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                  ConstantWEB.WEB_LOG.info( "Se elimino correctamente el tipo de tarea " + string + ", del area " + idArea );
                } else {
                  ConstantWEB.WEB_LOG.error( "Error eliminando el tipo de tarea " + string + ", del area " + idArea );
                  //Obetener codigo de error de oracle
                  if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
                    code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ),
                                                               responseDTO.getMessage().lastIndexOf( ":" ) );
                  }
                  String fullName = areaTypeTaskDTO.getTaskType().getDescription();
                  addMessageWarning( null,
                                     ConstantWEB.MESSAGE_WARNING,
                                     code + ":"
                                                                  + MessageFormat.format( ConstantWEB.MESSAGE_ERROR_DELETE_AREA_TASK_TYPE, fullName,
                                                                                          idArea ) );
                }
                break;
              }
            }
          }
        }
      }
      
      listNew.removeAll( listExistsAux );
      ConstantWEB.WEB_LOG.info( "Lista de tipos de tarea a insertar al area " + listNew.toString() );
      
      if ( listNew.size() > 0 ) {
        for ( String string: listNew ) {
          AreaTypeTaskDTO areaTypeTaskDTO = new AreaTypeTaskDTO();
          areaTypeTaskDTO.setCreationDate( currentDate );
          areaTypeTaskDTO.setCreationUser( getUserInSession() );
          
          areaTypeTaskDTO.setArea( new AreaDTO() );
          areaTypeTaskDTO.getArea().setId( idArea );
          areaTypeTaskDTO.setTaskType( new TaskTypeDTO() );
          areaTypeTaskDTO.getTaskType().setId( Integer.parseInt( string ) );
          
          responseDTO = generalDelegate.create( areaTypeTaskDTO );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            ConstantWEB.WEB_LOG.info( "Se inserto correctamente el tipo de tarea " + string + ", en el area " + idArea );
          } else {
            ConstantWEB.WEB_LOG.error( "Error insertando el tipo de tarea " + string + ", en el area " + idArea );
            //Obetener codigo de error de oracle
            if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
              code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ),
                                                         responseDTO.getMessage().lastIndexOf( ":" ) );
            }
            String fullName = mapDescription.get( string );
            addMessageWarning( null,
                               ConstantWEB.MESSAGE_WARNING,
                               code + ":" + MessageFormat.format( ConstantWEB.MESSAGE_ERROR_CREATE_AREA_TASK_TYPE, fullName, idArea ) );
          }
          
        }
      }
      
      addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
      clean();
      this.initElement();
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
    }
    return null;
  }
  
  private void clean () {
    idArea = 0;
    cleanObjectSession( "idArea" );
    cleanObjectSession( "listAreaTaskType" );
    cleanObjectSession( "pickListTaskType" );
    cleanObjectSession( "listAreaTypeTaskDTO" );
    cleanObjectSession( "listTaskTypeClientCetus" );
  }
  
  /**
   * </p> List register. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (17/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void listRegister ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findAreaByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listRegister = ( List< AreaDTO > ) response.getObjectResponse();
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< AreaDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Load. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (17/06/2015)
   */
  public void load ( ActionEvent event ) {
    try {
      idArea = ( int ) getObjectSession( "idArea" );
      if ( idArea > 0 ) {
        if ( pickListTaskType != null ) {
          addObjectSession( pickListTaskType, "pickListTaskType" );
          this.showConfirmAdd = true;
        }
      } else {
        addMessageError( "areaAdd", ConstantWEB.FIELD_REQUIRED, null );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Change area. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (15/04/2016)
   */
  public void changeArea () {
    try {
      ConstantWEB.WEB_LOG.info( idArea );
      addObjectSession( idArea, "idArea" );
      if ( idArea > 0 ) {
        loadPickListTaskType( userPortalDTO.getPerson().getClient().getClientCetus().getId(), idArea );
      }else{
        cleanObjectSession( "listAreaTaskType" );
        cleanObjectSession( "pickListTaskType" );
        cleanObjectSession( "listAreaTypeTaskDTO" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load pick list task type. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClient the id client
   * @param idGroup the id group
   * @since CetusControlWEB (15/04/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void loadPickListTaskType ( int idClientCetus, int idArea ) {
    ResponseDTO response = null;
    List< TaskTypeDTO > listTaskTypeClientCetus = null;
    List< TaskTypeDTO > listAreaTaskType = null;
    List< AreaTypeTaskDTO > listAreaTypeTaskDTO = null;
    
    try {
      response = this.generalDelegate.findTaskTypeByArea( idArea );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        listAreaTypeTaskDTO = ( List< AreaTypeTaskDTO > ) response.getObjectResponse();
        if ( listAreaTypeTaskDTO != null && listAreaTypeTaskDTO.size() > 0 ) {
          listAreaTaskType = new ArrayList< TaskTypeDTO >();
          for ( AreaTypeTaskDTO areaTypeTaskDTO: listAreaTypeTaskDTO ) {
            listAreaTaskType.add( areaTypeTaskDTO.getTaskType() );
          }
        }
      } else {
        listAreaTaskType = new ArrayList< TaskTypeDTO >();
      }
      
      response = this.generalDelegate.findTaskType( idClientCetus );
      if ( UtilCommon.validateResponseSuccessXMLOutput( response ) ) {
        listTaskTypeClientCetus = ( List< TaskTypeDTO > ) response.getObjectResponse();
      } else {
        listTaskTypeClientCetus = new ArrayList< TaskTypeDTO >();
      }
      
      if ( listAreaTaskType != null && listAreaTaskType.size() > 0 && listTaskTypeClientCetus != null && listTaskTypeClientCetus.size() > 0 ) {
        int i = 0;
        for ( TaskTypeDTO taskTypeDTO: listAreaTaskType ) {
          i = 0;
          for ( TaskTypeDTO taskTypeDTOClient: listTaskTypeClientCetus ) {
            if ( taskTypeDTO.getId() == taskTypeDTOClient.getId() ) {
              listTaskTypeClientCetus.remove( i );
              break;
            }
            i++;
          }
        }
      }
      
      addObjectSession( listAreaTaskType, "listAreaTaskType" );
      addObjectSession( listAreaTypeTaskDTO, "listAreaTypeTaskDTO" );
      addObjectSession( listTaskTypeClientCetus, "listTaskTypeClientCetus" );
      
      pickListTaskType = new DualListModel< TaskTypeDTO >( listTaskTypeClientCetus, listAreaTaskType );
      addObjectSession( pickListTaskType, "pickListTaskType" );
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public List< AreaDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< AreaDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public int getIdArea () {
    return idArea;
  }
  
  public void setIdArea ( int idArea ) {
    this.idArea = idArea;
  }
  
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  @SuppressWarnings ( "unchecked" )
  public DualListModel< TaskTypeDTO > getPickListTaskType () {
    pickListTaskType = ( DualListModel< TaskTypeDTO > ) getObjectSession( "pickListTaskType" );
    if ( pickListTaskType == null ) {
      pickListTaskType = new DualListModel< TaskTypeDTO >( new ArrayList< TaskTypeDTO >(), new ArrayList< TaskTypeDTO >() );
    }
    return pickListTaskType;
  }
  
  public void setPickListTaskType ( DualListModel< TaskTypeDTO > pickListTaskType ) {
    this.pickListTaskType = pickListTaskType;
  }
  
}
