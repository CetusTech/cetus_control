package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the notification_send_mail database table.
 * 
 */
@Entity
@Table ( name = "NOTIFICATION_SEND_MAIL" )
@NamedQuery ( name = "NotificationSendMail.findAllToProcess", query = "SELECT n FROM NotificationSendMail n WHERE n.processed = 1 " )
public class NotificationSendMail implements Serializable {
  private static final long serialVersionUID = 1L;
                                             
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
                            
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATE_DATE" )
  private Date              createDate;
                            
  @Column ( name = "CREATE_USER" )
  private String            createUser;
                            
  private String            emails;
                            
  @Column ( name = "ID_NOTIFICATION_SETTING" )
  private int               idNotificationSetting;
                            
  private String            parameters;
                            
  private boolean           processed;
                            
  @Column ( name = "TEMPLATE_NAME" )
  private String            templateName;
                            
  @Column ( name = "SUBJECT_EMAIL" )
  private String            subjectEmail;
                            
  public NotificationSendMail () {
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
  
  public int getIdNotificationSetting () {
    return this.idNotificationSetting;
  }
  
  public void setIdNotificationSetting ( int idNotificationSetting ) {
    this.idNotificationSetting = idNotificationSetting;
  }
  
  public String getParameters () {
    return this.parameters;
  }
  
  public void setParameters ( String parameters ) {
    this.parameters = parameters;
  }
  
  public boolean isProcessed () {
    return processed;
  }
  
  public void setProcessed ( boolean processed ) {
    this.processed = processed;
  }
  
  public String getTemplateName () {
    return this.templateName;
  }
  
  public void setTemplateName ( String templateName ) {
    this.templateName = templateName;
  }
  
  public String getSubjectEmail () {
    return subjectEmail;
  }
  
  public void setSubjectEmail ( String subjectEmail ) {
    this.subjectEmail = subjectEmail;
  }
  
}