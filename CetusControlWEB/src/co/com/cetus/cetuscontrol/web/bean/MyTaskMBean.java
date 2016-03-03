package co.com.cetus.cetuscontrol.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.delegate.GeneralDelegate;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class MyTaskMBean.
 * Clase para el manejo de reporte diagrama con los status de las tareas del usuario relacionado.
 * @author eChia - Cetus Technology
 * @version CetusControlWEB (16/07/2015)
 */
@ManagedBean
@RequestScoped
public class MyTaskMBean implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID   = 1L;
                                               
  /** The list register. */
  private List< TaskDTO >   listRegister       = null;
                                               
  /** The selected object. */
  private TaskDTO           selectedObject     = null;
                                               
  private BarChartModel     barModel;
                            
  private DashboardModel    model;
                            
  private PieChartModel     pieModel;
                            
  protected GeneralDelegate generalDelegate    = GeneralDelegate.getInstance();
                                               
  Map< String, Integer >    progressTask       = new HashMap< String, Integer >();
                                               
  private List< TaskDTO >   reportTaskSelected = null;
                                               
  private long              prioritySelected   = 0;
                                               
  private boolean           showViewDetail     = false;
                                               
  List< PriorityDTO >       priorityList;
                            
  /**
   * 
   */
  public MyTaskMBean () {
  }
  
  @PostConstruct
  public void init () {
    listRegister();
    createBarModel();
    createPieModels();
    
    model = new DefaultDashboardModel();
    DashboardColumn column1 = new DefaultDashboardColumn();
    DashboardColumn column2 = new DefaultDashboardColumn();
    
    column1.addWidget( "task" );
    column2.addWidget( "priority" );
    column1.addWidget( "myProgress" );
    
    model.addColumn( column1 );
    model.addColumn( column2 );
    
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listRegister () {
    ResponseDTO response = null;
    try {
      /**Consultar tareas en curso del usuario:**/
      response = generalDelegate.findTaskByPerson( getUserDTO().getPerson().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listRegister = ( List< TaskDTO > ) response.getObjectResponse();
        if ( this.listRegister == null ) this.listRegister = new ArrayList< TaskDTO >();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< TaskDTO >();
        }
      }
      
      /**Consultar situación del usuario:**/
      response = generalDelegate.findProgressTaskByPerson( getUserDTO().getPerson().getId(), getUserDTO().getPerson().getClient().getClientCetus()
                                                                                                         .getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.progressTask = ( HashMap< String, Integer > ) response.getObjectResponse();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.progressTask = new HashMap< String, Integer >();
        }
      }
      
    } catch ( Exception e ) {
      this.listRegister = new ArrayList< TaskDTO >();
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  public void onRowSelect ( SelectEvent event ) {
    try {
      selectedObject = ( ( TaskDTO ) event.getObject() );
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        showViewDetail = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void createBarModel () {
    barModel = new BarChartModel();
    //barModel.setTitle( ConstantWEB.MYTASK_MY_PRIORITY_DIGRAM_TITLE );
    barModel.setLegendPosition( "ne" );
    barModel.setSeriesColors( ConstantWEB.MYTASK_TASK_BAR_SERIESCOLORS );
    
    Axis xAxis = barModel.getAxis( AxisType.X );
    //xAxis.setLabel( ConstantWEB.MYTASK_MY_PRIORITY_H_LABEL );
    
    Axis yAxis = barModel.getAxis( AxisType.Y );
    yAxis.setLabel( ConstantWEB.MYTASK_MY_PRIORITY_V_LABEL );
    yAxis.setMin( 0 );
    yAxis.setMax( 10 );
    
    ResponseDTO response = null;
    try {
      response = generalDelegate.findPriorityByClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.priorityList = ( List< PriorityDTO > ) response.getObjectResponse();
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.priorityList = new ArrayList< PriorityDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
    ChartSeries prioridad = new ChartSeries();
    for ( PriorityDTO priorityDTO: priorityList ) {
      long sum = 0;
      prioridad = new ChartSeries();
      prioridad.setLabel( priorityDTO.getDescription() );
      for ( TaskDTO taskDTO2: listRegister ) {
        if ( taskDTO2.getPriority().getId() == priorityDTO.getId() ) sum++;
      }
      prioridad.set( ConstantWEB.MYTASK_MY_PRIORITY_H_LABEL, sum );
      barModel.addSeries( prioridad );
    }
    
  }
  
  private void createPieModels () {
    createPieModel();
  }
  
  private void createPieModel () {
    try {
      pieModel = new PieChartModel();
      pieModel.setFill( true );
      pieModel.setShowDataLabels( true );
      //pieModel.setTitle( ConstantWEB.MYTASK_MY_PROGRESS_DIGRAM_TITLE );
      pieModel.setLegendPosition( "w" );
      pieModel.setMouseoverHighlight( true );
      pieModel.setSeriesColors( ConstantWEB.MYTASK_TASK_PIE_SERIESCOLORS );
      
      for ( Map.Entry< String, Integer > entry: progressTask.entrySet() ) {
        pieModel.set( entry.getKey(), entry.getValue() );
      }
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
  public void handleReorder ( DashboardReorderEvent event ) {
    FacesMessage message = new FacesMessage();
    message.setSeverity( FacesMessage.SEVERITY_INFO );
    message.setSummary( "Reordered: " + event.getWidgetId() );
    message.setDetail( "Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: "
                       + event.getSenderColumnIndex() );
                       
    addMessage( message );
  }
  
  public void handleClose ( CloseEvent event ) {
    FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'" );
    
    addMessage( message );
  }
  
  public void handleToggle ( ToggleEvent event ) {
    FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:"
                                                                                                                    + event.getVisibility().name() );
                                                                                                                    
    addMessage( message );
  }
  
  private void addMessage ( FacesMessage message ) {
    FacesContext.getCurrentInstance().addMessage( null, message );
  }
  
  public static UserPortalDTO getUserDTO () {
    return ( UserPortalDTO ) getObjectSession( ConstantWEB.DESC_USER_DTO );
  }
  
  public static Object getObjectSession ( String pKey ) {
    try {
      if ( FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null ) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get( pKey );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  public static void addMessageError ( String compId, String msg, String detail ) {
    FacesContext ctx = null;
    FacesMessage fmsg = null;
    try {
      ctx = FacesContext.getCurrentInstance();
      fmsg = new FacesMessage( FacesMessage.SEVERITY_ERROR, msg, detail );
      ctx.addMessage( compId, fmsg );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void addObjectSession ( Object obj, String key ) {
    try {
      if ( FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null ) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put( key, obj );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public void itemBarSelect ( ItemSelectEvent event ) {
    showViewDetail = true;
    int itemIndex = event.getSeriesIndex();
    int count = 0;
    String key = null;
    ResponseDTO response = null;
    List< ChartSeries > chartSeries = barModel.getSeries();
    for ( ChartSeries chartSerie: chartSeries ) {
      if ( count == itemIndex ) {
        key = chartSerie.getLabel();
        break;
      }
      count++;
    }
    
    /**Consultar tareas por prioridad seleccionada:**/
    response = generalDelegate.findTaskByPersonPriority( getUserDTO().getPerson().getId(), key );
    if ( UtilCommon.validateResponseSuccess( response ) ) {
      //Respuesta del servicio 
      this.reportTaskSelected = ( List< TaskDTO > ) response.getObjectResponse();
      if ( this.reportTaskSelected == null ) this.reportTaskSelected = new ArrayList< TaskDTO >();
    } else {
      //si no encontro registro para listar 
      if ( UtilCommon.validateResponseNoResult( response ) ) {
        this.reportTaskSelected = new ArrayList< TaskDTO >();
      }
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public void itemPieSelect ( ItemSelectEvent event ) {
    showViewDetail = true;
    int itemIndex = event.getItemIndex();
    int count = 0;
    String key = null;
    ResponseDTO response = new ResponseDTO();
    try {
      @SuppressWarnings ( { "rawtypes" } )
      Map< String, Integer > hmap = ( Map ) pieModel.getData();
      for ( Map.Entry< String, Integer > entry: hmap.entrySet() ) {
        key = entry.getKey();
        if ( count == itemIndex ) {
          if ( key.equals( ConstantWEB.MESSAGE_DES_RUN ) ) { // En curso
            response = generalDelegate.findTaskByPersonRun( getUserDTO().getPerson().getId(), getUserDTO().getPerson().getClient().getId() );
          } else if ( key.equals( ConstantWEB.MESSAGE_DES_EXPIRED ) ) { // Vencidas
            response = generalDelegate.findTaskByPersonExpired( getUserDTO().getPerson().getId(), getUserDTO().getPerson().getClient()
                                                                                                              .getClientCetus().getId() );
          } else if ( key.equals( ConstantWEB.MESSAGE_DES_NEXT_OVERCOME ) ) { // Proximas a vencer
            response = generalDelegate.findTaskByPersonNexOvercome( getUserDTO().getPerson().getId(), getUserDTO().getPerson().getClient()
                                                                                                                  .getClientCetus().getId() );
          } else if ( key.equals( ConstantWEB.MESSAGE_DES_FINISH ) ) { // Completadas
            response = generalDelegate.findTaskByPersonCompleted( getUserDTO().getPerson().getId(), getUserDTO().getPerson().getClient()
                                                                                                                .getClientCetus().getId() );
          }
          
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            //Respuesta del servicio 
            this.reportTaskSelected = ( List< TaskDTO > ) response.getObjectResponse();
            if ( this.reportTaskSelected == null ) this.reportTaskSelected = new ArrayList< TaskDTO >();
          } else {
            //si no encontro registro para listar 
            if ( UtilCommon.validateResponseNoResult( response ) ) {
              this.reportTaskSelected = new ArrayList< TaskDTO >();
            }
            
          }
          count = 0;
          break;
        }
        count++;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void closeDialogView () {
    selectedObject = new TaskDTO();
    selectedObject.setArea( new AreaDTO() );
    selectedObject.setPriority( new PriorityDTO() );
    selectedObject.setStatus( new StatusDTO() );
    selectedObject.setTaskType( new TaskTypeDTO() );
  }
  
  public List< TaskDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< TaskDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public TaskDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( TaskDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  public BarChartModel getBarModel () {
    return barModel;
  }
  
  public void setBarModel ( BarChartModel barModel ) {
    this.barModel = barModel;
  }
  
  public DashboardModel getModel () {
    return model;
  }
  
  public void setModel ( DashboardModel model ) {
    this.model = model;
  }
  
  public Map< String, Integer > getProgressTask () {
    return progressTask;
  }
  
  public void setProgressTask ( Map< String, Integer > progressTask ) {
    this.progressTask = progressTask;
  }
  
  public PieChartModel getPieModel () {
    return pieModel;
  }
  
  public void setPieModel ( PieChartModel pieModel ) {
    this.pieModel = pieModel;
  }
  
  public List< TaskDTO > getReportTaskSelected () {
    return reportTaskSelected;
  }
  
  public void setReportTaskSelected ( List< TaskDTO > reportTaskSelected ) {
    this.reportTaskSelected = reportTaskSelected;
  }
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public long getPrioritySelected () {
    return prioritySelected;
  }
  
  public void setPrioritySelected ( long prioritySelected ) {
    this.prioritySelected = prioritySelected;
  }
  
}
