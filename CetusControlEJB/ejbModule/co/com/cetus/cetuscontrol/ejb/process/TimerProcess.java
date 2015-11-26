package co.com.cetus.cetuscontrol.ejb.process;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.jpa.entity.ClientCetus;

/**
 * The Class TimerProcess.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (26/07/2015)
 */
@Singleton
@DependsOn ( "GeneralProcess" )
@Lock ( LockType.READ )
public class TimerProcess {
  
  /** The general process. */
  @EJB
  private GeneralProcess generalProcess;
  
  @PersistenceContext ( unitName = "CetusControlJPA" )
  private EntityManager  em;
  
  public TimerProcess () {
  }
  
  /**
   * </p> Find client cetus enabled timer. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el list
   * @since CetusControlEJB (26/07/2015)
   */
  public List< ClientCetus > findClientCetusEnabledTimer () {
    List< ClientCetus > list = null;
    TypedQuery< ClientCetus > query = null;
    try {
      query = em.createNamedQuery( "ClientCetus.findClientCetusEnabledTimer", ClientCetus.class );
      list = query.getResultList();
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return list;
  }
  
  /**
   * </p> Find jornada max. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @param day the day
   * @return el long
   * @since CetusControlEJB (26/07/2015)
   */
  public long findJornadaMax ( int idClientCetus, String day ) {
    long max = -1;
    TypedQuery< Integer > query = null;
    Integer resp = null;
    try {
      query = em.createNamedQuery( "Workday.findJornadaMax", Integer.class );
      query.setParameter( "idClientCetus", idClientCetus );
      query.setParameter( "day", day );
      resp = query.getSingleResult();
      if ( resp != null && resp.longValue() > 0 ) {
        max = resp.longValue();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return max;
  }
  
  /**
   * </p> Find jornada min. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @param day the day
   * @return el long
   * @since CetusControlEJB (26/07/2015)
   */
  public long findJornadaMin ( int idClientCetus, String day ) {
    long max = -1;
    TypedQuery< Integer > query = null;
    Integer resp = null;
    try {
      query = em.createNamedQuery( "Workday.findJornadaMin", Integer.class );
      query.setParameter( "idClientCetus", idClientCetus );
      query.setParameter( "day", day );
      resp = query.getSingleResult();
      if ( resp != null && resp.longValue() > 0 ) {
        max = resp.longValue();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return max;
  }
  
  /**
   * </p> Find time before expiration. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el long
   * @since CetusControlEJB (31/07/2015)
   */
  public long findTimeBeforeExpiration ( int idClientCetus ) {
    long time = -1;
    TypedQuery< Integer > query = null;
    Integer resp = null;
    try {
      query = em.createNamedQuery( "ParameterGeneral.findTimeBeforeExpirationByClientCetus", Integer.class );
      query.setParameter( "idClientCetus", idClientCetus );
      resp = query.getSingleResult();
      if ( resp != null && resp.longValue() > 0 ) {
        time = resp.longValue();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return time;
  }
  
  /**
   * </p> Find task before expiration. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el list
   * @since CetusControlEJB (17/08/2015)
   */
  public List< Integer > findIdTaskBeforeExpiration ( int idClientCetus, Integer timeBefore ) {
    List< Integer > list = null;
    TypedQuery< Integer > query = null;
    try {
      Calendar calMin = Calendar.getInstance();
      calMin.add( Calendar.MINUTE, -timeBefore.intValue() );
      Date dateMin = calMin.getTime();
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "idClientCetus=" + idClientCetus + ", timeBefore=" + timeBefore.intValue() + ", dateMin=" + dateMin );
      query = em.createQuery( "SELECT t.id "
                              + "FROM Task t "
                              + "JOIN t.personGroup pg "
                              + "JOIN pg.person p "
                              + "JOIN p.client c "
                              + "JOIN c.clientCetus cc "
                              + "WHERE DATE_FORMAT(now(), '%Y-%m-%d %H:%i') "
                              + "    BETWEEN DATE_FORMAT( :dateMin , '%Y-%m-%d %H:%i') "
                              + "    AND DATE_FORMAT(t.deliveryDate, '%Y-%m-%d %H:%i') "
                              + "AND cc.id = :idClientCetus"
                              , Integer.class );
      query.setParameter( "idClientCetus", idClientCetus );
      query.setParameter( "dateMin", dateMin );
      list = query.getResultList();
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return list;
  }
  
  /**
   * </p> Find notification sent. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idtask the idtask
   * @param event the event
   * @return el long
   * @since CetusControlEJB (22/08/2015)
   */
  public long findNotificationSent ( int idTask, String event ) {
    int sent = 0;
    TypedQuery< Integer > query = null;
    Integer resp = null;
    try {
      query = em.createNamedQuery( "NotificationTask.findNotificationSent", Integer.class );
      query.setParameter( "idTask", idTask );
      query.setParameter( "event", event );
      
      resp = query.getSingleResult();
      if ( resp != null && resp.intValue() > 0 ) {
        sent = resp.intValue();
      }
    } catch ( NoResultException nr ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "No se encontraron registros de notificacion para la tarea " + idTask + " en el evento " + event );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return sent;
  }
  
  /**
   * </p> Gets the information for notification. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idTask the id task
   * @return el string[]
   * @since CetusControlEJB (10/09/2015)
   */
  public String[] getInformationForNotification ( int idTask ) {
    TypedQuery< Object[] > query = null;
    Object[] resp = null;
    String[] response = new String[3];
    try {
      query = em.createQuery( "SELECT t.id, t.description, p.email " +
                              "FROM Task t " +
                              " JOIN t.personGroup pg " +
                              " JOIN pg.person p " +
                              "WHERE t.id = :idTask", Object[].class );
      query.setParameter( "idTask", idTask );
      
      resp = ( Object[] ) query.getSingleResult();
      if ( resp != null && resp.length > 0 ) {
        response[0] = String.valueOf( resp[0] );
        response[1] = String.valueOf( resp[1] );
        response[2] = String.valueOf( resp[2] );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return response;
  }
  
}
