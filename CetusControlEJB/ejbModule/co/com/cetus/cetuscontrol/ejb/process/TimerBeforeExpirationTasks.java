package co.com.cetus.cetuscontrol.ejb.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import co.com.cetus.cetuscontrol.ejb.delegate.CetusMessageServiceDelegate;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.ResponseWSDTO;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;
import co.com.cetus.messageservice.ejb.service.UserWSDTO;

/**
 * The Class TimerBeforeExpirationTasks.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (26/07/2015)
 */
@Singleton
@Startup
public class TimerBeforeExpirationTasks {
  
  /** The timer service. */
  @Resource
  TimerService         timerService;
  
  @EJB
  private TimerProcess timerProcess;
  
  @EJB
  CetusControlProcess  cetusControlProcess;
  
  /**
   * </p> Instancia un nuevo timer before expiration tasks. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (26/07/2015)
   */
  public TimerBeforeExpirationTasks () {
  }
  
  /**
   * </p> Start timer. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameTimer the name timer
   * @since CetusControlEJB (26/07/2015)
   */
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
  
  /**
   * </p> Stop timer. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameTimer the name timer
   * @since CetusControlEJB (26/07/2015)
   */
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
  
  /**
   * </p> Execute process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param timer the timer
   * @since CetusControlEJB (26/07/2015)
   */
  @Timeout
  public void executeProcess ( Timer timer ) {
    List< Long > listTask = null;
    String idClientCetus = null;
    String nameTimer = ( String ) timer.getInfo();
    String event = ConstantEJB.EVENT_BEFORE_EXPIRATION;
    long notificationSent = 0;
    long timeBefore = 0;
    String[] infoNotification = null;
    String wsdlMessageService = null;
    SendMailRequestDTO sendMailRequestDTO = null;
    List< String > listRecipients = null;
    List< String > listParameters = null;
    ResponseWSDTO responseWSDTO = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "INICIA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date() );
      
      idClientCetus = nameTimer.substring( ConstantEJB.NAME_TIMER_BEFORE_EXPIRATION_TASKS.length() );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "[" + timer.getInfo() + "] idClientCetus=" + idClientCetus );
      timeBefore = timerProcess.findTimeBeforeExpiration( Long.parseLong( idClientCetus ) );
      listTask = timerProcess.findIdTaskBeforeExpiration( Long.parseLong( idClientCetus ), new Integer( ( int ) timeBefore ) );
      if ( listTask != null && listTask.size() > 0 ) {
        wsdlMessageService = cetusControlProcess.getValueParameter( ConstantEJB.WSDL_CETUS_MESSAGE_SERVICE );
        
        sendMailRequestDTO = new SendMailRequestDTO();
        sendMailRequestDTO.setUser( cetusControlProcess.getValueParameter( ConstantEJB.USER_WS_MESSAGE_SERVICE ) );
        sendMailRequestDTO.setPassword( cetusControlProcess.getValueParameter( ConstantEJB.PASSWORD_WS_MESSAGE_SERVICE ) );
        
        sendMailRequestDTO.setNameTemplateHTML( ConstantEJB.TEMPLATE_EMAIL_BEFORE_EXPIRATION );
        sendMailRequestDTO.setSenderEmail( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_FROM ) );
        sendMailRequestDTO.setSenderName( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_USERNAME ) );
        sendMailRequestDTO.setSenderPassword( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_PASS ) );
        sendMailRequestDTO.setServerPort( cetusControlProcess.getValueParameter( ConstantEJB.SMPT_PORT ) );
        sendMailRequestDTO.setServerSmtp( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_HOST ) );
        sendMailRequestDTO.setSubject( cetusControlProcess.getValueParameter( ConstantEJB.SUBJECT_BEFORE_EXPIRATION ) );
        
        CetusMessageServiceDelegate messageServiceDelegate = new CetusMessageServiceDelegate( wsdlMessageService );
        
        for ( Long idTask: listTask ) {
          notificationSent = 0;
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] tarea a validar idTask=" + idTask );
          notificationSent = timerProcess.findNotificationSent( idTask, event );
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Notificaciones enviadas para la tarea " + idTask + " = "
                                                   + notificationSent );
          
          if ( notificationSent == 0 ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Se procede a enviar una alerta (proxima a vencer) para la tarea "
                                                     + idTask );
            infoNotification = timerProcess.getInformationForNotification( idTask );
            if ( infoNotification != null && infoNotification.length > 0 ) {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Informacion para la notificacion de la tarea : "
                                                       + Arrays.toString( infoNotification ) );

              sendMailRequestDTO.setRecipients( infoNotification[2] );
              sendMailRequestDTO.setParametersTemplateHTML( infoNotification[0]+","+infoNotification[1] );
              
              responseWSDTO = messageServiceDelegate.sendEmail( sendMailRequestDTO );
              
              if ( responseWSDTO != null ) {
                ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Respuesta del envio de la notificacion de la tarea : " + idTask
                                                         + ", " + responseWSDTO.toString() );
              }
            }
          } else {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Ya fue alertada la tarea " + idTask
                                                     + " con el evento proxima a vencer" );
          }
        }
      }
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "FINALIZA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date() );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Exists timer running. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameTimer the name timer
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (26/07/2015)
   */
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
  
}
