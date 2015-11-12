package co.com.cetus.cetuscontrol.ejb.process;

import java.math.BigDecimal;
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
  public long findJornadaMax ( long idClientCetus, String day ) {
    long max = -1;
    TypedQuery< BigDecimal > query = null;
    BigDecimal resp = null;
    try {
      query = em.createNamedQuery( "Workday.findJornadaMax", BigDecimal.class );
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
  public long findJornadaMin ( long idClientCetus, String day ) {
    long max = -1;
    TypedQuery< BigDecimal > query = null;
    BigDecimal resp = null;
    try {
      query = em.createNamedQuery( "Workday.findJornadaMin", BigDecimal.class );
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
  public long findTimeBeforeExpiration ( long idClientCetus ) {
    long time = -1;
    TypedQuery< BigDecimal > query = null;
    BigDecimal resp = null;
    try {
      query = em.createNamedQuery( "ParameterGeneral.findTimeBeforeExpirationByClientCetus", BigDecimal.class );
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
  public List< Long > findIdTaskBeforeExpiration ( long idClientCetus, Integer timeBefore ) {
    List< Long > list = null;
    TypedQuery< Long > query = null;
    try {
      
      query = em.createQuery( "SELECT t.id "
                            + "FROM Task t "
                            + "JOIN t.personGroup pg "
                            + "JOIN pg.person p "
                            + "JOIN p.client c "
                            + "JOIN c.clientCetus cc "
                            + "WHERE TO_DATE(SYSDATE, 'yyyy-MM-dd HH24:mi') "
                            + "    BETWEEN TO_DATE((t.deliveryDate - ( :timeBefore / 1440)), 'yyyy-MM-dd HH24:mi') "
                            + "    AND TO_DATE(t.deliveryDate, 'yyyy-MM-dd HH24:mi') "
                            + "AND cc.id = :idClientCetus"
                              , Long.class );
      query.setParameter( "idClientCetus", idClientCetus );
      query.setParameter( "timeBefore", timeBefore );
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
  public long findNotificationSent ( long idTask, String event ) {
    long sent = 0;
    TypedQuery< BigDecimal > query = null;
    BigDecimal resp = null;
    try {
      query = em.createNamedQuery( "NotificationTask.findNotificationSent", BigDecimal.class );
      query.setParameter( "idTask", idTask );
      query.setParameter( "event", event );
      
      resp = query.getSingleResult();
      if ( resp != null && resp.longValue() > 0 ) {
        sent = resp.longValue();
      }
    } catch (NoResultException nr){
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
  public String[] getInformationForNotification ( long idTask ) {
    long sent = 0;
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
