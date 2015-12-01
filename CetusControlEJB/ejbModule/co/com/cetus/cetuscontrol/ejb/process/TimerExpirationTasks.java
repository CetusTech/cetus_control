package co.com.cetus.cetuscontrol.ejb.process;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class TimerExpirationTasks.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (1/12/2015)
 */
public class TimerExpirationTasks {
  
  @Resource
  TimerService         timerService;
  
  @EJB
  private TimerProcess timerProcess;
  
  public TimerExpirationTasks () {
  }
  
  public void startTimer ( String nameTimer, String rangeHours, String minutesForExecute ) {
    try {
      timerService.createCalendarTimer( new ScheduleExpression().hour( rangeHours ).minute( minutesForExecute ),
                                        new TimerConfig( nameTimer, true ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Se creo exitosamente el timer [" + nameTimer + "], rangeHours=" + rangeHours + ", minutesForExecute="
                                               + minutesForExecute );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  public void stopTimer ( String nameTimer ) {
    try {
      if ( timerService.getTimers() != null ) {
        for ( Timer timer: timerService.getTimers() ) {
          if ( timer.getInfo().equals( nameTimer ) ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Deteniendo el timer [" + nameTimer + "]" );
            timer.cancel();
          }
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
  public boolean existsTimerRunning ( String nameTimer ) {
    boolean exists = false;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Validando si el timer " + nameTimer + " esta ejecutandose" );
      if ( !UtilCommon.stringNullOrEmpty( nameTimer ) && timerService.getTimers() != null ) {
        for ( Timer timer: timerService.getTimers() ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Validando timer " + timer.getInfo() );
          if ( timer.getInfo().equals( nameTimer ) ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "El timer " + nameTimer + ", esta ejecutandose...." );
            exists = true;
            break;
          }
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return exists;
  }
  
  @Timeout
  public void executeProcess ( Timer timer ) {
    //    List< Long > listTask = null;
    //    String idClientCetus = null;
    //    String nameTimer = ( String ) timer.getInfo();
    //    String event = ConstantEJB.EVENT_EXPIRATION;
    //    long notificationSent = 0;
    //    long timeBefore = 0;
    //    try {
    //      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "INICIA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date() );
    //      
    //      idClientCetus = nameTimer.substring( ConstantEJB.NAME_TIMER_EXPIRATION_TASKS.length() );
    //      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "[" + timer.getInfo() + "] idClientCetus=" + idClientCetus );
    //      timeBefore = timerProcess.findTimeBeforeExpiration(  Integer.parseInt( idClientCetus ) );
    //      listTask = timerProcess.findIdTaskBeforeExpiration( Long.parseLong( idClientCetus ), new Integer(( int ) timeBefore) );
    //      for ( Long idTask: listTask ) {
    //        notificationSent = 0;
    //        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] tarea a validar idTask=" + idTask );
    //        notificationSent = timerProcess.findNotificationSent( idTask, event );
    //        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Notificaciones enviadas para la tarea " + idTask + " " + notificationSent );
    //      }
    //      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "FINALIZA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date() );
    //    } catch ( Exception e ) {
    //      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    //    }
    
  }
  
}
