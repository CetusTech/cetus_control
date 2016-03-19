package co.com.cetus.cetuscontrol.ejb.process;

import java.util.List;

import co.com.cetus.cetuscontrol.ejb.delegate.CetusMessageServiceDelegate;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationSendMail;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.ResponseWSDTO;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

/**
 * The Class ThreadNotificationProcess.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (11/03/2016)
 */
public class ThreadNotificationProcess extends Thread {
  
  /** The timer process. */
  private TimerProcess                 timerProcess;
                                       
  /** The list task. */
  private List< NotificationSendMail > listNotification   = null;
                                                          
  /** The send mail request dto. */
  private SendMailRequestDTO           sendMailRequestDTO = null;
                                                          
  /** The wsdl message service. */
  private String                       wsdlMessageService = null;
                                                          
  private CetusControlProcess          cetusControlProcess;
                                       
  /**
   * </p> Instancia un nuevo thread notification process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param listNotification the list notification
   * @param sendMailRequestDTO the send mail request dto
   * @param wsdlMessageService the wsdl message service
   * @since CetusControlEJB (11/03/2016)
   */
  public ThreadNotificationProcess ( List< NotificationSendMail > listNotification, SendMailRequestDTO sendMailRequestDTO,
                                     String wsdlMessageService ) {
    try {
      this.listNotification = listNotification;
      this.sendMailRequestDTO = sendMailRequestDTO;
      this.wsdlMessageService = wsdlMessageService;
      
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
    String[] arrEmails = null;
    String[] parameters = null;
    ResponseWSDTO responseWSDTO = null;
    boolean response = false;
    String subject = null;
    String email = null;
    String emails = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "////////////// INICIA LA EJECUCION DEL HILO [" + this.getName() + "] //////////////" );
      if ( listNotification != null && listNotification.size() > 0 ) {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] listNotification= " + listNotification.size() );
        
        CetusMessageServiceDelegate messageServiceDelegate = new CetusMessageServiceDelegate( wsdlMessageService );
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Inicia envio de correo para las notificaciones" );
        for ( NotificationSendMail notificationSendMail: listNotification ) {
          
          subject = cetusControlProcess.getValueParameter( notificationSendMail.getSubjectEmail() );
          if ( notificationSendMail.getEmails() != null ) {
            if ( notificationSendMail.getEmails().contains( ";" ) ) {
              email = notificationSendMail.getEmails().substring( 0, notificationSendMail.getEmails().indexOf( ";" ) );
              emails = notificationSendMail.getEmails().substring( notificationSendMail.getEmails().indexOf( ";" ) + 1 );
            } else {
              email = notificationSendMail.getEmails();
              emails = null;
            }
          }
          parameters = notificationSendMail.getParameters() != null ? notificationSendMail.getParameters().split( "\\|" ) : null;
          sendMailRequestDTO.setSubject( subject.concat( "[" + parameters[0] + "]" ) );
          sendMailRequestDTO.setRecipients( new String[]{ email } );
          if ( emails != null ) {
            arrEmails = emails.split( ";" );
            sendMailRequestDTO.setCopyToRecipients( arrEmails );
          }
          sendMailRequestDTO.setNameTemplateHTML( notificationSendMail.getTemplateName() );
          
          sendMailRequestDTO.setParametersTemplateHTML( parameters );
          
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Se procede a enviar la notificacion :: "
                                                   + sendMailRequestDTO.toString() );
                                                   
          responseWSDTO = messageServiceDelegate.sendEmail( sendMailRequestDTO );
          
          if ( responseWSDTO != null ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta del envio de la notificacion " + responseWSDTO.toString() );
            if ( responseWSDTO.getCode() != null && responseWSDTO.getCode().equals( ConstantCommon.WSResponse.CODE_ONE ) ) {
              response = timerProcess.deleteNotificationSendMail( notificationSendMail );
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta de la eliminacion de la notificacion : " + response );
            } else {
              notificationSendMail.setProcessed( false );
              response = timerProcess.updateNotificationSendMail( notificationSendMail );
              ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] Respuesta de la actualizacion de la notificacion : " + response );
            }
          }
        }
      } else {
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + this.getName() + "] La lista de notificaciones nulas o vacia" );
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    
    ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "////////////// FINALIZA LA EJECUCION DEL HILO [" + this.getName() + "] //////////////" );
    
  }
}
