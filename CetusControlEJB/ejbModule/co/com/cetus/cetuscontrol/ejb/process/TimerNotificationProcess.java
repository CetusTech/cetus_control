package co.com.cetus.cetuscontrol.ejb.process;

import java.util.ArrayList;
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

import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationSendMail;
import co.com.cetus.common.util.UtilCommon;
import co.com.cetus.messageservice.ejb.service.SendMailRequestDTO;

/**
 * The Class TimerNotificationProcess.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (11/03/2016)
 */
@Singleton
@Startup
public class TimerNotificationProcess {
  
  /** The timer service. */
  @Resource
  TimerService         timerService;
                       
  /** The timer process. */
  @EJB
  private TimerProcess timerProcess;
                       
  /** The cetus control process. */
  @EJB
  CetusControlProcess  cetusControlProcess;
                       
  /**
   * </p> Instancia un nuevo timer notification process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (11/03/2016)
   */
  public TimerNotificationProcess () {
  }
  
  /**
   * </p> Start timer. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param nameTimer the name timer
   * @param rangeHours the range hours
   * @param minutesForExecute the minutes for execute
   * @since CetusControlEJB (11/03/2016)
   */
  public void startTimer ( String nameTimer, String rangeHours, String minutesForExecute ) {
    try {
      timerService.createCalendarTimer( new ScheduleExpression().hour( rangeHours ).minute( minutesForExecute ),
                                        new TimerConfig( nameTimer, true ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Se creo exitosamente el timer [" + nameTimer + "], rangeHours=" + rangeHours + ", minutesForExecute="
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
   * @since CetusControlEJB (11/03/2016)
   */
  public void stopTimer ( String nameTimer ) {
    try {
      if ( timerService.getTimers() != null ) {
        for ( Timer timer: timerService.getTimers() ) {
          if ( timer.getInfo().equals( nameTimer ) ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Deteniendo el timer [" + nameTimer + "]" );
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
   * @since CetusControlEJB (11/03/2016)
   */
  @Timeout
  public void executeProcess ( Timer timer ) {
    List< NotificationSendMail > listNotification = null;
    int numThread = 0;
    int numSubList = 0;
    int numResidue = 0;
    int contStart = 0;
    int contEnd = 0;
    List< NotificationSendMail > subList = null;
    List< List< NotificationSendMail > > list = null;
    ThreadNotificationProcess thread = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "--------------- INICIA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date()
                                               + " ---------------" );
                                               
      listNotification = timerProcess.findNotificationSendMail( Integer.parseInt( cetusControlProcess.getValueParameter( ConstantEJB.REGISTER_NOTIFICATION_NUMBER ) ) );
      
      if ( listNotification != null && listNotification.size() > 0 ) {
        
        numThread = Integer.parseInt( cetusControlProcess.getValueParameter( ConstantEJB.THREAD_NOTIFICATION_PROCESS ) );
        numSubList = listNotification.size() / numThread;
        numResidue = listNotification.size() % numThread;
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] numThread :: " + numThread + ", numSubList :: " + numSubList
                                                 + ", numResidue :: " + numResidue );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia la construccion de las listas para cada hilo..." );
        list = new ArrayList< List< NotificationSendMail > >();
        if ( numSubList == 0 ) {
          list.add( listNotification );
        } else {
          for ( int i = 0; i < numThread; i++ ) {
            contEnd += numSubList;
            
            if ( numResidue > 0 ) {
              contEnd++;
              numResidue--;
            }
            subList = new ArrayList< NotificationSendMail >();
            subList = listNotification.subList( contStart, contEnd );
            contStart = contEnd;
            
            list.add( subList );
            
          }
        }
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia la construccion del objeto para enviar el correo " );
        String wsdlMessageService = cetusControlProcess.getValueParameter( ConstantEJB.WSDL_CETUS_MESSAGE_SERVICE );
        SendMailRequestDTO sendMailRequestDTO = new SendMailRequestDTO();
        sendMailRequestDTO.setUser( cetusControlProcess.getValueParameter( ConstantEJB.USER_WS_MESSAGE_SERVICE ) );
        sendMailRequestDTO.setPassword( cetusControlProcess.getValueParameter( ConstantEJB.PASSWORD_WS_MESSAGE_SERVICE ) );
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia la ejecucion de cada hilo..." );
        
        for ( List< NotificationSendMail > subL: list ) {
          thread = new ThreadNotificationProcess( subL, sendMailRequestDTO, wsdlMessageService );
          thread.start();
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
   * @since CetusControlEJB (11/03/2016)
   */
  public boolean existsTimerRunning ( String nameTimer ) {
    boolean exists = false;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Validando si el timer " + nameTimer + " esta ejecutandose" );
      if ( !UtilCommon.stringNullOrEmpty( nameTimer ) && timerService.getTimers() != null ) {
        for ( Timer timer: timerService.getTimers() ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "Validando timer " + timer.getInfo() );
          if ( timer.getInfo().equals( nameTimer ) ) {
            ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "El timer " + nameTimer + ", esta ejecutandose...." );
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
  
  /**
   * </p> Stop all timer. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (11/03/2016)
   */
  public void stopAllTimer () {
    try {
      if ( timerService.getTimers() != null ) {
        for ( Timer timer: timerService.getTimers() ) {
          ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Deteniendo el timer [" + timer.getInfo() + "]" );
          timer.cancel();
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
}
