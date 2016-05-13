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
 * The persistent class for the status database table.
 * 
 */
@Entity
@Table(name="STATUS")
@NamedQueries ( {
  @NamedQuery ( name = "Status.findAll", query = "SELECT s FROM Status s" ),
  @NamedQuery ( name = "Status.findStatusById", query = "SELECT s FROM Status s where s.id = :idStatus" ),
  @NamedQuery (
               name = "Status.findStatus", query = "SELECT s FROM Status s WHERE s.clientCetus.id is null or s.clientCetus.id =:idclientCetus ORDER BY s.description ASC" ) } )
//  @NamedQuery (
//      name = "Status.findStatus", query = "SELECT NEW co.com.cetus.cetuscontrol.dto.StatusDTO("
//                                          + " s.id, s.description,s.color, s.creationDate, s.creationUser, s.clientCetus.id, s.modificationDate, s.modificationUser)"
//                                          + " FROM Status s WHERE s.clientCetus.id is null or s.clientCetus.id =:idclientCetus ORDER BY s.description ASC" ) } )
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String color;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	//bi-directional many-to-one association to ClientCetus
	@ManyToOne
	@JoinColumn(name="ID_CLIENT_CETUS")
	private ClientCetus clientCetus;

	//bi-directional many-to-one association to Task
	@OneToMany(mappedBy="status")
	private List<Task> tasks;

	public Status() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationUser() {
		return this.creationUser;
	}

	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModificationUser() {
		return this.modificationUser;
	}

	public void setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
	}

	public ClientCetus getClientCetus() {
		return this.clientCetus;
	}

	public void setClientCetus(ClientCetus clientCetus) {
		this.clientCetus = clientCetus;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setStatus(this);

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setStatus(null);

		return task;
	}

}