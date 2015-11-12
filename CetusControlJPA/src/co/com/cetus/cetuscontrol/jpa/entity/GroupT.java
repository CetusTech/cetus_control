package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the group_t database table.
 * 
 */
@Entity
@Table ( name = "GROUP_T" )
@NamedQueries ( {
                 @NamedQuery ( name = "GroupT.findAll", query = "SELECT g FROM GroupT g" ),
                 @NamedQuery ( name = "GroupT.findGroupByClient", query = "select g from GroupT g where g.person.client.id = :idClient order by g.name" ),
                 @NamedQuery ( name = "GroupT.findGroupByPerson", query = "select g from GroupT g where g.person.id = :idPerson order by g.name" ),
                 @NamedQuery ( name = "GroupT.findGroupByClientCetus", query = "select g from GroupT g where g.person.client.clientCetus.id = :idClientCetus order by g.name" ) } )
public class GroupT implements Serializable {
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
	@Column(name="END_DATE")
	private Date endDate;

	private int hour;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INIT_DATE")
	private Date initDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	private String name;

	private String status;

	//bi-directional many-to-one association to GroupType
	@ManyToOne
	@JoinColumn(name="ID_GROUP_TYPE")
	private GroupType groupType;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="ID_PERSON_LIDER")
	private Person person;

	//bi-directional many-to-one association to PersonGroup
	@OneToMany(mappedBy="groupT")
	private List<PersonGroup> personGroups;

	public GroupT() {
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

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getHour() {
		return this.hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public Date getInitDate() {
		return this.initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public GroupType getGroupType() {
		return this.groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<PersonGroup> getPersonGroups() {
		return this.personGroups;
	}

	public void setPersonGroups(List<PersonGroup> personGroups) {
		this.personGroups = personGroups;
	}

	public PersonGroup addPersonGroup(PersonGroup personGroup) {
		getPersonGroups().add(personGroup);
		personGroup.setGroupT(this);

		return personGroup;
	}

	public PersonGroup removePersonGroup(PersonGroup personGroup) {
		getPersonGroups().remove(personGroup);
		personGroup.setGroupT(null);

		return personGroup;
	}

}