package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the notification_send_mail database table.
 * 
 */
public class NotificationSendMailDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private String            process;
  
  private Date              processDate;
  
  private NotificationDTO   notification;
  
  public NotificationSendMailDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public String getProcess () {
    return this.process;
  }
  
  public void setProcess ( String process ) {
    this.process = process;
  }
  
  public Date getProcessDate () {
    return this.processDate;
  }
  
  public void setProcessDate ( Date processDate ) {
    this.processDate = processDate;
  }
  
  public NotificationDTO getNotification () {
    return this.notification;
  }
  
  public void setNotification ( NotificationDTO notification ) {
    this.notification = notification;
  }
  
}