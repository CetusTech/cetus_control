package co.com.cetus.cetuscontrol.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.extensions.event.timeline.TimelineSelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;

import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TraceTaskDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

@ManagedBean
@RequestScoped
public class BasicTimelineController extends GeneralManagedBean implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
                                             
  private TimelineModel     model;
                            
  private boolean           selectable       = true;
  private boolean           zoomable         = true;
  private boolean           moveable         = true;
  private boolean           stackEvents      = true;
  private String            eventStyle       = "box";
  private boolean           axisOnTop;
  private boolean           showCurrentTime  = true;
  private boolean           showNavigation   = false;
  private TimelineEvent     timelineEvent    = null;
  private boolean           flag             = false;
                                             
  @SuppressWarnings ( "unchecked" )
  @PostConstruct
  protected void initialize () {
    List< TraceTaskDTO > list = null;
    TraceTaskDTO tmpCreated = new TraceTaskDTO();
    TraceTaskDTO tmpClosed = new TraceTaskDTO();
    try {
      TaskDTO task = ( TaskDTO ) getObjectSession( "selectedObject" );
      if ( task != null ) {
        model = new TimelineModel();
        Calendar cal = Calendar.getInstance();
        //Al tener la tarea se debe consultar la trace task
        ResponseDTO response = null;
        response = generalDelegate.findAllTraceTaskByTaskId( task.getId() );
        
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          list = ( ( List< TraceTaskDTO > ) response.getObjectResponse() );
        } else {
          list = new ArrayList< TraceTaskDTO >();
        }
        
        if ( !list.isEmpty() ) {
          
          tmpCreated.setId( 0 );
          tmpCreated.setCreationDate( task.getCreationDate() );
          tmpCreated.setNote( "Estado de la creación de la orden" );
          tmpCreated.setStatus( new StatusDTO() );
          tmpCreated.getStatus().setId( 0 );
          tmpCreated.getStatus().setDescription( "CREADA" );
          model.add( new TimelineEvent( tmpCreated, task.getAssingDate() ) );
          for ( TraceTaskDTO traceTaskDTO: list ) {
            Date temp = traceTaskDTO.getCreationDate();
            cal.setTime( temp );
            cal.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ), cal.get( Calendar.DAY_OF_MONTH ), cal.get( Calendar.HOUR_OF_DAY ),
                     cal.get( Calendar.MINUTE ), cal.get( Calendar.SECOND ) );
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              model.add( new TimelineEvent( traceTaskDTO, cal.getTime(), false ) );
            }
          }
          tmpClosed.setId( 0 );
          tmpClosed.setCreationDate( new Date() );
          tmpClosed.setNote( "Estado Actual de la Tarea" );
          tmpClosed.setStatus( new StatusDTO() );
          tmpClosed.getStatus().setId( task.getStatus().getId() );
          tmpClosed.getStatus().setDescription( task.getStatus().getDescription() );
          if ( task.getStatus().getId() == ConstantWEB.STATUS_COMPLETED_VAL ) {
            model.add( new TimelineEvent( tmpClosed, task.getDateEnd() ) );
          } else {
            model.add( new TimelineEvent( tmpClosed, new Date() ) );
          }
          addObjectSession( model, "model" );
        }
      }
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
  public void onSelect ( TimelineSelectEvent e ) {
    TimelineEvent timelineEvent = e.getTimelineEvent();
    if ( timelineEvent != null ) {
      flag = true;
      addObjectSession( timelineEvent, "timelineEvent" );
    }
  }
  
  public TimelineModel getModel () {
    model = ( getObjectSession( "model" ) != null ? ( TimelineModel ) getObjectSession( "model" ) : null );
    return model;
  }
  
  public boolean isSelectable () {
    return selectable;
  }
  
  public void setSelectable ( boolean selectable ) {
    this.selectable = selectable;
  }
  
  public boolean isZoomable () {
    return zoomable;
  }
  
  public void setZoomable ( boolean zoomable ) {
    this.zoomable = zoomable;
  }
  
  public boolean isMoveable () {
    return moveable;
  }
  
  public void setMoveable ( boolean moveable ) {
    this.moveable = moveable;
  }
  
  public boolean isStackEvents () {
    return stackEvents;
  }
  
  public void setStackEvents ( boolean stackEvents ) {
    this.stackEvents = stackEvents;
  }
  
  public String getEventStyle () {
    return eventStyle;
  }
  
  public void setEventStyle ( String eventStyle ) {
    this.eventStyle = eventStyle;
  }
  
  public boolean isAxisOnTop () {
    return axisOnTop;
  }
  
  public void setAxisOnTop ( boolean axisOnTop ) {
    this.axisOnTop = axisOnTop;
  }
  
  public boolean isShowCurrentTime () {
    return showCurrentTime;
  }
  
  public void setShowCurrentTime ( boolean showCurrentTime ) {
    this.showCurrentTime = showCurrentTime;
  }
  
  public boolean isShowNavigation () {
    return showNavigation;
  }
  
  public void setShowNavigation ( boolean showNavigation ) {
    this.showNavigation = showNavigation;
  }
  
  @Override
  public void initElement () {
    // TODO Auto-generated method stub
    
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
  
  public TimelineEvent getTimelineEvent () {
    timelineEvent = ( getObjectSession( "timelineEvent" ) != null ? ( TimelineEvent ) getObjectSession( "timelineEvent" ) : null );
    return timelineEvent;
  }
  
  public void setTimelineEvent ( TimelineEvent timelineEvent ) {
    this.timelineEvent = timelineEvent;
  }
  
  public boolean isFlag () {
    return flag;
  }
  
  public void setFlag ( boolean flag ) {
    this.flag = flag;
  }
  
}
