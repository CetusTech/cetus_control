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
import co.com.cetus.common.util.UtilCommon;
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
    int numThread = 0;
    int numSubList = 0;
    int numResidue = 0;
    int contStart = 0;
    int contEnd = 0;
    List< Integer > subList = null;
    List< List< Integer > > list = null;
    ThreadBeforeExpirationTasks thread = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "--------------- INICIA LA EJECUCION DEL TIMER " + timer.getInfo() + ", " + new Date()
                                               + " ---------------" );
      
      idClientCetus = nameTimer.substring( ConstantEJB.NAME_TIMER_BEFORE_EXPIRATION_TASKS.length() );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] idClientCetus=" + idClientCetus );
      timeBefore = timerProcess.findTimeBeforeExpiration( Integer.parseInt( idClientCetus ) );
      listTask = timerProcess.findIdTaskBeforeExpiration( Integer.parseInt( idClientCetus ),
                                                          new Integer( ( int ) timeBefore ),
                                                          Integer.parseInt( cetusControlProcess.getValueParameter( ConstantEJB.TASK_STATUS_IN_PROGRESS ) ) );
      ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] timeBefore=" + timeBefore + ", listTask=" + listTask );
      if ( listTask != null && listTask.size() > 0 ) {
        
        numThread = Integer.parseInt( cetusControlProcess.getValueParameter( ConstantEJB.THREAD_BEFORE_EXPIRATION_TASK ) );
        numSubList = listTask.size() / numThread;
        numResidue = listTask.size() % numThread;
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] numThread :: " + numThread + ", numSubList :: " + numSubList
                                                 + ", numResidue :: " + numResidue );
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia la construccion de las listas para cada hilo..." );
        list = new ArrayList< List< Integer > >();
        if ( numSubList == 0 ) {
          list.add( listTask );
        } else {
          for ( int i = 0; i < numThread; i++ ) {
            contEnd += numSubList;
            
            if ( numResidue > 0 ) {
              contEnd++;
              numResidue--;
            }
            subList = new ArrayList< Integer >();
            subList = listTask.subList( contStart, contEnd );
            contStart = contEnd;
            
            list.add( subList );
            
          }
        }
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia la construccion del objeto para enviar el correo " );
        String wsdlMessageService = cetusControlProcess.getValueParameter( ConstantEJB.WSDL_CETUS_MESSAGE_SERVICE );
        SendMailRequestDTO sendMailRequestDTO = new SendMailRequestDTO();
        sendMailRequestDTO.setUser( cetusControlProcess.getValueParameter( ConstantEJB.USER_WS_MESSAGE_SERVICE ) );
        sendMailRequestDTO.setPassword( cetusControlProcess.getValueParameter( ConstantEJB.PASSWORD_WS_MESSAGE_SERVICE ) );
        sendMailRequestDTO.setNameTemplateHTML( ConstantEJB.TEMPLATE_EMAIL_BEFORE_EXPIRATION );
        sendMailRequestDTO.setSenderEmail( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_FROM ) );
        sendMailRequestDTO.setSenderName( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_USERNAME ) );
        sendMailRequestDTO.setSenderPassword( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_PASS ) );
        sendMailRequestDTO.setServerPort( cetusControlProcess.getValueParameter( ConstantEJB.SMPT_PORT ) );
        sendMailRequestDTO.setServerSmtp( cetusControlProcess.getValueParameter( ConstantEJB.SMTP_HOST ) );
        sendMailRequestDTO.setSubject( cetusControlProcess.getValueParameter( ConstantEJB.SUBJECT_BEFORE_EXPIRATION ) );
        
        ConstantEJB.CETUS_CONTROL_EJB_LOG.debug( "[" + timer.getInfo() + "] Inicia la ejecucion de cada hilo..." );
        
        for ( List< Integer > subL: list ) {
          thread = new ThreadBeforeExpirationTasks( subL, sendMailRequestDTO, wsdlMessageService );
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
