package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class NotificationSendMailDTO.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlDTO (16/02/2016)
 */
public class NotificationSendMailDTO implements Serializable {
  private static final long serialVersionUID = 1L;
                                             
  private int               id;
  private Date              createDate;
  private String            createUser;
  private String            emails;
  private int               idNotificationSetting;
  private String            parameters;
  private boolean           processed;
  private String            templateName;
  private String            subjectEmail;
                            
  public NotificationSendMailDTO () {
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
  
  @Override
  public String toString () {
    return "NotificationSendMailDTO [id=" + id + ", createDate=" + createDate + ", createUser=" + createUser + ", emails=" + emails
           + ", idNotificationSetting=" + idNotificationSetting + ", parameters=" + parameters + ", processed=" + processed + ", templateName="
           + templateName + ", subjectEmail=" + subjectEmail + "]";
  }
  
}