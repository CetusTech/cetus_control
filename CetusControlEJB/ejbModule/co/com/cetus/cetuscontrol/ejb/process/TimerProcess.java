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
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.cetuscontrol.jpa.entity.ClientCetus;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationSendMail;
import co.com.cetus.cetuscontrol.jpa.entity.NotificationTask;

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
  public List< Integer > findIdTaskBeforeExpiration ( int idClientCetus, Integer timeBefore, int status ) {
    List< Integer > list = null;
    TypedQuery< Integer > query = null;
    try {
      Calendar calMax = Calendar.getInstance();
      calMax.add( Calendar.MINUTE, timeBefore.intValue() );
      Date dateMax = calMax.getTime();
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "idClientCetus=" + idClientCetus + ", timeBefore=" + timeBefore.intValue() + ", dateMax=" + dateMax );
      query = em.createQuery( "SELECT t.id "
                              + "FROM Task t "
                              + "JOIN t.personGroup pg "
                              + "JOIN pg.person p "
                              + "JOIN p.client c "
                              + "JOIN c.clientCetus cc "
                              + "WHERE DATE_FORMAT(t.deliveryDate, '%Y-%m-%d %H:%i') "
                              + "    BETWEEN DATE_FORMAT( now() , '%Y-%m-%d %H:%i') "
                              + "    AND DATE_FORMAT(:dateMax, '%Y-%m-%d %H:%i') "
                              + "AND cc.id = :idClientCetus "
                              + "AND t.status.id = :status "
                              + "AND t.id NOT IN (SELECT nt.task.id FROM NotificationTask nt "
                              + "                 WHERE DATE_FORMAT(t.deliveryDate, '%Y-%m-%d %H:%i') = DATE_FORMAT(nt.taskDeliveryDate, '%Y-%m-%d %H:%i') "
                              + "                 AND nt.event = :event AND nt.task.id = t.id ) ", Integer.class );
      query.setParameter( "idClientCetus", idClientCetus );
      query.setParameter( "dateMax", dateMax );
      query.setParameter( "event", ConstantEJB.EVENT_BEFORE_EXPIRATION );
      query.setParameter( "status", status );
      
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
   * @param idTask the id task
   * @param event the event
   * @param deliveryDate the delivery date
   * @return el NotificationTask
   * @since CetusControlEJB (22/08/2015)
   */
  public NotificationTask findNotificationSent ( int idTask, String event, Date deliveryDate ) {
    TypedQuery< NotificationTask > query = null;
    NotificationTask notificationTask = null;
    try {
      
      query = em.createQuery( "SELECT n FROM NotificationTask n "
                              + "WHERE n.task.id = :idTask "
                              + "AND n.event = :event "
                              + "AND DATE_FORMAT(n.task.deliveryDate, '%Y-%m-%d %H:%i') = DATE_FORMAT(:deliveryDate, '%Y-%m-%d %H:%i')",
                              NotificationTask.class );
                              
      query.setParameter( "idTask", idTask );
      query.setParameter( "event", event );
      query.setParameter( "deliveryDate", event );
      
      notificationTask = query.getSingleResult();
    } catch ( NoResultException nr ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( "No se encontraron registros de notificacion para la tarea " + idTask + " en el evento " + event );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return notificationTask;
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
    String[] response = new String[4];
    try {
      query = em.createQuery( "SELECT t.code, t.description, p.email, t.deliveryDate, g.id " +
                              "FROM Task t " +
                              " JOIN t.personGroup pg " +
                              " JOIN pg.person p " +
                              " JOIN pg.groupT g " +
                              "WHERE t.id = :idTask", Object[].class );
      query.setParameter( "idTask", idTask );
      
      resp = query.getSingleResult();
      if ( resp != null && resp.length > 0 ) {
        response[0] = String.valueOf( resp[0] );
        response[1] = String.valueOf( resp[1] );
        response[2] = String.valueOf( resp[2] );
        response[3] = String.valueOf( ( ( Date ) resp[3] ).getTime() );
        response[4] = String.valueOf( resp[4] );
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return response;
  }
  
  /**
   * </p> Creates the notification task. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param notificationTask the notification task
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (1/12/2015)
   */
  public boolean createNotificationTask ( NotificationTask notificationTask ) {
    boolean result = false;
    try {
      notificationTask = generalProcess.create( notificationTask );
      if ( notificationTask != null && notificationTask.getId() > 0 ) {
        result = true;
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Update notification task. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param notificationTask the notification task
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (3/12/2015)
   */
  public boolean updateNotificationTask ( NotificationTask notificationTask ) {
    boolean result = false;
    try {
      result = generalProcess.edit( notificationTask );
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Find task expiration. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @param status the status
   * @return el list
   * @since CetusControlEJB (20/01/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public List< Object[] > findTaskExpiration ( int idClientCetus, int status ) {
    List< Object[] > list = null;
    Query query = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "idClientCetus=" + idClientCetus + ", status=" + status );
      query = em.createNativeQuery( "SELECT T.ID, T.DESCRIPTION, P.EMAIL, T.DELIVERY_DATE, T.CODE, PG.ID_GROUP "
                                    + "FROM TASK T, "
                                    + "   PERSON_GROUP PG, "
                                    + "   PERSON P, "
                                    + "   CLIENT C, "
                                    + "   CLIENT_CETUS CC, "
                                    + "   PARAMETER_GENERAL PEG "
                                    + "WHERE T.ID_GROUP_PERSON = PG.ID "
                                    + "AND PG.ID_PERSON = P.ID "
                                    + "AND P.ID_CLIENT = C.ID "
                                    + "AND C.ID_CLIENT_CETUS = CC.ID "
                                    + "AND CC.ID = PEG.ID_CLIENT_CETUS "
                                    + "AND CC.ID = ? "
                                    + "AND T.ID_STATUS = ? "
                                    + "AND ( ( T.ID NOT IN (SELECT NT.ID_TASK  "
                                    + "             FROM NOTIFICATION_TASK NT "
                                    + "             WHERE DATE_FORMAT(T.DELIVERY_DATE, '%Y-%m-%d %H:%i') = DATE_FORMAT(NT.TASK_DELIVERY_DATE, '%Y-%m-%d %H:%i') "
                                    + "             AND NT.EVENT = 'EXPIRATION_TASK' "
                                    + "             AND T.ID = NT.ID_TASK "
                                    + "            ) "
                                    + "    AND DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i') "
                                    + "      BETWEEN DATE_FORMAT( DATE_ADD(T.DELIVERY_DATE, INTERVAL (PEG.TIME_AFTER_EXPIRATION ) MINUTE) , '%Y-%m-%d %H:%i') "
                                    + "      AND DATE_FORMAT( DATE_ADD(T.DELIVERY_DATE, INTERVAL (PEG.TIME_AFTER_EXPIRATION * 2) MINUTE) , '%Y-%m-%d %H:%i') "
                                    + "     ) "
                                    + "  OR "
                                    + "  T.ID IN (SELECT NT.ID_TASK "
                                    + "       FROM NOTIFICATION_TASK NT "
                                    + "       WHERE DATE_FORMAT(T.DELIVERY_DATE, '%Y-%m-%d %H:%i') = DATE_FORMAT(NT.TASK_DELIVERY_DATE, '%Y-%m-%d %H:%i') "
                                    + "       AND NT.EVENT = 'EXPIRATION_TASK' "
                                    + "       AND T.ID = NT.ID_TASK  "
                                    + "       AND NT.SENT < PEG.COL_NUMBER_2 "
                                    + "       AND DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i') "
                                    + "        BETWEEN DATE_FORMAT( DATE_ADD(T.DELIVERY_DATE, INTERVAL (PEG.TIME_AFTER_EXPIRATION * (NT.SENT + 1)) MINUTE) , '%Y-%m-%d %H:%i') "
                                    + "        AND DATE_FORMAT( DATE_ADD(T.DELIVERY_DATE, INTERVAL (PEG.TIME_AFTER_EXPIRATION * (NT.SENT + 2)) MINUTE) , '%Y-%m-%d %H:%i') "
                                    + "      ) "
                                    + "  ) " );
                                    
      query.setParameter( 1, idClientCetus );
      query.setParameter( 2, status );
      
      if ( query.getResultList() != null && query.getResultList().size() > 0 ) {
        list = ( List< Object[] > ) query.getResultList();
      }
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return list;
  }
  
  /**
   * </p> Find time after expiration. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el long
   * @since CetusControlEJB (25/01/2016)
   */
  public long findTimeAfterExpiration ( int idClientCetus ) {
    long time = -1;
    TypedQuery< Integer > query = null;
    Integer resp = null;
    try {
      query = em.createNamedQuery( "ParameterGeneral.findTimeAfterExpirationByClientCetus", Integer.class );
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
   * </p> Find notifications emails. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idGroup the id group
   * @return el string
   * @since CetusControlEJB (9/03/2016)
   */
  public String findNotificationsEmailsBef ( int idGroup ) {
    String emails = null;
    TypedQuery< String > query = null;
    try {
      query = em.createNamedQuery( "NotificationSetting.findNotificationEmailsBef", String.class );
      query.setParameter( "idGroup", idGroup );
      
      if ( query.getResultList() != null && query.getResultList().size() > 0 ) {
        emails = query.getSingleResult();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return emails;
  }
  
  /**
   * </p> Find notifications emails exp. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idGroup the id group
   * @return el string
   * @since CetusControlEJB (9/03/2016)
   */
  public String findNotificationsEmailsExp ( int idGroup ) {
    String emails = null;
    TypedQuery< String > query = null;
    try {
      query = em.createNamedQuery( "NotificationSetting.findNotificationEmailsExp", String.class );
      query.setParameter( "idGroup", idGroup );
      
      if ( query.getResultList() != null && query.getResultList().size() > 0 ) {
        emails = query.getSingleResult();
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return emails;
  }
  
  /**
   * </p> Delete notification send mail. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param notificationSendMail the notification send mail
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (11/03/2016)
   */
  public boolean deleteNotificationSendMail ( NotificationSendMail notificationSendMail ) {
    boolean result = false;
    try {
      generalProcess.remove( notificationSendMail );
      result = true;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Update notification send mail. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param notificationSendMail the notification send mail
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (11/03/2016)
   */
  public boolean updateNotificationSendMail ( NotificationSendMail notificationSendMail ) {
    boolean result = false;
    try {
      generalProcess.edit( notificationSendMail );
      result = true;
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Find notification send mail. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param limit the limit
   * @return el list
   * @since CetusControlEJB (11/03/2016)
   */
  public List< NotificationSendMail > findNotificationSendMail ( int limit ) {
    List< NotificationSendMail > list = null;
    Query query = null;
    TypedQuery< NotificationSendMail > queryList = null;
    try {
      query = em.createNativeQuery( "UPDATE NOTIFICATION_SEND_MAIL SET PROCESSED = 1 WHERE PROCESSED = 0 ORDER BY CREATE_DATE LIMIT ?" );
      query.setParameter( 1, limit );
      int result = query.executeUpdate();
      if ( result > 0 ) {
        queryList = em.createNamedQuery( "NotificationSendMail.findAllToProcess", NotificationSendMail.class );
        if ( queryList != null && queryList.getResultList() != null && queryList.getResultList().size() > 0 ) {
          list = queryList.getResultList();
        }
      }
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    return list;
  }
  
  /**
   * </p> Load variable mysql. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlEJB (26/03/2016)
   */
  public void loadVariableMysql () {
    Query query = null;
    try {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Ejecutando variable lc_time_names" );
      query = em.createNativeQuery( "SET @@global.lc_time_names = 'es_CO'" );
      query.executeUpdate();
      
      ConstantEJB.CETUS_CONTROL_EJB_LOG.info( "Ejecutando variable time_zone" );
      query = em.createNativeQuery( "SET @@global.time_zone='-05:00'" );
      query.executeUpdate();
      
    } catch ( Exception e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
  }
  
}
