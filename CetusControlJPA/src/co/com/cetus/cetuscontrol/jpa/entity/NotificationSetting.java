package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the notification_settings database table.
 * 
 */
@Entity
@Table ( name = "NOTIFICATION_SETTINGS" )
@NamedQueries ( {
                  @NamedQuery ( name = "NotificationSetting.findAll", query = "SELECT n FROM NotificationSetting n" ),
                  @NamedQuery ( name = "NotificationSetting.findNotificationByGroup", query = "SELECT n FROM NotificationSetting n WHERE n.idGroup = :idGroup" )
})

public class NotificationSetting implements Serializable {
  private static final long   serialVersionUID = 1L;
                                               
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int                 id;
                              
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATE_DATE" )
  private Date                createDate;
                              
  @Column ( name = "CREATE_USER" )
  private String              createUser;
                              
  private String              emails;
                              
  @Column ( name = "ID_GROUP" )
  private int                 idGroup;
                              
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "MODIFICATION_DATE" )
  private Date                modificationDate;
                              
  @Column ( name = "MODIFICATION_USER" )
  private String              modificationUser;
                              
  //bi-directional many-to-one association to NotificationGeneral
  @ManyToOne
  @JoinColumn ( name = "ID_NOTIFICATION_GENERAL" )
  private NotificationGeneral notificationGeneral;
                              
  public NotificationSetting () {
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
  
  public NotificationGeneral getNotificationGeneral () {
    return this.notificationGeneral;
  }
  
  public void setNotificationGeneral ( NotificationGeneral notificationGeneral ) {
    this.notificationGeneral = notificationGeneral;
  }
  
}