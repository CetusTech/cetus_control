package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class NotificationSettingDTO.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlDTO (16/02/2016)
 */
public class NotificationSettingDTO implements Serializable {
  private static final long      serialVersionUID = 1L;
                                                  
  private int                    id;
  private Date                   createDate;
  private String                 createUser;
  private String                 emails;
  private int                    idGroup;
  private Date                   modificationDate;
  private String                 modificationUser;
  private NotificationGeneralDTO notificationGeneral;
                                 
  public NotificationSettingDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public Date getCreateDate () {
    return this.createDate;
  }
  
  public void setCreateDate ( Date createDate ) {
    this.createDate = createDate;
  }
  
  public String getCreateUser () {
    return this.createUser;
  }
  
  public void setCreateUser ( String createUser ) {
    this.createUser = createUser;
  }
  
  public String getEmails () {
    return this.emails;
  }
  
  public void setEmails ( String emails ) {
    this.emails = emails;
  }
  
  public int getIdGroup () {
    return this.idGroup;
  }
  
  public void setIdGroup ( int idGroup ) {
    this.idGroup = idGroup;
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
  
  public NotificationGeneralDTO getNotificationGeneral () {
    return this.notificationGeneral;
  }
  
  public void setNotificationGeneral ( NotificationGeneralDTO notificationGeneral ) {
    this.notificationGeneral = notificationGeneral;
  }
  
  @Override
  public String toString () {
    return "NotificationSettingDTO [id=" + id + ", createDate=" + createDate + ", createUser=" + createUser + ", emails=" + emails + ", idGroup="
           + idGroup + ", modificationDate=" + modificationDate + ", modificationUser=" + modificationUser + ", notificationGeneral="
           + notificationGeneral + "]";
  }
  
}