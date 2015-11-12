package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the notification database table.
 * 
 */
@Entity
@NamedQueries ( {
                 @NamedQuery ( name = "Notification.findAll", query = "SELECT n FROM Notification n" ),
                 @NamedQuery ( name = "Notification.findAllByClientCetus", query = ""
                                                                                   + " SELECT NEW co.com.cetus.cetuscontrol.dto.NotificationDTO (n.id, n.clientCetus.id, n.notificationTable.id, n.description, n.mails, n.createAction, "
                                                                                   + " n.createActionMessage, n.updateAction, n.updateActionMessage, n.deleteAction, n.deleteActionMessage, n.active, n.ownerNotify, n.createDate, "
                                                                                   + " n.createUser, n.modificationDate, n.modificationUser, nt.id, nt.description, nt.system ) FROM Notification n"
                                                                                   + " JOIN n.notificationTable nt"
                                                                                   + " WHERE n.clientCetus.id =:idClientCetus" )
} )
public class Notification implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
  private boolean           active;
  
  @Column ( name = "CREATE_ACTION" )
  private boolean           createAction;
  
  @Column ( name = "CREATE_ACTION_MESSAGE" )
  private String            createActionMessage;
  
  @Temporal ( TemporalType.DATE )
  @Column ( name = "CREATE_DATE" )
  private Date              createDate;
  
  @Column ( name = "CREATE_USER" )
  private String            createUser;
  
  @Column ( name = "DELETE_ACTION" )
  private boolean           deleteAction;
  
  @Column ( name = "DELETE_ACTION_MESSAGE" )
  private String            deleteActionMessage;
  
  private String            description;
  
  //bi-directional many-to-one association to ClientCetus
  @ManyToOne
  @JoinColumn ( name = "ID_CLIENT_CETUS" )
  private ClientCetus       clientCetus;
  
  private String            mails;
  
  @Temporal ( TemporalType.DATE )
  @Column ( name = "MODIFICATION_DATE" )
  private Date              modificationDate;
  
  @Column ( name = "MODIFICATION_USER" )
  private String            modificationUser;
  
  @Column ( name = "OWNER_NOTIFY" )
  private boolean           ownerNotify;
  
  @Column ( name = "UPDATE_ACTION" )
  private boolean           updateAction;
  
  @Column ( name = "UPDATE_ACTION_MESSAGE" )
  private String            updateActionMessage;
  
  //bi-directional many-to-one association to NotificationTable
  @ManyToOne
  @JoinColumn ( name = "ID_NOTIFICATION_TABLE" )
  private NotificationTable notificationTable;
  
  public Notification () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public String getCreateActionMessage () {
    return this.createActionMessage;
  }
  
  public void setCreateActionMessage ( String createActionMessage ) {
    this.createActionMessage = createActionMessage;
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
  
  public String getDeleteActionMessage () {
    return this.deleteActionMessage;
  }
  
  public void setDeleteActionMessage ( String deleteActionMessage ) {
    this.deleteActionMessage = deleteActionMessage;
  }
  
  public String getDescription () {
    return this.description;
  }
  
  public void setDescription ( String description ) {
    this.description = description;
  }
  
  public String getMails () {
    return this.mails;
  }
  
  public void setMails ( String mails ) {
    this.mails = mails;
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
  
  public String getUpdateActionMessage () {
    return this.updateActionMessage;
  }
  
  public void setUpdateActionMessage ( String updateActionMessage ) {
    this.updateActionMessage = updateActionMessage;
  }
  
  public NotificationTable getNotificationTable () {
    return this.notificationTable;
  }
  
  public void setNotificationTable ( NotificationTable notificationTable ) {
    this.notificationTable = notificationTable;
  }
  
  public ClientCetus getClientCetus () {
    return clientCetus;
  }
  
  public void setClientCetus ( ClientCetus clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
  public boolean isActive () {
    return active;
  }
  
  public void setActive ( boolean active ) {
    this.active = active;
  }
  
  public boolean isDeleteAction () {
    return deleteAction;
  }
  
  public void setDeleteAction ( boolean deleteAction ) {
    this.deleteAction = deleteAction;
  }
  
  public boolean isUpdateAction () {
    return updateAction;
  }
  
  public void setUpdateAction ( boolean updateAction ) {
    this.updateAction = updateAction;
  }
  
  public boolean isCreateAction () {
    return createAction;
  }
  
  public void setCreateAction ( boolean createAction ) {
    this.createAction = createAction;
  }
  
  public boolean isOwnerNotify () {
    return ownerNotify;
  }
  
  public void setOwnerNotify ( boolean ownerNotify ) {
    this.ownerNotify = ownerNotify;
  }
}