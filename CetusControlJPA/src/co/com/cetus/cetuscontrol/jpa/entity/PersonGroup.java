package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the person_group database table.
 * 
 */
@Entity
@Table ( name = "PERSON_GROUP" )
@NamedQueries ( {
                 @NamedQuery ( name = "PersonGroup.findAll", query = "SELECT p FROM PersonGroup p" ),
                 @NamedQuery ( name = "PersonGroup.findPersonGroupByPerson", query = "SELECT pg  FROM PersonGroup pg, Person p, GroupT g WHERE g.id =pg.groupT.id and p.id = pg.person.id and p.id =:idPerson order by p.names, p.lastNames" ),
                 @NamedQuery ( name = "PersonGroup.findPersonByGroup", query = "SELECT p FROM PersonGroup p WHERE p.groupT.id = :idGroup order by p.person.names, p.person.lastNames" ),
                 @NamedQuery ( name = "PersonGroup.findPersonGroup", query = "SELECT p FROM PersonGroup p WHERE p.groupT.id = :idGroup and p.person.identity = :personIdentity" )
} )

public class PersonGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	//bi-directional many-to-one association to GroupT
	@ManyToOne
	@JoinColumn(name="ID_GROUP")
	private GroupT groupT;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="ID_PERSON")
	private Person person;

	//bi-directional many-to-one association to Task
	@OneToMany(mappedBy="personGroup")
	private List<Task> tasks;

	public PersonGroup() {
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

	public GroupT getGroupT() {
		return this.groupT;
	}

	public void setGroupT(GroupT groupT) {
		this.groupT = groupT;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setPersonGroup(this);

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setPersonGroup(null);

		return task;
	}

}