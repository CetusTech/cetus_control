package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the task database table.
 * 
 */
@Entity
@NamedQueries ( {
                 @NamedQuery ( name = "Task.findAll", query = "SELECT t FROM Task t" ),
                 @NamedQuery ( name = "Task.getPercentageCurrent", query = "select sum(t.percentage) from Task t where t.status.id = '2' and t.personGroup.person.id = :idPerson" ),
                 @NamedQuery ( name = "Task.findTaskByClient", query = "select t from Task t, GroupT g, Person p, PersonGroup pg where g.id = pg.groupT.id and p.id = pg.person.id and t.id = pg.id and p.client.id = :idClient  " ),
                 @NamedQuery ( name = "Task.findTaskByPersonGroup", query = "select t from Task t, GroupT g, Person p, PersonGroup pg where g.id = pg.groupT.id and p.id = pg.person.id and t.personGroup.id = pg.id and g.id = :idGroup and p.id = :idPerson  " ),
                 @NamedQuery ( name = "Task.findTaskByPerson", query = ""
                                                                       + "  select "
                                                                       + "  t "
                                                                       + "  from "
                                                                       + "  Task t  "
                                                                       + "  JOIN t.personGroup pg "
                                                                       + "  JOIN pg.person p WHERE p.id = :idPerson AND t.status.description <> :statusExclude " ),
                 @NamedQuery ( name = "Task.findTaskByPersonPriority", query = ""
                                                                               + "  select "
                                                                               + "  t "
                                                                               + "  from "
                                                                               + "  Task t  "
                                                                               + "  JOIN t.priority pr"
                                                                               + "  JOIN t.personGroup pg "
                                                                               + "  JOIN pg.person p WHERE p.id = :idPerson AND t.status.description <> :statusExclude AND pr.description =:prDescrip" )

} )
public class Task implements Serializable {
  private static final long        serialVersionUID = 1L;
  
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int                      id;
  
