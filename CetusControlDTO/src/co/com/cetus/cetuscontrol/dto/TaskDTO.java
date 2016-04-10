package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the task database table.
 * 
 */
public class TaskDTO implements Serializable {
  private static final long           serialVersionUID = 1L;
                                                       
  private int                         id;
                                      
  private int                         approved;
                                      
  private Date                        assingDate;
                                      
  private String                      code;
                                      
  private Date                        creationDate;
                                      
  private String                      creationUser;
                                      
  private Date                        dateEnd;
                                      
  private Date                        dateInit;
                                      
  private Date                        dateInitCurrent;
                                      
  private Date                        deliveryDate;
                                      
  private String                      description;
                                      
  private Date                        modificationDate;
                                      
  private String                      modificationUser;
                                      
  private String                      observation;
                                      
  private String                      observationRequestor;
                                      
  private double                      percentage;
                                      
  private String                      requestor;
                                      
  private String                      suspended;
                                      
  private String                      userFunctional;
                                      
  private int                         vDuration;
                                      
  private String                      vLength;
                                      
  private int                         vOrder;
                                      
  private List< AttachDTO >           attaches;
                                      
  private AreaDTO                     area;
                                      
  private List< NotificationTaskDTO > notificationTasks;
                                      
  private PersonGroupDTO              personGroup;
                                      
  private PriorityDTO                 priority;
                                      
  private StatusDTO                   status;
                                      
  private TaskTypeDTO                 taskType;
                                      
  private List< TraceTaskDTO >        traceTasks;
  
                                      
  public TaskDTO () {
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
  
  public List< AttachDTO > getAttaches () {
    return this.attaches;
  }
  
  public void setAttaches ( List< AttachDTO > attaches ) {
    this.attaches = attaches;
  }
  
  public AttachDTO addAttach ( AttachDTO attach ) {
    getAttaches().add( attach );
    attach.setTask( this );
    
    return attach;
  }
  
  public AttachDTO removeAttach ( AttachDTO attach ) {
    getAttaches().remove( attach );
    attach.setTask( null );
    
    return attach;
  }
  
  public AreaDTO getArea () {
    return this.area;
  }
  
  public void setArea ( AreaDTO area ) {
    this.area = area;
  }
  
  public List< NotificationTaskDTO > getNotificationTasks () {
    return notificationTasks;
  }
  
  public void setNotificationTasks ( List< NotificationTaskDTO > notificationTasks ) {
    this.notificationTasks = notificationTasks;
  }
  
  public PersonGroupDTO getPersonGroup () {
    return this.personGroup;
  }
  
  public void setPersonGroup ( PersonGroupDTO personGroup ) {
    this.personGroup = personGroup;
  }
  
  public PriorityDTO getPriority () {
    return this.priority;
  }
  
  public void setPriority ( PriorityDTO priority ) {
    this.priority = priority;
  }
  
  public StatusDTO getStatus () {
    return this.status;
  }
  
  public String getStatusDes () {
    return this.status.getDescription();
  }
  
  public void setStatus ( StatusDTO status ) {
    this.status = status;
  }
  
  public TaskTypeDTO getTaskType () {
    return taskType;
  }
  
  public void setTaskType ( TaskTypeDTO taskType ) {
    this.taskType = taskType;
  }
  
  public List< TraceTaskDTO > getTraceTasks () {
    return this.traceTasks;
  }
  
  public void setTraceTasks ( List< TraceTaskDTO > traceTasks ) {
    this.traceTasks = traceTasks;
  }
  
  public TraceTaskDTO addTraceTask ( TraceTaskDTO traceTask ) {
    getTraceTasks().add( traceTask );
    traceTask.setTask( this );
    
    return traceTask;
  }
  
  public TraceTaskDTO removeTraceTask ( TraceTaskDTO traceTask ) {
    getTraceTasks().remove( traceTask );
    traceTask.setTask( null );
    
    return traceTask;
  }

  public String getAreaDesc () {
    return area != null ? area.getDescription() : "";
  }
  
  public String getTypeTaskDesc () {
    return taskType != null ? taskType.getDescription() : "";
  }
  
  public String getPriorityDesc () {
    return priority != null ? priority.getDescription() : "";
  }
  
  public String getResponsible () {
    return personGroup != null ? personGroup.getPerson() != null ? personGroup.getPerson().getNames() + " " + personGroup.getPerson().getLastNames() : "" : "";
  }
  
  public String getPercentageDesc () {
    return percentage + " %";
  }
  
  public TaskDTO clone(){
    TaskDTO taskClone = new TaskDTO();
    taskClone.setId( id );
    taskClone.setApproved( approved );
    taskClone.setAssingDate( assingDate );
    taskClone.setCode( code );
    taskClone.setCreationDate( creationDate );
    taskClone.setCreationUser( creationUser );
    taskClone.setDateEnd( dateEnd );
    taskClone.setDateInit( dateInit );
    taskClone.setDateInitCurrent( dateInitCurrent );
    taskClone.setDeliveryDate( deliveryDate );
    taskClone.setDescription( description );
    taskClone.setModificationDate( modificationDate );
    taskClone.setModificationUser( modificationUser );
    taskClone.setObservation( observation );
    taskClone.setObservationRequestor( observationRequestor );
    taskClone.setPercentage( percentage );
    taskClone.setRequestor( observationRequestor );
    taskClone.setSuspended( suspended );
    taskClone.setUserFunctional( userFunctional );
    taskClone.setVDuration( vDuration );
    taskClone.setVLength( vLength );
    taskClone.setVOrder( vOrder );
    taskClone.setArea( area );
    taskClone.setPersonGroup( personGroup );
    taskClone.setPriority( priority );
    taskClone.setStatus( status );
    taskClone.setTaskType( taskType );
    
    return taskClone;
    
  }
}