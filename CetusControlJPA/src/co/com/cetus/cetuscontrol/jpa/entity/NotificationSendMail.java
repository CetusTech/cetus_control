package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the notification_send_mail database table.
 * 
 */
@Entity
@Table(name="NOTIFICATION_SEND_MAIL")
@NamedQuery(name="NotificationSendMail.findAll", query="SELECT n FROM NotificationSendMail n")
public class NotificationSendMail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String process;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PROCESS_DATE")
	private Date processDate;

	//bi-directional many-to-one association to Notification
	@ManyToOne
	@JoinColumn(name="ID_NOTIFICATION")
	private Notification notification;

	public NotificationSendMail() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProcess() {
		return this.process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Date getProcessDate() {
		return this.processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public Notification getNotification() {
		return this.notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}