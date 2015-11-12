package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the type_task database table.
 * 
 */
public class TaskTypeDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private String            description;
  
  private Date              modificationDate;
  
  private String            modificationUser;
  
  private List< TaskDTO >   tasks;
  
  private ClientCetusDTO    clientCetus;
  
  public TaskTypeDTO () {
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
  
  public List< TaskDTO > getTasks () {
    return this.tasks;
  }
  
  public void setTasks ( List< TaskDTO > tasks ) {
    this.tasks = tasks;
  }
  
  public TaskDTO addTask ( TaskDTO task ) {
    getTasks().add( task );
    task.setTaskType( this );
    
    return task;
  }
  
  public TaskDTO removeTask ( TaskDTO task ) {
    getTasks().remove( task );
    task.setTaskType( null );
    
    return task;
  }
  
  public ClientCetusDTO getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetusDTO clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
}