package co.com.cetus.cetuscontrol.ejb.process;

import java.net.HttpURLConnection;
import java.net.URL;
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
                                     
  @EJB
  private TimerNotificationProcess   timerNotificationProcess;
                                     
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
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Eliminando todos los timer con prefijo: " + ConstantEJB.NAME_TIMER_NOTIFICATION_PROCESS );
        timerNotificationProcess.stopAllTimer();
        
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
        
        // Inicio Control TimerNotificationProcess
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Validando el TimerNotificationProcess..." );
        if ( timerNotificationProcess.existsTimerRunning( ConstantEJB.NAME_TIMER_NOTIFICATION_PROCESS ) ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer [" + ConstantEJB.NAME_TIMER_NOTIFICATION_PROCESS
                                                   + "] esta en ejecucion, se procede a validar si se puede detener " );
          if ( validateTimerStopNotification() ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Se procede a detener el timer [" + nameTimerBefore + "]" );
            timerNotificationProcess.stopTimer( nameTimerBefore );;
          }
        } else {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer [" + nameTimerBefore + "] NO esta en ejecucion, se procede crearlo " );
          startTimerNotificationProcess();
        }
        // Fin Control TimerNotificationProcess
        
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    } finally {
      startTimerControllerPrincipal();
      sendRequestAccountOpenshift();
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
                                              new TimerConfig( "TimerControllerForWarning-1", true ) );
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
          
          min = (min/100) - (timeBeforeExpiration/60); 
          if( timeBeforeExpiration % 60 > 0 ){
            min--;
          }
          
          rangeHours = min + "-" + ( max / 100 );
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
        calMax.set( Calendar.MINUTE, ( int ) ( max % 100 ) );
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
          timerExpirationTasks.startTimer( nameTimer, rangeHours, minutesForExecute );
        } else if ( calMin.after( currentCal ) ) {
          rangeHours = ( min / 100 ) + "-" + ( max / 100 );
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] rangeHours = " + rangeHours + ", minutesForExecute = "
                                                   + minutesForExecute );
          timerExpirationTasks.startTimer( nameTimer, rangeHours, minutesForExecute );
        } else {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[nameTimer = " + nameTimer + "] El timer " + nameTimer + " ya termino la ejecucion en este dia" );
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate timer stop notification. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (12/03/2016)
   */
  private boolean validateTimerStopNotification () {
    boolean result = false;
    Calendar cal = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    int max = 0;
    try {
      try {
        max = Integer.parseInt( cetusControlProcess.getValueParameter( ConstantEJB.NOTIFICATION_HOUR_END_TIMER ) );
      } catch ( NumberFormatException e ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error obteniendo la hora maxima del timer de notificacion, se toma por defecto 18 horas" );
        max = 18;
      }
      
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "validateTimerStopNotification Max=" + max );
      
      cal.set( Calendar.HOUR_OF_DAY, max );
      cal.set( Calendar.MINUTE, 0 );
      cal.set( Calendar.SECOND, 0 );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Fecha maxima de ejecucion timer notificacion " + cal.getTime() );
      
      currentCal.set( Calendar.SECOND, 0 );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Fecha actual del sistema = " + currentCal.getTime() );
      
      if ( currentCal.compareTo( cal ) > 0 ) {
        result = true;
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
      result = true;
    }
    return result;
  }
  
  /**
   * </p> Start timer notification process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (12/03/2016)
   */
  private void startTimerNotificationProcess () {
    Calendar calMin = Calendar.getInstance();
    Calendar calMax = Calendar.getInstance();
    Calendar currentCal = Calendar.getInstance();
    int max = 0;
    int min = 0;
    String rangeHours = "*";
    String minutesForExecute = "*/";
    try {
      
      try {
        max = Integer.parseInt( cetusControlProcess.getValueParameter( ConstantEJB.NOTIFICATION_HOUR_END_TIMER ) );
      } catch ( NumberFormatException e ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error obteniendo la hora maxima del timer de notificacion, se toma por defecto 18 horas" );
        max = 18;
      }
      
      try {
        min = Integer.parseInt( cetusControlProcess.getValueParameter( ConstantEJB.NOTIFICATION_HOUR_START_TIMER ) );
      } catch ( NumberFormatException e ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "Error obteniendo la hora minima del timer de notificacion, se toma por defecto 8 horas" );
        min = 8;
      }
      
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Hora minima = " + min + ", Hora maxima = " + max );
      
      calMin.set( Calendar.HOUR_OF_DAY, min );
      calMin.set( Calendar.MINUTE, 0 );
      calMin.set( Calendar.SECOND, 0 );
      
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Fecha inicio timer = " + calMin.getTime() );
      
      calMax.set( Calendar.HOUR_OF_DAY, max );
      calMax.set( Calendar.MINUTE, 0 );
      calMax.set( Calendar.SECOND, 0 );
      
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Fecha fin timer = " + calMax.getTime() );
      
      currentCal.set( Calendar.SECOND, 0 );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Fecha actula del sistema = " + currentCal.getTime() );
      
      minutesForExecute += cetusControlProcess.getValueParameter( ConstantEJB.MINUTE_FOR_EXECUTE_TIMER_NOTIF );
      
      if ( calMin.before( currentCal ) && calMax.after( currentCal ) ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "rangeHours = " + rangeHours + ", minutesForExecute = " + minutesForExecute );
        timerNotificationProcess.startTimer( ConstantEJB.NAME_TIMER_NOTIFICATION_PROCESS, rangeHours, minutesForExecute );
      } else if ( calMin.after( currentCal ) ) {
        rangeHours = ( min ) + "-" + ( max );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "rangeHours = " + rangeHours + ", minutesForExecute = " + minutesForExecute );
        timerNotificationProcess.startTimer( ConstantEJB.NAME_TIMER_NOTIFICATION_PROCESS, rangeHours, minutesForExecute );
      } else {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer " + ConstantEJB.NAME_TIMER_NOTIFICATION_PROCESS + " ya termino la ejecucion en este dia" );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Send request account openshift. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (22/03/2016)
   */
  private void sendRequestAccountOpenshift () {
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "\n\nINICIA ENVIO DEL REQUEST A LA CUENTA EN OPENSHIFT" );
      
      String url = "http://cetusprep-cetustech.rhcloud.com/";
      
      URL obj = new URL( url );
      HttpURLConnection con = ( HttpURLConnection ) obj.openConnection();

      con.setRequestMethod( "GET" );
      
      int responseCode = con.getResponseCode();
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Sending 'GET' request to URL : " + url );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Response Code : " + responseCode );

      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "FINALIZA ENVIO DEL REQUEST A LA CUENTA EN OPENSHIFT\n\n" );
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
}
