package co.com.cetus.cetuscontrol.ejb.process;

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
import co.com.cetus.cetuscontrol.jpa.entity.NotificationTask;
import co.com.cetus.cetuscontrol.jpa.entity.Task;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.ResponseWSDTO;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

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
    List< Integer > listTask = null;
    String idClientCetus = null;
    String nameTimer = ( String ) timer.getInfo();
    long timeBefore = 0;
    String[] infoNotification = null;
    String wsdlMessageService = null;
    SendMailRequestDTO sendMailRequestDTO = null;
    ResponseWSDTO responseWSDTO = null;
    boolean respCreate = false;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "--------------- INICIA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date()
                                               + " ---------------" );
      
      idClientCetus = nameTimer.substring( ConstantEJB.NAME_TIMER_BEFORE_EXPIRATION_TASKS.length() );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] idClientCetus=" + idClientCetus );
      timeBefore = timerProcess.findTimeBeforeExpiration( Integer.parseInt( idClientCetus ) );
      listTask = timerProcess.findIdTaskBeforeExpiration( Integer.parseInt( idClientCetus ), new Integer( ( int ) timeBefore ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] timeBefore=" + timeBefore + ", listTask=" + listTask );
      if ( listTask != null && listTask.size() > 0 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia la construccion del objeto para enviar el correo " );
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
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia envio de correo para cada tarea " + listTask );
        for ( Integer idTask: listTask ) {
          
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Se procede a enviar una alerta (proxima a vencer) para la tarea "
                                                   + idTask );
          infoNotification = timerProcess.getInformationForNotification( idTask );
          if ( infoNotification != null && infoNotification.length > 0 ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Informacion para la notificacion de la tarea : "
                                                     + Arrays.toString( infoNotification ) );
            
            sendMailRequestDTO.setRecipients( new String[]{ infoNotification[2] } );
            sendMailRequestDTO.setParametersTemplateHTML( new String[]{ infoNotification[0], infoNotification[1] } );
            
            responseWSDTO = messageServiceDelegate.sendEmail( sendMailRequestDTO );
            
            if ( responseWSDTO != null ) {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Respuesta del envio de la notificacion de la tarea : " + idTask
                                                       + ", " + responseWSDTO.toString() );
              if ( responseWSDTO.getCode() != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
                NotificationTask notificationTask = new NotificationTask();
                notificationTask.setCreationDate( new Date() );
                notificationTask.setCreationUser( nameTimer );
                notificationTask.setEvent( ConstantEJB.EVENT_BEFORE_EXPIRATION );
                notificationTask.setSent( 1 );
                notificationTask.setTask( new Task() );
                notificationTask.getTask().setId( Integer.parseInt( infoNotification[0] ) );
                notificationTask.setTaskDeliveryDate( new Date( Long.parseLong( infoNotification[3] ) ) );
                respCreate = timerProcess.createNotificationTable( notificationTask );
                
                ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Respuesta de la creacion del registro de notificacion : "
                                                         + respCreate );
                
              }
            }
          }
        }
      }
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "--------------- FINALIZA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date()
                                               + " ---------------" );
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
  
  public void stopAllTimer () {
    try {
      if ( timerService.getTimers() != null ) {
        for ( Timer timer: timerService.getTimers() ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Deteniendo el timer [" + timer.getInfo() + "]" );
          timer.cancel();
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
}
