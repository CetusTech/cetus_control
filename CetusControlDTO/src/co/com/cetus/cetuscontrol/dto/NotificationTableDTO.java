package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the notification_table database table.
 * 
 */
public class NotificationTableDTO implements Serializable {
  private static final long       serialVersionUID = 1L;
  
  private int                     id;
  
  private Date                    creationDate;
  
  private String                  description;
  
  private boolean                 system;
  
  private String                  tableColum;
  
  private String                  tableName;
  
  private List< NotificationDTO > notifications;
  
  public NotificationTableDTO () {
  }
  
  public NotificationTableDTO ( int id, String description, String tableName, String tableColum, boolean system, Date creationDate ) {
    this.id = id;
    this.description = description;
    this.tableName = tableName;
    this.tableColum = tableColum;
    this.system = system ;
    this.creationDate = creationDate;
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
  
  public String getDescription () {
    return this.description;
  }
  
  public void setDescription ( String description ) {
    this.description = description;
  }
    
  public boolean isSystem () {
    return system;
  }

  public void setSystem ( boolean system ) {
    this.system = system;
  }

  public String getTableColum () {
    return this.tableColum;
  }
  
  public void setTableColum ( String tableColum ) {
    this.tableColum = tableColum;
  }
  
  public String getTableName () {
    return this.tableName;
  }
  
  public void setTableName ( String tableName ) {
    this.tableName = tableName;
  }
  
  public List< NotificationDTO > getNotifications () {
    return this.notifications;
  }
  
  public void setNotifications ( List< NotificationDTO > notifications ) {
    this.notifications = notifications;
  }
  
  public NotificationDTO addNotification ( NotificationDTO notification ) {
    getNotifications().add( notification );
    notification.setNotificationTable( this );
    
    return notification;
  }
  
  public NotificationDTO removeNotification ( NotificationDTO notification ) {
    getNotifications().remove( notification );
    notification.setNotificationTable( null );
    
    return notification;
  }
  
}