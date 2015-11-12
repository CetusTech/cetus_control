package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the trace_task database table.
 * 
 */
public class TraceTaskDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private Date              assingDate;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private int               idPerson;
  
  private int               idStatus;
  
  private String            note;
  
  private TaskDTO           task;
  
  public TraceTaskDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public Date getAssingDate () {
    return this.assingDate;
  }
  
  public void setAssingDate ( Date assingDate ) {
    this.assingDate = assingDate;
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
  
  public int getIdPerson () {
    return this.idPerson;
  }
  
  public void setIdPerson ( int idPerson ) {
    this.idPerson = idPerson;
  }
  
  public int getIdStatus () {
    return this.idStatus;
  }
  
  public void setIdStatus ( int idStatus ) {
    this.idStatus = idStatus;
  }
  
  public String getNote () {
    return this.note;
  }
  
  public void setNote ( String note ) {
    this.note = note;
  }
  
  public TaskDTO getTask () {
    return this.task;
  }
  
  public void setTask ( TaskDTO task ) {
    this.task = task;
  }
  
}