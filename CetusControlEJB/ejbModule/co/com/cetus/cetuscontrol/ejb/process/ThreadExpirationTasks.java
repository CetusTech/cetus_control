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
  private TimerProcess       timerProcess;
  
  /** The list task. */
  private List< Object[] >   listTask           = null;
  
  private SendMailRequestDTO sendMailRequestDTO = null;
  
  private String             wsdlMessageService = null;
  
  /**
   * </p> Instancia un nuevo thread expiration tasks. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param listTask the list task
   * @since CetusControlEJB (3/12/2015)
   */
  public ThreadExpirationTasks ( List< Object[] > listTask, SendMailRequestDTO sendMailRequestDTO, String wsdlMessageService ) {
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
    ResponseWSDTO responseWSDTO = null;
    boolean respNotification = false;
    NotificationTask notificationTask = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "////////////// INICIA LA EJECUCION DEL HILO [" + this.getName() + "] //////////////" );
      if ( listTask != null && listTask.size() > 0 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] listTask= " + listTask );
        
        CetusMessageServiceDelegate messageServiceDelegate = new CetusMessageServiceDelegate( wsdlMessageService );
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Inicia envio de correo para cada tarea " );
        for ( Object[] task: listTask ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Informacion para la notificacion de la tarea : "
                                                   + Arrays.toString( task ) );
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Se procede a enviar una alerta (tarea vencida) para la tarea "
                                                   + String.valueOf( task[0] ) );
          
          sendMailRequestDTO.setRecipients( new String[]{ String.valueOf( task[2] ) } );
          sendMailRequestDTO.setParametersTemplateHTML( new String[]{ String.valueOf( task[0] ), String.valueOf( task[1] ) } );
          
          responseWSDTO = messageServiceDelegate.sendEmail( sendMailRequestDTO );
          
          if ( responseWSDTO != null ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta del envio de la notificacion de la tarea : " + task[0]
                                                     + ", " + responseWSDTO.toString() );
            if ( responseWSDTO.getCode() != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
              
              notificationTask = timerProcess.findNotificationSent( Integer.parseInt( String.valueOf( task[0] ) ), ConstantEJB.EVENT_EXPIRATION_TASK,
                                                                    ( Date ) task[3] );
              if ( notificationTask != null ) {
                notificationTask.setSent( notificationTask.getSent() + 1 );
                notificationTask.setCreationDate( new Date() );
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
                notificationTask.getTask().setId( Integer.parseInt( String.valueOf( task[0] ) ) );
                notificationTask.setTaskDeliveryDate( ( Date ) task[3] );
                
                respNotification = timerProcess.createNotificationTask( notificationTask );
                
                ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta de la creacion del registro de notificacion : "
                                                         + respNotification );
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
