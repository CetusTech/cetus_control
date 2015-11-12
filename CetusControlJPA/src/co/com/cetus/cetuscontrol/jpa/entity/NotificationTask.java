package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the notification_task database table.
 * 
 */
@Entity
@Table ( name = "NOTIFICATION_TASK" )
@NamedQueries ( {
                 @NamedQuery ( name = "NotificationTask.findAll", query = "SELECT n FROM NotificationTask n" ),
                 @NamedQuery ( name = "NotificationTask.findNotificationSent", query = "SELECT n.sent FROM NotificationTask n WHERE n.task.id = :idTask AND n.event = :event" )
} )
public class NotificationTask implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date              creationDate;
  
  @Column ( name = "CREATION_USER" )
  private String            creationUser;
  
  private String            event;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "MODIFICATION_DATE" )
  private Date              modificationDate;
  
  @Column ( name = "MODIFICATION_USER" )
  private String            modificationUser;
  
  private int               sent;
  
  //bi-directional many-to-one association to Task
  @ManyToOne
  @JoinColumn ( name = "ID_TASK" )
  private Task              task;
  
  public NotificationTask () {
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
  
  public Task getTask () {
    return this.task;
  }
  
  public void setTask ( Task task ) {
    this.task = task;
  }
  
}