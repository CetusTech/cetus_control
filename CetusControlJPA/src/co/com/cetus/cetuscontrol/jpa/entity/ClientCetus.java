package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the client_cetus database table.
 * 
 */
@Entity
@Table ( name = "CLIENT_CETUS" )

@NamedQueries ( {
                  @NamedQuery ( name = "ClientCetus.findAll", query = "SELECT c FROM ClientCetus c" ),
                  @NamedQuery ( name = "ClientCetus.findClientCetusEnabledTimer", query = "SELECT c FROM ClientCetus c, ParameterGeneral pg WHERE c.id = pg.clientCetus.id ORDER BY c.id " )
})
public class ClientCetus implements Serializable {
  private static final long           serialVersionUID = 1L;
                                                       
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int                         id;
                                      
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date                        creationDate;
                                      
  @Column ( name = "CREATION_USER" )
  private String                      creationUser;
                                      
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "END_DATE" )
  private Date                        endDate;
                                      
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "INIT_DATE" )
  private Date                        initDate;
                                      
  private String                      name;
                                      
  @Column ( name = "NUMBER_LICENSE" )
  private int                         numberLicense;
                                      
  //bi-directional many-to-one association to Area
  @OneToMany ( mappedBy = "clientCetus" )
  private List< Area >                areas;
                                      
  //bi-directional many-to-one association to Client
  @OneToMany ( mappedBy = "clientCetus" )
  private List< Client >              clients;
                                      
  //bi-directional many-to-one association to ExceptionWorkday
  @OneToMany ( mappedBy = "clientCetus" )
  private List< ExceptionWorkday >    exceptionWorkdays;
                                      
  //bi-directional many-to-one association to GroupType
  @OneToMany ( mappedBy = "clientCetus" )
  private List< GroupType >           groupTypes;
                                      
  //bi-directional many-to-one association to Notification
  @OneToMany ( mappedBy = "clientCetus" )
  private List< NotificationGeneral > notifications;
                                      
  //bi-directional many-to-one association to ParameterGeneral
  @OneToMany ( mappedBy = "clientCetus" )
  private List< ParameterGeneral >    parameterGenerals;
                                      
  //bi-directional many-to-one association to Status
  @OneToMany ( mappedBy = "clientCetus" )
  private List< Status >              statuses;
                                      
  //bi-directional many-to-one association to TypeTask
  @OneToMany ( mappedBy = "clientCetus" )
  private List< TaskType >            typeTasks;
                                      
  //bi-directional many-to-one association to Workday
  @OneToMany ( mappedBy = "clientCetus" )
  private List< Workday >             workdays;
                                      
  public ClientCetus () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
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
  
  public Date getEndDate () {
    return this.endDate;
  }
  
  public void setEndDate ( Date endDate ) {
    this.endDate = endDate;
  }
  
  public Date getInitDate () {
    return this.initDate;
  }
  
  public void setInitDate ( Date initDate ) {
    this.initDate = initDate;
  }
  
  public String getName () {
    return this.name;
  }
  
  public void setName ( String name ) {
    this.name = name;
  }
  
  public int getNumberLicense () {
    return this.numberLicense;
  }
  
  public void setNumberLicense ( int numberLicense ) {
    this.numberLicense = numberLicense;
  }
  
  public List< Area > getAreas () {
    return this.areas;
  }
  
  public void setAreas ( List< Area > areas ) {
    this.areas = areas;
  }
  
  public Area addArea ( Area area ) {
    getAreas().add( area );
    area.setClientCetus( this );
    
    return area;
  }
  
  public Area removeArea ( Area area ) {
    getAreas().remove( area );
    area.setClientCetus( null );
    
    return area;
  }
  
  public List< Client > getClients () {
    return this.clients;
  }
  
  public void setClients ( List< Client > clients ) {
    this.clients = clients;
  }
  
  public Client addClient ( Client client ) {
    getClients().add( client );
    client.setClientCetus( this );
    
    return client;
  }
  
  public Client removeClient ( Client client ) {
    getClients().remove( client );
    client.setClientCetus( null );
    
    return client;
  }
  
  public List< ExceptionWorkday > getExceptionWorkdays () {
    return this.exceptionWorkdays;
  }
  
  public void setExceptionWorkdays ( List< ExceptionWorkday > exceptionWorkdays ) {
    this.exceptionWorkdays = exceptionWorkdays;
  }
  
  public ExceptionWorkday addExceptionWorkday ( ExceptionWorkday exceptionWorkday ) {
    getExceptionWorkdays().add( exceptionWorkday );
    exceptionWorkday.setClientCetus( this );
    
    return exceptionWorkday;
  }
  
  public ExceptionWorkday removeExceptionWorkday ( ExceptionWorkday exceptionWorkday ) {
    getExceptionWorkdays().remove( exceptionWorkday );
    exceptionWorkday.setClientCetus( null );
    
    return exceptionWorkday;
  }
  
  public List< GroupType > getGroupTypes () {
    return this.groupTypes;
  }
  
  public void setGroupTypes ( List< GroupType > groupTypes ) {
    this.groupTypes = groupTypes;
  }
  
  public GroupType addGroupType ( GroupType groupType ) {
    getGroupTypes().add( groupType );
    groupType.setClientCetus( this );
    
    return groupType;
  }
  
  public GroupType removeGroupType ( GroupType groupType ) {
    getGroupTypes().remove( groupType );
    groupType.setClientCetus( null );
    
    return groupType;
  }
  
  public List< NotificationGeneral > getNotifications () {
    return this.notifications;
  }
  
  public void setNotifications ( List< NotificationGeneral > notifications ) {
    this.notifications = notifications;
  }
  
  public NotificationGeneral addNotification ( NotificationGeneral notification ) {
    getNotifications().add( notification );
    notification.setClientCetus( this );
    
    return notification;
  }
  
  public NotificationGeneral removeNotification ( NotificationGeneral notification ) {
    getNotifications().remove( notification );
    notification.setClientCetus( null );
    
    return notification;
  }
  
  public List< ParameterGeneral > getParameterGenerals () {
    return this.parameterGenerals;
  }
  
  public void setParameterGenerals ( List< ParameterGeneral > parameterGenerals ) {
    this.parameterGenerals = parameterGenerals;
  }
  
  public ParameterGeneral addParameterGeneral ( ParameterGeneral parameterGeneral ) {
    getParameterGenerals().add( parameterGeneral );
    parameterGeneral.setClientCetus( this );
    
    return parameterGeneral;
  }
  
  public ParameterGeneral removeParameterGeneral ( ParameterGeneral parameterGeneral ) {
    getParameterGenerals().remove( parameterGeneral );
    parameterGeneral.setClientCetus( null );
    
    return parameterGeneral;
  }
  
  public List< Status > getStatuses () {
    return this.statuses;
  }
  
  public void setStatuses ( List< Status > statuses ) {
    this.statuses = statuses;
  }
  
  public Status addStatus ( Status status ) {
    getStatuses().add( status );
    status.setClientCetus( this );
    
    return status;
  }
  
  public Status removeStatus ( Status status ) {
    getStatuses().remove( status );
    status.setClientCetus( null );
    
    return status;
  }
  
  public List< TaskType > getTypeTasks () {
    return this.typeTasks;
  }
  
  public void setTypeTasks ( List< TaskType > typeTasks ) {
    this.typeTasks = typeTasks;
  }
  
  public TaskType addTypeTask ( TaskType typeTask ) {
    getTypeTasks().add( typeTask );
    typeTask.setClientCetus( this );
    
    return typeTask;
  }
  
  public TaskType removeTypeTask ( TaskType typeTask ) {
    getTypeTasks().remove( typeTask );
    typeTask.setClientCetus( null );
    
    return typeTask;
  }
  
  public List< Workday > getWorkdays () {
    return this.workdays;
  }
  
  public void setWorkdays ( List< Workday > workdays ) {
    this.workdays = workdays;
  }
  
  public Workday addWorkday ( Workday workday ) {
    getWorkdays().add( workday );
    workday.setClientCetus( this );
    
    return workday;
  }
  
  public Workday removeWorkday ( Workday workday ) {
    getWorkdays().remove( workday );
    workday.setClientCetus( null );
    
    return workday;
  }
  
}