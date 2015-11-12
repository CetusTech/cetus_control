package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the notification_table database table.
 * 
 */
@Entity
@Table(name="NOTIFICATION_TABLE")
@NamedQueries ( {
                  @NamedQuery(name="NotificationTable.findAll", query="SELECT n FROM NotificationTable n"),
                  @NamedQuery(name="NotificationTable.findAllDto", query=""
                      + " SELECT NEW co.com.cetus.cetuscontrol.dto.NotificationTableDTO( nt.id, nt.description, nt.tableName, nt.tableColum, nt.system, nt.creationDate )"
                      + " FROM NotificationTable nt "),
                  @NamedQuery(name="NotificationTable.findAllDtoNoSystem", query=""
                          + " SELECT NEW co.com.cetus.cetuscontrol.dto.NotificationTableDTO( nt.id, nt.description, nt.tableName, nt.tableColum, nt.system, nt.creationDate )"
                          + " FROM NotificationTable nt WHERE nt.system = '0' "),    
                  @NamedQuery(name="NotificationTable.findByClientNotification", query=" "
                      + " SELECT NEW co.com.cetus.cetuscontrol.dto.NotificationTableDTO( nt.id, nt.description, nt.tableName, nt.tableColum, nt.system, nt.creationDate)"
                      + " FROM NotificationTable nt "
                      + " JOIN nt.notifications n"
                      + " WHERE nt.id = n.notificationTable.id AND n.clientCetus.id =:idClientCetus")
} )
public class NotificationTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	private String description;

	private boolean system;

	@Column(name="TABLE_COLUM")
	private String tableColum;

	@Column(name="TABLE_NAME")
	private String tableName;

	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="notificationTable")
	private List<Notification> notifications;

	public NotificationTable() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSystem () {
    return system;
  }

  public void setSystem ( boolean system ) {
    this.system = system;
  }

  public String getTableColum() {
		return this.tableColum;
	}

	public void setTableColum(String tableColum) {
		this.tableColum = tableColum;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public Notification addNotification(Notification notification) {
		getNotifications().add(notification);
		notification.setNotificationTable(this);

		return notification;
	}

	public Notification removeNotification(Notification notification) {
		getNotifications().remove(notification);
		notification.setNotificationTable(null);

		return notification;
	}

}