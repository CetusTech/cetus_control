package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the notification_general database table.
 * 
 */

@Entity
@Table ( name = "NOTIFICATION_GENERAL" )
@NamedQueries ( {
                  @NamedQuery ( name = "NotificationGeneral.findAll", query = "SELECT n FROM NotificationGeneral n" ),
                  @NamedQuery ( name = "NotificationGeneral.findByClientCetus", query = "SELECT n FROM NotificationGeneral n "
                      + "where (n.clientCetus.id = :idClientDefault AND n.isDefault = '1') OR n.clientCetus.id = :idClientCetus ORDER BY n.id" )
})
public class NotificationGeneral implements Serializable {
  private static final long           serialVersionUID = 1L;
                                                       
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int                         id;
                                      
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date                        creationDate;
                                      
  private String                      description;
                                      
  @ManyToOne
  @JoinColumn ( name = "ID_CLIENT_CETUS" )
  private ClientCetus                 clientCetus;
                                      
  private boolean                     mandatory;
                                      
  private String                      name;
                                      
  @Column ( name = "TABLE_COLUMN" )
  private String                      tableColumn;
                                      
  @Column ( name = "TABLE_NAME" )
  private String                      tableName;
                                      
  @Column ( name = "TEMPLATE_NAME" )
  private String                      templateName;
                                      
  @Column ( name = "IS_DEFAULT" )
  private boolean                     isDefault;
                                      
  //bi-directional many-to-one association to NotificationSetting
  @OneToMany ( mappedBy = "notificationGeneral" )
  private List< NotificationSetting > notificationSettings;
                                      
  public NotificationGeneral () {
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
  
  public ClientCetus getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetus clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
  public boolean isMandatory () {
    return mandatory;
  }
  
  public void setMandatory ( boolean mandatory ) {
    this.mandatory = mandatory;
  }
  
  public boolean isDefault () {
    return isDefault;
  }
  
  public void setDefault ( boolean isDefault ) {
    this.isDefault = isDefault;
  }
  
  public String getName () {
    return this.name;
  }
  
  public void setName ( String name ) {
    this.name = name;
  }
  
  public String getTableColumn () {
    return this.tableColumn;
  }
  
  public void setTableColumn ( String tableColumn ) {
    this.tableColumn = tableColumn;
  }
  
  public String getTableName () {
    return this.tableName;
  }
  
  public void setTableName ( String tableName ) {
    this.tableName = tableName;
  }
  
  public String getTemplateName () {
    return this.templateName;
  }
  
  public void setTemplateName ( String templateName ) {
    this.templateName = templateName;
  }
  
  public List< NotificationSetting > getNotificationSettings () {
    return this.notificationSettings;
  }
  
  public void setNotificationSettings ( List< NotificationSetting > notificationSettings ) {
    this.notificationSettings = notificationSettings;
  }
  
  public NotificationSetting addNotificationSetting ( NotificationSetting notificationSetting ) {
    getNotificationSettings().add( notificationSetting );
    notificationSetting.setNotificationGeneral( this );
    
    return notificationSetting;
  }
  
  public NotificationSetting removeNotificationSetting ( NotificationSetting notificationSetting ) {
    getNotificationSettings().remove( notificationSetting );
    notificationSetting.setNotificationGeneral( null );
    
    return notificationSetting;
  }
  
}