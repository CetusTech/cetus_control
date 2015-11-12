package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;

import java.util.Date;

/**
 * The persistent class for the notification_task database table.
 * 
 */
public class NotificationTaskDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private String            event;
  private Date              modificationDate;
  
  private String            modificationUser;
  
  private int               sent;
  
  private TaskDTO           task;
  
  public NotificationTaskDTO () {
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
  
  public String getEvent () {
    return this.event;
  }
  
  public void setEvent ( String event ) {
    this.event = event;
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
  
  public int getSent () {
    return this.sent;
  }
  
  public void setSent ( int sent ) {
    this.sent = sent;
  }
  
  public TaskDTO getTask () {
    return this.task;
  }
  
  public void setTask ( TaskDTO task ) {
    this.task = task;
  }
  
}