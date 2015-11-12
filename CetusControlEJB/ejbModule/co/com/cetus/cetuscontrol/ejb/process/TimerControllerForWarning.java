package co.com.cetus.cetuscontrol.ejb.process;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.ejb.util.EWeekDay;
import co.com.cetus.cetuscontrol.jpa.entity.ClientCetus;

/**
 * Session Bean implementation class TimerControllerForWarning.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (26/07/2015)
 */
@Singleton
@DependsOn ( "TimerProcess" )
@Startup
public class TimerControllerForWarning {
  
  /** The timer service. */
  @Resource
  TimerService                       timerService;
  
  @EJB
  private TimerProcess               timerProcess;
  
  @EJB
  private TimerBeforeExpirationTasks timerBeforeExpirationTasks;
  
  @EJB
  CetusControlProcess                cetusControlProcess;
  
  
  /**
   * </p> Instancia un nuevo timer controller. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (26/07/2015)
   */
  public TimerControllerForWarning () {
  }
  
  /**
   * </p> Start timer. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (26/07/2015)
   */
  @PostConstruct
  private void startTimer () {
    try {
      if ( ConstantEJB.ENABLED_TIMER_CONTROLLER ) {
        if ( timerService.getTimers() != null ) {
          for ( Timer timer: timerService.getTimers() ) {
            if ( timer.getInfo().equals( "TimerControllerForWarning" ) ) {
              System.out.println( "************************************ Eliminando TimerControllerForWarning... ************************************" );
              timer.cancel();
            }
          }
        }
        
        System.out.println( "************************************ Creando TimerControllerForWarning... ************************************" );
        timerService.createCalendarTimer( new ScheduleExpression().timezone( ConstantEJB.TIME_ZONE ).hour( "*" )
                                                                  .minute( "*/" + ConstantEJB.TIME_HOUR_EXECUTE_TIMER_CONTROLLER ),
                                          new TimerConfig( "TimerControllerForWarning", true ) );
      } else {
        System.out.println( "TimerControllerForWarning no habilitado " );
      }
    } catch ( Exception e ) {
      System.err.println( "Error iniciando TimerControllerForWarning " + e.getMessage() );
    }
  }
  
  /**
   * </p> Execute process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param timer the timer
   * @since CetusControlEJB (26/07/2015)
   */
  @Timeout
  public void executeProcess ( Timer timer ) {
    List< ClientCetus > list = null;
    String nameTimerBefore = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "********************* Ejecutando proceso del timer TimerController *********************" );
      list = timerProcess.findClientCetusEnabledTimer();
      if ( list != null && list.size() > 0 ) {
        for ( ClientCetus clientCetus: list ) {
          // Inicio Control TimerBeforeExpirationTasks
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Validando el TimerBeforeExpirationTasks del cliente cetus [" + clientCetus.getId() + "]" );
          nameTimerBefore = ConstantEJB.NAME_TIMER_BEFORE_EXPIRATION_TASKS + clientCetus.getId();
//          if ( timerBeforeExpirationTasks.existsTimerRunning( nameTimerBefore ) ) {
//            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer [" + nameTimerBefore
//                                                     + "] esta en ejecucion, se procede a validar si se puede detener " );
//            if ( validateTimerStop( clientCetus.getId() ) ) {
//              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Se procede a detener el timer [" + nameTimerBefore + "]" );
//              timerBeforeExpirationTasks.stopTimer( nameTimerBefore );;
//            }
//          } else {
//            startTimerBeforeExpirationTasks( nameTimerBefore, clientCetus.getId() );;
//          }
          // Fin Control TimerBeforeExpirationTasks
          
          if( !timerBeforeExpirationTasks.existsTimerRunning( nameTimerBefore ) )
            timerBeforeExpirationTasks.startTimer( nameTimerBefore, "*", "*" );
          
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "********************* ************************* *********************" );
  }
  
  /**
   * </p> Validate timer stop. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (26/07/2015)
   */
  private boolean validateTimerStop ( long idClientCetus ) {
    boolean result = false;
    Calendar cal = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    long max = 0;
    try {
      max = timerProcess.findJornadaMax( idClientCetus, EWeekDay.getValue( cal.get( Calendar.DAY_OF_WEEK ) ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus + "] max = " + max );
      if ( max == -1 ) {
        result = true;
      } else {
        cal.set( Calendar.HOUR_OF_DAY, ( int ) ( max / 100 ) );
        cal.set( Calendar.MINUTE, ( int ) ( max % 100 ) );
        cal.set( Calendar.SECOND, 0 );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus + "] fecha maxima de la jornada = " + cal.getTime() );
        
        currentCal.set( Calendar.SECOND, 0 );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus + "] fecha actula del sistema = " + currentCal.getTime() );
        
        if ( currentCal.compareTo( cal ) > 0 ) {
          result = true;
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  private void startTimerBeforeExpirationTasks ( String nameTimer, long idClientCetus ) {
    Calendar calMin = Calendar.getInstance();
    Calendar calMax = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    long max = 0;
    long min = 0;
    String weekDay = null;
    long timeBeforeExpiration = 0;
    long timeHours = 0;
    String rangeHours = "*";
    String minutesForExecute = "*/";
    try {
      weekDay = EWeekDay.getValue( currentCal.get( Calendar.DAY_OF_WEEK ) );
      min = timerProcess.findJornadaMin( idClientCetus, weekDay );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] min = " + min );
      if ( min == -1 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "No existe jornada par el dia " + weekDay + ", por lo tanto no se crea el timer " + nameTimer );
      } else {
        max = timerProcess.findJornadaMax( idClientCetus, weekDay );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] max = " + max );
        
        timeBeforeExpiration = timerProcess.findTimeBeforeExpiration( idClientCetus );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] timeBeforeExpiration = " + timeBeforeExpiration );
        
        timeHours = timeBeforeExpiration / 60;
        if ( ( timeBeforeExpiration % 60 ) > 0 ) {
          timeHours++;
        }
        min = ( min / 100 ) - timeHours;
        if ( min < 0 ) {
          min = 0;
        }
        
        calMin.set( Calendar.HOUR_OF_DAY, ( int ) min );
        calMin.set( Calendar.MINUTE, 0 );
        calMin.set( Calendar.SECOND, 0 );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] fecha inicio timer = " + calMin.getTime() );
        
        max = ( max / 100 );
        if( max % 100 > 0 ){
          max++;  
        }
        
        
        calMax.set( Calendar.HOUR_OF_DAY, ( int ) max );
        calMax.set( Calendar.MINUTE, 0 );
        calMax.set( Calendar.SECOND, 0 );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] fecha fin timer = " + calMax.getTime() );
        
        currentCal.set( Calendar.SECOND, 0 );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] fecha actula del sistema = " + currentCal.getTime() );
        
        minutesForExecute += cetusControlProcess.getValueParameter( ConstantEJB.MINUTE_FOR_EXECUTE_TIMER_BEFORE );
        
        if ( calMin.before( currentCal ) && calMax.after( currentCal ) ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] rangeHours = " + rangeHours + ", minutesForExecute = "
                                                   + minutesForExecute );
          timerBeforeExpirationTasks.startTimer( nameTimer, rangeHours, minutesForExecute );
        } else if ( calMin.after( currentCal ) ) {
          rangeHours = min + "-" + max;
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] rangeHours = " + rangeHours + ", minutesForExecute = "
                                                   + minutesForExecute );
          timerBeforeExpirationTasks.startTimer( nameTimer, rangeHours, minutesForExecute );
        } else {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] El timer " + nameTimer + " ya termino la ejecucion en este dia" );
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
}
