package co.com.cetus.cetuscontrol.ejb.process;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import co.com.cetus.cetuscontrol.ejb.delegate.CetusMessageServiceDelegate;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationTask;
import co.com.cetus.cetuscontrol.jpa.entity.Task;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.DateUtility;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.ResponseWSDTO;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

/**
 * The Class ThreadBeforeExpirationTasks.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (1/12/2015)
 */
public class ThreadBeforeExpirationTasks extends Thread {
  
  private TimerProcess       timerProcess;
                             
  private List< Integer >    listTask           = null;
                                                
  private SendMailRequestDTO sendMailRequestDTO = null;
                                                
  private String             wsdlMessageService = null;
                                                
  /**
   * </p> Instancia un nuevo thread before expiration tasks. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param listTask the list task
   * @param sendMailRequestDTO the send mail request dto
   * @since CetusControlEJB (1/12/2015)
   */
  public ThreadBeforeExpirationTasks ( List< Integer > listTask, SendMailRequestDTO sendMailRequestDTO, String wsdlMessageService ) {
    try {
      this.listTask = listTask;
      this.sendMailRequestDTO = sendMailRequestDTO;
      this.wsdlMessageService = wsdlMessageService;
      
      timerProcess = UtilCommon.getLookup( ConstantEJB.CONTEXT_TIMER_PROCESS );
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
    ResponseWSDTO responseWSDTO = null;
    boolean respCreate = false;
    String emails = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "////////////// INICIA LA EJECUCION DEL HILO [" + this.getName() + "] //////////////" );
      if ( listTask != null && listTask.size() > 0 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] listTask= " + listTask );
        
        CetusMessageServiceDelegate messageServiceDelegate = new CetusMessageServiceDelegate( wsdlMessageService );
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Inicia envio de correo para cada tarea " );
        for ( Integer idTask: listTask ) {
          
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Se procede a enviar una alerta (proxima a vencer) para la tarea "
                                                   + idTask );
          infoNotification = timerProcess.getInformationForNotification( idTask );
          if ( infoNotification != null && infoNotification.length > 0 ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Informacion para la notificacion de la tarea : "
                                                     + Arrays.toString( infoNotification ) );
                                                     
            emails = timerProcess.findNotificationsEmailsBef( Integer.parseInt( infoNotification[4] ) );
            if ( emails != null ) {
              if ( emails.startsWith( ConstantEJB.RESPONSIBLE_TASK ) ) {
                emails = emails.substring( emails.indexOf( ";" ) + 1 );
              }
              sendMailRequestDTO.setCopyToRecipients( emails.split( ";" ) );
            }
            
            sendMailRequestDTO.setRecipients( new String[]{ infoNotification[2] } );
            sendMailRequestDTO.setParametersTemplateHTML( new String[]{ infoNotification[0], infoNotification[1],
                                                                        DateUtility.formatDatePattern( new Date( Long.parseLong( infoNotification[3] ) ),
                                                                                                       ConstantEJB.DATE_PATTERN_NOTIFICATION ) } );
                                                                                                       
            sendMailRequestDTO.setSubject( sendMailRequestDTO.getSubject() + " [" + String.valueOf( infoNotification[0] ) +"]" );
            
            responseWSDTO = messageServiceDelegate.sendEmail( sendMailRequestDTO );
            
            if ( responseWSDTO != null ) {
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta del envio de la notificacion de la tarea : " + idTask
                                                       + ", " + responseWSDTO.toString() );
              if ( responseWSDTO.getCode() != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
                NotificationTask notificationTask = new NotificationTask();
                notificationTask.setCreationDate( new Date() );
                notificationTask.setCreationUser( this.getName() );
                notificationTask.setEvent( ConstantEJB.EVENT_BEFORE_EXPIRATION );
                notificationTask.setSent( 1 );
                notificationTask.setTask( new Task() );
                notificationTask.getTask().setId( idTask );
                notificationTask.setTaskDeliveryDate( new Date( Long.parseLong( infoNotification[3] ) ) );
                respCreate = timerProcess.createNotificationTask( notificationTask );
                
                ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta de la creacion del registro de notificacion : "
                                                         + respCreate );
                                                         
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
