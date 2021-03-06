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
 * The persistent class for the type_task database table.
 * 
 */
@Entity
@Table ( name = "TYPE_TASK" )
@NamedQueries ( {
                 @NamedQuery ( name = "TaskType.findAll", query = "SELECT t FROM TaskType t" ),
                 @NamedQuery (
                     name = "TaskType.findTaskType", query = "SELECT t FROM TaskType t WHERE t.clientCetus.id =:idclientCetus ORDER BY t.description ASC" ) } )
public class TaskType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

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

	//bi-directional many-to-one association to Task
	@OneToMany(mappedBy="taskType")
	private List<Task> tasks;

	//bi-directional many-to-one association to ClientCetus
	@ManyToOne
	@JoinColumn(name="ID_CLIENT_CETUS")
	private ClientCetus clientCetus;
	
  
  @OneToMany(mappedBy="taskType")
  private List<AreaTypeTask> areaTypeTask;

	public TaskType() {
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

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setTaskType( this );

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setTaskType(null);

		return task;
	}

	public ClientCetus getClientCetus() {
		return this.clientCetus;
	}

	public void setClientCetus(ClientCetus clientCetus) {
		this.clientCetus = clientCetus;
	}

  public List< AreaTypeTask > getAreaTypeTask () {
    return areaTypeTask;
  }

  public void setAreaTypeTask ( List< AreaTypeTask > areaTypeTask ) {
    this.areaTypeTask = areaTypeTask;
  }

	
	
}