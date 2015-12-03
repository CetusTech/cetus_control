package co.com.cetus.cetuscontrol.ejb.process;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import co.com.cetus.cetuscontrol.ejb.delegate.CetusMessageServiceDelegate;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationTask;
import co.com.cetus.cetuscontrol.jpa.entity.Task;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.ResponseWSDTO;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

/**
 * The Class ThreadExpirationTasks.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (3/12/2015)
 */
public class ThreadExpirationTasks extends Thread {
  
  /** The timer process. */
  private TimerProcess        timerProcess;
  
  /** The cetus control process. */
  private CetusControlProcess cetusControlProcess;
  
  /** The list task. */
  private List< Integer >     listTask = null;
  
  /**
   * </p> Instancia un nuevo thread expiration tasks. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param listTask the list task
   * @since CetusControlEJB (3/12/2015)
   */
  public ThreadExpirationTasks ( List< Integer > listTask ) {
    try {
      this.listTask = listTask;
      timerProcess = UtilCommon.getLookup( ConstantEJB.CONTEXT_TIMER_PROCESS );
      cetusControlProcess = UtilCommon.getLookup( ConstantEJB.CONTEXT_CETUS_CONTROL_PROCESS );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Thread#run()
   */
  @Override
  public void run () {
    String[] infoNotification = null;
    String wsdlMessageService = null;
    SendMailRequestDTO sendMailRequestDTO = null;
    ResponseWSDTO responseWSDTO = null;
    boolean respNotification = false;
    NotificationTask notificationTask = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "////////////// INICIA LA EJECUCION DEL HILO [" + this.getName() + "] //////////////" );
      if ( listTask != null && listTask.size() > 0 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] listTask= " + listTask );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Inicia la construccion del objeto para enviar el correo " );
        wsdlMessageService = cetusControlProcess.getValueParameter( ConstantEJB.WSDL_CETUS_MESSAGE_SERVICE );
        sendMailRequestDTO = new SendMailRequestDTO();
        sendMailRequestDTO.setUser( cetusControlProcess.getValueParameter( ConstantEJB.USER_WS_MESSAGE_SERVICE ) );
        sendMailRequestDTO.setPassword( cetusControlProcess.getValueParameter( ConstantEJB.PASSWORD_WS_MESSAGE_SERVICE ) );
        sendMailRequestDTO.setNameTemplateHTML( ConstantEJB.TEMPLATE_EMAIL_EXPIRATION );
        sendMailRequestDTO.setSenderEmail( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_FROM ) );
        sendMailRequestDTO.setSenderName( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_USERNAME ) );
        sendMailRequestDTO.setSenderPassword( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_PASS ) );
        sendMailRequestDTO.setServerPort( cetusControlProcess.getValueParameter( ConstantEJB.SMPT_PORT ) );
        sendMailRequestDTO.setServerSmtp( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_HOST ) );
        sendMailRequestDTO.setSubject( cetusControlProcess.getValueParameter( ConstantEJB.SUBJECT_EXPIRATION_TASK ) );
        
        CetusMessageServiceDelegate messageServiceDelegate = new CetusMessageServiceDelegate( wsdlMessageService );
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Inicia envio de correo para cada tarea " );
        for ( Integer idTask: listTask ) {
          
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Se procede a enviar una alerta (tarea vencida) para la tarea "
                                                   + idTask );
          infoNotification = timerProcess.getInformationForNotification( idTask );
          if ( infoNotification != null && infoNotification.length > 0 ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Informacion para la notificacion de la tarea : "
                                                     + Arrays.toString( infoNotification ) );
            
            sendMailRequestDTO.setRecipients( new String[]{ infoNotification[2] } );
            sendMailRequestDTO.setParametersTemplateHTML( new String[]{ infoNotification[0], infoNotification[1] } );
            
            responseWSDTO = messageServiceDelegate.sendEmail( sendMailRequestDTO );
            
            if ( responseWSDTO != null ) {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta del envio de la notificacion de la tarea : " + idTask
                                                       + ", " + responseWSDTO.toString() );
              if ( responseWSDTO.getCode() != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
                
                notificationTask = timerProcess.findNotificationSent( idTask, ConstantEJB.EVENT_EXPIRATION_TASK,
                                                                      new Date( Long.parseLong( infoNotification[3] ) ) );
                if ( notificationTask != null ) {
                  notificationTask.setSent( notificationTask.getSent() + 1 );
                  
                  respNotification = timerProcess.updateNotificationTask( notificationTask );
                  
                  ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta de la actualizacion del registro de notificacion : "
                                                           + respNotification );
                } else {
                  notificationTask = new NotificationTask();
                  notificationTask.setCreationDate( new Date() );
                  notificationTask.setCreationUser( this.getName() );
                  notificationTask.setEvent( ConstantEJB.EVENT_EXPIRATION_TASK );
                  notificationTask.setSent( 1 );
                  notificationTask.setTask( new Task() );
                  notificationTask.getTask().setId( Integer.parseInt( infoNotification[0] ) );
                  notificationTask.setTaskDeliveryDate( new Date( Long.parseLong( infoNotification[3] ) ) );
                  
                  respNotification = timerProcess.createNotificationTask( notificationTask );
                  
                  ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta de la creacion del registro de notificacion : "
                                                           + respNotification );
                }
                
              }
            }
          }
        }
      } else {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] La lista de tareas esta nula o vacia" );
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    
    ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "////////////// FINALIZA LA EJECUCION DEL HILO [" + this.getName() + "] //////////////" );
    
  }
}
