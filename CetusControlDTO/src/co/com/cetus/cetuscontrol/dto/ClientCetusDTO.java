package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the client_cetus database table.
 * 
 */

public class ClientCetusDTO implements Serializable {
  private static final long              serialVersionUID = 1L;
                                                          
  private int                            id;
                                         
  private Date                           creationDate;
                                         
  private String                         creationUser;
                                         
  private Date                           endDate;
                                         
  private Date                           initDate;
                                         
  private String                         name;
                                         
  private int                            numberLicense;
                                         
  private List< AreaDTO >                areas;
                                         
  private List< ClientDTO >              clients;
                                         
  private List< ExceptionWorkdayDTO >    exceptionWorkdays;
                                         
  private List< GroupTypeDTO >           groupTypes;
                                         
  private List< NotificationGeneralDTO > notifications;
                                         
  private List< ParameterGeneralDTO >    parameterGenerals;
                                         
  private List< StatusDTO >              statuses;
                                         
  private List< TaskTypeDTO >            typeTasks;
                                         
  private List< WorkdayDTO >             workdays;
                                         
  public ClientCetusDTO () {
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
  
  public List< AreaDTO > getAreas () {
    return this.areas;
  }
  
  public void setAreas ( List< AreaDTO > areas ) {
    this.areas = areas;
  }
  
  public AreaDTO addArea ( AreaDTO area ) {
    getAreas().add( area );
    area.setClientCetus( this );
    
    return area;
  }
  
  public AreaDTO removeArea ( AreaDTO area ) {
    getAreas().remove( area );
    area.setClientCetus( null );
    
    return area;
  }
  
  public List< ClientDTO > getClients () {
    return this.clients;
  }
  
  public void setClients ( List< ClientDTO > clients ) {
    this.clients = clients;
  }
  
  public ClientDTO addClient ( ClientDTO client ) {
    getClients().add( client );
    client.setClientCetus( this );
    
    return client;
  }
  
  public ClientDTO removeClient ( ClientDTO client ) {
    getClients().remove( client );
    client.setClientCetus( null );
    
    return client;
  }
  
  public List< ExceptionWorkdayDTO > getExceptionWorkdays () {
    return this.exceptionWorkdays;
  }
  
  public void setExceptionWorkdays ( List< ExceptionWorkdayDTO > exceptionWorkdays ) {
    this.exceptionWorkdays = exceptionWorkdays;
  }
  
  public ExceptionWorkdayDTO addExceptionWorkday ( ExceptionWorkdayDTO exceptionWorkday ) {
    getExceptionWorkdays().add( exceptionWorkday );
    exceptionWorkday.setClientCetus( this );
    
    return exceptionWorkday;
  }
  
  public ExceptionWorkdayDTO removeExceptionWorkday ( ExceptionWorkdayDTO exceptionWorkday ) {
    getExceptionWorkdays().remove( exceptionWorkday );
    exceptionWorkday.setClientCetus( null );
    
    return exceptionWorkday;
  }
  
  public List< GroupTypeDTO > getGroupTypes () {
    return this.groupTypes;
  }
  
  public void setGroupTypes ( List< GroupTypeDTO > groupTypes ) {
    this.groupTypes = groupTypes;
  }
  
  public GroupTypeDTO addGroupType ( GroupTypeDTO groupType ) {
    getGroupTypes().add( groupType );
    groupType.setClientCetus( this );
    
    return groupType;
  }
  
  public GroupTypeDTO removeGroupType ( GroupTypeDTO groupType ) {
    getGroupTypes().remove( groupType );
    groupType.setClientCetus( null );
    
    return groupType;
  }
  
  public List< NotificationGeneralDTO > getNotifications () {
    return this.notifications;
  }
  
  public void setNotifications ( List< NotificationGeneralDTO > notifications ) {
    this.notifications = notifications;
  }
  
  public NotificationGeneralDTO addNotification ( NotificationGeneralDTO notification ) {
    getNotifications().add( notification );
    notification.setClientCetus( this );
    
    return notification;
  }
  
  public NotificationGeneralDTO removeNotification ( NotificationGeneralDTO notification ) {
    getNotifications().remove( notification );
    notification.setClientCetus( null );
    
    return notification;
  }
  
  public List< ParameterGeneralDTO > getParameterGenerals () {
    return this.parameterGenerals;
  }
  
  public void setParameterGenerals ( List< ParameterGeneralDTO > parameterGenerals ) {
    this.parameterGenerals = parameterGenerals;
  }
  
  public ParameterGeneralDTO addParameterGeneral ( ParameterGeneralDTO parameterGeneral ) {
    getParameterGenerals().add( parameterGeneral );
    parameterGeneral.setClientCetus( this );
    
    return parameterGeneral;
  }
  
  public ParameterGeneralDTO removeParameterGeneral ( ParameterGeneralDTO parameterGeneral ) {
    getParameterGenerals().remove( parameterGeneral );
    parameterGeneral.setClientCetus( null );
    
    return parameterGeneral;
  }
  
  public List< StatusDTO > getStatuses () {
    return this.statuses;
  }
  
  public void setStatuses ( List< StatusDTO > statuses ) {
    this.statuses = statuses;
  }
  
  public StatusDTO addStatus ( StatusDTO status ) {
    getStatuses().add( status );
    status.setClientCetus( this );
    
    return status;
  }
  
  public StatusDTO removeStatus ( StatusDTO status ) {
    getStatuses().remove( status );
    status.setClientCetus( null );
    
    return status;
  }
  
  public List< TaskTypeDTO > getTypeTasks () {
    return this.typeTasks;
  }
  
  public void setTypeTasks ( List< TaskTypeDTO > typeTasks ) {
    this.typeTasks = typeTasks;
  }
  
  public TaskTypeDTO addTypeTask ( TaskTypeDTO typeTask ) {
    getTypeTasks().add( typeTask );
    typeTask.setClientCetus( this );
    
    return typeTask;
  }
  
  public TaskTypeDTO removeTypeTask ( TaskTypeDTO typeTask ) {
    getTypeTasks().remove( typeTask );
    typeTask.setClientCetus( null );
    
    return typeTask;
  }
  
  public List< WorkdayDTO > getWorkdays () {
    return this.workdays;
  }
  
  public void setWorkdays ( List< WorkdayDTO > workdays ) {
    this.workdays = workdays;
  }
  
  public WorkdayDTO addWorkday ( WorkdayDTO workday ) {
    getWorkdays().add( workday );
    workday.setClientCetus( this );
    
    return workday;
  }
  
  public WorkdayDTO removeWorkday ( WorkdayDTO workday ) {
    getWorkdays().remove( workday );
    workday.setClientCetus( null );
    
    return workday;
  }
  
}