  private int                      approved;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "ASSING_DATE" )
  private Date                     assingDate;
  
  private String                   code;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date                     creationDate;
  
  @Column ( name = "CREATION_USER" )
  private String                   creationUser;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "DATE_END" )
  private Date                     dateEnd;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "DATE_INIT" )
  private Date                     dateInit;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "DATE_INIT_CURRENT" )
  private Date                     dateInitCurrent;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "DELIVERY_DATE" )
  private Date                     deliveryDate;
  
  private String                   description;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "MODIFICATION_DATE" )
  private Date                     modificationDate;
  
  @Column ( name = "MODIFICATION_USER" )
  private String                   modificationUser;
  
  private String                   observation;
  
  @Column ( name = "OBSERVATION_REQUESTOR" )
  private String                   observationRequestor;
  
  private double                   percentage;
  
  private String                   requestor;
  
  private String                   suspended;
  
  @Column ( name = "USER_FUNCTIONAL" )
  private String                   userFunctional;
  
  @Column ( name = "V_DURATION" )
  private int                      vDuration;
  
  @Column ( name = "V_LENGTH" )
  private String                   vLength;
  
  @Column ( name = "V_ORDER" )
  private int                      vOrder;
  
  //bi-directional many-to-one association to Attach
  @OneToMany ( mappedBy = "task" )
  private List< Attach >           attaches;
  
  //bi-directional many-to-one association to Area
  @ManyToOne
  @JoinColumn ( name = "ID_AREA" )
  private Area                     area;
  
  //bi-directional many-to-one association to NotificationTask
  @OneToMany ( mappedBy = "task" )
  private List< NotificationTask > notificationTasks;
  
  //bi-directional many-to-one association to PersonGroup
  @ManyToOne
  @JoinColumn ( name = "ID_GROUP_PERSON" )
  private PersonGroup              personGroup;
  
  //bi-directional many-to-one association to Priority
  @ManyToOne
  @JoinColumn ( name = "ID_PRIORITY" )
  private Priority                 priority;
  
  //bi-directional many-to-one association to Status
  @ManyToOne
  @JoinColumn ( name = "ID_STATUS" )
  private Status                   status;
  
  //bi-directional many-to-one association to TypeTask
  @ManyToOne
  @JoinColumn ( name = "ID_TYPE_TASK" )
  private TaskType                 taskType;
  
  //bi-directional many-to-one association to TraceTask
  @OneToMany ( mappedBy = "task" )
  private List< TraceTask >        traceTasks;
  
  public Task () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public int getApproved () {
    return this.approved;
  }
  
  public void setApproved ( int approved ) {
    this.approved = approved;
  }
  
  public Date getAssingDate () {
    return this.assingDate;
  }
  
  public void setAssingDate ( Date assingDate ) {
    this.assingDate = assingDate;
  }
  
  public String getCode () {
    return this.code;
  }
  
  public void setCode ( String code ) {
    this.code = code;
  }
  
  public Date getCreationDate () {
    return this.creationDate;
  }
  
  public void setCreationDate ( Date creationDate ) {
    this.creationDate = creationDate;
  }
  
  public String getCreationUser () {
    return this.creationUser;
  }
  
  public void setCreationUser ( String creationUser ) {
    this.creationUser = creationUser;
  }
  
  public Date getDateEnd () {
    return this.dateEnd;
  }
  
  public void setDateEnd ( Date dateEnd ) {
    this.dateEnd = dateEnd;
  }
  
  public Date getDateInit () {
    return this.dateInit;
  }
  
  public void setDateInit ( Date dateInit ) {
    this.dateInit = dateInit;
  }
  
  public Date getDateInitCurrent () {
    return this.dateInitCurrent;
  }
  
  public void setDateInitCurrent ( Date dateInitCurrent ) {
    this.dateInitCurrent = dateInitCurrent;
  }
  
  public Date getDeliveryDate () {
    return this.deliveryDate;
  }
  
  public void setDeliveryDate ( Date deliveryDate ) {
    this.deliveryDate = deliveryDate;
  }
  
  public String getDescription () {
    return this.description;
  }
  
  public void setDescription ( String description ) {
    this.description = description;
  }
  
  public Date getModificationDate () {
    return this.modificationDate;
  }
  
  public void setModificationDate ( Date modificationDate ) {
    this.modificationDate = modificationDate;
  }
  
  public String getModificationUser () {
    return this.modificationUser;
  }
  
  public void setModificationUser ( String modificationUser ) {
    this.modificationUser = modificationUser;
  }
  
  public String getObservation () {
    return this.observation;
  }
  
  public void setObservation ( String observation ) {
    this.observation = observation;
  }
  
  public String getObservationRequestor () {
    return this.observationRequestor;
  }
  
  public void setObservationRequestor ( String observationRequestor ) {
    this.observationRequestor = observationRequestor;
  }
  
  public double getPercentage () {
    return this.percentage;
  }
  
  public void setPercentage ( double percentage ) {
    this.percentage = percentage;
  }
  
  public String getRequestor () {
    return this.requestor;
  }
  
  public void setRequestor ( String requestor ) {
    this.requestor = requestor;
  }
  
  public String getSuspended () {
    return this.suspended;
  }
  
  public void setSuspended ( String suspended ) {
    this.suspended = suspended;
  }
  
  public String getUserFunctional () {
    return this.userFunctional;
  }
  
  public void setUserFunctional ( String userFunctional ) {
    this.userFunctional = userFunctional;
  }
  
  public int getVDuration () {
    return this.vDuration;
  }
  
  public void setVDuration ( int vDuration ) {
    this.vDuration = vDuration;
  }
  
  public String getVLength () {
    return this.vLength;
  }
  
  public void setVLength ( String vLength ) {
    this.vLength = vLength;
  }
  
  public int getVOrder () {
    return this.vOrder;
  }
  
  public void setVOrder ( int vOrder ) {
    this.vOrder = vOrder;
  }
  
  public List< Attach > getAttaches () {
    return this.attaches;
  }
  
  public void setAttaches ( List< Attach > attaches ) {
    this.attaches = attaches;
  }
  
  public Attach addAttach ( Attach attach ) {
    getAttaches().add( attach );
    attach.setTask( this );
    
    return attach;
  }
  
  public Attach removeAttach ( Attach attach ) {
    getAttaches().remove( attach );
    attach.setTask( null );
    
    return attach;
  }
  
  public Area getArea () {
    return this.area;
  }
  
  public void setArea ( Area area ) {
    this.area = area;
  }
  
  public List< NotificationTask > getNotificationTasks () {
    return notificationTasks;
  }
  
  public void setNotificationTasks ( List< NotificationTask > notificationTasks ) {
    this.notificationTasks = notificationTasks;
  }
  
  public PersonGroup getPersonGroup () {
    return this.personGroup;
  }
  
  public void setPersonGroup ( PersonGroup personGroup ) {
    this.personGroup = personGroup;
  }
  
  public Priority getPriority () {
    return this.priority;
  }
  
  public void setPriority ( Priority priority ) {
    this.priority = priority;
  }
  
  public Status getStatus () {
    return this.status;
  }
  
  public void setStatus ( Status status ) {
    this.status = status;
  }
  
  public TaskType getTaskType () {
    return taskType;
  }
  
  public void setTaskType ( TaskType taskType ) {
    this.taskType = taskType;
  }
  
  public List< TraceTask > getTraceTasks () {
    return this.traceTasks;
  }
  
  public void setTraceTasks ( List< TraceTask > traceTasks ) {
    this.traceTasks = traceTasks;
  }
  
  public TraceTask addTraceTask ( TraceTask traceTask ) {
    getTraceTasks().add( traceTask );
    traceTask.setTask( this );
    
    return traceTask;
  }
  
  public TraceTask removeTraceTask ( TraceTask traceTask ) {
    getTraceTasks().remove( traceTask );
    traceTask.setTask( null );
    
    return traceTask;
  }
  
}