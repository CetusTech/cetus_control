package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the notification database table.
 * 
 */
public class NotificationDTO implements Serializable {
  private static final long    serialVersionUID = 1L;
  
  private int                  id;
  
  /** The id client cetus. */
  private ClientCetusDTO       clientCetus;
  
  /** The id notification table. */
  private NotificationTableDTO notificationTable;
  
  /** The description. */
  private String               description;
  
  /** The mails. */
  private String               mails;
  
  /** The create action. */
  private boolean              createAction;
  
  /** The create action message. */
  private String               createActionMessage;
  
  /** The update action. */
  private boolean              updateAction;
  
  /** The update action message. */
  private String               updateActionMessage;
  
  /** The delete action. */
  private boolean              deleteAction;
  
  /** The delete action message. */
  private String               deleteActionMessage;
  
  /** The active. */
  private boolean              active;
  
  /** The owner notify. */
  private boolean              ownerNotify;
  
  /** The create date. */
  private Date                 createDate;
  
  /** The create u ser. */
  private String               createUser;
  
  /** The modification date. */
  private Date                 modificationDate;
  
  /** The modification user. */
  private String               modificationUser;
  
  private int                 idNotTable;
  
  private String               descNotTable;
  
  private boolean              noTableSystem;
  
  /**
   * </p> Instancia un nuevo notification dto. </p>
   *
   * @author eChia - Cetus Technology
   * @since CetusControlDTO (12/04/2015)
   */
  public NotificationDTO () {
  }
  
  public NotificationDTO ( int id, int idClientCetus, int idNotificationTable, String description, String mails, boolean createAction,
                           String createActionMessage, boolean updateAction, String updateActionMessage, boolean deleteAction,
                           String deleteActionMessage, boolean active, boolean ownerNotify, Date createDate, String createUser,
                           Date modificationDate, String modificationUser, int idNotTable, String descNotTable, boolean noTableSystem ) {
    
    this.id = id;
    this.clientCetus = new ClientCetusDTO();
    this.clientCetus.setId( idClientCetus );
    this.notificationTable = new NotificationTableDTO();
    this.notificationTable.setId( idNotificationTable );
    this.description = description;
    this.mails = mails;
    this.createAction = createAction;
    this.createActionMessage = createActionMessage;
    this.updateAction = updateAction;
    this.updateActionMessage = updateActionMessage;
    this.deleteAction = deleteAction;
    this.deleteActionMessage = deleteActionMessage;
    this.active = active;
    this.ownerNotify = ownerNotify;
    this.createDate = createDate;
    this.createUser = createUser;
    this.modificationDate = modificationDate;
    this.modificationUser = modificationUser;
    this.idNotTable = idNotTable;
    this.descNotTable = descNotTable;
    this.noTableSystem = noTableSystem;
    
  }
  
  public int getId () {
    return id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public ClientCetusDTO getClientCetus () {
    return clientCetus;
  }
  
  public void setClientCetus ( ClientCetusDTO clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
  public NotificationTableDTO getNotificationTable () {
    return notificationTable;
  }
  
  public void setNotificationTable ( NotificationTableDTO notificationTable ) {
    this.notificationTable = notificationTable;
  }
  
  public String getDescription () {
    return description;
  }
  
  public void setDescription ( String description ) {
    this.description = description;
  }
  
  public String getMails () {
    return mails;
  }
  
  public void setMails ( String mails ) {
    this.mails = mails;
  }
  
  public String getCreateActionMessage () {
    return createActionMessage;
  }
  
  public void setCreateActionMessage ( String createActionMessage ) {
    this.createActionMessage = createActionMessage;
  }
  
  public String getUpdateActionMessage () {
    return updateActionMessage;
  }
  
  public void setUpdateActionMessage ( String updateActionMessage ) {
    this.updateActionMessage = updateActionMessage;
  }
  
  public String getDeleteActionMessage () {
    return deleteActionMessage;
  }
  
  public void setDeleteActionMessage ( String deleteActionMessage ) {
    this.deleteActionMessage = deleteActionMessage;
  }
  
  public Date getCreateDate () {
    return createDate;
  }
  
  public void setCreateDate ( Date createDate ) {
    this.createDate = createDate;
  }
  
  public String getCreateUser () {
    return createUser;
  }
  
  public void setCreateUser ( String createUser ) {
    this.createUser = createUser;
  }
  
  public Date getModificationDate () {
    return modificationDate;
  }
  
  public void setModificationDate ( Date modificationDate ) {
    this.modificationDate = modificationDate;
  }
  
  public String getModificationUser () {
    return modificationUser;
  }
  
  public void setModificationUser ( String modificationUser ) {
    this.modificationUser = modificationUser;
  }
  
  public boolean isCreateAction () {
    return createAction;
  }
  
  public void setCreateAction ( boolean createAction ) {
    this.createAction = createAction;
  }
  
  public boolean isUpdateAction () {
    return updateAction;
  }
  
  public void setUpdateAction ( boolean updateAction ) {
    this.updateAction = updateAction;
  }
  
  public boolean isDeleteAction () {
    return deleteAction;
  }
  
  public void setDeleteAction ( boolean deleteAction ) {
    this.deleteAction = deleteAction;
  }
  
  public boolean isActive () {
    return active;
  }
  
  public void setActive ( boolean active ) {
    this.active = active;
  }
  
  public boolean isOwnerNotify () {
    return ownerNotify;
  }
  
  public void setOwnerNotify ( boolean ownerNotify ) {
    this.ownerNotify = ownerNotify;
  }
  
  public int getIdNotTable () {
    return idNotTable;
  }
  
  public void setIdNotTable ( int idNotTable ) {
    this.idNotTable = idNotTable;
  }
  
  public String getDescNotTable () {
    return descNotTable;
  }
  
  public void setDescNotTable ( String descNotTable ) {
    this.descNotTable = descNotTable;
  }
  
  public boolean isNoTableSystem () {
    return noTableSystem;
  }
  
  public void setNoTableSystem ( boolean noTableSystem ) {
    this.noTableSystem = noTableSystem;
  }
  
}