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
  
  @EJB
  private TimerExpirationTasks       timerExpirationTasks;
  
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
            if ( timer.getInfo().equals( "TimerControllerForWarning-0" ) || timer.getInfo().equals( "TimerControllerForWarning-1" ) ) {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "************************************ Eliminando " + timer.getInfo()
                                                       + " ************************************" );
              timer.cancel();
            }
          }
        }
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Eliminando todos los timer con prefijo: " + ConstantEJB.NAME_TIMER_BEFORE_EXPIRATION_TASKS );
        timerBeforeExpirationTasks.stopAllTimer();
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Eliminando todos los timer con prefijo: " + ConstantEJB.NAME_TIMER_EXPIRATION_TASKS );
        timerExpirationTasks.stopAllTimer();
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "************************************ Creando TimerControllerForWarning-0 ************************************" );
        timerService.createCalendarTimer( new ScheduleExpression().timezone( ConstantEJB.TIME_ZONE ).hour( "*" )
                                                                  .minute( "*/" + ConstantEJB.TIME_EXECUTE_TIMER_CONTROLLER_0 ),
                                          new TimerConfig( "TimerControllerForWarning-0", true ) );
      } else {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "TimerControllerForWarning no habilitado " );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Error iniciando TimerControllerForWarning " + e.getMessage() );
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
          if ( timerBeforeExpirationTasks.existsTimerRunning( nameTimerBefore ) ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer [" + nameTimerBefore
                                                     + "] esta en ejecucion, se procede a validar si se puede detener " );
            if ( validateTimerStopBefore( clientCetus.getId() ) ) {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Se procede a detener el timer [" + nameTimerBefore + "]" );
              timerBeforeExpirationTasks.stopTimer( nameTimerBefore );;
            }
          } else {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer [" + nameTimerBefore + "] NO esta en ejecucion, se procede crearlo " );
            startTimerBeforeExpirationTasks( nameTimerBefore, clientCetus.getId() );
          }
          // Fin Control TimerBeforeExpirationTasks
          
          // Inicio Control TimerExpirationTasks
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Validando el TimerExpirationTasks del cliente cetus [" + clientCetus.getId() + "]" );
          nameTimerBefore = ConstantEJB.NAME_TIMER_EXPIRATION_TASKS + clientCetus.getId();
          if ( timerExpirationTasks.existsTimerRunning( nameTimerBefore ) ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer [" + nameTimerBefore
                                                     + "] esta en ejecucion, se procede a validar si se puede detener " );
            if ( validateTimerStopExpiration( clientCetus.getId() ) ) {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Se procede a detener el timer [" + nameTimerBefore + "]" );
              timerExpirationTasks.stopTimer( nameTimerBefore );
            }
          } else {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer [" + nameTimerBefore + "] NO esta en ejecucion, se procede crearlo " );
              startTimerExpirationTasks( nameTimerBefore, clientCetus.getId() );
          }
          // Fin Control TimerBeforeExpirationTasks
          
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    } finally {
      startTimerControllerPrincipal();
    }
    ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "********************* Fin ejecucion TimerController *********************" );
  }
  
  /**
   * </p> Start timer controller principal. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (25/11/2015)
   */
  private void startTimerControllerPrincipal () {
    try {
      if ( timerService.getTimers() != null ) {
        for ( Timer timer0: timerService.getTimers() ) {
          if ( timer0.getInfo().equals( "TimerControllerForWarning-0" ) ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Eliminando Timer TimerControllerForWarning-0..." );
            timer0.cancel();
            
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Creando Timer de control TimerControllerForWarningPrincipal..." );
            String executeTime = cetusControlProcess.getValueParameter( ConstantEJB.TIME_HOUR_EXECUTE_TIMER_CONTROLLER );
            timerService.createCalendarTimer( new ScheduleExpression().timezone( ConstantEJB.TIME_ZONE ).hour( "*/" + executeTime ),
                                              new TimerConfig( "TimerControllerForWarningPrincipal", true ) );
          }
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate timer stop. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (26/07/2015)
   */
  private boolean validateTimerStopBefore ( int idClientCetus ) {
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
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus + "] fecha actual del sistema = " + currentCal.getTime() );
        
        if ( currentCal.compareTo( cal ) > 0 ) {
          result = true;
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  private void startTimerBeforeExpirationTasks ( String nameTimer, int idClientCetus ) {
    Calendar calMin = Calendar.getInstance();
    Calendar calMax = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    long max = 0;
    long min = 0;
    String weekDay = null;
    long timeBeforeExpiration = 0;
    Double timeMin = 0.0;
    String rangeHours = "*";
    String minutesForExecute = "*/";
    try {
      weekDay = EWeekDay.getValue( currentCal.get( Calendar.DAY_OF_WEEK ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] obteniendo jornada minima. idClientCetus=" + idClientCetus
                                               + ", weekDay=" + weekDay );
      min = timerProcess.findJornadaMin( idClientCetus, weekDay );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] min = " + min );
      if ( min == -1 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "No existe jornada par el dia " + weekDay + ", por lo tanto no se crea el timer " + nameTimer );
      } else {
        max = timerProcess.findJornadaMax( idClientCetus, weekDay );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] max = " + max );
        
        calMin.set( Calendar.HOUR_OF_DAY, ( int ) ( min / 100 ) );
        calMin.set( Calendar.MINUTE, ( int ) ( min % 100 ) );
        calMin.set( Calendar.SECOND, 0 );
        
        timeBeforeExpiration = timerProcess.findTimeBeforeExpiration( idClientCetus );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] timeBeforeExpiration = " + timeBeforeExpiration );
        
        if ( timeBeforeExpiration > 0 ) {
          timeMin = timeBeforeExpiration * ConstantEJB.ADITIONAL_TIME_BEFORE_EXPIRATION;
          calMin.add( Calendar.MINUTE, ( -timeMin.intValue() ) );
        }
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] fecha inicio timer = " + calMin.getTime() );
        
        calMax.set( Calendar.HOUR_OF_DAY, ( int ) ( max / 100 ) );
        calMax.set( Calendar.MINUTE, ( int ) ( max % 100 ) );
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
  
  /**
   * </p> Validate timer stop expiration. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (25/01/2016)
   */
  private boolean validateTimerStopExpiration ( int idClientCetus ) {
    boolean result = false;
    Calendar cal = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    long max = 0;
    long timeAfterExpiration = 0;
    Double aditionalTime = 0.0;
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
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus + "] fecha actual del sistema = " + currentCal.getTime() );
        
        if ( currentCal.compareTo( cal ) > 0 ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus
                                                   + "] se tendra en cuenta 1.5 unidades mas del TIME_AFTER_EXPIRATION para detener el timer" );
          timeAfterExpiration = timerProcess.findTimeAfterExpiration( idClientCetus );
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus + "] timeAfterExpiration = " + timeAfterExpiration );
          if ( timeAfterExpiration > 0 ) {
            aditionalTime = timeAfterExpiration * ConstantEJB.ADITIONAL_TIME_EXPIRATION_TASK;
            cal.add( Calendar.MINUTE, aditionalTime.intValue() );
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[idClientCetus = " + idClientCetus + "] fecha maxima del timer = " + cal.getTime() );
          }
          
          if ( currentCal.compareTo( cal ) > 0 ) {
            result = true;
          }
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Start timer expiration tasks. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameTimer the name timer
   * @param idClientCetus the id client cetus
   * @since CetusControlEJB (26/01/2016)
   */
  private void startTimerExpirationTasks ( String nameTimer, int idClientCetus ) {
    Calendar calMin = Calendar.getInstance();
    Calendar calMax = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    long max = 0;
    long min = 0;
    String weekDay = null;
    long timeExpiration = 0;
    Double aditionalTime = 0.0;
    String rangeHours = "*";
    String minutesForExecute = "*/";
    try {
      weekDay = EWeekDay.getValue( currentCal.get( Calendar.DAY_OF_WEEK ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] obteniendo jornada minima. idClientCetus=" + idClientCetus
                                               + ", weekDay=" + weekDay );
      min = timerProcess.findJornadaMin( idClientCetus, weekDay );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] min = " + min );
      if ( min == -1 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "No existe jornada par el dia " + weekDay + ", por lo tanto no se crea el timer " + nameTimer );
      } else {
        max = timerProcess.findJornadaMax( idClientCetus, weekDay );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] max = " + max );
        
        calMin.set( Calendar.HOUR_OF_DAY, ( int ) ( min / 100 ) );
        calMin.set( Calendar.MINUTE, ( int ) ( min % 100 ) );
        calMin.set( Calendar.SECOND, 0 );
        
        timeExpiration = timerProcess.findTimeAfterExpiration( idClientCetus );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] timeExpiration = " + timeExpiration );
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] fecha inicio timer = " + calMin.getTime() );
        
        calMax.set( Calendar.HOUR_OF_DAY, ( int ) ( max / 100 ) );
        calMax.set( Calendar.MINUTE, (int) ( max % 100 ) );
        calMax.set( Calendar.SECOND, 0 );
        
        
        if ( timeExpiration > 0 ) {
          aditionalTime = timeExpiration * ConstantEJB.ADITIONAL_TIME_BEFORE_EXPIRATION;
          calMax.add( Calendar.MINUTE, ( aditionalTime.intValue() ) );
        }
        
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
