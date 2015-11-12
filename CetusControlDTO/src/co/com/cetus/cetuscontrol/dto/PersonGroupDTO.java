package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the person_group database table.
 * 
 */
public class PersonGroupDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private GroupTDTO         groupT;
  
  private PersonDTO         person;
  
  private List< TaskDTO >   tasks;
  
  public PersonGroupDTO () {
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
  
  public GroupTDTO getGroupT () {
    return this.groupT;
  }
  
  public void setGroupT ( GroupTDTO groupT ) {
    this.groupT = groupT;
  }
  
  public PersonDTO getPerson () {
    return this.person;
  }
  
  public void setPerson ( PersonDTO person ) {
    this.person = person;
  }
  
  public List< TaskDTO > getTasks () {
    return this.tasks;
  }
  
  public void setTasks ( List< TaskDTO > tasks ) {
    this.tasks = tasks;
  }
  
  public TaskDTO addTask ( TaskDTO task ) {
    getTasks().add( task );
    task.setPersonGroup( this );
    
    return task;
  }
  
  public TaskDTO removeTask ( TaskDTO task ) {
    getTasks().remove( task );
    task.setPersonGroup( null );
    
    return task;
  }
  
}